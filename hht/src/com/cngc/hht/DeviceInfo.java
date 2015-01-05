package com.cngc.hht;

public class DeviceInfo {
	private boolean flag;

	private int equipmentSN;
	private int equipAssetNo;
	private String equipmentName;
	private int equipManuClass;
	private String equipFactory;
	private String equipManuTime;
	private String equipFitoutTime;
	
	public boolean getFlag(){
		return this.flag;
	}
	
	public int getEquipmentSN(){
		return this.equipmentSN;
	}
	
	public int getEquipAssetNo(){
		return this.equipAssetNo;
	}
	
	public String getEquipmentName(){
		return this.equipmentName;
	}
	
	public int getEquipManuClass(){
		return this.equipManuClass;
	}
	
	public String getEquipFactory(){
		return this.equipFactory;
	}
	
	public String getEquipManuTime(){
		return this.equipManuTime;
	}
	
	public String getEquipFitoutTime(){
		return this.equipFitoutTime;
	}

	public void setEquipInfo(int equipmentSN, int equipAssetNo,
			String equipmentName, int equipManuClass, String equipFactory,
			String equipManuTime, String equipFitoutTime) {
		this.equipAssetNo = equipAssetNo;
		this.equipFactory = equipFactory;
		this.equipFitoutTime = equipFitoutTime;
		this.equipManuClass = equipManuClass;
		this.equipManuTime = equipManuTime;
		this.equipmentName = equipmentName;
		this.equipmentSN = equipmentSN;
		this.flag=true;
	}

	public DeviceInfo() {
		this.flag = false;
		setEquipInfo(0, 0, null, 0, null, null, null);
	}

}
