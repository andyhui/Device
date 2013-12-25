package com.norinco.rfid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norinco.device.R;
import com.norinco.rfid.RfidParamaSet.SpinnerSelectedListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class gpioMask extends Activity {

	String[] gpioMaskS = { "高", "低" };
	// Spinner gpioMaskA = null;
	// Spinner gpioMaskB = null;
	// Spinner gpioMaskC = null;
	// Spinner gpioMaskD = null;

	Spinner spinner = null;

	// int []arraySpinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpiomask);

		// gpioMaskA = (Spinner) findViewById(R.id.antennaA);
		// gpioMaskB = (Spinner) findViewById(R.id.antennaB);
		// gpioMaskC = (Spinner) findViewById(R.id.antennaC);
		// gpioMaskD = (Spinner) findViewById(R.id.antennaD);

		int[] arraySpinner = { R.id.gpiomaskA, R.id.gpiomaskB, R.id.gpiomaskC,
				R.id.gpiomaskD, R.id.gpiomaskE, R.id.gpiomaskF, R.id.gpiomaskG,
				R.id.gpiomaskH };

		for (int i = 0; i < 8; i++) {
			myspinner(arraySpinner[i]);
		}

		// List<Map<String, Object>> antennaA_items = new ArrayList<Map<String,
		// Object>>();
		// List<Map<String, Object>> antennaB_items = new ArrayList<Map<String,
		// Object>>();
		// List<Map<String, Object>> antennaC_items = new ArrayList<Map<String,
		// Object>>();
		// List<Map<String, Object>> antennaD_items = new ArrayList<Map<String,
		// Object>>();
		// for (int i = 0; i < 2; i++) {
		// Map<String, Object> item = new HashMap<String, Object>();
		// item.put("antennaA", antennaS[i]);
		// antennaA_items.add(item);
		// antennaB_items.add(item);
		// antennaC_items.add(item);
		// antennaD_items.add(item);
		// }
		//
		// SimpleAdapter antennaA_adapter = new SimpleAdapter(this,
		// antennaA_items, android.R.layout.simple_spinner_item,
		// new String[] { "antennaA" }, new int[] { android.R.id.text1 });
		//
		// SimpleAdapter antennaB_adapter = new SimpleAdapter(this,
		// antennaA_items, android.R.layout.simple_spinner_item,
		// new String[] { "antennaA" }, new int[] { android.R.id.text1 });
		//
		// SimpleAdapter antennaC_adapter = new SimpleAdapter(this,
		// antennaA_items, android.R.layout.simple_spinner_item,
		// new String[] { "antennaA" }, new int[] { android.R.id.text1 });
		//
		// SimpleAdapter antennaD_adapter = new SimpleAdapter(this,
		// antennaA_items, android.R.layout.simple_spinner_item,
		// new String[] { "antennaA" }, new int[] { android.R.id.text1 });
		//
		// antennaA_adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// antennaB_adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// antennaC_adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// antennaD_adapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// antennaA.setAdapter(antennaA_adapter);
		// antennaB.setAdapter(antennaB_adapter);
		// antennaC.setAdapter(antennaC_adapter);
		// antennaD.setAdapter(antennaD_adapter);
		//
		// /* 添加事件Spinner事件监听 */
		// antennaA.setOnItemSelectedListener(new SpinnerSelectedListener());
		// antennaA.setVisibility(View.VISIBLE);
		//
		// antennaB.setOnItemSelectedListener(new SpinnerSelectedListener());
		// antennaB.setVisibility(View.VISIBLE);
		//
		// antennaC.setOnItemSelectedListener(new SpinnerSelectedListener());
		// antennaC.setVisibility(View.VISIBLE);
		//
		// antennaD.setOnItemSelectedListener(new SpinnerSelectedListener());
		// antennaD.setVisibility(View.VISIBLE);

	}

	void myspinner(int spinnerId) {

		spinner = (Spinner) findViewById(spinnerId);
		List<Map<String, Object>> spinner_items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 2; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("gpioMask", gpioMaskS[i]);
			spinner_items.add(item);
		}

		SimpleAdapter spinner_adapter = new SimpleAdapter(this, spinner_items,
				android.R.layout.simple_spinner_item,
				new String[] { "gpioMask" }, new int[] { android.R.id.text1 });

		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(spinner_adapter);

		/* 添加事件Spinner事件监听 */
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}
