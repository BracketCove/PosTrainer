package com.bracketcove.postrainer.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.alarmlist.AlarmListActivity;

import javax.inject.Inject;

/**
 * Created by Ryan on 05/03/2017.
 */

public class SettingsFragment extends Fragment implements SettingsContract.View {

    @Inject
    SettingsPresenter settingsPresenter;

    private ImageButton back;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        DaggerSettingsComponent.builder()
                .settingsPresenterModule(new SettingsPresenterModule(this))
                .applicationComponent(
                        ((PostrainerApplication) getActivity().getApplication())
                                .getApplicationComponent()
                )
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        back = (ImageButton) v.findViewById(R.id.imb_settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.onBackButtonPress();
            }
        });
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void makeToast(@StringRes int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void startAlarmListActivity() {
        startActivity(new Intent(getActivity(), AlarmListActivity.class));
    }
}
