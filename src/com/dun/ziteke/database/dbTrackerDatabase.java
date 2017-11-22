/**
 * 
 */
package com.dun.ziteke.database;

import android.provider.BaseColumns;

/**
 * @author Duncan
 *
 */
public class dbTrackerDatabase {
	private dbTrackerDatabase() {
	}

	// users table
	public static final class user implements BaseColumns {

		private user() {
		}

		public static final String USER_TABLE_NAME = "tUSERS";
		public static final String USER_ID = "id";
		public static final String USER_NICKNAME= "firstname";
		public static final String USER_NAME = "username";
		public static final String USER_PASSWORD = "password";
		public static final String USER_LASTLOGIN = "lastlogin";
		public static final String USER_ACCESSLEVEL = "accesslevel";

		public static final String DEFAULT_SORT_ORDER = "username ASC";

	}
	
	// dealer table
	public static final class tdealer implements BaseColumns {
		private tdealer() {
		}

		public static final String DEALER_TABLE_NAME = "tdealer";
		public static final String DEALER_ID = "ID";
		public static final String DEALER_NO = "DealerNo";
		public static final String DEALER_WEEKNO = "WeekNo";
		public static final String DEALER_SHOPNAME = "Shopname";
		public static final String DEALER_NAME = "DealerName";
		public static final String DEALER_OUTLET = "Location";
		public static final String DEALER_TOWN = "Town";
		public static final String DEALER_RETAILCLASS = "RetailClass";
		public static final String DEALER_TIMEVISITED = "TimeVisit";
		public static final String DEALER_FSS = "FSS_Visited";
	}
	
	// dealer Input table
	public static final class tdealerInput implements BaseColumns {
		private tdealerInput() {
		}

		public static final String DEALERINPUT_TABLE_NAME = "tdealerInput";
		public static final String DEALERINPUT_ID = "ID";
		public static final String DEALERINPUT_NO = "DealerNo";
		public static final String DEALERINPUT_SHOPNAME = "Shopname";				
		public static final String DEALERINPUT_TOWN = "Town";
		public static final String DEALERINPUT_STATUS="Status";
		public static final String DEALERINPUT_ADDRESS = "Address";
		public static final String DEALERINPUT_OWNER = "Shop_Owner";
		public static final String DEALERINPUT_MOBILE = "PhoneNo";
	}	
	
	//outlet table
	public static final class outlet implements BaseColumns {
		private outlet() {
		}

		public static final String OUTLET_TABLE_NAME = "tOUTLET";
		public static final String OUTLET_ID = "_id";
		public static final String OUTLET_NAME = "DealerNo";
		public static final String OUTLET_STATUS = "Status";
		
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		
			
	}
	
	//ProductList table
	public static final class productList implements BaseColumns {
		private productList() {
		}

		public static final String PRODUCT_LIST_TABLE_NAME = "tPRODUCTLIST";
		public static final String PRODUCT_LIST_ID = "productList_id";
		public static final String PRODUCT_LIST_NAME= "productList_name";
		public static final String PRODUCT_LIST_CATEGORY= "cat_id";
		
		public static final String DEFAULT_SORT_ORDER = "productList_id ASC";

	}
	
	//Category table
	public static final class category implements BaseColumns {
		private category() {
		}

		public static final String eCAT_TABLE_NAME = "tCATEGORY";
		public static final String eCAT_ID = "_id";
		public static final String eCAT_IDs = "category_id";
		public static final String eCAT_NAME = "category_name";
		public static final String eCAT_DESCRIPTION = "description";
		
		public static final String DEFAULT_SORT_ORDER = "category_id ASC";

	}

}
