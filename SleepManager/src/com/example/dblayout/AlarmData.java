package com.example.dblayout;

public class AlarmData {
     
    //private variables
    private int id;
    private int hour;
    private int minute;
    private int second;
    private int dayOfWeek;
    private int ringtone;
    private int captcha;
    private boolean toggle;
    private int reqCode;

	// Empty constructor
    public AlarmData() {
    	id = 0;
        hour = 0;
        minute = 0;
        second = 0;
        dayOfWeek = 0;
        setRingtone(0);
        captcha = 0;
        toggle = false;
        setReqCode(0);
    }
    
    // constructor
    public AlarmData(int id, int hour, int minute, int second, int dayOfWeek, 
    		int ringtone, int captcha, boolean toggle, int reqCode) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.dayOfWeek = dayOfWeek;
        this.setRingtone(ringtone);
        this.captcha = captcha;
        this.toggle = toggle;
        this.setReqCode(reqCode);
    }
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	/**
	 * @return the ringtone
	 */
	public int getRingtone() {
		return ringtone;
	}

	/**
	 * @param ringtone the ringtone to set
	 */
	public void setRingtone(int ringtone) {
		this.ringtone = ringtone;
	}

	public int getCaptcha() {
		return captcha;
	}

	public void setCaptcha(int captcha) {
		this.captcha = captcha;
	}

	/**
	 * @return the toggle
	 */
	public boolean isToggle() {
		return toggle;
	}

	/**
	 * @param toggle the toggle to set
	 */
	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	/**
	 * @return the reqCode
	 */
	public int getReqCode() {
		return reqCode;
	}

	/**
	 * @param reqCode the reqCode to set
	 */
	public void setReqCode(int reqCode) {
		this.reqCode = reqCode;
	}
}