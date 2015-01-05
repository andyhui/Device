package com.norinco.upload;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class SerializeEmeHandler {
	public String writeXml(ArrayList<EquipmentInfo> EquipmentInfoList) {
		StringWriter xmlWriter = new StringWriter();
		try {
			// 创建XmlSerializer,有两种方式
			// XmlPullParserFactory pullFactory;
			// 使用工厂类XmlPullParserFactory的方式
			// pullFactory = XmlPullParserFactory.newInstance();
			// XmlSerializer xmlSerializer = pullFactory.newSerializer();
			// 使用Android提供的实用工具类android.util.Xml
			XmlSerializer xmlSerializer = Xml.newSerializer();
			xmlSerializer.setOutput(xmlWriter);
			// 开始具体的写xml
			// <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
			xmlSerializer.startDocument("UTF-8", true);
			/* DatabaseOperateLogTable */
			xmlSerializer.startTag("", "DatabaseOperateLogTable");
			int i = 1;
			for (EquipmentInfo EquipmentInfo : EquipmentInfoList) {
				/* <add id=”1”> */				
				xmlSerializer.startTag("", "add");
				xmlSerializer.attribute("", "id", String.valueOf(i));
				// <Number>3</Number>
				xmlSerializer.startTag("", "Number");
				xmlSerializer.text(String.valueOf(EquipmentInfo.getNum()));
				xmlSerializer.endTag("", "Number");
				// <OperateTable>equip_record</OperateTable>
				xmlSerializer.startTag("", "OperateTable");
				xmlSerializer.text(EquipmentInfo.getOperateTable());
				xmlSerializer.endTag("", "OperateTable");
				// <OperateType>add</OperateType>
				// <RenewedTime>2013/12/17 14:23</RenewedTime>
				// <OriginalData>null</OriginalData>
				xmlSerializer.startTag("", "PrimaryKey");
				xmlSerializer.text(EquipmentInfo.getPrimaryKey());
				xmlSerializer.endTag("", "PrimaryKey");

				xmlSerializer.startTag("", "OperateObject");
				xmlSerializer.text(EquipmentInfo.getOperateObject());
				xmlSerializer.endTag("", "OperateObject");

				xmlSerializer.startTag("", "OperateType");
				xmlSerializer.text(EquipmentInfo.getOperateType());
				xmlSerializer.endTag("", "OperateType");

				xmlSerializer.startTag("", "RenewedTime");
				xmlSerializer.text(EquipmentInfo.getRenewedTime());
				xmlSerializer.endTag("", "RenewedTime");

				xmlSerializer.startTag("", "OriginalData");
				xmlSerializer.text(EquipmentInfo.getOriginalData());
				xmlSerializer.endTag("", "OriginalData");

				// <ModifiedData>
				xmlSerializer.startTag("", "ModifiedData");
				// <EquipID>45944</EquipID>
				xmlSerializer.startTag("", "EquipID");
				xmlSerializer.text(String.valueOf(EquipmentInfo.getEquipID()));
				xmlSerializer.endTag("", "EquipID");

				// <EquipName>指控显示终端</EquipName>
				xmlSerializer.startTag("", "EquipName");
				xmlSerializer.text(EquipmentInfo.getEquipName());
				xmlSerializer.endTag("", "EquipName");
				// <Department>5311工厂</Department>
				xmlSerializer.startTag("", "Department");
				xmlSerializer.text(EquipmentInfo.getDepartment());
				xmlSerializer.endTag("", "Department");
				// <ReporterName>舒克</ReporterName>
				xmlSerializer.startTag("", "ReporterName");
				xmlSerializer.text(EquipmentInfo.getReporterName());
				xmlSerializer.endTag("", "ReporterName");
				// <RepairName>贝塔</RepairName>
				xmlSerializer.startTag("", "RepairName");
				xmlSerializer.text(EquipmentInfo.getRepairName());
				xmlSerializer.endTag("", "RepairName");
				// <RepairNum>2</RepairNum>
				xmlSerializer.startTag("", "RepairNum");
				xmlSerializer
						.text(String.valueOf(EquipmentInfo.getRepairNum()));
				xmlSerializer.endTag("", "RepairNum");
				// <ProblemClass>3</ProblemClass>
				xmlSerializer.startTag("", "ProblemClass");
				xmlSerializer.text(String.valueOf(EquipmentInfo
						.getProblemClass()));
				xmlSerializer.endTag("", "ProblemClass");
				// <ProblemDescrip>显示屏花屏</ProblemDescrip>
				xmlSerializer.startTag("", "ProblemDescrip");
				xmlSerializer.text(EquipmentInfo.getProblemDescrip());
				xmlSerializer.endTag("", "ProblemDescrip");
				// <FixMethod>返厂维修</FixMethod>
				xmlSerializer.startTag("", "FixMethod");
				xmlSerializer.text(EquipmentInfo.getFixMethod());
				xmlSerializer.endTag("", "FixMethod");
				// <MaterialConsume>更换显示屏1块</MaterialConsume>
				xmlSerializer.startTag("", "MaterialConsume");
				xmlSerializer.text(EquipmentInfo.getMaterialConsume());
				xmlSerializer.endTag("", "MaterialConsume");
				// <ReportTime>2013/12/17 14:28</ReportTime>
				xmlSerializer.startTag("", "ReportTime");
				xmlSerializer.text(EquipmentInfo.getReportTime());
				xmlSerializer.endTag("", "ReportTime");
				// <FixTime>2013/12/17 14:29</FixTime>
				xmlSerializer.startTag("", "FixTime");
				xmlSerializer.text(EquipmentInfo.getFixTime());
				xmlSerializer.endTag("", "FixTime");
				// <Others>null</Others>
				xmlSerializer.startTag("", "Others");
				xmlSerializer.text(" ");
				xmlSerializer.endTag("", "Others");

				xmlSerializer.endTag("", "ModifiedData");
				// </entry>
				xmlSerializer.endTag("", "add");
				i++;
			}
			// </feed>
			xmlSerializer.endTag("", "DatabaseOperateLogTable");
			xmlSerializer.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlWriter.toString();
	}
}
