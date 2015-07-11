package com.canberk.hospitalappointmentsystem.model;

import android.app.Activity;

/**
 * A super class for patient and doctor.
 *
 */
public abstract class User {
	
	private String password;
	private String e_mail;
	private String f_name;
	private String l_name;
	private String phone_number;

	/**
	 * Constructor of User
	 * @param password
	 * @param e_mail
	 * @param f_name
	 * @param l_name
	 * @param phone_number
	 */
	public User(String password, String e_mail, String f_name, String l_name,
			String phone_number) {
		super();
		this.password = password;
		this.e_mail = e_mail;
		this.f_name = f_name;
		this.l_name = l_name;
		this.phone_number = phone_number;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the e_mail
	 */
	public String getE_mail() {
		return e_mail;
	}

	/**
	 * @param e_mail the e_mail to set
	 */
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	/**
	 * @return the f_name
	 */
	public String getF_name() {
		return f_name;
	}

	/**
	 * @param f_name the f_name to set
	 */
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	/**
	 * @return the l_name
	 */
	public String getL_name() {
		return l_name;
	}

	/**
	 * @param l_name the l_name to set
	 */
	public void setL_name(String l_name) {
		this.l_name = l_name;
	}

	/**
	 * @return the phone_number
	 */
	public String getPhone_number() {
		return phone_number;
	}

	/**
	 * @param phone_number the phone_number to set
	 */
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	/**
	 * Abstract methods for patient and doctor
	 * @param activity
	 */
	public abstract void Login(Activity activity);

	/**
	 * Abstract methods for patient and doctor
	 * @param activity
	 */
	public abstract void Sign_up(Activity activity);

}
