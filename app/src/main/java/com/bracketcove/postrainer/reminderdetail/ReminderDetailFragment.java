package com.bracketcove.postrainer.reminderdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import javax.inject.Inject;


/**
 * I treat my Fragments as Views
 * Created by Ryan on 08/08/2016.
 */
public class ReminderDetailFragment extends Fragment implements ReminderDetailContract.View {
    private static final String REMINDER_TO_BE_EDITED = "REMINDER_TO_BE_EDITED";

    ReminderDetailContract.Presenter presenter;

    private EditText reminderTitle;
    private CheckBox vibrateOnly, autoRenew;
    private TimePicker nosePicker;
    private ImageView back, proceed;

    private String reminderId;
    private boolean currentAlarmState;

    public ReminderDetailFragment() {
    }

    public static ReminderDetailFragment newInstance(String reminderId) {
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        Bundle args = new Bundle();
        args.putString(REMINDER_TO_BE_EDITED, reminderId);
        fragment.setArguments(args);
        return fragment;
    }

        /*------------------------------- Lifecycle -------------------------------*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        /*
         *When you call setRetainInstance(true) on a Fragment, it allows you to
         * preserve Fragment instances during events which might destroy the Activity.
         *
         * Most importantly, Orientation Changes
         */

        this.reminderId = getArguments().getString(REMINDER_TO_BE_EDITED);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_detail, container, false);

        reminderTitle = (AppCompatEditText) v.findViewById(R.id.edt_reminder_title);
        nosePicker = (TimePicker) v.findViewById(R.id.pck_reminder_time);

        vibrateOnly = (AppCompatCheckBox) v.findViewById(R.id.chb_vibrate_only);
        autoRenew = (AppCompatCheckBox) v.findViewById(R.id.chb_renew_automatically);

        back = (ImageButton) v.findViewById(R.id.imb_reminder_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackIconPress();
            }
        });

        proceed = (ImageButton) v.findViewById(R.id.imb_reminder_detail_proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDoneIconPress();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*------------------------------- Contract -------------------------------*/

    @Override
    public Reminder getViewModel() {
        Reminder reminder = new Reminder();

        reminder.setReminderId(reminderId);
        reminder.setReminderTitle(reminderTitle.getText().toString());
        reminder.setMinute(getPickerMinute());
        reminder.setHourOfDay(getPickerHour());
        reminder.setActive(false);
        reminder.setRenewAutomatically(getRenewAutomatically());
        reminder.setVibrateOnly(getVibrateOnly());

        return reminder;
    }

    @Override
    public void setReminderTitle(String title) {
        reminderTitle.setText(title);
    }

    @Override
    public void setVibrateOnly(boolean active) {
        vibrateOnly.setChecked(active);
    }

    @Override
    public void setRenewAutomatically(boolean active) {
        autoRenew.setChecked(active);
    }

    @Override
    public void setPickerTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nosePicker.setHour(hour);
            nosePicker.setMinute(minute);
        } else {
            nosePicker.setCurrentHour(hour);
            nosePicker.setCurrentMinute(minute);
        }
    }

    /**
     * Knowing if the Alarm is Active or not is very important
     *
     * @param active can be: true (active) or false (inactive
     */
    @Override
    public void setCurrentAlarmState(boolean active) {
        this.currentAlarmState = active;
    }

    public String getReminderTitle() {
        return reminderTitle.getText().toString();
    }

    @Override
    public void startReminderListActivity() {
        //TODO: do I need to null check activity before calling this?

        //Is there an edge case, where Activity may be null, when this
        //method is called?
        Intent i = new Intent(getActivity(), ReminderListActivity.class);
        startActivity(i);
    }

    @Override
    public String getReminderId() {
        return this.reminderId;
    }

    public boolean getVibrateOnly() {
        return vibrateOnly.isChecked();
    }

    public boolean getRenewAutomatically() {
        return autoRenew.isChecked();
    }

    public int getPickerHour() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return nosePicker.getHour();
        } else {
            return nosePicker.getCurrentHour();
        }
    }

    public int getPickerMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return nosePicker.getMinute();
        } else {
            return nosePicker.getCurrentMinute();
        }
    }

    public boolean getCurrentAlarmState() {
        return currentAlarmState;
    }

    @Override
    public void setPresenter(ReminderDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void makeToast(int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
