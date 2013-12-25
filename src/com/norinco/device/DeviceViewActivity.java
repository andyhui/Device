package com.norinco.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norinco.eme.Eme_fault;
import com.norinco.eme.history_fault;
import com.norinco.eme.history_method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceViewActivity extends Activity {
	private Button but_return;
	private Button but_update;

	private HashMap map;

	private int on = R.drawable.arrow_right;

	List<HashMap<String, Object>> mylist;

	SimpleAdapter adapter;

	ListView myListView = null;

	private int l;
	private int m = 0;
	private int flag;

	private String user;

	private ListView listView;
	private List<Map<String, String>> personList = new ArrayList<Map<String, String>>();

	private SimpleAdapter simpleAdapter = null;

	DeviceDao personDao;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deviceview);
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		l = bundle.getInt("level");
		user = bundle.getString("user");
		flag = bundle.getInt("flag");

		map = (HashMap) intent.getSerializableExtra("device");

		((TextView) findViewById(R.id.tv_devname)).setText(map.get("devname")
				+ "");
		((TextView) findViewById(R.id.tv_devnum)).setText(map.get("number")
				+ "");
		((TextView) findViewById(R.id.tv_group)).setText(map.get("devgroup")
				+ "");
		((TextView) findViewById(R.id.tv_factory)).setText(map.get("factory")
				+ "");
		((TextView) findViewById(R.id.tv_prodate)).setText(map
				.get("productdate") + "");
		((TextView) findViewById(R.id.tv_recdate)).setText(map.get("recvdate")
				+ "");
		((TextView) findViewById(R.id.tv_modifydate)).setText(map
				.get("modifydate") + "");

		personDao = new DeviceDao(this);
		personList = personDao.findBynum(map.get("number") + "");
		listView = (ListView) findViewById(R.id.dmlist);
		simpleAdapter = new SimpleAdapter(this, personList,
				R.layout.mdevicedatelist,
				new String[] { "devnum", "finishdate" }, new int[] {
						R.id.textView2, R.id.tv_mduser });
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(listener1);

		but_return = (Button) findViewById(R.id.dbut_return);
		but_return.setOnClickListener(listener);

		but_update = (Button) findViewById(R.id.dbut_update);
		but_update.setOnClickListener(listener);

		/*
		 * 以下是显示修复问题的list
		 * 
		 * @liuhuiwu
		 */
		myListView = (ListView) findViewById(R.id.myListView);
		mylist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();

		map1.put("text", this.getString(R.string.history_fault));
		map1.put("img", on);
		map2.put("text", this.getString(R.string.update_fault));
		map2.put("img", on);
		map3.put("text", this.getString(R.string.history_sloved));
		map3.put("img", on);

		mylist.add(map1);
		mylist.add(map2);
		mylist.add(map3);

		adapter = new SimpleAdapter(this, mylist, R.layout.device_fault_list,
				new String[] { "text", "img" }, new int[] { R.id.text_fault,
						R.id.img_fault });
		
//		adapter = new SimpleAdapter(this, mylist, android.R.layout.simple_list_item_1,
//				new String[] { "text", "img" }, new int[] { R.id.text_fault,
//						R.id.img_fault });
		myListView.setAdapter(adapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Map<String, Object> map = mylist.get(position);
				switch (position) {
				case 0:
					Intent intent1 = new Intent();
					intent1.putExtra("level", l);
					intent1.putExtra("user", user);
					intent1.setClass(DeviceViewActivity.this,
							history_fault.class);
					startActivity(intent1);
					break;
				case 1:
					Intent intent2 = new Intent();
					intent2.putExtra("level", l);
					intent2.putExtra("user", user);
					intent2.setClass(DeviceViewActivity.this, Eme_fault.class);
					startActivity(intent2);
					break;
				case 2:
					Intent intent3 = new Intent();
					// intent3.setClass(MDeviceAddActivity.this,
					// history_solved.class);
					intent3.putExtra("level", l);
					intent3.putExtra("user", user);
					intent3.setClass(DeviceViewActivity.this,
							history_method.class);
					startActivity(intent3);
					break;
				/*
				 * case 3: Intent intent4 = new Intent();
				 * intent4.setClass(MDeviceAddActivity.this,
				 * update_solved.class); startActivity(intent4); break;
				 */
				default:
					break;
				}

			}

		});
		/*
		 * 以上是显示修复问题的list
		 * 
		 * @liuhuiwu
		 */

	}

	private OnItemClickListener listener1 = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			HashMap map = (HashMap) adapterView.getItemAtPosition(position);
			System.out.println("---> adapterView "
					+ adapterView.getItemAtPosition(position));
			Intent intent = new Intent();
			intent.putExtra("devicemodify", map);
			intent.putExtra("level", l);
			intent.putExtra("flag", m);
			intent.setClass(DeviceViewActivity.this, MDeviceViewActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {

			Button but = (Button) v;
			int id = but.getId();
			if (id == R.id.dbut_update) {
				Intent updateintent = new Intent();
				updateintent.setClass(DeviceViewActivity.this,
						DeviceUpdateActivity.class);
				updateintent.putExtra("device", map);
				updateintent.putExtra("level", l);
				updateintent.putExtra("user", user);
				startActivity(updateintent);
				finish();
			} else if (id == R.id.dbut_return) {
				Intent listintent = new Intent();
				if (flag == 0) {
					listintent.setClass(DeviceViewActivity.this,
							DeviceListActivity.class);
					listintent.putExtra("flag", flag);
				} else {
					listintent.setClass(DeviceViewActivity.this,
							MessageListActivity.class);
				}
				listintent.putExtra("level", l);
				listintent.putExtra("user", user);
				startActivity(listintent);
				finish();
			}
		}
	};
}