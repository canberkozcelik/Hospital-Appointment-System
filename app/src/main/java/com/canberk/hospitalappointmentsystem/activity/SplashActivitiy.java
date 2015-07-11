package com.canberk.hospitalappointmentsystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

public class SplashActivitiy extends Activity {

	ProgressBar p;
	private static int SPLASH_TIME_OUT = 1300;
	TextView info;

	DatabaseConnector db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		db = new DatabaseConnector();
		
		p = (ProgressBar) findViewById(R.id.progressBar1);
		p.getIndeterminateDrawable().setColorFilter(Color.CYAN, Mode.MULTIPLY);
		info = (TextView) findViewById(R.id.infoSplash);
		info.setText("Checking internet connection..");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Check your internet connection !");
		builder.setCancelable(false);
		builder.setNegativeButton("Quit", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		final AlertDialog alert = builder.create();
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				if (isNetworkAvailable()) {
					info.setText("Checking service availability..");
					db.checkDatabaseConnection(SplashActivitiy.this);
				} else {
					info.setText("No connection.");
					alert.show();
				}
			}
		}, SPLASH_TIME_OUT);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
