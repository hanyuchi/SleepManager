package com.example.sleepmanager;

import java.util.Calendar;

import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;
import com.example.receiver.AlarmManagerBroadcastReceiver;
import com.example.settings.AlarmSetting;
import com.example.time.DigitalClock;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View.OnClickListener;

public class Alarm extends ActionBarActivity {
	
	private final String ALARM_ID = "alarmID";
	private final String ACTION = "action";
	private static int count = 0;
	private RelativeLayout relativeLayout;
	private DigitalClock dc;
	private Button button;
	private Button prevButton;
	private DatabaseHandler db = new DatabaseHandler(this);
	private AlarmManagerBroadcastReceiver alarmReceiver = new AlarmManagerBroadcastReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepmanager_alarm);
		
		count = db.getAlarmsCount();
		
		// Creating a new RelativeLayout
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);        

		if(count == 0) {
			db.addAlarm(new AlarmData());
			
			AlarmData t = db.getAlarm(0);
			int hour = t.getHour();
			int minute = t.getMinute();
			int dayOfWeek = t.getDayOfWeek();
			dc = (DigitalClock) findViewById(R.id.digitalClock1);
			dc.setTime(hour, minute);
			
			button = (Button) findViewById(R.id.button1);
			button.setText(dayOfWeekIntToString(dayOfWeek));
			button.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
					Intent intent = new Intent(Alarm.this, AlarmSetting.class);
					intent.putExtra(ACTION, "set");
					intent.putExtra(ALARM_ID, 0);
					startActivity(intent);
					finish();
				}
			});
			ToggleButton tb = (ToggleButton) findViewById(R.id.toggle1);
			tb.setTextOn("ON");
			tb.setTextOff("OFF");
			if(db.getAlarm(0).isToggle() == true) {
				tb.setChecked(true);
			} else {
				tb.setChecked(false);
			}
			tb.setOnCheckedChangeListener(new MyOnCheckedChangeListener(0));
		} else {
			for(int i = 0; i < count; i++) {
				// Gets Database time
				AlarmData t = db.getAlarm(i);
				int hour = t.getHour();
				int minute = t.getMinute();
				int dayOfWeek = t.getDayOfWeek();
				
				if(i == 0) {					
					dc = (DigitalClock) findViewById(R.id.digitalClock1);
					dc.setTime(hour, minute);
					
					button = (Button) findViewById(R.id.button1);
					button.setText(dayOfWeekIntToString(dayOfWeek));
					button.setOnClickListener(new OnClickListener() {			
						public void onClick(View v) {
							Intent intent = new Intent(Alarm.this, AlarmSetting.class);
							intent.putExtra(ACTION, "set");
							intent.putExtra(ALARM_ID, 0);
							startActivity(intent);
							finish();
						}
					});
					prevButton = (Button) findViewById(R.id.button1);
					ToggleButton tb = (ToggleButton) findViewById(R.id.toggle1);
					tb.setTextOn("ON");
					tb.setTextOff("OFF");
					if(db.getAlarm(0).isToggle() == true) {
						tb.setChecked(true);
					} else {
						tb.setChecked(false);
					}					
					tb.setOnCheckedChangeListener(new MyOnCheckedChangeListener(0));
				} else {
					// Defining the RelativeLayout layout parameters
			        RelativeLayout.LayoutParams rlpButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);		
			        rlpButton.addRule(RelativeLayout.BELOW, prevButton.getId());			        
					// Adds Button
					button = new Button(this);				
					button.setId(View.generateViewId());
					//button.setBackgroundColor(Color.WHITE);
					button.setBackgroundResource(R.drawable.alarmsetting);
					button.setHeight(dipToPixels(this, 120));
					button.setText(dayOfWeekIntToString(dayOfWeek));
					button.setTextSize(18);
					relativeLayout.addView(button, rlpButton);					
					//button.setLayoutParams(rlpButton);
					button.setOnClickListener(new MyButtonOnClickListener(i));
					prevButton = button;
					
					// Defining the RelativeLayout layout parameters
					RelativeLayout.LayoutParams rlpClock = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);		
			        rlpClock.addRule(RelativeLayout.ALIGN_BASELINE, button.getId());
			        rlpClock.leftMargin = dipToPixels(this, 30);			        
					// Adds DigitalClock
					dc = new DigitalClock(this);
					dc.setTime(hour, minute);
					dc.setId(View.generateViewId());
					dc.setTextSize(50);
					relativeLayout.addView(dc, rlpClock);					
					
					// Defining the RelativeLayout layout parameters
					RelativeLayout.LayoutParams rlpToggle = new RelativeLayout.LayoutParams(dipToPixels(this, 80), dipToPixels(this, 60));		
			        rlpToggle.addRule(RelativeLayout.ALIGN_BASELINE, button.getId());
			        rlpToggle.addRule(RelativeLayout.ALIGN_RIGHT, button.getId());
			        rlpToggle.rightMargin = dipToPixels(this, 30);
					// Adds Toggle
			        ToggleButton tb = new ToggleButton(this);
					tb.setBackgroundResource(R.drawable.toggle);
					tb.setId(View.generateViewId());
					tb.setTextSize(20);
					tb.setTextOn("ON");
					tb.setTextOff("OFF");
					if(db.getAlarm(i).isToggle() == true) {
						tb.setChecked(true);
					} else {
						tb.setChecked(false);
					}					
					relativeLayout.addView(tb, rlpToggle);
					tb.setOnCheckedChangeListener(new MyOnCheckedChangeListener(i));
				}						
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}
	
	/**
     * On selecting action bar icons
     * */
    @Override
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
        case R.id.action_add:
            // add action
        	addAction();
            return true;
        case R.id.action_help:
            // help action
            return true;        
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void addAction() {    	
    	Intent intent = new Intent(Alarm.this, AlarmSetting.class);
    	intent.putExtra(ACTION, "add");
    	intent.putExtra(ALARM_ID, count);
    	startActivity(intent);
    	finish();
    }
    
    /**
     * Launching new activity
     * */
    private void BackHome() {
        Intent i = new Intent(Alarm.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    
    public void onBackPressed(){
       Intent intent = new Intent(this, MainActivity.class);    
       startActivity(intent);   
       finish();
    }
    
    private int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    
    private String dayOfWeekIntToString(int i) {
    	switch(i) {
    	case 0: return "All";
    	case 1: return "Sun";
    	case 2: return "Mon";
    	case 3: return "Tue";
    	case 4: return "Wed";
    	case 5: return "Thu";
    	case 6: return "Fri";
    	case 7: return "Sat";
    	default: return "ERROR";
    	}
    }
    
    private class MyButtonOnClickListener implements OnClickListener {
      int id;
      public MyButtonOnClickListener(int id) {
    	  this.id = id;
      }

      @Override
      public void onClick(View v) {
    	  Intent intent = new Intent(Alarm.this, AlarmSetting.class);
    	  intent.putExtra(ACTION, "set");
    	  intent.putExtra(ALARM_ID, id);
    	  startActivity(intent);
    	  finish();
      }
    }
    
    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        int id;
    	
        public MyOnCheckedChangeListener(int id) {
      	  	this.id = id;
        }

        @Override        
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	AlarmData ad = db.getAlarm(id);
        	if (isChecked) {
	            // The toggle is enabled
				ad.setToggle(true);
				// Updates receiver request
				Calendar calNow = Calendar.getInstance();
			    Calendar calSet = (Calendar) calNow.clone();			    
			    
			    calSet.set(Calendar.HOUR_OF_DAY, ad.getHour());
			    calSet.set(Calendar.MINUTE, ad.getMinute());
			    calSet.set(Calendar.SECOND, 0);
			    calSet.set(Calendar.MILLISECOND, 0);
			    
			    int dayOfWeek = ad.getDayOfWeek();
			    if(dayOfWeek != 0) {	// One-time alarm
			    	int diff = (dayOfWeek - calNow.get(Calendar.DAY_OF_WEEK) + 7) % 7;
			    	if(diff == 0) {
			    		if(calSet.compareTo(calNow) <= 0){
					    	//Today Set time passed, count to tomorrow
					    	calSet.add(Calendar.DATE, 7);
					    }					    				    
			    	} else {
			    		calSet.add(Calendar.DATE, diff);
			    	}
			    } else {		// Do not care day of week
			    	if(calSet.compareTo(calNow) <= 0){
				    	//Today Set time passed, count to tomorrow
				    	calSet.add(Calendar.DATE, 1);
				    }
			    }	    
			    onetimeTimer(calSet.getTimeInMillis() - calNow.getTimeInMillis(), ad.getReqCode());
	        } else {
	        	// The toggle is disabled
    			ad.setToggle(false);
    			cancelTimer(ad.getReqCode());
	        }
        	db.updateAlarm(ad);
	    }
    }
    
    public void onetimeTimer(long diff, int reqCode) {
	     Context context = this.getApplicationContext();
	     //Context context = AlarmSetting.this;
	     if(alarmReceiver != null) {
	    	 alarmReceiver.setOnetimeTimer(context, diff, reqCode);
	     } else {
	    	 Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	     }
	}
	 
	public void startRepeatingTimer(int milliseconds, int reqCode) {
	     Context context = this.getApplicationContext();
	     if(alarmReceiver != null){
	    	 alarmReceiver.SetAlarm(context, milliseconds, reqCode);
	     } else {
	    	 Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	     }
	}
	     
	public void cancelTimer(int reqCode) {
	     Context context = this.getApplicationContext();
	     if(alarmReceiver != null) {
	    	 alarmReceiver.CancelAlarm(context, reqCode);
	     } else {
	    	 Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	     }
	}
}