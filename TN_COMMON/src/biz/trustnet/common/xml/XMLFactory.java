/* 
 * Project Name : TN_COMMON
 * File Name	: XMLFactory.java
 * Date			: 2007. 08. 17
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.util.FastHashMap;
import biz.trustnet.common.util.Property;

public class XMLFactory {

	
	private static FastHashMap fMap = null;
	
	
	public XMLFactory(){
	}
	
	
	public static FastHashMap getEntity(){
		if(fMap == null){
			createInstance();
		}
		return fMap;
	}
	
	public static Object getEntity(String name){
		if(fMap == null){
			createInstance();
		}
		return fMap.get(name);
	}
	
	public static void createInstance(){
		
		XMLHandler xml = new XMLHandler();
		CreateParser cParser = new CreateParser(xml);
		try{
			cParser.parse(Property.getTNLocation()+java.io.File.separator+"service.xml");
			
			
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),null);
		}
		fMap = xml.map;
	}
}
