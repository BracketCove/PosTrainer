package com.bracketcove.postrainer.reminderdetail;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.util.ActivityUtils;


public class ReminderDetailActivity extends AppCompatActivity {
    private static final String FRAG_REMINDER_DETAIL = "FRAG_REMINDER_DETAIL";

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);


        manager = getSupportFragmentManager();

        ReminderDetailFragment fragment =  (ReminderDetailFragment) manager.findFragmentByTag(FRAG_REMINDER_DETAIL);

        if (fragment == null){
            fragment = ReminderDetailFragment.newInstance();

        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_reminder_list_fragment,
                FRAG_REMINDER_DETAIL
                );
    }


//    public void initComponent(){
//        this.reminderDetailComponent = DaggerReminderDetailComponent
//                .builder()
//                .applicationComponent(getApplicationComponent())
//                .build();
//    }
//
//    protected ApplicationComponent getApplicationComponent() {
//        return PostrainerApplication.getApplicationComponent();
//    }


}
