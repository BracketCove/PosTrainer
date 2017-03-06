package com.bracketcove.postrainer.database;

import android.os.AsyncTask;

/**
 * Special case where the user toggles an Active or Inactive alarm. In that case, I know that only
 * one column in the Database is going to be changed, and there is no need to reload the entire
 * reminder object, thereby restarting ReminderListFragment
 * Created by Ryan on 09/08/2016.
 */
public class ToggleReminderAlarmState  extends AsyncTask<AlarmDatabase, Void, Long> {
    private Reminder reminder;

    /*private OnWriteComplete onWriteComplete;
    public interface OnWriteComplete {
        void setWriteComplete(long result);
    }
    public void setWriteCompleteListener(OnWriteComplete onWriteComplete) {
        this.onWriteComplete = onWriteComplete;
    }*/

    public ToggleReminderAlarmState (Reminder reminder){
        this.reminder = reminder;
    }

    @Override
    protected Long doInBackground(AlarmDatabase... params) {
        AlarmDatabase database = params[0];

        return database.toggleAlarmState(reminder);
    }

    @Override
    protected void onPostExecute(Long param) {
    }
}
