package com.norinco.eme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.norinco.device.DeviceViewActivity;
import com.norinco.device.R;

@SuppressLint("NewApi")
public class FragmentPage1 extends Fragment {

	private SQLiteDatabase mSQLiteDatabase = null;
	private final static String DATABASE_NAME = "eme.db";
	private static final String CREATE_TABLE = "CREATE TABLE face_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT,method TEXT);";
	private static final String KEY_ID = "_id";
	private static final String KEY_NUM = "num";
	private static final String KEY_DATA = "data";
	private static final String KEY_METHOD = "method";

	private static final String DB_TABLE = "face_table";

	private static int miCount = 0;

	private int off = R.drawable.radio_off;
	private int on = R.drawable.radio_on;

	private static int checkbox_c1 = 1;
	private static int checkbox_c2 = 2;
	private static int checkbox_c3 = 3;
	private static int checkbox_c4 = 4;
	private static int checkbox_c5 = 5;

	private static String checkbox_s1 = "显示屏不亮";
	private static String checkbox_s2 = "机器过热";
	private static String checkbox_s3 = "电线断裂";
	private static String checkbox_s4 = "显示屏损坏";
	private static String checkbox_s5 = "天线折损";

	int[] table_id = { 1, 2, 3, 4, 5 };
	String[] table_data = { "显示屏不亮", "机器过热", "电线断裂", "显示屏损坏", "天线折损" };
	String[] table_method = { "重启程序", "关机，等待", "换电线", "换显示屏", "换天线" };
	String[] table_method1 = { "aaaa", "bbbb", "cccc", "dddd", "eeee" };
	String[] table_method2 = { "重启机器", "稍等。。。", "重新接电线", "用外接显示屏", "启用备用天线" };

	private CheckBox c1, c2, c3, c4, c5;
	private TextView t1;
	private EditText e1;
	private Button mButtonOk = null;
	private Button mButtonFault = null;

	ListView m_ListView = null;
	List<HashMap<String, Object>> mList = null;
	SimpleAdapter adapter;

	boolean c1_stat = false;
	boolean c2_stat = false;
	boolean c3_stat = false;
	boolean c4_stat = false;
	boolean c5_stat = false;

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

	public static String getKeyId() {
		return KEY_ID;
	}

	public static String getKeyNum() {
		return KEY_NUM;
	}

	public static String getKeyData() {
		return KEY_DATA;
	}

	public static String getKeyMethod() {
		return KEY_METHOD;
	}

	public static String getDbTable() {
		return DB_TABLE;
	}

	// DataBaseAdapter_face m_MyDataBaseAdapter;
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

	// View.OnClickListener buttonListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.ok:
	// String solved_method = e1.getText().toString();
	// ContentValues initialValues = new ContentValues();
	// initialValues.put(KEY_NUM, miCount);
	// initialValues.put(KEY_DATA, solved_method);
	// mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
	// miCount++;
	// break;
	// case R.id.delete:
	// break;
	// default:
	// break;
	// }
	//
	// }
	//
	// };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment1, null);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		m_ListView = (ListView) getActivity().findViewById(R.id.method_list);
		/*
		 * m_MyDataBaseAdapter = new DataBaseAdapter_face(getActivity());
		 * m_MyDataBaseAdapter.open();
		 */
		// mButtonOk = (Button) getActivity().findViewById(R.id.ok);
		// mButtonFault = (Button) getActivity().findViewById(R.id.delete);
		//
		// mButtonOk.setOnClickListener(buttonListener);
		// mButtonFault.setOnClickListener(buttonListener);

