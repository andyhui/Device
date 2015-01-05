package com.norinco.upload;

public class EquipmentInfo {
	/*<add id=”1”>
	<Number>3</Number>
	<OperateTable>equip_record</OperateTable>
	<PrimaryKey>45944</PrimaryKey>
	<OperateObject>all</OperateObject>
	<OperateType>add</OperateType>
	<RenewedTime>2013/12/17 14:23</RenewedTime>
	<OriginalData>null</OriginalData>
		<ModifiedData>
			<EquipID>45944</EquipID>
			<EquipName>指控显示终端</EquipName>
			<Department>5311工厂</Department>
			<ReporterName>舒克</ReporterName>
			<RepairName>贝塔</RepairName>
			<RepairNum>2</RepairNum> 
			<ProblemClass>3</ProblemClass>
			<ProblemDescrip>显示屏花屏</ProblemDescrip>
			<FixMethod>返厂维修</FixMethod>
			<MaterialConsume>更换显示屏1块</MaterialConsume>
			<ReportTime>2013/12/17 14:28</ReportTime>
			<FixTime>2013/12/17 14:29</FixTime>
			<Others>null</Others>
	    </ModifiedData>
	</add>
	*/
	
	int num;
	String OperateTable;
	String PrimaryKey;
	String OperateObject;
	String OperateType;
	String RenewedTime;/*更新时间*/
	String OriginalData;
	String EquipID;
	String EquipName;
	String Department;
	String ReporterName;
	String RepairName;
	int RepairNum;/*维修次数*/
	int ProblemClass;/*故障等级*/
	String ProblemDescrip;/*故障描述*/
	String FixMethod;
	String MaterialConsume;
	String ReportTime;
	String FixTime;
	String Others;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getOperateTable() {
		return OperateTable;
	}
	public void setOperateTable(String operateTable) {
		OperateTable = operateTable;
	}
	public String getPrimaryKey() {
		return PrimaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		PrimaryKey = primaryKey;
	}
	public String getOperateObject() {
		return OperateObject;
	}
	public void setOperateObject(String operateObject) {
		OperateObject = operateObject;
	}
	public String getOperateType() {
		return OperateType;
	}
	public void setOperateType(String operateType) {
		OperateType = operateType;
	}
	public String getRenewedTime() {
		return RenewedTime;
	}
	public void setRenewedTime(String renewedTime) {
		RenewedTime = renewedTime;
	}
	public String getOriginalData() {
		return OriginalData;
	}
	public void setOriginalData(String originalData) {
		OriginalData = originalData;
	}
	public String getEquipID() {
		return EquipID;
	}
	public void setEquipID(String equipID) {
		EquipID = equipID;
	}
	public String getEquipName() {
		return EquipName;
	}
	public void setEquipName(String equipName) {
		EquipName = equipName;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	public String getReporterName() {
		return ReporterName;
	}
	public void setReporterName(String reporterName) {
		ReporterName = reporterName;
	}
	public String getRepairName() {
		return RepairName;
	}
	public void setRepairName(String repairName) {
		RepairName = repairName;
	}
	public int getRepairNum() {
		return RepairNum;
	}
	public void setRepairNum(int repairNum) {
		RepairNum = repairNum;
	}
	public int getProblemClass() {
		return ProblemClass;
	}
	public void setProblemClass(int problemClass) {
		ProblemClass = problemClass;
	}
	public String getProblemDescrip() {
		return ProblemDescrip;
	}
	public void setProblemDescrip(String problemDescrip) {
		ProblemDescrip = problemDescrip;
	}
	public String getFixMethod() {
		return FixMethod;
	}
	public void setFixMethod(String fixMethod) {
		FixMethod = fixMethod;
	}
	public String getMaterialConsume() {
		return MaterialConsume;
	}
	public void setMaterialConsume(String materialConsume) {
		MaterialConsume = materialConsume;
	}
	public String getReportTime() {
		return ReportTime;
	}
	public void setReportTime(String reportTime) {
		ReportTime = reportTime;
	}
	public String getFixTime() {
		return FixTime;
	}
	public void setFixTime(String fixTime) {
		FixTime = fixTime;
	}
	public String getOthers() {
		return Others;
	}
	public void setOthers(String others) {
		Others = others;
	}
	

}
