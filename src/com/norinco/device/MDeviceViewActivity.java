package com.norinco.device;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MDeviceViewActivity extends Activity{
	private Button but_return;
	private Button but_update;
	
	private HashMap map;
	
	private int l;
	private int m;
	private String user;
	
	DeviceDao personDao;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mdeviceview);
		Intent intent = getIntent();
		
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		m=bundle.getInt("flag");
		user=bundle.getString("user");
		
		
		map = (HashMap) intent.getSerializableExtra("devicemodify");
		
		((TextView) findViewById(R.id.tv_mdevnum)).setText(map.get("devnum")+"");
		((TextView) findViewById(R.id.tv_msperson)).setText(map.get("sperson")+"");
		((TextView) findViewById(R.id.tv_mrperson)).setText(map.get("rperson")+"");
		((TextView) findViewById(R.id.tv_mtimes)).setText(map.get("mtimes")+"");
		((TextView) findViewById(R.id.tv_mlevel)).setText(map.get("mlevel")+"");
		((TextView) findViewById(R.id.tv_consume)).setText(map.get("consume")+"");
		((TextView) findViewById(R.id.tv_finishdate)).setText(map.get("finishdate")+"");
		((TextView) findViewById(R.id.tv_faultcause)).setText(map.get("faulecause")+"");
		
		but_return = (Button) findViewById(R.id.mdbut_return);		
		but_return.setOnClickListener(listener);
		
		but_update = (Button) findViewById(R.id.mdbut_update);
		but_update.setOnClickListener(listener);
		
		
		
	}

	private OnClickListener listener = new OnClickListener() {
		
		public void onClick(View v) {

			Button but = (Button) v;
			int id = but.getId();
			if (id == R.id.mdbut_update) {
				Intent updateintent = new Intent();
				updateintent.setClass(MDeviceViewActivity.this, MDeviceUpdateActivity.class);
				updateintent.putExtra("mdeviceview", map);
				updateintent.putExtra("level", l);
				updateintent.putExtra("user", user);
				startActivity(updateintent);
				finish();
			} else if (id == R.id.mdbut_return) {
				Intent listintent = new Intent();
				if(m!=0){
					listintent.setClass(MDeviceViewActivity.this, MDeviceListActivity.class);
					listintent.putExtra("level", l);
					listintent.putExtra("user", user);
					startActivity(listintent);
				}
				else
					finish();
			}
		}
	};
}
