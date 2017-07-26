package com.bracketcove.postrainer.data.viewmodel;

/**
 * POJO for the View
 * Created by Ryan on 10/04/2017.
 */

public class Alarm {

    private String alarmId;
    private String alarmTitle;
    private boolean active;
    private boolean vibrateOnly;
    private boolean renewAutomatically;
    private int hourOfDay;
    private int minute;


    public Alarm() {
    }

    public Alarm(String alarmId,
                 String alarmTitle,
                 boolean active,
                 boolean vibrateOnly,
                 boolean renewAutomatically,
                 int hourOfDay,
                 int minute) {
        this.alarmId = alarmId;
        this.alarmTitle = alarmTitle;
        this.active = active;
        this.vibrateOnly = vibrateOnly;
        this.renewAutomatically = renewAutomatically;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
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
