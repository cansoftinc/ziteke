/**
 * 
 */
package com.dun.ziteke;

import com.dun.ziteke.database.dbTrackerActivity;
import com.dun.ziteke.database.dbTrackerDatabase.outlet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Duncan
 * 
 */
public class Route extends dbTrackerActivity {

	String TAG = "ROUTE FORM ";
	Bundle bundle;
	String name, dealer, week, lo, la;
	int userid;

	String msg, msg2;
	String outletid = "";
	TableLayout proF;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route);

		bundle = getIntent().getExtras();
		name = bundle.getString("user");
		userid = bundle.getInt("userid");
		week = bundle.getString("week");

		// TableLayout where we want to Display list
		proF = (TableLayout) findViewById(R.id.TableLayout_route);
		
		TextView aTitle = (TextView) findViewById(R.id.rDetails);
		aTitle.setText("Welcome " + name + ",");
		aTitle.setTypeface(null, Typeface.BOLD);

		// Method for listing dealers to visit
		dealer();

		// Button Section
		// => Back button
		final Button logout = (Button) findViewById(R.id.r_logout);
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something
				AlertDialog selectdial = createSelDial();
				selectdial.show();

			}
		});

		// =>Options button
		final Button Options = (Button) findViewById(R.id.r_options);
		Options.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent myintent = null;
				myintent = new Intent(Route.this, Options.class);

				Bundle bund = new Bundle();
				bund.putString("user", name);
				bund.putInt("userid", userid);
				bund.putString("outletid", outletid);
				bund.putString("dealer", dealer);
				bund.putString("week", week);
				myintent.putExtras(bund);

				Route.this.startActivity(myintent);
				Route.this.finish();

			}
		});

	}


	private AlertDialog createSelDial() {
		// TODO Auto-generated method stub
		AlertDialog seldialbox = new AlertDialog.Builder(Route.this)
				.setTitle("Confirm Logout")
				.setMessage("Are You sure you want to Logout")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								msg = "Yes" + Integer.toString(which);

								Intent myintent = null;
								myintent = new Intent(Route.this, Login.class);
								startActivity(myintent);

								Route.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg = "No" + Integer.toString(which);
					}
				}).create();
		return seldialbox;
	}

	private void dealer() {
		// TODO Auto-generated method stub

		

		// Get the database and run the query
		SQLiteDatabase db1 = mDatabase.getReadableDatabase();

		// Select dealers from the product table
		// AV FORM
		Cursor c = db1.rawQuery("SELECT * FROM " + outlet.OUTLET_TABLE_NAME
				+ " ORDER BY " + outlet.OUTLET_ID, null);

		Log.v(TAG, "No of Outlet Records:: " + c.getCount());
		c.moveToFirst();

		//  Dynamic Outlet List table

		if (c.getCount() > 0) {

			int validate = 0;
			for (int i = 0; i < c.getCount(); i++) {

				Log.v(TAG,
						">> OUTLET ID::"
								+ c.getInt(c
										.getColumnIndex(outlet.OUTLET_ID)));

				TableRow newRow = new TableRow(this);
				newRow.setGravity(Gravity.CENTER);
				newRow.setTag(c.getInt(c.getColumnIndex(outlet.OUTLET_ID)));

				
				// column 1
				Button aCol = new Button(this);
				aCol.setText(c.getInt(c.getColumnIndex(outlet.OUTLET_ID))
						+ " "
						+ c.getString(c
								.getColumnIndex(outlet.OUTLET_NAME)));
				aCol.setTag(c.getInt(c.getColumnIndex(outlet.OUTLET_ID)));
				aCol.setGravity(Gravity.LEFT);
				aCol.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Integer id = (Integer) v.getTag();
						Log.v(TAG, "OUTLET ID SELECTED: " + id + " ");
						showdetails(id);

						Intent myintent = null;
						myintent = new Intent(Route.this, ProductForm.class);

						Bundle bund = new Bundle();
						bund.putString("user", name);
						bund.putInt("userid", userid);
						bund.putString("outletid", outletid);
						bund.putString("dealer", dealer);
						bund.putString("week", week);
						myintent.putExtras(bund);

						Route.this.startActivity(myintent);
						Route.this.finish();
					}

				});

				
				newRow.addView(aCol);
				proF.addView(newRow);
				validate++;

				c.moveToNext();

			}
			if (validate == 0) {
				TableRow newRow = new TableRow(this);
				TextView noResults = new TextView(this);
				noResults.setText("Please download current week's route");
				noResults.setTextColor(Color.MAGENTA);
				newRow.addView(noResults);
				proF.addView(newRow);
			}

		} else {
			TableRow newRow = new TableRow(this);
			TextView noResults = new TextView(this);
			noResults.setText("No dealer route to show");
			noResults.setTextColor(Color.MAGENTA);
			newRow.addView(noResults);
			proF.addView(newRow);
		}
		c.close();
	}

	public void showdetails(Integer id) {

		String _id = id.toString().trim();
		Log.v(TAG, "inside showdetails, id is : " + id);

		SQLiteDatabase db = mDatabase.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ outlet.OUTLET_TABLE_NAME + " WHERE "
				+ outlet.OUTLET_ID + " = " + " '" + _id + "' ", null);
		if (cursor.moveToFirst()) {
			outletid = cursor.getString(cursor.getColumnIndex(outlet.OUTLET_ID.toString()));
			dealer = cursor.getString(cursor
					.getColumnIndex(outlet.OUTLET_NAME.toString()));
			Log.v(TAG, "Deal to send is : " + dealer);
		
		}
		cursor.close();
		db.close();
	}

}
