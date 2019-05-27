package com.bracketcove.postrainer.alarmlist


import androidx.recyclerview.widget.DiffUtil
import com.wiseassblog.domain.domainmodel.Alarm

class AlarmDiffUtilCallback : DiffUtil.ItemCallback<Alarm>() {
    override fun areItemsTheSame(
        oldItem: Alarm,
        newItem: Alarm
    ): Boolean {
        return oldItem.alarmId == newItem.alarmId
    }

    override fun areContentsTheSame(
        oldItem: Alarm,
        newItem: Alarm
    ): Boolean {
        return oldItem.alarmId == newItem.alarmId
    }
}