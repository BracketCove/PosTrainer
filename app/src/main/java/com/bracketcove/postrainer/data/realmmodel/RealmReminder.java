package com.bracketcove.postrainer.data.realmmodel;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * POJO for Realm
 * Created by Ryan on 10/04/2016.
 */
@RealmClass
public class RealmReminder implements RealmModel {

    @PrimaryKey
    private String reminderId;

    private String reminderTitle;
    private boolean active;
    private boolean vibrateOnly;
    private boolean renewAutomatically;
    private int hourOfDay;
    private int minute;

    public RealmReminder() {
        //blank constructor for realm?
    }

    public RealmReminder(String reminderId,
                         String reminderTitle,
                         boolean active,
                         boolean vibrateOnly,
                         boolean renewAutomatically,
                         int minute,
                         int hourOfDay
    ) {

        this.reminderId = reminderId;
        this.reminderTitle = reminderTitle;
        this.active = active;
        this.vibrateOnly = vibrateOnly;
        this.renewAutomatically = renewAutomatically;
        this.minute = minute;
        this.hourOfDay = hourOfDay;
    }

    /**
     * To create a new RealmReminder
     *
     * @param reminderId
     */
    public RealmReminder(String reminderId) {
        this.reminderId = reminderId;
        this.hourOfDay = 12;
        this.minute = 0;
        this.reminderTitle = "New Alarm";
        this.active = false;
        this.vibrateOnly = false;
        this.renewAutomatically = false;

    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
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
