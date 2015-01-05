package com.cngc.hht;

import android.util.Log;

public class RfidPower {
	private int hCom;
	private String hardware;
	private String firmware;
	private char[] txpower;
	private int[] radiostate;
	private String rflinkprofile;
	private char antenna;
	private String hz;
	private short temperature;
	private char[] gen2;

	private String[] rfidstate;
	private String[] txpower_value;
	private String[] gen2_value;

	private DeviceInfo devinfo;

	public RfidPower() {
		InitRfid();
		devinfo = new DeviceInfo();
	}

	private void InitRfid() {
		hCom = 0;
		hardware = null;
		firmware = null;
		txpower = new char[4];
		radiostate = new int[20];
		rflinkprofile = null;
		antenna = 0;
		hz = null;
		temperature = 0;
		gen2 = new char[5];

		int i;
		for (i = 0; i < 4; i++) {
			txpower[i] = '\0';
		}
		for (i = 0; i < 20; i++) {
			radiostate[i] = 0;
		}
		for (i = 0; i < 5; i++) {
			gen2[i] = '\0';
		}
	}

	public String getHardware() {
		return this.hardware;
	}

	public String getFirmware() {
		return this.firmware;
	}

	public String[] getTxpower() {
		txpower_value = new String[4];
		txpower_value[0] = ((txpower[0] & 0xff) == 0x01 ? "闭环" : "开环");
		txpower_value[1] = String.valueOf(txpower[1] & 0x000000ff) + "dBm";
		txpower_value[2] = String.valueOf(txpower[2] & 0x000000ff) + "dBm";
		txpower_value[3] = "";
		return this.txpower_value;
	}

	public int[] getRadiostate() {
		return this.radiostate;
	}

	public String getRflinkprofile() {
		return this.rflinkprofile;
	}

	public char getAntenna() {
		return this.antenna;
	}

	public String getHZ() {
		return this.hz;
	}

	public short getTemperature() {
		return this.temperature;
	}

	public String[] getGen2() {
		gen2_value = new String[5];
		gen2_value[0] = ((gen2[0] & 0xff) == 1 ? "动态" : "固定");
		gen2_value[1] = String.valueOf(gen2[1] & 0x000000ff);
		if ((gen2[0] & 0xff) == 1) {
			gen2_value[2] = String.valueOf(gen2[2] & 0x000000ff);
			gen2_value[3] = String.valueOf(gen2[3] & 0x000000ff);
		} else {
			gen2_value[2] = "忽略";
			gen2_value[3] = "忽略";
		}
		gen2_value[4] = "";
		return this.gen2_value;
	}

	public String[] getRfidState() {
		rfidstate = new String[10];
		rfidstate[0] = this.hardware;
		rfidstate[1] = this.firmware;
		rfidstate[2] = "点击查看详细";
		rfidstate[3] = "点击查看详细";
		rfidstate[4] = this.rflinkprofile;
		rfidstate[5] = String.valueOf(this.antenna & 0x000000ff);
		rfidstate[6] = this.hz;
		rfidstate[7] = String.valueOf(this.temperature & 0x0000ffff) + "摄氏度";
		rfidstate[8] = "点击查看详细";
		rfidstate[9] = "";

		return this.rfidstate;
	}

	public void setRfidState(String hardware, String firmware, char[] txpower,
			int[] radiostate, String rflinkprofile, char antenna, String hz,
			short temperature, char[] gen2) {
		this.hardware = hardware;
		this.firmware = firmware;
		System.arraycopy(txpower, 0, this.txpower, 0, 3);
		System.arraycopy(radiostate, 0, this.radiostate, 0, radiostate[0] + 1);
		this.rflinkprofile = rflinkprofile;
		this.antenna = antenna;
		this.hz = hz;
		this.temperature = temperature;
		System.arraycopy(gen2, 0, this.gen2, 0, 4);
	}
	
	public int getEquipmentSN(){
		return devinfo.getEquipmentSN();
	}
	
	public int getEquipAssetNo(){
		return devinfo.getEquipAssetNo();
	}
	
	public String getEquipmentName(){
		return devinfo.getEquipmentName();
	}
	
	public int getEquipManuClass(){
		return devinfo.getEquipManuClass();
	}
	
	public String getEquipFactory(){
		return devinfo.getEquipFactory();
	}
	
