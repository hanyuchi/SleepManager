package com.example.chart;

import com.example.sleepmanager.Help;
import com.example.sleepmanager.MainActivity;
import com.example.sleepmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

/**
 * TimeSeriesChartDemo01Activity
 */
public class Chart extends Activity {

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        Bundle b = this.getIntent().getExtras();
        int year = b.getInt("year");
        int month = b.getInt("month");
        int day = b.getInt("day");
        int startId = this.getIntent().getIntExtra("startId", 0);
        int endId = this.getIntent().getIntExtra("endId", 0);

        TimeSeriesChart mView = new TimeSeriesChart(year, month, day, startId, endId, this);
        ll.addView(mView);
        
        setContentView(ll);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
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
        Intent i = new Intent(Chart.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}