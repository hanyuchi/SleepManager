package com.example.alert;

import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;
import com.example.motiondetector.SensorAccelerometer;
import com.example.sleepmanager.MainActivity;
import com.example.sleepmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class ShakeActivity extends Activity {
	public static long mStartTime = 0L;
    private TextView mTimerLabel;
    private Handler mHandler = new Handler();
    SensorAccelerometer acc = null;
    String timerStop1;
    private static DatabaseHandler db;
    private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_shake);
		
		db = new DatabaseHandler(this);		
		Intent intent = getIntent();		
		i = intent.getIntExtra("i", 0);

		mStartTime = SystemClock.uptimeMillis();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);

        //activating the sensor and the acclerometer
		acc = new SensorAccelerometer(this, mTimerLabel, mHandler, mUpdateTimeTask); 
		acc.isShake = true;
	}
	
	private Runnable mUpdateTimeTask = new Runnable(){
        public void run() {
            if(acc.shakeRetval == 1) {
            	MyRingtoneManager.player.release();
            	//Log.d("i", String.valueOf(i));
            	AlarmData ad = db.getAlarm(i);
				ad.setToggle(false);
				db.updateAlarm(ad);
            	acc.unregisterSensor();
            	Intent intent = new Intent(ShakeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
            }
        }  
    };
    
    protected void onPause() {
        super.onPause();
        SensorAccelerometer scc = new SensorAccelerometer(this, mTimerLabel, mHandler, mUpdateTimeTask);
        scc.unregisterSensor();
    };
}
