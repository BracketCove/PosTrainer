package com.bracketcove.postrainer.reminderlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.data.viewmodel.Reminder;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailActivity;
import com.bracketcove.postrainer.settings.SettingsActivity;
import com.bracketcove.postrainer.util.TimeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderListFragment extends Fragment implements ReminderListContract.View {
    private static final String REMINDER_TO_BE_EDITED = "REMINDER_TO_BE_EDITED";

    private RecyclerView reminderList;
    private FloatingActionButton fabulous;
    private TextView prompt;
    private ReminderListAdapter adapter;
    private ArrayList<Reminder> reminders;
    private ImageButton settings;

    ReminderListContract.Presenter presenter;

    public ReminderListFragment() {

    }

    public static ReminderListFragment newInstance() {
        return new ReminderListFragment();
    }

    /*------------------------------- Lifecycle -------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        prompt = (TextView) v.findViewById(R.id.lbl_reminder_prompt);

        settings = (ImageButton) v.findViewById(R.id.btn_reminder_list_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSettingsIconClick();
            }
        });

        reminderList = (RecyclerView) v.findViewById(R.id.lst_reminder_list);

        reminders = new ArrayList<>();

        initializeRecyclerView();

        fabulous = (FloatingActionButton) v.findViewById(R.id.fab_reminders);
        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onCreateReminderButtonClick(
                        reminders.size(),
                        new Reminder(
                                getDate(),
                                getString(R.string.def_reminder_name),
                                false,
                                true,
                                false,
                                12,
                                30
                        )
                );
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void setPresenter(ReminderListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void makeToast(@StringRes int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    /*
    Context may be risky here during orientation changes. Check for such use cases
    during tests.
     */
    @Override
    public void setReminderListData(List<Reminder> reminders) {
        prompt.setVisibility(View.INVISIBLE);
        reminderList.setVisibility(View.VISIBLE);
        //if reminderListData isn't empty
        if (!this.reminders.isEmpty()) {
            this.reminders.clear();
            adapter.notifyDataSetChanged();
        }

        for (Reminder reminder : reminders) {
            //add reminder to fragments list, and inform adapter of this change
            this.reminders.add(reminder);
            adapter.notifyItemInserted(this.reminders.lastIndexOf(reminder));
        }
    }

    @Override
    public void setNoReminderListDataFound() {
        reminderList.setVisibility(View.INVISIBLE);
        prompt.setVisibility(View.VISIBLE);
    }

    /**
     * Must add RealmReminder both to list and UI
     *
     * @param reminder new RealmReminder to be added
     */
    @Override
    public void addNewReminderToListView(Reminder reminder) {
        if (reminders.size() == 0) {
            initializeRecyclerView();
            reminderList.setVisibility(View.VISIBLE);
            prompt.setVisibility(View.INVISIBLE);
        }

        reminders.add(reminder);
        adapter.notifyItemInserted(this.reminders.lastIndexOf(reminder));
    }

    @Override
    public void undoDeleteReminderAt(int index, Reminder reminder) {
        reminders.add(index, reminder);
        adapter.notifyItemInserted(index);
    }

    @Override
    public void startReminderDetailActivity(String reminderId) {
        Intent i = new Intent(getActivity(), ReminderDetailActivity.class);
        i.putExtra(REMINDER_TO_BE_EDITED, reminderId);
        startActivity(i);
    }

    @Override
    public void startSettingsActivity() {
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
    }

    /**
     * All of this crap is just to create a unique identifier which is used to identify Reminders in
     * the Database.
     *
     * @return a unique id, based on the current time. Doesn't need to be fancy or stupidly long
     * like Calendar.getTimeInMillis()
     */
    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        String date =
                "" + calendar.get(Calendar.DAY_OF_YEAR) +
                        "" + calendar.get(Calendar.HOUR_OF_DAY) +
                        "" + calendar.get(Calendar.MINUTE) +
                        "" + calendar.get(Calendar.SECOND);

        return date;
    }

    /*------------------------------- RecView Boilerplate -------------------------------*/

    private void initializeRecyclerView() {
        adapter = new ReminderListAdapter(getActivity());
        reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderList.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((createHelperCallback()));
        itemTouchHelper.attachToRecyclerView(reminderList);

        reminderList.addItemDecoration(new CustomItemDecorator(getActivity()));
    }

    private class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
        private LayoutInflater inflater;

        /**
         * @param context - Context of Activity which contains contains the fragment which manages
         *                this class
         */
        public ReminderListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_alarm_widget, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReminderListAdapter.ViewHolder holder, final int position) {
            Reminder item = reminders.get(position);
            holder.alarmTitle.setText(item.getReminderTitle());

            try {
                holder.alarmTime.setText(
                        TimeConverter.convertTime(item.getHourOfDay(), item.getMinute())
                );
            } catch (ParseException e) {
                //todo: handle this error case better if necessary
                holder.alarmTime.setText("12:00pm");
            }

            if (item.isActive()) {
                holder.alarmStateLabel.setText(R.string.on);
            } else {
                holder.alarmStateLabel.setText(R.string.off);
            }
            holder.alarmStateSwitch.setChecked(item.isActive());

        }

        @Override
        public int getItemCount() {
            return reminders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView alarmTitle;
            TextView alarmTime;
            TextView alarmStateLabel;
            ImageView alarmIcon;
            SwitchCompat alarmStateSwitch;
            View alarmActiveContainer;

            public ViewHolder(View itemView) {
                super(itemView);
                alarmTitle = (TextView) itemView.findViewById(R.id.lbl_alarm_title);
                alarmTime = (TextView) itemView.findViewById(R.id.lbl_alarm_time);
                alarmStateLabel = (TextView) itemView.findViewById(R.id.lbl_alarm_activation);
                alarmStateSwitch = (SwitchCompat) itemView.findViewById(R.id.swi_alarm_activation);
                alarmIcon = (ImageView) itemView.findViewById(R.id.im_clock);
                alarmIcon.setOnClickListener(this);
                alarmActiveContainer = (View) itemView.findViewById(R.id.cont_alarm_activation);
                alarmStateSwitch.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int id = v.getId();

                if (id == R.id.swi_alarm_activation) {

                    if (alarmStateSwitch.isChecked()) {
                        alarmStateLabel.setText(R.string.on);
                        presenter.onReminderToggled(
                                true,
                                reminders.get(this.getAdapterPosition())
                        );
                    } else {
                        alarmStateLabel.setText(R.string.off);
                        presenter.onReminderToggled(
                                false,
                                reminders.get(this.getAdapterPosition())
                        );
                    }
                } else if (id == R.id.im_clock) {
                    presenter.onReminderIconClick(
                            reminders.get(this.getAdapterPosition())
                    );
                }

            }
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                presenter.onReminderSwiped(
                        position,
                        reminders.get(position)
                );

                reminders.remove(position);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());


            }
        };
        return simpleItemTouchCallback;
    }
}
