package com.norinco.eme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.norinco.device.DeviceViewActivity;
import com.norinco.device.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class TestActivity extends Activity{

	private int on = R.drawable.arrow_right;

	List<HashMap<String, Object>> mylist;

	SimpleAdapter adapter;

	ListView myListView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
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
//					intent1.putExtra("level", l);
//					intent1.putExtra("user", user);
					intent1.setClass(TestActivity.this,
							history_fault.class);
					startActivity(intent1);
					break;
				case 1:
					Intent intent2 = new Intent();
//					intent2.putExtra("level", l);
//					intent2.putExtra("user", user);
					intent2.setClass(TestActivity.this, Eme_fault.class);
					startActivity(intent2);
					break;
				case 2:
					Intent intent3 = new Intent();
					// intent3.setClass(MDeviceAddActivity.this,
					// history_solved.class);
//					intent3.putExtra("level", l);
//					intent3.putExtra("user", user);
					intent3.setClass(TestActivity.this,
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

}
