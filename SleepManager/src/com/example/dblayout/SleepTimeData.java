package com.example.dblayout;

public class SleepTimeData {
     
    //private variables
    private int id;
    private int hour;
    private int minute;
    private int second;
    private int dayOfWeek;
    private int ringtone;
    private boolean toggle;
    private int wakeupTime;
    private int lockMode;
    private boolean forceToggle;

	// Empty constructor
    public SleepTimeData() {
    	id = 0;
        hour = 0;
        minute = 0;
        second = 0;
        dayOfWeek = 0;
        ringtone = 0;
        toggle = false;
        setWakeupTime(0);
        lockMode = 0;
        forceToggle = false;
    }
    
    // constructor
    public SleepTimeData(int id, int hour, int minute, int second, 
    		int dayOfWeek, int ringtone, boolean toggle, int wakeupTime, 
    		int lockMode, boolean forceToggle) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.dayOfWeek = dayOfWeek;
        this.ringtone = ringtone;
        this.toggle = toggle;
        this.setWakeupTime(wakeupTime);
        this.lockMode = lockMode;
        this.forceToggle = forceToggle;
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
	 * @return the wakeupTime
	 */
	public int getWakeupTime() {
		return wakeupTime;
	}

	/**
	 * @param wakeupTime the wakeupTime to set
	 */
	public void setWakeupTime(int wakeupTime) {
		this.wakeupTime = wakeupTime;
	}

	/**
	 * @return the lockMode
	 */
	public int getLockMode() {
		return lockMode;
	}

	/**
	 * @param lockMode the lockMode to set
	 */
	public void setLockMode(int lockMode) {
		this.lockMode = lockMode;
	}

	public boolean isForceToggle() {
		return forceToggle;
	}

	public void setForceToggle(boolean forceToggle) {
		this.forceToggle = forceToggle;
	}
}