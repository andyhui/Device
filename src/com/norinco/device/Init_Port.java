package com.norinco.device;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class Init_Port extends Activity{
	private Button single=null;
	private Button back=null;
	private Button loop=null;
	
	private Intent intent=null;
	
	static SerialPort sp=null;
	static GetData gd=null;
	
	HashMap<String,String> map;
	
	private int l;
	private int i;
	private int flag=0;
	private String user;
	
	private int hCom;
	
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("-------Init_PortActivity-------");
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.serialport);
		
		intent = getIntent();
		Bundle bundle=intent.getExtras();
		l=bundle.getInt("level");
		user=bundle.getString("user");
		
		single=(Button)findViewById(R.id.button1);
		back=(Button)findViewById(R.id.button2);
		loop=(Button)findViewById(R.id.button3);
		
		map=new HashMap<String,String>();
		
		single.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String PORT = "/dev/s3c2410_serial3";
				
				try {
					sp=new SerialPort(new File(PORT),115200);
				}
				catch (SecurityException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
				hCom=sp.getmFd();
				
				gd=new GetData(hCom,flag);
				i=gd.getI();
				if(i==1){
					sp.close(hCom);
					Toast.makeText(Init_Port.this, "读取RFID数据成功！", Toast.LENGTH_LONG).show();
					
					map.put("devname", gd.getDevname());    
				    map.put("number", String.valueOf(gd.getDevnum()));    
				    map.put("devgroup", String.valueOf(gd.getDevgroup()));    
				    map.put("factory", gd.getFactory());
				    map.put("productdate", gd.getProdate()); 
				    map.put("recvdate", gd.getRecdate()); 
				    map.put("modifydate", gd.getMdfdate());
				  				    
					intent = new Intent(Init_Port.this,DeviceListActivity.class);
					intent.putExtra("level", l);
					intent.putExtra("user", user);
					intent.putExtra("device", map);
					intent.putExtra("flag",1);
					startActivity(intent);
					finish();
				}
				else {
					sp.close(hCom);
					Toast.makeText(Init_Port.this, "读取RFID数据失败！", Toast.LENGTH_LONG).show();
				}					
			}
		});
		
		/*loop.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String PORT = "/dev/s3c2410_serial3";
				
				try {
					sp=new SerialPort(new File(PORT),115200);
				}
				catch (SecurityException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
				hCom=sp.getmFd();
				
				gd=new GetData(hCom,flag);
				i=gd.getI();
				if(i==1){
					sp.close(hCom);
					Toast.makeText(Init_Port.this, "读取RFID数据成功！", Toast.LENGTH_LONG).show();
					
					map.put("devname", gd.getDevname());    
				    map.put("number", String.valueOf(gd.getDevnum()));    
				    map.put("devgroup", String.valueOf(gd.getDevgroup()));    
				    map.put("factory", gd.getFactory());
				    map.put("productdate", "2013.04.23"); 
				    map.put("recvdate", "2013.08.03"); 
				    map.put("modifydate", "2013.12.05");
				    
					intent = new Intent(Init_Port.this,DeviceListActivity.class);
					intent.putExtra("level", l);
					intent.putExtra("user", user);
					intent.putExtra("device", map);
					startActivity(intent);
					finish();
				}
				else {
					sp.close(hCom);
					Toast.makeText(Init_Port.this, "写入RFID数据失败！", Toast.LENGTH_LONG).show();
				}					
			}
		});*/
		
		back.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				intent = new Intent(Init_Port.this,MessageListActivity.class);
				intent.putExtra("level",l);
				intent.putExtra("user", user);
				startActivity(intent);
				finish();
			}
		});		
	}
}
