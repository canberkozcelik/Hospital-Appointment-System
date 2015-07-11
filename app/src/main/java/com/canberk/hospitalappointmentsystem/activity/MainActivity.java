package com.canberk.hospitalappointmentsystem.activity;


import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;
import com.canberk.hospitalappointmentsystem.methods.Mail;
import com.canberk.hospitalappointmentsystem.methods.PasswordEncryption;
import com.canberk.hospitalappointmentsystem.model.Doctor;
import com.canberk.hospitalappointmentsystem.model.Patient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("ClickableViewAccessibility") public class MainActivity extends Activity {

	private GoogleMap googleMap;
	Button showMapButton;

	Dialog sectorlistDialog;

	Menu menu;

	LinearLayout linearLayoutLogin;
	EditText username;
	EditText password;
	Button loginButton;
	ToggleButton toggleButton;

	TextView createAccount;
	TextView loginInfo;
	TextView forget_password;

	LinearLayout LinearLayoutApp;
	Button requestApp;
	TextView faqs;

	ScrollView mainScrollView;
	ImageView transparentImageView;
	LatLng hospital_location;

	Patient patient = new Patient();
	Doctor doctor = new Doctor();
	InputMethodManager imm;

	boolean is_logged = false;
	boolean is_patient = false;
	Button getDoctors1;
	Button getDoctors2;
	Button getDoctors3;
	Button getDoctors4;
	Button getDoctors5;
	Button getDoctors6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		showMapButton = (Button) findViewById(R.id.showMapButton);
		showMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri = "http://maps.google.com/maps?daddr="
						+ hospital_location.latitude + ","
						+ hospital_location.longitude;
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(uri));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		});
		hospital_location = new LatLng(41.168573, 29.561279);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getDoctors1 = (Button) findViewById(R.id.getdoctors1);
		getDoctors2 = (Button) findViewById(R.id.getdoctors2);
		getDoctors3 = (Button) findViewById(R.id.getdoctors3);
		getDoctors4 = (Button) findViewById(R.id.getdoctors4);
		getDoctors5 = (Button) findViewById(R.id.getdoctors5);
		getDoctors6 = (Button) findViewById(R.id.getdoctors6);

		myDoctorClickListener x = new myDoctorClickListener(false);

		getDoctors1.setOnClickListener(x);
		getDoctors2.setOnClickListener(x);
		getDoctors3.setOnClickListener(x);
		getDoctors4.setOnClickListener(x);
		getDoctors5.setOnClickListener(x);
		getDoctors6.setOnClickListener(x);

		username = (EditText) findViewById(R.id.UsernameEditText);
		password = (EditText) findViewById(R.id.PasswordEditText);

		faqs = (TextView) findViewById(R.id.FAQsTextView);
		faqs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent faqs = new Intent(MainActivity.this, FAQsActivity.class);
				startActivity(faqs);
			}
		});
		loginButton = (Button) findViewById(R.id.LoginButton);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		createAccount = (TextView) findViewById(R.id.appointment_item);
		loginInfo = (TextView) findViewById(R.id.loginInfo);
		linearLayoutLogin = (LinearLayout) findViewById(R.id.LinearLayout1);
		forget_password = (TextView) findViewById(R.id.forget_password);
		
		forget_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Builder alert = new Builder(MainActivity.this);
			    final EditText input = new EditText(MainActivity.this);
			    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			    input.setHint("Here...");
			    alert.setView(input);
			    alert.setTitle("Enter your e-mail adress");
			    alert.setPositiveButton("Send new password", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			        	String email = input.getText().toString().trim();
			        	if(Mail.validEmailChecker(email)){
			        		Mail m = new Mail(email,"SONYCSYSTEMS PASSWORD RECOVERY-NO REPLY","You have requested a new password.\n Password: ");
			        		m.sendNewPassword(MainActivity.this);
			        		dialog.dismiss();
			        	}
			        	else{
			        		dialog.dismiss();
			        		alertDialog(MainActivity.this, "Enter valid email adress.");
			        	}
			        }
			    });

			    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.dismiss();
			        }
			    });
			    alert.show();
			}
		});

		LinearLayoutApp = (LinearLayout) findViewById(R.id.LinearLayoutRequestApp);

		toggleButton.setOnClickListener(toggleClick);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				if (username.getText().toString().equals("")
						|| password.getText().toString().equals("")) {
					String x;
					if (toggleButton.isChecked()) { // doctor
						x = "username";
					} else { // patient
						x = "TC number";
					}
					alertDialog(MainActivity.this, "You must provide " + x
							+ " and password to login.");
				} else {
					if (toggleButton.isChecked()) { // doctor
						doctor.setPassword(PasswordEncryption.SHA1(password.getText().toString()));
						doctor.setUsername(username.getText().toString());
						doctor.Login(MainActivity.this);
					} else { // patient
						patient.setPassword(PasswordEncryption.SHA1(password.getText().toString()));
						patient.setTc_number(username.getText().toString());
						patient.Login(MainActivity.this);
					}
				}
				/*
				 * linearLayoutLogin.setVisibility(View.GONE);
				 * LinearLayoutApp.setVisibility(View.VISIBLE);
				 */
			}
		});

		createAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent signup = new Intent(MainActivity.this,
						SignupActivity.class);
				startActivity(signup);
			}
		});

		try {
			// Loading map
			initilizeMap();
			MarkerOptions marker = new MarkerOptions().position(
					hospital_location).title("Sonyc Hospital");
			googleMap.addMarker(marker);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					hospital_location, 15));

		} catch (Exception e) {
			e.printStackTrace();
		}

		mainScrollView = (ScrollView) findViewById(R.id.scrollView1);
		transparentImageView = (ImageView) findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					mainScrollView.requestDisallowInterceptTouchEvent(true);
					// Disable touch on transparent view
					return false;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					mainScrollView.requestDisallowInterceptTouchEvent(false);
					return true;

				case MotionEvent.ACTION_MOVE:
					mainScrollView.requestDisallowInterceptTouchEvent(true);
					return false;

				default:
					return true;
				}
			}
		});

		requestApp = (Button) findViewById(R.id.requestAppButton);
		requestApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSectorListDialog();
			}
		});
	}

	public void showSectorListDialog() {
		sectorlistDialog = new Dialog(MainActivity.this);
		sectorlistDialog.setContentView(R.layout.sectorlist_dialog);
		sectorlistDialog.setTitle("Choose a sector");

		Button button1 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors1);
		Button button2 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors2);
		Button button3 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors3);
		Button button4 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors4);
		Button button5 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors5);
		Button button6 = (Button) sectorlistDialog
				.findViewById(R.id.getdoctors6);

		myDoctorClickListener x = new myDoctorClickListener(true);
		button1.setOnClickListener(x);
		button2.setOnClickListener(x);
		button3.setOnClickListener(x);
		button4.setOnClickListener(x);
		button5.setOnClickListener(x);
		button6.setOnClickListener(x);

		if (!sectorlistDialog.isShowing()) {
			sectorlistDialog.show();
		}
	}

	OnClickListener toggleClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			updateMenuTitles();
			if (((ToggleButton) v).isChecked()) { // doctor
				username.setHint("Username");
				createAccount.setVisibility(View.INVISIBLE);
				loginInfo.setVisibility(View.INVISIBLE);
				forget_password.setVisibility(View.INVISIBLE);
				username.setInputType(InputType.TYPE_CLASS_TEXT);
			} else { // patient
				username.setHint("TC Number");
				createAccount.setVisibility(View.VISIBLE);
				loginInfo.setVisibility(View.VISIBLE);
				forget_password.setVisibility(View.VISIBLE);
				username.setInputType(InputType.TYPE_CLASS_NUMBER);
			}
		}
	};

	class myDoctorClickListener implements OnClickListener {

		boolean x;

		public myDoctorClickListener(boolean x) {
			// TODO Auto-generated constructor stub
			this.x = x;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doctor.getUFLbySector(
					v.getContentDescription().toString(),
					MainActivity.this,
					findViewById(R.id.loginLayout).getVisibility() == View.GONE,
					findViewById(R.id.LinearLayoutRequestApp).getVisibility() == View.GONE,
					patient);
			if (x) {
				if (sectorlistDialog.isShowing()) {
					sectorlistDialog.dismiss();
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		this.menu = menu;
		return true;
	}

	public void updateMenuTitles() {
		MenuItem item = menu.findItem(R.id.cancel);
		if (toggleButton.isChecked()) { // doctor
			item.setTitle("Block hours");
			menu.removeItem(R.id.changepassword);
		}else { // patient
			item.setTitle("Cancel an appointment");
			menu.removeItem(R.id.logout);
			menu.add(item.getGroupId(), R.id.changepassword, item.getOrder()+1, "Change password");//R.id.changepassword
			menu.add(item.getGroupId(), R.id.logout, item.getOrder()+1, "Logout");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (findViewById(R.id.loginLayout).getVisibility() == View.GONE) {
			if (item.getItemId() == R.id.cancel) {
				if (toggleButton.isChecked()) { // doctor
					
					 Intent schedule = new
					 Intent(MainActivity.this,ScheduleActivity.class);
					 schedule.putExtra("DOCTOR_USERNAME", doctor.getUsername());
					 schedule.putExtra("BOOLEAN", false);
					 startActivity(schedule);
					 
				} else { // patient
					Intent cancel = new Intent(MainActivity.this,
							CancelActivity.class);
					cancel.putExtra("PATIENT_TC_NO", patient.getTc_number());
					cancel.putExtra("BOOLEAN", true);
					startActivity(cancel);
				}
			}
			if(item.getItemId() == R.id.changepassword){
				final Builder alert = new Builder(MainActivity.this);
			    final EditText input = new EditText(MainActivity.this);
			    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			    input.setHint("Here...");
			    alert.setView(input);
			    alert.setTitle("Enter your new password");
			    alert.setPositiveButton("Save password", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            
			        	new DatabaseConnector().updatePassword(patient.getTc_number(), PasswordEncryption.SHA1(input.getText().toString()), MainActivity.this);
			        }
			    });

			    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            dialog.dismiss();
			        }
			    });
			    alert.show();
			}
			if (item.getItemId() == R.id.listhist) {
				if (toggleButton.isChecked()) { // doctor
					Intent list = new Intent(MainActivity.this,
							CancelActivity.class);
					list.putExtra("DOCTOR_USERNAME", doctor.getUsername());
					list.putExtra("BOOLEAN", false);
					startActivity(list);
				} else { // patient
					Intent list = new Intent(MainActivity.this,
							CancelActivity.class);
					list.putExtra("PATIENT_TC_NO", patient.getTc_number());
					list.putExtra("BOOLEAN", false);
					startActivity(list);
				}
			}
			if (item.getItemId() == R.id.logout) {
				patient = new Patient();
				doctor = new Doctor();
				findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
				LinearLayoutApp.setVisibility(View.GONE);
				username.setText("");
				password.setText("");
				alertDialog(this, "You have succresfully logged out.");
				setTitle("Home Page");
			}
		} else {
			alertDialog(this, "You must login first");
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {

			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	public void alertDialog(Activity activity, String message) {
		Builder builder = new Builder(activity);
		builder.setTitle("Warning !");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this);
		builder.setTitle("Warning !");
		builder.setMessage("Do you want to quit?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();

	}
	
	
}
