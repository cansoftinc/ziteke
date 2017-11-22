/**
 * 
 */
package com.dun.ziteke;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dun.ziteke.database.dbTrackerActivity;

/**
 * @author Duncan
 *
 */
public class RecoverPassword extends dbTrackerActivity {

	String recover_email;
	String msg;
	String TAG = "RECOVER PASSWORD FORM";
	
	ProgressDialog progressDialog, progressDialog2;

	private ConnectivityManager connec;

	private static String urlcon = " http://ziteke.ebitsonline-vws.com/mobile-forgotten-pswrd.php?";
	Boolean OK = false;

	DataInputStream inputstream;
	DataOutputStream dataStream;
	URLConnection conn;
	URL url;
	String result;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recover);
		
		final EditText email = (EditText)findViewById(R.id.edemail);
		Button back =(Button)findViewById(R.id.btnback);
		Button submit = (Button)findViewById(R.id.btnsubmit);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recover_email = email.getText().toString();
				if(recover_email.length()==0||recover_email.equals("")){					
					//ALERTDIALOG empty EditView
					AlertDialog al = createEmptyEmailAlert();
					al.show();
					
				}else{
					//submit email
					isInternetOk();
				}
			}
		});
		
		
		back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myintent = null;
				myintent = new Intent(RecoverPassword.this, Login.class);
				
				RecoverPassword.this.startActivity(myintent);
				RecoverPassword.this.finish();
			}
		});
	}
	
	@SuppressWarnings("static-access")
	protected void isInternetOk() {
		// TODO Auto-generated method stub
		progressDialog = progressDialog.show(this, "", "Checking Internet connection");
		new Thread(){
			public void run(){
				Message msg = Message.obtain();
				msg.what = 1;
				connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				messageHandler.sendMessage(msg);
			}
		}.start();
		return;
		
	}

	private Handler messageHandler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// ARE WE CONNECTED TO THE NET
				progressDialog.dismiss();
				Log.v(TAG, "Inside handler for internet on");
				if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
						|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
						|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
						|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
					update();
					Log.v(TAG, "Internet is on");

				} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
						|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
					Log.v(TAG, "There is no internet");
					AlertDialog internet = NoInternet();
					internet.show();
				}
				break;

			case 2:
				if (OK) {
					AlertDialog sucess = createSuccessDial();
					sucess.show();

				} else {
					AlertDialog fail = createFailureDial();
					fail.show();
				}
				progressDialog2.dismiss();
				break;
			}
		}
	};
	protected AlertDialog createEmptyEmailAlert() {		
		AlertDialog myNegCredentials = new AlertDialog.Builder(RecoverPassword.this)
				.setMessage("Please input your email address")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg = "OK" + Integer.toString(which);
					}
				}).create();
		return myNegCredentials;
	}

	protected AlertDialog createFailureDial() {
		// TODO Auto-generated method stub
		AlertDialog fail = new AlertDialog.Builder(RecoverPassword.this)
		.setTitle("Server Error")
		.setMessage("Please try later")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int which) {
				// TODO Auto-generated method stub
				msg = "OK" + Integer.toString(which);
			}
		}).create();
		return fail;
	}

	protected AlertDialog createSuccessDial() {
		// TODO Auto-generated method stub
		AlertDialog success = new AlertDialog.Builder(RecoverPassword.this)
		.setTitle("Email Received..")
		.setTitle("Please check your email for the right password to use")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int which) {
				// TODO Auto-generated method stub
				msg = "OK" + Integer.toString(which);
				
				Intent myintent = null;
				myintent = new Intent(RecoverPassword.this, Login.class);
				
				RecoverPassword.this.startActivity(myintent);
				RecoverPassword.this.finish();
			}
		}).create();
		return success;
	}

	protected AlertDialog NoInternet() {
		AlertDialog noNet = new AlertDialog.Builder(RecoverPassword.this)
		.setMessage("No internet Connectivity")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				msg = "OK"+Integer.toString(which);
			}
		}).create();
		return noNet;
	}

	protected void update() {
		// TODO Auto-generated method stub
		progressDialog2 = ProgressDialog.show(this, "Please wait...", "Sending email");
		
		new Thread(){
			public void run(){
				Looper.prepare();
				
				Message msg = Message.obtain();
				msg.what = 2;			
				
				String logg = "email=" + recover_email + "";
				String rs = getResultFromServlet(logg);
				
				if (!rs.equals("0")) {
					String s1;
					if (rs.length() < 4) {
						s1 = "";

					} else {
						s1 = rs.substring(0, 4);
					}
					if (s1.equals("*ZZZ")) {
						rs = rs.substring(4, rs.length());
						Log.v(TAG, "Result is: " + rs);
						// Retrieve and split the receive Response from the
						// server
						String[] tbl = rs.trim().split("YYY");
						if (tbl[0].toLowerCase().equals("ok")) {
							OK = true;						
						}

					}
				} else if (rs.equals("-1")) {
					AlertDialog user = createFailureDial();
					user.show();
				}

				
				messageHandler.handleMessage(msg);		
				Looper.loop();
			}
		}.start();
		
		
	}

	private String getResultFromServlet(String text) {
		String result = "";
		InputStream in = callService(text);
		if (in != null) {
			result = convertStreamToString(in);
			Log.v(TAG, "result NOT NULL " + result);
		} else {
			result = "Error: Service not returning result";
		}
		return result;
	}

	private InputStream callService(String text) {
		InputStream in = null;
		Log.v(TAG, "TRACE LINK ::" + text);

		try {
			url = new URL(urlcon + text);
			conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("POST");
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.connect();

			dataStream = new DataOutputStream(conn.getOutputStream());
			// dataStream.writeBytes(text);
			dataStream.flush();
			dataStream.close();

			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			AlertDialog netproblem = createNetProblem();
			netproblem.show();
		}
		return in;
	}

	private AlertDialog createNetProblem() {
		// TODO Auto-generated method stub
		AlertDialog netProblem = new AlertDialog.Builder(this)
		.setTitle("Internet Failure...")
		.setMessage("Check your Internet connectivity")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				msg = "OK"+Integer.toString(which);
			}
		}).create();
		return netProblem;
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
}
