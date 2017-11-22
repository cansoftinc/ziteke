/**
 * 
 */
package com.dun.ziteke.database;

import android.app.Activity;
import android.os.Bundle;

import com.dun.ziteke.database.dbTrackerDatabaseHelper;

/**
 * @author Duncan
 *
 */
public class dbTrackerActivity extends Activity {

	// Our application database
	protected dbTrackerDatabaseHelper mDatabase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDatabase = new dbTrackerDatabaseHelper(this.getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDatabase != null) {
			mDatabase.close();
		}
	}
}
