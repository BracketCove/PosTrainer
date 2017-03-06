package com.bracketcove.postrainer.reminderlist;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.database.Reminder;

import java.util.ArrayList;

/**
 * Created by Ryan on 08/08/2016.
 */
public class ReminderListFragment extends Fragment {
    private static final String LIST_DATA = "LIST_DATA";

    private ArrayList<Reminder> listData;
    private FragmentReminderListCallback callback;
    private RecyclerView reminderList;
    private FloatingActionButton fabulous;
    private TextView prompt;
    private ReminderListAdapter adapter;

    public ReminderListFragment() {
    }

    public static ReminderListFragment newInstance(ArrayList<Reminder> listData) {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_DATA, listData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.listData = getArguments().getParcelableArrayList(LIST_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_list, container, false);
        reminderList = (RecyclerView) v.findViewById(R.id.lst_reminders);
        fabulous = (FloatingActionButton) v.findViewById(R.id.fab_reminders);
        prompt = (TextView) v.findViewById(R.id.lbl_reminder_prompt);
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (listData.size() > 0) {
            setUpRecyclerView();
        } else {
            prompt.setVisibility(View.VISIBLE);
        }

        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onFabAddReminderClicked();
                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpRecyclerView() {
        adapter = new ReminderListAdapter(getActivity(), listData);
        reminderList.setAdapter(adapter);
        adapter.setOnClickListener(new ReminderListAdapter.OnItemClickListener() {

            //TODO may need to be renamed to onAlarmClick, Icon obscure
            @Override
            public void onAlarmIconClick(int position) {
                callback.onReminderClicked(position);
            }

            @Override
            public void onAlarmSwitchClick(int position) {
                callback.onReminderToggled(position);
            }
        });
        reminderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((createHelperCallback()));
        itemTouchHelper.attachToRecyclerView(reminderList);

        reminderList.addItemDecoration(new CustomItemDecorator(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentReminderListCallback) {
            callback = (FragmentReminderListCallback) context;
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
                callback.onReminderSwiped(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        return simpleItemTouchCallback;
    }

    public interface FragmentReminderListCallback {
        //includes a click anywhere except the switch
        void onReminderClicked(int position);

        //specifically the switch
        void onReminderToggled(int position);

        void onFabAddReminderClicked();

        void onReminderSwiped(int position);
    }

}
