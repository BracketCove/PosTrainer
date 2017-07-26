package com.bracketcove.postrainer.alarmdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.alarmlist.AlarmListActivity;

import javax.inject.Inject;


/**
 * I treat my Fragments as Views
 * Created by Ryan on 08/08/2016.
 */
public class AlarmDetailFragment extends Fragment implements AlarmDetailContract.View {
    private static final String ALARM_TO_BE_EDITED = "ALARM_TO_BE_EDITED";

    @Inject
    AlarmDetailPresenter presenter;

    private EditText alarmTitle;
    private CheckBox vibrateOnly, autoRenew;
    private TimePicker nosePicker;
    private ImageView back, proceed;

    private String alarmId;
    private boolean currentAlarmState;

    public AlarmDetailFragment() {
    }

    public static AlarmDetailFragment newInstance(String alarmId) {
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        Bundle args = new Bundle();
        args.putString(ALARM_TO_BE_EDITED, alarmId);
        fragment.setArguments(args);
        return fragment;
    }

        /*------------------------------- Lifecycle -------------------------------*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.alarmId = getArguments().getString(ALARM_TO_BE_EDITED);

        DaggerAlarmDetailComponent.builder()
                .alarmDetailPresenterModule(new AlarmDetailPresenterModule(this))
                .applicationComponent(
                        ((PostrainerApplication) getActivity().getApplication())
                                .getApplicationComponent()
                )
                .build().inject(this);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_detail, container, false);

        alarmTitle = (EditText) v.findViewById(R.id.edt_alarm_title);
        nosePicker = (TimePicker) v.findViewById(R.id.pck_alarm_time);

        vibrateOnly = (CheckBox) v.findViewById(R.id.chb_vibrate_only);
        autoRenew = (CheckBox) v.findViewById(R.id.chb_renew_automatically);

        back = (ImageButton) v.findViewById(R.id.imb_alarm_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackIconPress();
            }
        });

        proceed = (ImageButton) v.findViewById(R.id.imb_alarm_detail_proceed);
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
        presenter.start();
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
    public Alarm getViewModel() {
        Alarm alarm = new Alarm();

        alarm.setAlarmId(alarmId);
        alarm.setAlarmTitle(alarmTitle.getText().toString());
        alarm.setMinute(getPickerMinute());
        alarm.setHourOfDay(getPickerHour());
        alarm.setActive(false);
        alarm.setRenewAutomatically(getRenewAutomatically());
        alarm.setVibrateOnly(getVibrateOnly());

        return alarm;
    }

    @Override
    public void setAlarmTitle(String title) {
        alarmTitle.setText(title);
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
        return alarmTitle.getText().toString();
    }

    @Override
    public void startAlarmListActivity() {
        //TODO: do I need to null check activity before calling this?

        //Is there an edge case, where Activity may be null, when this
        //method is called?
        Intent i = new Intent(getActivity(), AlarmListActivity.class);
        startActivity(i);
    }

    @Override
    public String getAlarmId() {
        return this.alarmId;
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
    public void makeToast(int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
