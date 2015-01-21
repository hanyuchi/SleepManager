package com.example.alert;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.example.sleepmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class SnoozeActivity extends Activity {

	private TextView textViewTime;
	private int ringtone;
	private int captcha;
    private int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_snooze);
		
		Intent getIntent = getIntent();
		ringtone = getIntent.getIntExtra("ringtone", 0);
		captcha = getIntent.getIntExtra("captcha", 0);
		i = getIntent.getIntExtra("i", 0);

		textViewTime  = (TextView)findViewById(R.id.textViewTime);        

        Random r = new Random();
        int Low = 60000;	// 1 minute
        int High = 180000;	// 3 minutes
        int R = (r.nextInt(High-Low) + Low)/1000;
        
        final CounterClass timer = new CounterClass(R*1000, 1000);
        
        int minute = R/60;
        int second = R - 60 * minute;
        if(second < 10) {
        	textViewTime.setText("00:0" + minute + ":0" + second);
        } else {
        	textViewTime.setText("00:0" + minute + ":" + second);
        }
        
        timer.start();
	}
	
	public class CounterClass extends CountDownTimer {

		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
        public void onFinish() {
			Intent intent = new Intent(SnoozeActivity.this, NotifyAlarmReceivedActivity.class);
			intent.putExtra("ringtone", ringtone);
			intent.putExtra("captcha", captcha);
			intent.putExtra("i", i);
			startActivity(intent);
			finish();
        }

		@Override
		public void onTick(long millisUntilFinished) {

			long millis = millisUntilFinished;
		    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		    System.out.println(hms);

		    textViewTime.setText(hms);
		}
	}
}
