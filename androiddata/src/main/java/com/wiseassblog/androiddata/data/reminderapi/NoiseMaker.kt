package com.wiseassblog.androiddata.data.reminderapi

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

internal object NoiseMaker {

    @Throws(java.io.IOException::class)
    private fun playAlarmSound(context:Context) {
//        val mediaPlayer = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        object : CountDownTimer(30000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//
//            }
//
//            override fun onFinish() {
//                if (mediaPlayer != null) {
//                    mediaPlayer!!.stop()
//                    mediaPlayer!!.release()
//                }
//            }
//        }
//
//        mediaPlayer!!.start()
    }

    private fun vibratePhone(vibe: Vibrator) {
        val vPatternOne = longArrayOf(0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibe!!.vibrate(VibrationEffect.createWaveform(vPatternOne, -1))
        } else {
            vibe!!.vibrate(vPatternOne, -1)
        }
    }

    private fun makeSound(vibe: Vibrator) {
        val vPatternOne = longArrayOf(0, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibe!!.vibrate(VibrationEffect.createWaveform(vPatternOne, -1))
        } else {
            vibe!!.vibrate(vPatternOne, -1)
        }
    }
}