package com.bracketcove.postrainer.alarmreceiver

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bracketcove.postrainer.ALARM_ID
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.R
import com.bracketcove.postrainer.alarmreceiver.buildlogic.AlarmReceiverInjector
import kotlinx.android.synthetic.main.activity_alarm_receiver.*


/**
 * This Activity is fired when an Alarm goes off. Once it is active, it handles the Alarm based on
 * the Alarm's configuration (such as Vibrate Only, etc.). It also allows the user to stop
 * an Alarm which is going off.
 * Created by Ryan on 17/04/2016.
 */
class AlarmReceiverActivity : AppCompatActivity(), AlarmReceiverContract.View {

    lateinit var logic: BaseLogic<AlarmReceiverEvent>
    lateinit var alarmId: String

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //apparently flags need to be set before calling setContentView
        this.getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            this.setShowWhenLocked(true)
            this.setTurnScreenOn(true)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }



        setContentView(R.layout.activity_alarm_receiver)

        logic = AlarmReceiverInjector.provideLogic(this)

        alarmId = getIntent().getStringExtra(ALARM_ID)

        Log.d("ALARM", "Activity invoked with extra $alarmId")


        btn_alarm_dismiss.setOnClickListener {
            logic.handleEvent(
                AlarmReceiverEvent.OnDismissed(
                    alarmId
                )
            )
        }
        logic.handleEvent(AlarmReceiverEvent.OnStart(alarmId))

    }
}
