package com.norinco.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.norinco.eme.Eme_fault;
import com.norinco.eme.history_fault;
import com.norinco.eme.history_method;

public class MDeviceAddActivity extends Activity {

	private static int miCount = 0;

	private int on = R.drawable.arrow_right;

	List<HashMap<String, Object>> mylist;

	SimpleAdapter adapter;

	ListView myListView = null;

	private EditText et_sperson;
	private EditText et_devnum;
	private EditText et_rperson;
	private EditText et_mtimes;
	private EditText et_mlevel;
	private EditText et_consume;
	private EditText et_finishdate;
	/* private EditText et_faultcause; */
	private EditText et_fixedmothod;/* 故障解决方法 */

	private Button but_save;
	private Button but_return;

	private int l;
	private String user;

	private Mdevice person;
	private DeviceDao personDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("%%%%%%%%%-1-%%%%%%%%%%%%");
		setContentView(R.layout.mdevieceadd);
		System.out.println("%%%%%%%%%-2-%%%%%%%%%%%%");
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		l = bundle.getInt("level");
		user = bundle.getString("user");

		/*以下是显示修复问题的list
		  @liuhuiwu */
		myListView = (ListView) findViewById(R.id.myListView);
		mylist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();

		map1.put("text", this.getString(R.string.history_fault));
		map1.put("img", on);
		map2.put("text", this.getString(R.string.update_fault));
		map2.put("img", on);
		map3.put("text", this.getString(R.string.history_sloved));
		map3.put("img", on);

		mylist.add(map1);
		mylist.add(map2);
		mylist.add(map3);

		adapter = new SimpleAdapter(this, mylist, R.layout.device_fault_list,
				new String[] { "text", "img" }, new int[] { R.id.text_fault,
						R.id.img_fault });
		myListView.setAdapter(adapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Map<String, Object> map = mylist.get(position);
				switch (position) {
				case 0:
					Intent intent1 = new Intent();
					intent1.putExtra("level", l);
					intent1.putExtra("user", user);
					intent1.setClass(MDeviceAddActivity.this,
							history_fault.class);
					startActivity(intent1);
					break;
				case 1:
					Intent intent2 = new Intent();
					intent2.putExtra("level", l);
					intent2.putExtra("user", user);
					intent2.setClass(MDeviceAddActivity.this, Eme_fault.class);
					startActivity(intent2);
					break;
				case 2:
					Intent intent3 = new Intent();
					// intent3.setClass(MDeviceAddActivity.this,
					// history_solved.class);
					intent3.putExtra("level", l);
					intent3.putExtra("user", user);
					intent3.setClass(MDeviceAddActivity.this,
							history_method.class);
					startActivity(intent3);
					break;
				/*
				 * case 3: Intent intent4 = new Intent();
				 * intent4.setClass(MDeviceAddActivity.this,
				 * update_solved.class); startActivity(intent4); break;
				 */
				default:
					break;
				}

			}

		});
		/*以上是显示修复问题的list
		  @liuhuiwu */
		
		System.out.println("%%%%%%%%%-3-%%%%%%%%%%%%");
		personDao = new DeviceDao(this);
		System.out.println("%%%%%%%%%-4-%%%%%%%%%%%%");
		initWidget();
		System.out.println("%%%%%%%%%-5-%%%%%%%%%%%%");
	}

	private void initWidget() {
		et_sperson = (EditText) findViewById(R.id.et_tv_asperson);
		System.out.println("%%%%%%%%%-41-%%%%%%%%%%%%");
		et_devnum = (EditText) findViewById(R.id.et_tv_madevnum);
		System.out.println("%%%%%%%%%-42-%%%%%%%%%%%%");
		et_rperson = (EditText) findViewById(R.id.et_tv_arperson);
		System.out.println("%%%%%%%%%-43-%%%%%%%%%%%%");
		et_mtimes = (EditText) findViewById(R.id.et_tv_amtimes);
		System.out.println("%%%%%%%%%-44-%%%%%%%%%%%%");
		et_mlevel = (EditText) findViewById(R.id.et_tv_amlevel);
		System.out.println("%%%%%%%%%-45-%%%%%%%%%%%%");
		et_consume = (EditText) findViewById(R.id.et_tv_aconsume);
		System.out.println("%%%%%%%%%-46-%%%%%%%%%%%%");
		et_finishdate = (EditText) findViewById(R.id.et_tv_afinishdate);
		System.out.println("%%%%%%%%%-47-%%%%%%%%%%%%");

		but_save = (Button) findViewById(R.id.admbut_save);
		System.out.println("%%%%%%%%%-48-%%%%%%%%%%%%");
		but_save.setOnClickListener(listener);
		System.out.println("%%%%%%%%%-49-%%%%%%%%%%%%");
		but_return = (Button) findViewById(R.id.admbut_list);
		System.out.println("%%%%%%%%%-4a-%%%%%%%%%%%%");
		but_return.setOnClickListener(listener);
		System.out.println("%%%%%%%%%-4b-%%%%%%%%%%%%");
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button but = (Button) v;
			switch (but.getId()) {
			case R.id.admbut_save:
				person = new Mdevice();
				String sperson = et_sperson.getText().toString();
				String devnum = et_devnum.getText().toString();
				String rperson = et_rperson.getText().toString();
				String mtimes = et_mtimes.getText().toString();
				String mlevel = et_mlevel.getText().toString();
				String consume = et_consume.getText().toString();
				String finishdate = et_finishdate.getText().toString();
				/* String faultcause = et_faultcause.getText().toString(); */

				String fixedmethod = et_fixedmothod.getText().toString();/* 获取填写的故障原因 */

				if ("".equals(sperson)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入送装人",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(devnum)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入设备号",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(rperson)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入接装人",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(mtimes)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入维修次数",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(mlevel)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入维修等级",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(consume)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入消耗的器材",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(finishdate)) {
					Toast.makeText(MDeviceAddActivity.this, "请输入修竣时间",
							Toast.LENGTH_LONG).show();
					return;
				}
				/*
				 * if ("".equals(faultcause)) {
				 * Toast.makeText(MDeviceAddActivity.this, "请输入故障原因",
				 * Toast.LENGTH_LONG).show(); return; }
				 */
				person.setSperson(sperson);
				person.setDevnum(Integer.parseInt(devnum));
				person.setRperson(rperson);
				person.setMtimes(Integer.parseInt(mtimes));
				person.setMlevel(Integer.parseInt(mlevel));
				person.setConsume(consume);
				person.setFinishdate(finishdate);
				/* person.setFaultcause(faultcause); */

				personDao.saveM(person);
				openActivity(v);
				break;

			case R.id.admbut_list:
				openActivity(v);
				break;
			}
		}
	};

	public void openActivity(View v) {
		Intent intent = new Intent(this, MDeviceListActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}
}