package com.bracketcove.postrainer.data;

import android.os.AsyncTask;

import com.bracketcove.postrainer.data.reminder.Reminder;
import com.bracketcove.postrainer.data.reminder.ReminderDatabase;

/**
 * Special case where the user toggles an Active or Inactive alarm. In that case, I know that only
 * one column in the Database is going to be changed, and there is no need to reload the entire
 * reminder object, thereby restarting ReminderListFragment
 * Created by Ryan on 09/08/2016.
 */
public class ToggleReminderAlarmState  extends AsyncTask<ReminderDatabase, Void, Long> {
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
    protected Long doInBackground(ReminderDatabase... params) {
        ReminderDatabase database = params[0];

        return database.toggleAlarmState(reminder);
    }

    @Override
    protected void onPostExecute(Long param) {
    }
}
