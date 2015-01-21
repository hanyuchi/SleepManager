package com.example.receiver;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.alert.NotifyAlarmReceivedActivity;
import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
 
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
 
	final public static String ONE_TIME = "onetime";
	final public static String REQUEST_CODE = "request_code";
 
 	@SuppressLint("Wakelock")
	@Override
 	//when wake up, program runs here
 	public void onReceive(Context context, Intent intent) {
 		
 		final DatabaseHandler db = new DatabaseHandler(context);
 		
	 	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();
 
        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
          
        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
        	//Make sure this intent has been sent by the one-time timer button.
        	msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
 
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        
        int reqCode = extras.getInt(REQUEST_CODE);
        Log.d("req_code", String.valueOf(reqCode));
        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int i = 0;
        for( ; i < db.getAlarmsCount(); i++) {
        	AlarmData ad = db.getAlarm(i);
        	if(ad.getReqCode() == reqCode) {
        		break;
        	}
        }
        int ringtone = db.getAlarm(i).getRingtone();
        int captcha = db.getAlarm(i).getCaptcha();
        
        Intent it = new Intent(context, NotifyAlarmReceivedActivity.class);
        it.putExtra("ringtone", ringtone);
        it.putExtra("captcha", captcha);
        it.putExtra("i", i);
 	    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	    //i.putExtra("ring", ringtone);
 	    context.startActivity(it);
 	    
        //Release the lock
        wl.release();
 	}
 
 	public void SetAlarm(Context context, int milliseconds, int reqCode) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * milliseconds , pi); 
    }
 
    public void CancelAlarm(Context context, int reqCode) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
 
    public void setOnetimeTimer(Context context, long diff, int reqCode){
    	AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra(REQUEST_CODE, reqCode);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + diff, pi);
    }
}