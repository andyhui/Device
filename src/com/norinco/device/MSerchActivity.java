package com.norinco.device;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MSerchActivity extends Activity {
	private boolean i=true; 
	private Intent intent;
	private int l;
	private String user;
	private String[] szOut;
	HashMap<String,String> map;
	
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("-------MSerchActivity-------");
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.messageserch);
		System.out.println("-------MSerchActivity1-------");
		intent = getIntent();
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");
		
		map=new HashMap<String,String>();
		szOut=new String[7];
		System.out.println("-------MSerchActivity2-------");
		//i=SrGetSingleLabel(hCom, iPw, shPc, szEpc, iszLen, cMem, shSa, shDl, szOut, &dwErr);
		if(i){
			//sp.close();
			Toast.makeText(this, "读取RFID数据成功！", Toast.LENGTH_LONG).show();
		}
		else {
			//sp.close();
			Toast.makeText(this, "读取RFID数据失败！", Toast.LENGTH_LONG).show();
		}		
		
		map.put("devname", szOut[0]);    
	    map.put("number", szOut[1]);    
	    map.put("devgroup", szOut[2]);    
	    map.put("factory", szOut[3]);
	    map.put("productdate", szOut[4]); 
	    map.put("recvdate", szOut[5]); 
	    map.put("modifydate", szOut[6]);
	        
	    intent = new Intent(this,DeviceViewActivity.class);
		intent.putExtra("level", l);
		intent.putExtra("user", user);
		intent.putExtra("device", map);
		startActivity(intent);
		finish();
	}
}
