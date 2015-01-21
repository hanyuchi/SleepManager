package com.example.alert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;

public class NotifyAlarmReceivedActivity extends Activity {
	
	private int ringtone;
	private int captcha;
	private int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_notify_alarm_received);//?
		//ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        //ringtone.play();
		AssetFileDescriptor afd;
		Intent intent = getIntent();
		ringtone = intent.getIntExtra("ringtone", 0);
		captcha = intent.getIntExtra("captcha", 0);
		i = intent.getIntExtra("i", 0);
		//Log.d("RINGTONE", String.valueOf(ringtone));
		try {
			afd = getAssets().openFd(ringtoneIntToStr(ringtone));
			MyRingtoneManager.SoundPlayer(afd);
			afd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
		alarmDialog();		
	}
	    
    private void alarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Calendar cal = Calendar.getInstance();        
        SimpleDateFormat format = new SimpleDateFormat("HH:mm E");
        System.out.println(cal.getTime());
        
        builder.setMessage("Alarm: " + format.format(cal.getTime())).setCancelable(
            false).setPositiveButton("Snooze", new MyPositiveOnClickListener(
            ringtone, captcha, i)).setNegativeButton("Stop", new MyNegativeOnClickListener(captcha, i));
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private String ringtoneIntToStr(int i) {
    	switch(i) {
    	case 0: return "lemon_tree.mp3";
    	case 1: return "all_of_me.mp3";
    	case 2: return "dark_horse.mp3";
    	default: return "";
    	}
    }
    
    private class MyPositiveOnClickListener implements DialogInterface.OnClickListener {
        int ringtone;
    	int captcha;
    	int i;
    	
        public MyPositiveOnClickListener(int ringtone, int captcha, int i) {
        	this.ringtone = ringtone;
      	  	this.captcha = captcha;
      	  	this.i = i;
        }

        @Override
        public void onClick(DialogInterface dialog, int id) {
        	//MyRingtoneManager.player.stop();
        	MyRingtoneManager.player.release();
        	dialog.cancel();
            Intent intent = new Intent(NotifyAlarmReceivedActivity.this, SnoozeActivity.class);
            intent.putExtra("ringtone", ringtone);
            intent.putExtra("captcha", captcha);
            intent.putExtra("i", i);
			startActivity(intent);
			finish();
        }
    }
    
    private class MyNegativeOnClickListener implements DialogInterface.OnClickListener {    	
    	int captcha;
    	int i;
    	
        public MyNegativeOnClickListener(int captcha, int i) {
      	  	this.captcha = captcha;
      	  	this.i = i;
        }

        @Override
        public void onClick(DialogInterface dialog, int id) {
        	//ringtone.stop();
            dialog.cancel();
            if(captcha == 2) {
            	Intent intent = new Intent(NotifyAlarmReceivedActivity.this, MathProblemActivity.class);
            	intent.putExtra("i", i);
    			startActivity(intent);
    			finish();
            } else {
            	Intent intent = new Intent(NotifyAlarmReceivedActivity.this, ShakeActivity.class);
            	intent.putExtra("i", i);
    			startActivity(intent);
    			finish();
            }            
        }
    }
}
