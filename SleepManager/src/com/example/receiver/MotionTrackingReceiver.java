package com.example.receiver;

import java.text.ParseException;

import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.TrackTimeData;
import com.example.motiondetector.Motion;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class MotionTrackingReceiver extends BroadcastReceiver {
	static final int RESULT_ENABLE = 1;    
	private ActivityManager activityManager = null;  
	final public static String ONE_TIME = "Lock Screen";
	private static long end = 0;
  
 	@Override 
 	//when wake up, program runs here
 	public void onReceive(Context context, Intent intent) {
        Intent exe = new Intent(context, Motion.class); 
        
        DatabaseHandler db = new DatabaseHandler(context);
        try {
			TrackTimeData ttd = db.getTrackTime(db.getTrackTimeCount()-1);
			ttd.setToggle(false);
			db.updateTrackTime(ttd);
		} catch (NumberFormatException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        exe.putExtra("end", end);
        exe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	    context.startActivity(exe);
 	}

 	@SuppressWarnings("static-access")
	public void wakeUp(Context context, long start, long diff, long end, int reqCode){
 		this.end = end;
    	AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MotionTrackingReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + start, pi);
    }

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	public void setActivityManager(ActivityManager activityManager) {
		this.activityManager = activityManager;
	}
}