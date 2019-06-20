package com.bracketcove.postrainer.reminderlist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bracketcove.postrainer.R
import com.wiseassblog.common.convertTime
import com.wiseassblog.common.toAlarmState
import com.wiseassblog.domain.domainmodel.Reminder
import kotlinx.android.synthetic.main.item_reminder_widget.view.*

class ReminderListAdapter(var event: MutableLiveData<ReminderListEvent> = MutableLiveData()) :
    ListAdapter<Reminder, ReminderListAdapter.AlarmViewHolder>(ReminderDiffUtilCallback()) {

    internal fun setObserver(observer: Observer<ReminderListEvent>) = event.observeForever(observer)

    override fun onBindViewHolder(holder: ReminderListAdapter.AlarmViewHolder, position: Int) {
        getItem(position).let { alarm ->
            holder.alarmTitle.text = alarm.reminderTitle
            holder.alarmTime.text = convertTime(alarm.hourOfDay, alarm.minute)
            holder.alarmStateLabel.text = alarm.isActive.toAlarmState()
            holder.alarmStateSwitch.setChecked(alarm.isActive)
            holder.alarmIcon.setOnClickListener {
                //Since I'm relying on the Adapter to store state, we must rely on the View's switch to infer if the
                //reminder is active or not
                event.value = ReminderListEvent.OnReminderIconClick(
                    this.getItem(position)
                    .copy(isActive = holder.alarmStateSwitch.isChecked)
                )
            }
            holder.alarmStateSwitch.setOnClickListener {
                event.value = ReminderListEvent.OnReminderToggled(holder.alarmStateSwitch.isChecked, this.getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlarmViewHolder(
            inflater.inflate(R.layout.item_reminder_widget, parent, false)
        )
    }

    class AlarmViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        var alarmTitle: TextView = root.lblReminderTitle
        var alarmTime: TextView = root.lblReminderTime
        var alarmStateLabel: TextView = root.lblReminderActivation
        var alarmIcon: ImageView = root.imClock
        var alarmStateSwitch: Switch = root.swiReminderActivation
    }
}

