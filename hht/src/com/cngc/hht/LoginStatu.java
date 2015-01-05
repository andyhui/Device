package com.cngc.hht;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class LoginStatu extends Application {
	private String user;
	private String password;
	private int level;

	private boolean loginstatu = false;
	private boolean starting = false;

	public String getUser() {
		return this.user;
	}

	public String getPassword() {
		return this.password;
	}
	
	public int getLevel(){
		return this.level;
	}

	public boolean getLoginstatu() {
		return this.loginstatu;
	}

	public void setLoginStatu(String user, String password, int level) {
		this.user = user;
		this.password = password;
		this.level = level;
		if (level != -1)
			this.loginstatu = true;
		else
			this.loginstatu = false;
	}

	private List<Activity> activities = new ArrayList<Activity>();

	public void addActivity(Activity activity) {
		activities.add(activity);
		if (!starting)
			starting = true;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		for (Activity activity : activities) {
			activity.finish();
		}
		this.loginstatu = false;
		this.starting = false;
		System.exit(0);
	}

	public boolean getstarting() {
		return this.starting;
	}

}
