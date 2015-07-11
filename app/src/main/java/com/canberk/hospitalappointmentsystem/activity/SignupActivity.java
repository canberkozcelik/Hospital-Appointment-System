package com.canberk.hospitalappointmentsystem.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.methods.Mail;
import com.canberk.hospitalappointmentsystem.methods.PasswordEncryption;
import com.canberk.hospitalappointmentsystem.model.Patient;

public class SignupActivity extends Activity{
	
	EditText tc_no;
	EditText password;
	EditText email;
	
	EditText firstname;
	EditText lastname;
	EditText phone;
	 
	Button submit;
	
	TextView error;
	TextView allfields;
	 
	Patient patient_to_insert;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		allfields = (TextView) findViewById(R.id.allfields);
		allfields.setVisibility(View.INVISIBLE);
		tc_no = (EditText) findViewById(R.id.tcNumberEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		email = (EditText) findViewById(R.id.emailEditText);
		firstname = (EditText) findViewById(R.id.firstnameEditText);
		lastname = (EditText) findViewById(R.id.lastnameEditText);
		phone = (EditText) findViewById(R.id.phonenumberEditText);
		
		submit = (Button) findViewById(R.id.submitButton);
		
		error = (TextView) findViewById(R.id.signUpError);
		
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				error.setText("");
				if(!tc_no.getText().toString().equals("") && !password.getText().toString().equals("") && Mail.validEmailChecker(email.getText().toString()) && !firstname.getText().toString().equals("") && !lastname.getText().toString().equals("") && !phone.getText().toString().equals("") ){
					//login
					patient_to_insert = new Patient(PasswordEncryption.SHA1(password.getText().toString()), email.getText().toString(), firstname.getText().toString(), lastname.getText().toString(), phone.getText().toString(), tc_no.getText().toString());
					patient_to_insert.Sign_up(SignupActivity.this);
				}
				
				else{
					allfields.setVisibility(View.VISIBLE);
					if(tc_no.getText().toString().length()<11){
						error.append("*You must provide 11 digit TC number.\n");
					}
					if(password.getText().toString().equals("")){
						error.append("*Password field can not be empty.\n");
					}
					if(!Mail.validEmailChecker(email.getText().toString())){
						if(email.getText().toString().equals("")){
							error.append("*Email field can not be empty.\n");
						}
						else{
							error.append("*Please provide valid email adress.\n");
						}
						
					}
					if(firstname.getText().toString().equals("")){
						error.append("*Firstname field can not be empty.\n");
					}
					if(lastname.getText().toString().equals("")){
						error.append("*Lastname field can not be empty.\n");
					}
					if(phone.getText().toString().equals("")){
						error.append("*Phone number field can not be empty.\n");
					}
				}
			}
		});
	}
	
	
	
}
