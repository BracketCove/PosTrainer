package com.bracketcove.postrainer.alarmreceiver;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bracketcove.postrainer.R;

import javax.inject.Inject;

/**
 *
 *      SQLBrite and RxJava
 *
 *      SQLBrite is a plugin for RxJava to work with SQLite Database
 * Created by Ryan on 05/03/2017.
 */

public class AlarmReceiverFragment extends Fragment implements AlarmReceiverContract.View {

    @Inject AlarmReceiverContract.Presenter presenter;

    public AlarmReceiverFragment() {

    }

    public static AlarmReceiverFragment newInstance() {
        return new AlarmReceiverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This is important for Orientation Change handling!!!
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
    public void makeToast(@StringRes int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
