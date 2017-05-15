package com.bracketcove.postrainer.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.reminderdetail.ReminderDetailFragment;
import com.bracketcove.postrainer.util.ActivityUtils;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {
    private static final String FRAG_SETTINGS = "FRAG_SETTINGS";

    private Toolbar toolbar;
    private FragmentManager manager;

    @Inject
    SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        manager = getSupportFragmentManager();

        SettingsFragment fragment =  (SettingsFragment) manager.findFragmentByTag(FRAG_SETTINGS);

        if (fragment == null){
            fragment = SettingsFragment.newInstance();
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_settings_fragment,
                FRAG_SETTINGS
        );

        DaggerSettingsComponent.builder()
                .settingsPresenterModule(new SettingsPresenterModule(fragment))
                .build().inject(this);
    }
}
