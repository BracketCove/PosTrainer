package com.bracketcove.postrainer.data.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Ryan on 07/09/2015. This class contains a model for the list of
 * exercises available to the user to select.
 */
public class ReminderDatabase {
    private static ReminderDatabase mInstance;

    private static final String TABLE_NAME = "alarms";
    private static final String COLUMN_ENTRY_ID = "entry_id";
    private static final String COLUMN_ALARM_TITLE = "title";
    private static final String COLUMN_ALARM_ACTIVE = "active";
    private static final String COLUMN_ALARM_MINUTE = "minute";
    private static final String COLUMN_ALARM_HOUR = "hour";
    private static final String COLUMN_ALARM_AUTO_RENEW = "renew";
    private static final String COLUMN_ALARM_VIBRATE_ONLY = "vibrate";
    private static final String COLUMN_ALARM_CREATION_DATE = "creation";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "AlarmList.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    ExerciseListHelper helper;

    public static ReminderDatabase getInstance(Context c) {
        if (mInstance == null) {
            mInstance = new ReminderDatabase(c);
        }
        return mInstance;

    }

    private ReminderDatabase(Context c) {
        helper = new ExerciseListHelper(c);
    }

    public long insertData(Object data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Reminder item = (Reminder) data;
        //Gson gson = new Gson();
        ContentValues val = new ContentValues();

        val.put(COLUMN_ALARM_TITLE, item.getAlarmTitle());
        val.put(COLUMN_ALARM_MINUTE, item.getMinute());
        val.put(COLUMN_ALARM_HOUR, item.getHourOfDay());
        val.put(COLUMN_ALARM_ACTIVE, Boolean.toString(item.isActive()));
        val.put(COLUMN_ALARM_VIBRATE_ONLY, Boolean.toString(item.isVibrateOnly()));
        val.put(COLUMN_ALARM_AUTO_RENEW, Boolean.toString(item.isRenewAutomatically()));
        val.put(COLUMN_ALARM_CREATION_DATE, item.getCreationDate());


        long id = db.insert(TABLE_NAME, null, val);
        db.close();
        return id;
    }

    public long insertOrUpdateData(Object data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Reminder item = (Reminder) data;

        //Gson gson = new Gson();
        //create a ContentValues (like Java Map class), and populate with Key Value pairs. Key is the column name
        ContentValues val = new ContentValues();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                if (c.getInt(c.getColumnIndex(COLUMN_ALARM_CREATION_DATE))
                        == (item.getCreationDate())
                        ) {//if match occurs, update appropriate entry
                    val.put(COLUMN_ALARM_TITLE, item.getAlarmTitle());
                    val.put(COLUMN_ALARM_MINUTE, item.getMinute());
                    val.put(COLUMN_ALARM_HOUR, item.getHourOfDay());
                    val.put(COLUMN_ALARM_ACTIVE, Boolean.toString(item.isActive()));
                    val.put(COLUMN_ALARM_VIBRATE_ONLY, Boolean.toString(item.isVibrateOnly()));
                    val.put(COLUMN_ALARM_AUTO_RENEW, Boolean.toString(item.isRenewAutomatically()));
                    val.put(COLUMN_ALARM_CREATION_DATE, item.getCreationDate());

                    String selection = COLUMN_ENTRY_ID + " LIKE ?";
                    String[] selectionArgs = {
                            String.valueOf(c.getString(c.getColumnIndex(COLUMN_ENTRY_ID)))
                    };
                    long id = db.update(TABLE_NAME, val, selection, selectionArgs);
                    c.close();
                    db.close();
                    return id;
                }
            } while (c.moveToNext());
        }
        //if no match is found, add a new entry to the DB

        val.put(COLUMN_ALARM_TITLE, item.getAlarmTitle());
        val.put(COLUMN_ALARM_MINUTE, item.getMinute());
        val.put(COLUMN_ALARM_HOUR, item.getHourOfDay());
        val.put(COLUMN_ALARM_ACTIVE, Boolean.toString(item.isActive()));
        val.put(COLUMN_ALARM_VIBRATE_ONLY, Boolean.toString(item.isVibrateOnly()));
        val.put(COLUMN_ALARM_AUTO_RENEW, Boolean.toString(item.isRenewAutomatically()));
        val.put(COLUMN_ALARM_CREATION_DATE, item.getCreationDate());

        if (!c.isClosed()) {
            c.close();
        }
        long id = db.insert(TABLE_NAME, null, val);
        db.close();
        return id;
    }

    public ArrayList getAllDataAsList() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Reminder> result = new ArrayList<>();
        // Gson gson = new Gson();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        int iterator = 0;
        if (c.moveToFirst()) {
            do {
                Reminder item = new Reminder(
                        c.getInt(c.getColumnIndex(COLUMN_ALARM_HOUR)),
                        c.getInt(c.getColumnIndex(COLUMN_ALARM_MINUTE)),
                        c.getString(c.getColumnIndex(COLUMN_ALARM_TITLE)),
                        Boolean.parseBoolean(c.getString(c.getColumnIndex(COLUMN_ALARM_ACTIVE))),
                        Boolean.parseBoolean(c.getString(c.getColumnIndex(COLUMN_ALARM_VIBRATE_ONLY))),
                        Boolean.parseBoolean(c.getString(c.getColumnIndex(COLUMN_ALARM_AUTO_RENEW))),
                        c.getInt(c.getColumnIndex(COLUMN_ALARM_CREATION_DATE))

                );
                result.add(iterator, item);
                iterator++;

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    //TODO set "1" to null once testing is complete
    public long deleteAllData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        long id = db.delete(TABLE_NAME, "1", null);
        return id;
    }

    public long deleteData(Reminder reminder) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int creationDate = reminder.getCreationDate();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (c.getInt(c.getColumnIndex(COLUMN_ALARM_CREATION_DATE))
                        == (creationDate)) {
                    String selection = COLUMN_ENTRY_ID + " LIKE ?";
                    String[] selectionArgs = {
                            String.valueOf(c.getString(c.getColumnIndex(COLUMN_ENTRY_ID)))
                    };

                    long id = db.delete(TABLE_NAME, selection, selectionArgs);
                    c.close();
                    db.close();
                    return id;
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return -1;
    }

    public long toggleAlarmState(Reminder reminder){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (c.getInt(c.getColumnIndex(COLUMN_ALARM_CREATION_DATE))
                        == (reminder.getCreationDate())
                        ) {//if match occurs, update appropriate entry
                    ContentValues val = new ContentValues();

                    val.put(COLUMN_ALARM_ACTIVE, Boolean.toString(reminder.isActive()));

                    String selection = COLUMN_ENTRY_ID + " LIKE ?";
                    String[] selectionArgs = {
                            String.valueOf(c.getString(c.getColumnIndex(COLUMN_ENTRY_ID)))
                    };

                    long id = db.update(TABLE_NAME, val, selection, selectionArgs);
                    c.close();
                    db.close();
                    return id;
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return -1;
    }

    private static class ExerciseListHelper extends SQLiteOpenHelper {
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ALARM_TITLE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_ACTIVE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_MINUTE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_HOUR + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_AUTO_RENEW + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_VIBRATE_ONLY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ALARM_CREATION_DATE + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        Context context;

        public ExerciseListHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
