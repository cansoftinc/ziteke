package com.dun.ziteke.database;


import com.dun.ziteke.database.dbTrackerDatabase.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbTrackerDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "dbSF_sqlite";
	private static final int DATABASE_VERSION = 1;
	String TAG = "TABLES";
	public long rowId = 0;

	public dbTrackerDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	

		// Create the users table
		db.execSQL("CREATE TABLE " + user.USER_TABLE_NAME + "(" + user.USER_ID
				+ " INTEGER PRIMARY KEY,"+ user.USER_NICKNAME + " TEXT," + user.USER_NAME + " TEXT,"
				+ user.USER_PASSWORD + " VARCHAR," + user.USER_LASTLOGIN
				+ " DATETIME," + user.USER_ACCESSLEVEL + " NUMERIC);");

		Log.w(TAG, user.USER_TABLE_NAME);
		// insert dummy user
		// long rowId = 0;
//		ContentValues userRecordToAdd = new ContentValues();
//		userRecordToAdd.put(user.USER_NAME, "duncan");
//		userRecordToAdd.put(user.USER_PASSWORD, "duncan");
//		userRecordToAdd.put(user.USER_LASTLOGIN, currentTimeString);
//		rowId = db
//				.insert(user.USER_TABLE_NAME, user.USER_NAME, userRecordToAdd);
//
//		// long rowId = 0;
//		userRecordToAdd.put(user.USER_NAME, "pato");
//		userRecordToAdd.put(user.USER_PASSWORD, "pato");
//		userRecordToAdd.put(user.USER_LASTLOGIN, currentTimeString);
//		rowId = db
//				.insert(user.USER_TABLE_NAME, user.USER_NAME, userRecordToAdd);

		// Create the outlet table
		db.execSQL("CREATE TABLE " + outlet.OUTLET_TABLE_NAME + "("
				+ outlet.OUTLET_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
				+ outlet.OUTLET_NAME + " VARCHAR(45)  NULL  NOT NULL,"
				+ outlet.OUTLET_STATUS + " INT( 2 ) NOT NULL DEFAULT '0');");

		Log.w(TAG, outlet.OUTLET_TABLE_NAME);

		// insert into outlet table
		ContentValues loc = new ContentValues();
		loc.put(outlet.OUTLET_ID, "1");
		loc.put(outlet.OUTLET_NAME, "MOZEL MOTORS");
		db.insert(outlet.OUTLET_TABLE_NAME, outlet.OUTLET_ID, loc);

		loc.put(outlet.OUTLET_ID, "2");
		loc.put(outlet.OUTLET_NAME, "GIGI MOTORS");
		db.insert(outlet.OUTLET_TABLE_NAME, outlet.OUTLET_ID, loc);

		loc.put(outlet.OUTLET_ID, "3");
		loc.put(outlet.OUTLET_NAME, "GM");
		db.insert(outlet.OUTLET_TABLE_NAME, outlet.OUTLET_ID, loc);

		loc.put(outlet.OUTLET_ID, "4");
		loc.put(outlet.OUTLET_NAME, "TOYOTA KENYA");
		db.insert(outlet.OUTLET_TABLE_NAME, outlet.OUTLET_ID, loc);

		// Create ProductsList table
		db.execSQL("CREATE TABLE " + productList.PRODUCT_LIST_TABLE_NAME + "("
				+ productList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ productList.PRODUCT_LIST_ID + " INTEGER(10) NULL NOT NULL ,"
				+ productList.PRODUCT_LIST_NAME + " VARCHAR(100) NULL NOT NULL,"
				+ productList.PRODUCT_LIST_CATEGORY	+ " VARCHAR(45) NULL NOT NULL)");

		Log.w(TAG, productList.PRODUCT_LIST_TABLE_NAME);
		
		//Create the existing Category Product table
		db.execSQL("CREATE TABLE " + category.eCAT_TABLE_NAME+ "(" 
				+category.eCAT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
				+category.eCAT_IDs+ " NUMERIC NOT NULL ,"
				+category.eCAT_NAME+" VARCHAR(45)  NULL  NOT NULL);");
		
		Log.w(TAG, category.eCAT_TABLE_NAME);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Housekeeping here.
		// Implement how "move" your application data during an upgrade of
		// schema versions
		// There is no ALTER TABLE command in SQLite, so this generally involves
		// CREATING a new table, moving data if possible, or deleting the old
		// data and starting fresh
		// Your call.

		Log.w(dbTrackerDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		// db.execSQL("DROP TABLE IF EXISTS " + shelf_HA.SHELF_HA_TABLE_NAME);

	}

}
