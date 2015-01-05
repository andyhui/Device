package com.cngc.hht;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RfidSetting extends Activity {

	private int flag = 0;

	private RfidPower power;

	private ListView list_menu;
	private String[] liststr = { "硬件版本", "固件版本", "发射功率", "射频频率状态",
			"RF LINK RPOFILE", "当前天线", "当前频率区域", "当前读写器温度", "Gen2参数" };
	private String[] liststr_value = null;

	private String[] liststr_txpower = { "状态", "读功率", "写功率" };
	private String[] liststr_txpower_value = null;

	private String[] liststr_rdstate = { "频点数", "频点" };
	private String[] liststr_rdstate_value = null;
	private int[] rdstate;

	private String[] liststr_gen2 = { "Q算法", "StartQ", "MinQ", "MaxQ" };
	private String[] liststr_gen2_value = null;

	private ArrayList<Map<String, String>> mData;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GetData();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);

		list_menu = (ListView) findViewById(R.id.setting_menu);
		Init(liststr, liststr_value);
		list_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 2 && flag == 0) {
					flag = 1;
					Init(liststr_txpower, liststr_txpower_value);
				} else if (position == 3 && flag == 0) {
					flag = 1;
					showradiostate();
				} else if (position == 8 && flag == 0) {
					flag = 1;
					Init(liststr_gen2, liststr_gen2_value);
				}
			}
		});
	}

	private void GetData() {
		int ret;
		power = new RfidPower();
		if ((ret = power.openserial()) < 0) {
			if (ret == -2) {
				Toast.makeText(RfidSetting.this, "无RFID设备可用，请检查再试",
						Toast.LENGTH_LONG).show();
			}
			finish();
		}
		power.getsetting();
		power.closeserial();

		liststr_value = new String[10];
		liststr_value = power.getRfidState();

		liststr_txpower_value = new String[4];
		liststr_txpower_value = power.getTxpower();

		int i;
		rdstate = new int[20];
		rdstate = power.getRadiostate();
		liststr_rdstate_value = new String[rdstate[0] + 2];
		liststr_rdstate_value[0] = String.valueOf(rdstate[0]);
		for (i = 1; i < (rdstate[0] + 1); i++)
			liststr_rdstate_value[i] = String.valueOf(rdstate[i]) + "KHZ";
		liststr_rdstate_value[i] = "";

		liststr_gen2_value = new String[5];
		liststr_gen2_value = power.getGen2();

	}

	private void showradiostate() {
		mData = new ArrayList<Map<String, String>>();
		int lengh = liststr_rdstate_value.length - 1;
		for (int i = 0; i < lengh; i++) {
			Map<String, String> item = new HashMap<String, String>();
			if (i == 0)
				item.put("param_name", liststr_rdstate[0]);
			else
				item.put("param_name", liststr_rdstate[1] + i);
			item.put("param_value", liststr_rdstate_value[i]);
			mData.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, mData,
				R.layout.setting_value, new String[] { "param_name",
						"param_value" }, new int[] { R.id.param_name,
						R.id.param_value });
		list_menu.setAdapter(adapter);
	}

	private void Init(String[] name, String[] value) {
		mData = new ArrayList<Map<String, String>>();
		int lengh = name.length;
		for (int i = 0; i < lengh; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("param_name", name[i]);
			item.put("param_value", value[i]);
			mData.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, mData,
				R.layout.setting_value, new String[] { "param_name",
						"param_value" }, new int[] { R.id.param_name,
						R.id.param_value });
		list_menu.setAdapter(adapter);
	}

	public void about_back(View v) {
		if (flag == 1) {
			flag = 0;
			Init(liststr, liststr_value);
		} else
			finish();
	}
}
