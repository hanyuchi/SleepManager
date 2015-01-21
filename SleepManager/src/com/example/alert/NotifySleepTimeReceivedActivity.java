package com.example.alert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.SleepTimeData;
import com.example.receiver.ForceEffectiveReceiver;
import com.example.sleepmanager.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NotifySleepTimeReceivedActivity extends Activity {
	
	private int ringtone;
	private static DatabaseHandler db;
	private ForceEffectiveReceiver timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_notify_alarm_received);//?
		//ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        //ringtone.play();
		AssetFileDescriptor afd;
		Intent intent = getIntent();
		ringtone = intent.getIntExtra("ringtone", 0);
		Log.d("RINGTONE", String.valueOf(ringtone));
		try {
			afd = getAssets().openFd(ringtoneIntToStr(ringtone));
			MyRingtoneManager.SoundPlayer(afd);
			afd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
		sleepTimeDialog();		
	}

    private void sleepTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Calendar cal = Calendar.getInstance();        
        SimpleDateFormat format = new SimpleDateFormat("HH:mm E");
        System.out.println(cal.getTime());
        
        String alartMessage = "";
        db = new DatabaseHandler(this);
        if(db.getSleepTime(0).isForceToggle()) {
        	alartMessage = "\nPlease Notice: One minute after this alarm, Forcing Sleep will be effective!";
        	//get forcing option here
        	//here need to change!!!!!!!!
        	long t = db.getSleepTime(0).getWakeupTime();
        	long lockafter = 60000;
        	timer(lockafter, t);//delete view!!!
        }
        
        builder.setMessage("Alarm: " + format.format(cal.getTime())+alartMessage).setCancelable(
            false).setNegativeButton("Stop", new DialogInterface.OnClickListener(){
            	@Override
                public void onClick(DialogInterface dialog, int id) {
                	//ringtone.stop();
                    dialog.cancel();
                    MyRingtoneManager.player.release();
                    SleepTimeData std = db.getSleepTime(0);
                    std.setToggle(false);
                    db.updateSleepTime(std);
                    Intent intent = new Intent(NotifySleepTimeReceivedActivity.this, MainActivity.class);
        			startActivity(intent);      
        			finish();
                }
            });
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
    
    private void timer(long diff, long time) {
		Context context = this.getApplicationContext();
		timer = new ForceEffectiveReceiver();
		     //Context context = AlarmSetting.this;
		if(timer != null) {
			timer.wakeUp(context, diff, db.getSleepTime(0).getLockMode(), time, 200);
		} else {
			Toast.makeText(context, "Timer is null", Toast.LENGTH_SHORT).show();
		}
	}
}
