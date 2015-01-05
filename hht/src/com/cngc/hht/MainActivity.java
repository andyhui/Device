package com.cngc.hht;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.norinco.upload.Upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private LoginStatu loginstatu;

	private ImageButton more_icon;
	private ImageButton login_icon;
	private ImageButton fast_fix_icon;
	private Button read_icon;
	private Button write_icon;

	private SlidingMenu menu;
	private ListView list_menu;
	private String[] liststr = { "主界面", "同步", "设置", "退出", "关于" };
	private int[] pic = { R.drawable.main_more_goto_main,
			R.drawable.menu_icon_update, R.drawable.netflowmgr_float_setting,
			R.drawable.main_more_shortmessage_monitor,
			R.drawable.main_more_about };
	private ArrayList<Map<String, Object>> mData;

	private Button sure;
	private Button forgetpw;
	private EditText user_edittext;
	private EditText password;

	private boolean u = false;
	private boolean p = false;
	private int level = -1;

	private Login loginAction;

	private Button logout;
	private TextView user;

	private RfidPower power;
	private int rfidflag = -10;

	private DataBase logindb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);

		loginstatu = (LoginStatu) getApplicationContext();
		loginstatu.addActivity(MainActivity.this);
		System.out.print("2");
		menu = new SlidingMenu(this);
		slidingmenu();
		leftmenu();
		rightmenu();
		System.out.print("3");
		mainmenu();
	}

	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			exit_app();
		}
	}

	private void exit_app() {
		new AlertDialog.Builder(MainActivity.this).setTitle("注意")
				.setMessage("确定要退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (rfidflag == 0)
							power.off();
						loginstatu.onTerminate();
					}
				}).setNegativeButton("取消", null).show();
	}

	private void logout() {
		new AlertDialog.Builder(MainActivity.this).setTitle("注意")
				.setMessage("确定要注销？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						level = -1;
						loginstatu.setLoginStatu(null, null, level);
						u = false;
						p = false;
						menu.showContent();
						menu.setSecondaryMenu(R.layout.login);
						rightmenu();
					}
				}).setNegativeButton("取消", null).show();
	}

	private void mainmenu() {
		logindb = new DataBase();

		power = new RfidPower();
		if ((rfidflag = power.openserial()) == 0) {
			power.closeserial();
			power.on();
		}

		login_icon = (ImageButton) findViewById(R.id.login);
		login_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showSecondaryMenu();
			}
		});

		more_icon = (ImageButton) findViewById(R.id.more_icon);
		more_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showMenu();
			}
		});

		fast_fix_icon = (ImageButton) findViewById(R.id.fast_fix);
		fast_fix_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (loginstatu.getLoginstatu()) {
					if ((rfidflag = power.openserial()) == 0) {
						power.closeserial();
						JumpActivity(MultiLabel.class);
					} else {
						if (rfidflag == -1) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误").setMessage("打开RFID设备失败！")
									.show();
						} else if (rfidflag == -2) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误")
									.setMessage("RFID设备不存在或驱动未安装！").show();
						}
					}
				} else {
					settoast();
					menu.showSecondaryMenu();
				}
			}
		});

		read_icon = (Button) findViewById(R.id.read);
		read_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {System.out.println("read label*********");
				if (!loginstatu.getLoginstatu()) {
					settoast();
					menu.showSecondaryMenu();
				} else {
					if ((rfidflag = power.openserial()) == 0) {
						power.closeserial();
						JumpActivity(SingleLabel.class);
					} else {
						if (rfidflag == -1) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误").setMessage("打开RFID设备失败！")
									.show();
						} else if (rfidflag == -2) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误")
									.setMessage("RFID设备不存在或驱动未安装！").show();
						}
					}
				}
			}
		});

		write_icon = (Button) findViewById(R.id.write);
		write_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (loginstatu.getLoginstatu())
					JumpActivity(InputLabel.class);
				else {
					settoast();
					menu.showSecondaryMenu();
				}
			}
		});
	}

	private void rightmenu() {
		if (!loginstatu.getLoginstatu()) {
			user_edittext = (EditText) findViewById(R.id.login_user_edit);
			password = (EditText) findViewById(R.id.login_passwd_edit);
			sure = (Button) findViewById(R.id.login_login_btn);
			forgetpw = (Button) findViewById(R.id.forget_passwd);

			loginAction = new Login(sure, user_edittext, password);
			sure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					u = loginAction.getU();
					p = loginAction.getP();
					if (u && p) {
						if ((level = logindb.isUser(user_edittext.getText()
								.toString(), password.getText().toString())) != -1) {
							loginstatu.setLoginStatu(
									String.valueOf(user_edittext.getText()),
									String.valueOf(password.getText()), level);
							menu.setSecondaryMenu(R.layout.loging);
							menu.showContent();

							hideInput(v);

							user = (TextView) findViewById(R.id.username);
							user.setText(loginstatu.getUser());

							logout = (Button) findViewById(R.id.logout);
							logout.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									logout();
								}
							});
						} else {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误").setMessage("用户名或者密码错误")
									.show();
						}
					}
				}
			});

			forgetpw.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("帮助")
							.setMessage(
									"1.确定用户名或密码输入正确\n" + "2.确定用户名长度不超过20\n"
											+ "3.密码长度不超过50\n"
											+ "4.以上均没问题，请联系管理员").show();
				}

			});
		} else {
			user = (TextView) findViewById(R.id.username);
			user.setText(loginstatu.getUser());

			logout = (Button) findViewById(R.id.logout);
			logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					logout();
				}
			});
		}
	}

	private void leftmenu() {
		list_menu = (ListView) findViewById(R.id.menu_list);
		mData = new ArrayList<Map<String, Object>>();
		int lengh = liststr.length;
		for (int i = 0; i < lengh; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("image", pic[i]);
			item.put("title", liststr[i]);
			mData.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, mData,
				R.layout.menu_value, new String[] { "image", "title" },
				new int[] { R.id.image, R.id.title });
		list_menu.setAdapter(adapter);
		list_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					menu.showContent();
				} else if (position == 1) {
					/*
					 * update
					 */
					Upload up = new Upload(MainActivity.this);
				} else if (position == 2) {
					if ((rfidflag=power.openserial()) == 0){
						power.closeserial();
						JumpActivity(RfidSetting.class);
					}
					else {
						if (rfidflag == -1) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误").setMessage("打开RFID设备失败！")
									.show();
						} else if (rfidflag == -2) {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("错误")
									.setMessage("RFID设备不存在或驱动未安装！").show();
						}
					}
				} else if (position == 3) {
					exit_app();
				} else if (position == 4) {
					JumpActivity(StartActivity.class);
				}
			}
		});
	}

	private void slidingmenu() {
		menu.setMode(SlidingMenu.LEFT_RIGHT);

		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.title_bar);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(0.45f);
		menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu);

		if (!loginstatu.getLoginstatu())
			menu.setSecondaryMenu(R.layout.login);
		else
			menu.setSecondaryMenu(R.layout.loging);
	}

	private void settoast() {
		Toast toast = Toast.makeText(getApplicationContext(), "请登陆后再使用本软件！",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void hideInput(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	private void JumpActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	protected void onDestroy() {
		super.onDestroy();
		power.off();
	}

	protected void onResume() {
		super.onResume();
		power.on();
	}
}
