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

public class powerModer extends Activity {

	String[] powerS = { "开环", "闭环" };
	Spinner powerModer = null;
	EditText readPower = null;
	EditText writePower = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.power);

		powerModer = (Spinner) findViewById(R.id.moder);
		readPower = (EditText)findViewById(R.id.read);
		writePower = (EditText)findViewById(R.id.write);
		
		String readP = readPower.getText().toString();
		String writeP = writePower.getText().toString();
	

		List<Map<String, Object>> powerModer_items = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < 2; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("antennaA", powerS[i]);
			powerModer_items.add(item);
		}

		SimpleAdapter powerModer_adapter = new SimpleAdapter(this,
				powerModer_items, android.R.layout.simple_spinner_item,
				new String[] { "antennaA" }, new int[] { android.R.id.text1 });


		powerModer_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		powerModer.setAdapter(powerModer_adapter);

		/* 添加事件Spinner事件监听 */
		powerModer.setOnItemSelectedListener(new SpinnerSelectedListener());
		powerModer.setVisibility(View.VISIBLE);


	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}
