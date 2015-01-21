package com.example.dblayout;

import java.util.Calendar;
import java.util.Date;

public class TrackTimeData {
     
    //private variables
    private int id;
    private Date date;
    private int beginHour;
    private int beginMinute;
    private int endHour;
    private int endMinute;
    private boolean toggle;    

	// Empty constructor
    public TrackTimeData() {
    	id = 0;
    	date = Calendar.getInstance().getTime();
        beginHour = 0;
        beginMinute = 0;
        beginHour = 0;
        beginMinute = 0;        
        toggle = false;
    }
    
    // constructor
    public TrackTimeData(int id, Date date, int beginHour, int beginMinute, 
    		int endHour, int endMinute, boolean toggle) {
        this.id = id;
        this.date = date;
        this.beginHour = beginHour;
        this.beginMinute = beginMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.toggle = toggle;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public int getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}

	public int getBeginMinute() {
		return beginMinute;
	}

	public void setBeginMinute(int beginMinute) {
		this.beginMinute = beginMinute;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}
}