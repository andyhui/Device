package com.cngc.hht;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.AssetManager;

public class DataBase {
	private List<Map<String, String>> List = new ArrayList<Map<String, String>>();

	private void setFaultInfo(String faultRepairTime, String faultDescription,
			String faultSolution) {
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("faultRepairTime", faultRepairTime);
		map.put("faultDescription", faultDescription);
		map.put("faultSolution", faultSolution);

		List.add(map);
	}
	
	public List<Map<String, String>> getList(){
		return this.List;
	}

	public native int DatabaseInit(AssetManager assetManager);

	public native int isUser(String name, String passwd);

	public native String getFactory(int factoryId);

	public native String getDeviceName(int NameId);

	public native int getModifyInfo(int devNum);

	static {
		System.loadLibrary("database");
	}
}
