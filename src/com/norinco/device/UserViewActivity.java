package com.norinco.device;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserViewActivity extends Activity{
	private TextView username;
	private TextView level;
	private TextView password;
	private Button but_return;
	private Button but_update;
	private int l=0;
	private String user;
	Intent intent = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userview);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");

		HashMap map = (HashMap) intent.getSerializableExtra("login");
		username = (TextView) findViewById(R.id.tv_name);
		level = (TextView) findViewById(R.id.tv_mlevel);
		password = (TextView) findViewById(R.id.tv_group);
		
		username.setText(map.get("username")+"");
		level.setText(map.get("level")+"");
		password.setText(map.get("password")+"");
		
		but_return = (Button) findViewById(R.id.but_return);		
		but_return.setOnClickListener(listener);
		
		but_update = (Button) findViewById(R.id.but_update);
		but_update.setOnClickListener(listener);
		
	}	

	private OnClickListener listener = new OnClickListener() {
		
		public void onClick(View v) {

			Button but = (Button) v;
			int id = but.getId();
			if (id == R.id.but_update) {
				intent = getIntent();
				intent.setClass(UserViewActivity.this, UserUpdateActivity.class);
				intent.putExtra("level",l);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
			} else if (id == R.id.but_return) {
				intent = new Intent();
				intent.setClass(UserViewActivity.this, UserListActivity.class);
				intent.putExtra("level", l);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
			}
		}
	};
}

