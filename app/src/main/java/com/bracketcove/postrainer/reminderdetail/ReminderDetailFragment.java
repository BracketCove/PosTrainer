package com.bracketcove.postrainer.reminderdetail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.database.Reminder;

import java.util.Calendar;


/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderDetailFragment extends Fragment {
    private static final String REMINDER_ITEM = "REMINDER_ITEM";


    private AppCompatEditText reminderTitle;
    private AppCompatCheckBox vibrateOnly, autoRenew;
    private TimePicker nosePicker;
    private Reminder reminder;

    //it is necessary to keep track fo the oldTitle of a Reminder, as this is our unique
    //identifier for the Database
    private String oldTitle;

    private ImageView back, proceed;

    private FragmentManageReminderCallback callback;

    public ReminderDetailFragment() {
    }

    /**
     * This static constructor is used when we wish to create a new Reminder object, in which case no
     * arguments are passed in
     *
     * @return Fragment without arguments
     */
    public static ReminderDetailFragment newInstance() {
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        return fragment;
    }

    /**
     * This static constructor is used when we wish to edit an existing Reminder object.
     *
     * @param reminder The reminder we wish to edit
     * @return Fragment with arguments
     */
    public static ReminderDetailFragment newInstance(Reminder reminder) {
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(REMINDER_ITEM, reminder);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.reminder = getArguments().getParcelable(REMINDER_ITEM);
            oldTitle = reminder.getAlarmTitle();
        } else {
            this.reminder = new Reminder(12,
                    0,
                    "",
                    false,
                    false,
                    false,
                    getDate());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_reminder, container, false);

        reminderTitle = (AppCompatEditText) v.findViewById(R.id.edt_reminder_title);
        nosePicker = (TimePicker) v.findViewById(R.id.pck_reminder_time);

        vibrateOnly = (AppCompatCheckBox) v.findViewById(R.id.chb_vibrate_only);
        autoRenew = (AppCompatCheckBox) v.findViewById(R.id.chb_renew_automatically);

        back = (ImageButton) v.findViewById(R.id.imb_manage_back);
        proceed = (ImageButton) v.findViewById(R.id.imb_manage_proceed);

        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initializeViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void initializeViews() {
        //set up check boxes
        if (reminder.isVibrateOnly()) {
            vibrateOnly.setChecked(true);
        } else {
            vibrateOnly.setChecked(false);
        }

        if (reminder.isRenewAutomatically()) {
            autoRenew.setChecked(true);
        } else {
            autoRenew.setChecked(false);
        }

        //set title
        reminderTitle.setText(reminder.getAlarmTitle());

        //set up Picker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nosePicker.setHour(reminder.getHourOfDay());
            nosePicker.setMinute(reminder.getMinute());
        } else {
            nosePicker.setCurrentHour(reminder.getHourOfDay());
            nosePicker.setCurrentMinute(reminder.getMinute());
        }
    }

    private void validateInput() {
        String titleInput = reminderTitle.getText().toString();
        if (titleInput.equals("")) {
            makeSomeToast(getString(R.string.error_input_invalid_blank));
        } else if (titleInput.length() > 36) {
            makeSomeToast(getString(R.string.error_input_invalid_too_long));
        } else {
            reminder.setAlarmTitle(titleInput);
            reminder.setVibrateOnly(vibrateOnly.isChecked());
            reminder.setRenewAutomatically(autoRenew.isChecked());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                reminder.setHourOfDay(nosePicker.getHour());
                reminder.setMinute(nosePicker.getMinute());
            } else {
                reminder.setHourOfDay(nosePicker.getCurrentHour());
                reminder.setMinute(nosePicker.getCurrentMinute());
            }

            if (reminder.isActive()) {
                reminder.setActive(false);
            }

            callback.onProceedPressed(reminder);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentManageReminderCallback) {
            callback = (FragmentManageReminderCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface FragmentManageReminderCallback {
        void onProceedPressed(Reminder reminder);
    }

    private void makeSomeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * All of this crap is just to create a unique identifier which is used in Database and for
     * creating unique intents.
     *
     * @return a unique id, based on the current time. Doesn't need to be fancy or stupidly long
     * like Calendar.getTimeInMillis()
     */
    public int getDate() {
        Calendar calendar = Calendar.getInstance();
        String date  = "" + calendar.get(Calendar.DAY_OF_YEAR);
        date += "" + calendar.get(Calendar.HOUR_OF_DAY);
        date += "" + calendar.get(Calendar.MINUTE);
        date += "" + calendar.get(Calendar.SECOND);
        Log.d("TAG", "Date works out to: " + date);
        return Integer.parseInt(date);
    }
}
