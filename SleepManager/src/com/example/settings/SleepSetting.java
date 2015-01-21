package com.example.settings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.SleepTimeData;
import com.example.receiver.SleepTimeManagerBroadcastReceiver;
import com.example.sleepmanager.MainActivity;
import com.example.sleepmanager.R;

import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

/* ReqCode == 100 */
public class SleepSetting extends ActionBarActivity {
	private Spinner s1, s2;
	private TimePicker timePicker;
	
	private SleepTimeManagerBroadcastReceiver sleepTimeReceiver = new SleepTimeManagerBroadcastReceiver();
	private final DatabaseHandler db = new DatabaseHandler(this);
	private SleepTimeData tmpSleepTime = new SleepTimeData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_sleep_setting);		
		
		addDayOnSpinner();
		addRingOnSpinner();
		
		tmpSleepTime = db.getSleepTime(0);
		
		timePicker = (TimePicker)findViewById (R.id.sleeptimePicker1);
		timePicker.setCurrentHour(db.getSleepTime(0).getHour());
		timePicker.setCurrentMinute(db.getSleepTime(0).getMinute());
		
		Button delete = (Button) findViewById(R.id.sleepsettingdelete);
		delete.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				db.deleteSleepTime(db.getSleepTime(0));
				cancelTimer(100);
				Intent intent = new Intent(SleepSetting.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button save = (Button) findViewById(R.id.sleepsettingsave);
		save.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Calendar calNow = Calendar.getInstance();
			    Calendar calSet = (Calendar) calNow.clone();			    
			    int hour = timePicker.getCurrentHour();
			    int minute = timePicker.getCurrentMinute();
			    
			    calSet.set(Calendar.HOUR_OF_DAY, hour);
			    calSet.set(Calendar.MINUTE, minute);
			    calSet.set(Calendar.SECOND, 0);
			    calSet.set(Calendar.MILLISECOND, 0);
			    
			    int dayOfWeek = tmpSleepTime.getDayOfWeek();
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
			    
			    tmpSleepTime.setHour(hour);
			    tmpSleepTime.setMinute(minute);
			    tmpSleepTime.setToggle(true);
			    //tmpSleepTime.setForceToggle(true);
			    
			    db.updateSleepTime(tmpSleepTime);
			    onetimeTimer(calSet.getTimeInMillis() - calNow.getTimeInMillis(), 100);
				Intent intent = new Intent(SleepSetting.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	public void onetimeTimer(long diff, int reqCode) {
	     Context context = this.getApplicationContext();
	     //Context context = AlarmSetting.this;
	     if(sleepTimeReceiver != null) {
	    	 sleepTimeReceiver.setOnetimeTimer(context, diff, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}	
	 
	public void startRepeatingTimer(int milliseconds, int reqCode) {
	     Context context = this.getApplicationContext();
	     if(sleepTimeReceiver != null){
	    	 sleepTimeReceiver.SetSleepTime(context, milliseconds, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}
	     
	public void cancelTimer(int reqCode) {
	     Context context = this.getApplicationContext();
	     if(sleepTimeReceiver != null) {
	    	 sleepTimeReceiver.CancelSleepTime(context, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}
	     
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sleep_setting, menu);
		return true;
	}
	
	/**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        //case R.id.action_settings:
            // settings action
            //return true;
        case R.id.action_home:
            // home action
        	BackHome();
            return true;        
        case R.id.action_help:
            // help action
        	Help();
            return true;        
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
    
    /**
     * Launching new activity
     * */
    private void BackHome() {
        Intent i = new Intent(SleepSetting.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    
    private void Help() {
    	Intent intent = new Intent(this, com.example.sleepmanager.Help.class);  
        startActivity(intent); 
        finish();
    }

	public void addDayOnSpinner() {
		s1 = (Spinner) findViewById(R.id.sleepdaychoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Everyday");
		list.add("Monday");
		list.add("Tuesday");
		list.add("Wednesday");
		list.add("Thursday");
		list.add("Friday");
		list.add("Saturday");
		list.add("Sunday");		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(dataAdapter);
		s1.setSelection(dataAdapter.getPosition(dayOfWeekIntToStr(db.getSleepTime(0).getDayOfWeek())));
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String dayOfWeek = parent.getSelectedItem().toString();
		        tmpSleepTime.setDayOfWeek(dayOfWeekStrToInt(dayOfWeek));
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
	public void addRingOnSpinner() {
		s2 = (Spinner) findViewById(R.id.sleepringchoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Lemon Tree");
		list.add("All of Me");
		list.add("Dark Horse");		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s2.setAdapter(dataAdapter);
		s2.setSelection(dataAdapter.getPosition(ringtoneIntToStr(db.getSleepTime(0).getRingtone())));
		s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String ringtone = parent.getSelectedItem().toString();
		        tmpSleepTime.setRingtone(ringtoneStrToInt(ringtone));
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
    
    private int dayOfWeekStrToInt(String s) {
    	switch(s) {
    	case "Everyday": return 0;
    	case "Sunday": return 1;
    	case "Monday": return 2;
    	case "Tuesday": return 3;
    	case "Wednesday": return 4;
    	case "Thursday": return 5;
    	case "Friday": return 6;
    	case "Saturday": return 7;   	
    	default: return 0;
    	}
    }
    
    private String dayOfWeekIntToStr(int i) {
    	switch(i) {
    	case 0: return "Everyday";
    	case 1: return "Sunday";
    	case 2: return "Monday";
    	case 3: return "Tuesday";
    	case 4: return "Wednesday";
    	case 5: return "Thursday";
    	case 6: return "Friday";
    	case 7: return "Saturday";   	
    	default: return "Everyday";
    	}
    }
    
    private int ringtoneStrToInt(String s) {
    	switch(s) {
    	case "Lemon Tree": return 0;
    	case "All of Me": return 1;
    	case "Dark Horse": return 2;    		
    	default: return 0;
    	}
    }
    
    private String ringtoneIntToStr(int i) {
    	switch(i) {
    	case 0: return "Lemon Tree";
    	case 1: return "All of Me";
    	case 2: return "Dark Horse";    		
    	default: return "Lemon Tree";
    	}
    }
}
