package com.example.dblayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "sleep_manager";
 
    // Table name
    private static final String TABLE_ALARM = "alarm";
    private static final String TABLE_SLEEP_TIME = "sleep_time";
    private static final String TABLE_TRACK_TIME = "track_time";
    private static final String TABLE_TRACK_DATA = "track_data";
 
    // Table Alarm Columns names
    private static final String ID = "id";
    private static final String Hour = "hour";
    private static final String Minute = "minute";
    private static final String Second = "second";
    private static final String DayOfWeek = "day_of_week";
    private static final String Ringtone = "ringtone";
    private static final String Captcha = "captcha";
    private static final String Toggle = "toggle";
    private static final String ReqCode = "req_code";
    // Table Sleep_time Columns names
    private static final String ForceToggle = "force_toggle";
    private static final String WakeupTime = "wakeup_time";
    private static final String LockMode = "lock_mode";
    // Table Track_time Columns names
    private static final String Date = "date";
    private static final String BeginHour = "begin_hour";
    private static final String BeginMinute = "begin_minute";
    private static final String EndHour = "end_hour";
    private static final String EndMinute = "end_minute";
    // Table Track_time Columns names
    private static final String Acceleration = "acceleration";
    
    private final SimpleDateFormat dateParserFormate = new SimpleDateFormat("yyyy-MM-dd");   
     
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_ALARM + "("
                + ID + " INT PRIMARY KEY, " + Hour + " INT, " + Minute + " INT, " 
                + Second + " INT, " + DayOfWeek + " INT, " + Ringtone + " INT, " 
                + Captcha + " INT, " + Toggle + " BOOLEAN, " + ReqCode + " INT)";
        String CREATE_SLEEP_TIME_TABLE = "CREATE TABLE " + TABLE_SLEEP_TIME + "("
                + ID + " INT PRIMARY KEY, " + Hour + " INT, " + Minute + " INT, " 
                + Second + " INT, " + DayOfWeek + " INT, " + Ringtone + " INT, " 
                + Toggle + " BOOLEAN, " + WakeupTime + " INT, " + LockMode + " INT, "
                + ForceToggle + " BOOLEAN)";
        String CREATE_TRACK_TIME_TABLE = "CREATE TABLE " + TABLE_TRACK_TIME + "("
                + ID + " INT PRIMARY KEY, " + Date + " DATE, " + BeginHour + " INT, " 
        		+ BeginMinute + " INT, " + EndHour + " INT, " + EndMinute + " INT, " 
                + Toggle + " BOOLEAN)";
        String CREATE_TRACK_DATA_TABLE = "CREATE TABLE " + TABLE_TRACK_DATA + "("
                + ID + " INT, " + Hour + " INT, " + Minute + " INT, " 
                + Second + " INT, " + Acceleration + " FLOAT)";
        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_SLEEP_TIME_TABLE);
        db.execSQL(CREATE_TRACK_TIME_TABLE);
        db.execSQL(CREATE_TRACK_DATA_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK_DATA);
 
        // Create tables again
        onCreate(db);
    }
    
    /*********************************************************
     * ********************** ADD ****************************
     * *******************************************************/
    // Adding new alarm
    public void addAlarm(AlarmData alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(ID, alarm.getId());
        values.put(Hour, alarm.getHour());
        values.put(Minute, alarm.getMinute());
        values.put(Second, alarm.getSecond());
        values.put(DayOfWeek, alarm.getDayOfWeek());
        values.put(Ringtone, alarm.getRingtone());
        values.put(Captcha, alarm.getCaptcha());
        values.put(Toggle, alarm.isToggle());
        values.put(ReqCode, alarm.getReqCode());
     
        // Inserting Row
        db.insert(TABLE_ALARM, null, values);
        db.close(); // Closing database connection
    }
    
    // Adding new sleep time
    public void addSleepTime(SleepTimeData sleepTime) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(ID, sleepTime.getId());
        values.put(Hour, sleepTime.getHour());
        values.put(Minute, sleepTime.getMinute());
        values.put(Second, sleepTime.getSecond());
        values.put(DayOfWeek, sleepTime.getDayOfWeek()); 
        values.put(Ringtone, sleepTime.getRingtone());
        values.put(Toggle, sleepTime.isToggle());
        values.put(WakeupTime, sleepTime.getWakeupTime());
        values.put(LockMode, sleepTime.getLockMode());
        values.put(ForceToggle, sleepTime.isForceToggle());
     
        // Inserting Row
        db.insert(TABLE_SLEEP_TIME, null, values);
        db.close(); // Closing database connection
    }
    
    // Adding new track time
    public void addTrackTime(TrackTimeData trackTime) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(ID, trackTime.getId());
        values.put(Date, dateParserFormate.format(Calendar.getInstance().getTime()));
        values.put(BeginHour, trackTime.getBeginHour());
        values.put(BeginMinute, trackTime.getBeginMinute());
        values.put(EndHour, trackTime.getEndHour());
        values.put(EndMinute, trackTime.getEndMinute());        
        values.put(Toggle, trackTime.isToggle());
     
        // Inserting Row
        db.insert(TABLE_TRACK_TIME, null, values);
        db.close(); // Closing database connection
    }
    
    // Adding new alarm
    public void addTrackData(TrackDataData trackData) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(ID, trackData.getId());
        values.put(Hour, trackData.getHour());
        values.put(Minute, trackData.getMinute());
        values.put(Second, trackData.getSecond());
        values.put(Acceleration, trackData.getAcceleration());        
     
        // Inserting Row
        db.insert(TABLE_TRACK_DATA, null, values);
        db.close(); // Closing database connection
    }
    
    /*********************************************************
     * ********************** GET ****************************
     * *******************************************************/    
    // Getting single alarm
    public AlarmData getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_ALARM, new String[] {ID, Hour, Minute, Second, DayOfWeek, Ringtone, Captcha, Toggle, ReqCode}, ID + "=?",
        		new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
     
        AlarmData alarm = new AlarmData(cursor.getInt(0),
        		cursor.getInt(1),
        		cursor.getInt(2),
        		cursor.getInt(3),
        		cursor.getInt(4),
        		cursor.getInt(5),
        		cursor.getInt(6),
        		cursor.getInt(7) > 0,
        		cursor.getInt(8));
        
        db.close();       
        // return student
        return alarm;
    }
    
    // Getting single sleep time
    public SleepTimeData getSleepTime(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_SLEEP_TIME, new String[] {ID, Hour, Minute, Second, DayOfWeek, Ringtone, Toggle, WakeupTime, LockMode, ForceToggle}, ID + "=?",
        		new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
     
        SleepTimeData sleepTime = new SleepTimeData(cursor.getInt(0),
        		cursor.getInt(1),
        		cursor.getInt(2),
        		cursor.getInt(3),
        		cursor.getInt(4),
        		cursor.getInt(5),
        		cursor.getInt(6) > 0,        		
        		cursor.getInt(7),
        		cursor.getInt(8),
        		cursor.getInt(9) > 0);        
        
        db.close();       
        // return student
        return sleepTime;
    }
    
	// Getting single track time
    public TrackTimeData getTrackTime(int id) throws NumberFormatException, ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_TRACK_TIME, new String[] {ID, Date, BeginHour, BeginMinute, EndHour, EndMinute, Toggle}, ID + "=?",
        		new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
     
        TrackTimeData trackTime = new TrackTimeData(cursor.getInt(0),
        		dateParserFormate.parse(cursor.getString(1)),
        		cursor.getInt(2),
        		cursor.getInt(3),
        		cursor.getInt(4),
        		cursor.getInt(5),
        		cursor.getInt(6) > 0);
        
        //Log.d("Database date", String.valueOf(dateParserFormate.parse(cursor.getString(1))));
        //Log.d("Database boolean", String.valueOf(cursor.getInt(6) > 0));
        db.close();       
        // return student
        return trackTime;
    }
    
    // Getting track data
    public TrackDataData getTrackData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_TRACK_DATA, new String[] {ID, Hour, Minute, Second, Acceleration}, ID + "=?",
        		new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
     
        TrackDataData trackData = new TrackDataData(cursor.getInt(0),
        		cursor.getInt(1),
        		cursor.getInt(2),
        		cursor.getInt(3),
        		cursor.getFloat(4));
        
        db.close();       
        // return student
        return trackData;
    }
    
    /*********************************************************
     * ****************** GET ALL ****************************
     * *******************************************************/ 
    // Getting all alarms
    public List<AlarmData> getAllAlarms() {
       List<AlarmData> alarmList = new ArrayList<AlarmData>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_ALARM;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               AlarmData alarm = new AlarmData();
               alarm.setId(cursor.getInt(0));               
               alarm.setHour(cursor.getInt(1));
               alarm.setMinute(cursor.getInt(2));
               alarm.setSecond(cursor.getInt(3));                   
               alarm.setDayOfWeek(cursor.getInt(4));
               alarm.setRingtone(cursor.getInt(5));
               alarm.setCaptcha(cursor.getInt(6));
               alarm.setToggle(cursor.getInt(7) > 0);
               alarm.setReqCode(8);
               // Adding student to list
               alarmList.add(alarm);
           } while (cursor.moveToNext());
       }
       
       cursor.close();
       db.close();
       // return time list
       return alarmList;
    }
    
    // Getting All sleep time
    public List<SleepTimeData> getAllSleepTime() {
       List<SleepTimeData> sleepTimeList = new ArrayList<SleepTimeData>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_SLEEP_TIME;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               SleepTimeData sleepTime = new SleepTimeData();
               sleepTime.setId(cursor.getInt(0));               
               sleepTime.setHour(cursor.getInt(1));
               sleepTime.setMinute(cursor.getInt(2));
               sleepTime.setSecond(cursor.getInt(3));                   
               sleepTime.setDayOfWeek(cursor.getInt(4));
               sleepTime.setRingtone(cursor.getInt(5));
               sleepTime.setToggle(cursor.getInt(6) > 0);
               sleepTime.setWakeupTime(cursor.getInt(7));
               sleepTime.setLockMode(cursor.getInt(8));
               sleepTime.setForceToggle(cursor.getInt(9) > 0);
               // Adding student to list
               sleepTimeList.add(sleepTime);
           } while (cursor.moveToNext());
       }
       
       cursor.close();
       db.close();
       // return time list
       return sleepTimeList;
    }
    
    // Getting All Track times
    public List<TrackTimeData> getAllTrackTime() throws ParseException {
       List<TrackTimeData> trackTimeList = new ArrayList<TrackTimeData>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_TRACK_TIME;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               TrackTimeData alarm = new TrackTimeData();
               alarm.setId(cursor.getInt(0));
               alarm.setDate(dateParserFormate.parse(cursor.getString(1))); 
               alarm.setBeginHour(cursor.getInt(2));
               alarm.setBeginMinute(cursor.getInt(3));
               alarm.setEndHour(cursor.getInt(4));                   
               alarm.setEndMinute(cursor.getInt(5));
               alarm.setToggle(cursor.getInt(6) > 0);
               // Adding student to list
               trackTimeList.add(alarm);
           } while (cursor.moveToNext());
       }
       
       cursor.close();
       db.close();
       // return time list
       return trackTimeList;
    }
    
    // Getting All TrackData
    public List<TrackDataData> getAllTrackData() {
       List<TrackDataData> trackDataList = new ArrayList<TrackDataData>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_TRACK_DATA;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               TrackDataData trackData = new TrackDataData();
               trackData.setId(cursor.getInt(0));               
               trackData.setHour(cursor.getInt(1));
               trackData.setMinute(cursor.getInt(2));
               trackData.setSecond(cursor.getInt(3));                   
               trackData.setAcceleration(cursor.getFloat(4));               
               // Adding student to list
               trackDataList.add(trackData);
           } while (cursor.moveToNext());
       }
       
       cursor.close();
       db.close();
       // return time list
       return trackDataList;
    }
   
    /*********************************************************
     * ********************* UPDATE **************************
     * *******************************************************/ 
    // Updating single alarm
    public int updateAlarm(AlarmData alarm) {
        SQLiteDatabase db = this.getWritableDatabase();        
        ContentValues values = new ContentValues();
        values.put(ID, alarm.getId());
        values.put(Hour, alarm.getHour());
        values.put(Minute, alarm.getMinute());
        values.put(Second, alarm.getSecond());
        values.put(DayOfWeek, alarm.getDayOfWeek());
        values.put(Ringtone, alarm.getRingtone());
        values.put(Captcha, alarm.getCaptcha());
        values.put(Toggle, alarm.isToggle());
        values.put(ReqCode, alarm.getReqCode());
 
        // updating row
        int ret = db.update(TABLE_ALARM, values, ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });
        db.close(); // Closing database connection
        return ret;
    }
    
    // Updating single alarm
    public int updateSleepTime(SleepTimeData sleepTime) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ID, sleepTime.getId());
        values.put(Hour, sleepTime.getHour());
        values.put(Minute, sleepTime.getMinute());
        values.put(Second, sleepTime.getSecond());
        values.put(DayOfWeek, sleepTime.getDayOfWeek());
        values.put(Ringtone, sleepTime.getRingtone());
        values.put(Toggle, sleepTime.isToggle());
        values.put(WakeupTime, sleepTime.getWakeupTime());
        values.put(LockMode, sleepTime.getLockMode());
        values.put(ForceToggle, sleepTime.isForceToggle());
 
        // updating row
        int ret = db.update(TABLE_SLEEP_TIME, values, ID + " = ?",
                new String[] { String.valueOf(sleepTime.getId()) });
        db.close(); // Closing database connection
        return ret;
    }
    
    // Updating single track time
    public int updateTrackTime(TrackTimeData trackTime) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ID, trackTime.getId());       
        values.put(Date, dateParserFormate.format(Calendar.getInstance().getTime()));
        values.put(BeginHour, trackTime.getBeginHour());
        values.put(BeginMinute, trackTime.getBeginMinute());
        values.put(EndHour, trackTime.getEndHour());
        values.put(EndMinute, trackTime.getEndMinute());        
        values.put(Toggle, trackTime.isToggle());
 
        // updating row
        int ret = db.update(TABLE_TRACK_TIME, values, ID + " = ?",
                new String[] { String.valueOf(trackTime.getId()) });
        db.close(); // Closing database connection
        return ret;
    }
    
    // Updating single track data
    public int updateTrackData(TrackDataData trackData) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(ID, trackData.getId());
        values.put(Hour, trackData.getHour());
        values.put(Minute, trackData.getMinute());
        values.put(Second, trackData.getSecond());
        values.put(Acceleration, trackData.getAcceleration());
 
        // updating row
        int ret = db.update(TABLE_TRACK_DATA, values, ID + " = ?",
                new String[] { String.valueOf(trackData.getId()) });
        db.close(); // Closing database connection
        return ret;
    }

    /*********************************************************
     * ********************* DELETE **************************
     * *******************************************************/ 
    // Deleting single alarm
    public void deleteAlarm(AlarmData alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });
        db.close();
    }
    
    // Deleting single sleep time
    public void deleteSleepTime(SleepTimeData sleepTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SLEEP_TIME, ID + " = ?",
                new String[] { String.valueOf(sleepTime.getId()) });
        db.close();
    }
    
    // Deleting single track time
    public void deleteTrackTime(TrackTimeData trackTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACK_TIME, ID + " = ?",
                new String[] { String.valueOf(trackTime.getId()) });
        db.close();
    }
    
    // Deleting single track data
    public void deleteTrackData(TrackDataData trackData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACK_DATA, ID + " = ?",
                new String[] { String.valueOf(trackData.getId()) });
        db.close();
    }
    
    /*********************************************************
     * ********************** COUNT ***************************
     * *******************************************************/ 
    // Getting alarms count
    public int getAlarmsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();
 
        db.close();
        // return count
        return res;
    }
    
    // Getting sleep time count
    public int getSleepTimeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SLEEP_TIME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();
 
        db.close();
        // return count
        return res;
    }
    
    // Getting track times Count
    public int getTrackTimeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRACK_TIME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();
 
        db.close();
        // return count
        return res;
    }
    
    // Getting alarms Count
    public int getTrackDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRACK_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();
 
        db.close();
        // return count
        return res;
    }
    
    /**********************************************************************
     * **************************** Track Data ****************************
     * */
    public List<TrackDataData> getAllTrackData(int id) {
        List<TrackDataData> trackDataList = new ArrayList<TrackDataData>();
        // Select All Query
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TRACK_DATA, new String[] {ID, Hour, Minute, Second, Acceleration}, ID + "=?",
        		new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrackDataData trackData = new TrackDataData();
                trackData.setId(cursor.getInt(0));               
                trackData.setHour(cursor.getInt(1));
                trackData.setMinute(cursor.getInt(2));
                trackData.setSecond(cursor.getInt(3));                   
                trackData.setAcceleration(cursor.getFloat(4));               
                // Adding student to list
                trackDataList.add(trackData);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        // return time list
        return trackDataList;
     }
}

	