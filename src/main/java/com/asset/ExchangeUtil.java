package com.asset;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExchangeUtil {
	private HashMap<String, Double> ratesMap = new HashMap<String, Double>();
	
	public void getExchangeRates() throws Exception {

		URL url = new URL("http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote");
		URLConnection connection = url.openConnection();

		Document doc = parseXML(connection.getInputStream());
		NodeList descNodes = doc.getElementsByTagName("resource");
		System.out.println("dddddddddddddddddddd  " + descNodes.getLength());
		for (int i = 0; i < descNodes.getLength(); i++) {
			System.out.println("//start node ");
			Node nNode = descNodes.item(i);
			/*
			 * System.out.println("\nCurrent Element :" + nNode.getNodeName());
			 * System.out.println(nNode.getTextContent());
			 */

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				NodeList descNodes1 = eElement.getElementsByTagName("field");

//				System.out.println("dddddddddddddddddddd  " + descNodes1.getLength());

				if (descNodes1.getLength() >= 2) {
					Node nNode1 = null;
					Node nNode2 = null;

					if (descNodes1.getLength() == 7) {
						nNode1 = descNodes1.item(0);
						nNode2 = descNodes1.item(1);
					}

					if (descNodes1.getLength() == 9) {
						nNode1 = descNodes1.item(2);
						nNode2 = descNodes1.item(3);
					}

//					System.out.println(nNode1.getTextContent());
//					System.out.println(nNode2.getTextContent());
					ratesMap.put(nNode1.getTextContent(), Double.parseDouble(nNode2.getTextContent()));
//					System.out.println("//////////////");
				}
			}
//			System.out.println("//end node ");
		}
	}

	public void getExchangeRates(String jsonString) throws Exception {
		
		/*
		 * Convert JSON TO XML
		 */
		/*String xml = XML.toString(jsonString);
        ExchangeUtil ff = new ExchangeUtil();
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));*/
		
		JSONObject jsons = new JSONObject(jsonString);
		
		JSONArray resources = ((JSONObject) jsons.get("list")).getJSONArray("resources");
		for (int i = 0; i < resources.length(); i++) {
			JSONObject item = resources.getJSONObject(i);
			JSONObject resource = (JSONObject) item.get("resource");
			JSONObject fields = (JSONObject) resource.get("fields");
			String name = fields.getString("name");
			String price = fields.getString("price");
			ratesMap.put(name, Double.parseDouble(price));
		}
		
		JSONObject paramaters =(JSONObject) jsons.getJSONObject("paramaters");
//		for (Map.Entry<String, Double> entry : ratesMap.entrySet())
//		{
//		    System.out.println(entry.getKey() + " - " + entry.getValue());
//		}
	}

	private Document parseXML(InputStream stream) throws Exception {
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;
		try {
			objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

			doc = objDocumentBuilder.parse(stream);
		} catch (Exception ex) {
			throw ex;
		}

		return doc;
	}
	public HashMap<String,String> getParamaters(String jsonString){
		HashMap<String,String> result = new HashMap<String,String>();
		JSONObject jsons = new JSONObject(jsonString);
		JSONObject paramaters =(JSONObject) jsons.getJSONObject("paramaters");
		
		result.put("before",paramaters.getString("before"));
		result.put("after",paramaters.getString("after"));
		result.put("amount",paramaters.getString("amount"));
		
		return result;
	}
	public double convertCurrancy(String from, String to, double amount) throws Exception {
		double rateFrom = 0;
		double rateTo = 0;

		if (to.equals("USD")) {

			rateFrom = ratesMap.get("USD/" + from);
			return amount / rateFrom;

		}

		else if (from.equals("USD")) {
			rateTo = ratesMap.get("USD/" + to);
			return rateTo * amount;
		}

		else {
			rateFrom = ratesMap.get("USD/" + from);
			double USDvalue = amount / rateFrom;
			rateTo = ratesMap.get("USD/" + to);
			double wantedAmount = USDvalue * rateTo;
			return wantedAmount;
		}
	}
}
