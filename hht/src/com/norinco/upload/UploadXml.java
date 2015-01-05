package com.norinco.upload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cngc.hht.R;
import com.norinco.eme.MethodData;

public class UploadXml extends Activity {

//	static {
//		System.loadLibrary("update");
//	}

//	private native int update_all(String db_name); // sucess:0

	//MyApp myApp;

	private String srcPath = "/data/data/com.cngc.hht/files";
	private String serverUrl = "";

	private static final String TAG = "Upload";

	int num = 3;
	String OperateTable = "equip_record";
	String PrimaryKey = "45944";
	String OperateObject = "all";
	String OperateType = "add";
	String RenewedTime = null;
	String OriginalData = "tttt";
	String EquipID = "45944";
	String EquipName = "指控显示终端";
	String Department = "北方信息控制集团";
	String ReporterName = "舒克";
	String RepairName = "贝塔";
	int RepairNum = 3;
	String ProblemDescrip = "显示屏花屏";
	String FixMethod = "返厂维修";
	String MaterialConsume = "更换显示屏1块";
	String ReportTime = null;
	String FixTime = null;
	String Others = " ";

	Button upload = null;
	Button saveWithXml = null;

	ListView listV = null;

	MethodData methodData;

	EquipmentInfo equipmentInfo;

	// MyReceiver myReceiver;

	Collection<EquipmentInfo> EquipmentInfoList;

