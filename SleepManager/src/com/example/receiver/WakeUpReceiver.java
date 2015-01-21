package com.example.receiver;

import com.example.administration.MyAdmin;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;
 
public class WakeUpReceiver extends BroadcastReceiver {
	static final int RESULT_ENABLE = 1;    
	private DevicePolicyManager deviceManger;  
	private ActivityManager activityManager = null;  
	private ComponentName compName;
	private static int f = 2;
	final public static String ONE_TIME = "Lock Screen";
 
 	@Override
 	//when wake up, program runs here
 	public void onReceive(Context context, Intent intent) {
 		if(f == 0){
 	        //Toast.makeText(context, "Password Changed", Toast.LENGTH_LONG).show();//show sth. on the screen

 	        deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);  
 	        setActivityManager((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));  
 	        compName = new ComponentName(context, MyAdmin.class); 	        
 			deviceManger.setPasswordQuality(compName,DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
 			deviceManger.setPasswordMinimumLength(compName, 4);
 			//reset password
 			deviceManger.resetPassword("0000", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
 		}
 		
 		else if(f == 1){
 			Toast.makeText(context, "Wifi Turned On", Toast.LENGTH_LONG).show();
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);
 		}
 	}

 	public void wakeUp(Context context, long diff, int flag){
    	f = flag;
    	AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakeUpReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + diff, pi);
    }

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	public void setActivityManager(ActivityManager activityManager) {
		this.activityManager = activityManager;
	}
}