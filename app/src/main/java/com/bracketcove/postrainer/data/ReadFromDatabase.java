package com.bracketcove.postrainer.data;

import android.os.AsyncTask;


import com.bracketcove.postrainer.data.reminder.ReminderDatabase;

import java.util.ArrayList;


/**
 * String ->
 * The purpose of this class, is to allow a database to be queried in the background, with the UI
 * thread being updated subsequently.
 * @param
 * @Created by Ryan on 29/12/2015.
 */
 public class ReadFromDatabase extends AsyncTask<ReminderDatabase, Void, ArrayList> {
    //private String args;
    private OnQueryComplete onQueryComplete;

    public interface OnQueryComplete {
        void setQueryComplete(ArrayList result);
    }

    public void setQueryCompleteListener(OnQueryComplete onQueryComplete) {
        this.onQueryComplete = onQueryComplete;
    }

   // public void setArgs (String args){
     //   this.args = args;
   // }


    @Override
    protected ArrayList doInBackground(ReminderDatabase... params) {
        ReminderDatabase database = params[0];
        return database.getAllDataAsList();
    }

    //Value returned from doInBackground is passed to onPostExecute
    @Override
    protected void onPostExecute(ArrayList result) {
        onQueryComplete.setQueryComplete(result);
    }


}
