package com.bracketcove.postrainer.alarmreceiver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;

import javax.inject.Inject;

/**
 *
 *      SQLBrite and RxJava
 *
 *      SQLBrite is a plugin for RxJava to work with SQLite Database
 * Created by Ryan on 05/03/2017.
 */

public class AlarmFragment extends Fragment implements AlarmContract.View {

    @Inject AlarmContract.Presenter presenter;

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
                //alarmController.onDismissAlarmClick();
            }
        });
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (presenter == null) {
            DaggerAlarmComponent.builder()
                    .alarmPresenterModule(new AlarmPresenterModule(this))
                    .reminderRepositoryComponent(
                            ((PostrainerApplication) getActivity().getApplication())
                                    .getReminderRepositoryComponent()

                    )
                    .build();
            presenter.subscribe();
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setPresenter(AlarmContract.Presenter presenter) {

    }

    @Override
    public void makeToast(String message) {

    }
}
