package com.example.motiondetector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemClock;
import android.util.FloatMath;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("FloatMath")
public class SensorAccelerometer implements SensorEventListener{
	double mAccelLast = 0, mAccelCurrent = 0, mAccel = 0;
	private Context context;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView timelabel = null;
    private Handler mHandler;
    Runnable run;
    float record;
    public boolean isShake = false;
    public int shakeCount = 0;
    public int shakeRetval = 0;
    private int counter = 0;

    public SensorAccelerometer(Context context) {
    }

    public SensorAccelerometer(Context context,TextView timelabel, Handler mHandler2, Runnable mUpdateTimeTask) {
        this.context = context;
        this.setTimelabel(timelabel);
        this.mHandler = mHandler2;
        this.run = mUpdateTimeTask;
        initialiseSensor();
    }

    public void initialiseSensor(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensor(){
        sensorManager.unregisterListener(this);
        Toast.makeText(context, "Sensor Stopped..", Toast.LENGTH_SHORT).show();
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
   	    float x = event.values[0];
	    float y = event.values[1];
	    float z = event.values[2];

	    mAccelLast = mAccelCurrent;
	
	    mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
	    double delta = mAccelCurrent - mAccelLast;
	    //mAccel = mAccel * 0.9f + delta;
	
	    delta = Math.abs(delta);
	    //report data 
	    record = (float)delta;
	    
	    if(delta > 5) {
	        Motion.mStartTime = SystemClock.uptimeMillis();
	        mHandler.removeCallbacks(run);
	        mHandler.postDelayed(run, 50);
	    }
	    Log.d("SHAKE", String.valueOf(isShake));
	    if(isShake == true) {
	    	Log.d("SHAKE", String.valueOf(delta));
            //Toast.makeText(context, String.valueOf(delta), Toast.LENGTH_SHORT).show();
            if(delta > 15) {
            	counter++;
            	Toast.makeText(context, String.valueOf(counter), Toast.LENGTH_SHORT).show();
            	if(counter > 9) {
            		shakeRetval = 1;
                    Log.d("GOOD", String.valueOf(delta));
                    Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show();
                    unregisterSensor();
            	}                
            }
        }
    }

	public TextView getTimelabel() {
		return timelabel;
	}

	public void setTimelabel(TextView timelabel) {
		this.timelabel = timelabel;
	}
}
