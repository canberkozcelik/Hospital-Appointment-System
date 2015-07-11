package com.canberk.hospitalappointmentsystem.methods;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.canberk.hospitalappointmentsystem.database.DatabaseConnector;

/**
 * A class for Mail.
 * sends mail to associated user.
 * contains a method called validEmailChecker for valid email checking.
 */
public class Mail {
	
	final String to;
	final String subject;
	final String body;
	private static String ip;
	
	/**
	 * @param to String to represent receiver of mail
	 * @param subject String to represent subject of mail
	 * @param body  String to represent body of mail
	 */
	public Mail(String to,String subject,String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.setIp(new DatabaseConnector().ip);
	}
	
	/**
	 * @return
	 */
	public String getTo() {
		return to;
	}

	

	/**
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	

	/**
	 * @return
	 */
	public String getBody() {
		return body;
	}

	/**
	 * gets parameter as Activity , and sends mail to this
	 * @param activity to send mail from
	 */
	public void sendNewPassword(final Activity activity){
		final String php = "sendmail.php";
		final String new_password = new PasswordGenerator(4).generate();
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
				String link = getIp() + php;
				try {
					String data = URLEncoder.encode("to", "UTF-8") + "="
							+ URLEncoder.encode(to, "UTF-8");
					data += "&" + URLEncoder.encode("subject", "UTF-8") + "="
							+ URLEncoder.encode(subject, "UTF-8");
					data += "&" + URLEncoder.encode("bodyx", "UTF-8") + "="
							+ URLEncoder.encode(body+new_password, "UTF-8");

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
				DatabaseConnector db = new DatabaseConnector();
				if (p.isShowing()) {
					p.dismiss();
				}
				if (result == 1) {
					db.updatePassword(to, PasswordEncryption.SHA1(new_password), activity);
				} 
				if(result == 0) {
					db.alertDialog(activity, "That e-mail address doesn't have an associated user account. Are you sure you've registered?");
				}
				if(result == -1) {
					db.alertDialog(activity, "Please try again later.");
				}
			}
		};
		async.execute();
	}
	
	public void sendFeedback(){
		
	}
	
	
	
	/**
	 * Valid email checker
	 * @param email to check is valid or not
	 * @return TRUE if email is valid or FALSE is not valid.
	 */
	public static boolean validEmailChecker(String email){
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * @return
	 */
	public static String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 */
	public void setIp(String ip) {
		Mail.ip = ip;
	}
}
