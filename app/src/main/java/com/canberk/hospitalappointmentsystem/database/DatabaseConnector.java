package com.canberk.hospitalappointmentsystem.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.canberk.hospitalappointmentsystem.activity.MainActivity;
import com.canberk.hospitalappointmentsystem.activity.ScheduleActivity;
import com.canberk.hospitalappointmentsystem.R;
import com.canberk.hospitalappointmentsystem.methods.Mail;
import com.canberk.hospitalappointmentsystem.model.Appointment;
import com.canberk.hospitalappointmentsystem.model.Doctor;
import com.canberk.hospitalappointmentsystem.model.Patient;
import com.canberk.hospitalappointmentsystem.model.Test;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class for php connections.And most of the methods updates UI 
 * interface by given parameter activity excepts "DELETE","UPDATE" methods
 * "DELETE" and "UPDATE" methods has parameter activity to display loading screen such as ProgressDialog
 *
 */
public class DatabaseConnector {

	public String ip;
	
	String[] hours = { "08:00-08:30", "08:30-09:00", "09:00-09:30",
			"09:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30",
			"11:30-12:00", "13:00-13:30", "13:30-14:00", "14:00-14:30",
			"14:30-15:00", "15:00-15:30", "15:30-16:00", "16:00-16:30",
			"16:30-17:00" };

	public DatabaseConnector() {
		// TODO Auto-generated constructor stub
		readIP();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("resource")
	public void readIP(){
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(sdcard,"ip.txt");

		//Read text from file
		String ip = null ;

		try {
		    BufferedReader br;
			br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        ip = line;
		    }
		}catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		this.ip = "http://"+ip+"/se301/phpscripts/";
	}
	
