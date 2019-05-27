package com.bracketcove.postrainer.alarmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.R
import com.bracketcove.postrainer.alarmlist.buildlogic.AlarmListInjector
import com.bracketcove.postrainer.showToast
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.android.synthetic.main.fragment_alarm_list.*

/**
 * Created by Ryan on 08/08/2016.
 */
class AlarmListFragment : Fragment(), AlarmListContract.View {

    var logic: BaseLogic<AlarmListEvent>? = null
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alarm_list, container, false)
    }

    override fun onStart() {
        super.onStart()

       logic = AlarmListInjector.provideLogic(
            requireActivity().application,
            this
            )

        fab_alarms.setOnClickListener {
            logic?.handleEvent(AlarmListEvent.OnCreateButtonClick)
        }

        setUpAdapter()
        logic?.handleEvent(AlarmListEvent.OnStart)
    }

    private fun setUpAdapter() {
        adapter = NoteListAdapter()
        //forward events to logic
        adapter.event.observe(this,
            Observer {
                   logic?.handleEvent(it)
            }
        )

        rec_alarm_list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rec_alarm_list.adapter = null
    }


    /*------------------------------- Contract -------------------------------*/

    override fun showMessage(msg: String) = showToast(msg)


    /*
    Context may be risky here during orientation changes. Check for such use cases
    during tests.
     */
    override fun setAlarmListData(alarms: List<Alarm>) {
        adapter.submitList(alarms)
        rec_alarm_list.visibility = View.VISIBLE
    }

    override fun setNoAlarmListDataFound() {
        rec_alarm_list.visibility = View.INVISIBLE
    }

    override fun startAlarmDetailActivity(alarmId: String) {
        findNavController().navigate(
            AlarmListFragmentDirections
                .actionAlarmListFragmentToAlarmDetailFragment(alarmId)
        )
    }

    override fun startSettingsActivity() {
        //TODO("Implement navigation")
    }

}
