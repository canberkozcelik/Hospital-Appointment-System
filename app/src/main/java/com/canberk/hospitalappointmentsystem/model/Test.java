package com.canberk.hospitalappointmentsystem.model;

import android.app.Activity;

import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

/**
 * A class for appointments' test 
 * 
 */
public class Test {
	
	private int appointment_id;
	private String result;
	private DatabaseConnector db;
	
	/**
	 * @param appointment_id of appointment which owns test
	 * @param result of test
	 */
	
	
	public Test(String assistant_username, int appointment_id, String result) {
		super();
		
		this.appointment_id = appointment_id;
		this.result = result;
		db = new DatabaseConnector();
	}
	
	/**
	 * 
	 */
	public Test() {
	// TODO Auto-generated constructor stub
		db= new DatabaseConnector();
	}
	/**
	 * @return
	 */
	public int getAppointment_id() {
		return appointment_id;
	}

	/**
	 * @param appointment_id
	 */
	public void setAppointment_id(int appointment_id) {
		this.appointment_id = appointment_id;
	}

	/**
	 * @return
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	/**
	 * Method gets test result of this.appointment_id and Updates UI from given activity.
	 * @param activity
	 */
	public void getTestResultOfAppointment(Activity activity){
		db.selectTestResultOfAppointment(this, activity);
	}
	
	
}
