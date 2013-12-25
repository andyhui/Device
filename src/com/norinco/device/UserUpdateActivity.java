package com.norinco.device;

import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserUpdateActivity extends Activity{
		private EditText et_name;
		private EditText et_mps;
		private EditText et_mlevel;
		private Button but_save;
		private Button but_list;
		private int l;
		private String user;
		Intent intent = null;
		private DeviceDao personDao;
		HashMap map;

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.personupdate);
			personDao = new DeviceDao(this);
			initWidget();
			intent = getIntent();
			Bundle bundle = intent.getExtras();
			l = bundle.getInt("level");
			user = bundle.getString("user");
			map = (HashMap) intent.getSerializableExtra("login");
			et_name.setText(map.get("username")+"");
			et_mps.setText(map.get("password")+"");
			et_mlevel.setText(map.get("level")+"");
			
		}
		private void initWidget(){
			et_name = (EditText) findViewById(R.id.et_name);
			et_mps = (EditText) findViewById(R.id.et_mps);
			et_mlevel = (EditText) findViewById(R.id.et_mlevel);

			but_list = (Button) findViewById(R.id.but_list);
			but_list.setOnClickListener(listener);
			
			but_save = (Button) findViewById(R.id.but_save);
			but_save.setOnClickListener(listener);
		}
		
		private OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				
				Button but = (Button) v;
				int id = but.getId();
				if (id == R.id.but_list) {
					openActivity(v);
				} else if (id == R.id.but_save) {
					ContentValues values = new ContentValues();
					values.put("_id", map.get("_id")+"");
					values.put("username", et_name.getText().toString());
					values.put("password", et_mps.getText().toString());
					values.put("level", et_mlevel.getText().toString());
					int sun = personDao.update(values);
					if (sun == -1) {
						
						Toast.makeText(UserUpdateActivity.this, " 更新错误", Toast.LENGTH_LONG).show();
						return;
					}else{
						Toast.makeText(UserUpdateActivity.this, "数据已成功更新", Toast.LENGTH_LONG).show();
					}
					openActivity(v);
				}
			}
		};
		public void openActivity(View v){
			Intent intent = new Intent(this,UserListActivity.class);
			intent.putExtra("level",l);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
		}
}
