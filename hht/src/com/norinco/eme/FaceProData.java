package com.norinco.eme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FaceProData extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "eme.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "face_table";

	public static final String _ID = BaseColumns._ID;
	public static final String NUM = "num";
	public static final String PRO_NAME = "data";
	public static final String SOLUTION = "method";

	public FaceProData(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public FaceProData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NUM + " INTEGER, "
				+ PRO_NAME + " TEXT NOT NULL, " + SOLUTION + " TEXT NOT NULL"
				+ ");";

		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

	public Cursor fetchByProName(String ProName) throws SQLException {
		String[] from = { _ID, NUM, PRO_NAME, SOLUTION };
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME, from, PRO_NAME + "=" + ProName,
				null, null, null, null, null);
//		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " whele "
//				+ PRO_NAME + " = " + "'"+ProName+"'",
//				null);
//		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " whele "
//				+ PRO_NAME + " = " + ProName +" group by " + PRO_NAME,
//				null);
		// String order = _ID;
		// Cursor cursor = db.query(true, TABLE_NAME, from, NUM + "=" + rowid,
		// null, null, null, null, null);
		return cursor;
	}

	public Cursor fetchData(long rowid) throws SQLException {
		String[] from = { _ID, NUM, PRO_NAME, SOLUTION };
		SQLiteDatabase db = getReadableDatabase();
		// String order = _ID;
		Cursor cursor = db.query(true, TABLE_NAME, from, NUM + "=" + rowid,
				null, null, null, null, null);
		return cursor;
	}

	public Cursor fetchProData() {
		String[] from = { _ID, NUM, PRO_NAME, SOLUTION };
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
				+ " group by " + NUM, null);
		// String order = _ID;
		// Cursor cursor = db.query(true, TABLE_NAME, from, NUM + "=" + rowid,
		// null, null, null, null, null);
		return cursor;

	}

	public void insert(int num, String name, String solution) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NUM, num);
		values.put(PRO_NAME, name);
		values.put(SOLUTION, solution);

		db.insertOrThrow(TABLE_NAME, null, values);
	}

	public Cursor all(Activity activity) {
		String[] from = { _ID, NUM, PRO_NAME, SOLUTION };
		String order = _ID;

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

	public void close() {
		SQLiteDatabase db = getReadableDatabase();
		db.close();
	}

}
