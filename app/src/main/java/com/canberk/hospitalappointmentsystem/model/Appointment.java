package com.canberk.hospitalappointmentsystem.model;

import java.util.ArrayList;


import android.app.Activity;

import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

/**
 * A class for doctor's and user's appointment.
 *
 */
public class Appointment {

	private int id;
	private String patient_tc_no;
	private String doctor_username;
	private String feedback;
	private String hour;
	private String date;

	DatabaseConnector db;
	
	/**
	 * Constructor of appointment
	 * @param id of appointment
	 * @param patient_tc_no of patient who owns this appointment
	 * @param doctor_username of doctor who owns this appointment
	 * @param hour of appointment 
	 * @param date of appointment
	 */
	public Appointment(int id, String patient_tc_no, String doctor_username,
			String hour, String date) {
		super();
		this.id = id;
		this.patient_tc_no = patient_tc_no;
		this.doctor_username = doctor_username;
		this.hour = hour;
		this.date = date;
		this.db = new DatabaseConnector();
	}
	
	/**
	 * Default constructer of appointment 
	 */
	public Appointment() {
		// TODO Auto-generated constructor stub
		this.db = new DatabaseConnector();
	}

	/**
	 * @return
	 */
	public String getPatient_tc_no() {
		return patient_tc_no;
	}

	/**
	 * @param patient_tc_no
	 */
	/**
	 * @param patient_tc_no
	 */
	public void setPatient_tc_no(String patient_tc_no) {
		this.patient_tc_no = patient_tc_no;
	}

	/**
	 * @return
	 */
	public String getDoctor_username() {
		return doctor_username;
	}

	/**
	 * @param doctor_username
	 */
	public void setDoctor_username(String doctor_username) {
		this.doctor_username = doctor_username;
	}

	/**
	 * @return
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * @param hour
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}

	/**
	 * @return
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @param activity
	 */
	public void InsertAppointment(Activity activity){
		db.insertAppointment(this, activity);
	}
	
	/**
	 * Method to update UI of ScheduleActivity by patient or doctor's selected date.
	 * @param activity
	 * @param not_available_hours of 
	 * @param hours an String array of hours
	 * @param is_ if TRUE, method called by patient or false method called by doctor
	 */
	public void getNotAvailableHours(Activity activity,ArrayList<Integer> not_available_hours,String [] hours,boolean is_){
		db.selectAppointmentsOfDoctorbyDate(this, activity, not_available_hours,hours,is_);
	}
	
}
