package com.norinco.upload;

import com.norinco.device.R;
import com.norinco.eme.MethodData;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Upload extends Activity{
	
	Button upload = null;
	Button show = null;
	
	ListView listV = null;
	
	MethodData methodData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadtoserver);
		
		methodData = new MethodData(this);
		
		upload = (Button)findViewById(R.id.upload);
		show = (Button)findViewById(R.id.show);
		listV = (ListView)findViewById(R.id.equipmentlist);
		
		upload.setOnClickListener(bListener);
		show.setOnClickListener(bListener);
		
		UpdateAdapter();
		
	}
	
	View.OnClickListener bListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	};

	private void UpdateAdapter() {
		Cursor cursor = methodData.all(this);
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter CursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor,
				new String[] { MethodData.SOLUTION },
				new int[] { android.R.id.text1 });
		listV.setAdapter(CursorAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			methodData.close();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}
