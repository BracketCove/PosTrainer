package com.bracketcove.postrainer.reminderdetail;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.reminderlist.ReminderListActivity;
import com.bracketcove.postrainer.util.ActivityUtils;

import javax.inject.Inject;


public class ReminderDetailActivity extends AppCompatActivity {
    private static final String FRAG_REMINDER_DETAIL = "FRAG_REMINDER_DETAIL";
    private static final String REMINDER_TO_BE_EDITED = "REMINDER_TO_BE_EDITED";

    private FragmentManager manager;

    @Inject
    ReminderDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);

        String reminderId = getIntent().getStringExtra(REMINDER_TO_BE_EDITED);

        if (reminderId == null){
            startActivity(new Intent(this, ReminderListActivity.class));
        }

        manager = getSupportFragmentManager();

        ReminderDetailFragment fragment =  (ReminderDetailFragment) manager.findFragmentByTag(FRAG_REMINDER_DETAIL);

        if (fragment == null){
            fragment = ReminderDetailFragment.newInstance(reminderId);

        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_reminder_detail_fragment,
                FRAG_REMINDER_DETAIL
        );

        DaggerReminderDetailComponent.builder()
                .reminderDetailPresenterModule(new ReminderDetailPresenterModule(fragment))
                .reminderComponent(
                        ((PostrainerApplication) getApplication())
                                .getReminderComponent()
                )
                .build().inject(this);
    }
}
