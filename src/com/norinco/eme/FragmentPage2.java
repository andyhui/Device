package com.norinco.eme;

import com.norinco.device.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentPage2 extends Fragment {

	private SQLiteDatabase mSQLiteDatabase = null;
	private final static String DATABASE_NAME = "eme.db";
	private static final String CREATE_TABLE = "CREATE TABLE function_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT)";
	private static final String KEY_ID = "_id";
	private static final String KEY_NUM = "num";
	private static final String KEY_DATA = "data";
	private static final String DB_TABLE = "function_table";

	public boolean tabIsExist(SQLiteDatabase mSQLiteDatabase, String tabName) {
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mSQLiteDatabase.close();
		// getActivity().finish();
		super.onDestroyView();
	}

	public static String getDatabaseName() {
		return DATABASE_NAME;
	}

	public static String getCreateTable() {
		return CREATE_TABLE;
	}

	public static String getKeyId() {
		return KEY_ID;
	}

	public static String getKeyNum() {
		return KEY_NUM;
	}

	public static String getKeyData() {
		return KEY_DATA;
	}

	public static String getDbTable() {
		return DB_TABLE;
	}

	private Button mButton01 = null;
	private Button mButtonOk = null;
	private Button mButtonFault = null;
	private CheckBox c1, c2, c3, c4, c5;
	private TextView t1;
	private EditText e1;

	private static int miCount = 0;

	private static int checkbox_c1 = 1;
	private static int checkbox_c2 = 2;
	private static int checkbox_c3 = 3;
	private static int checkbox_c4 = 4;
	private static int checkbox_c5 = 5;

	/* private Context mContext = getActivity(); */
	/* mContext = getActivity(); */

	private static String checkbox_s1 = "指示灯不亮";
	private static String checkbox_s2 = "按钮失效";
	private static String checkbox_s3 = "键盘失效";
	private static String checkbox_s4 = "无法开机";
	private static String checkbox_s5 = "听筒无效";

	boolean c1_stat = false;
	boolean c2_stat = false;
	boolean c3_stat = false;
	boolean c4_stat = false;
	boolean c5_stat = false;

	// DataBaseAdapter_function m_MyDataBaseAdapter;

	View.OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ok:
				String solved_method = e1.getText().toString();
				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_NUM, miCount);
				initialValues.put(KEY_DATA, solved_method);
				mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
				miCount++;
				break;
			case R.id.delete:
				break;
			default:
				break;
			}

		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment2, null);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/*
		 * m_MyDataBaseAdapter = new DataBaseAdapter_function(getActivity());
		 * m_MyDataBaseAdapter.open();
		 */

		mSQLiteDatabase = this.getActivity().openOrCreateDatabase(
				DATABASE_NAME, 0, null);
		try {
			if (!tabIsExist(mSQLiteDatabase, DB_TABLE)) {
				System.out.println("=asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_TABLE);
			}
		} catch (Exception e) {

		} finally {
			// UpdataAdapter();
			// cur.close();
		}

		mButton01 = (Button) getActivity().findViewById(R.id.button);
		mButton01.setOnClickListener(new myOnClickListener());

		mButtonOk = (Button) getActivity().findViewById(R.id.ok);
		mButtonFault = (Button) getActivity().findViewById(R.id.delete);
		mButtonOk.setOnClickListener(buttonListener);
		mButtonFault.setOnClickListener(buttonListener);

		c1 = (CheckBox) getActivity().findViewById(R.id.CheckBox01);
		status(c1, checkbox_c1);

		c2 = (CheckBox) getActivity().findViewById(R.id.CheckBox02);
		status(c2, checkbox_c2);

		c3 = (CheckBox) getActivity().findViewById(R.id.CheckBox03);
		status(c3, checkbox_c3);

		c4 = (CheckBox) getActivity().findViewById(R.id.CheckBox04);
		status(c4, checkbox_c4);

		c5 = (CheckBox) getActivity().findViewById(R.id.CheckBox05);
		status(c5, checkbox_c5);

		t1 = (TextView) getActivity().findViewById(R.id.TextView);

		e1 = (EditText) getActivity().findViewById(R.id.edit_message);

		String otherProb = e1.getText().toString();

		c1.setOnCheckedChangeListener(new CheckBoxListener());

		c2.setOnCheckedChangeListener(new CheckBoxListener());

		c3.setOnCheckedChangeListener(new CheckBoxListener());

		c4.setOnCheckedChangeListener(new CheckBoxListener());

		c5.setOnCheckedChangeListener(new CheckBoxListener());
	}

	private class CheckBoxListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// Toast
				AddData(buttonView.getText().toString());
				Toast.makeText(getActivity(), buttonView.getText() + "被选择",
						Toast.LENGTH_SHORT).show();
			} else {
				DeleteData(buttonView.getText().toString());
				Toast.makeText(getActivity(), buttonView.getText() + "取消选择",
						Toast.LENGTH_SHORT).show();
			}
		}

		private void DeleteData(String data) {
			// TODO Auto-generated method stub
			if (data.equals(checkbox_s1)) {
				miCount = 1;
			}
			if (data.equals(checkbox_s2)) {
				miCount = 2;
			}
			if (data.equals(checkbox_s3)) {
				miCount = 3;
			}
			if (data.equals(checkbox_s4)) {
				miCount = 4;
			}
			if (data.equals(checkbox_s5)) {
				miCount = 5;
			}
			deleteData(miCount);

		}

		private void AddData(String data) {
			// TODO Auto-generated method stub
			if (data.equals(checkbox_s1)) {
				miCount = 1;
			}
			if (data.equals(checkbox_s2)) {
				miCount = 2;
			}
			if (data.equals(checkbox_s3)) {
				miCount = 3;
			}
			if (data.equals(checkbox_s4)) {
				miCount = 4;
			}
			if (data.equals(checkbox_s5)) {
				miCount = 5;
			}
			insertData(miCount, data);

		}
	}

	private Cursor fetchData(int num) {
		// TODO Auto-generated method stub
		Cursor mCursor = mSQLiteDatabase.query(true, DB_TABLE, new String[] {
				KEY_ID, getKeyNum(), getKeyData() }, getKeyNum() + "=" + num,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private boolean deleteData(int num) {
		// TODO Auto-generated method stub
		return mSQLiteDatabase.delete(DB_TABLE, getKeyNum() + "=" + num, null) > 0;

	}

	private void status(CheckBox cb, int cb_c) {
		Cursor cursor = fetchData(cb_c);
		boolean stat = false;
		if (cursor.moveToFirst()) {/* 判断游标里面是否为空 */
			int count = cursor.getInt(0);
			if (count > 0) {
				stat = true;
			}
		}
		cb.setChecked(stat);
		cursor.close();
	}

	private long insertData(int num, String data) {
		// TODO Auto-generated method stub
		ContentValues initialValues = new ContentValues();
		initialValues.put(getKeyNum(), num);
		initialValues.put(getKeyData(), data);
		return mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);

	}

	private class myOnClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == mButton01) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), DetailsActivity.class);
				startActivity(intent);
			}
		}
	}
}
