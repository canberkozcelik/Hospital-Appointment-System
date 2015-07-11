package com.canberk.hospitalappointmentsystem.model;

import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

import android.app.Activity;

/**
 * A class for doctor.
 *
 */
/**
 * @author yavuz
 *
 */
public class Doctor extends User {

	private String username;
	private String sector;

	DatabaseConnector db;
	
	/**
	 * @param password
	 * @param e_mail
	 * @param f_name
	 * @param l_name
	 * @param phone_number
	 * @param username
	 * @param sector
	 */
	public Doctor(String password, String e_mail, String f_name, String l_name,
			String phone_number, String username, String sector) {
		super(password, e_mail, f_name, l_name, phone_number);
		// TODO Auto-generated constructor stub
		this.username = username;
		this.sector = sector;
		db = new DatabaseConnector();
	}
	
	
	
	public Doctor() {
		super();
		// TODO Auto-generated constructor stub
		db = new DatabaseConnector();
	}
	
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * @param sector
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	
	
	/* (non-Javadoc)
	 * @see com.se301.model.User#Login(android.app.Activity)
	 */
	@Override
	public void Login(Activity activity) {
		// TODO Auto-generated method stub
		db.doctorLogin(this, activity);
	}
	
	/* (non-Javadoc)
	 * @see com.example.classes.User#Sign_up(android.app.Activity)
	 */
	@Override
	public void Sign_up(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param sector
	 * @param activity
	 * @param is_logged
	 * @param is_doctor
	 * @param patient
	 */
	public void getUFLbySector(String sector,Activity activity,boolean is_logged,boolean is_doctor,Patient patient){
		db.selectInfoDoctorbySector(sector, activity,is_logged,is_doctor,patient);
	}
	/**
	 * Method gets appointments of this doctor's id and updates UI by given parameter activity.
	 * @param activity
	 */
	public void getAppointmentOfDoctor(Activity activity){
		db.selectAppointmentsDoctor(this.username,activity);
	}

}
