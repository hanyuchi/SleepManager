package com.example.sleepmanager;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.TrackTimeData;
import com.example.receiver.MotionTrackingReceiver;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class Tracking extends ActionBarActivity {
	private static DatabaseHandler db;
	private MotionTrackingReceiver timer;
	private long diff = 0;
	private long starttime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepmanager_tracking);
		
		db = new DatabaseHandler(this);
		int count = db.getTrackTimeCount();
		
		TimePicker start = (TimePicker)findViewById (R.id.starttime);
		TimePicker end = (TimePicker)findViewById (R.id.endtime);
		if(count == 0) {
			try {
				start.setCurrentHour(0);
				start.setCurrentMinute(0);
				end.setCurrentHour(0);
				end.setCurrentMinute(0);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			try {
				start.setCurrentHour(db.getTrackTime(count-1).getBeginHour());
				start.setCurrentMinute(db.getTrackTime(count-1).getBeginMinute());
				end.setCurrentHour(db.getTrackTime(count-1).getEndHour());
				end.setCurrentMinute(db.getTrackTime(count-1).getEndMinute());
			} catch (NumberFormatException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(Tracking.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		//press save button
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				try {
					TimePicker start = (TimePicker)findViewById (R.id.starttime);
					TimePicker end = (TimePicker)findViewById (R.id.endtime);
					int hour1 = start.getCurrentHour();
					int min1 = start.getCurrentMinute();
					int hour2 = end.getCurrentHour();
					int min2 = end.getCurrentMinute();
					
					int dif = 0;
					if(hour1 > hour2){
						dif = hour2 + 24 - hour1;
					} else {
						dif = hour2 - hour1;
					}						
					dif = dif * 60;					
					if(dif == 0) {
						if(min1 != min2) {
							dif = 24 * 60 - min1 + min2;
						}							
					} else {
						dif = dif - min1 + min2;
					}
					
					diff = (long) dif;
					diff = diff * 60000;
					
					Date d = Calendar.getInstance().getTime();
					int trackTimeCount = db.getTrackTimeCount();
					db.addTrackTime(new TrackTimeData(trackTimeCount, d, hour1, min1, hour2, min2, true));
					
			        //system timer
		            Calendar rightNow = Calendar.getInstance();
		            long offset = rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
		            long sinceMidnight = (rightNow.getTimeInMillis() + offset) % (24 * 60 * 60 * 1000);
		            int second = (int) sinceMidnight / 1000;
		            int hour = second / 3600;
		            int min = second / 60 - hour * 60;
		            second = second - hour * 3600 - min * 60;
		            
		            long t1 = (long) hour1 * 3600 + (long) min1 * 60;
		            long t2 = (long) hour * 3600 + (long) min * 60 + second;//system time
		            long end1 = (long) hour2 * 3600 + (long) min2 * 60;
		            
		            if(t1 < t2) {
		            	starttime = t1 - t2 + 24 * 3600;
		            } else {
		            	starttime = t1 - t2;
		            }
		            
		            starttime = starttime * 1000;
		            Log.d("time!!!!!", String.valueOf(starttime));
					timer(starttime, diff, end1, 300);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(Tracking.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	public void timer(long start, long diff, long end, int reqCode) {
		Context context = this.getApplicationContext();
		timer = new MotionTrackingReceiver();
		if(timer != null) {
			timer.wakeUp(context, start, diff, end, reqCode);
		} else {
			Toast.makeText(context, "Timer is null", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tracking, menu);
		return true;
	}
	
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);    
        startActivity(intent);   
        finish();
     }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_settings:
            // settings action
            return true;
        case R.id.action_home:
            // home action
        	BackHome();
            return true;
        case R.id.action_help:
            Intent intent = new Intent(this, Help.class);  
            startActivity(intent);  
            finish();
            return true;        
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Launching new activity
     * */
    private void BackHome() {
        Intent i = new Intent(Tracking.this, MainActivity.class);
        startActivity(i);
    }
}
