package com.bracketcove.postrainer.reminder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bracketcove.postrainer.*
import com.bracketcove.postrainer.reminder.buildlogic.ReminderDetailInjector
import com.google.android.material.snackbar.Snackbar
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.android.synthetic.main.fragment_reminder_detail.*

/**
 * I treat my Fragments as Views
 * Created by Ryan on 08/08/2016.
 */

class ReminderDetailFragment : Fragment(), ReminderDetailContract.View {

    var logic: BaseLogic<ReminderDetailEvent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reminder_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        logic = ReminderDetailInjector.provideLogic(
            requireActivity().application,
            this,
            ReminderDetailFragmentArgs.fromBundle(arguments!!).reminderId
        )

        imbReminderDetailBack.setOnClickListener { logic?.handleEvent(ReminderDetailEvent.OnBackIconPress) }
        imbReminderDetailProceed.setOnClickListener { logic?.handleEvent(ReminderDetailEvent.OnDoneIconPress) }
        imbReminderDetailDelete.setOnClickListener { logic?.handleEvent(ReminderDetailEvent.OnDeleteIconPress) }

        logic?.handleEvent(ReminderDetailEvent.OnStart)
    }


    /* Interface Functions  */


    override fun showDeleteConfirm() {
        if (activity != null) {
            Snackbar.make(rootReminderDetailFragment, MESSAGE_DELETE_CONFIRMATION, Snackbar.LENGTH_LONG)
                .setAction(PROMPT_DELETE) {
                    logic?.handleEvent(ReminderDetailEvent.OnDeleteConfirmed)
                }
                .show()
        }
    }

    //TODO: Decide if VibrateOnly and Renew Automatically are still feasible options

    override fun getState(): Reminder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Reminder(
                null,
                edtReminderTitle.text.toString(),
                false,
                false,
                false,
                pckAlarmTime.hour,
                pckAlarmTime.minute
            )
        } else {
            Reminder(
                null,
                edtReminderTitle.text.toString(),
                false,
                false,
                false,
                pckAlarmTime.currentHour,
                pckAlarmTime.currentMinute
            )
        }
    }

    override fun showMessage(msg: String) = showToast(msg)

    override fun setReminderTitle(title: String) {
        edtReminderTitle.setText(title)
    }


    override fun setVibrateOnly(active: Boolean) {

    }

    override fun setRenewAutomatically(active: Boolean) {

    }

    override fun setPickerTime(hour: Int, minute: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pckAlarmTime!!.hour = hour
            pckAlarmTime!!.minute = minute
        } else {
            pckAlarmTime!!.currentHour = hour
            pckAlarmTime!!.currentMinute = minute
        }
    }

    override fun startReminderListActivity() {
        findNavController().navigate(R.id.reminderListFragment)
    }
}