	private HashMap<String, String> map;
	private static final String INTENAL_ACTION = "com.norinco.upload.Upload.MyReceiver";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadtoserver);

		methodData = new MethodData(this);

		upload = (Button) findViewById(R.id.upload);
		saveWithXml = (Button) findViewById(R.id.show);
		listV = (ListView) findViewById(R.id.equipmentlist);

		//myApp = (MyApp) getApplication();

		EquipmentInfoList = new ArrayList<EquipmentInfo>();

		UpdateAdapter();

		upload.setOnClickListener(bListener);
		saveWithXml.setOnClickListener(bListener);

	}

	/* 将XML文件存入到sdcard里 */
	public void saveSdcard(String name, String content) throws IOException {
		try {
			// ---SD Card Storage---
			File sdCard = Environment.getExternalStorageDirectory();
			// File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
			File directory = new File(sdCard.getAbsolutePath());
			directory.mkdirs();
			File file = new File(directory, "XmlSerializeEme.xml");
			FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * FileOutputStream fOut = openFileOutput("textfile.txt",
			 * MODE_WORLD_READABLE);
			 */

			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// ---write the string to the file---
			osw.write(content);
			osw.flush();
			osw.close();

			// ---display file saved message---
			Toast.makeText(getBaseContext(), "File saved successfully!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	View.OnClickListener bListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == saveWithXml) {
				SerializeEmeHandler serializeEmeHandler = new SerializeEmeHandler();
				String serializeEmeXml = serializeEmeHandler
						.writeXml((ArrayList<EquipmentInfo>) EquipmentInfoList);
				try {
					saveSdcard("XmlSerializeEme.xml", serializeEmeXml);
					System.out.println("保存成功！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Log.v("XmlSerializeEme", serializeEmeXml);
				OutputStream outStream;
				try {
					outStream = openFileOutput("XmlSerializeEme.xml",
							MODE_PRIVATE);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
							outStream);
					outputStreamWriter.write(serializeEmeXml);
					outputStreamWriter.close();
					outStream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (v == upload) {

				// uploadFile(serverUrl);

				// Socket socket = null;
				// try {
				// socket = new Socket("127.0.0.1", 54321);
				// // 向服务器发送消息
				// PrintWriter out = new PrintWriter(new BufferedWriter(
				// new OutputStreamWriter(socket.getOutputStream())),
				// true);
				// // out.println();
				//
				// // 从服务器接收消息
				// BufferedReader br = new BufferedReader(
				// new InputStreamReader(socket.getInputStream()));
				// String msg = br.readLine();
				// // 处理msg
				//
				// out.close();
				// br.close();
				// socket.close();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }

			}

		}

	};

	/* 上传到服务器的函数 */
	private void uploadFile(String serverUrl) {

		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(serverUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setUseCaches(false);

			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Chareset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream out = new DataOutputStream(
					httpURLConnection.getOutputStream());
			out.writeBytes(twoHyphens + boundary + end);
			out.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			out.writeBytes(end);

			FileInputStream fls = new FileInputStream(srcPath);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = fls.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			fls.close();
			out.writeBytes(end);
			out.writeBytes(twoHyphens + boundary + twoHyphens + end);
			out.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String res = br.readLine();
			Toast.makeText(this, res, Toast.LENGTH_LONG).show();

			out.close();
			is.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void UpdateAdapter() {
		Cursor cursor = methodData.all(this);
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter CursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor,
				new String[] { MethodData.SOLUTION },
				new int[] { android.R.id.text1 });

		int count = cursor.getCount();

		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			equipmentInfo = new EquipmentInfo();
			ProblemDescrip = cursor.getString(2);
			System.out.println("故障现象" + ProblemDescrip);

			//map = myApp.gethMap();
			EquipName = map.get("devname");
			Log.v(TAG, EquipName);
			PrimaryKey = map.get("number");

			PrimaryKey += i;

			SetEquipmentInfo(equipmentInfo);
			System.out.println(equipmentInfo.getDepartment());
			if (equipmentInfo != null) {
				EquipmentInfoList.add(equipmentInfo);
			}
			cursor.moveToNext();
		}

		// EquipmentInfo equipmentInfo = new EquipmentInfo();

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

	private void SetEquipmentInfo(EquipmentInfo equipmentInfo) {

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String sysDate = formatter.format(curDate);
		RenewedTime = sysDate;

		ReportTime = sysDate;

		FixTime = sysDate;

		// map.put("devname", gd.getDevname());
		// map.put("number", String.valueOf(gd.getDevnum()));
		// map.put("devgroup", String.valueOf(gd.getDevgroup()));
		// map.put("factory", gd.getFactory());
		// map.put("productdate", gd.getProdate());
		// map.put("recvdate", gd.getRecdate());
		// map.put("modifydate", gd.getMdfdate());

		// map = myApp.gethMap();
		// EquipName = map.get("devname");
		// Log.v(TAG, EquipName);
		// EquipID = map.get("number");

		equipmentInfo.setNum(3);
		equipmentInfo.setOperateTable(OperateTable);
		equipmentInfo.setPrimaryKey(PrimaryKey);
		equipmentInfo.setOperateObject(OperateObject);
		equipmentInfo.setOperateType(OperateType);
		equipmentInfo.setRenewedTime(RenewedTime);
		equipmentInfo.setOriginalData(OriginalData);
		equipmentInfo.setEquipID(EquipID);
		equipmentInfo.setEquipName(EquipName);
		equipmentInfo.setDepartment(Department);
		equipmentInfo.setReporterName(ReporterName);
		equipmentInfo.setRepairName(RepairName);
		equipmentInfo.setRepairNum(RepairNum);
		equipmentInfo.setProblemDescrip(ProblemDescrip);
		equipmentInfo.setFixMethod(FixMethod);
		equipmentInfo.setMaterialConsume(MaterialConsume);
		equipmentInfo.setReportTime(ReportTime);
		equipmentInfo.setFixTime(FixTime);
		equipmentInfo.setOthers(null);
	}

	// public class MyReceiver extends BroadcastReceiver {
	//
	// public MyReceiver() {
	// Log.v(TAG, "广播实例化");
	// }
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// // Intent intent2 = getIntent();
	// Bundle bundle = intent.getExtras();
	// // map = new HashMap<String, String>();
	// // map = (HashMap)intent.getSerializableExtra("device");
	// // Toast.makeText(context, String.valueOf(map.get("devname")),
	// // Toast.LENGTH_LONG).show();
	// String s = bundle.getString("device");
	// Log.v(TAG, s);
	//
	// }
	// }

	// private BroadcastReceiver bcrIntenal = new BroadcastReceiver(){
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// String action = intent.getAction();
	// if(action.equals(INTENAL_ACTION)){
	// Log.v(TAG, "receive broadcast!");
	// }else{
	// Log.v(TAG,"receive nothing!");
	// }
	// }
	//
	// };

	// @Override
	// protected void onStart() {
	// // TODO Auto-generated method stub
	// myReceiver = new MyReceiver();
	// IntentFilter intentfilter = new IntentFilter();
	// intentfilter.addAction(INTENAL_ACTION);
	// registerReceiver(myReceiver, intentfilter);
	// super.onStart();
	// }
	//
	// @Override
	// protected void onStop() {
	// // TODO Auto-generated method stub
	// unregisterReceiver(myReceiver);
	// super.onStop();
	// }
}
