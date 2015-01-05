package com.cngc.hht;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartActivity extends Activity {
	private RelativeLayout layout;
	boolean starting = false;

	private DataBase dbinit;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		LoginStatu loginstatu = (LoginStatu) getApplicationContext();
		starting = loginstatu.getstarting();

		dbinit = new DataBase();	
		if(dbinit.DatabaseInit(getAssets())==0)
			System.out.println("初始化失败");

		if (!starting) {
			TimerTask task = new TimerTask() {
				public void run() {
					JumpActivity();
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, 3000);
		} else {
			layout = (RelativeLayout) findViewById(R.id.top_layout);
			layout.setVisibility(View.VISIBLE);

			TextView text = (TextView) findViewById(R.id.main_text);
			text.setVisibility(View.INVISIBLE);
		}
	}

	private void JumpActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void about_back(View v) {
		finish();
	}
}
