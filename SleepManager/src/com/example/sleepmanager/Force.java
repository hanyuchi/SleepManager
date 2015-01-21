package com.example.sleepmanager;

import java.util.ArrayList;

import com.example.administration.MyAdmin;
import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.SleepTimeData;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v7.app.ActionBarActivity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Force extends ActionBarActivity {
	private Spinner s1, s2;
	private final DatabaseHandler db = new DatabaseHandler(this);
	private SleepTimeData tmpSleepTime = new SleepTimeData();
	static final int RESULT_ENABLE = 1;    
	private DevicePolicyManager deviceManger;  
	private ComponentName compName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepmanager_force);
		
        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);   
        compName = new ComponentName(this, MyAdmin.class);  
		
		addDayOnSpinner();
		addLockModeOnSpinner();
		
		tmpSleepTime = db.getSleepTime(0);
		
		Button cancel = (Button) findViewById(R.id.force_cancel);
		cancel.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(Force.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});		
		
		Button save = (Button) findViewById(R.id.force_save);
		save.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				//set forcing sleep effective
				getPolicy();
				
				if(tmpSleepTime.isToggle()) {
					tmpSleepTime.setForceToggle(true);
				}
				db.updateSleepTime(tmpSleepTime);
				
				Intent intent = new Intent(Force.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.force, menu);
		return true;
	}

	public void addDayOnSpinner() {
		s1 = (Spinner) findViewById(R.id.timechoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("1");
		list.add("5");
		list.add("15");
		list.add("30");
		list.add("60");		
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(dataAdapter);
		s1.setSelection(dataAdapter.getPosition(String.valueOf(db.getSleepTime(0).getWakeupTime())));
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        int wakeupTime = Integer.parseInt(parent.getSelectedItem().toString());
		        tmpSleepTime.setWakeupTime(60000*wakeupTime);
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
	}
	
	public void addLockModeOnSpinner() {
		s2 = (Spinner) findViewById(R.id.modechoose);
		ArrayList<String> list = new ArrayList<String>();
		list.add("Lock Screen");
		list.add("Disconnect Wi-fi");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s2.setAdapter(dataAdapter);
		s2.setSelection(dataAdapter.getPosition(lockModeIntToStr(db.getSleepTime(0).getLockMode())));
		s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        String lockMode = parent.getSelectedItem().toString();
		        tmpSleepTime.setLockMode(lockModeStrToInt(lockMode));
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
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
        Intent i = new Intent(Force.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    
    private int lockModeStrToInt(String s) {
    	switch(s) {
    	case "Lock Screen": return 0;
    	case "Disconnect Wi-fi": return 1;    		
    	default: return 0;
    	}
    }
    
    private String lockModeIntToStr(int i) {
    	switch(i) {
    	case 0: return "Lock Screen";
    	case 1: return "Disconnect Wi-fi";
    	default: return "Lock Screen";
    	}
    }
    
    private void getPolicy() {    	
		for(int i = 0; i < 2; i++){	
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);  
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);  
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");  
			startActivityForResult(intent, RESULT_ENABLE);    					    
			
			boolean active = deviceManger.isAdminActive(compName);  
			if (active) {  					
				break;
			}
			else{
				continue;						
			}
		}
    }
}
