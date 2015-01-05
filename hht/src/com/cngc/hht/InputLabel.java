package com.cngc.hht;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class InputLabel extends Activity {
	private RfidPower power;
	private DeviceInfo device = null;
	private EditText idevname;
	private EditText idevnum;
	private EditText idevSN;
	private EditText idevgroup;
	private EditText ifactory;
	private EditText iprodate;
	private EditText irecedate;
	private TextView sure;

	private int equipmentSN = 0;
	private int equipAssetNo = 0;
	private String equipmentName;
	private int equipManuClass = 0;
	private String equipFactory;
	private String equipManuTime;
	private String equipFitoutTime;

	private Calendar currentdate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inputinfo);
		init();

		iprodate.setClickable(true);
		iprodate.setFocusable(false);
		iprodate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(InputLabel.this, data, currentdate
						.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),
						currentdate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		irecedate.setClickable(true);
		irecedate.setFocusable(false);
		irecedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(InputLabel.this, data1, currentdate
						.get(Calendar.YEAR), currentdate.get(Calendar.MONTH),
						currentdate.get(Calendar.DAY_OF_MONTH)).show();
			}

		});

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (setdevice() == 0) {
					new AlertDialog.Builder(InputLabel.this)
							.setTitle("注意")
							.setMessage(
									"装备名称：" + equipmentName + "\n" + "资产编号："
											+ String.valueOf(equipAssetNo)
											+ "\n" + "装备序列："
											+ String.valueOf(equipmentSN)
											+ "\n" + "生产批次："
											+ String.valueOf(equipManuClass)
											+ "\n" + "生产厂家：" + equipFactory
											+ "\n" + "生产日期：" + equipManuTime
											+ "\n" + "接收日期：" + equipFitoutTime
											+ "\n")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											writelabel();
										}
									}).setNegativeButton("取消", null).show();
				} else {
					new AlertDialog.Builder(InputLabel.this)
							.setTitle("警告")
							.setMessage("输入为空！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
										}
									}).show();
				}
			}
		});
	}

	private DatePickerDialog.OnDateSetListener data = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			currentdate.set(Calendar.YEAR, year);
			currentdate.set(Calendar.MONTH, monthOfYear);
			currentdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			monthOfYear += 1;
			iprodate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
		}
	};

	private DatePickerDialog.OnDateSetListener data1 = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			currentdate.set(Calendar.YEAR, year);
			currentdate.set(Calendar.MONTH, monthOfYear);
			currentdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			monthOfYear += 1;
			irecedate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
		}
	};

	private int setdevice() {
		if (TextUtils.isEmpty(idevSN.getText())
				|| TextUtils.isEmpty(idevnum.getText())
				|| TextUtils.isEmpty(idevgroup.getText())
				|| TextUtils.isEmpty(idevname.getText())
				|| TextUtils.isEmpty(ifactory.getText())
				|| TextUtils.isEmpty(iprodate.getText())
				|| TextUtils.isEmpty(irecedate.getText())) {
			return -1;
		}
		equipmentSN = Integer.parseInt(idevSN.getText().toString());
		equipAssetNo = Integer.parseInt(idevnum.getText().toString());
		equipManuClass = Integer.parseInt(idevgroup.getText().toString());
		equipmentName = idevname.getText().toString();
		equipFactory = ifactory.getText().toString();
		equipManuTime = iprodate.getText().toString();
		equipFitoutTime = irecedate.getText().toString();

		device.setEquipInfo(equipmentSN, equipAssetNo, equipmentName,
				equipManuClass, equipFactory, equipManuTime + '-',
				equipFitoutTime + '-');

		return 0;
	}

	private void init() {
		idevname = (EditText) findViewById(R.id.idevname);
		idevnum = (EditText) findViewById(R.id.idevnum);
		idevSN = (EditText) findViewById(R.id.idevSN);
		idevgroup = (EditText) findViewById(R.id.idevgroup);
		ifactory = (EditText) findViewById(R.id.ifactory);
		iprodate = (EditText) findViewById(R.id.iprodate);
		irecedate = (EditText) findViewById(R.id.irecdate);
		sure = (TextView) findViewById(R.id.sure);

		device = new DeviceInfo();
		power = new RfidPower();

		currentdate = Calendar.getInstance(Locale.CHINA);
	}

	private void writelabel() {
		int ret = 0;
		if ((ret = power.openserial()) < 0) {
			if (ret == -1) {
				new AlertDialog.Builder(InputLabel.this).setTitle("错误")
						.setMessage("打开RFID设备失败！").show();
			} else if (ret == -2) {
				new AlertDialog.Builder(InputLabel.this).setTitle("错误")
						.setMessage("RFID设备不存在或驱动未安装！").show();
			}
			return;
		}
		if (power.writelabel(device) < 0) {
			new AlertDialog.Builder(InputLabel.this)
					.setTitle("警告")
					.setMessage("写入标签失败！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
		} else {
			new AlertDialog.Builder(InputLabel.this)
					.setTitle("提示")
					.setMessage("写入标签成功！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									JumpActivity(MainActivity.class);
								}
							}).show();
		}
		power.closeserial();
	}

	private void JumpActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	}

	public void about_back(View v) {
		JumpActivity(MainActivity.class);
	}
}
