package com.canberk.hospitalappointmentsystem.activity;

import java.util.Date;


import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.model.Doctor;
import com.canberk.hospitalappointmentsystem.model.Patient;

public class CancelActivity extends Activity {
	
	Patient patient = new Patient();
	Doctor doctor = new Doctor();

	LinearLayout linearCancelLayout ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel);
		
		linearCancelLayout = (LinearLayout) findViewById(R.id.cancelLinearLayout);
		
		String tc_no = getIntent().getExtras().getString("PATIENT_TC_NO", "");
		String username = getIntent().getExtras().getString("DOCTOR_USERNAME", "");
		boolean b = getIntent().getExtras().getBoolean("BOOLEAN");
		if(b){
			setTitle("Cancel Appointment");
			if(!tc_no.equals("")){
				patient.setTc_number(tc_no);
				patient.getAppointmentsOfUserByDate(this,true,new Date());
			}
		}
		else{
			setTitle("Appointment History");
			if(!tc_no.equals("")){
				patient.setTc_number(tc_no);
				patient.getAppointmentsOfUserByDate(this,true,new Date(0L));
			}
			if(!username.equals("")){
				doctor.setUsername(username);
				doctor.getAppointmentOfDoctor(this);
			}
		}
	}
}
