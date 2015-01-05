package com.norinco.eme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MethodData extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "method.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "methods";

	public static final String _ID = BaseColumns._ID;
	public static final String PRO_NAME = "pro_name";
	public static final String SOLUTION = "solution";

	public MethodData(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public MethodData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRO_NAME
				+ " TEXT NOT NULL, " + SOLUTION + " TEXT NOT NULL" + ");";

		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

	public void insert(String name, String solution) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PRO_NAME, name);
		values.put(SOLUTION, solution);

		db.insertOrThrow(TABLE_NAME, null, values);
	}

	public Cursor all(Activity activity) {
		String[] from = { _ID, PRO_NAME, SOLUTION };
		String order = PRO_NAME;

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null,
				order);
		activity.startManagingCursor(cursor);
		return cursor;
	}

	public long count() {
		SQLiteDatabase db = getReadableDatabase();
		return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
	}
	
	public void close(){
		SQLiteDatabase db = getReadableDatabase();
		db.close();
	}

}
