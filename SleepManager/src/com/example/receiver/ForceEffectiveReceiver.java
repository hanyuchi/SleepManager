package com.example.receiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class ForceEffectiveReceiver extends BroadcastReceiver {
	static final int RESULT_ENABLE = 1;    
	private ActivityManager activityManager = null;  
	private static int option = 2;
	final public static String ONE_TIME = "Lock Screen";
	private static long time = 0;
 
 	@Override
 	//when wake up, program runs here
 	public void onReceive(Context context, Intent intent) {
        Intent exe = new Intent(context, ExcecutionForcingReceiver.class);        
        //String s1 = String.valueOf(f);
        //String s2 = String.valueOf(time);
        //Log.e("Time!!!!!", s2);
        exe.putExtra("option", option);
        exe.putExtra("time", time);
        exe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	    context.startActivity(exe);
 	}

 	@SuppressWarnings("static-access")
	public void wakeUp(Context context, long diff, int flag, long time, int reqCode){
    	option = flag;//lock mode
    	this.time = time;//lock time
    	AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ForceEffectiveReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + diff, pi);
    }

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	public void setActivityManager(ActivityManager activityManager) {
		this.activityManager = activityManager;
	}
}