package com.bracketcove.postrainer.reminderlist;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.util.TimeConverter;
import com.bracketcove.postrainer.database.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderListFragment extends Fragment implements ReminderListContract.View {
    private static final String LIST_DATA = "LIST_DATA";

    private ArrayList<Reminder> reminderListData;
    private RecyclerView reminderList;
    private FloatingActionButton fabulous;
    private TextView prompt;
    private ReminderListAdapter adapter;

    private ReminderListContract.Presenter presenter;

    public ReminderListFragment() {
    }

    public static ReminderListFragment newInstance() {
        return new ReminderListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_list, container, false);
        reminderList = (RecyclerView) v.findViewById(R.id.lst_reminders);
        fabulous = (FloatingActionButton) v.findViewById(R.id.fab_reminders);
        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onFabAddReminderClicked();
                }
            }
        });
        prompt = (TextView) v.findViewById(R.id.lbl_reminder_prompt);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = new ReminderListPresenter(this,

                    );
        }
        presenter.subscribe();
    }

    private void setUpRecyclerView() {

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
    public void setPresenter(ReminderListContract.Presenter presenter) {

    }

    @Override
    public void makeToast(String message) {

    }

    @Override
    public void setReminderListData(ArrayList<Reminder> reminderListData) {
        this.reminderListData = reminderListData;

        // Let's build an App! | Episode 3| Unit Testing and RxJava 2
        //

        adapter = new ReminderListAdapter(getActivity(), reminderListData);
        reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderList.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((createHelperCallback()));
        itemTouchHelper.attachToRecyclerView(reminderList);

        reminderList.addItemDecoration(new CustomItemDecorator(getActivity()));
    }

    @Override
    public void setNoReminderListDataFound() {

    }

    private class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<Reminder> data = Collections.emptyList();

        /**
         * @param context - Context of Activity which contains contains the fragment which manages
         *                this class
         * @param data    - List of NavListItems which contain a title and icon resource id, to be
         *                passed to bound to ViewHolder objects appropriately.
         */
        public ReminderListAdapter(Context context, List<Reminder> data) {
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_alarm_widget, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReminderListAdapter.ViewHolder holder, final int position) {
            Reminder item = data.get(position);
            holder.alarmTitle.setText(item.getAlarmTitle());

            holder.alarmTime.setText(
                    TimeConverter.convertTime(item.getHourOfDay(),item.getMinute())
            );
            if (item.isActive()){
                holder.alarmStateLabel.setText(R.string.on);
            } else {
                holder.alarmStateLabel.setText(R.string.off);
            }
            holder.alarmStateSwitch.setChecked(item.isActive());

        }

        @Override
        public int getItemCount() {
            return data.size();
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

                if (id == R.id.swi_alarm_activation){
                    if (alarmStateSwitch.isChecked()){
                        alarmStateLabel.setText(R.string.on);
                    } else {
                        alarmStateLabel.setText(R.string.off);
                    }
                } else if (id == R.id.im_clock){
                }

            }
        }

    }

    /*----------------------RecyclerView S**t--------------------------*/

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
                presenter
                callback.onReminderSwiped(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        return simpleItemTouchCallback;
    }

}
