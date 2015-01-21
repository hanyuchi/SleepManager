package com.example.receiver;

import java.util.Random;

import com.example.administration.MyAdmin;
import com.example.sleepmanager.MainActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;  
import android.content.ComponentName;  
import android.content.Context;  
import android.util.Log;  

public class ExcecutionForcingReceiver extends Activity {
	private WakeUpReceiver timer;
	private int flag = 2;
	private long time = 0;
	static final int RESULT_ENABLE = 1;    
	private DevicePolicyManager deviceManger;  
	private ComponentName compName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		flag = this.getIntent().getIntExtra("option", 0);
		time = this.getIntent().getLongExtra("time", 1);
		
        String ss = String.valueOf(time);
        Log.e("Locking mode!!!", ss);
		
        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);   
        compName = new ComponentName(this, MyAdmin.class);  
		
		if(flag == 1){
			WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
			wifiManager.setWifiEnabled(false);
			timer(time, 1);
		}
		
		else if(flag == 0){
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);  
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);  
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");  
			startActivityForResult(intent, RESULT_ENABLE);  
			
			boolean active = deviceManger.isAdminActive(compName);  
			if (active) {  					
				deviceManger.setPasswordQuality(compName,DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
				deviceManger.setPasswordMinimumLength(compName, 4);
				//set password
				Random rand = new Random();		
				int pw = rand.nextInt(9999) + 1;
				String sss = String.valueOf(pw);
				deviceManger.resetPassword(sss, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);						

				timer(time, 0);
				deviceManger.lockNow();  
			}
		}
		Intent intent = new Intent(ExcecutionForcingReceiver.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.force, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		 switch (requestCode) {  
		 case RESULT_ENABLE:  
			 if (resultCode == Activity.RESULT_OK) {  
				 Log.i("Sleep Maneger", "Lock Screen Operation Enabled!");  
			 } else {  
				 Log.i("Sleep Manager", "FAILED!");
			 }
			 return;  
		 }  
		 super.onActivityResult(requestCode, resultCode, data);  
	}*/
	 
	public void timer(long diff, int flag) {
		Context context = this.getApplicationContext();
		timer = new WakeUpReceiver();
		     //Context context = AlarmSetting.this;
		if(timer != null) {
			timer.wakeUp(context, diff, flag);
		} else {
			Toast.makeText(context, "Timer is null", Toast.LENGTH_SHORT).show();
		}
	}
	
}