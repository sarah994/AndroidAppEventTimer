package com.example.r0462870.eventtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sarah on 26/12/2015.
 */
public class DBEventList {
    public static final String DB_NAME = "eventlist.db";
    public static final int DB_VERSION = 1;

    public static final String LIST_TABLE = "list";
    public static final String LIST_ID = "_id";
    public static final int LIST_ID_COL = 0;
    public static final String LIST_NAME = "list_name";
    public static final int LIST_NAME_COL = 1;

    public static final String EVENT_TABLE = "event";
    public static final String EVENT_ID = "_id";
    public static final int EVENT_ID_COL = 0;
    public static final String EVENT_LIST_ID = "list_id";
    public static final int EVENT_LIST_ID_COL = 1;
    public static final String EVENT_NAME = "event_name";
    public static final int EVENT_NAME_COL = 2;
    public static final String EVENT_NR = "event_nr";
    public static final int EVENT_NR_COL = 3;
    public static final String EVENT_TIME = "event_time";
    public static final int EVENT_TIME_COL = 4;
    public static final String EVENT_WAYPOINT = "event_waypoint";
    public static final int EVENT_WAYPOINT_COL = 5;
    public static final String EVENT_LOCATION = "event_location";
    public static final int EVENT_LOCATION_COL = 6;
    public static final String EVENT_PRE = "event_pre";
    public static final int EVENT_PRE_COL = 7;
    public static final String EVENT_PRELOCATION = "event_pre_location";
    public static final int EVENT_PRELOCATION_COL = 8;
    public static final String EVENT_PREWAYPOINT = "event_pre_waypoint";
    public static final int EVENT_PREWAYPOINT_COL = 9;

    public static final String CREATE_LIST_TABLE = "CREATE TABLE " + LIST_TABLE + " (" +
            LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LIST_NAME + " TEXT NOT NULL UNIQUE);";
    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE + " (" +
            EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_LIST_ID + " INTEGER NOT NULL, " +
            EVENT_NAME + " TEXT NOT NULL, " +
            EVENT_NR + " TEXT NOT NULL, " +
            EVENT_TIME + " TEXT NOT NULL, " +
            EVENT_WAYPOINT + " TEXT NOT NULL, " +
            EVENT_LOCATION + " TEXT NOT NULL, " +
            EVENT_PRE + " TEXT NOT NULL, " +
            EVENT_PRELOCATION + " TEXT NOT NULL, " +
            EVENT_PREWAYPOINT + " TEXT NOT NULL);";
    public static final String DROP_LIST_TABLE = "DROP TABLE IF EXISTS " + LIST_TABLE;
    public static final String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS " + EVENT_TABLE;


    //open and close connection
    //database object en helper object
    private SQLiteDatabase db;
    private DBHelper dbh;

    public DBEventList(Context context){
        dbh = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB(){
        db = dbh.getReadableDatabase();
    }

    private void openWriteableDB(){
        db = dbh.getWritableDatabase();
    }

    private void closeDB(){
        if(db != null)
            db.close();
    }

    //create and upgrade
    private static class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_LIST_TABLE);
            db.execSQL(CREATE_EVENT_TABLE);

            db.execSQL("INSERT INTO list VALUES (1, 'events')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.d("Event list", "upgrading db from version " + oldVersion + " to "+ newVersion);
            db.execSQL(DBEventList.DROP_LIST_TABLE);
            db.execSQL(DBEventList.DROP_EVENT_TABLE);
            onCreate(db);
        }
    }

    public ArrayList<DBEvent> getEvents(String listName){
        String where = EVENT_LIST_ID + "= ?";
        int listID = getList(listName).getId();
        String[] whereArgs = { Integer.toString(listID) };

        this.openReadableDB();
        Cursor cursor = db.query(EVENT_TABLE, null, where, whereArgs, null, null, null);
        ArrayList<DBEvent> dbes = new ArrayList<DBEvent>();
        while(cursor.moveToNext()){
            dbes.add(getEventFromCursor(cursor));
        }
        if(cursor !=null)
            cursor.close();
        this.closeDB();
        return dbes;
    }

    public DBList getList(String name){
        String where = LIST_NAME + "= ?";
        String[] whereArgs = { name };

        this.openReadableDB();
        Cursor cursor = db.query(LIST_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        DBList dbl = getListFromCursor(cursor);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        return dbl;
    }

    private static DBList getListFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try{
                DBList dbl = new DBList(cursor.getString(LIST_NAME_COL), cursor.getInt(LIST_ID_COL));
                return dbl;
            }
            catch (Exception e){
                return null;
            }
        }
    }

    private static DBEvent getEventFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        else{
            try{
                DBEvent dbe = new DBEvent(
                        cursor.getInt(EVENT_ID_COL),
                        cursor.getInt(EVENT_LIST_ID_COL),
                        cursor.getString(EVENT_NAME_COL),
                        cursor.getString(EVENT_NR_COL),
                        cursor.getString(EVENT_TIME_COL),
                        cursor.getString(EVENT_WAYPOINT_COL),
                        cursor.getString(EVENT_LOCATION_COL),
                        cursor.getString(EVENT_PRE_COL),
                        cursor.getString(EVENT_PRELOCATION_COL),
                        cursor.getString(EVENT_PREWAYPOINT_COL));
                return dbe;
            }
            catch (Exception e){
                return null;
            }
        }
    }

    public long insertEvent(DBEvent dbe){
        ContentValues cv = new ContentValues();
        cv.put(EVENT_LIST_ID, dbe.getListId());
        cv.put(EVENT_NAME, dbe.getName());
        cv.put(EVENT_NR, dbe.getNr());
        cv.put(EVENT_TIME , dbe.getEventTime());
        cv.put(EVENT_WAYPOINT , dbe.getWaypoint());
        cv.put(EVENT_LOCATION , dbe.getLocation());
        cv.put(EVENT_PRE , dbe.getPre());
        cv.put(EVENT_PRELOCATION , dbe.getPreLocation());
        cv.put(EVENT_PREWAYPOINT , dbe.getPreWaypoint());

        this.openWriteableDB();
        long rowID = db.insert(EVENT_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

    public int updateEvent(DBEvent dbe){
        ContentValues cv = new ContentValues();
        cv.put(EVENT_LIST_ID, dbe.getListId());
        cv.put(EVENT_NAME, dbe.getName());
        cv.put(EVENT_NR, dbe.getNr());
        cv.put(EVENT_TIME , dbe.getEventTime());
        cv.put(EVENT_WAYPOINT , dbe.getWaypoint());
        cv.put(EVENT_LOCATION , dbe.getLocation());
        cv.put(EVENT_PRE , dbe.getPre());
        cv.put(EVENT_PRELOCATION , dbe.getPreLocation());
        cv.put(EVENT_PREWAYPOINT , dbe.getPreWaypoint());
        String where = EVENT_ID +"= ?";
        String[] whereArgs = {String.valueOf(dbe.getId())};

        this.openWriteableDB();
        int rowCount = db.update(EVENT_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTask(long id){
        String where = EVENT_ID + "= ?";
        String[] whereArgs = { String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(EVENT_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
 }
