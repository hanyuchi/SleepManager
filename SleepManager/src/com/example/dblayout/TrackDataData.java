package com.example.dblayout;

public class TrackDataData {
     
    //private variables
    private int id;
    private int hour;
    private int minute;
    private int second;
    private float acceleration;    

	// Empty constructor
    public TrackDataData() {
    	id = 0;
        hour = 0;
        minute = 0;
        second = 0;
        setAcceleration(0);        
    }
    
    // constructor
    public TrackDataData(int id, int hour, int minute, int second, 
    		float acceleration) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.setAcceleration(acceleration);
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

	/**
	 * @return the acceleration
	 */
	public float getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
}