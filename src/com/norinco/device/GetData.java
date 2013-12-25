package com.norinco.device;

import android.util.Log;

public class GetData {
	private static final String TAG = "SrGetDataLabel";
	
	private int ret;
	private int t; 
	
	/* label info */
	private int factoryId;
	private int deviceId;
	private int devnum;
	private int devgroup;
	private int devgid;
	private int PC;
	private int y1,y2,y3;
	private int m1,m2,m3;
	private int d1,d2,d3;
	private double rssi;
	private Boolean[] EPC=new Boolean[12];
	private Boolean[] szOut=new Boolean[50];
	private String[] factory={
			"北方信息集团",
            "北方光电集团",
            "北方夜视集团",
            "北方导航集团",
            "北方车辆集团",
            "北方工业集团"
	};
	private String[] devname={
			"122自行榴弹炮",
            "履带式步兵战车",
            "轮式装甲连指挥车"
	};
	
	/* parameter setting */
	private Boolean antMask;  
	
	public GetData(int hCom,int flag)
	{
		if(flag==0)
		{
			ret=SrGetSingleLabel(hCom,EPC);
			if(ret==0)
			{
				Log.e(TAG, "getdata fail");
			}
			ret=SrGetDataLabel(hCom,0,PC,EPC,3,0,5,szOut);
		}
		else if(flag==1)
		{
			
		}
	}
	
	public int setDataLabel(int hCom)
	{
		System.out.println("factoryId="+factoryId+"devnum="+devnum+"deviceId="+deviceId);
		System.out.println("prodate="+y1+m1+d1);
		ret=SrSetDataLabel(hCom);
		if(ret==0)
		{
			Log.e(TAG, "setdata fail");
		}
		return ret;
	}
	
	public int getI()
	{
		return ret;
	}
	
	public String getFactory()
	{
		return factory[factoryId];
	}
	
	public void setFactoryId(String fname)
	{
		for(factoryId=0;factoryId<factory.length;factoryId++)
		{
			if(fname.equals(factory[factoryId]))
			{
				break;
			}
		}
	}
	
	public String getDevname()
	{
		return devname[deviceId];
	}
	
	public void setDeviceId(String dname)
	{
		for(deviceId=0;deviceId<devname.length;deviceId++)
		{
			if(dname.equals(devname[deviceId]))
			{
				break;
			}
		}
	}
	
	public int getDevnum()
	{
		return devnum;
	}
	
	public void setDevnum(int i)
	{
		devnum=i;
	}
	
	public int getDevgroup()
	{
		return devgroup;
	}
	
	public void setDevgroup(int i)
	{
		devgroup=i;
	}
	
	public int getDevgid()
	{
		return devgid;
	}
	
	public void setDevgid(int i)
	{
		devgid=i;
	}
	
	public String getProdate()
	{
		String date;
		date=String.valueOf(y1)+"."+String.valueOf(m1)+"."+String.valueOf(d1);
		return date;
	}
	
	public int atoi(char[] t,int num)
	{
		int ret=0,j;
		j=num-1;
		for(int i=0;i<num;i++)
		{
			ret += (((int)t[i])-48)*Math.pow(10,j);
			j--;
		}
		return ret;
	}
	
	public void getData(int[] d,String Data)
	{
		int i=0;
		int j=0;
		char[] t;
		t=new char[Data.length()];
		for(;Data.charAt(i)!='.';i++)
		{
			t[j++]=Data.charAt(i);
		}
		d[0]=atoi(t,j);
		for(i++;Data.charAt(i)!='.';i++)
		{
			j=0;
			t[j++]=Data.charAt(i);
		}
		d[1]=atoi(t,j);
		for(i++;i<Data.length() && Data.charAt(i)!='.';i++)
		{
			j=0;
			t[j++]=Data.charAt(i);
		}
		d[2]=atoi(t,j);
	}
	
	public void setProdate(String Data)
	{
		int[] d;
		d=new int[3];
		getData(d,Data);
		y1=d[0];
		m1=d[1];
		d1=d[2];
	}
	
	public String getRecdate()
	{
		String date;
		date=String.valueOf(y2)+"."+String.valueOf(m2)+"."+String.valueOf(d2);
		return date;
	}
	
	public void setRecdate(String Data)
	{
		int[] d;
		d=new int[3];
		getData(d,Data);
		y2=d[0];
		m2=d[1];
		d2=d[2];
	}
	
	public String getMdfdate()
	{
		String date;
		date=String.valueOf(y3)+"."+String.valueOf(m3)+"."+String.valueOf(d3);
		return date;
	}
	
	public void setMdfdate(String Data)
	{
		int[] d;
		d=new int[3];
		getData(d,Data);
		y3=d[0];
		m3=d[1];
		d3=d[2];
	}
	
	/*public void getAnt()
	{
		int ret;
		ret=(int)antMask;
	}*/
	
	//private native int SrGetAnt(int hCom);
	
	private native int SrSetDataLabel(int hCom);
	
	private native int SrGetSingleLabel(int hCom,Boolean[] EPC);
	
	private native int SrGetDataLabel(int hCom, int iPw ,int shPc ,Boolean[] szEpc , 
			int cBlock ,int shStartAddr ,int shDataLenWord,Boolean[] szOut);
	
	static {
		System.loadLibrary("serial_port");
	}
}
