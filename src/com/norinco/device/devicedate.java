package com.norinco.device;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class devicedate extends SQLiteOpenHelper{
	private final static int version=1;
	private final static String dbname="Device.db";
	public devicedate(Context context){
		super(context, dbname, null, version);     
	}       
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE login (_id INTEGER PRIMARY KEY AUTOINCREMENT,username text,password text,level int);");
		db.execSQL("CREATE TABLE device (_id INTEGER PRIMARY KEY AUTOINCREMENT,factory text,devname text,devgroup int,number int,productdate text,recvdate text,modifydate text);");
		db.execSQL("CREATE TABLE devicemodify (_id INTEGER PRIMARY KEY AUTOINCREMENT,number int,sendname text,recvname text,reptimes int,replevel int,faultCause text,consume text,finishDate text);");
		db.execSQL("INSERT INTO login(username,password,level) VALUES('admin','admin',3)");
	}  
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		String sql = "drop table if exists device";		
		db.execSQL(sql);
		db.execSQL("drop table if exists login");
		db.execSQL("drop table if exists devicemodify");
		this.onCreate(db);
		System.out.println("--->>>   onUpgrade");
	}
	public void onOpen(SQLiteDatabase db) {     
	         super.onOpen(db);
	}
}
