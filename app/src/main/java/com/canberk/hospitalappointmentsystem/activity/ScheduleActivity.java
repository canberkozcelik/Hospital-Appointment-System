package com.canberk.hospitalappointmentsystem.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.CalendarView.OnDateChangeListener;

import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.model.Appointment;

@SuppressLint("SimpleDateFormat") public class ScheduleActivity extends Activity {

	CalendarView calendarView;

	String doctor_username;
	String doctor_fullname;
	String patient_tc_no;
	String sector_name;

	Appointment appointment = new Appointment();
	Date currentSelectedDate ;
	LinearLayout schedule;

	boolean is_task_finished = false;
	
	String[] hours = { "08:00-08:30", "08:30-09:00", "09:00-09:30",
			"09:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30",
			"11:30-12:00", "13:00-13:30", "13:30-14:00", "14:00-14:30",
			"14:30-15:00", "15:00-15:30", "15:30-16:00", "16:00-16:30",
			"16:30-17:00" };

	ArrayList<Integer> not_available_hours = new ArrayList<Integer>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		boolean x = getIntent().getExtras().getBoolean("BOOLEAN");
		calendarView = (CalendarView) findViewById(R.id.calendarView1);
		calendarView.setDate(calendarView.getDate()+86400000);
		calendarView.setMinDate(calendarView.getDate());
		calendarView.setMaxDate(calendarView.getDate()+2592000000L);
		
		currentSelectedDate = new Date(calendarView.getDate());
		schedule = (LinearLayout) findViewById(R.id.scheduleLinearLayout);
		doctor_username = getIntent().getExtras().getString("DOCTOR_USERNAME");
		if (x) {
			doctor_fullname = getIntent().getExtras().getString(
					"DOCTOR_FULLNAME");
			patient_tc_no = getIntent().getExtras().getString("PATIENT_TCNO");
			sector_name = getIntent().getExtras().getString("SECTOR_NAME");
			appointment.setDoctor_username(doctor_username);
			appointment.setPatient_tc_no(patient_tc_no);
			appointment.setDate(new SimpleDateFormat("dd-MM-yyyy")
					.format(currentSelectedDate));
			setTitle(sector_name + " - " + doctor_fullname);
			
			calendarView.setOnDateChangeListener(new OnDateChangeListener() {

				@Override
				public void onSelectedDayChange(CalendarView view, int year,
						int month, int dayOfMonth) {
					// TODO Auto-generated method stub

					if (dayOfMonth != currentSelectedDate.getDate()
							|| (dayOfMonth == currentSelectedDate.getDate() && month != currentSelectedDate
									.getMonth())) {

						currentSelectedDate = new Date(calendarView.getDate());
						appointment.setDate(new SimpleDateFormat("dd-MM-yyyy")
								.format(currentSelectedDate));
						updateSchedulePatient();
					}

				}
			});
			updateSchedulePatient();
		}else {
			setTitle("Block Hours");
			appointment.setDoctor_username(doctor_username);
			appointment.setPatient_tc_no("1");
			appointment.setDate(new SimpleDateFormat("dd-MM-yyyy")
			.format(currentSelectedDate));
			calendarView.setOnDateChangeListener(new OnDateChangeListener() {

				@Override
				public void onSelectedDayChange(CalendarView view, int year,
						int month, int dayOfMonth) {
					// TODO Auto-generated method stub

					if (dayOfMonth != currentSelectedDate.getDate()
							|| (dayOfMonth == currentSelectedDate.getDate() && month != currentSelectedDate
									.getMonth())) {

						currentSelectedDate = new Date(calendarView.getDate());
						appointment.setDate(new SimpleDateFormat("dd-MM-yyyy")
								.format(currentSelectedDate));
						updateScheduleDoctor();
					}

				}
			});
			updateScheduleDoctor();
		}
	}

	public void updateSchedulePatient() {
		schedule.removeAllViews();
		not_available_hours = new ArrayList<Integer>();
		appointment.getNotAvailableHours(this, not_available_hours,hours,true);
	}
	
	public void updateScheduleDoctor() {
		schedule.removeAllViews();
		not_available_hours = new ArrayList<Integer>();
		appointment.getNotAvailableHours(this, not_available_hours,hours,false);
	}
}
