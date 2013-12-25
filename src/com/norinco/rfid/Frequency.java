package com.norinco.rfid;

import com.norinco.device.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class Frequency extends Activity {

	EditText chanlCount = null;
	EditText chanlValue = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frequency);

		chanlCount = (EditText) findViewById(R.id.ChannelCount);
		chanlValue = (EditText) findViewById(R.id.ChannelVuale);

		// 频道数
		CharSequence chnalC = chanlCount.getText().toString();
		// 每个频道频道值
		CharSequence chnalV = chanlValue.getText().toString();
	}

}
