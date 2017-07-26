package com.bracketcove.postrainer.data.realmmodel;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * POJO for Realm
 * Created by Ryan on 10/04/2016.
 */
@RealmClass
public class RealmAlarm implements RealmModel {

    @PrimaryKey
    private String alarmId;

    private String alarmTitle;
    private boolean active;
    private boolean vibrateOnly;
    private boolean renewAutomatically;
    private int hourOfDay;
    private int minute;

    public RealmAlarm() {
        //blank constructor for realm?
    }

    public RealmAlarm(String alarmId,
                      String alarmTitle,
                      boolean active,
                      boolean vibrateOnly,
                      boolean renewAutomatically,
                      int minute,
                      int hourOfDay
    ) {

        this.alarmId = alarmId;
        this.alarmTitle = alarmTitle;
        this.active = active;
        this.vibrateOnly = vibrateOnly;
        this.renewAutomatically = renewAutomatically;
        this.minute = minute;
        this.hourOfDay = hourOfDay;
    }

    /**
     * To create a new RealmAlarm
     *
     * @param alarmId
     */
    public RealmAlarm(String alarmId) {
        this.alarmId = alarmId;
        this.hourOfDay = 12;
        this.minute = 0;
        this.alarmTitle = "New Alarm";
        this.active = false;
        this.vibrateOnly = false;
        this.renewAutomatically = false;

    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVibrateOnly() {
        return vibrateOnly;
    }

    public void setVibrateOnly(boolean vibrateOnly) {
        this.vibrateOnly = vibrateOnly;
    }

    public boolean isRenewAutomatically() {
        return renewAutomatically;
    }

    public void setRenewAutomatically(boolean renewAutomatically) {
        this.renewAutomatically = renewAutomatically;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }


}
