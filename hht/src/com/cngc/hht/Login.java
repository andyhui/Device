package com.cngc.hht;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class Login {
	private Button sure;
	private EditText user;
	private EditText password;

	private boolean u = false;
	private boolean p = false;

	public Login(Button sure, EditText user, EditText password) {
		this.sure = sure;
		this.user = user;
		this.password = password;		

		loginaction();
	}

	public boolean getU() {
		return this.u;
	}

	public boolean getP() {
		return this.p;
	}

	private void loginaction() {

		user.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0 && s.length()<21 && !u)
					u = true;
				else if (s.length() == 0 || s.length() > 20) {
					u = false;
					sure.setBackgroundResource(R.color.green_btn_color_disable);
				}

				if (u && p)
					sure.setBackgroundResource(R.color.green_btn_color_normal);
			}
		});

		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0 && s.length() < 51 && !p)
					p = true;
				else if (s.length() == 0 || s.length() > 50) {
					p = false;
					sure.setBackgroundResource(R.color.green_btn_color_disable);
				}

				if (u && p)
					sure.setBackgroundResource(R.color.green_btn_color_normal);
			}
		});
	}
}
