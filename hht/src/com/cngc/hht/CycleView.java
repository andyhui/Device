package com.cngc.hht;

public class CycleView {
	private DeviceInfo dev[];
	private int tail = 0;

	public CycleView() {
		dev = new DeviceInfo[100];
	}

	public DeviceInfo getDevice(int i) {
		return this.dev[i];
	}

	public void saveEquipInfo(int equipmentSN, int equipAssetNo,
			String equipmentName, int equipManuClass, String equipFactory,
			String equipManuTime, String equipFitoutTime) {
		int i;
		for (i = 0; i < 100; i++)
			if (dev[i].getFlag() == false)
				dev[i].setEquipInfo(equipmentSN, equipAssetNo, equipmentName,
						equipManuClass, equipFactory, equipManuTime,
						equipFitoutTime);
	}

	public int saveEquipInfo(DeviceInfo dev) {
		tail += 1;
		if(tail>99)
			return -1;
		if (this.dev[tail].getFlag() == false)
			this.dev[tail].setEquipInfo(dev.getEquipmentSN(),
					dev.getEquipAssetNo(), dev.getEquipmentName(),
					dev.getEquipManuClass(), dev.getEquipFactory(),
					dev.getEquipManuTime(), dev.getEquipFitoutTime());
		return 0;
	}
}
