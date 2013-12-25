package com.norinco.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DeviceDao {
	private devicedate helper = null;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;
	private int level; 
	public DeviceDao(Context context) {
		helper = new devicedate(context);
	}
	
	public  void insertAdmin(String name,String password){
		db = helper.getWritableDatabase();
		db.execSQL("insert into admin(username,password) values(?,?);",new String[]{name,password});
		Log.i("------>","ok?");
		this.close();
	}
	
	public int findAdmin(String name,String password){
		System.out.println("-------findAdmin-------");
		db = helper.getWritableDatabase();
		cursor = db.rawQuery("select * from login",null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			if(cursor.getString(1).equals(name) && cursor.getString(2).equals(password))
			{
				Log.i("---->", "---");
				level=cursor.getInt(3);
				this.close();
				return level; 
			}
			else
				cursor.moveToNext();
		}
		this.close();
		return -1;
	}
	
	public ArrayList<Map<String, String>> findAll(){
		System.out.println("-------findAll-------");
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from login", null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("username", cursor.getString(cursor.getColumnIndex("username")));
			map.put("password", cursor.getString(cursor.getColumnIndex("password")));
			map.put("level", cursor.getString(cursor.getColumnIndex("level")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public ArrayList<Map<String, String>> findAllD(){
		System.out.println("-------findAllD-------");
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from device", null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("factory", cursor.getString(cursor.getColumnIndex("factory")));
			map.put("devname", cursor.getString(cursor.getColumnIndex("devname")));
			map.put("devgroup", cursor.getString(cursor.getColumnIndex("devgroup")));
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("productdate", cursor.getString(cursor.getColumnIndex("productdate")));
			map.put("recvdate", cursor.getString(cursor.getColumnIndex("recvdate")));
			map.put("modifydate", cursor.getString(cursor.getColumnIndex("modifydate")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public ArrayList<Map<String, String>> findAllM(){
		System.out.println("-------findAllM-------");
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from devicemodify", null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("devnum", cursor.getString(cursor.getColumnIndex("number")));
			map.put("sperson", cursor.getString(cursor.getColumnIndex("sendname")));
			map.put("rperson", cursor.getString(cursor.getColumnIndex("recvname")));
			map.put("mtimes", cursor.getString(cursor.getColumnIndex("reptimes")));
			map.put("mlevel", cursor.getString(cursor.getColumnIndex("replevel")));
			map.put("consume", cursor.getString(cursor.getColumnIndex("consume")));
			map.put("finishdate", cursor.getString(cursor.getColumnIndex("finishDate")));
			map.put("faulecause", cursor.getString(cursor.getColumnIndex("faultCause")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public Map<String, Cursor> find(){
		System.out.println("-------find-------");
		Map<String, Cursor> map = new HashMap<String, Cursor>();
		db = helper.getWritableDatabase();
		cursor = db.rawQuery("select * from login", null);
		map.put("cursor", cursor);
		return map;
	}
	
	public Map<String, Cursor> findD(){
		System.out.println("-------findD-------");
		Map<String, Cursor> map = new HashMap<String, Cursor>();
		db = helper.getWritableDatabase();
		cursor = db.rawQuery("select * from device", null);
		map.put("cursor", cursor);
		return map;
	}
	
	public Map<String, Cursor> findM(){
		System.out.println("-------findM-------");
		Map<String, Cursor> map = new HashMap<String, Cursor>();
		db = helper.getWritableDatabase();
		cursor = db.rawQuery("select * from devicemodify", null);
		map.put("cursor", cursor);
		return map;
	}	
	
	public ArrayList<Map<String, String>> findByName(String name){
		System.out.println("-------findByName-------");
		db = helper.getWritableDatabase();
		
		cursor = db.rawQuery("select * from login where username like '%"+name+"%'",null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("username", cursor.getString(cursor.getColumnIndex("username")));
			map.put("password", cursor.getString(cursor.getColumnIndex("password")));
			map.put("level", cursor.getString(cursor.getColumnIndex("level")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public ArrayList<Map<String, String>> findByIdOrName(String nameorid){
		db = helper.getWritableDatabase();
		
		cursor = db.rawQuery("select * from device where devname like '%"+nameorid+"%' or number like '%"+nameorid+"%'",null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("factory", cursor.getString(cursor.getColumnIndex("factory")));
			map.put("devname", cursor.getString(cursor.getColumnIndex("devname")));
			map.put("devgroup", cursor.getInt(cursor.getColumnIndex("devgroup"))+"");
			map.put("number", cursor.getInt(cursor.getColumnIndex("number"))+"");
			map.put("productdate", cursor.getString(cursor.getColumnIndex("productdate")));
			map.put("recvdate", cursor.getString(cursor.getColumnIndex("recvdate")));
			map.put("modifydate", cursor.getString(cursor.getColumnIndex("modifydate")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public ArrayList<Map<String, String>> findBynum(String num){
		db = helper.getWritableDatabase();
		
		cursor = db.rawQuery("select * from devicemodify where number like '%"+num+"%'",null);
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String,String> map = new HashMap<String,String>();
			
			map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("devnum", cursor.getString(cursor.getColumnIndex("number")));
			map.put("sperson", cursor.getString(cursor.getColumnIndex("sendname")));
			map.put("rperson", cursor.getString(cursor.getColumnIndex("recvname")));
			map.put("mtimes", cursor.getString(cursor.getColumnIndex("reptimes")));
			map.put("mlevel", cursor.getString(cursor.getColumnIndex("replevel")));
			map.put("consume", cursor.getString(cursor.getColumnIndex("consume")));
			map.put("finishdate", cursor.getString(cursor.getColumnIndex("finishDate")));
			map.put("faulecause", cursor.getString(cursor.getColumnIndex("faultCause")));
			
			list.add(map);
		}
		this.close();
		return list;
	}
	
	public void save(User person){
		System.out.println("-------save-------");
		db = helper.getWritableDatabase();
		System.out.println("------- "+db+"  ---"+db==null);
		db.execSQL("insert into login (username,password,level) values(?,?,?)",
				new Object[]{person.getName(),person.getPassword(),person.getLevel()});
		this.close();
	}
	
	public void saveD(Device person){
		System.out.println("-------saveD-------");
		db = helper.getWritableDatabase();
		System.out.println("------- "+db+"  ---"+db==null);
		db.execSQL("insert into device (devname,factory,number,devgroup,productdate,recvdate,modifydate) values(?,?,?,?,?,?,?)",
				new Object[]{person.getDevname(),person.getFactory(),person.getDevnum(),person.getDevgroup(),person.getProdate(),person.getRecdate(),person.getModifydate()});
		this.close();
	}
	
	public void saveM(Mdevice person){
		System.out.println("-------saveM-------");
		db = helper.getWritableDatabase();
		System.out.println("------- "+db+"  ---"+db==null);
		db.execSQL("insert into devicemodify (sendname,recvname,number,reptimes,replevel,consume,faultCause,finishDate) values(?,?,?,?,?,?,?,?)",
				new Object[]{person.getSperson(),person.getRperson(),person.getDevnum(),person.getMtimes(),person.getMlevel(),person.getConsume(),person.getFaultcause(),person.getFinishdate()});
		this.close();
	}
	
	public int update(ContentValues values) {
		System.out.println("-------update-------");
		db = helper.getWritableDatabase();		
		int sun = db.update("login", values, "_id = ?", new String[]{values.get("_id")+""});
		this.close();
		return sun;
	}
	
	public int updateD(ContentValues values) {
		System.out.println("-------updateD-------");
		db = helper.getWritableDatabase();		
		int sun = db.update("device", values, "_id = ?", new String[]{values.get("_id")+""});
		this.close();
		return sun;
	}
	
	public int updateM(ContentValues values) {
		System.out.println("-------updateM-------");
		db = helper.getWritableDatabase();		
		int sun = db.update("devicemodify", values, "_id = ?", new String[]{values.get("_id")+""});
		this.close();
		return sun;
	}
	
	
	public int delete(String id) {
		System.out.println("-------delete-------");
		db = helper.getWritableDatabase();
		System.out.println("------- "+db+"  ---"+db==null);
		System.out.println("�Ƿ��ܻ�ȡid-values.get(_id)= "+id);
		
		int sun = db.delete("login", "_id = ?", new String[]{id+""});
		System.out.println("�Ƿ���³ɹ���xum= "+sun);
		this.close();
		return sun;
	}
	
	public int deleteD(String id) {
		System.out.println("-------deleteD-------");
		db = helper.getWritableDatabase();
		System.out.println("------- "+db+"  ---"+db==null);
		System.out.println("�Ƿ��ܻ�ȡid-values.get(_id)= "+id);
		
		int sun = db.delete("device", "factory = ?", new String[]{id+""});
		System.out.println("�Ƿ���³ɹ���xum= "+sun);
		this.close();
		return sun;
	}
	
	public int deleteM(String id) {
		System.out.println("-------deleteM-------");
		db = helper.getWritableDatabase();		
		int sun = db.delete("devicemodify", "number = ?", new String[]{id+""});
		System.out.println("�Ƿ���³ɹ���xum= "+sun);
		this.close();
		return sun;
	}
	
	public void clearTable(){
		System.out.println("-------clearTable-------");
		db = helper.getWritableDatabase();
		db.execSQL("delete from login");
		this.close();
	}
	
	public void clearTableD(){
		System.out.println("-------clearTableD-------");
		db = helper.getWritableDatabase();
		db.execSQL("delete from device");
		this.close();
	}
	
	public void clearTableM(){
		System.out.println("-------clearTableM-------");
		db = helper.getWritableDatabase();
		db.execSQL("delete from devicemodify");
		this.close();
	}	
	
	public void close(){
		if (db != null ) {
			db.close();
		}
		if (cursor != null ) {
			cursor.close();
		}
	}
}
