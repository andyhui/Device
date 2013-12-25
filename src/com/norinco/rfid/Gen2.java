package com.norinco.rfid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norinco.device.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Gen2 extends Activity {

	String[] algorithmS = { "动态Q算法", "固定Q算法" };
	Spinner algorithmStyle = null;
	EditText init = null;
	EditText min = null;
	EditText max = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gen2);

		algorithmStyle = (Spinner) findViewById(R.id.algorithmstyle);
		init = (EditText) findViewById(R.id.init);
		min = (EditText) findViewById(R.id.minvalue);
		max = (EditText) findViewById(R.id.maxvalue);

		String initVal = init.getText().toString();
		String minVal = min.getText().toString();
		String maxVal = max.getText().toString();

		List<Map<String, Object>> algorithmStyle_items = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < 2; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("algorithm", algorithmS[i]);
			algorithmStyle_items.add(item);
		}

		SimpleAdapter algorithmStyle_adapter = new SimpleAdapter(this,
				algorithmStyle_items, android.R.layout.simple_spinner_item,
				new String[] { "algorithm" }, new int[] { android.R.id.text1 });

		algorithmStyle_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		algorithmStyle.setAdapter(algorithmStyle_adapter);

		/* 添加事件Spinner事件监听 */
		algorithmStyle.setOnItemSelectedListener(new SpinnerSelectedListener());
		algorithmStyle.setVisibility(View.VISIBLE);

	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}