		mSQLiteDatabase = this.getActivity().openOrCreateDatabase(
				DATABASE_NAME, 0, null);
		try {
			if (!tabIsExist(mSQLiteDatabase, DB_TABLE)) {
				System.out.println("=asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_TABLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		c1 = (CheckBox) getActivity().findViewById(R.id.CheckBox01);
		// status(c1, checkbox_c1);

		c2 = (CheckBox) getActivity().findViewById(R.id.CheckBox02);
		// status(c2, checkbox_c2);

		c3 = (CheckBox) getActivity().findViewById(R.id.CheckBox03);
		// status(c3, checkbox_c3);

		c4 = (CheckBox) getActivity().findViewById(R.id.CheckBox04);
		// status(c4, checkbox_c4);

		c5 = (CheckBox) getActivity().findViewById(R.id.CheckBox05);
		// status(c5, checkbox_c5);

		t1 = (TextView) getActivity().findViewById(R.id.TextView);

		e1 = (EditText) getActivity().findViewById(R.id.edit_message);

		c1.setOnCheckedChangeListener(new CheckBoxListener());
		c2.setOnCheckedChangeListener(new CheckBoxListener());
		c3.setOnCheckedChangeListener(new CheckBoxListener());
		c4.setOnCheckedChangeListener(new CheckBoxListener());
		c5.setOnCheckedChangeListener(new CheckBoxListener());

		/*
		 * for (int i = 0; i < 5; i++) { insertTable(table_id[i], table_data[i],
		 * table_method1[i]); }
		 */
	}

	class CheckBoxListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// Toast
				AddData(buttonView.getText().toString());
				// update(buttonView.getText().toString());
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
			// deleteData(miCount);

		}

		private void AddData(String data) {
			// TODO Auto-generated method stub
			if (data.equals(checkbox_s1)) {
				System.out.println("hello world!");
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
			// insertData(miCount, data);
			update(miCount);

		}

	}

	private void update(int num) {
		// Cursor m_consor = fetchData(num);
		System.out.println(num);

		mList = new ArrayList<HashMap<String, Object>>();
		Cursor cur_all = mSQLiteDatabase.query(DB_TABLE, new String[] { KEY_ID,
				KEY_NUM, KEY_DATA, KEY_METHOD }, getKeyNum() + "=" + num, null,
				null, null, null, null);
		int rows_num = cur_all.getCount();
		if (rows_num != 0) {
			cur_all.moveToFirst();
			for (int i = 0; i < rows_num; i++) {
				int _id = cur_all.getInt(0);
				// int num = cur_all.getInt(1);
				String value = cur_all.getString(3);
				System.out.println(value);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("image", off);
				map.put("text", value);
				mList.add(map);
				cur_all.moveToNext();
			}
		}

		adapter = new SimpleAdapter(getActivity(), mList,
				R.layout.solvedlistview, new String[] { "image", "text" },
				new int[] { R.id.img, R.id.title });
		m_ListView.setAdapter(adapter);
		cur_all.close();

		m_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					LayoutInflater factory = LayoutInflater.from(getActivity());
					final View DialogView = factory.inflate(
							R.layout.dialogview, null);
					AlertDialog dialog = new AlertDialog.Builder(getActivity())
							.setTitle("解决方案")
							.setView(DialogView)
							.setPositiveButton("可用",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											System.out.println("可用");
											Map<String, Object> map = (Map<String, Object>)adapter
													.getItem(position);
											map.put("imge", on);
											adapter.notifyDataSetChanged();
//											ChangeImg(position, true);

										}
									})
							.setNeutralButton("不可用",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											System.out.println("不可用");
											ChangeImg(position, false);

										}

									}).create();
					dialog.show();
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;

				}

			}

		});
	}

	private void ChangeImg(int selectedItem, boolean b) {
		SimpleAdapter la = adapter;
		System.out.println(selectedItem);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)la
				.getItem(selectedItem);
		if (b) {
			map.put("imge", on);
			System.out.println("可用改变图片");
		} else {
			map.put("imge", off);
			System.out.println("不可用改变图片");
		}
		adapter.notifyDataSetChanged();
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

	// private Cursor fetchData(String data) {
	// // TODO Auto-generated method stub
	// Cursor mCursor = mSQLiteDatabase.query(true, DB_TABLE, new String[] {
	// KEY_ID, getKeyNum(), getKeyData() }, getKeyData() + "=" + data,
	// null, null, null, null, null);
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }

	private Cursor fetchData(int num) {
		// TODO Auto-generated method stub
		Cursor mCursor = mSQLiteDatabase.query(true, DB_TABLE, new String[] {
				KEY_ID, getKeyNum(), getKeyData(), getKeyMethod() },
				getKeyNum() + "=" + num, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private boolean deleteData(int num) {
		// TODO Auto-generated method stub
		return mSQLiteDatabase.delete(DB_TABLE, getKeyNum() + "=" + num, null) > 0;

	}

	private long insertTable(int num, String data, String method) {
		// TODO Auto-generated method stub
		ContentValues initialValues = new ContentValues();
		initialValues.put(getKeyNum(), num);
		initialValues.put(getKeyData(), data);
		initialValues.put(getKeyMethod(), method);
		System.out.println(num);
		System.out.println(data);
		System.out.println(method);
		return mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
	}

	private long insertData(int num, String data) {
		// TODO Auto-generated method stub
		ContentValues initialValues = new ContentValues();
		initialValues.put(getKeyNum(), num);
		initialValues.put(getKeyData(), data);
		return mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);

	}

}