	public String getEquipManuTime(){
		return devinfo.getEquipManuTime();
	}
	
	public String getEquipFitoutTime(){
		return devinfo.getEquipFitoutTime();
	}

	public void setDeviceInfo(int equipmentSN, int equipAssetNo,
			String equipmentName, int equipManuClass, String equipFactory,
			String equipManuTime, String equipFitoutTime) {
		devinfo.setEquipInfo(equipmentSN, equipAssetNo, equipmentName,
				equipManuClass, equipFactory, equipManuTime, equipFitoutTime);

		System.out.println("sn:" + devinfo.getEquipmentSN() + "assetno:"
				+ devinfo.getEquipAssetNo() + "name:"
				+ devinfo.getEquipmentName() + "group:"
				+ devinfo.getEquipManuClass() + "factory:"
				+ devinfo.getEquipFactory() + "manutime:"
				+ devinfo.getEquipManuTime() + "receivetime:"
				+ devinfo.getEquipFitoutTime());
	}

	public DeviceInfo getDevice() {
		return this.devinfo;
	}
	
	public int writelabel(DeviceInfo device){
		System.out.println("write label");
		this.devinfo=device;
		if(WriteLabel(hCom)==-1){
			Log.e("writelabel", "write label error");
			return -1;
		}
		return 0;
	}

	public int readlabel(int flag) {
		System.out.println("read label");
		if (flag == 0) {
			if (ReadLabelSingle(hCom) == -1) {
				Log.e("readlabel", "read label error");
				return -1;
			}
		} else if (flag == 1) {
			if (ReadLabelMulti(hCom) == -1) {
				Log.e("readlabel", "read label error");
				return -1;
			}
		} else
			return -2;
		return 0;
	}

	public void stopMuti() {
		StopLabelMulti(hCom);
	}

	public void getsetting() {
		if (GetRfidState(hCom) == -1) {
			Log.e("getsetting", "get rfid states fail!");
		}
	}

	public void closeserial() {
		CloseUart(hCom);
	}

	public int openserial() {
		hCom = OpenUart();
		if (hCom == -1) {
			Log.e("UART", "open UART error");
			return hCom;
		} else if (hCom == -2) {
			Log.i("UART", "串口设备不存在");
			return hCom;
		}
		// System.out.println("fd:" + hCom);
		return 0;
	}

	public void off() {
		UnInit();
		hCom=0;
	}

	public void on() {
		Init();
	}

	private void showrfidstate() {
		int i;

		System.out.println("Hardware版本号：" + hardware);
		System.out.println("Firmware版本号：" + firmware);
		System.out.println("TxPower:状态为" + ((txpower[0] == 0x01) ? "闭环" : "开环")
				+ "读功率为" + (txpower[1] & 0xff) + "写功率为" + (txpower[2] & 0xff));
		System.out.println("射频频率（单位:KHZ）:   状态: 跳频，共有" + radiostate[0]
				+ "个跳频频点");
		System.out.println("频率分别为");
		for (i = 0; i < (radiostate[0]); i++) {
			System.out.println(radiostate[i + 1]);
		}

		System.out.println("RF LINK PROFILE:" + rflinkprofile);
		System.out.println("当前工作的天线：" + (antenna & 0xff));
		System.out.println("当前工作的频率区域:" + hz);
		System.out.println("当前工作的温度：" + temperature + "摄氏度");
		System.out.println("gen2参数:    Q算法:" + (gen2[0] == 1 ? "动态" : "固定"));
		System.out.println("StartQ:" + (gen2[1] & 0xff));
		if (gen2[0] == 1) {
			System.out.println("MinQ:" + (gen2[2] & 0xff));
			System.out.println("MaxQ:" + (gen2[3] & 0xff));
		} else
			System.out.println("固定Q算法，忽略MinQ及MaxQ！");
	}

	private native void Init();

	private native void UnInit();

	private native int OpenUart();

	private native void CloseUart(int hCom);

	private native int GetRfidState(int hCom);
	
	private native int WriteLabel(int hCom);

	private native int ReadLabelSingle(int hCom);

	private native int ReadLabelMulti(int hCom);

	private native int StopLabelMulti(int hCom);

	static {
		System.loadLibrary("sanray_api");
	}
}
