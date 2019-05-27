package com.bracketcove.postrainer.alarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bracketcove.postrainer.*
import com.bracketcove.postrainer.alarm.buildlogic.AlarmDetailInjector
import com.google.android.material.snackbar.Snackbar
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.android.synthetic.main.fragment_alarm_detail.*

/**
 * I treat my Fragments as Views
 * Created by Ryan on 08/08/2016.
 */

private const val ALARM_TO_BE_EDITED = "ALARM_TO_BE_EDITED"

class AlarmDetailFragment : Fragment(), AlarmDetailContract.View {

    var logic: BaseLogic<AlarmDetailEvent>? = null

    override fun showDeleteConfirm() {
        if (activity != null) {
            Snackbar.make(root_alarm_detail_fragment, MESSAGE_DELETE_CONFIRMATION, Snackbar.LENGTH_LONG)
                .setAction(PROMPT_DELETE) {
                    logic?.handleEvent(AlarmDetailEvent.OnDeleteConfirmed)
                }
                .show()
        }
    }

    override fun getState(): Alarm {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Alarm(
                null,
                edt_alarm_title.text.toString(),
                false,
                chb_vibrate_only.isChecked,
                chb_renew_automatically.isChecked,
                pck_alarm_time.hour,
                pck_alarm_time.minute
            )
        } else {
            Alarm(
                null,
                edt_alarm_title.text.toString(),
                false,
                chb_vibrate_only.isChecked,
                chb_renew_automatically.isChecked,
                pck_alarm_time.currentHour,
                pck_alarm_time.currentMinute
            )
        }
    }

    override fun showMessage(msg: String) = showToast(msg)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_alarm_detail, container, false)


        return v
    }

    override fun onStart() {
        super.onStart()
        logic = AlarmDetailInjector.provideLogic(
            requireActivity().application,
            this,
            AlarmDetailFragmentArgs.fromBundle(arguments!!).alarmId
        )

        imb_alarm_detail_back.setOnClickListener { logic?.handleEvent(AlarmDetailEvent.OnBackIconPress) }
        imb_alarm_detail_proceed.setOnClickListener { logic?.handleEvent(AlarmDetailEvent.OnDoneIconPress) }

        logic?.handleEvent(AlarmDetailEvent.OnStart)
    }

    override fun setAlarmTitle(title: String) {
        edt_alarm_title.setText(title)
    }

    override fun setVibrateOnly(active: Boolean) {
        chb_vibrate_only.isChecked = active
    }

    override fun setRenewAutomatically(active: Boolean) {
        chb_renew_automatically!!.isChecked = active
    }

    override fun setPickerTime(hour: Int, minute: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pck_alarm_time!!.hour = hour
            pck_alarm_time!!.minute = minute
        } else {
            pck_alarm_time!!.currentHour = hour
            pck_alarm_time!!.currentMinute = minute
        }
    }

    override fun startAlarmListActivity() {
        findNavController().navigate(R.id.alarmListFragment)
    }
}
