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
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.widget.TextView;

import com.dun.ziteke.database.dbTrackerActivity;

/**
 * @author Duncan
 * 
 */
public class ProductFormConfirm extends dbTrackerActivity {

	String TAG = "PRODUCT FORM CONFIRMATION";
	int userid;
	String productid, outletid;
	String name, dealer, week, prodqty, productname;
	Double lat, longi;

	TextView tvproductname, tvproductqty;
	Button Cancel, Edit, Confirm;
	ProgressDialog progressDialog, progressDialog2;

	String msg, msg2;

	private ConnectivityManager connec;

	private static String urlcon = "http://ziteke.ebitsonline-vws.com/mobile-receive-productinfo.php?";
	Boolean OK = false;

	DataInputStream inputstream;
	DataOutputStream dataStream;
	URLConnection conn;
	URL url;
	
	String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	.format(new Date());

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productformconfirm);

		Bundle bundle = getIntent().getExtras();
		userid = bundle.getInt("userid");
		name = bundle.getString("name");
		outletid = bundle.getString("outletid");
		dealer = bundle.getString("dealer");
		week = bundle.getString("week");
		productid = bundle.getString("productid");
		productname = bundle.getString("productname");
		prodqty = bundle.getString("strQty");
		lat = bundle.getDouble("longitude");
		longi = bundle.getDouble("latitude");

		TextView proTitle = (TextView)findViewById(R.id.aoutletname);
		proTitle.setText("OUTLET NAME::"+dealer);
		tvproductname = (TextView) findViewById(R.id.tvprodname);
		tvproductname.setText(productname);
		tvproductqty = (TextView) findViewById(R.id.tvqty);
		tvproductqty.setText("Quantity:: "+prodqty);

		Confirm = (Button) findViewById(R.id.pc_save);
		Confirm.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetOK();
			}
		});

		Edit = (Button) findViewById(R.id.pc_edit);
		Edit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProductFormConfirm.this.finish();
			}
		});

		Cancel = (Button) findViewById(R.id.pc_cancel);
		Cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ProductFormConfirm.this.finish();
			}
		});

	}

	// CHECK FOR INTERNET METHOD
	public final boolean isInternetOK() {
		Log.v(TAG, "inside IsInterneton method...");
		progressDialog2 = ProgressDialog.show(this, "",
				"Checking Internet Connection...");
		new Thread() {

			public void run() {
				Message msg = Message.obtain();
				msg.what = 1;
				connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				messageHandler.sendMessage(msg);
			}
		}.start();
		return false;

	}

	private Handler messageHandler = new Handler() {
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// ARE WE CONNECTED TO THE NET
				progressDialog2.dismiss();
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
				progressDialog.dismiss();
				break;

			}
		}
	};

	protected AlertDialog NoInternet() {
		// TODO Auto-generated method stub
		AlertDialog userdialog = new AlertDialog.Builder(
				ProductFormConfirm.this)
				.setTitle("Error")
				.setMessage(
						"Please Check Your network connection settings to access the internet")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						msg = "Ok" + Integer.toString(which);
					}
				}).create();
		return userdialog;
	}

	private AlertDialog createSuccessDial() {
		// TODO Auto-generated method stub
		Log.v(TAG, "inside success dialogue..");
		AlertDialog myPosCredentials = new AlertDialog.Builder(
				ProductFormConfirm.this)
				.setMessage("Product Submitted Successfully")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);

						Intent myintent = null;
						myintent = new Intent(ProductFormConfirm.this,
								Options.class);

						Bundle bundle = new Bundle();
						bundle.putInt("userid", userid);						
						bundle.putString("user", name);
						bundle.putString("outletid", outletid);
						bundle.putString("dealer", dealer);
						bundle.putString("week", week);
						bundle.putDouble("lat", lat);
						bundle.putDouble("longi", longi);
						myintent.putExtras(bundle);

						ProductFormConfirm.this.startActivity(myintent);
						ProductFormConfirm.this.finish();
						
					}
				}).create();
		return myPosCredentials;
	}

	public void update() {
		progressDialog = ProgressDialog.show(this, "Please wait...",
				"Submitting Product Info.");

		new Thread() {
			public void run() {
				Looper.prepare();

				Message msg = Message.obtain();
				msg.what = 2;
//				userid=2&outletId=1&latitude=0.00&longitude=0.00&currentdate=2012-03-19 12:30:25&ProductId=31&Quantity=10
				String logg = "userid=" + userid + "&outletId=" + outletid
						+ "&latitude=" + lat + "&longitude=" + longi
						+ "&currentdate=" + currentTimeString + "&ProductId="
						+ productid + "&Quantity=" + prodqty + "";
				
				Log.v(TAG, "Server Variable::"+logg);
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
					AlertDialog user = createUserLog("Server unreachable");
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

	private AlertDialog createFailureDial() {

	
		AlertDialog myNegCredentials = new AlertDialog.Builder(
				ProductFormConfirm.this)
				.setTitle("Server Error...")
				.setMessage("Please try later.")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);
					}
				}).create();
		return myNegCredentials;
	}

	private AlertDialog createUserLog(String lon) {
		// TODO Auto-generated method stub
		AlertDialog userdialog = new AlertDialog.Builder(
				ProductFormConfirm.this).setTitle("Server Error").setMessage(lon)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						msg = "Ok" + Integer.toString(which);
					}
				}).create();
		return userdialog;
	}

	private AlertDialog createNetProblem() {
		// TODO Auto-generated method stub
		AlertDialog myNetProb = new AlertDialog.Builder(ProductFormConfirm.this)
				.setMessage("Network Connection Problem")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);
					}
				}).create();
		return myNetProb;
	}

}
