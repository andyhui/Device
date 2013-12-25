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

public class MDeviceUpdateActivity extends Activity{
		private EditText et_sperson;
		private EditText et_devnum;
		private EditText et_rperson;
		private EditText et_mtimes;
		private EditText et_mlevel;
		private EditText et_consume;
		private EditText et_finishdate;
		private EditText et_faulcause;
		
		private int l;
		private String user;
		
		private Button but_save;
		private Button but_list;
		
		Intent intent = null;
		private DeviceDao personDao;
		HashMap map;

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.mdeviceupdate);
			personDao = new DeviceDao(this);
			initWidget();
			intent = getIntent();
			l=intent.getExtras().getInt("level");
			user=intent.getExtras().getString("user");
			
			map = (HashMap) intent.getSerializableExtra("mdeviceview");
			et_sperson.setText(map.get("sperson")+"");
			et_devnum.setText(map.get("devnum")+"");
			et_rperson.setText(map.get("rperson")+"");
			et_mtimes.setText(map.get("mtimes")+"");
			et_mlevel.setText(map.get("mlevel")+"");
			et_consume.setText(map.get("consume")+"");
			et_finishdate.setText(map.get("finishdate")+"");
			et_faulcause.setText(map.get("faulecause")+"");
			
		}
		private void initWidget(){
			et_sperson = (EditText) findViewById(R.id.et_tv_sperson);
			et_devnum = (EditText) findViewById(R.id.et_tv_devnum);
			et_rperson = (EditText) findViewById(R.id.et_tv_rperson);
			et_mtimes = (EditText) findViewById(R.id.et_tv_mtimes);
			et_mlevel = (EditText) findViewById(R.id.et_tv_mlevel);
			et_consume = (EditText) findViewById(R.id.et_tv_consume);
			et_finishdate = (EditText) findViewById(R.id.et_tv_finishdate);
			et_faulcause = (EditText) findViewById(R.id.et_tv_faultcause);
			

			but_list = (Button) findViewById(R.id.dmbut_list);
			but_list.setOnClickListener(listener);
			
			but_save = (Button) findViewById(R.id.dmbut_save);
			but_save.setOnClickListener(listener);
		}
		
		private OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				
				Button but = (Button) v;
				int id = but.getId();
				if (id == R.id.dmbut_list) {
					openActivity(v);
				} else if (id == R.id.dmbut_save) {
					ContentValues values = new ContentValues();
					values.put("_id", map.get("_id")+"");
					values.put("sendname", et_sperson.getText().toString());
					values.put("number", et_devnum.getText().toString());
					values.put("recvname", et_rperson.getText().toString());
					values.put("reptimes", et_mtimes.getText().toString());
					values.put("replevel", et_mlevel.getText().toString());
					values.put("faultCause", et_faulcause.getText().toString());
					values.put("consume", et_consume.getText().toString());
					values.put("finishDate", et_finishdate.getText().toString());
					int sun = personDao.updateM(values);
					if (sun == -1) {
						
						Toast.makeText(MDeviceUpdateActivity.this, " 更新错误", Toast.LENGTH_LONG).show();
						return;
					}else{
						Toast.makeText(MDeviceUpdateActivity.this, "数据已成功更新", Toast.LENGTH_LONG).show();
					}
					openActivity(v);
				}
			}
		};
		public void openActivity(View v){
			Intent intent = new Intent(this,MDeviceListActivity.class);
			intent.putExtra("level", l);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
		}
}
