package com.norinco.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private int level=3;
	private String user="admin";
 	
	private Button login = null;
	private EditText username = null;
	private EditText password = null;
	private Intent intent = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("-------MainActivity-------");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		login = (Button) findViewById(R.id.login);		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
				
		login.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
					/*String name = username.getText().toString();
					user=name;
					String pwd = password.getText().toString();
					DeviceDao Dev=new DeviceDao(getApplicationContext());
					if((level=Dev.findAdmin(name,pwd)) != -1){*/
						openActivity(v,level);
					/*}else{
						Toast.makeText(getApplicationContext(), "帐号密码输入错误", Toast.LENGTH_LONG).show();
					}*/
			}
		});
	}
	
	public void openActivity(View v,int l){
		intent = new Intent(this,MessageListActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user",user);
		startActivity(intent);
		finish();
	}	
}

