package com.bracketcove.postrainer.data.reminder;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * POJO for Realm
 * Created by Ryan on 10/04/2016.
 */
public class RealmReminder extends RealmObject {

    @PrimaryKey
    private String reminderId;

    private String reminderTitle;
    private boolean active;
    private boolean vibrateOnly;
    private boolean renewAutomatically;
    private int minute;
    private int hourOfDay;

    public RealmReminder() {
        //blank constructor for realm?
    }

    /**
     * General Purpose Constructor
     *
     * @param hourOfDay
     * @param minute
     * @param reminderTitle
     * @param active
     * @param vibrateOnly
     * @param renewAutomatically
     */
    public RealmReminder(String reminderId,
                         int hourOfDay,
                         int minute,
                         String reminderTitle,
                         boolean active,
                         boolean vibrateOnly,
                         boolean renewAutomatically
    ) {
        this.reminderId = reminderId;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.reminderTitle = reminderTitle;
        this.active = active;
        this.vibrateOnly = vibrateOnly;
        this.renewAutomatically = renewAutomatically;
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
