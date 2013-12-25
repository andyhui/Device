package com.norinco.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserAddActivity extends Activity{
	private EditText et_name;
	private EditText et_alevel;
	private EditText et_aps;
	private Button but_save;
	private Button but_return;
	
	private int l;
	private String user;
	
	private User person;
	private DeviceDao personDao;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");
		personDao = new DeviceDao(this);
		initWidget();
	}
	
	private void initWidget(){
		et_name = (EditText) findViewById(R.id.et_name);
		et_alevel = (EditText) findViewById(R.id.et_alevel);
		et_aps = (EditText) findViewById(R.id.et_aps);
		
		but_save = (Button) findViewById(R.id.but_save);
		but_save.setOnClickListener(listener);
		but_return = (Button) findViewById(R.id.but_return);
		but_return.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			Button but = (Button) v;
			int id = but.getId();
			if (id == R.id.but_save) {
				person = new User();
				String name = et_name.getText().toString();
				String password = et_aps.getText().toString();
				String level = et_alevel.getText().toString();
				if ("".equals(name)) {
					Toast.makeText(UserAddActivity.this, "请输入姓名", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(password)) {
					Toast.makeText(UserAddActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				if ("".equals(level)) {
					Toast.makeText(UserAddActivity.this, "请输入权限", Toast.LENGTH_LONG).show();
					return;
				}
				person.setName(name);
				person.setLevel(Integer.parseInt(level));
				person.setPassword(password);
				personDao.save(person);
				openActivity(v);
			} else if (id == R.id.but_return) {
				openActivity(v);
			}
		}
	};
	public void openActivity(View v){
		Intent intent = new Intent(this,UserListActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user", user);
		startActivity(intent);
		finish();
	}
}
