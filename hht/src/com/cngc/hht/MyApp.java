package com.cngc.hht;

import java.util.HashMap;

import android.app.Application;


public class MyApp extends Application {
	private HashMap<String,String> hMap;

	public HashMap<String, String> gethMap() {
		return hMap;
	}

	public void sethMap(HashMap<String, String> hMap) {
		this.hMap = hMap;
	}

}
