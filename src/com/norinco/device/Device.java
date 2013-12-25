package com.norinco.device;

public class Device {	
	private int id;
	private String devname;
	private String factory;
	private int devnum;
	private int devgroup;
	private String prodate;
	private String recdate;
	private String modifydate;
	
	public Device()
	{
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevname() {
		return devname;
	}
	public void setDevname(String devname) {
		this.devname = devname;
	}
	public int getDevnum() {
		return devnum;
	}
	public void setDevnum(int devnum) {
		this.devnum = devnum;
	}
	public String getFactory(){
		return factory;
	}
	public void setFactory(String factory){
		this.factory = factory;
	}
	public int getDevgroup(){
		return devgroup;
	}
	public void setDevgroup(int devgroup){
		this.devgroup = devgroup;
	}
	public String getProdate(){
		return prodate;
	}
	public void setProdate(String prodate){
		this.prodate=  prodate;
	}
	public String getRecdate(){
		return recdate;
	}
	public void setRecdate(String recdate){
		this.recdate = recdate;
	}
	public String getModifydate(){
		return modifydate;
	}
	public void setModifydate(String modifydate){
		this.modifydate = modifydate;
	}
}
