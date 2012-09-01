package kr.swmaestro.hsb.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class JsonXmlUtil {

	public static String jsonToXml(String jsonStr) {
		XMLSerializer serializer = new XMLSerializer();
		JSON json = JSONSerializer.toJSON(jsonStr); 
		return serializer.write(json);
	}
	
	public static List<String> jsonListToXmlList(List<String> jsonList) {
		List<String> xmlList = new ArrayList<>();
		for (String json : jsonList) {
			xmlList.add(jsonToXml(json));
		}
		return xmlList;
	}
	
}
