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

public class CheckBoxAdaper extends SimpleCursorAdapter{
	private LayoutInflater inflater;
	int nCount ;
	
	Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	
	public CheckBoxAdaper(Context context, int layout, Cursor cursor, String[] from,int[] to) {
		super(context, layout, cursor, from, to);
		
		for (int i = 0; i < cursor.getCount(); i++) {
			map.put(i, false);
		}
	}

	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		RelativeLayout relativeLayout = null;
		if (view == null) {
			relativeLayout = (RelativeLayout) inflater.inflate(R.layout.contactlistitem, null);
		} else {
			relativeLayout = (RelativeLayout) view;
		}
		String id = cursor.getInt(cursor.getColumnIndex("_id"))+"";
		String name = cursor.getString(cursor.getColumnIndex("username"));
		String level = cursor.getString(cursor.getColumnIndex("level"));
		
		nCount = cursor.getPosition();
		TextView tv_id = (TextView) relativeLayout.findViewById(R.id.tv_cb_id);
		TextView tv_name = (TextView) relativeLayout.findViewById(R.id.tv_cb_name);
		TextView tv_level = (TextView) relativeLayout.findViewById(R.id.tv_cb_level);
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