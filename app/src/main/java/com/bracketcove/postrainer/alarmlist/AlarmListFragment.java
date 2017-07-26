package com.bracketcove.postrainer.alarmlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.alarmdetail.AlarmDetailActivity;
import com.bracketcove.postrainer.data.viewmodel.Alarm;
import com.bracketcove.postrainer.settings.SettingsActivity;
import com.bracketcove.postrainer.util.TimeConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ryan on 08/08/2016.
 */
public class AlarmListFragment extends Fragment implements AlarmListContract.View {
    private static final String ALARM_TO_BE_EDITED = "ALARM_TO_BE_EDITED";

    private RecyclerView alarmList;
    private FloatingActionButton fabulous;
    private TextView prompt;
    private AlarmListAdapter adapter;
    private ArrayList<Alarm> alarms;
    private ImageButton settings;

    @Inject
    AlarmListPresenter presenter;

    public AlarmListFragment() {

    }

    public static AlarmListFragment newInstance() {
        return new AlarmListFragment();
    }

    /*------------------------------- Lifecycle -------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        DaggerAlarmListComponent.builder()
                .alarmListPresenterModule(new AlarmListPresenterModule(this))
                .applicationComponent(
                        ((PostrainerApplication) getActivity().getApplication())
                                .getApplicationComponent()
                )
                .build().inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        prompt = (TextView) v.findViewById(R.id.lbl_alarm_prompt);

        settings = (ImageButton) v.findViewById(R.id.btn_alarm_list_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSettingsIconClick();
            }
        });

        alarmList = (RecyclerView) v.findViewById(R.id.lst_alarm_list);

        alarms = new ArrayList<>();

        initializeRecyclerView();

        fabulous = (FloatingActionButton) v.findViewById(R.id.fab_alarms);
        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onCreateAlarmButtonClick(
                        alarms.size(),
                        new Alarm(
                                getDate(),
                                getString(R.string.def_alarm_name),
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
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stop();
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
    public void makeToast(@StringRes int message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showUndoSnackbar() {
        Snackbar.make(
                getView().findViewById(R.id.root_alarm_list_fragment),
                getString(R.string.action_delete_item),
                Snackbar.LENGTH_LONG
        )
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onUndoConfirmed();
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        presenter.onSnackbarTimeout();
                    }
                })
                .show();
    }

    @Override
    public void insertAlarmAt(int position, Alarm alarm) {
        alarms.add(position, alarm);

        adapter.notifyItemInserted(position);
    }

    /*
    Context may be risky here during orientation changes. Check for such use cases
    during tests.
     */
    @Override
    public void setAlarmListData(List<Alarm> alarms) {
        prompt.setVisibility(View.INVISIBLE);
        alarmList.setVisibility(View.VISIBLE);
        //if alarmListData isn't empty
        if (!this.alarms.isEmpty()) {
            this.alarms.clear();
            adapter.notifyDataSetChanged();
        }

        for (Alarm alarm : alarms) {
            //add alarm to fragments list, and inform adapter of this change
            this.alarms.add(alarm);
            adapter.notifyItemInserted(this.alarms.lastIndexOf(alarm));
        }
    }

    @Override
    public void setNoAlarmListDataFound() {
        alarmList.setVisibility(View.INVISIBLE);
        prompt.setVisibility(View.VISIBLE);
    }

    /**
     * Must add RealmAlarm both to list and UI
     *
     * @param alarm new RealmAlarm to be added
     */
    @Override
    public void addNewAlarmToListView(Alarm alarm) {
        if (alarms.size() == 0) {
            initializeRecyclerView();
            alarmList.setVisibility(View.VISIBLE);
            prompt.setVisibility(View.INVISIBLE);
        }

        alarms.add(alarm);
        adapter.notifyItemInserted(this.alarms.lastIndexOf(alarm));
    }

    @Override
    public void startAlarmDetailActivity(String alarmId) {
        Intent i = new Intent(getActivity(), AlarmDetailActivity.class);
        i.putExtra(ALARM_TO_BE_EDITED, alarmId);
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
        adapter = new AlarmListAdapter(getActivity());
        alarmList.setLayoutManager(new LinearLayoutManager(getActivity()));
        alarmList.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((createHelperCallback()));
        itemTouchHelper.attachToRecyclerView(alarmList);

        alarmList.addItemDecoration(new CustomItemDecorator(getActivity()));
    }

    private class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
        private LayoutInflater inflater;

        /**
         * @param context - Context of Activity which contains contains the fragment which manages
         *                this class
         */
        public AlarmListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_alarm_widget, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmListAdapter.ViewHolder holder, final int position) {
            Alarm item = alarms.get(position);
            holder.alarmTitle.setText(item.getAlarmTitle());

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
            return alarms.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView alarmTitle;
            TextView alarmTime;
            TextView alarmStateLabel;
            ImageView alarmIcon;
            SwitchCompat alarmStateSwitch;

            public ViewHolder(View itemView) {
                super(itemView);
                alarmTitle = (TextView) itemView.findViewById(R.id.lbl_alarm_title);
                alarmTime = (TextView) itemView.findViewById(R.id.lbl_alarm_time);
                alarmStateLabel = (TextView) itemView.findViewById(R.id.lbl_alarm_activation);
                alarmStateSwitch = (SwitchCompat) itemView.findViewById(R.id.swi_alarm_activation);
                alarmIcon = (ImageView) itemView.findViewById(R.id.im_clock);
                alarmIcon.setOnClickListener(this);
                alarmStateSwitch.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int id = v.getId();

                if (id == R.id.swi_alarm_activation) {

                    if (alarmStateSwitch.isChecked()) {
                        alarmStateLabel.setText(R.string.on);
                        presenter.onAlarmToggled(
                                true,
                                alarms.get(this.getAdapterPosition())
                        );
                    } else {
                        alarmStateLabel.setText(R.string.off);
                        presenter.onAlarmToggled(
                                false,
                                alarms.get(this.getAdapterPosition())
                        );
                    }
                } else if (id == R.id.im_clock) {
                    presenter.onAlarmIconClick(
                            alarms.get(this.getAdapterPosition())
                    );
                }

            }
        }
    }

    /**
     * ItemTouchHlper
      * @return
     */
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
                presenter.onAlarmSwiped(
                        position,
                        alarms.get(position)
                );

                alarms.remove(position);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());


            }
        };
        return simpleItemTouchCallback;
    }
}
