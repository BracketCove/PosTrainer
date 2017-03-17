package com.bracketcove.postrainer.reminderdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
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
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import javax.inject.Inject;


/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderDetailFragment extends Fragment implements ReminderDetailContract.View {
    private static final String REMINDER_ITEM = "REMINDER_ITEM";

    @Inject ReminderDetailContract.Presenter presenter;

    private AppCompatEditText reminderTitle;
    private AppCompatCheckBox vibrateOnly, autoRenew;
    private TimePicker nosePicker;
    private ImageView back, proceed;

    private PowerManager.WakeLock wakeLock;


    public ReminderDetailFragment() {
    }

    public static ReminderDetailFragment newInstance() {
        return new ReminderDetailFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
//        if (presenter == null) {
//                DaggerReminderDetailComponent.builder()
//                        .reminderDetailPresenterModule(new ReminderDetailPresenterModule(this))
//                        .reminderRepositoryComponent(
//                                ((PostrainerApplication) getActivity().getApplication())
//                                        .getReminderRepositoryComponent()
//
//                        )
//                       .build();
//            presenter.subscribe();
//        }

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

        back = (ImageButton) v.findViewById(R.id.imb_manage_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        proceed = (ImageButton) v.findViewById(R.id.imb_manage_proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return v;
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
    public void setPresenter(ReminderDetailContract.Presenter presenter) {

    }

    @Override
    public void makeToast(String message) {

    }

    @Override
    public void setAlarmTitle(String title) {
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

    @Override
    public void startReminderListActivity() {
        //TODO: do I need to null check activity before calling this?

        //Is there an edge case, where Activity may be null, when this
        //method is called?
        Intent i = new Intent(getActivity(), ReminderListActivity.class);
        startActivity(i);
    }

    @Override
    public void showDatabaseErrorMessage() {
        Toast.makeText(getActivity(), R.string.error_database_write_failure, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showDatabaseUpdatedMessage() {
        Toast.makeText(getActivity(), R.string.message_database_write_successful, Toast.LENGTH_SHORT)
                .show();
    }


}
