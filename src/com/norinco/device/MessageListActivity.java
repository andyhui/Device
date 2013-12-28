package com.norinco.device;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.norinco.rfid.RfidParamaSet;
import com.norinco.upload.Upload;
import com.norinco.eme.Eme_fault;
import com.norinco.eme.TestActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageListActivity extends Activity {
	// private Button userin = null;
	// private ImageButton devin = null;
	// private ImageButton modifyin = null;
	// private Button logout = null;
	// private ImageButton checkout = null;
	// private ImageButton writein = null;
	// private ImageButton eme = null;
	private TextView textview = null;
	private Intent intent = null;
	private int l;
	private String user;

	private GridView gv;

	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("-------MessageListActivity-------");
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.maininterface);
		setContentView(R.layout.testinterface);
		// System.out.println("-------MessageListActivity1-------");
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		l = bundle.getInt("level");
		user = bundle.getString("user");

		// System.out.println("-------MessageListActivity2-------");
		textview = (TextView) findViewById(R.id.textView2);

		// userin = (Button) findViewById(R.id.userinformation);
		// devin = (ImageButton) findViewById(R.id.devinformation);
		// modifyin = (ImageButton) findViewById(R.id.modifyinformation);
		// // logout = (Button) findViewById(R.id.button1);
		// checkout = (ImageButton) findViewById(R.id.button2);
		// writein = (ImageButton) findViewById(R.id.button3);
		// eme = (ImageButton) findViewById(R.id.button4);
		// System.out.println("-------MessageListActivity3-------");
		textview.setText(user);
		System.out.println("-------MessageListActivity4-------");
		int[] icon = { R.drawable.spotify, R.drawable.equalizer,
				R.drawable.dropbox, R.drawable.magnifying_glass_add,
				R.drawable.notepad_ok, R.drawable.browser_delete };
		String[] title = { "数据录入", "标签扫描", "检索", "设置", "上传", "退出" };

		// 准备要添加的数据条目
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("imageItem", icon[i]);// 添加图像资源的ID
			item.put("textItem", title[i]);// 按序号添加ItemText
			items.add(item);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.grid_item, new String[] { "imageItem", "textItem" },
				new int[] { R.id.image_item, R.id.text_item });

		// 获得GridView实例
		gv = (GridView) findViewById(R.id.mygridview);
		// 为GridView设置适配器
		gv.setAdapter(adapter);

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Map<String, Object> map = mylist.get(position);
				switch (position) {
				case 0:
					// System.out.println("====================" + l +
					// "==========================");
					if (l >= 2) {
						/* 跳转到录入信息 */
						intent = new Intent(MessageListActivity.this,
								DeviceAddActivity.class);
						intent.putExtra("level", l);
						intent.putExtra("user", user);
						intent.putExtra("action", 'w');
						startActivity(intent);
						finish();
						/*
						 * intent = new Intent(MessageListActivity.this,
						 * DeviceListActivity.class); intent.putExtra("level",
						 * l); intent.putExtra("user", user);
						 * startActivity(intent); finish();
						 */
					}
					break;
				case 1:
					if (l >= 2) {
						/* 跳转到扫描 (多扫：DeviceListActivity;单扫：DeviceViewActivity) */
						Init_port();
					}
					break;
				case 2:
					/* 跳转到检索故障修复方法 */
					if (l >= 1) {
						// intent = new Intent(MessageListActivity.this,
						// DeviceViewActivity.class);
						// intent.putExtra("level", l);
						// intent.putExtra("user", user);
						// startActivity(intent);
					}
					break;
				case 3:
					if (l >= 1) {
						/* 跳转到设置参数 */
						intent = new Intent(MessageListActivity.this,
								RfidParamaSet.class);
						intent.putExtra("level", l);
						intent.putExtra("user", user);
						intent.putExtra("action", 'r');
						startActivity(intent);
						// finish();
					}
					break;
				case 4:
					/* 上传维修信息与故障修复办法 */
					if (l >= 1) {
						intent = new Intent(MessageListActivity.this,
								Upload.class);
//						intent.putExtra("level", l);
//						intent.putExtra("user", user);
						startActivity(intent);
					}
					break;
				case 5:
					/* 退出 */
					intent = new Intent(MessageListActivity.this,
							MainActivity.class);
					startActivity(intent);
				default:
					break;
				}

			}

		});
		// System.out.println("-------MessageListActivity5-------");

	}

	public void Init_port() {
		SerialPort sp = null;
		GetData gd = null;

		HashMap<String, String> map;

		int i;
		int flag = 0;

		int hCom;

		String PORT = "/dev/s3c2410_serial3";

		map = new HashMap();

		try {
			sp = new SerialPort(new File(PORT), 115200);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		hCom = sp.getmFd();

		gd = new GetData(hCom, flag);
		i = gd.getI();
		if (i == 1) {
			sp.close(hCom);
			Toast.makeText(MessageListActivity.this, "读取RFID数据成功！",
					Toast.LENGTH_LONG).show();

			map.put("devname", gd.getDevname());
			map.put("number", String.valueOf(gd.getDevnum()));
			map.put("devgroup", String.valueOf(gd.getDevgroup()));
			map.put("factory", gd.getFactory());
			map.put("productdate", gd.getProdate());
			map.put("recvdate", gd.getRecdate());
			map.put("modifydate", gd.getMdfdate());

			intent = new Intent(MessageListActivity.this,
					DeviceViewActivity.class);
			intent.putExtra("level", l);
			intent.putExtra("user", user);
			intent.putExtra("device", map);
			intent.putExtra("flag", 1);
			startActivity(intent);
			finish();
		} else {
			sp.close(hCom);
			Toast.makeText(MessageListActivity.this, "读取RFID数据失败！",
					Toast.LENGTH_LONG).show();
		}
	}
}