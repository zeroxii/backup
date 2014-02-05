/*
 * Project Name	:   TN_COMMON
 * File Name		:	Log.java
 * Date				:	2005. 3. 31. - 오후 2:22:34
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */

package biz.trustnet.common.log;


import org.apache.log4j.PropertyConfigurator ;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;

import biz.trustnet.common.util.Property;

public final class Log {

	private static Log logInstance = null;
	private final static String DEFAULT_LOG_FILE = Property.getTNLocation()+java.io.File.separator+"log4j.properties";



	/**
	 * 생성자 Log4j 설정 파일을 읽어 Logging 정보를 설정한다.
	 */
	private Log() {
		this(DEFAULT_LOG_FILE);
	}

	private Log(String path) {
		try{
		PropertyConfigurator.configure(path);
		}catch(Exception e){
			System.out.println(e.getMessage());
			PropertyConfigurator.configure("../conf/log4j.properties");
		}
	}

	/**
	 * 처음 Log객체가 존재하지 않으면 객체를 생성한다. 만약 생성되었있다면 다시 생성하지않고 재 사용한다.
	 * @return logInstace 객체
	 */
	public static Log getInstance(){
		if (logInstance == null) {
			logInstance = new Log();
		}
		return logInstance;
	}

	public static void debug(String category,String msg,Throwable t) {
		debug(category,msg,t,null);
	}

	public static void debug(String category,String msg,Object ref) {
		debug(category,msg,null,ref);
	}

	public static void debug(String category,String msg,Throwable t,Object ref) {
		registLog(Level.DEBUG,category,msg,t,ref);
	}

	public static void info(String category,String msg,Throwable t) {
		info(category,msg,t,null);
	}

	public static void info(String category,String msg,Object ref) {
		info(category,msg,null,ref);
	}

	public static void info(String category,String msg,Throwable t,Object ref) {
		registLog(Level.INFO,category,msg,t,ref);
	}

	public static void warn(String category,String msg,Throwable t) {
		warn(category,msg,t,null);
	}

	public static void warn(String category,String msg,Object ref) {
		warn(category,msg,null,ref);
	}

	public static void warn(String category,String msg,Throwable t,Object ref) {
		registLog(Level.WARN,category,msg,t,ref);
	}

	public static void registLog(Level level,String category,String msg,Throwable throwable,Object ref) {
		if(logInstance == null){
			Log.getInstance();
		}
		Logger logger = LogManager.exists(category);

		if(logger == null){
			logger = LogManager.getRootLogger();
		}
		if(ref != null) {
			   msg = "[" + ref.getClass().getName()+ "]## "+ msg;
		}

		if (level == Level.DEBUG) {
			logger.error("[DEBUG]+"+msg,throwable);
		} else if (level == Level.INFO) {
			logger.info("[INFO]+"+msg,throwable);
		} else if(level == Level.WARN){
			logger.debug("[WARN]+"+msg,throwable);
		}
	}



}
