package com.example.motiondetector;

import java.text.ParseException;
import java.util.Calendar;

import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.TrackDataData;
import com.example.dblayout.TrackTimeData;
import com.example.sleepmanager.MainActivity;
import com.example.sleepmanager.R;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class Motion extends ActionBarActivity{
    public static long mStartTime = 0L;
    private TextView mTimerLabel;
    private Handler mHandler = new Handler();
    SensorAccelerometer acc = null;
    String timerStop1;
    private DatabaseHandler db = new DatabaseHandler(this);
    private static long end = 0l;
    Activity act = null;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepmanager_motion);

		end = this.getIntent().getLongExtra("end", 0L);
		
        mTimerLabel = (TextView) findViewById(R.id.textView1);
        if(mStartTime == 0L){
            mStartTime = SystemClock.uptimeMillis();
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 100);
            act = this;
            //activating the sensor and the acclerometer
			acc = new SensorAccelerometer(this, mTimerLabel, mHandler, mUpdateTimeTask);
		}
        Intent intent = new Intent(Motion.this, MainActivity.class);
		startActivity(intent);
		finish();
    }

    private Runnable mUpdateTimeTask = new Runnable(){
        public void run() {
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;

            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            mTimerLabel.setText("" + minutes + ":" + String.format("%02d", seconds));                    
            timerStop1 = minutes + ":" + String.format("%02d", seconds);
            mHandler.postDelayed(this, 500);
            
            //system timer
            Calendar rightNow = Calendar.getInstance();
            long offset = rightNow.get(Calendar.ZONE_OFFSET) +
            rightNow.get(Calendar.DST_OFFSET);
            long sinceMidnight = (rightNow.getTimeInMillis() + offset) % (24 * 60 * 60 * 1000);
            int second = (int) sinceMidnight / 1000;
            int hour = second / 3600;
            int min = second / 60 - hour * 60;
            second = second - hour * 3600 - min * 60;
            
            Log.e("end clock: ", String.valueOf(end));
            Log.e("now clock: ", String.valueOf(hour * 3600 + min * 60 + second));
            
            if(end == hour * 3600 + min * 60){
                mHandler.removeCallbacks(mUpdateTimeTask);
                mTimerLabel.setText(timerStop1);
                mStartTime = 0L;
 
                SensorAccelerometer scc = new SensorAccelerometer(act, mTimerLabel, mHandler, mUpdateTimeTask);
                scc.unregisterSensor();
				TrackTimeData tt;
				try {
					tt = db.getTrackTime(0);
					tt.setToggle(false);
					db.updateTrackTime(tt);
				} catch (NumberFormatException | ParseException e) {
					e.printStackTrace();
				}	
				android.os.Process.killProcess(android.os.Process.myPid());
            }
            
            String h = String.valueOf(hour);
            String m = String.valueOf(min);
            String s = String.valueOf(second);
            
            String cc = h + " " + m + " " + s;
            Log.d("clock: ", cc);
            
            if(seconds % 30 == 0) {
            	//get statistics
            	int trackTimeCount = db.getTrackTimeCount();
            	TrackDataData td = new TrackDataData(trackTimeCount-1, hour, min, second, acc.record);
            	db.addTrackData(td);
            	Log.d("Record", String.valueOf(acc.record));
            }
        }  
    };

    protected void onPause() {
        super.onPause();
        SensorAccelerometer scc = new SensorAccelerometer(this, mTimerLabel, mHandler, mUpdateTimeTask);
        scc.unregisterSensor();
    };
}
