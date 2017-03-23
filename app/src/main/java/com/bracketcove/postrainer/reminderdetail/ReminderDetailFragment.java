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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.alarmreceiver.AlarmReceiverPresenterModule;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import javax.inject.Inject;


/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderDetailFragment extends Fragment implements ReminderDetailContract.View {
    private static final String REMINDER_TO_BE_EDITED = "REMINDER_TO_BE_EDITED";

    @Inject
    ReminderDetailPresenter presenter;

    private AppCompatEditText reminderTitle;
    private AppCompatCheckBox vibrateOnly, autoRenew;
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

        this.reminderId = getArguments().getString(REMINDER_TO_BE_EDITED);

        DaggerAlarmReceiverComponent.builder()
                .alarmReceiverPresenterModule(new AlarmReceiverPresenterModule(this))
                .reminderComponent(
                        ((PostrainerApplication) getActivity().getApplication())
                                .getReminderComponent()
                )
                .alarmComponent(((PostrainerApplication) getActivity().getApplication())
                        .getAlarmComponent())
                .build().inject(this);
        
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
     * @param active can be: true (active) or false (inactive
     */
    @Override
    public void setCurrentAlarmState(boolean active) {
        this.currentAlarmState = active;
    }

    @Override
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

    @Override
    public boolean getVibrateOnly() {
        return vibrateOnly.isChecked();
    }

    @Override
    public boolean getRenewAutomatically() {
        return autoRenew.isChecked();
    }

    @Override
    public int getPickerHour() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return nosePicker.getHour();
        } else {
            return nosePicker.getCurrentHour();
        }
    }

    @Override
    public int getPickerMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return nosePicker.getMinute();
        } else {
            return nosePicker.getCurrentMinute();
        }
    }

    @Override
    public boolean getCurrentAlarmState() {
        return currentAlarmState;
    }

    @Override
    public void makeToast(int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

}
