/*
 * Project Name	:   TN_COMMON
 * File Name		:	Property.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 3:19:48
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:
 * Comment      	:
 */

package biz.trustnet.common.util;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;

import biz.trustnet.common.log.Log;



public class Property {

	Map map = null;
	String file_location = "";

	public Property(){
	}
	public Property(String file_location){
		map = new HashMap();
		this.file_location = file_location;
		load();
	}

	public void load(){
		PropertyResourceBundle props = null;
		try {
			props = new PropertyResourceBundle(new FileInputStream(file_location));
		}
		catch (Exception e) {
			Log.debug("log.root","PROPERTY FILE LOAD ERROR = ["+file_location+"]" , this);
		}
		Enumeration keyEnum = props.getKeys();
		while(keyEnum.hasMoreElements()){
			String key = (String)keyEnum.nextElement();
			map.put(key,props.getString(key.trim()).trim());
		 }
		props = null;
	}

	public Map getPropertiesMap(){
		return map;
	}

	public int getPropertiesInt(String key){
		int value = CommonUtil.parseInt((String)map.get(key));
		return value;
	}

	public String getProperties(String key){
		String value = (String)map.get(key);
		if(value == null){
			return "";
		}
		return value;
	}

	public String getSystemProperties(String key){
		String value = System.getProperty(key);
		if(value==null){
			return "";
		}else{
			return value;
		}
	}

	public static String getTNLocation(){
		String loc ="";
		try{


		String isWeb = CommonUtil.nToB(System.getProperty("webapp.root"));
		if(isWeb.equals("")){
			if(System.getProperty("java.version").startsWith("1.4") || System.getProperty("java.version").startsWith("1.3")){
				loc = System.getProperty("TN_ENV");
			}else{
				loc = System.getenv("TN_ENV");
				if(loc == null){
					loc = System.getProperty("TN_ENV");
				}
			}
		}else{
			loc = isWeb+"WEB-INF";
			System.setProperty("TN_ENV",loc);
		}
		}catch(Exception e){
			System.out.println("TN_ENV Location is not set! "+e.getMessage());
		}

		return loc;
	}

	public static void setTNLocation(String value){
		System.setProperty("TN_ENV",value);
	}

	public Map getSystemPropertiesMap(){
		if(map == null){
			map = new HashMap();
		}

		Enumeration propNames = System.getProperties().propertyNames();
		while(propNames.hasMoreElements()){
          String key = (String) propNames.nextElement();
          map.put(key,System.getProperty(key.trim()).trim());
		}
		return map;
	}

	public void viewSystemProperty(){
		map = getSystemPropertiesMap();

		StringBuffer sb = new StringBuffer();
		Iterator iter = map.keySet().iterator();
		String name = "";
		String value = "";
		while (iter.hasNext()) {
			name = (String) iter.next();
			value = (String) map.get(name);
			sb.append(name + "=" + value +"&");
		}
		Log.debug("log.root","VIEW ENV= ["+sb.toString()+"]" , this);
	}

	public void setSystemProperty(String key, String value){
		System.setProperty(key,value);
	}



}