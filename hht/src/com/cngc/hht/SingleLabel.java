package com.cngc.hht;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.norinco.eme.face_Activity001;

public class SingleLabel extends Activity {
	private RfidPower power;
	private DeviceInfo device = null;
	private TextView tv_devname;
	private TextView tv_devnum;
	private TextView tv_devSN;
	private TextView tv_group;
	private TextView tv_factory;
	private TextView tv_prodate;
	private TextView tv_recdate;

	private TextView modify;

	private DataBase db;
	private ListView list;
	private SimpleAdapter simpleAdapter=null;
	private List<Map<String, String>> List=null;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deviceinfo);
		init();
		readlabel();
		setdata();
	}

	private void setdata() {
		tv_devname.setText(device.getEquipmentName());
		tv_devnum.setText(String.valueOf(device.getEquipAssetNo()));
		tv_devSN.setText(String.valueOf(device.getEquipmentSN()));
		tv_group.setText(String.valueOf(device.getEquipManuClass()));
		tv_factory.setText(device.getEquipFactory());
		tv_prodate.setText(device.getEquipManuTime());
		tv_recdate.setText(device.getEquipFitoutTime());

		db.getModifyInfo(device.getEquipAssetNo());
		List = db.getList();
		simpleAdapter = new SimpleAdapter(this, List, R.layout.faultinfo,
				new String[] { "faultRepairTime" },
				new int[] { R.id.modifytime });
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(listener);
	}

	private void init() {
		tv_devname = (TextView) findViewById(R.id.tv_devname);
		tv_devnum = (TextView) findViewById(R.id.tv_devnum);
		tv_devSN = (TextView) findViewById(R.id.tv_devSN);
		tv_group = (TextView) findViewById(R.id.tv_group);
		tv_factory = (TextView) findViewById(R.id.tv_factory);
		tv_prodate = (TextView) findViewById(R.id.tv_prodate);
		tv_recdate = (TextView) findViewById(R.id.tv_recdate);
		list = (ListView) findViewById(R.id.dmlist);

		modify = (TextView) findViewById(R.id.modify);
		modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HashMap<String, String> map;
				map = new HashMap<String, String>();
				map.put("devname", device.getEquipmentName());
				map.put("number", String.valueOf(device.getEquipAssetNo()));
				map.put("devgroup", String.valueOf(device.getEquipManuClass()));
				map.put("factory", device.getEquipFactory());
				map.put("productdate", device.getEquipManuTime());
				map.put("recvdate", device.getEquipFitoutTime());

				Intent intent = new Intent(SingleLabel.this,
						face_Activity001.class);
				 intent.putExtra("device", map);
				 startActivity(intent);
			}
		});

		power = new RfidPower();

		db = new DataBase();
	}

	private void readlabel() {
		System.out.println("start to read label*********");
		power.openserial();
		if (power.readlabel(0) < 0) {
			new AlertDialog.Builder(SingleLabel.this)
					.setTitle("警告")
					.setMessage("读取失败！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									power.closeserial();
									JumpActivity(MainActivity.class);
								}
							}).show();
		} else {
			power.closeserial();
			device = power.getDevice();
		}
	}

	private OnItemClickListener listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) adapterView
					.getItemAtPosition(position);
			AlertDialog.Builder dlg = new AlertDialog.Builder(SingleLabel.this);
			dlg.setTitle("故障维修信息！");
			dlg.setMessage("维修日期：" + map.get("faultRepairTime") + "\n故障描述："
					+ map.get("faultDescription") + "\n维修方案："
					+ map.get("faultSolution"));
			dlg.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			dlg.create().show();
		}
	};

	private void JumpActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

	public void about_back(View v) {
		//finish();
		//JumpActivity(MainActivity.class);
	}
}
