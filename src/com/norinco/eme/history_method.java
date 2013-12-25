package com.norinco.eme;

import java.util.HashMap;
import java.util.List;

import com.norinco.device.R;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class history_method extends Activity {

	private EditText e_pro = null;
	private EditText e_method = null;
	private Button b_ok = null;
	private Button b_delete = null;

	List<HashMap<String, Object>> mList = null;
	SimpleAdapter adapter;
	ListView m_ListView = null;

	MethodData methodData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solved);
		m_ListView = (ListView) findViewById(R.id.my_list);
		e_pro = (EditText) findViewById(R.id.pro_edit);
		e_method = (EditText) findViewById(R.id.method_edit);
		b_ok = (Button) findViewById(R.id.ok);
		b_delete = (Button) findViewById(R.id.delete);
		m_ListView.setBackgroundColor(Color.WHITE);

		b_delete.setOnClickListener(buttonListener);
		b_ok.setOnClickListener(buttonListener);

		methodData = new MethodData(this);

		if (methodData.count() == 0) {
			methodData.insert("Earl Grey", "更换灯泡");
			methodData.insert("Assam", "重启系统");
			methodData.insert("Jasmine Green", "关掉其他应用程序");
			methodData.insert("Darjeeling", "重新启动应用程序");
		}

		UpdateAdapter();

	}

	View.OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ok:
				String problem = e_pro.getText().toString();
				String method = e_method.getText().toString();
				methodData.insert(problem, method);
				UpdateAdapter();
				break;
			case R.id.delete:
				break;
			default:
				break;
			}

		}

	};

	private void UpdateAdapter() {
		Cursor cursor = methodData.all(this);
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter CursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor,
				new String[] { MethodData.SOLUTION },
				new int[] { android.R.id.text1 });
		m_ListView.setAdapter(CursorAdapter);
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

	public history_method() {
		// TODO Auto-generated constructor stub
	}

}
