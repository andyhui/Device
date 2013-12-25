package com.norinco.device;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class DeviceAddActivity extends Activity {
	private EditText et_devname;
	private EditText et_devnum;
	private EditText et_devgroup;
	private EditText et_factory;
	private EditText et_prodate;
	private EditText et_recdate;
	private EditText et_modifydate;
	
	private Button but_save;
	private Button but_return;
	private Button prodate;
	private Button recdate;
	private Button modifydate;
	
	private int l;
	private String user;
	private char flag;
	
	private Device person;
	private DeviceDao personDao;
	
	int ret;
	int mFd;
	
	private int year=2013;
	private int month=6;
	private int day=01;
	
	static SerialPort sp=null;
	static GetData gd=null;
	
	DatePickerDialog datepickerdialog;
	
	HashMap<String,String> map;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deviceadd);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");
		flag=bundle.getChar("action");
		personDao = new DeviceDao(this);
		initWidget();
	}
	
	private void initWidget(){
		et_devname = (EditText) findViewById(R.id.et_tv_devname);
		et_devnum = (EditText) findViewById(R.id.et_tv_devnum);
		et_devgroup = (EditText) findViewById(R.id.et_tv_devgroup);
		et_factory = (EditText) findViewById(R.id.et_tv_factory);
		et_prodate = (EditText) findViewById(R.id.et_tv_prodate);
		et_recdate = (EditText) findViewById(R.id.et_tv_recdate);
		et_modifydate = (EditText) findViewById(R.id.et_tv_modifydate);
		
		but_save = (Button) findViewById(R.id.dabut_save);
		but_save.setOnClickListener(listener);
		but_return = (Button) findViewById(R.id.dabut_list);
		but_return.setOnClickListener(listener);
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
			int butin = but.getId();
			if (butin==R.id.dabut_save) {
				person = new Device();
				String devname = et_devname.getText().toString();
				String devnum = et_devnum.getText().toString();
				String devgroup = et_devgroup.getText().toString();
				String factory = et_factory.getText().toString();
				String prodate = et_prodate.getText().toString();
				String recdate = et_recdate.getText().toString();
				String modifydate = et_modifydate.getText().toString();
				
				if ("".equals(devname)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备名称", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(devnum)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备号", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(devgroup)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备批次", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(factory)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备的生产厂商", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(prodate)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备的生产日期", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(recdate)) {
					Toast.makeText(DeviceAddActivity.this, "请输入设备的签收日期", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(flag=='w')
				{
					try {
						sp=new SerialPort(new File("/dev/s3c2410_serial3"),115200);
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					mFd=sp.getmFd();
					gd=new GetData(mFd,1);
					gd.setFactoryId(factory);
					gd.setDeviceId(devname);
					gd.setDevnum(Integer.parseInt(devnum));
					gd.setDevgroup(Integer.parseInt(devgroup));
					gd.setProdate(prodate);
					gd.setRecdate(recdate);
					gd.setMdfdate(modifydate);
					/*gd.setFactoryId("北方信息集团");
					gd.setDeviceId("履带式步兵战车");
					gd.setDevnum(6789);
					gd.setDevgroup(2);
					gd.setProdate("1987.11.2");
					gd.setRecdate("1988.12.6.1");
					gd.setMdfdate("1990.5.23");*/
					
					ret=gd.setDataLabel(mFd);
					if(ret==0)
						Toast.makeText(DeviceAddActivity.this, "写入数据失败", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(DeviceAddActivity.this, "成功写入数据", Toast.LENGTH_LONG).show();
					sp.close(mFd);
					
					Intent intent = new Intent(DeviceAddActivity.this,MessageListActivity.class);
					intent.putExtra("level", l);
					intent.putExtra("user",user);
					startActivity(intent);
					finish();
				}
				else
				{
					person.setDevname(devname);
					person.setDevnum(Integer.parseInt(devnum));
					person.setFactory(factory);
					person.setDevgroup(Integer.parseInt(devgroup));
					person.setProdate(prodate);
					person.setRecdate(recdate);
					person.setModifydate(modifydate);
				
					personDao.saveD(person);
					
					openActivity(v);
				}
			}
			else if(butin==R.id.dabut_list) {
				openActivity(v);
				}
			else if(butin==R.id.button1 || butin==R.id.Button01 || butin==R.id.Button02) {				
				if(butin==R.id.button1) {
					datepickerdialog = new DatePickerDialog(DeviceAddActivity.this,new pickdialog(),year,month,day);
					}
				else if(butin==R.id.Button01) {
					datepickerdialog = new DatePickerDialog(DeviceAddActivity.this,new pickdialog1(),year,month,day);
					}
				else if(butin==R.id.Button02) {
					datepickerdialog = new DatePickerDialog(DeviceAddActivity.this,new pickdialog2(),year,month,day);
					}
				datepickerdialog.show();
				}
			
			}
		};
	public void openActivity(View v){
		Intent intent = new Intent(this,DeviceListActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user",user);
		startActivity(intent);
		finish();
		}
	
	public class pickdialog implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			monthOfYear += 1;
			et_prodate.setText(year+"."+monthOfYear+"."+dayOfMonth);			
		}
		
	}
	public class pickdialog1 implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			monthOfYear += 1;
			et_recdate.setText(year+"."+monthOfYear+"."+dayOfMonth);			
		}
		
	}
	public class pickdialog2 implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			monthOfYear += 1;
			et_modifydate.setText(year+"."+monthOfYear+"."+dayOfMonth);			
		}
		
	}
}
