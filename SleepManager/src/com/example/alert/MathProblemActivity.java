package com.example.alert;

import java.util.Random;

import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;
import com.example.sleepmanager.MainActivity;
import com.example.sleepmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MathProblemActivity extends Activity {
	
	private TextView oprand1, oprand2, oprator;
	private Button bt;
	private int op1, op2, op, ans;
	private static DatabaseHandler db;
    private int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_math_problem);
		
		db = new DatabaseHandler(this);		
		Intent intent = getIntent();		
		i = intent.getIntExtra("i", 0);
		
		oprand1 = (TextView) findViewById(R.id.textView2);
		oprand2 = (TextView) findViewById(R.id.textView3);
		oprator = (TextView) findViewById(R.id.textView1);
		
		Random r = new Random();
        int Low = 1;	// 1 minute
        int High = 100;	// 3 minutes
        op1 = (r.nextInt(High-Low) + Low);
        op2 = (r.nextInt(High-Low) + Low);
        op = r.nextInt(3);
        
        oprand1.setText(String.valueOf(op1));
        oprand2.setText(String.valueOf(op2));        
                
        switch(op) {
        case 0: ans = op1 + op2;oprator.setText("+");break;
        case 1: ans = op1 - op2;oprator.setText("-");break;
        case 2: ans = op1 * op2;oprator.setText("*");break;
        default: ans = 0;
        }
        
        bt = (Button) findViewById(R.id.button1);
        bt.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.editText1);
				int myAns = -1;
				try {
					myAns = Integer.parseInt(et.getText().toString());
				} catch (Exception e){
	       			Toast.makeText(getApplicationContext(), "Please enter the answer!", Toast.LENGTH_LONG).show();
	       			e.printStackTrace();
	       		}		
				if(myAns == ans) {
					Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
					MyRingtoneManager.player.release();
					AlarmData ad = db.getAlarm(i);
					ad.setToggle(false);
					db.updateAlarm(ad);
					//Log.d("i", String.valueOf(i));					
					Intent intent = new Intent(MathProblemActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Wrong answer!", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}	
}
