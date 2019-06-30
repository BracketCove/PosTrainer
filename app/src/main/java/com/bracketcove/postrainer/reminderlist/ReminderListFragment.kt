package com.bracketcove.postrainer.reminderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.R
import com.bracketcove.postrainer.reminderlist.buildlogic.ReminderListInjector
import com.bracketcove.postrainer.showToast
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.android.synthetic.main.fragment_reminder_list.*

/**
 * Created by Ryan on 08/08/2016.
 */
class ReminderListFragment : Fragment(), ReminderListContract.View {
    override fun startMovementsView() {
        if (findNavController().currentDestination?.id == R.id.reminderListFragment) {
            findNavController().navigate(
                ReminderListFragmentDirections.actionReminderListFragmentToMovementListFragment()
            )
        }
    }

    var logic: BaseLogic<ReminderListEvent>? = null
    private lateinit var adapter: ReminderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_reminder_list, container, false)
    }

    override fun onStart() {
        super.onStart()

        logic = ReminderListInjector.provideLogic(
            requireActivity().application,
            this
        )

        fabAlarm.setOnClickListener {
            logic?.handleEvent(ReminderListEvent.OnCreateButtonClick)
        }

        setUpAdapter()
        setUpBottomNav()
        logic?.handleEvent(ReminderListEvent.OnStart)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setUpBottomNav() {
        bottomNavReminders.setupWithNavController(findNavController())
        bottomNavReminders.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.movements -> logic?.handleEvent(ReminderListEvent.OnMovementsClick)
                R.id.settings -> logic?.handleEvent(ReminderListEvent.OnSettingsClick)
            }

            true
        }
    }

    private fun setUpAdapter() {
        adapter = ReminderListAdapter()
        //forward events to logic
        adapter.event.observe(this,
            Observer {
                logic?.handleEvent(it)
            }
        )

        rec_reminder_list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rec_reminder_list.adapter = null
    }


    /*------------------------------- Contract -------------------------------*/

    override fun showMessage(msg: String) = showToast(msg)


    /*
    Context may be risky here during orientation changes. Check for such use cases
    during tests.
     */
    override fun setReminderListData(reminders: List<Reminder>) {
        adapter.submitList(reminders)
        rec_reminder_list.visibility = View.VISIBLE
    }

    override fun setNoReminderListDataFound() {
        rec_reminder_list.visibility = View.INVISIBLE
    }

    override fun startReminderDetailView(reminderId: String) {
        if (findNavController().currentDestination?.id == R.id.reminderListFragment) {
            findNavController().navigate(
                ReminderListFragmentDirections
                    .actionReminderListFragmentToReminderDetailFragment(reminderId)
            )
        }
    }

    override fun startSettingsActivity() {
        if (findNavController().currentDestination?.id == R.id.reminderListFragment) {
            findNavController().navigate(
                ReminderListFragmentDirections.actionReminderListFragmentToSettingsFragment()
            )
        }
    }

}
