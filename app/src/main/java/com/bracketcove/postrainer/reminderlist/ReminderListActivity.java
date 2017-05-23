package com.bracketcove.postrainer.reminderlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bracketcove.postrainer.PostrainerApplication;
import com.bracketcove.postrainer.R;
import com.bracketcove.postrainer.util.ActivityUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import javax.inject.Inject;

public class ReminderListActivity extends AppCompatActivity {
    private static final String FRAG_REMINDER_LIST = "FRAG_REMINDER_LIST";

    private FragmentManager manager;

    @Inject ReminderListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);
        //Toolbar
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();

        ReminderListFragment fragment = (ReminderListFragment) manager.findFragmentByTag(FRAG_REMINDER_LIST);

        if (fragment == null){
            fragment = ReminderListFragment.newInstance();
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.cont_reminder_list_fragment,
                FRAG_REMINDER_LIST
        );

        new TedPermission(this)
                .setPermissionListener(
                        new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {

                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                            }
                        }
                )
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.VIBRATE
                )
                .check();

        DaggerReminderListComponent.builder()
                .reminderListPresenterModule(new ReminderListPresenterModule(fragment))
                .applicationComponent(
                        ((PostrainerApplication) getApplication())
                                .getApplicationComponent()
                )
                .build().inject(this);
    }
}
