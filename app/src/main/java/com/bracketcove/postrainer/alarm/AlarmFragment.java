package com.bracketcove.postrainer.alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bracketcove.postrainer.R;

/**
 *
 *      SQLBrite and RxJava
 *
 *      SQLBrite is a plugin for RxJava to work with SQLite Database
 * Created by Ryan on 05/03/2017.
 */

public class AlarmFragment extends Fragment  {
    private AlarmController alarmController;

    public AlarmFragment() {

    }

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
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
                alarmController.onDismissAlarmClick();
            }
        });
        return v;
    }
}
