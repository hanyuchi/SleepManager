/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.time;

import android.content.Context;
//import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Like AnalogClock, but digital.  Shows seconds.
 *
 * FIXME: implement separate views for hours/minutes/seconds, so
 * proportional fonts don't shake rendering
 */

public class DigitalClock extends TextView {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    //private Handler mHandler;
    
    private long timeInMillis;

    private boolean mTickerStopped = false;

    String mFormat;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        //Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }
    
    public void setTime(int hour, int minute) {
        mTickerStopped = false;
    	//mTickerStopped = true;
        Calendar calSet = Calendar.getInstance();

        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        
        timeInMillis = calSet.getTimeInMillis();      
        //mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    mCalendar.setTimeInMillis(timeInMillis);
                    setText(DateFormat.format(mFormat, mCalendar));
                    invalidate();
                    //long now = SystemClock.uptimeMillis();
                    //long next = now + (1000 - now % 1000);
                    //mHandler.postAtTime(mTicker, next);
                }
            };
        mTicker.run();
    }
    /*
    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
    	//mTickerStopped = true;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
/*        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    mCalendar.setTimeInMillis(timeInMillis);
                    setText(DateFormat.format(mFormat, mCalendar));
                    invalidate();
                    //long now = SystemClock.uptimeMillis();
                    //long next = now + (1000 - now % 1000);
                    //mHandler.postAtTime(mTicker, next);
                }
            };
        mTicker.run();
    }*/

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }
}
