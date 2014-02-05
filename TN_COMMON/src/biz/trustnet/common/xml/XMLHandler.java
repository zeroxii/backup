/* 
 * Project Name : TN_COMMON
 * File Name	: XMLHandler.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.util.FastHashMap;

public class XMLHandler extends DefaultHandler{
	 
	public static FastHashMap map = null;
	boolean isStart = false;
	private Object object = null;
	private String eleValue= "";
	private String eleName = "";
	private String objName = "";
	
	
	public XMLHandler(){
		map = new FastHashMap();
	}
	
	public void startElement(String uri, String localName, String name,Attributes attributes) throws SAXException {
		
		if(name.equals("entity")){
			isStart = true;
			for(int i = 0 ; i < attributes.getLength() ; i++){
				
				if(attributes.getQName(i).equals("class")){
					try{
						object = Class.forName(attributes.getValue(i)).newInstance();
					}catch(Exception e){
						Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
					}
				}
				if(attributes.getQName(i).equals("name")){
					objName = attributes.getValue(i);
				}
			}
		}
		eleName = name;
	}
	
	public void endElement(String uri, String localName, String name) throws SAXException {
		
		if(name.equals("entity")){
			map.put(objName,object);
			isStart = false;
		}
		if(isStart){
			BeanUtil.setField(object,eleName,eleValue);
		}
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(length ==0){
			eleValue = "";
		}else{
			eleValue = CommonUtil.toString(new String(ch,start,length)).trim();
		}
	}
	
	
}
