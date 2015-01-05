package com.norinco.eme;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.cngc.hht.R;

@SuppressLint("NewApi")
public class face_Activity extends Activity {

	private SQLiteDatabase mSQLiteDatabase = null;
	private final static String DATABASE_NAME = "eme.db";
	private static final String CREATE_TABLE = "CREATE TABLE face_table (_id INTEGER PRIMARY KEY AUTOINCREMENT,num INTEGER,data TEXT,method TEXT);";
	private static final String KEY_ID = "_id";
	private static final String KEY_NUM = "num";
	private static final String KEY_DATA = "data";
	private static final String KEY_METHOD = "method";

	private static final String DB_TABLE = "face_table";

	private static int miCount = 0;

	// private int off = R.drawable.radio_off;
	// private int on = R.drawable.radio_on;

	// private int off = R.drawable.checkbox_normal;
	private int off = R.drawable.circle_delete;
	private int on = R.drawable.circle_ok;
	private int init = R.drawable.circle;

	private static int checkbox_c1 = 1;
	private static int checkbox_c2 = 2;
	private static int checkbox_c3 = 3;
	private static int checkbox_c4 = 4;
	private static int checkbox_c5 = 5;

	// private static String checkbox_s1 = "显示屏不亮";
	// private static String checkbox_s2 = "机器过热";
	// private static String checkbox_s3 = "电线断裂";
	// private static String checkbox_s4 = "显示屏损坏";
	// private static String checkbox_s5 = "天线折损";

	private static String checkbox_s1 = "操作系统启动不了，蓝屏";
	private static String checkbox_s2 = "系统自动重启";
	private static String checkbox_s3 = "开机有警报声，一长两短";
	private static String checkbox_s4 = "开机不进系统，显示兵器log不动";
	private static String checkbox_s5 = "显示字符出现乱码";

	int[] table_id = { 1, 2, 3, 4, 5 };
	// String[] table_data = { "显示屏不亮", "机器过热", "电线断裂", "显示屏损坏", "天线折损" };
	// String[] table_method = { "重启程序", "关机，等待", "换电线", "换显示屏", "换天线" };

	String[] table_data = { "操作系统启动不了，蓝屏", "系统自动重启", "开机有警报声，一长两短",
			"开机不进系统，显示兵器log不动", "显示字符出现乱码" };
	String[] table_method = { "按F2进入bios设置启动选项，如不成功则需要一键恢复系统", "重装操作系统",
			"BIOS出错，重写BIOS程序", "BIOS或南桥BIOS重写", "编码格式重新设置" };

	String[] table_method1 = { "aaaa", "bbbb", "cccc", "dddd", "eeee" };
	String[] table_method2 = { "重启机器", "重启系统", "重启BIOS", "换一个系统", "修改文本中的字符" };

	private CheckBox c1, c2, c3, c4, c5;
	private TextView t1;
	private EditText e1;
	private Button mButtonOk = null;
	private Button mButtonFault = null;

	private Button mSave = null;
	private Button mBack = null;

	ArrayList<ListStatus> arrList = new ArrayList<ListStatus>();

	ListView m_ListView = null;
	List<HashMap<String, Object>> mList = null;
	SimpleAdapter adapter;

	private int selectItem;
	private boolean flag = false;
	private int countMethod = 0;
	MethodData methodData;

	boolean c1_stat = false;
	boolean c2_stat = false;
	boolean c3_stat = false;
	boolean c4_stat = false;
	boolean c5_stat = false;

