package com.wiseassblog.androiddata.data.alarmservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.wiseassblog.androiddata.data.*
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Alarm
import com.wiseassblog.domain.repository.IAlarmService
import java.util.*

/**
 * Entrypoint and Facade for AlarmService. Has two primary purposes:
 * - Talks to system services responsible for setting alarms (alarmManager) and for indicating to the user that an
 * alarm has fired.
 * - Broadcasts intents and pendingIntents to AlarmReceiver, which consequently tells the Service what's going on.
 */
class AlarmServiceManager(
    private var mediaPlayer: MediaPlayer?,
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

        val intent = Intent(
            applicationContext,
            Class.forName(ACTIVITY_CLASS_NAME)
        )
        intent.putExtra(ALARM_ID, alarm.alarmId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val alarmIntent = PendingIntent.getActivity(
            applicationContext,
            Integer.parseInt(alarm.alarmId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmClockInfo = AlarmManager.AlarmClockInfo(
            alarmTime.timeInMillis,
            alarmIntent
        )

        val receiverIntent = Intent(applicationContext, AlarmReceiver::class.java)
            .setAction(
                ACTION_ALARM_START
            )

        alarmManager.setAlarmClock(
            alarmClockInfo,
            PendingIntent.getBroadcast(
                applicationContext,
                Integer.parseInt(alarm.alarmId),
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        return ResultWrapper.build { Unit }
    }

    override suspend fun cancelAlarm(alarm: Alarm): ResultWrapper<Exception, Unit> {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.setAction(
            ACTION_ALARM_START
        )

        intent.putExtra(ALARM_ID, alarm.alarmId)
        intent.action = ACTION_ALARM_CANCEL

        val alarmIntent = PendingIntent.getActivity(
            applicationContext,
            Integer.parseInt(alarm.alarmId),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(alarmIntent)

        return ResultWrapper.build { Unit }
    }

    override suspend fun dismissAlarm(): ResultWrapper<Exception, Unit> {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

        vibe?.cancel()

        applicationContext.sendBroadcast(
            Intent(applicationContext, Class.forName(RECEIVER_CLASS_NAME))
                .also {
                    it.action = ACTION_ALARM_DISMISS
                }
        )

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
