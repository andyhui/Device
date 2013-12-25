package com.norinco.rfid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norinco.device.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class RfidParamaSet extends Activity {

	ListView lv = null;

	// Spinner antenna;
	Spinner fraquency;
	// Spinner gpio;
	Spinner rflink;

	TextView frimwareVersion = null;
	TextView hardwareVersion = null;
	TextView temperature = null;

	EditText inquiryTime = null;
	// EditText chanlCount = null;
	// EditText chanlValue = null;

	CharSequence frim = "固件版本:v.1.0.0";
	CharSequence hard = "硬件版本:v.2.0.0";
	CharSequence temp = "温度：25度";

	// String[] antennaS = { "天线1", "天线2", "天线3", "天线4" };
	String[] fraquencyS = { "China1", "China2", "Europe", "USA", "Korea",
			"Japan" };
	// String[] switchS = { "开", "关" };
	String[] rflinkS = { "40KHZ", "250KHZ", "300KHZ", "400KHZ" };

	String[] lists = { "功率", "天线", "版本与温度", "跳频频道", "Gen2 参数", "GPIO 电平" };

	Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rfidparam);
		lv = (ListView) findViewById(R.id.listView);

//		frimwareVersion = (TextView) findViewById(R.id.firmwareversion);
//		hardwareVersion = (TextView) findViewById(R.id.hardwareversion);
//		temperature = (TextView) findViewById(R.id.temperature);

		inquiryTime = (EditText) findViewById(R.id.inquiry);
		// chanlCount = (EditText) findViewById(R.id.ChannelCount);
		// chanlValue = (EditText) findViewById(R.id.ChannelVuale);

		// 频道数
		// CharSequence chnalC = chanlCount.getText().toString();
		// 每个频道频道值
		// CharSequence chnalV = chanlValue.getText().toString();

//		frimwareVersion.setText(frim);
//		hardwareVersion.setText(hard);
//		temperature.setText(temp);

		// lv = new ListView(this);
		/* 填入循环查询周期时间 */
		CharSequence iqTime = inquiryTime.getText().toString();

		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			data.add(lists[i]);
		}

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, data);

		ArrayAdapter<String> adapter = new SmallArrayAdapter(this,
				android.R.layout.simple_list_item_1, data);

		lv.setAdapter(adapter);
		// setContentView(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					/* 功率 */
					intent.setClass(RfidParamaSet.this, powerModer.class);
					startActivity(intent);
					break;
				case 1:
					/* 天线 */
					intent.setClass(RfidParamaSet.this, antenna.class);
					startActivity(intent);
					break;
				case 2:
					/**/
					intent.setClass(RfidParamaSet.this, powerModer.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(RfidParamaSet.this, Frequency.class);
					startActivity(intent);
					break;
				case 4:
					/* gen2参数 */
					intent.setClass(RfidParamaSet.this, Gen2.class);
					startActivity(intent);
					break;
				case 5:
					/* 电平 */
					intent.setClass(RfidParamaSet.this, gpioMask.class);
					startActivity(intent);
					break;
				default:
					break;
				}

			}

		});

		// List<Map<String, Object>> items = new ArrayList<Map<String,
		// Object>>(); for (int i = 0; i < 10; i++) { Map<String, Object> item =
		// new HashMap<String, Object>(); item.put("antennas", lists[i]);
		// items.add(item); }
		//
		// SimpleAdapter adapter = new SimpleAdapter(this, items,
		// android.R.layout.simple_list_item_1, new String[] { "antennas" }, new
		// int[] { android.R.id.text1 });

		// lv.setAdapter(adapter);

		// antenna = (Spinner) findViewById(R.id.antenna);
		fraquency = (Spinner) findViewById(R.id.fraquency);
		// gpio = (Spinner) findViewById(R.id.gpio);
		rflink = (Spinner) findViewById(R.id.rflink);

		List<Map<String, Object>> fraquency_items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("fraquency", fraquencyS[i]);
			fraquency_items.add(item);
		}

		SimpleAdapter fraquencyS_adapter = new SimpleAdapter(this,
				fraquency_items, android.R.layout.simple_spinner_item,
				new String[] { "fraquency" }, new int[] { android.R.id.text1 });

		fraquencyS_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		fraquency.setAdapter(fraquencyS_adapter);

		/* 添加事件Spinner事件监听 */
		fraquency.setOnItemSelectedListener(new SpinnerSelectedListener());
		fraquency.setVisibility(View.VISIBLE);

		List<Map<String, Object>> rflink_items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("rflink", rflinkS[i]);
			rflink_items.add(item);
		}

		SimpleAdapter rflink_adapter = new SimpleAdapter(this, rflink_items,
				android.R.layout.simple_spinner_item,
				new String[] { "rflink" }, new int[] { android.R.id.text1 });
		rflink_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		rflink.setAdapter(rflink_adapter);

		/* 添加事件Spinner事件监听 */
		rflink.setOnItemSelectedListener(new SpinnerSelectedListener());
		rflink.setVisibility(View.VISIBLE);

	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SmallArrayAdapter extends ArrayAdapter<String> {

		private int resourceId;

		public SmallArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			this.resourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String user = getItem(position);

			LinearLayout userListItem = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resourceId, userListItem, true);

			TextView text1 = (TextView) userListItem
					.findViewById(android.R.id.text1);
			// TextView text1 = (TextView)
			// convertView.findViewById(android.R.id.text1);
			text1.setTextSize(14);
			text1.setText(user);

			return userListItem;
		}

	}

}
