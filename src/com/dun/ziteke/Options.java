/**
 * 
 */
package com.dun.ziteke;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dun.ziteke.database.dbTrackerActivity;

/**
 * @author Duncan
 * 
 */
public class Options extends dbTrackerActivity {

	Bundle bundle;
	String name, outletid, dealer, week;
	double lo, la;
	int userid;

	String msg, msg2;
	String deal;
	String TAG = "OPTIONS FORM";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		bundle = getIntent().getExtras();
		name = bundle.getString("user");
		userid = bundle.getInt("userid");
		week = bundle.getString("week");
		outletid = bundle.getString("outletid");
		dealer = bundle.getString("dealer");
		la = bundle.getDouble("lat");
		lo = bundle.getDouble("longi");

		TextView oTitle = (TextView) findViewById(R.id.oTitle);
		oTitle.setText("OUTLET :" + dealer);
		Button item = (Button) findViewById(R.id.o_item);
		Button route = (Button) findViewById(R.id.o_route);
		Button logout = (Button) findViewById(R.id.o_logout);

		item.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(outletid.length()==0||outletid.equals("")){
					AlertDialog al = alertNoSelectedOutlet();
					al.show();
				}else{
					Intent myIntent = null;
					myIntent = new Intent(Options.this, ProductForm.class);
					Bundle b = new Bundle();
					b.putString("user", name);
					b.putInt("userid", userid);
					b.putString("outletid", outletid);
					b.putString("dealer", dealer);
					b.putString("week", week);
					b.putDouble("lat", la);
					b.putDouble("longi", lo);				
					myIntent.putExtras(b);
					
					Options.this.startActivity(myIntent);
					Options.this.finish();
				}
				
			}
		});

		route.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent myintent = null;
				myintent = new Intent(Options.this, Route.class);

				Bundle bund = new Bundle();
				bund.putString("user", name);
				bund.putInt("userid", userid);
				bund.putString("week", week);
				myintent.putExtras(bund);

				Options.this.startActivity(myintent);
				Options.this.finish();
			}
		});

		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog selectdial = createSelDial();
				selectdial.show();
			}
		});

	}

	protected AlertDialog alertNoSelectedOutlet() {
		AlertDialog al = new AlertDialog.Builder(this)
		.setTitle("No outlet selected..")
		.setMessage("Please select an Outlet to proceed")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				msg2 = "OK"+Integer.toString(which);
			}
		}).create();
		
		return al;
	}

	private AlertDialog createSelDial() {
		// TODO Auto-generated method stub
		AlertDialog seldialbox = new AlertDialog.Builder(Options.this)
				.setTitle("Confirm Logout")
				.setMessage("Are You sure you want to Logout")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								msg = "Yes" + Integer.toString(which);

								Intent myintent = null;
								myintent = new Intent(Options.this, Login.class);
								startActivity(myintent);

								Options.this.finish();
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

}
