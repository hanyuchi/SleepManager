package com.example.sleepmanager;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import com.example.chart.Chart;
import com.example.dblayout.AlarmData;
import com.example.dblayout.DatabaseHandler;
import com.example.dblayout.SleepTimeData;
import com.example.dblayout.TrackTimeData;
import com.example.receiver.SleepTimeManagerBroadcastReceiver;
import com.example.settings.SleepSetting;
import com.example.time.DigitalClock;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {
	
	private static DatabaseHandler db;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private static SleepTimeManagerBroadcastReceiver sleepTimeReceiver = new SleepTimeManagerBroadcastReceiver();

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	int pagenum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleepmanager_activity_main);
		
		db = new DatabaseHandler(this);
		int alarmCount = db.getAlarmsCount();
		int sleepCount = db.getSleepTimeCount();
		if(alarmCount == 0) {
			db.addAlarm(new AlarmData());
		}
		if(sleepCount == 0) {
			db.addSleepTime(new SleepTimeData());
		}
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
		    switch(position) {
	        case 0:
	        	return PlaceholderFragment.newInstance(1);
	        case 1:
	        	return PlaceholderFragment.newInstance(2);
	        case 2:
	        	return PlaceholderFragment.newInstance(3);
	        case 3:
	        	return PlaceholderFragment.newInstance(4);
	        default:
	            return null;
		    }
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			rootView = inflater.inflate(R.layout.sleepmanager_home, container, false);
			
			if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
				AlarmData alarmTime = db.getAlarm(0);
				int alarmHour = alarmTime.getHour();
				int alarmMinute = alarmTime.getMinute();
				//int dayOfWeek = alarmTime.getDayOfWeek();
				// TODO: get the nearest future
				
				SleepTimeData sleepTime = db.getSleepTime(0);
				int sleepTimeHour = sleepTime.getHour();
				int sleepTimeMinute = sleepTime.getMinute();
				
				DigitalClock dc1 = (DigitalClock) rootView.findViewById(R.id.digitalClock1);
				dc1.setTime(alarmHour, alarmMinute);
				
				DigitalClock dc2 = (DigitalClock) rootView.findViewById(R.id.digitalClock2);
				dc2.setTime(sleepTimeHour, sleepTimeMinute);
				
				//final String s1, s2, s3, s4;
				final ToggleButton tb1 = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
				final ToggleButton tb2 = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
				final ToggleButton tb3 = (ToggleButton) rootView.findViewById(R.id.toggleButton3);
				final ToggleButton tb4 = (ToggleButton) rootView.findViewById(R.id.toggleButton4);
				
				//toggle 1
				tb1.setTextOn("ON");
				tb1.setTextOff("OFF");
				if(db.getAlarm(0).isToggle() == true) {
					Log.d("alarm", "1");
					tb1.setChecked(true);
				} else {
					Log.d("alarm", "0");
					tb1.setChecked(false);
				}				
				
				//toggle 2
				tb2.setTextOn("ON");
				tb2.setTextOff("OFF");
				if(db.getSleepTime(0).isToggle() == true) {
					tb2.setChecked(true);
				} else {
					tb2.setChecked(false);
				}
				
				//toggle 3
				if(db.getSleepTime(0).isForceToggle() == true) {					
					tb3.setChecked(true);
					tb3.setText("ON");
				} else {
					tb3.setChecked(false);
					tb3.setText("OFF");					
				}
				
				//toggle 4
				if(db.getTrackTimeCount() == 0) {
					tb4.setChecked(false);
					tb4.setText("OFF");
				} else {
					try {
					    Calendar calSet = (Calendar) Calendar.getInstance().clone();
					    calSet.set(Calendar.HOUR_OF_DAY, 0);
					    calSet.set(Calendar.MINUTE, 0);
					    calSet.set(Calendar.SECOND, 0);
					    calSet.set(Calendar.MILLISECOND, 0);					    
						// The last row of Table Track_time
					    //Log.d("AM", db.getTrackTime(db.getTrackTimeCount()-1).getDate().toString());
					    //Log.d("BM", calSet.getTime().toString());
					    //Log.d("CM", String.valueOf(db.getTrackTime(db.getTrackTimeCount()-1).getDate()
							//.compareTo(calSet.getTime())));					    
						if((db.getTrackTime(db.getTrackTimeCount()-1).getDate()
							.compareTo(calSet.getTime()) >= 0)
							&& db.getTrackTime(db.getTrackTimeCount()-1).isToggle() == true) {
							//Log.d("JIAN", "1");
							tb4.setChecked(true);
							tb4.setText("ON");//get current state
						} else {
							tb4.setChecked(false);
							tb4.setText("OFF");
							//Log.d("JIAN", "2");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				/*******************************************
				 *******  OnCheckedChangeListener **********
				 *******************************************/
				tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						AlarmData ad = db.getAlarm(0);
						if (isChecked) {
				            // The toggle is enabled
							ad.setToggle(true);
				        } else {
				        	// The toggle is disabled
							ad.setToggle(false);
				        }
						db.updateAlarm(ad);
				    }						
				});
				
				tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						SleepTimeData st = db.getSleepTime(0);
						if (isChecked) {
				            // The toggle is enabled
							st.setToggle(true);
							Calendar calNow = Calendar.getInstance();
						    Calendar calSet = (Calendar) calNow.clone();			    
						    
						    calSet.set(Calendar.HOUR_OF_DAY, st.getHour());
						    calSet.set(Calendar.MINUTE, st.getMinute());
						    calSet.set(Calendar.SECOND, 0);
						    calSet.set(Calendar.MILLISECOND, 0);
						    
						    int dayOfWeek = st.getDayOfWeek();
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
						    onetimeTimer(getActivity().getApplicationContext(), calSet.getTimeInMillis() - calNow.getTimeInMillis(), 100);
				        } else {
				        	// The toggle is disabled
				        	tb3.setChecked(false);
				        	tb3.setText("OFF");							
							st.setToggle(false);
							st.setForceToggle(false);
							cancelTimer(getActivity().getApplicationContext(), 100);
				        }
						db.updateSleepTime(st);
				    }						
				});
				
				tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						SleepTimeData st = db.getSleepTime(0);
						if (isChecked) {
				            // The toggle is enabled
							if(tb2.isChecked()){
								tb3.setText("ON");
								st.setForceToggle(true);
								db.updateSleepTime(st);
							} else {
								tb3.setChecked(false);
							}
				        } else {
				        	// The toggle is disabled
				        	tb3.setText("OFF");
							st.setForceToggle(false);
							db.updateSleepTime(st);
				        }
				    }						
				});
						
				tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				        if (isChecked) {
				            // The toggle is enabled
				        	tb4.setChecked(false);
				        } else {
				        	// The toggle is disabled
				        	if(tb4.getText().toString().equals("ON")) {
				        		tb4.setText("OFF");
								try {
									TrackTimeData tt = db.getTrackTime(db.getTrackTimeCount()-1);
									tt.setToggle(false);
									db.updateTrackTime(tt);
								} catch (NumberFormatException | ParseException e) {
									e.printStackTrace();
								}
				        	}			 
				        }
				    }						
				});

				Button alarm = (Button) rootView.findViewById(R.id.buttonhome1);				
				alarm.setOnClickListener(new OnClickListener() {			
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), Alarm.class);//change to test
						startActivity(intent);
					}
				});
				
				Button sleep = (Button) rootView.findViewById(R.id.buttonhome2);
				sleep.setOnClickListener(new OnClickListener() {			
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), SleepSetting.class);
						startActivity(intent);
					}
				});
				
				Button force = (Button) rootView.findViewById(R.id.buttonhome3);
				force.setOnClickListener(new OnClickListener() {			
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), Force.class);
						startActivity(intent);
					}
				});
				
				Button tracking = (Button) rootView.findViewById(R.id.buttonhome4);
				tracking.setOnClickListener(new OnClickListener() {			
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), Tracking.class);
						startActivity(intent);
					}
				});
			}
			
			else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
				rootView = inflater.inflate(R.layout.sleepmanager_statistics, container, false);	
				CalendarView calendar = (CalendarView) rootView.findViewById(R.id.calendarView1);
				
				calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
					public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
						month++;
						//Log.d("Year", String.valueOf(year));
						//Log.d("Month", String.valueOf(month));
						//Log.d("Day", String.valueOf(dayOfMonth));			
						int i;
						int startId = -1;
						int endId = -1;
						for(i = db.getTrackTimeCount()-1; i >= 0 ; i--) {
							Calendar calendar = Calendar.getInstance();  
							try {
								calendar.setTime(db.getTrackTime(i).getDate());
							} catch (NumberFormatException | ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        //Log.d("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
					        //Log.d("MONTH", String.valueOf(calendar.get(Calendar.MONTH)));
					        //Log.d("DAY", String.valueOf(calendar.get(Calendar.DATE)));
							if(calendar.get(Calendar.YEAR) == year
							&& calendar.get(Calendar.MONTH) == month - 1
							&& calendar.get(Calendar.DATE) == dayOfMonth) {
								if(startId == -1) {
									startId = i;
								}
								endId = i;								
							}
						}
						Log.d("Date I", String.valueOf(startId));
						Log.d("Date J", String.valueOf(endId));
						if(startId == -1) {
							Toast.makeText(getActivity(),month+"-"+dayOfMonth +"-"+ year,Toast.LENGTH_SHORT).show();
						} else {
							//Create the bundle
							Bundle bundle = new Bundle();
							//Add your data to bundle
							bundle.putInt("year", year); 
							bundle.putInt("month", month); 
							bundle.putInt("day", dayOfMonth);
							Intent myIntent = new Intent(getActivity(),Chart.class);
							//Add the bundle to the intent
							myIntent.putExtras(bundle);
							myIntent.putExtra("startId", startId);
							myIntent.putExtra("endId", endId);
							startActivity(myIntent);
						}
					} 
				});
			}
			
			else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
				rootView = inflater.inflate(R.layout.sleepmanager_share, container, false);	

				final EditText name = (EditText)rootView.findViewById(R.id.name);
				final EditText email = (EditText)rootView.findViewById(R.id.emailadds);
		        Button byemail = (Button)rootView.findViewById(R.id.byemail);
		        Button byfb = (Button)rootView.findViewById(R.id.byfb);
		        
		        byemail.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) { 
						if (name.getText().toString().length() == 0)
							name.setError("Name here!");
						else if (email.getText().toString().length() == 0)
							email.setError("Email here!");
						else{
							String body = "Hello "+name.getText().toString()+",<br>";
							String addr = String.valueOf(email.getText());
							Intent email = new Intent(Intent.ACTION_SEND);
							email.putExtra(Intent.EXTRA_EMAIL, new String[]{addr});
							email.putExtra(Intent.EXTRA_SUBJECT, "My Sleep Quality"); 
							email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
							email.setType("message/rfc822");
				            startActivityForResult(Intent.createChooser(email, "Sleep Quality"),1);
						}
					}
		        }); 
		        
		        byfb.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) { 
						Intent intent = new Intent(getActivity(), OnFaceBook.class);
						startActivity(intent);
					}
		        });
			}
			
			else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4){
				rootView = inflater.inflate(R.layout.sleepmanager_help, container, false);			
			}
			return rootView;
		}
	}
	
    public void onBackPressed(){
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    public static void onetimeTimer(Context context, long diff, int reqCode) {
	     //Context context = AlarmSetting.this;
	     if(sleepTimeReceiver != null) {
	    	 sleepTimeReceiver.setOnetimeTimer(context, diff, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}	
	 
	public static void startRepeatingTimer(Context context, int milliseconds, int reqCode) {
	     if(sleepTimeReceiver != null){
	    	 sleepTimeReceiver.SetSleepTime(context, milliseconds, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}
	     
	public static void cancelTimer(Context context, int reqCode) {
	     if(sleepTimeReceiver != null) {
	    	 sleepTimeReceiver.CancelSleepTime(context, reqCode);
	     } else {
	    	 Toast.makeText(context, "SleepTime is null", Toast.LENGTH_SHORT).show();
	     }
	}
    
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }*/
}
