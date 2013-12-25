package com.norinco.device;

import java.util.HashMap;

import com.norinco.device.DeviceAddActivity.pickdialog;
import com.norinco.device.DeviceAddActivity.pickdialog1;
import com.norinco.device.DeviceAddActivity.pickdialog2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class DeviceUpdateActivity extends Activity{
		private EditText et_devname;
		private EditText et_devnum;
		private EditText et_devgroup;
		private EditText et_factory;
		private EditText et_prodate;
		private EditText et_recdate;
		private EditText et_modifydate;
		
		private int l;
		private int yearmain=2013;
		private int month=6;
		private int day=01;
		
		private Button but_save;
		private Button but_list;
		private Button prodate;
		private Button recdate;
		private Button modifydate;
		
		Intent intent = null;
		
		private DeviceDao personDao;
		
		private String user;
		
		HashMap map;
		
		DatePickerDialog datepickerdialog;

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.deviceupdate);
			personDao = new DeviceDao(this);
			initWidget();
			intent = getIntent();
			l=intent.getExtras().getInt("level");
			user=intent.getExtras().getString("user");
			map = (HashMap) intent.getSerializableExtra("device");
			et_devname.setText(map.get("devname")+"");
			et_devnum.setText(map.get("number")+"");
			et_factory.setText(map.get("factory")+"");
			et_devgroup.setText(map.get("devgroup")+"");
			et_prodate.setText(map.get("productdate")+"");
			et_recdate.setText(map.get("recvdate")+"");
			et_modifydate.setText(map.get("modifydate")+"");			
		}
		private void initWidget(){
			et_devname = (EditText) findViewById(R.id.et_tv_devname);
			et_devnum = (EditText) findViewById(R.id.et_tv_devnum);
			et_devgroup = (EditText) findViewById(R.id.et_tv_devgroup);
			et_factory = (EditText) findViewById(R.id.et_tv_factory);
			et_prodate = (EditText) findViewById(R.id.et_tv_prodate);
			et_recdate = (EditText) findViewById(R.id.et_tv_recdate);
			et_modifydate = (EditText) findViewById(R.id.et_tv_modifydate);
			

			but_list = (Button) findViewById(R.id.dmbut_list);
			but_list.setOnClickListener(listener);
			
			but_save = (Button) findViewById(R.id.dmbut_save);
			but_save.setOnClickListener(listener);
			
			prodate = (Button) findViewById(R.id.button1);
			prodate.setOnClickListener(listener);
			
			recdate =(Button) findViewById(R.id.Button01);
			recdate.setOnClickListener(listener);
			
			modifydate =(Button) findViewById(R.id.Button02);
			modifydate.setOnClickListener(listener);
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
					values.put("devname", et_devname.getText().toString());
					values.put("number", et_devnum.getText().toString());
					values.put("devgroup", et_devgroup.getText().toString());
					values.put("factory", et_factory.getText().toString());
					values.put("productdate", et_prodate.getText().toString());
					values.put("recvdate", et_recdate.getText().toString());
					values.put("modifydate", et_modifydate.getText().toString());
					int sun = personDao.updateD(values);
					if (sun == -1) {
						
						Toast.makeText(DeviceUpdateActivity.this, " 更新错误", Toast.LENGTH_LONG).show();
						return;
					}else{
						Toast.makeText(DeviceUpdateActivity.this, "数据已成功更新", Toast.LENGTH_LONG).show();
					}
					openActivity(v);
				}
				else {
					if(id==R.id.button1 || id==R.id.Button01 || id==R.id.Button02) {						
						if(id==R.id.button1) {							
							datepickerdialog = new DatePickerDialog(DeviceUpdateActivity.this,new datepicker(),yearmain,month,day);
						}
						else if(id==R.id.Button01) {
							datepickerdialog = new DatePickerDialog(DeviceUpdateActivity.this,new datepicker1(),yearmain,month,day);
						}
						else if(id==R.id.Button02) {
							datepickerdialog = new DatePickerDialog(DeviceUpdateActivity.this,new datepicker2(),yearmain,month,day);
						}
						datepickerdialog.show();
					}
				}
			}
		};
		public void openActivity(View v){
			Intent intent = new Intent(this,DeviceListActivity.class);
			intent.putExtra("level", l);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();
		}
		
		public class datepicker implements DatePickerDialog.OnDateSetListener{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				monthOfYear += 1;
				et_prodate.setText(year+"."+monthOfYear+"."+dayOfMonth);
			}			
		}
		public class datepicker1 implements DatePickerDialog.OnDateSetListener{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				monthOfYear += 1;
				et_recdate.setText(year+"."+monthOfYear+"."+dayOfMonth);
			}			
		}
		public class datepicker2 implements DatePickerDialog.OnDateSetListener{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				monthOfYear += 1;
				et_modifydate.setText(year+"."+monthOfYear+"."+dayOfMonth);
			}			
		}
}
