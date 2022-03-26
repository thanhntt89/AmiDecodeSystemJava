package saoviet.amisystem.ulti;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import saoviet.amisystem.model.SystemConfig;

public class FileUlti {
	
 	public static SystemConfig getSystemConfig() {
		String filename = System.getProperty("user.dir") + "\\config\\SystemConfig.xml";
		SystemConfig sys = new SystemConfig();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {

			db = dbf.newDocumentBuilder();
			doc = db.parse(new File(filename));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("mqttconfig");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;

			sys.setMqttGatwayAddress(eElement.getAttribute("gatewayAddress"));
			sys.setMqttPort(Integer.parseInt(eElement.getAttribute("port")));
			sys.setOpenwirePort(Integer.parseInt(eElement.getAttribute("openwirePort")));
			sys.setMqttUserName(eElement.getAttribute("userName"));
			sys.setMqttPassword(eElement.getAttribute("password"));
			sys.setMqttQoSSubcribe(Integer.parseInt(eElement.getAttribute("qosSubcribe")));
			sys.setMqttTopics(eElement.getAttribute("topics"));
			sys.setOpenwireTopics(eElement.getAttribute("openwireTopics"));
			sys.setDbConnectionString(eElement.getAttribute("sqlConnectionString"));
			sys.setTimeAutoReconnect(Integer.parseInt(eElement.getAttribute("timeAutoReconnect")));
			sys.setMqttKeepAlive(Integer.parseInt(eElement.getAttribute("mqttKeepAlive")));
			sys.setClientId(eElement.getAttribute("clientId"));
			sys.setCleanSession(Integer.parseInt(eElement.getAttribute("cleanSession")));
			sys.setUnSubcribeAffterDisconnect(Integer.parseInt(eElement.getAttribute("unSubcribeAffterDisconnect")));
			sys.setMaxThreadNum(Integer.parseInt(eElement.getAttribute("maxThreadNum")));
			sys.setMinThreadNum(Integer.parseInt(eElement.getAttribute("minThreadNum")));
			sys.setThreadKeepAlive(Integer.parseInt(eElement.getAttribute("threadKeepAlive")));

			// sql info
			sys.setSqlUserName(eElement.getAttribute("sqlUserName"));
			sys.setSqlPassword(eElement.getAttribute("sqlPassword"));
			sys.setSqlDefaultAutoCommit(eElement.getAttribute("sqlDefaultAutoCommit").toUpperCase().equals("TRUE"));
			sys.setSqlPoolInitSize(Integer.parseInt(eElement.getAttribute("sqlPoolInitSize")));
			sys.setSqlMinIdl(Integer.parseInt(eElement.getAttribute("sqlMinIdle")));
			sys.setSqlMaxIdle(Integer.parseInt(eElement.getAttribute("sqlMaxIdle")));
			sys.setSqlQueryTimeOut(Integer.parseInt(eElement.getAttribute("sqlQueryTimeOut")));
			sys.setSqlLoginTimeout(Integer.parseInt(eElement.getAttribute("sqlLoginTimeout")));
			sys.setSqlMaxActive(Integer.parseInt(eElement.getAttribute("sqlMaxActive")));
			
			sys.setSaveMessage(eElement.getAttribute("isSaveMessage").toUpperCase().equals("TRUE"));
			sys.setLoadMessage(eElement.getAttribute("isLoadMessage").toUpperCase().equals("TRUE"));
			sys.setSqlReConnectionCount(Integer.parseInt(eElement.getAttribute("sqlReConnectionCount")));
			SessionEntity.setMaxLostCount(sys.getSqlReConnectionCount());
			
			SessionEntity.setTimeInsertData(Integer.parseInt(eElement.getAttribute("timeInsertData"))*1000);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sys;
	}
}
