package com.canberk.hospitalappointmentsystem.model;

import java.util.Date;

import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

import android.app.Activity;

public class Patient extends User {
	
	private String tc_number;
	DatabaseConnector db;
	/**
	 * @param password
	 * @param e_mail
	 * @param f_name
	 * @param l_name
	 * @param phone_number
	 * @param tc_number
	 */
	public Patient(String password, String e_mail, String f_name,
			String l_name, String phone_number,String tc_number) {
		super(password, e_mail, f_name, l_name, phone_number);
		// TODO Auto-generated constructor stub
		this.tc_number = tc_number;
		db = new DatabaseConnector();
	}
	
	/**
	 * 
	 */
	public Patient() {
		super();
		db = new DatabaseConnector();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public String getTc_number() {
		return tc_number;
	}
	
	/* (non-Javadoc)
	 * @see com.example.classes.User#Login(android.app.Activity)
	 */
	@Override
	public void Login(Activity activity) {
		// TODO Auto-generated method stub
		db.patientLogin(this, activity);
	}

	/* (non-Javadoc)
	 * @see com.example.classes.User#Sign_up(android.app.Activity)
	 */
	@Override
	public void Sign_up(Activity activity) {
		// TODO Auto-generated method stub
		db.insertPatient(this, activity);
	}

	
	/**
	 * @param tc_number
	 */
	public void setTc_number(String tc_number) {
		this.tc_number = tc_number;
	}
	
	/**
	 * Method gets appointments of patient before the parameter date and updates UI .
	 * @param activity
	 * @param is_
	 * @param date
	 */
	public void getAppointmentsOfUserByDate(Activity activity,boolean is_,Date date){
		db.selectAppointmentsUserbyDate(activity,this.tc_number,is_,date);
	}

	
}
