package com.norinco.eme;

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
			// <feed number="25">
			// xmlSerializer.startTag("", "feed");
			// xmlSerializer.attribute("", "number",
			// String.valueOf(earthquakeEntryList.size()));
			for (EquipmentInfo EquipmentInfo : EquipmentInfoList) {
				/* <add id=”1”> */
				xmlSerializer.startTag("", "add");
				xmlSerializer.attribute("", "id", "1");
				// <Number>3</Number>
				xmlSerializer.startTag("", "Number");
				xmlSerializer.text("3");
				xmlSerializer.endTag("", "Number");
				// <OperateTable>equip_record</OperateTable>
				xmlSerializer.startTag("", "OperateTable");
				xmlSerializer.text("equip_record");
				xmlSerializer.endTag("", "OperateTable");
				// <OperateType>add</OperateType>
				// <RenewedTime>2013/12/17 14:23</RenewedTime>
				// <OriginalData>null</OriginalData>
				xmlSerializer.startTag("", "OperateType");
				xmlSerializer.text("add");
				xmlSerializer.endTag("", "OperateType");

				xmlSerializer.startTag("", "RenewedTime");
				xmlSerializer.text("2013/12/17 14:23");
				xmlSerializer.endTag("", "RenewedTime");

				xmlSerializer.startTag("", "OriginalData");
				xmlSerializer.text("null");
				xmlSerializer.endTag("", "OriginalData");

				// <ModifiedData>
				xmlSerializer.startTag("", "ModifiedData");
//				<EquipID>45944</EquipID>
				xmlSerializer.startTag("","EquipID");
				xmlSerializer.text("45944");
				xmlSerializer.endTag("", "EquipID");
				
//				<EquipName>指控显示终端</EquipName>
				xmlSerializer.startTag("", "EquipName");
				xmlSerializer.text("指控显示终端");
				xmlSerializer.endTag("", "EquipName");
//				<Department>5311工厂</Department>
				xmlSerializer.startTag("", "Department");
				xmlSerializer.text("5311工厂");
				xmlSerializer.endTag("", "Department");
//				<ReporterName>舒克</ReporterName>
				xmlSerializer.startTag("", "ReporterName");
				xmlSerializer.text("舒克");
				xmlSerializer.endTag("", "ReporterName");
//				<RepairName>贝塔</RepairName>
				xmlSerializer.startTag("", "RepairName");
				xmlSerializer.text("贝塔");
				xmlSerializer.endTag("", "RepairName");
//				<RepairNum>2</RepairNum>
				xmlSerializer.startTag("", "RepairNum");
				xmlSerializer.text("2");
				xmlSerializer.endTag("", "RepairNum");
//				<ProblemClass>3</ProblemClass>
				xmlSerializer.startTag("", "ProblemClass");
				xmlSerializer.text("3");
				xmlSerializer.endTag("", "ProblemClass");
//				<ProblemDescrip>显示屏花屏</ProblemDescrip>
				xmlSerializer.startTag("", "ProblemDescrip");
				xmlSerializer.text("显示屏花屏");
				xmlSerializer.endTag("", "ProblemDescrip");
//				<FixMethod>返厂维修</FixMethod>
				xmlSerializer.startTag("", "FixMethod");
				xmlSerializer.text("返厂维修");
				xmlSerializer.endTag("", "FixMethod");
//				<MaterialConsume>更换显示屏1块</MaterialConsume>
				xmlSerializer.startTag("", "MaterialConsume");
				xmlSerializer.text("更换显示屏1块");
				xmlSerializer.endTag("", "MaterialConsume");
//				<ReportTime>2013/12/17 14:28</ReportTime>
				xmlSerializer.startTag("", "ReportTime");
				xmlSerializer.text("2013/12/17 14:28");
				xmlSerializer.endTag("", "ReportTime");
//				<FixTime>2013/12/17 14:29</FixTime>
				xmlSerializer.startTag("", "FixTime");
				xmlSerializer.text("2013/12/17 14:29");
				xmlSerializer.endTag("", "FixTime");
//				<Others>null</Others>
				xmlSerializer.startTag("", "Others");
				xmlSerializer.text(null);
				xmlSerializer.endTag("", "Others");
				
				xmlSerializer.endTag("", "ModifiedData");
				/*// <PrimaryKey>45944</PrimaryKey>
				xmlSerializer.startTag("", "updated");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateString = sdf.format(emeProblem.getDate());
				xmlSerializer.text(dateString);
				xmlSerializer.endTag("", "updated");
				// <link>http://earthquake.usgs.gov/earthquakes/recenteqsww/Quakes/us2010brar.php</link>
				xmlSerializer.startTag("", "link");
				xmlSerializer.text(emeProblem.getLink());
				xmlSerializer.endTag("", "link");
				// <latitude>-14.3009</latitude>
				xmlSerializer.startTag("", "latitude");
				xmlSerializer.text(String.valueOf(emeProblem.getLocation()
						.getLatitude()));
				xmlSerializer.endTag("", "latitude");
				// <longitude>167.9491</longitude>
				xmlSerializer.startTag("", "longitude");
				xmlSerializer.text(String.valueOf(emeProblem.getLocation()
						.getLongitude()));
				xmlSerializer.endTag("", "longitude");
				// <elev>-80100.0</elev>
				xmlSerializer.startTag("", "elev");
				xmlSerializer.text(String.valueOf(emeProblem.getElev()));
				xmlSerializer.endTag("", "elev");*/
				// </entry>
				xmlSerializer.endTag("", "add");
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
