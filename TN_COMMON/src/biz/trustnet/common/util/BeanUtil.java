/* 
 * Project Name : TN_COMMON
 * File Name	: BeanUtil.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.util;

import java.beans.BeanInfo;
import java.beans.Expression;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import biz.trustnet.common.log.Log;

public class BeanUtil {

	
	public static ArrayList getBeanValue(Object object){
		ArrayList list = new ArrayList();
		try{
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (int i=0; i<pds.length; i++) {
	            Expression exp = new Expression(object,pds[i].getReadMethod().getName(),new Object[0]);
	 
	            if(!pds[i].getReadMethod().getName().equals("getClass")){
	            	list.add(CommonUtil.toString(exp.getValue()));
	            }
	        }
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),null);
		}
		return list;
	}
	
	public static String beanToString(Object object){
		StringBuffer sb = new StringBuffer();
		try{
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			sb.append("[CLASSNAME ="+object.getClass().toString()+"]");
			
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (int i=0; i<pds.length; i++) {
	            Expression exp = new Expression(object,pds[i].getReadMethod().getName(),new Object[0]);
	            
	            if(!pds[i].getReadMethod().getName().equals("getClass")){
	            	
	            	sb.append("["+pds[i].getReadMethod().getName()+"=");
	            	sb.append(CommonUtil.toString(exp.getValue()+"]"));
	            }
	        }
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),null);
		}
		return sb.toString();
	}
	
	public static String getBeanName(Object object){
		return object.getClass().getName();
	}
	

	
	public static Method[] getBeanMethod(Object object){
		Method[] m= object.getClass().getDeclaredMethods();
		return m;
	}
	
	public static Field[] getBeanField(Object object){
		Field[] f = object.getClass().getDeclaredFields();
		return f;
	}
	
	public static String[] getBeanFieldName(Object object){
		String[] fieldName = null;
		try{
			
			Field[] f = getBeanField(object);
			fieldName = new String[f.length];
			for(int i=0 ; i < f.length ; i++){
				fieldName[i] = f[i].getName();
			}
			
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),null);
		}
		return fieldName;
	}
	
	public static void setField(Object object,String fieldName,String fieldValue){

		try{
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			
			for (int i=0; i<pds.length; i++) {
				
				if(pds[i].getDisplayName().equals(fieldName)){
					Expression exp = new Expression(object,pds[i].getWriteMethod().getName(),new Object[]{fieldValue});
					exp.execute();
	            }
	        }
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),null);
		}
	}
	
	
}
