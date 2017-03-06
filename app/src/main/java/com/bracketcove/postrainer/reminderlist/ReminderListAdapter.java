package com.bracketcove.postrainer.reminderlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.database.Reminder;
import com.bracketcove.postrainer.alarm.TimeConverter;

import java.util.Collections;
import java.util.List;

/**
 * Adapter class for Globally used Navigation Drawer.
 * Created by Ryan on 18/09/2015.
 */
public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Reminder> data = Collections.emptyList();
    private OnItemClickListener itemClickListener;

    /**
     * @param context - Context of Activity which contains contains the fragment which manages
     *                this class
     * @param data    - List of NavListItems which contain a title and icon resource id, to be
     *                passed to bound to ViewHolder objects appropriately.
     */
    public ReminderListAdapter(Context context, List<Reminder> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_alarm_widget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderListAdapter.ViewHolder holder, final int position) {
        Reminder item = data.get(position);
        holder.alarmTitle.setText(item.getAlarmTitle());

        holder.alarmTime.setText(
                TimeConverter.convertTime(item.getHourOfDay(),item.getMinute())
        );
        if (item.isActive()){
            holder.alarmStateLabel.setText(R.string.on);
        } else {
            holder.alarmStateLabel.setText(R.string.off);
        }
        holder.alarmStateSwitch.setChecked(item.isActive());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView alarmTitle;
        TextView alarmTime;
        TextView alarmStateLabel;
        ImageView alarmIcon;
         SwitchCompat alarmStateSwitch;
        View alarmActiveContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            alarmTitle = (TextView) itemView.findViewById(R.id.lbl_alarm_title);
            alarmTime = (TextView) itemView.findViewById(R.id.lbl_alarm_time);
            alarmStateLabel = (TextView) itemView.findViewById(R.id.lbl_alarm_activation);
            alarmStateSwitch = (SwitchCompat) itemView.findViewById(R.id.swi_alarm_activation);
            alarmIcon = (ImageView) itemView.findViewById(R.id.im_clock);
            alarmIcon.setOnClickListener(this);
            alarmActiveContainer = (View) itemView.findViewById(R.id.cont_alarm_activation);
            alarmStateSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.swi_alarm_activation){
                itemClickListener.onAlarmSwitchClick(getAdapterPosition());
                if (alarmStateSwitch.isChecked()){
                    alarmStateLabel.setText(R.string.on);
                } else {
                    alarmStateLabel.setText(R.string.off);
                }
            } else if (id == R.id.im_clock){
                itemClickListener.onAlarmIconClick(getAdapterPosition());
            }

        }
    }

    public interface OnItemClickListener {
        void onAlarmIconClick(int position);
        void onAlarmSwitchClick(int position);
    }

    public void setOnClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