	/**
	 * @param email
	 * @param password
	 * @param activity
	 */
	public void updatePassword(final String email,final String password,final Activity activity){
		
		final String php = "update_password.php";
		
		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {
			 
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("email", "UTF-8") + "="
							+ URLEncoder.encode(email, "UTF-8");
					data += "&" + URLEncoder.encode("password", "UTF-8") + "="
							+ URLEncoder.encode(password, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					return Integer.parseInt(sb.toString());
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if(Mail.validEmailChecker(email)){
						Toast.makeText(activity, "Your new password has been sent to your email.", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(activity, "Your new password has been changed.", Toast.LENGTH_LONG).show();
					}
					
				} 
				if(result == 0) {
					
				}
				if(result == -1) {
					
				}
			}
		};
		async.execute();
	}
	/**
	 * @param patient
	 * @param activity
	 */
	public void insertPatient(final Patient patient, final Activity activity) {

		final String php = "insert_patient.php";

		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("tc_no", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(patient.getTc_number(), "UTF-8");
					data += "&" + URLEncoder.encode("password", "UTF-8") + "="
							+ URLEncoder.encode(patient.getPassword(), "UTF-8");
					data += "&" + URLEncoder.encode("email", "UTF-8") + "="
							+ URLEncoder.encode(patient.getE_mail(), "UTF-8");
					data += "&" + URLEncoder.encode("fname", "UTF-8") + "="
							+ URLEncoder.encode(patient.getF_name(), "UTF-8");
					data += "&" + URLEncoder.encode("lname", "UTF-8") + "="
							+ URLEncoder.encode(patient.getL_name(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("phone", "UTF-8")
							+ "="
							+ URLEncoder.encode(patient.getPhone_number(),
									"UTF-8");
					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("Your account has been created. Press 'OK' to continue");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									// Intent main = new
									// Intent(activity,MainActivity.class);
									// activity.startActivity(main);
									activity.finish();
								}
							});
					builder.show();
					// new activity
					// Toast
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String message;
					if (result == 0) {
						message = "TC number, e-mail or phone number you entered already in using.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}

	/**
	 * @param a
	 * @param activity
	 */
	public void insertAppointment(final Appointment a, final Activity activity) {
		final String php = "insert_appointment.php";

		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("hour", "UTF-8") + "="
							+ URLEncoder.encode(a.getHour(), "UTF-8");
					data += "&" + URLEncoder.encode("date", "UTF-8") + "="
							+ URLEncoder.encode(a.getDate(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("Doctor_username", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(a.getDoctor_username(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("Patient_tc_number", "UTF-8")
							+ "="
							+ URLEncoder.encode(a.getPatient_tc_no(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("Your appointment created. Press 'OK' to go Home Page");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									activity.finish();
								}
							});
					
					if(!a.getPatient_tc_no().equals("1")){
						builder.show();
					}
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String message;
					if (result == 0) {
						message = "Something went wrong.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}

	/**
	 * @param id
	 * @param activity
	 * @param lin
	 */
	public void deleteAppointmentPatient(final int id, final Activity activity,final LinearLayout lin) {
		final String php = "delete_appointment_patient.php";
		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("id", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(String.valueOf(id), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("Cancelled.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									lin.setVisibility(View.GONE);
									
									LinearLayout linear = (LinearLayout) activity.findViewById(R.id.cancelLinearLayout);
									boolean x = false;
									Log.w("sout",linear.getChildCount()+"");
									for (int i = 0; i < linear.getChildCount(); i++) {
										if(((LinearLayout)linear.getChildAt(i)).getVisibility() != View.GONE && i%2 == 0){
											x = true;
										}
									}
									Log.w("sout",x+"");
									if(!x){
										Builder builder = new Builder(activity);
										builder.setTitle("Warning !");
										builder.setMessage("You don't have any appointments.");
										builder.setPositiveButton("OK",
												new Dialog.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog,
															int which) {
														// TODO Auto-generated method stub
														dialog.dismiss();
														activity.finish();
													}
												});
										builder.show();
									}
								}
							});
					builder.show();
					
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String message;
					if (result == 0) {
						message = "Your appointment can not be cancelled.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}


	/**
	 * @param id
	 * @param textview
	 */
	public void selectFeedbackOfAppointment(final int id,final TextView textview){
		final String php = "select_feedback_of_appointment.php";
		
		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("id", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(id), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					return sb.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result.equals("")) {
					textview.setText(result);
				} else {
					textview.setText("No feedback yet.");
				}
			}
		};
		async.execute();
	}

	/**
	 * @param activity
	 * @param tc_no
	 * @param is_patient
	 * @param datex
	 */
	public void selectAppointmentsUserbyDate(final Activity activity,
			final String tc_no, boolean is_patient,final Date datex) {
		final String php = "select_appointments_user_by_date.php";

		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("Patient_tc_number",
							"UTF-8") + "=" + URLEncoder.encode(tc_no, "UTF-8");
					data += "&"
							+ URLEncoder.encode("date", "UTF-8")
							+ "="
							+ URLEncoder.encode(new SimpleDateFormat(
									"dd-MM-yyyy").format(datex), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("")) {
						return "no";
					} else {
						return sb.toString();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return "error";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.equals("error")) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Warning !");
					builder.setMessage("Please try again later.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									activity.finish();
								}
							});
					builder.show();
				} 
				if (result.equals("no")) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Warning !");
					builder.setMessage("You don't have any appointments.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									activity.finish();
								}
							});
					builder.show();
				}
				else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String date = "";
					String id = "";
					String doctor_username = "";
					String hour = "";
					boolean t = false;
					boolean y = false;
					boolean x = false;
					String tmpid = "", tmpusername = "", tmpdate = "";
					ArrayList<Appointment> app = new ArrayList<Appointment>();
					for (int i = 0; i < result.length(); i++) {
						if (!t && !y && !x && result.charAt(i) != '$') {
							id += result.charAt(i);
						}
						if (t && !y && !x && result.charAt(i) != '#') {
							doctor_username += result.charAt(i);
						}
						if (t && y && !x && result.charAt(i) != '&') {
							date += result.charAt(i);
						}
						if (t && y && x && result.charAt(i) != '*') {
							hour += result.charAt(i);
						}
						if (result.charAt(i) == '$') {
							tmpid = id;
							id = "";
							t = true;
						}
						if (result.charAt(i) == '#') {
							tmpusername = doctor_username;
							doctor_username = "";
							y = true;
						}

						if (result.charAt(i) == '&') {
							tmpdate = date;
							date = "";
							x = true;
						}
						if (result.charAt(i) == '*') {
							app.add(new Appointment(Integer.parseInt(tmpid),tc_no, tmpusername, hour, tmpdate));
							hour = "";
							t = false;
							y = false;
							x = false;
						}
					}
					for (int i = 0; i < app.size(); i++) {
						LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View cancel_item = inflater.inflate(R.layout.cancel_item, null);
						selectSectorbyDoctorUsername(app.get(i).getDoctor_username(),(TextView) cancel_item.findViewById(R.id.doctorf_cancel),(TextView)  cancel_item.findViewById(R.id.doctorl_cancel), (TextView) cancel_item.findViewById(R.id.sector_cancel));
						((TextView)cancel_item.findViewById(R.id.date_cancel)).setText(app.get(i).getDate());
						((TextView)cancel_item.findViewById(R.id.hour_cancel)).setText(hours[Integer.parseInt(app.get(i).getHour())]);
						Button but = (Button) cancel_item.findViewById(R.id.cancelButtonAppointment);
						but.setContentDescription(String.valueOf(app.get(i).getId()));
						final Test test = new Test();
						if(datex.getTime() == 0L){
							selectFeedbackOfAppointment(app.get(i).getId(), ((TextView)cancel_item.findViewById(R.id.feedbackTextView)));
							but.setText("Show test results");
							but.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									test.getTestResultOfAppointment(activity);
								}
							});
						}
						else{
							((LinearLayout)cancel_item.findViewById(R.id.linearLayoutFeedBack)).setVisibility(View.GONE); //NAL POÝNTER
							but.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(final View v) {
									// TODO Auto-generated method stub
									Builder builder = new Builder(activity);
									builder.setTitle("Warning !");
									builder.setMessage("Do you want to cancel this appointment?");
									builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											deleteAppointmentPatient(Integer.parseInt(v.getContentDescription().toString()), activity,(LinearLayout)(v.getParent().getParent()));
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
							});
						}
						((LinearLayout)activity.findViewById(R.id.cancelLinearLayout)).addView(cancel_item);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								10);
						LinearLayout linearLayout = new LinearLayout(activity);
						linearLayout.setLayoutParams(lp);
						linearLayout.setBackgroundResource(R.color.transparent);
						((LinearLayout)activity.findViewById(R.id.cancelLinearLayout)).addView(linearLayout);
					
					}
					
				}
			}
		};
		async.execute();
	}
	
	/**
	 * @param test
	 * @param activity
	 */
	public void selectTestResultOfAppointment(final Test test,final Activity activity){
		final String php = "select_result_of_appointment.php";
		
		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {
			
			ProgressDialog p ;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("id", "UTF-8") + "="
							+ URLEncoder.encode(String.valueOf(test.getAppointment_id()), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					return sb.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			}

			@Override
			protected void onPostExecute(final String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Builder builder = new Builder(activity);
				builder.setTitle("Test Result");
				//builder.setMessage();
				if(p.isShowing()){
					p.dismiss();
				}
				if (!result.equals("")) {
					builder.setMessage(result);
				} else {
					builder.setMessage("No result yet.");
				}
				builder.setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated
								// method stub
								dialog.dismiss();
							}
						});
				builder.setNegativeButton(
						"Copy result",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated
								// method stub
								ClipboardManager _clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
								_clipboard.setText(result);
								Toast.makeText(activity, "Result copied to clipboard", Toast.LENGTH_SHORT).show();
							}
						});
				builder.show();
			}
		};
		async.execute();
	}
	
	/**
	 * @param id
	 * @param feedback
	 * @param activity
	 * @param feedbackTextView
	 */
	public void uploadFeedback(final String id,final String feedback,final Activity activity,final TextView feedbackTextView){
		final String php = "upload_feedback_appointment.php";
		
		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {
 
			ProgressDialog p;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("id", "UTF-8") + "="
							+ URLEncoder.encode(id, "UTF-8");
					data += "&" + URLEncoder.encode("feedback", "UTF-8") + "="
							+ URLEncoder.encode(feedback, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					return Integer.parseInt(sb.toString());
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (p.isShowing()) {
					p.dismiss();
				}
				if (result == 1) {
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull");
					builder.setMessage("Feedback has been uploaded.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					builder.show();
					feedbackTextView.setText(feedback);
				} 
				if(result == 0) {
					alertDialog(activity, "Please try again later.");
				}
				if(result == -1) {
					alertDialog(activity, "Something went wrong.");
				}
			}
		};
		async.execute();
		
	}
	/**
	 * @param doctor_username
	 * @param ftext
	 * @param ltext
	 * @param stext
	 */
	public void selectSectorbyDoctorUsername(final String doctor_username,final TextView ftext,final TextView ltext,final TextView stext){
		final String php = "select_sector_of_doctor_by_username.php";
		
		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("username", "UTF-8") + "="
							+ URLEncoder.encode(doctor_username, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					return sb.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result.equals("")) {
					ftext.setText(result.substring(0, result.indexOf('$')));
					ltext.setText(result.substring(result.indexOf('$')+1, result.indexOf('#')));
					stext.setText(result.substring(result.indexOf('#')+1, result.indexOf('&')));
				} else {
					ltext.setText("Error");
					ftext.setText("Error");
					stext.setText("Error");
				}
			}
		};
		async.execute();
	}
	/**
	 * @param doctor_username
	 * @param activity
	 */
	public void selectAppointmentsDoctor(final String doctor_username,final Activity activity) {
		
		final String php = "select_appointments_doctor.php";
		
		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("Doctor_username",
							"UTF-8") + "=" + URLEncoder.encode(doctor_username, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("")) {
						return "no";
					} else {
						return sb.toString();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return "error";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.equals("error")) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Warning !");
					builder.setMessage("Please try again later.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									activity.finish();
								}
							});
					builder.show();
				} 
				if (result.equals("no")) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Warning !");
					builder.setMessage("You don't have any appointments.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									activity.finish();
								}
							});
					builder.show();
				}
				else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String date = "";
					String id = "";
					String patient_fullname = "";
					String hour = "";
					boolean t = false;
					boolean y = false;
					boolean x = false;
					String tmpid = "", tmpfullname = "", tmpdate = "";
					for (int i = 0; i < result.length(); i++) {
						if (!t && !y && !x && result.charAt(i) != '$') {
							id += result.charAt(i);
						}
						if (t && !y && !x && result.charAt(i) != '#') {
							patient_fullname += result.charAt(i);
						}
						if (t && y && !x && result.charAt(i) != '&') {
							date += result.charAt(i);
						}
						if (t && y && x && result.charAt(i) != '*') {
							hour += result.charAt(i);
						}
						if (result.charAt(i) == '$') {
							tmpid = id;
							id = "";
							t = true;
						}
						if (result.charAt(i) == '#') {
							tmpfullname = patient_fullname;
							patient_fullname = "";
							y = true;
						}

						if (result.charAt(i) == '&') {
							tmpdate = date;
							date = "";
							x = true;
						}
						if (result.charAt(i) == '*') {//tmpid- appid /// tmpfullname-patient adý soyadý/// 
							///////////////////////
							LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							final View cancel_item = inflater.inflate(R.layout.cancel_item, null);
							if (!tmpfullname.equals("1 1")) {
								((TextView) cancel_item
										.findViewById(R.id.date_cancel))
										.setText(tmpdate);
								((TextView) cancel_item
										.findViewById(R.id.hour_cancel))
										.setText(hours[Integer.parseInt(hour)]);
								((TextView) cancel_item
										.findViewById(R.id.sector_cancel))
										.setVisibility(View.GONE);
								((TextView) cancel_item
										.findViewById(R.id.doctorf_cancel))
										.setVisibility(View.GONE);
								((TextView) cancel_item
										.findViewById(R.id.TextView01))
										.setVisibility(View.GONE);
								((TextView) cancel_item
										.findViewById(R.id.doctorl_cancel))
										.setText(tmpfullname);
								final TextView feedbackTextView = (TextView) cancel_item
										.findViewById(R.id.feedbackTextView);
								selectFeedbackOfAppointment(
										Integer.parseInt(tmpid),
										feedbackTextView);
								Button but = (Button) cancel_item
										.findViewById(R.id.cancelButtonAppointment);
								but.setText("Upload Feedback");
								but.setContentDescription(tmpid + "");
								SimpleDateFormat format = new SimpleDateFormat(
										"dd-MM-yyyy");
								Date currentdate = new Date();
								Date appdate = null;
								try {
									appdate = format.parse(tmpdate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (currentdate.compareTo(appdate) <= 0) {
									but.setClickable(false);
									but.setText("Upcoming appointment");
									but.setBackgroundResource(R.color.transparent);
								} else {
									but.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(final View v) {
											// TODO Auto-generated method stub
											Builder alert = new Builder(
													activity);
											final EditText input = new EditText(
													activity);
											input.setInputType(InputType.TYPE_CLASS_TEXT
													| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
											input.setHint("Here...");
											alert.setView(input);
											alert.setTitle("Upload Feedback");
											alert.setPositiveButton(
													"Upload",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															//.trim();//asdasf
															uploadFeedback(
																	v.getContentDescription()
																			.toString(),
																	input.getText()
																			.toString(),
																	activity,
																	feedbackTextView);
															dialog.dismiss();
														}
													});

											alert.setNegativeButton(
													"Cancel",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															dialog.dismiss();
														}
													});
											alert.show();
										}
									});
								}
								((LinearLayout)activity.findViewById(R.id.cancelLinearLayout)).addView(cancel_item);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.MATCH_PARENT,
										10);
								LinearLayout linearLayout = new LinearLayout(activity);
								linearLayout.setLayoutParams(lp);
								linearLayout.setBackgroundResource(R.color.transparent);
								((LinearLayout)activity.findViewById(R.id.cancelLinearLayout)).addView(linearLayout);
							}
							///////////////////////
							hour = "";
							t = false;
							y = false;
							x = false;
						}
					}
				}
			}
		};
		async.execute();
	}
	
	/**
	 * @param activity
	 */
	public void checkDatabaseConnection(final Activity activity){
		final String php = "check_db_connection.php";
		
		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				/*
				if (result != 1) {
					
					Builder builder = new Builder(activity);
					builder.setTitle("Warning !");
					builder.setMessage("Service is unavailable. Please try again later.");
					((TextView) activity.findViewById(R.id.infoSplash)).setText("No service");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									activity.finish();
									dialog.dismiss();
								}
							});
					builder.show();
				}*/
				//else{
					Intent main = new Intent(activity,
							MainActivity.class);
					activity.startActivity(main);
					activity.finish();
				//}
			}
		};
		async.execute();
	}

	/**
	 * @param a
	 * @param activity
	 * @param not_available_hours
	 * @param hours
	 * @param _is
	 */
	public void selectAppointmentsOfDoctorbyDate(final Appointment a,
			final Activity activity,
			final ArrayList<Integer> not_available_hours, final String[] hours,final boolean _is) {

		final String php = "select_appointments_doctor_by_date.php";

		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("Doctor_username", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(a.getDoctor_username(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("appointment_date", "UTF-8")
							+ "=" + URLEncoder.encode(a.getDate(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("")) {
						
					} else {
						String x = "";
						for (int i = 0; i < sb.toString().length(); i++) {
							if (sb.toString().charAt(i) == '$') {
								not_available_hours.add(Integer.parseInt(x));
								x = "";
							} else {
								x += sb.toString().charAt(i);
							}
						}
					}
					return "success";
				} catch (Exception ex) {
					ex.printStackTrace();
					return "error";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.equals("error")) {
					if (p.isShowing()) {
						p.dismiss();
					}
					alertDialog(activity, "Please try again later.");
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					//
					for (int i = 0; i < hours.length; i++) {

						LayoutInflater inflater = (LayoutInflater) activity
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final View hour_item = inflater.inflate(
								R.layout.appointment_hour_item, null);
						final Button but = (Button) hour_item
								.findViewById(R.id.hour_button);
						but.setText(hours[i]);
						but.setContentDescription(i + "");
						if (!not_available_hours.contains(i)) {
							if(_is){
								but.setOnClickListener(new OnClickListener() {//patient

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									a.setHour(v.getContentDescription()
											.toString());

									Builder builder = new Builder(
											activity);
									builder.setTitle("Approval !");
									builder.setMessage(activity.getTitle()
											.toString()
											+ "\r\nDate:"
											+ a.getDate()
											+ "\r\nHour:"
											+ hours[Integer.parseInt(a
													.getHour())]
											+ "\r\nDo you want to request this appointment?");
									builder.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
													a.InsertAppointment(activity);
												}
											});
									builder.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											});
									builder.show();
								}
							});
							}else{
								but.setOnClickListener(new OnClickListener() {//doctor

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										a.setHour(v.getContentDescription()
												.toString());

										Builder builder = new Builder(
												activity);
										builder.setTitle("Approval !");
										builder.setMessage("Date:"
												+ a.getDate()
												+ "\r\nHour:"
												+ hours[Integer.parseInt(a
														.getHour())]
												+ "\r\nDo you want to block this hour?");
										builder.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
														but.setBackgroundResource(R.drawable.linearlayout_bg_red);
														LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
																LinearLayout.LayoutParams.MATCH_PARENT,
																LinearLayout.LayoutParams.WRAP_CONTENT);
														lp.setMargins(10, 10, 10, 10);
														hour_item.setLayoutParams(lp);
														a.InsertAppointment(activity);
														((LinearLayout)activity.findViewById(R.id.scheduleLinearLayout)).removeAllViews();
														selectAppointmentsOfDoctorbyDate(a, activity, new ArrayList<Integer>(), hours, _is);
													}
												});
										builder.setNegativeButton(
												"No",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
													}
												});
										builder.show();
									}
								});
							}
						} else {
							but.setBackgroundResource(R.drawable.linearlayout_bg_red);
							if (!_is) {
								but.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

										final Appointment ap = new Appointment();
										ap.setDoctor_username(a
												.getDoctor_username());
										ap.setPatient_tc_no("1");
										ap.setHour(v.getContentDescription()
												.toString());
										ap.setDate(a.getDate());
										Builder builder = new Builder(
												activity);
										builder.setTitle("Approval !");
										builder.setMessage("Date:"
												+ ap.getDate()
												+ "\r\nHour:"
												+ hours[Integer.parseInt(ap ///adasdsadaasa
														.getHour())]
												+ "\r\nDo you want to unblock this hour?");
										builder.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
														but.setBackgroundResource(R.drawable.linearlayout_bg);
														LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
																LinearLayout.LayoutParams.MATCH_PARENT,
																LinearLayout.LayoutParams.WRAP_CONTENT);
														lp.setMargins(9, 9, 9,
																9);
														hour_item
																.setLayoutParams(lp);
														deleteBlock(ap,
																activity);
														((LinearLayout)activity.findViewById(R.id.scheduleLinearLayout)).removeAllViews();
														selectAppointmentsOfDoctorbyDate(ap, activity, new ArrayList<Integer>(), hours, _is);
													}
												});
										builder.setNegativeButton(
												"No",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
													}
												});
										builder.show();
									}
								});
							}
							else{
								but.setClickable(false);
							}
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							lp.setMargins(9, 9, 9, 9);
							hour_item.setLayoutParams(lp);
						}
						((LinearLayout) activity
								.findViewById(R.id.scheduleLinearLayout))
								.addView(hour_item);

					}
				}
			}
		};
		async.execute();
	}
	
	/**
	 * @param a
	 * @param activity
	 */
	public void deleteBlock(final Appointment a,final Activity activity){
		final String php = "delete_block_doctor.php";
		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("hour", "UTF-8") + "="
							+ URLEncoder.encode(a.getHour(), "UTF-8");
					data += "&" + URLEncoder.encode("date", "UTF-8") + "="
							+ URLEncoder.encode(a.getDate(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("Doctor_username", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(a.getDoctor_username(), "UTF-8");
					data += "&"
							+ URLEncoder.encode("Patient_tc_number", "UTF-8")
							+ "="
							+ URLEncoder.encode(a.getPatient_tc_no(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("Unblocked.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
				} else {
					String message;
					if (result == 0) {
						message = "This is an appointment of patient.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}

	
	/**
	 * @param patient
	 * @param activity
	 */
	public void selectInfoPatient(final Patient patient, final Activity activity) {
		final String php = "select_fl_name_patient.php";

		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("tc_no", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(patient.getTc_number(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					return sb.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result.equals("")) {

					int x = result.indexOf("$");
					int y = result.indexOf("#");
					patient.setF_name(result.substring(0, x));
					patient.setL_name(result.substring(x + 1, y));
					activity.setTitle(patient.getF_name() + " "
							+ patient.getL_name());
					// eheheehe
				} else {

					alertDialog(activity, "Please try again later.");
				}
			}
		};
		async.execute();
	}

	
	/**
	 * @param doctor
	 * @param activity
	 */
	public void selectInfoDoctor(final Doctor doctor, final Activity activity) {
		final String php = "select_flsector_of_doctor.php";

		AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("username", "UTF-8") + "="
							+ URLEncoder.encode(doctor.getUsername(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString() + doctor.getUsername());
					return sb.toString();
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result.equals("")) {
					int x = result.indexOf("$");
					int y = result.indexOf("#");
					int z = result.indexOf("&");
					doctor.setF_name(result.substring(0, x));
					doctor.setL_name(result.substring(x + 1, y));
					doctor.setSector(result.substring(y + 1, z));
					activity.setTitle("Dr." + doctor.getF_name() + " "
							+ doctor.getL_name());
					// eheheehe
				} else {
					alertDialog(activity, "Please try again later.");
				}
			}
		};
		async.execute();
	}

	/**
	 * @param sector
	 * @param activity
	 * @param is_logged
	 * @param is_doctor
	 * @param patient
	 */
	public void selectInfoDoctorbySector(final String sector,
			final Activity activity, final boolean is_logged,
			final boolean is_doctor, final Patient patient) {

		final String php = "select_ufldoctor_bysector.php";

		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;
			ArrayList<Doctor> doctors;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				doctors = new ArrayList<Doctor>();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("sector", "UTF-8") + "="
							+ URLEncoder.encode(sector, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					if (!sb.toString().equals("")) {
						String lastname = "";
						String username = "";
						String firstname = "";
						boolean t = false;
						boolean y = false;
						String tmpusername = "", tmpfirstname = "";
						for (int i = 0; i < sb.toString().length(); i++) {
							if (!t && !y && sb.toString().charAt(i) != '$') {
								username += sb.toString().charAt(i);
							}
							if (t && !y && sb.toString().charAt(i) != '#') {
								firstname += sb.toString().charAt(i);
							}
							if (t && y && sb.toString().charAt(i) != '&') {
								lastname += sb.toString().charAt(i);
							}
							if (sb.toString().charAt(i) == '$') {
								tmpusername = username;
								username = "";
								t = true;
							}
							if (sb.toString().charAt(i) == '#') {
								tmpfirstname = firstname;
								firstname = "";
								y = true;
							}
							if (sb.toString().charAt(i) == '&') {
								Doctor temp_doctor = new Doctor();
								temp_doctor.setUsername(tmpusername);
								temp_doctor.setF_name(tmpfirstname);
								temp_doctor.setL_name(lastname);
								doctors.add(temp_doctor);
								lastname = "";
								t = false;
								y = false;
							}
						}
						return 1;
					} else {
						return -1;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return 0;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (p.isShowing()) {
					p.dismiss();
				}
				if (result == 1) {
					final Dialog dialog = new Dialog(activity);
					dialog.setContentView(R.layout.doctorlist_dialog);
					TableLayout tablelayoutlist = (TableLayout) dialog
							.findViewById(R.id.doctorListTableLayout);
					dialog.setTitle("Doctors of " + sector);
					for (int j = 0; j < doctors.size(); j++) {
						LayoutInflater inflater = (LayoutInflater) activity
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View newdoctor = inflater.inflate(
								R.layout.doctor_list_item, null);
						TextView fname = (TextView) newdoctor
								.findViewById(R.id.doctorFirstnameTextView);
						TextView lname = (TextView) newdoctor
								.findViewById(R.id.doctorLastnameTextView);
						Button but = (Button) newdoctor
								.findViewById(R.id.requestAppFromList);

						fname.setText(doctors.get(j).getF_name());
						lname.setText(doctors.get(j).getL_name());
						but.setContentDescription(doctors.get(j).getUsername());

						but.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								// burdan sonra o doktor için tarih saat
								// secilecek.
								Intent schedule = new Intent(activity,
										ScheduleActivity.class);
								schedule.putExtra("SECTOR_NAME", sector);
								schedule.putExtra("DOCTOR_USERNAME",
										v.getContentDescription());
								schedule.putExtra(
										"DOCTOR_FULLNAME",
										((TextView) ((LinearLayout) v
												.getParent())
												.findViewById(R.id.doctorFirstnameTextView))
												.getText().toString()
												+ " "
												+ ((TextView) ((LinearLayout) v
														.getParent())
														.findViewById(R.id.doctorLastnameTextView))
														.getText().toString());
								schedule.putExtra("PATIENT_TCNO",
										patient.getTc_number());
								schedule.putExtra("BOOLEAN", true);
								activity.startActivity(schedule);
								dialog.dismiss();
							}
						});

						if (!is_logged) {
							but.setVisibility(View.GONE);
							fname.setTextSize(24);
							lname.setTextSize(24);
						}
						if (is_doctor) {
							but.setVisibility(View.GONE);
							fname.setTextSize(24);
							lname.setTextSize(24);
						}
						tablelayoutlist.addView(newdoctor);//////////////////////////////////
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								6);
						LinearLayout linearLayout = new LinearLayout(activity);
						linearLayout.setLayoutParams(lp);
						linearLayout.setBackgroundResource(R.color.holo_blue);
						tablelayoutlist.addView(linearLayout);
					}
					dialog.show();
				}
				if (result == -1) {
					alertDialog(activity,
							"Could not find a doctor from section you selected.");
				}
				if (result == 0) {
					alertDialog(activity, "Please try again later.");
				}
			}
		};
		async.execute();
	}


	
	/**
	 * @param doctor
	 * @param activity
	 */
	public void doctorLogin(final Doctor doctor, final Activity activity) {

		final String php = "doctor_login.php";

		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("username", "UTF-8") + "="
							+ URLEncoder.encode(doctor.getUsername(), "UTF-8");
					data += "&" + URLEncoder.encode("password", "UTF-8") + "="
							+ URLEncoder.encode(doctor.getPassword(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("You have logged in.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					builder.show();
					selectInfoDoctor(doctor, activity);
					/*
					 * linearLayoutLogin.setVisibility(View.GONE);
					 * LinearLayoutApp.setVisibility(View.VISIBLE);
					 */
					activity.findViewById(R.id.loginLayout).setVisibility(
							View.GONE);
					// eheheehe
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String message;
					if (result == 0) {
						message = "You have entered wrong username or password.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}

	
	/**
	 * @param patient
	 * @param activity
	 */
	public void patientLogin(final Patient patient, final Activity activity) {
		final String php = "patient_login.php";

		AsyncTask<Void, Void, Integer> async = new AsyncTask<Void, Void, Integer>() {

			ProgressDialog p;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				p = new ProgressDialog(activity);
				p.setCancelable(false);
				p.setTitle("Please wait..");
				p.setMessage("Processing..");
				if (!p.isShowing()) {
					p.show();
				}
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String link = ip + php;
				try {
					String data = URLEncoder.encode("tc_no", "UTF-8")
							+ "="
							+ URLEncoder
									.encode(patient.getTc_number(), "UTF-8");
					data += "&" + URLEncoder.encode("password", "UTF-8") + "="
							+ URLEncoder.encode(patient.getPassword(), "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							conn.getOutputStream());
					wr.write(data);
					wr.flush();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					Log.w("sout", sb.toString());
					if (sb.toString().equals("1")) {
						return 1;
					} else {
						return 0;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return -1;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					if (p.isShowing()) {
						p.dismiss();
					}
					Builder builder = new Builder(activity);
					builder.setTitle("Succesfull !");
					builder.setMessage("You have logged in.");
					builder.setPositiveButton("OK",
							new Dialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					builder.show();
					selectInfoPatient(patient, activity);
					/*
					 * linearLayoutLogin.setVisibility(View.GONE);
					 * LinearLayoutApp.setVisibility(View.VISIBLE);
					 */
					activity.findViewById(R.id.loginLayout).setVisibility(
							View.GONE);
					activity.findViewById(R.id.LinearLayoutRequestApp)
							.setVisibility(View.VISIBLE);

					// eheheehe
				} else {
					if (p.isShowing()) {
						p.dismiss();
					}
					String message;
					if (result == 0) {
						message = "You have entered wrong TC number or password.";
					} else {
						message = "Please try again later.";
					}
					alertDialog(activity, message);
				}
			}
		};
		async.execute();
	}

	/**
	 * @param activity
	 * @param message
	 */
	public void alertDialog(Activity activity, String message) {
		Builder builder = new Builder(activity);
		builder.setTitle("ERROR");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
