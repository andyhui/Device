package com.norinco.eme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.norinco.device.R;

public class history_fault extends Activity {

	private SQLiteDatabase mSQLiteDatabase = null;
	private final static String DATABASE_NAME = "eme.db";
	private static final String CREATE_FACE_TABLE = "CREATE TABLE face_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT)";
	private static final String CREATE_FUNCTION_TABLE = "CREATE TABLE function_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT)";
	private static final String CREATE_SYS_TABLE = "CREATE TABLE sys_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT)";
	private static final String KEY_ID = "_id";
	private static final String KEY_NUM = "num";
	private static final String KEY_DATA = "data";
	private static final String FACE_TABLE = "face_table";
	
	private static final String FUNCTION_TABLE = "function_table";

	private static final String SYS_TABLE = "sys_table";
	
	// List<HashMap<String, Object>> mList = null;
	// SimpleAdapter adapter;
	private static int miCount = 0;
	SimpleAdapter adapter;
	ListView m_ListView = null;
	ListView function_ListView = null;
	ListView sys_ListView = null;
	List<HashMap<String, Object>> mList = null;
	List<HashMap<String, Object>> function_list = null;
	List<HashMap<String, Object>> sys_list = null;

	// DataBaseAdapter_face m_MyDataBaseAdapter = null;

	private void UpdataAdapter() {
		// TODO Auto-generated method stub Cursor
		mList = new ArrayList<HashMap<String, Object>>();
//		Cursor cur_all = m_MyDataBaseAdapter.fetchAlldata();
		Cursor cur_all  = mSQLiteDatabase.query(FACE_TABLE, new String[] { KEY_ID, KEY_NUM,
				KEY_DATA }, null, null, null, null, null);
		int rows_num = cur_all.getCount();
		if (rows_num != 0) {
			cur_all.moveToFirst();
			for (int i = 0; i < rows_num; i++) {
				int _id = cur_all.getInt(0);
				int num = cur_all.getInt(1);
				String value = cur_all.getString(2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", value);
				mList.add(map);
				cur_all.moveToNext();
			}
		}
		
		Cursor function_cur  = mSQLiteDatabase.query(FUNCTION_TABLE, new String[] { KEY_ID, KEY_NUM,
				KEY_DATA }, null, null, null, null, null);
		int rows_function_num = function_cur.getCount();
		if (rows_function_num != 0) {
			function_cur.moveToFirst();
			for (int i = 0; i < rows_function_num; i++) {
				int _id = function_cur.getInt(0);
				int num = function_cur.getInt(1);
				String value = function_cur.getString(2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", value);
				mList.add(map);
				function_cur.moveToNext();
			}
		}
		
		Cursor sys_cur  = mSQLiteDatabase.query(SYS_TABLE, new String[] { KEY_ID, KEY_NUM,
				KEY_DATA }, null, null, null, null, null);
		int rows_sys_num = sys_cur.getCount();
		if (rows_sys_num != 0) {
			sys_cur.moveToFirst();
			for (int i = 0; i < rows_sys_num; i++) {
				int _id = sys_cur.getInt(0);
				int num = sys_cur.getInt(1);
				String value = sys_cur.getString(2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", value);
				mList.add(map);
				sys_cur.moveToNext();
			}
		}
		
		adapter = new SimpleAdapter(this, mList,
				android.R.layout.simple_list_item_1, new String[] { "text" },
				new int[] { android.R.id.text1 });
		m_ListView.setAdapter(adapter);

		cur_all.close();
		function_cur.close();
		sys_cur.close();
		
		/*Cursor function_cur  = mSQLiteDatabase.query(FUNCTION_TABLE, new String[] { KEY_ID, KEY_NUM,
				KEY_DATA }, null, null, null, null, null);
		int rows_function_num = function_cur.getCount();
		if (rows_function_num != 0) {
			function_cur.moveToFirst();
			for (int i = 0; i < rows_function_num; i++) {
				int _id = function_cur.getInt(0);
				int num = function_cur.getInt(1);
				String value = function_cur.getString(2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", value);
				function_list.add(map);
				function_cur.moveToNext();
			}
		}
		adapter = new SimpleAdapter(this, function_list,
				android.R.layout.simple_list_item_1, new String[] { "text" },
				new int[] { android.R.id.text1 });
		function_ListView.setAdapter(adapter);
		function_cur.close();
		
		Cursor sys_cur  = mSQLiteDatabase.query(SYS_TABLE, new String[] { KEY_ID, KEY_NUM,
				KEY_DATA }, null, null, null, null, null);
		int rows_sys_num = sys_cur.getCount();
		if (rows_sys_num != 0) {
			sys_cur.moveToFirst();
			for (int i = 0; i < rows_sys_num; i++) {
				int _id = sys_cur.getInt(0);
				int num = sys_cur.getInt(1);
				String value = sys_cur.getString(2);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", value);
				sys_list.add(map);
				sys_cur.moveToNext();
			}
		}
		adapter = new SimpleAdapter(this, sys_list,
				android.R.layout.simple_list_item_1, new String[] { "text" },
				new int[] { android.R.id.text1 });
		sys_ListView.setAdapter(adapter);

		sys_cur.close();*/

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_fault);

		m_ListView = (ListView) findViewById(R.id.my_list);
//		function_ListView = (ListView) findViewById(R.id.function_list);
//		sys_ListView = (ListView) findViewById(R.id.sys_list);

		// mList = new ArrayList<HashMap<String, Object>>();

		// m_MyDataBaseAdapter = new DataBaseAdapter_face(this);
		// m_MyDataBaseAdapter.open();
		// UpdataAdapter();

		mSQLiteDatabase = this.openOrCreateDatabase(DATABASE_NAME,
				MODE_PRIVATE, null);
		try {
			if (!tabIsExist(mSQLiteDatabase,FACE_TABLE)) {
				System.out.println("====================asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_FACE_TABLE);
			}
			if (!tabIsExist(mSQLiteDatabase,FUNCTION_TABLE)) {
				System.out.println("====================asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_FUNCTION_TABLE);
			}
			if (!tabIsExist(mSQLiteDatabase,SYS_TABLE)) {
				System.out.println("====================asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_SYS_TABLE);
			}
		} catch (Exception e) {
			
		}finally{
			UpdataAdapter();
//			cur.close();
		}

		/*
		 * Cursor cur_all = m_MyDataBaseAdapter.fetchAlldata(); int rows_num =
		 * cur_all.getCount(); if (rows_num != 0) { cur_all.moveToFirst(); for
		 * (int i = 0; i < rows_num; i++) { int _id = cur_all.getInt(0); int num
		 * = cur_all.getInt(1); String value = cur_all.getString(2);
		 * HashMap<String, Object> map = new HashMap<String, Object>();
		 * map.put("text",value); mList.add(map); cur_all.moveToNext(); } }
		 * adapter = new SimpleAdapter(this, mList, R.layout.history_fault_list,
		 * new String[] { "text" }, new int[] { R.id.text_history_fault});
		 * m_ListView.setAdapter(adapter);
		 * 
		 * cur_all.close();
		 */

	}

	public boolean tabIsExist(SQLiteDatabase mSQLiteDatabase,String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// m_MyDataBaseAdapter.close();
			mSQLiteDatabase.close();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
