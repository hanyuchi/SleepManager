package com.example.settings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;
import com.example.receiver.AlarmManagerBroadcastReceiver;
import com.example.sleepmanager.Alarm;
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

/* ReqCode == 0 + alarmID */
public class AlarmSetting extends ActionBarActivity {	
	
	private final String ALARM_ID = "alarmID";
	private final String ACTION = "action";
	private static Spinner s1, s2, s3;
	private TimePicker timePicker;
	
	private AlarmManagerBroadcastReceiver alarmReceiver = new AlarmManagerBroadcastReceiver();
	private final DatabaseHandler db = new DatabaseHandler(this);
	private int alarmID = 0;
	private String action;
	private AlarmData tmpAlarm = new AlarmData();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_alarm_setting);
		
		Intent intent = getIntent();
		action = intent.getExtras().getString(ACTION);
		alarmID = intent.getIntExtra(ALARM_ID, 0);
				
		timePicker = (TimePicker)findViewById (R.id.timePicker1);
		if(action.equals("set")) {
			tmpAlarm = db.getAlarm(alarmID);
			timePicker.setCurrentHour(db.getAlarm(alarmID).getHour());
			timePicker.setCurrentMinute(db.getAlarm(alarmID).getMinute());			
			//Log.d("SET", action);
		} else if(action.equals("add")) {
			tmpAlarm.setId(alarmID);
			timePicker.setCurrentHour(0);
			timePicker.setCurrentMinute(0);			
			//Log.d("ADD", action);
		}		
		//Log.d("NEITHER", action);
		addDayOnSpinner();
		addRingOnSpinner();
		addCaptchaOnSpinner();
		
		Button delete = (Button) findViewById(R.id.alarmsettingdelete);
		delete.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if(action.equals("set")) {
					db.deleteAlarm(db.getAlarm(alarmID));
					updateDatabase();
				}
				cancelTimer(db.getAlarm(alarmID).getReqCode());
				Intent intent = new Intent(AlarmSetting.this, Alarm.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button save = (Button) findViewById(R.id.alarmsettingsave);
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
			    
			    int dayOfWeek = tmpAlarm.getDayOfWeek();
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
			    
			    tmpAlarm.setHour(hour);
			    tmpAlarm.setMinute(minute);
			    tmpAlarm.setToggle(true);			    
			    
			    if(action.equals("set")) {			    	
			    	db.updateAlarm(tmpAlarm);
			    } else if(action.equals("add")) {
			    	tmpAlarm.setReqCode(db.getAlarm(alarmID-1).getReqCode()+1);
			    	db.addAlarm(tmpAlarm);
			    }
			    onetimeTimer(calSet.getTimeInMillis() - calNow.getTimeInMillis(), tmpAlarm.getReqCode());
			    Log.d("ADIFF", String.valueOf(calSet.getTimeInMillis() - calNow.getTimeInMillis()));
				Intent intent = new Intent(AlarmSetting.this, Alarm.class);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_setting, menu);
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
            return true;        
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
    
    /**
     * Launching new activity
     * */
    private void BackHome() {
        Intent i = new Intent(AlarmSetting.this, MainActivity.class);
        startActivity(i);
        finish();
    }

	public void addDayOnSpinner() {
		s1 = (Spinner) findViewById(R.id.daychoose);
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
		if(action.equals("set")) {
			s1.setSelection(dataAdapter.getPosition(dayOfWeekIntToStr(db.getAlarm(alarmID).getDayOfWeek())));
		}
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String dayOfWeek = parent.getSelectedItem().toString();
		        tmpAlarm.setDayOfWeek(dayOfWeekStrToInt(dayOfWeek));		        
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
	public void addRingOnSpinner() {
		s2 = (Spinner) findViewById(R.id.ringchoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Lemon Tree");
		list.add("All of Me");
		list.add("Dark Horse");		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s2.setAdapter(dataAdapter);
		if(action.equals("set")) {
			s2.setSelection(dataAdapter.getPosition(ringtoneIntToStr(db.getAlarm(alarmID).getRingtone())));
		}
		s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String ringtone = parent.getSelectedItem().toString();
		        tmpAlarm.setRingtone(ringtoneStrToInt(ringtone));		        
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
	public void addCaptchaOnSpinner() {
		s3 = (Spinner) findViewById(R.id.captchachoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("QR Code");
		list.add("Shake");
		list.add("Math Problem");		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		s3.setAdapter(dataAdapter);
		if(action.equals("set")) {
			s3.setSelection(dataAdapter.getPosition(captchaIntToStr(db.getAlarm(alarmID).getCaptcha())));
		}		
		s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String captcha = parent.getSelectedItem().toString();
		        tmpAlarm.setCaptcha(captchaStrToInt(captcha));		        
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
    public void onBackPressed() {
        Intent intent = new Intent(this, Alarm.class);    
        startActivity(intent); 
        finish();
    }
    
    /* IMPORTANT when deleting in set mode */
    private void updateDatabase() {
    	for(int i = alarmID + 1; i <= db.getAlarmsCount(); i++) {
    		AlarmData ad = db.getAlarm(i);
    		ad.setId(i-1);
    		db.addAlarm(ad);
    		db.deleteAlarm(db.getAlarm(i));
    	}
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
    
    private int captchaStrToInt(String s) {
    	switch(s) {
    	case "QR Code": return 0;
    	case "Shake": return 1;
    	case "Math Problem": return 2;    		
    	default: return 0;
    	}
    }
    
    private String captchaIntToStr(int i) {
    	switch(i) {
    	case 0: return "QR Code";
    	case 1: return "Shake";
    	case 2: return "Math Problem";    		
    	default: return "QR Code";
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