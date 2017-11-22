/**
 * 
 */
package com.dun.ziteke;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dun.ziteke.database.dbTrackerActivity;
import com.dun.ziteke.database.dbTrackerDatabase.productList;
import com.dun.ziteke.database.dbTrackerDatabase.category;

/**
 * @author Duncan
 * 
 */
public class ProductForm extends dbTrackerActivity implements OnClickListener,
		OnItemSelectedListener {

	Bundle bundle;
	String name, dealer, week, lo, la;
	int userid;
	Double lat = 0.0, longi=0.0;

	TextView tvBrand, tvProductSelected;
	EditText edQty;
	String strQty;

	String Brand, Product;
	
	String productid;
	
	String productname;
	String msg, msg2;
	String outletid;
	String TAG = "PRODUCTLIST FORM";
	// private EditText[] columnEditTexts;
	protected Spinner spCat, spPro;
	Cursor c, c1;

	String z = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productform);

		bundle = getIntent().getExtras();
		name = bundle.getString("user");
		outletid = bundle.getString("outletid");
		userid = bundle.getInt("userid");
		dealer = bundle.getString("dealer");
		week = bundle.getString("week");

		TextView oname = (TextView) findViewById(R.id.aoutletname);
		oname.setText("OUTLET NAME :: " + dealer);

		tvBrand = (TextView) findViewById(R.id.tvType);
		// Populate Brand spinner
		spCat = (Spinner) findViewById(R.id.spBrand);
		spCat.setPrompt("Select Brand");
		c = this.fetchAllBrands();

		String[] from = new String[] { category.eCAT_NAME };
		int[] to = new int[] { android.R.id.text1 };

		SimpleCursorAdapter sa = new SimpleCursorAdapter(ProductForm.this,
				android.R.layout.simple_spinner_item, c, from, to);
		spCat.setAdapter(sa);
		sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCat.setOnItemSelectedListener(this);

		// Populate Product spinner(productlist)
		spPro = (Spinner) findViewById(R.id.spProduct);

		tvProductSelected = (TextView) findViewById(R.id.tvProductselected);
		edQty = (EditText) findViewById(R.id.edqty);

		Button save = (Button) findViewById(R.id.p_save);
		Button back = (Button) findViewById(R.id.p_back);
		Button logout = (Button) findViewById(R.id.p_logout);

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//check if Qty is null, or item is selected
				// proceed to saving the items
				strQty = edQty.getText().toString().trim();
				Log.v(TAG, ">>QTY ::"+strQty);
				if (strQty.length()== 0 || strQty == null||strQty=="") {


					AlertDialog al = CreateNoQtyAlert();
					al.show();
					
				} else {
					Intent In = null;
					In = new Intent(ProductForm.this,ProductFormConfirm.class);
					
					Bundle bun = new Bundle();
					bun.putInt("userid", userid);
					bun.putString("name", name);
					bun.putString("outletid", outletid);					
					bun.putString("dealer", dealer);
					bun.putString("week", week);
					bun.putString("productid", productid);
					bun.putString("productname", productname);
					bun.putString("strQty", strQty);
					bun.putDouble("latitude", lat);
					bun.putDouble("longitude", longi);
					In.putExtras(bun);
					
					ProductForm.this.startActivityForResult(In, 1);

				}

			}
		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent myintent = null;
				myintent = new Intent(ProductForm.this, Route.class);

				Bundle bund = new Bundle();
				bund.putString("user", name);
				bund.putInt("userid", userid);
				bund.putString("dealer", dealer);
				bund.putString("week", week);
				myintent.putExtras(bund);

				ProductForm.this.startActivity(myintent);
				ProductForm.this.finish();
			}
		});

		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected AlertDialog CreateNoQtyAlert() {
		AlertDialog myNoQty = new AlertDialog.Builder(ProductForm.this)
				.setMessage("Input Quantity to proceed")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						msg2 = "OK" + Integer.toString(which);
					}
				}).create();
		return myNoQty;
	}

	// public void fillProductList(String s) {
	// // TableLayout where we want to Display list
	// final TableLayout shelfHA = (TableLayout)
	// findViewById(R.id.TableLayout_productform);
	//
	// // Get the database and run the query
	// SQLiteDatabase db1 = mDatabase.getReadableDatabase();
	//
	// // Select products from the product table
	// Cursor c = db1.rawQuery("SELECT * FROM "
	// + productList.PRODUCT_LIST_TABLE_NAME
	// + " WHERE "+productList.PRODUCT_LIST_CATEGORY+" = "+s
	// + " ORDER BY "
	// + productList.PRODUCT_LIST_ID, null);
	//
	// Log.v(TAG, "No of Product Record counts :: " + c.getCount());
	//
	// c.moveToFirst();
	// columnEditTexts = new EditText[c.getCount()];
	// // Display the results by adding some TableRows to the existing table
	// // layout
	// if (c.moveToFirst()) {
	//
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// Log.v(TAG,
	// ">> P"
	// + c.getInt(c
	// .getColumnIndex(productList.PRODUCT_LIST_ID)));
	//
	// TableRow newRow = new TableRow(this);
	// newRow.setGravity(Gravity.CENTER);
	// // column 1
	// TextView nameCol = new TextView(this);
	// nameCol.setTextColor(Color.BLACK);
	// nameCol.setText(c.getInt(c
	// .getColumnIndex(productList.PRODUCT_LIST_ID))
	// + " "
	// + c.getString(c
	// .getColumnIndex(productList.PRODUCT_LIST_NAME)));
	//
	// // column 2
	// EditText typeCol = new EditText(this);
	// typeCol.setId(i);
	// typeCol.setGravity(Gravity.RIGHT);
	// typeCol.setInputType(InputType.TYPE_CLASS_NUMBER);
	// columnEditTexts[i] = typeCol;
	//
	// newRow.addView(nameCol);
	// newRow.addView(typeCol);
	// shelfHA.addView(newRow);
	//
	// c.moveToNext();
	//
	// }
	//
	// } else {
	// TableRow newRow = new TableRow(this);
	// TextView noResults = new TextView(this);
	// noResults.setText("No results to show.");
	// newRow.addView(noResults);
	// shelfHA.addView(newRow);
	// }
	// c.close();
	//
	// }

	public Cursor fetchAllBrands() {

		String[] from = new String[] { category.eCAT_ID, category.eCAT_NAME,
				category.eCAT_IDs };

		SQLiteDatabase db3 = mDatabase.getWritableDatabase();
		Cursor cursor = db3.query(category.eCAT_TABLE_NAME, from, null, null,
				null, null, category.eCAT_NAME + " ASC");
		startManagingCursor(cursor);
		return cursor;
	}

	public Cursor fetchFilteredProduct(String z) {
		Cursor cursor = null;
		if (z != null) {
			SQLiteDatabase db3 = mDatabase.getWritableDatabase();
			cursor = db3.rawQuery("SELECT * FROM "
					+ productList.PRODUCT_LIST_TABLE_NAME + " WHERE "
					+ productList.PRODUCT_LIST_CATEGORY + " = " + z
					+ " ORDER BY " + productList.PRODUCT_LIST_ID, null);

			startManagingCursor(cursor);

		}
		return cursor;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Brand = spCat.getSelectedItem().toString();

		Product = spPro.getSelectedItem().toString();
		Log.v(TAG, ">>BRAND SELECTED :: " + Brand + " PRODUCT SELECTED::"
				+ Product);

	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View view, int position,
			long id) {

		if (spinner == spCat) {
			// Update the TextView
			Cursor cursor = (Cursor) spinner.getSelectedItem();
			// tvBrand.setText(cursor.getString(cursor
			// .getColumnIndex(category.eCAT_NAME)));// cursor.getInt(2)
			System.out
					.println("TRACE BRAND ID::"
							+ cursor.getString(cursor
									.getColumnIndex(category.eCAT_IDs)));

			z = cursor.getString(cursor.getColumnIndex(category.eCAT_IDs));

			c1 = this.fetchFilteredProduct(z);

			String[] from1 = new String[] { productList.PRODUCT_LIST_NAME };
			int[] to1 = new int[] { android.R.id.text1 };

			SimpleCursorAdapter sa1 = new SimpleCursorAdapter(ProductForm.this,
					android.R.layout.simple_spinner_item, c1, from1, to1);
			spPro.setAdapter(sa1);
			sa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spPro.setOnItemSelectedListener(this);

			// fillProductList(cursor.getString(cursor
			// .getColumnIndex(category.eCAT_IDs)));

		}
		if (spinner == spPro) {
			Cursor cr = (Cursor) spinner.getSelectedItem();
			tvProductSelected.setText(cr.getString(cr
					.getColumnIndex(productList.PRODUCT_LIST_NAME)));
			productname = cr.getString(cr
					.getColumnIndex(productList.PRODUCT_LIST_NAME));
			productid = cr.getString(cr.getColumnIndex(productList.PRODUCT_LIST_ID));
			System.out.println("TRACE PRODUCT ID::"
					+ cr.getString(cr
							.getColumnIndex(productList.PRODUCT_LIST_ID)));
		}

	}

}
