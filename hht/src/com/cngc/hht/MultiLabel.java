package com.cngc.hht;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class MultiLabel extends Activity {

	private FrameLayout scan_layout;
	private RelativeLayout top;
	private ImageView scan;
	private ListView scan_menu;

	private ArrayList<Map<String, String>> mData;

	// private DataBase db;
	private CycleView CycleScan;
	private RfidPower rfpower;

	private MyHandler mHandler;

	private boolean stopflag = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multilabel);

		// db = new DataBase();
		CycleScan = new CycleView();
		rfpower = new RfidPower();

		scan_layout = (FrameLayout) findViewById(R.id.scan_button_layout);
		top = (RelativeLayout) findViewById(R.id.multi_top);

		scan_menu = (ListView) findViewById(R.id.scan_menu);
		mData = new ArrayList<Map<String, String>>();

		scan = (ImageView) findViewById(R.id.scan_button);
		scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				stopflag = false;
				scan_layout.setVisibility(8);
				top.setVisibility(0);
			}
		});

		mHandler = new MyHandler(this);

		new Thread(new Runnable() {
			//Message message = new Message();
			//Bundle bundle = new Bundle();

			public void run() {
				rfpower.openserial();
				//while (stopflag) {
					/*if (rfpower.readlabel(0) == 0) {
						CycleScan.saveEquipInfo(rfpower.getDevice());
						bundle.putString("name", rfpower.getDevice()
								.getEquipmentName());
						bundle.putString("number", String.valueOf(rfpower
								.getDevice().getEquipAssetNo()));
						message.setData(bundle);
						message.what = 1;
						mHandler.sendMessage(message);
					}*/
				//}
				rfpower.closeserial();
			}
		}).start();

	}
	
	

	static class MyHandler extends Handler {
		WeakReference<MultiLabel> mActivity;

		MyHandler(MultiLabel activity) {
			mActivity = new WeakReference<MultiLabel>(activity);
		}

		public void handleMessage(Message msg) {
			MultiLabel theActivity = mActivity.get();
			switch (msg.what) {
			case 1:
				String devicename = msg.getData().getString("name");
				String devicenumber = msg.getData().getString("number");
				theActivity.scan_result(devicename, devicenumber);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	public void scan_result(String devname, String devnum) {
		Map<String, String> item = new HashMap<String, String>();
		item.put("devname", devname);
		item.put("devnum", devnum);
		mData.add(item);

		SimpleAdapter adapter = new SimpleAdapter(this, mData,
				R.layout.multilabel_value,
				new String[] { "devname", "devnum" }, new int[] { R.id.devname,
						R.id.devnum });
		scan_menu.setAdapter(adapter);
	}

	public void onBackPressed() {
		finish();
	}

	public void about_back(View v) {
		finish();
	}
}
