package com.bracketcove.postrainer.alarmlist


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
import com.wiseassblog.domain.domainmodel.Alarm
import kotlinx.android.synthetic.main.item_alarm_widget.view.*

class NoteListAdapter(var event: MutableLiveData<AlarmListEvent> = MutableLiveData()) :
    ListAdapter<Alarm, NoteListAdapter.AlarmViewHolder>(AlarmDiffUtilCallback()) {

    internal fun setObserver(observer: Observer<AlarmListEvent>) = event.observeForever(observer)

    override fun onBindViewHolder(holder: NoteListAdapter.AlarmViewHolder, position: Int) {
        getItem(position).let { alarm ->
            holder.alarmTitle.text = alarm.alarmTitle
            holder.alarmTime.text = convertTime(alarm.hourOfDay, alarm.minute)
            holder.alarmStateLabel.text = alarm.isActive.toAlarmState()
            holder.alarmStateSwitch.setChecked(alarm.isActive)
            holder.alarmIcon.setOnClickListener {
                //Since I'm relying on the Adapter to store state, we must rely on the View's switch to infer if the
                //alarm is active or not
                event.value = AlarmListEvent.OnAlarmIconClick(
                    this.getItem(position)
                    .copy(isActive = holder.alarmStateSwitch.isChecked)
                )
            }
            holder.alarmStateSwitch.setOnClickListener {
                event.value = AlarmListEvent.OnAlarmToggled(holder.alarmStateSwitch.isChecked, this.getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlarmViewHolder(
            inflater.inflate(R.layout.item_alarm_widget, parent, false)
        )
    }

    class AlarmViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        var alarmTitle: TextView = root.lbl_alarm_title
        var alarmTime: TextView = root.lbl_alarm_time
        var alarmStateLabel: TextView = root.lbl_alarm_activation
        var alarmIcon: ImageView = root.im_clock
        var alarmStateSwitch: Switch = root.swi_alarm_activation
    }
}

