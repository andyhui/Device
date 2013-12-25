package com.norinco.device;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class MDeviceCheckBoxActivity extends Activity{
	private CheckBox checkBoxAll;
	private CheckBox cb_checkBox;
	private ListView listView;
	private Button but_delete;
	private Button but_return;
	boolean isAll = false;
	private MdBoxAdapter checkBoxAdaper;
	private Cursor cursor;
	private DeviceDao personDao;
	
	private int l;
	private String user;
	private String columnId;
	
	private boolean flag = false;
	private Map mapId = new HashMap();

	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");
		setTitle("删除列表信息");
		listView = (ListView) findViewById(R.id.lv_cb);
		checkBoxAll = (CheckBox) findViewById(R.id.cb_checkall);
		personDao = new DeviceDao(this);
		
		Map<String, Cursor> map = personDao.findM();
		cursor = map.get("cursor");

		checkBoxAdaper = new MdBoxAdapter(this, R.layout.contactlistitem,
				cursor, new String[] { "number","replevel","finishDate" }, new int[] {
						R.id.tv_cb_id,R.id.tv_cb_name,R.id.tv_cb_level});

		listView.setAdapter(checkBoxAdaper);

		checkBoxAll.setOnCheckedChangeListener(listener);System.out.println("---------4D0----------");
		listView.setOnItemClickListener(listViewListener);System.out.println("---------4D1----------");
		
		but_delete = (Button) findViewById(R.id.but_delete);System.out.println("---------4D2----------");
		but_return = (Button) findViewById(R.id.but_return);System.out.println("---------4D3----------");
		but_delete.setOnClickListener(buttonListener);System.out.println("---------4D4----------");
		but_return.setOnClickListener(buttonListener);System.out.println("---------4D5----------");
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				flag = true;
				
				for (int i = 0; i < checkBoxAdaper.getCount(); i++) {

					checkBoxAdaper.map.put(i, true);
				}
			} else {
				flag = false;
				for (int i = 0; i < checkBoxAdaper.getCount(); i++) {
					checkBoxAdaper.map.put(i, false);
				}
			}
			listView.setAdapter(checkBoxAdaper);
			
		}
	};

	private OnItemClickListener listViewListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long arg3) {
			cb_checkBox = (CheckBox) view.findViewById(R.id.cb_checkbox);
			TextView textViewid = (TextView) view.findViewById(R.id.tv_cb_id);
			cb_checkBox.toggle();
			if (cb_checkBox.isChecked()) {
				checkBoxAdaper.map.put(position, true);
				columnId = textViewid.getText().toString();
			
				mapId.put(columnId, columnId);
			} else {
				checkBoxAdaper.map.put(position, false);
				System.out.println("mapId.remove(columnId);= "
						+ mapId.remove("" + columnId));
				mapId.remove("" + columnId);
			}
		}
	};
	
	private OnClickListener buttonListener = new OnClickListener() {

		public void onClick(View v) {
			Button button = (Button) v;
			int id = button.getId();
			if (id == R.id.but_return) {
				personDao.close();
				openActivity(v);
			} else if (id == R.id.but_delete) {
				AlertDialog.Builder alert = new AlertDialog.Builder(MDeviceCheckBoxActivity.this);
				alert.setIcon(R.drawable.warming);
				alert.setTitle("警告提示");
				alert.setMessage("确定要删除选中的信息？");
				alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							personDao.clearTableM();
						} else {
							Set set = mapId.entrySet();
							Iterator iterator = set.iterator();
							while (iterator.hasNext()) {
								Map.Entry me = (Entry) iterator.next();
								System.out.println("me.getValue();;" + me.getValue());
								String id = me.getValue() + "";
								personDao.deleteM(id);
							}
						}
						Map<String, Cursor> map = personDao.findM();
						cursor = map.get("cursor");
						
						checkBoxAdaper = new MdBoxAdapter(
								MDeviceCheckBoxActivity.this, R.layout.contactlistitem,
								cursor, new String[] { "number", "replevel","finishDate"},
								new int[] { R.id.tv_cb_id, R.id.tv_cb_name,R.id.tv_cb_level });
						listView.setAdapter(checkBoxAdaper);
					}
				});
				alert.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				alert.create().show();
			}
		}
	};
	public void openActivity(View v){
		Intent intent = new Intent(this,MDeviceListActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user",user);
		startActivity(intent);
		finish();
	}
}