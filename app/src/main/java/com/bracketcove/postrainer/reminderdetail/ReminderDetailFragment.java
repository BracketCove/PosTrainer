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

import com.bracketcove.postrainer.ReminderInjection;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.SchedulerInjection;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;

import javax.inject.Inject;

import dagger.Provides;


/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderDetailFragment extends Fragment implements ReminderDetailContract.View {
    private static final String REMINDER_ITEM = "REMINDER_ITEM";


    /*
     * 1. Is it important that this Presenter is spoken to via the Interface?
     * 2. If #1 is the case, how can I make it so that The Injected class below is an Interface and
     * not a Concrete Class.
     */
    @Inject ReminderDetailPresenter presenter;

    private ReminderDetailContract.Presenter presenter;

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

    /**
     * The difference between my old approach and Dagger 2, is that
     * my old approach still requires the Fragment to create the Presenter.
     * This means that if I change my Presenter's Architecture, I may need to
     * then make the appropriate change in this Fragment
     * @param savedInstanceState
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        if (presenter == null) {
            /*
            presenter = new ReminderDetailPresenter(this,
                    ReminderInjection.provideReminderService(),
                    SchedulerInjection.provideSchedulerProvider()*/


            /*How can I reduce ReminderDetailPresenterModule's constructor to only
             *new ReminderDetailPresenterModule(this)?
             */

            DaggerReminderDetailComponent.builder()
                    .reminderDetailPresenterModule(
                            new ReminderDetailPresenterModule(this,
                                    ReminderInjection.provideReminderService(),
                                    SchedulerInjection.provideSchedulerProvider()
                            )).build()
                    .inject(this);
        }
        presenter.subscribe();
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
