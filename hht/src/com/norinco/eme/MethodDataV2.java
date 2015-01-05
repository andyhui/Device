package com.norinco.eme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MethodDataV2 extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "method.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "methodstwo";

	public static final String _ID = BaseColumns._ID;
	public static final String EQUIP_ID = "equip_id";
	public static final String FACTORY = "factory";
	public static final String DEVICE = "device_name";
	public static final String REPORTER = "reporter";
	public static final String FIXER = "fixer";
	public static final String PRODUCTDATE = "product_data";
	public static final String RECVEDATE = "recv_date";
	public static final String MODIFYDATE = "modify_date";
	public static final String PRO_NAME = "pro_name";
	public static final String SOLUTION = "solution";

	public MethodDataV2(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public MethodDataV2(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID
		// + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRO_NAME
		// + " TEXT NOT NULL, " + SOLUTION + " TEXT NOT NULL" + ");";

		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + EQUIP_ID
				+ " INTEGER, " + FACTORY + " TEXT NOT NULL, " + DEVICE
				+ " TEXT NOT NULL, "+ REPORTER
				+ " TEXT NOT NULL, "+ FIXER
				+ " TEXT NOT NULL, " + PRODUCTDATE + " TEXT NOT NULL, "
				+ RECVEDATE + " TEXT NOT NULL, " + MODIFYDATE
				+ " TEXT NOT NULL, " + PRO_NAME + " TEXT NOT NULL, " + SOLUTION
				+ " TEXT NOT NULL" + ");";

		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

	public void insert(int num, String factory, String device, String reporter,String fixer,String productDate,String recvDate,
			String modifyDate, String name, String solution) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(EQUIP_ID, num);
		values.put(FACTORY, factory);
		values.put(DEVICE, device);
		values.put(REPORTER, reporter);
		values.put(FIXER, fixer);
		values.put(PRODUCTDATE, productDate);
		values.put(RECVEDATE, recvDate);
		values.put(MODIFYDATE, modifyDate);
		values.put(PRO_NAME, name);
		values.put(SOLUTION, solution);

		db.insertOrThrow(TABLE_NAME, null, values);
	}

	public Cursor all() {
		// String[] from = { _ID, EQUIP_ID ,PRO_NAME, SOLUTION };
		// String order = PRO_NAME;

		SQLiteDatabase db = getReadableDatabase();
		// Cursor cursor = db.query(TABLE_NAME, from, null, null, null,
		// null,null);
		Cursor cursor = db.rawQuery("select * from methodstwo", null);
		return cursor;
	}

	public long count() {
		SQLiteDatabase db = getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}

	public void close() {
		SQLiteDatabase db = getReadableDatabase();
		db.close();
	}

}
