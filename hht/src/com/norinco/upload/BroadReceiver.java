package com.norinco.upload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class BroadReceiver extends BroadcastReceiver {

	private static final String TAG = "BroadReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		// map = new HashMap<String, String>();
		// map = (HashMap)intent.getSerializableExtra("device");
		// Toast.makeText(context, String.valueOf(map.get("devname")),
		// Toast.LENGTH_LONG).show();
		String s = bundle.getString("device");
		Log.v(TAG, s);
	}
}
