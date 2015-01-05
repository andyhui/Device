package com.norinco.eme;


import com.cngc.hht.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Eme_problem extends TabActivity implements OnCheckedChangeListener {
	
	private RadioGroup group;  
    private TabHost tabHost;
    
    RadioButton face;

    private String face_tab = "face";
    private String function_tab = "function";
    private String sys_tab = "system_checked";
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eme_pro);
		init();
		setNavigationBar();
		tabHost.setCurrentTabByTag(face_tab);
		face = (RadioButton)findViewById(R.id.radio_face);
		face.setChecked(true);
	}
    private void init(){
    	group = (RadioGroup)findViewById(R.id.buttongroup);
    	group.setOnCheckedChangeListener(this);
    	tabHost = getTabHost();
    }
    private void setNavigationBar(){
    	TabSpec tab_face = tabHost.newTabSpec(face_tab);
    	TabSpec tab_function = tabHost.newTabSpec(function_tab);
    	TabSpec tab_sys = tabHost.newTabSpec(sys_tab);
    	
    	tab_face.setIndicator(face_tab).setContent(new Intent(this,face_Activity001.class));
    	tab_function.setIndicator(function_tab).setContent(new Intent(this,function_Activity.class));
    	tab_sys.setIndicator(sys_tab).setContent(new Intent(this,sys_Activity.class));
    	
    	tabHost.addTab(tab_face);
    	tabHost.addTab(tab_function);
    	tabHost.addTab(tab_sys);
    }
    
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
		case R.id.radio_face:
			tabHost.setCurrentTabByTag(face_tab);
			break;
		case R.id.radio_function:
			tabHost.setCurrentTabByTag(function_tab);
			break;
		case R.id.radio_sys:
			tabHost.setCurrentTabByTag(sys_tab);
			break;
		
		}
		
	}

}
