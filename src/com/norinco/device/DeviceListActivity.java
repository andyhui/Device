package com.norinco.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends Activity{
		private ListView listView;
		private List<Map<String, String>> personList = new ArrayList<Map<String, String>>();

		private SimpleAdapter simpleAdapter = null;
		private RelativeLayout layout = null;
		
		private GridView gridView_menu;
		private EditText search;
		
		private HashMap map;
		
		private int l;
		private int flag=0;
		private String user;
		
		DeviceDao personDao;
		public void onCreate(Bundle savedInstanceState) {
			System.out.println("-------DeviceListActivity-------");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.devicelist);
			
			Intent lintent=getIntent();
			Bundle bundle=lintent.getExtras();
			l=bundle.getInt("level");
			user=bundle.getString("user");
			flag=bundle.getInt("flag");
			
			if(flag==1)
			{
				personList = new ArrayList<Map<String, String>>();
				map = (HashMap) lintent.getSerializableExtra("device");
				personList.add(map);
			}
			else
			{
				personDao = new DeviceDao(this);
				personList = personDao.findAllD();
			}
			simpleAdapter = new SimpleAdapter(this, personList, R.layout.devicedatelist, new String[]{"devname","number"},new int[]{R.id.tv_dname,R.id.tv_duser});
			listView = (ListView) findViewById(R.id.lv_duserlist);
			listView.setAdapter(simpleAdapter);
			listView.setOnItemClickListener(listener);
			
			search = (EditText) findViewById(R.id.et_dsearch);
			search.addTextChangedListener(watcher);			
			
			buttomMenu();
		}

		private TextWatcher watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String devnameordevnum = search.getText().toString();
				personList = personDao.findByIdOrName(devnameordevnum);
				listView = (ListView) findViewById(R.id.lv_duserlist);
				simpleAdapter = new SimpleAdapter(DeviceListActivity.this, personList, R.layout.devicedatelist,new String[]{"devname","number"},
										new int[]{R.id.tv_dname,R.id.tv_duser});
				listView.setAdapter(simpleAdapter);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			
			public void afterTextChanged(Editable s) {
				
			}
		};
		
		private OnItemClickListener listener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position,long id) {
				HashMap map = (HashMap) adapterView.getItemAtPosition(position);
				System.out.println("---> adapterView "+adapterView.getItemAtPosition(position));
				Intent intent = new Intent();
				intent.putExtra("device", map);
				intent.putExtra("level", l);
				intent.putExtra("user", user);
				intent.putExtra("flag", flag);
				intent.setClass(DeviceListActivity.this, DeviceViewActivity.class);
				startActivity(intent);
				finish();
			}
		};
		
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			System.out.println("-----");
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				if (gridView_menu == null) {
					this.buttomMenu();
				}
				if (gridView_menu.getVisibility() == View.GONE) {
					gridView_menu.setVisibility(View.VISIBLE);
				} else {
					gridView_menu.setVisibility(View.GONE);
				}
			}
			return super.onKeyDown(keyCode, event);
		}
		private void buttomMenu(){
			gridView_menu = (GridView) findViewById(R.id.gv_dbutton_menu);
			gridView_menu.setBackgroundResource(R.drawable.channelgallery_bg);
			gridView_menu.setNumColumns(5);
			gridView_menu.setGravity(Gravity.CENTER);
			gridView_menu.setVerticalSpacing(10);
			gridView_menu.setHorizontalSpacing(10);
			
			List<Map<String, String>> data = new ArrayList<Map<String,String>>();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("itemIamge", R.drawable.imageadd+"");
			map.put("itemText", "新增");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put("itemIamge", R.drawable.imagedelete+"");
			map.put("itemText", "删除");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put("itemIamge", R.drawable.imagesearch+"");
			map.put("itemText", "搜索");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put("itemIamge", R.drawable.imagemenu+"");
			map.put("itemText", "视图");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put("itemIamge", R.drawable.imageexit+"");
			map.put("itemText", "退出");
			data.add(map);
			
			simpleAdapter = new SimpleAdapter(DeviceListActivity.this, data, R.layout.buttom_menu,new String[]{"itemIamge","itemText"},new int[]{R.id.item_image,R.id.item_tv});
			gridView_menu.setAdapter(simpleAdapter);
			
			gridView_menu.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> adapterView, View view, int position,
						long id) {
					System.out.println("------- >"+position);
					switch (position) {
							case 0:
								if(l==3){
									Intent aintent = new Intent(DeviceListActivity.this,DeviceAddActivity.class);
									aintent.putExtra("level", l);
									startActivity(aintent);
									finish();
								}
								break;
							case 1:
								if(l==3){
									Intent dintent = new Intent(DeviceListActivity.this,DeviceCheckBoxActivity.class);	
									dintent.putExtra("level",l);
									startActivity(dintent);
									finish();
								}
								break;
							case 2:
								if(layout == null){
									layout = (RelativeLayout) findViewById(R.id.drl_header_layout);
								}
								if(layout.getVisibility() == View.GONE){
									layout.setVisibility(View.VISIBLE);
								}
								else{
									layout.setVisibility(View.GONE);
								}
								break;
							case 3:
								Toast.makeText(DeviceListActivity.this, "该功能暂时未提供", Toast.LENGTH_LONG).show();
								break;
							case 4:
								Intent startMain = new Intent(DeviceListActivity.this,MessageListActivity.class);
								startMain.putExtra("level", l);
								startMain.putExtra("user", user);
								startActivity(startMain);
								finish();
								break;
					}					
				}
			});
		}
}

