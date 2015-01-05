package com.norinco.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.norinco.eme.MethodDataV2;

public class Upload {

	//private static final String TAG = Upload.class.getSimpleName();

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

//	MethodData methodData;
	MethodDataV2 m2;

	EquipmentInfo equipmentInfo;

	// MyReceiver myReceiver;

	Collection<EquipmentInfo> EquipmentInfoList;
	
	private Context basetext;
	
	static {
		System.loadLibrary("update");
	}

	public native int sendrequst();
	
	public Upload(Context text) {

		basetext = text;
//		methodData = new MethodData(basetext);
		m2 = new MethodDataV2(basetext);

		EquipmentInfoList = new ArrayList<EquipmentInfo>();

		UpdateAdapter();
		
		setXml();
		
		sendrequst();

	}
	

	/* 将XML文件存入到sdcard里 */
	public void saveSdcard(String filename, String content) throws IOException {
		try {
			// ---SD Card Storage---
			File sdCard = Environment.getExternalStorageDirectory();
			// File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
			//File directory = new File(sdCard.getAbsolutePath());
			File directory = new File("/data/data/com.cngc.hht");			
			directory.mkdirs();
			File file = new File(directory, filename);
			OutputStream fOut = new FileOutputStream(file);
			byte[] b = content.getBytes();
			fOut.write(b);
			fOut.close();

			// OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// ---write the string to the file---

			// ---display file saved message---
			Toast.makeText(basetext, "File saved successfully!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void setXml(){
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
		
	}

	private void UpdateAdapter() {
//		Cursor cursor = methodData.all();
		Cursor cursor = m2.all();

		int count = cursor.getCount();

		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			equipmentInfo = new EquipmentInfo();
			PrimaryKey = String.valueOf(cursor.getInt(1));
			EquipID = String.valueOf(cursor.getInt(1));
			Department = cursor.getString(2);
			EquipName = cursor.getString(3);
			ReporterName = cursor.getString(4);
			RepairName = cursor.getString(5);
			ReportTime = cursor.getString(7);
			FixTime = cursor.getString(8);
			RenewedTime = cursor.getString(8);
			ProblemDescrip = cursor.getString(9);
			FixMethod = cursor.getString(10);
			System.out.println("故障现象" + ProblemDescrip);
			
			/*需要写入设备的名称*/

			EquipID += i;
			PrimaryKey += i;

			SetEquipmentInfo(equipmentInfo);
			System.out.println(equipmentInfo.getDepartment());
			if (equipmentInfo != null) {
				EquipmentInfoList.add(equipmentInfo);
			}
			cursor.moveToNext();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void SetEquipmentInfo(EquipmentInfo equipmentInfo) {

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String sysDate = formatter.format(curDate);
//		RenewedTime = sysDate;

//		ReportTime = sysDate;
//
//		FixTime = sysDate;

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
}
