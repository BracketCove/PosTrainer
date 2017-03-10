package com.bracketcove.postrainer.data;

import android.os.AsyncTask;

import com.bracketcove.postrainer.data.reminder.ReminderDatabase;


/**
 * Created by Ryan on 31/12/2015.
 */
public class WriteToDatabase extends AsyncTask<ReminderDatabase, Void, Long> {
    private boolean updateEntryIfPossible = true;
    private Object data;

    private OnWriteComplete onWriteComplete;

    public interface OnWriteComplete {
        void setWriteComplete(long result);
    }


    public void setData(Object data) {
        this.data = data;
    }

    public void setWriteCompleteListener(OnWriteComplete onWriteComplete) {
        this.onWriteComplete = onWriteComplete;
    }

    @Override
    protected Long doInBackground(ReminderDatabase... params) {
        if (data != null) {
            ReminderDatabase database = params[0];

            return database.insertOrUpdateData(data);

            //return database.insertData(data);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long param) {
        if (param != null && param != -1) {
            onWriteComplete.setWriteComplete(param);
        }

    }
}
