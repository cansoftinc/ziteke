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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dun.ziteke.database.dbTrackerActivity;
import com.dun.ziteke.database.dbTrackerDatabase.category;
import com.dun.ziteke.database.dbTrackerDatabase.outlet;
import com.dun.ziteke.database.dbTrackerDatabase.productList;
import com.dun.ziteke.database.dbTrackerDatabase.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends dbTrackerActivity {
	String TAG = "LOGIN FORM";
	String msg = null, msg2 = null;
	String userid = null, name=null;
	String strUser = null, strPass = null;
	int Userid;
	EditText username, password;
	Button login, reset, exit;
	ProgressDialog progressDialog, progressDialog2;

	private ConnectivityManager connec;

	private static String urlcon = "some login url....";
	Boolean OK = false;

	DataInputStream inputstream;
	DataOutputStream dataStream;
	URLConnection conn;
	URL url;
	String result;
	String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(new Date());
	
	int time = new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
	int ye = new GregorianCalendar().get(Calendar.YEAR);
	String saa = Integer.toString(time);
	String mwaka = Integer.toString(ye);
	String week = mwaka+saa;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		TextView tvCustom = (TextView) findViewById(R.id.custom);
		tvCustom.setGravity(Gravity.CENTER_HORIZONTAL);
		Typeface myNewFace = Typeface.createFromAsset(getAssets(),
				"fonts/LavanderiaSturdy.otf");
		tvCustom.setTypeface(myNewFace);

		String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		Log.v(TAG, currentTimeString);
		Toast.makeText(this, "Date today is :: " + currentTimeString,
				Toast.LENGTH_LONG).show();

		username = (EditText) findViewById(R.id.edusername);
		username.requestFocus();
		password = (EditText) findViewById(R.id.edpassword);
		login = (Button) findViewById(R.id.btnlogin);
		reset = (Button) findViewById(R.id.btnreset);
		exit = (Button) findViewById(R.id.btnexit);

		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				strUser = username.getText().toString().trim();
				strPass = password.getText().toString().trim();
				// validate username and password inputs ::
				if (strUser == "" || strPass == "" || strUser.length() == 0
						|| strPass.length() == 0) {
					Log.v(TAG, "Checking user credentials");

					AlertDialog user = createUserLog("Please Enter the Username and Password");
					user.show();
					username.requestFocus();
				} else {
					isInternetOK();
				}
				// Log.v(TAG, "Inside Login Click event");
				//
				// // Save record database
				// SQLiteDatabase db = mDatabase.getWritableDatabase();
				// db.beginTransaction();
				//
				// try {
				// // long rowId = 0;
				// strUser = username.getText().toString().toLowerCase().trim();
				// strPass = password.getText().toString().toLowerCase().trim();
				//
				//
				// // SQL Query
				// SQLiteQueryBuilder sq = new SQLiteQueryBuilder();
				// sq.setTables(user.USER_TABLE_NAME);
				// sq.appendWhere(user.USER_NAME + "='" + strUser + "' AND " +
				// user.USER_PASSWORD + "='" + strPass + "'");
				//
				// String asColumnsToReturn[] = { user.USER_ID, user.USER_NAME,
				// user.USER_PASSWORD };
				// String strSortOrder = user.DEFAULT_SORT_ORDER;
				//
				// // run cursor
				// Cursor c = sq.query(db, asColumnsToReturn, null, null, null,
				// null, strSortOrder);
				// Log.v(TAG, "TRACE SQLITE where ::" + user.USER_NAME + "='"
				// + strUser + "' AND " + user.USER_PASSWORD + "='"
				// + strPass);
				//
				// if (c.getCount() == 0) {
				//
				// // Log.v(TAG, "Added new user");
				// // add value to our table
				// /*
				// * ContentValues userRecordToAdd = new ContentValues();
				// * userRecordToAdd.put(user.USER_NAME, strUser);
				// * userRecordToAdd.put(user.USER_PASSWORD, strPass);
				// *
				// * userRecordToAdd.put(user.USER_LASTLOGIN, curTime);
				// * rowId = db.insert(user.USER_TABLE_NAME,
				// * user.USER_NAME, userRecordToAdd);
				// */
				//
				// // User does not exist
				// Log.v(TAG, "User does not exist");
				// AlertDialog fail = createFailureDial();
				// fail.show();
				//
				// } else {
				// c.moveToFirst();
				// for (int i = 0; i < c.getCount(); i++) {
				// int id = c.getInt(0);
				// Userid = id;
				// String name = c.getString(1);
				// System.out.println("Trace :: " + id);
				// System.out.println("Trace1 :: " + name);
				// c.moveToNext();
				// }
				//
				// // user exists
				// Log.v(TAG, "User Exists");
				// AlertDialog success = createSuccessDial();
				// success.show();
				//
				// }
				// c.close();
				// db.setTransactionSuccessful();
				//
				// } finally {
				// db.endTransaction();
				// }
				//
				// db.close();
			}
		});

		reset.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent myintent = null;
				myintent = new Intent(Login.this, RecoverPassword.class);

				Login.this.startActivity(myintent);
				Login.this.finish();
			}
		});

		exit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				AlertDialog diaBox = createDialogBox();
				diaBox.show();
			}
		});

	}

	private AlertDialog createUserLog(String lon) {
		// TODO Auto-generated method stub
		AlertDialog userdialog = new AlertDialog.Builder(Login.this)
				.setTitle("Error").setMessage(lon)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						msg = "Ok" + Integer.toString(which);
					}
				}).create();
		return userdialog;
	}

	private AlertDialog createFailureDial() {

		Log.v(TAG, "inside failure dialogue..");
		AlertDialog myNegCredentials = new AlertDialog.Builder(Login.this)
				.setMessage("Wrong user name or password")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);
					}
				}).create();
		return myNegCredentials;
	}

	private AlertDialog createSuccessDial() {
		// TODO Auto-generated method stub
		Log.v(TAG, "inside success dialogue..");
		AlertDialog myPosCredentials = new AlertDialog.Builder(Login.this)
				.setMessage(
						"Welcome, " + name
								+ ". You are successfully logged on")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);

						Intent myintent = null;
						myintent = new Intent(Login.this, Route.class);

						Bundle bundle = new Bundle();
						System.out.println("Trace ;: " + Userid);
						bundle.putInt("userid", Userid);
						bundle.putString("user", name);
						bundle.putString("week", week);
						myintent.putExtras(bundle);

						Login.this.startActivity(myintent);
						Login.this.finish();
					}
				}).create();
		return myPosCredentials;
	}

	private AlertDialog createDialogBox() {
		// TODO Auto-generated method stub
		AlertDialog myQuittingDialogBox = new AlertDialog.Builder(Login.this)
				.setTitle("Confirm Exit")
				.setMessage("Are You sure you want to exit")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								msg = "Yes" + Integer.toString(which);
								Login.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg = "No" + Integer.toString(which);
					}
				}).create();
		return myQuittingDialogBox;
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
		AlertDialog userdialog = new AlertDialog.Builder(Login.this)
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

	public void update() {
		progressDialog = ProgressDialog.show(this, "Please wait...",
				"Verifying user credentials.");

		new Thread() {
			public void run() {
				Looper.prepare();

				Message msg = Message.obtain();
				msg.what = 2;

				strUser = username.getText().toString().toLowerCase().trim();
				strPass = password.getText().toString().toLowerCase().trim();
				String logg = "uname=" + strUser + "&pwrd=" + strPass + "";
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
						if (tbl[0].toLowerCase().equals("correct")) {
							OK = true;
							SQLiteDatabase db = mDatabase.getWritableDatabase();

							// TODO CHANGE outlet, productlist and category
							// before inserting
							System.out
									.println("~~~~~~~~~~~~~~~~~Split :~~~~~~~~~~~~~\n");
							// DELETE OUTLET DATA, PRODUCTLIST DATA, CATEGORY
							// LIST DATA
							db.execSQL("DELETE FROM  "
									+ outlet.OUTLET_TABLE_NAME);
							db.execSQL("DELETE FROM  "
									+ productList.PRODUCT_LIST_TABLE_NAME);
							db.execSQL("DELETE FROM  "
									+ category.eCAT_TABLE_NAME);

							// Process USER DETAILS
							System.out.println("\nUSER:" + tbl[1]);
							String[] us = tbl[1].trim().split(":::");
							// for (int i = 0; i < us.length; i++) {
							// String[] oul = us[i].split(":::");
							System.out.println("\n ->" + us[0] + " " + us[1]);
							ContentValues uservalues= new ContentValues();
							uservalues.put(user.USER_ID, us[0]);
							uservalues.put(user.USER_NICKNAME, us[1]);
							uservalues.put(user.USER_NAME, strUser);
							uservalues.put(user.USER_PASSWORD, strPass);
							uservalues.put(user.USER_LASTLOGIN,	currentTimeString);
							db.insert(user.USER_TABLE_NAME, user.USER_NAME, uservalues);
							
							Userid  = Integer.parseInt(us[0]);
							name = us[1];
							
							// }

							System.out.println("\nSPLIT OUTLET :" + tbl[2]);

							String[] ou = tbl[2].trim().split("xxx");
							for (int i = 0; i < ou.length; i++) {

								String[] oul = ou[i].split(":::");
								System.out.println("\n ->" + oul[0] + " "
										+ oul[1]);
								ContentValues loc = new ContentValues();
								loc.put(outlet.OUTLET_ID, oul[0]);
								loc.put(outlet.OUTLET_NAME, oul[1]);
								db.insert(outlet.OUTLET_TABLE_NAME,
										outlet.OUTLET_ID, loc);

							}

							System.out.println("\nSPLIT PRODUCT :" + tbl[3]);
							String[] pr = tbl[3].trim().split("XXX");
							for (int i = 0; i < pr.length; i++) {
								String[] prod = pr[i].split(":::");
								System.out.println("\n ->" + prod[0] + " "
										+ prod[1]);
								ContentValues loc = new ContentValues();
								loc.put(productList.PRODUCT_LIST_ID, prod[0]);
								loc.put(productList.PRODUCT_LIST_NAME, prod[1]);
								loc.put(productList.PRODUCT_LIST_CATEGORY,
										prod[2]);
								db.insert(productList.PRODUCT_LIST_TABLE_NAME,
										productList.PRODUCT_LIST_NAME, loc);
							}

							System.out.println("\nSPLIT CATEGORY :" + tbl[4]);
							String[] cat = tbl[4].trim().split("XXX");
							for (int i = 0; i < cat.length; i++) {
								String[] catod = cat[i].split(":::");
								System.out.println("\n ->" + catod[0] + " "
										+ catod[1]);
								ContentValues loc = new ContentValues();
								loc.put(category.eCAT_IDs, catod[0]);
								loc.put(category.eCAT_NAME, catod[1]);
								db.insert(category.eCAT_TABLE_NAME,
										category.eCAT_NAME, loc);

							}
							System.out
									.println("~~~~~~~~~~~~~~~~~FIN Split:~~~~~~~~~~~~~\n");

							db.close();
						}

					}
				} else if (rs.equals("-1")) {
					AlertDialog user = createUserLog("Ensure the Username does exist and is Active");
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

	private AlertDialog createNetProblem() {
		// TODO Auto-generated method stub
		AlertDialog myNetProb = new AlertDialog.Builder(Login.this)
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
