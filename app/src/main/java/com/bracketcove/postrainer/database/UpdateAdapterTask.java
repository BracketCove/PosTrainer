package com.bracketcove.postrainer.database;

import android.os.AsyncTask;

import java.util.ArrayList;


/**
 * Created by Ryan on 03/11/2015.
 */
public class UpdateAdapterTask extends AsyncTask<MovementListAdapter, Void, MovementListAdapter> {
    private ArrayList data;
    private OnUpdateComplete onUpdateComplete;

    public UpdateAdapterTask(ArrayList data){
        this.data = data;
    }

    //For next time I forget, params[0] grabs the adapter passed in the Asynctask's parameters.
    @Override
    protected MovementListAdapter doInBackground(MovementListAdapter... params) {
        params[0].setMovementList(data);
        return params[0];
    }

    @Override
    protected void onPostExecute(MovementListAdapter adapter) {
        adapter.notifyDataSetChanged();
        onUpdateComplete.setUpdateComplete();
    }
    

    public interface OnUpdateComplete {
        void setUpdateComplete();
    }

    public void setUpdateCompleteListener(OnUpdateComplete onUpdateComplete) {
        this.onUpdateComplete = onUpdateComplete;
    }
}

