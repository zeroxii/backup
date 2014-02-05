/* 
 * Project Name : TN_COMMON
 * File Name	: URLEncDec.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import biz.trustnet.common.log.Log;


public class URLEncDec {

	private static String MIME_FORMAT = "euc-kr";

	public URLEncDec(){}
	
	public static void setType(String format){
		MIME_FORMAT = format;
	}
	
	public static String encode(String str){
		
		if(str == null || str.equals(""))
			return str;
		else{
			String encodedStr = "";
			try{
				encodedStr = URLEncoder.encode(str,MIME_FORMAT);
			}catch(Throwable t){
				Log.warn("log.root",MIME_FORMAT+" ENCODER ERROR",t);
			}
			return encodedStr;
		}
	}
	
	public static String encode(String str,String format){
		
		if(str == null || str.equals(""))
			return str;
		else{
			String encodedStr = "";
			try{
				encodedStr = URLEncoder.encode(str,format);
			}catch(Throwable t){
				Log.warn("log.root",format+" ENCODER ERROR",t);
			}
			return encodedStr;
		}
	}
	
	public static String decode(String str){
		if(str == null || str.equals(""))
			return str;
		else{
			String decodedStr = "";
			try{
				decodedStr = URLDecoder.decode(str,MIME_FORMAT);
			}catch(Throwable t){
				Log.warn("log.root",MIME_FORMAT+" DECODER ERROR",t);
			}
			return decodedStr;
		}
	}
	
	public static String decode(String str,String format){
		if(str == null || str.equals(""))
			return str;
		else{
			String decodedStr = "";
			try{
				decodedStr = URLDecoder.decode(str,format);
			}catch(Throwable t){
				Log.warn("log.root",format+" DECODER ERROR",t);
			}
			return decodedStr;
		}
	}


}
