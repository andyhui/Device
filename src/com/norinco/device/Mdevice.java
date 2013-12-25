package com.norinco.device;

public class Mdevice {	
	private int id;
	private String sperson;
	private String rperson;
	private int devnum;
	private int mtimes;
	private int mlevel;
	private String consume;
	private String finishdate;
	private String faultcause;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSperson() {
		return sperson;
	}
	public void setSperson(String sperson) {
		this.sperson = sperson;
	}
	public int getDevnum() {
		return devnum;
	}
	public void setDevnum(int devnum) {
		this.devnum = devnum;
	}
	public String getRperson(){
		return rperson;
	}
	public void setRperson(String rperson){
		this.rperson = rperson;
	}
	public int getMtimes(){
		return mtimes;
	}
	public void setMtimes(int mtimes){
		this.mtimes = mtimes;
	}
	public int getMlevel(){
		return mlevel;
	}
	public void setMlevel(int mlevel){
		this.mlevel = mlevel;
	}
	public String getFinishdate(){
		return finishdate;
	}
	public void setFinishdate(String finishdate){
		this.finishdate=  finishdate;
	}
	public String getConsume(){
		return consume;
	}
	public void setConsume(String consume){
		this.consume = consume;
	}
	public String getFaultcause(){
		return faultcause;
	}
	public void setFaultcause(String faultcause){
		this.faultcause = faultcause;
	}
}

