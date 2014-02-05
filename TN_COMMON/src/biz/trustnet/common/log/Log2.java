/*
 * Project Name	:   TN_COMMON
 * File Name		:	Log.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 2:22:34
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */


package biz.trustnet.common.log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;

import biz.trustnet.common.util.CommonUtil;

public final class Log2 {
	
	private static String fileName = "../logs/log.log";


	public  Log2() {
	}


	public void debug(String category,String msg,Throwable t) {
		debug(category,msg,t,null);
	}

	public  void debug(String category,String msg,Object ref) {
		debug(category,msg,null,ref);
	}

	public  void debug(String category,String msg,Throwable t,Object ref) {
		registLog(Level.FINE,category,msg,t,ref);
	}

	public  void info(String category,String msg,Throwable t) {
		info(category,msg,t,null);
	}

	public  void info(String category,String msg,Object ref) {
		info(category,msg,null,ref);
	}

	public  void info(String category,String msg,Throwable t,Object ref) {
		registLog(Level.INFO,category,msg,t,ref);
	}

	public  void warn(String category,String msg,Throwable t) {
		warn(category,msg,t,null);
	}

	public  void warn(String category,String msg,Object ref) {
		warn(category,msg,null,ref);
	}

	public  void warn(String category,String msg,Throwable t,Object ref) {
		registLog(Level.WARNING,category,msg,t,ref);
	}

	public  void registLog(Level level,String category,String msg,Throwable throwable,Object ref) {
		FileOutputStream file  = null;
		
		try{
			file = new FileOutputStream(new File(fileName),true);
			msg = CommonUtil.getCurrentDate("yy-MM-dd HH:mm:ss")+" ["+ref.getClass().getName()+"] : "+msg+"\n";
			file.write(msg.getBytes());
		}catch(Exception e){
			System.out.println("FfileHandler Error ="+e.getMessage());
		}
	}



}
