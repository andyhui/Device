package com.norinco.device;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MdBoxAdapter extends SimpleCursorAdapter{
	private LayoutInflater inflater;
	int nCount ;
	
	Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	
	public MdBoxAdapter(Context context, int layout, Cursor cursor, String[] from,int[] to) {
		super(context, layout, cursor, from, to);System.out.println("------->king0------>");
		
		for (int i = 0; i < cursor.getCount(); i++) {
			map.put(i, false);
		}System.out.println("------->king1------>");
	}

	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);System.out.println("------->king2------>");
		
		RelativeLayout relativeLayout = null;System.out.println("------->king3------>");
		if (view == null) {
			relativeLayout = (RelativeLayout) inflater.inflate(R.layout.contactlistitem, null);
		} else {
			relativeLayout = (RelativeLayout) view;
		}System.out.println("------->king4------>");
		String id = cursor.getString(cursor.getColumnIndex("number"));System.out.println("------->king5------>");
		String name = cursor.getString(cursor.getColumnIndex("replevel"));System.out.println("------->king6------>");
		String level = cursor.getString(cursor.getColumnIndex("finishDate"));System.out.println("------->king7------>");
		
		nCount = cursor.getPosition();
		TextView tv_id = (TextView) relativeLayout.findViewById(R.id.tv_cb_id);System.out.println("------->king8------>");
		TextView tv_name = (TextView) relativeLayout.findViewById(R.id.tv_cb_name);System.out.println("------->king9------>");
		TextView tv_level = (TextView) relativeLayout.findViewById(R.id.tv_cb_level);System.out.println("------->kinga------>");
		tv_id.setText(id);
		tv_name.setText(name);
		tv_level.setText(level);
		CheckBox checkBox = (CheckBox) relativeLayout.findViewById(R.id.cb_checkbox);
		checkBox.setOnCheckedChangeListener(listener);
		
		checkBox.setChecked(map.get(nCount));
		
	}
	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				map.put(nCount, true);
			} else {
				map.put(nCount, false);
			}
		}
	};	
}