	private int level = 3;
	private String user = "admin";

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mSQLiteDatabase.close();
		super.onDestroy();
	}

	// @Override
	// public void onDestroyView() {
	// // TODO Auto-generated method stub
	// mSQLiteDatabase.close();
	// // getActivity().finish();
	// super.onDestroyView();
	// }

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

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// return inflater.inflate(R.layout.fragment1, null);
	//
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment1);

		m_ListView = (ListView) findViewById(R.id.method_list);
		/*
		 * m_MyDataBaseAdapter = new DataBaseAdapter_face(getActivity());
		 * m_MyDataBaseAdapter.open();
		 */
		// mButtonOk = (Button) getActivity().findViewById(R.id.ok);
		// mButtonFault = (Button) getActivity().findViewById(R.id.delete);
		//
		// mButtonOk.setOnClickListener(buttonListener);
		// mButtonFault.setOnClickListener(buttonListener);

		methodData = new MethodData(this);

		mSQLiteDatabase = this.openOrCreateDatabase(DATABASE_NAME, 0, null);
		try {
			if (!tabIsExist(mSQLiteDatabase, DB_TABLE)) {
				System.out.println("=asd==============asdasd========");
				mSQLiteDatabase.execSQL(CREATE_TABLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		c1 = (CheckBox) findViewById(R.id.CheckBox01);
		c1.setText(R.string.face_prob1);
		// status(c1, checkbox_c1);

		c2 = (CheckBox) findViewById(R.id.CheckBox02);
		// status(c2, checkbox_c2);

		c3 = (CheckBox) findViewById(R.id.CheckBox03);
		// status(c3, checkbox_c3);

		c4 = (CheckBox) findViewById(R.id.CheckBox04);
		// status(c4, checkbox_c4);

		c5 = (CheckBox) findViewById(R.id.CheckBox05);
		// status(c5, checkbox_c5);

		t1 = (TextView) findViewById(R.id.TextView);

		e1 = (EditText) findViewById(R.id.edit_message);

		mSave = (Button) findViewById(R.id.isave);
		mBack = (Button) findViewById(R.id.iback);

		mSave.setOnClickListener(bListenler);
		mBack.setOnClickListener(bListenler);

		c1.setOnCheckedChangeListener(new CheckBoxListener());
		c2.setOnCheckedChangeListener(new CheckBoxListener());
		c3.setOnCheckedChangeListener(new CheckBoxListener());
		c4.setOnCheckedChangeListener(new CheckBoxListener());
		c5.setOnCheckedChangeListener(new CheckBoxListener());

		/*for (int i = 0; i < 5; i++) {
			insertTable(table_id[i], table_data[i], table_method2[i]);
		}*/

	}

	View.OnClickListener bListenler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == mSave) {
				/* 保存解决方案 */
				System.out.println(flag);
				if (flag == false) {
					Dialog dlg = new AlertDialog.Builder(face_Activity.this)
							.setTitle("友情提示")
							.setMessage("您还没有选择任何方案")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									})
							/*
							 * .setNeutralButton("退出", new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick( DialogInterface
							 * dialog, int which) { // TODO Auto-generated
							 * method stub
							 * 
							 * } })
							 */.create();
					dlg.show();
				}

				Properties properties = new Properties();
				Properties propertiesProblem = new Properties();

				int j = 0;
				for (int i = 0; i < countMethod; i++) {
					System.out.println("解决方案是：");
					System.out.println(arrList.get(i).isStatus());
					if (arrList.get(i).isStatus()) {
						/* 生成维修历史 */
						String methods = table_data[miCount - 1] + " " + "维修成功";
						insertData(arrList.get(i).getId(), methods);
						methodData.insert(methods,
								methods);
						properties.put("data", methods);

						try {
							@SuppressWarnings("deprecation")
							FileOutputStream stream = face_Activity.this
									.openFileOutput(
											"methods.cfg",
											Context.MODE_WORLD_READABLE
													+ Context.MODE_WORLD_WRITEABLE);
							properties.store(stream, "");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						/* 生成故障历史 */
						j++;
						if (j == countMethod) {
							System.out.println(miCount
									+ table_data[miCount - 1]);
							methodData.insert(table_data[miCount - 1],
									table_data[miCount - 1]);
							propertiesProblem.put("problem",
									table_data[miCount - 1]);
							try {
								@SuppressWarnings("deprecation")
								FileOutputStream stream = face_Activity.this
										.openFileOutput(
												"problem.cfg",
												Context.MODE_WORLD_READABLE
														+ Context.MODE_WORLD_WRITEABLE);
								properties.store(stream, "");
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				Toast.makeText(face_Activity.this, "方案已保存", Toast.LENGTH_SHORT)
						.show();
			} else if (v == mBack) {
				/* 直接退出 */
				if (flag == false) {
					Dialog dlg = new AlertDialog.Builder(face_Activity.this)
							.setTitle("友情提示")
							.setMessage("您还没有选择任何方案,确认退出吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Intent intent = new Intent();
											intent.putExtra("level", level);
											intent.putExtra("user", user);
											//intent.setClass(face_Activity.this,
													//MessageListActivity.class);
											startActivity(intent);

										}
									})
							.setNeutralButton("退出",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									}).create();
					dlg.show();
				} else {
					Toast.makeText(face_Activity.this, "直接退出",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra("level", level);
					intent.putExtra("user", user);
					//intent.setClass(face_Activity.this,
							//MessageListActivity.class);
					startActivity(intent);
				}
			}
		}

	};

	class CheckBoxListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// Toast
				AddData(buttonView.getText().toString());
				// update(buttonView.getText().toString());
				Toast.makeText(face_Activity.this,
						buttonView.getText() + "被选择", Toast.LENGTH_SHORT)
						.show();
			} else {
				DeleteData(buttonView.getText().toString());
				Toast.makeText(face_Activity.this,
						buttonView.getText() + "取消选择", Toast.LENGTH_SHORT)
						.show();
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
			flag = false;
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
		for (int i = 0; i < rows_num; i++) {
			ListStatus lStatus = new ListStatus();
			lStatus.setId(i);
			lStatus.setStatus(false);
			arrList.add(lStatus);
		}
		countMethod = rows_num;

		for (int i = 0; i < rows_num; i++) {
			System.out.println(arrList.get(i).getId());
		}

		if (rows_num != 0) {
			cur_all.moveToFirst();
			for (int i = 0; i < rows_num; i++) {
				int _id = cur_all.getInt(0);
				// int num = cur_all.getInt(1);
				String value = cur_all.getString(3);
				System.out.println(value);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("image", init);
				map.put("text", value);
				mList.add(map);
				cur_all.moveToNext();
			}
		}

		adapter = new SimpleAdapter(face_Activity.this, mList,
				R.layout.solvedlistview, new String[] { "image", "text" },
				new int[] { R.id.img, R.id.title });
		m_ListView.setAdapter(adapter);
		cur_all.close();

		m_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				flag = true;
				selectItem = position;
				switch (position) {
				case 0:
					showDialog();
					break;
				case 1:
					showDialog();
					break;
				case 2:
					showDialog();
					break;
				case 3:
					showDialog();
					break;
				case 4:
					showDialog();
					break;
				default:
					showDialog();
					break;
				}

			}

		});
	}

	private void showDialog() {
		LayoutInflater factory = LayoutInflater.from(face_Activity.this);
		final View DialogView = factory.inflate(R.layout.dialogview, null);
		AlertDialog dialog = new AlertDialog.Builder(face_Activity.this)
				.setTitle("解决方案").setView(DialogView)
				.setPositiveButton("可用", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						arrList.get(selectItem).setStatus(true);
						ChangeImg(selectItem, true);

					}
				})
				.setNeutralButton("不可用", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						arrList.get(selectItem).setStatus(false);
						ChangeImg(selectItem, false);
					}

				}).create();
		dialog.show();
	}

	private void ChangeImg(int selectedItem, boolean b) {
		SimpleAdapter la = adapter;
		System.out.println(selectedItem);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) la
				.getItem(selectedItem);
		if (b) {
			map.put("image", on);
		} else {
			map.put("image", off);
		}
		la.notifyDataSetChanged();
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