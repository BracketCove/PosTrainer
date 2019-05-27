package com.wiseassblog.androiddata.data.alarmservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.wiseassblog.androiddata.data.ALARM_ID
import com.wiseassblog.androiddata.data.AlarmReceiver
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmService
import java.util.*


class AlarmService(
    private val wakeLock: PowerManager.WakeLock?,
    private var mediaPlayer: MediaPlayer?,
    private val audioManager: AudioManager,
    private val vibe: Vibrator?,
    private val alarmManager: android.app.AlarmManager,
    private val applicationContext: Context
) : IAlarmService {
    override suspend fun setAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> {
        val alarmTime = Calendar.getInstance()
        alarmTime.timeInMillis = System.currentTimeMillis()
        alarmTime.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay)
        alarmTime.set(Calendar.MINUTE, alarm.minute)

        //make sure you aren't setting an alarm for earlier today
        checkAlarm(alarmTime)

        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra(ALARM_ID, alarm.alarmId)

        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            Integer.parseInt(alarm.alarmId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (alarm.isRenewAutomatically) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis,
                AlarmManager.INTERVAL_DAY, alarmIntent
            )
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis,
                    alarmIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis,
                    alarmIntent
                )
            }

        }

        enableReceiver(true)

        return ResultWrapper.build { Unit }
    }

    private fun enableReceiver(enable: Boolean) {
        if (enable) {
            val receiver = ComponentName(applicationContext, AlarmReceiver::class.java)

            applicationContext.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } else {
            val receiver = ComponentName(applicationContext, AlarmReceiver::class.java)

            applicationContext.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    override suspend fun cancelAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra(ALARM_ID, alarm.alarmId)

        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            Integer.parseInt(alarm.alarmId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(alarmIntent)

        enableReceiver(false)

        return ResultWrapper.build { Unit }
    }

    override suspend fun dismissAlarm(): ResultWrapper<Exception, Unit> {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

        vibe?.cancel()

        if (wakeLock != null && wakeLock.isHeld) {
            wakeLock.release()
        }

        return ResultWrapper.build { Unit }
    }

    override suspend fun startAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> {
        Log.d("ALARM", "startAlarm() was called with ${alarm.toString()}")
        if (alarm.isVibrateOnly) {
            vibratePhone()
        } else {
            vibratePhone()
            try {
                playAlarmSound()
            } catch (e: java.io.IOException) {
                e.printStackTrace()
            }

        }

        return ResultWrapper.build { Unit }
    }

    private val alarmUri: String
        get() {
            var alert: String? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
                if (alert == null) {
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString()
                }
            }
            return alert
        }


    private fun checkAlarm(alarm: Calendar) {
        val now = Calendar.getInstance()
        if (alarm.before(now)) {
            val alarmForFollowingDay = alarm.timeInMillis + 86400000L
            alarm.timeInMillis = alarmForFollowingDay
        }
    }

    @Throws(java.io.IOException::class)
    private fun playAlarmSound() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if (mediaPlayer != null) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.release()
                }
            }
        }

        mediaPlayer!!.start()
    }

    private fun vibratePhone() {
        val vPatternOne = longArrayOf(0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibe!!.vibrate(VibrationEffect.createWaveform(vPatternOne, -1))
        } else {
            vibe!!.vibrate(vPatternOne, -1)
        }
    }
}
