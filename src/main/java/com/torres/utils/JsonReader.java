package com.torres.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
  
  public static Map jsonToMap(JSONObject jsonObject) {
	Map<String,String> map = new HashMap<String,String>();
	String[] properties = JSONObject.getNames(jsonObject);
	for (int i = 0; i  < properties.length; i++){
		try {
			if ("geoLocation".equalsIgnoreCase(properties[i])) {
				map.put(properties[i], jsonObject.getJSONObject(properties[i]).getString("lat") + "," +jsonObject.getJSONObject(properties[i]).getString("lng") );
			}
			else {
				map.put(properties[i], jsonObject.getString(properties[i]));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return map;
  }
}
