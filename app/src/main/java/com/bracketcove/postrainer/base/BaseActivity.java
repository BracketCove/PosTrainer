package com.bracketcove.postrainer.base;

import android.support.v7.app.AppCompatActivity;

/**
 * What is this Class?
 * - All Activities have their Services Decoupled from their implementation (no Activity talks to
 * the Realm Database, or any System Service like AlarmManager, directly. They talk through
 * Activity->Presenter->UseCase->Service [Entity if you prefer])
 * - Since Activities don't create any of these things, they must all talk to the
 *
 * Created by R_KAY on 5/22/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {



}
