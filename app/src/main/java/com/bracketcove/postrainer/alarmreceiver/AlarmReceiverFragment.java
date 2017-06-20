package com.bracketcove.postrainer.alarmreceiver;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.viewmodel.Reminder;

/**
 * Created by Ryan on 05/03/2017.
 */

public class AlarmReceiverFragment extends Fragment implements AlarmReceiverContract.View {

    private static final String REMINDER_ID = "REMINDER_ID";
    private String reminderId;

    AlarmReceiverContract.Presenter presenter;

    public AlarmReceiverFragment() {

    }

    public static AlarmReceiverFragment newInstance(String reminderId) {
        AlarmReceiverFragment fragment = new AlarmReceiverFragment();
        Bundle args = new Bundle();
        args.putString(REMINDER_ID, reminderId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.reminderId = getArguments().getString(REMINDER_ID);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        Button stopAlarm = (Button) v.findViewById(R.id.btn_alarm_dismiss);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAlarmDismissClick();
            }
        });
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setPresenter(AlarmReceiverContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void makeToast(@StringRes int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }


    @Override
    public void onResume() {
        super.onResume();
        /*
                In order to set up the Presenter properly, it must be supplied with the Id of the
                Alarm which just went off.
                 */
        presenter.subscribe();
    }

    @Override
    public String getReminderId() {
        return this.reminderId;
    }

    @Override
    public Reminder getReminderViewModel() {
       return new Reminder(
                this.reminderId,
                getString(R.string.def_reminder_name),
                false,
                true,
                false,
                12,
                30
        );
    }

    @Override
    public void finishActivity() {
        Activity activity = getActivity();

        //null check to avoid cases where Act is destroyed. Not sure if necessary at this point.
        if (activity != null) {
            activity.finish();
        }
    }
}
