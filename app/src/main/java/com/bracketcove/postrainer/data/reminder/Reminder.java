package com.bracketcove.postrainer.data.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a Reminder (Alarm), as used in the RecyclerView in ReminderListActivity.java
 * Created by Ryan on 10/04/2016.
 */
public class Reminder implements Parcelable{

    private String reminderTitle;
    private boolean active;
    private boolean vibrateOnly;
    private boolean renewAutomatically;
    private int minute;
    private int hourOfDay;
    private String reminderId;

    public Reminder(int hourOfDay,
                    int minute,
                    String reminderTitle,
                    boolean active,
                    boolean vibrateOnly,
                    boolean renewAutomatically,
                    String reminderId) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.reminderTitle = reminderTitle;
        this.active = active;
        this.vibrateOnly = vibrateOnly;
        this.renewAutomatically = renewAutomatically;
        this.reminderId = reminderId;
    }

    protected Reminder(Parcel in) {
        reminderTitle = in.readString();
        active = in.readByte() != 0;
        vibrateOnly = in.readByte() != 0;
        renewAutomatically = in.readByte() != 0;
        minute = in.readInt();
        hourOfDay = in.readInt();
        reminderId = in.readString();
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public boolean isRenewAutomatically() {
        return renewAutomatically;
    }

    /**
     * @param renewAutomatically Automatically renew alarm once it fires
     */
    public void setRenewAutomatically(boolean renewAutomatically) {
        this.renewAutomatically = renewAutomatically;
    }

    public boolean isVibrateOnly() {
        return vibrateOnly;
    }

    /**
     * @param vibrateOnly self explanatory
     */
    public void setVibrateOnly(boolean vibrateOnly) {
        this.vibrateOnly = vibrateOnly;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    /**
     * @param reminderTitle User defined name of a title. Must be less than 36, characters,
     * (validation handled in Activity Code)
     */
    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * @param active Alarm is Active or Not (True or False)
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMinute() {
        return minute;
    }

    /**
     * @param minute Minute, to be combined with hour of day.
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    /**
     * @param hourOfDay Hour of day, which produces an Alarm trigger time when combined with
     * minute
     */
    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reminderTitle);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeByte((byte) (vibrateOnly ? 1 : 0));
        dest.writeByte((byte) (renewAutomatically ? 1 : 0));
        dest.writeInt(minute);
        dest.writeInt(hourOfDay);
        dest.writeString(reminderId);
    }
}
