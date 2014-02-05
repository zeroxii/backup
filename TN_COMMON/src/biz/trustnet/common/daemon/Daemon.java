/* 
 * Project Name : TN_COMMON
 * File Name	: Daemon.java
 * Date			: 2007. 08. 20
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} 임주섭
 * Comment      : TNDaemon 에 대한 클래스를 구현한 후 Daemon 에 해당 클래스의 위치 및 작동 간격을 설정하면 자동으로 데몬이 구동된다.
 */

package biz.trustnet.common.daemon;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;



public class Daemon extends Thread {
	private String DAEMONCONFIGNAME			= "DAEMONCONFIG";
	private String DEFAULT_INTERVAL			= "10000";
	private String DEFAULT_LOG				= "Y";
	private TNDaemon tn 					= null;
	private TNDaemonConfigBean  configBean 	= null;
	
	
	public Daemon(){
		if(getDaemonConfig()){
			daemonStart();
		}
	}
	
	public Daemon(String className,String interval){
		this(className,interval,"");
	}
	
	public Daemon(String className){
		this(className,"","");
	}
	
	public Daemon(String className,String interval,String executeLog){
		configBean = new TNDaemonConfigBean();
		configBean.setLoadClass(className);
		if(interval.equals("")){
			configBean.setInterval(DEFAULT_INTERVAL);
		}else{
			configBean.setInterval(interval);
		}
		
		if(executeLog.equals("")){
			configBean.setExecuteLog(DEFAULT_LOG);
		}else{
			configBean.setExecuteLog(executeLog);
		}
		daemonStart();
	}
	
	
	public boolean getDaemonConfig(){
		boolean isConfigLoad = false;
		try{
			configBean = (TNDaemonConfigBean)XMLFactory.getEntity(DAEMONCONFIGNAME);
			if(configBean != null){
				isConfigLoad = true;
			}
		}catch(Exception e){
			Log.debug("log.root","DAEMON LOAD CONFIG ERROR="+CommonUtil.getExceptionMessage(e),this);
		}
		return isConfigLoad;
	}
	
	
	public void daemonStart(){
		if(loadClass()){
			setDaemon(true);
			start();
			System.out.println("TRUSTMATE REALTIMEDAEMON START = "+ CommonUtil.getCurrentDate() +" Version = 1.1 , Update=2008-08-01");
			Log.debug("log.day","TRUSTMATE REALTIMEDAEMON START = "+ CommonUtil.getCurrentDate() +" Version = 1.1 , Update=2008-08-01",this);
			System.out.println("CLASSNAME =["+ configBean.getLoadClass()+"] INTERVAL=["+configBean.getInterval()+"]");
			Log.debug("log.day","CLASSNAME =["+ configBean.getLoadClass()+"] INTERVAL=["+configBean.getInterval()+"]",this);
			while(true){
				try { 
					sleep(CommonUtil.parseInt(configBean.getInterval()));
				} catch( InterruptedException e ) {
					Log.debug("log.root","REALTIMEDAEMON ERROR",this);
					Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
				}
			}
		}else{
			System.out.println("TRUSTMATE REALTIMEDAEMON START ERROR =["+configBean.getLoadClass()+"]");
			Log.debug("log.day","TRUSTMATE REALTIMEDAEMON START ERROR =["+configBean.getLoadClass()+"]",this);
		}
	}
	
	public boolean loadClass(){
		boolean isLoad = false;
		try{
			tn = (TNDaemon)Class.forName(configBean.getLoadClass().trim()).newInstance();
			isLoad = true;
		}catch(Exception e){
			e.printStackTrace();
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		return isLoad;
	}
	
	
	public void run(){   
        while(true) {   
            try {   
            	sleep(CommonUtil.parseLong(configBean.getInterval()));
            	if(configBean.getExecuteLog().equals("Y")){
            		Log.debug("log.day","DAEMON EXECUTE	="+tn.request(),this);
            	}else{
            		tn.request();
            	}
            	System.gc(); 
            } catch( Exception e ) {
            	Log.debug("log.root","DAEMON EXECUTE ERROR="+CommonUtil.getExceptionMessage(e),this);
			}
        }
    }
	
	
	public static void main(String[] args){
		if(args.length == 0){
			biz.trustnet.common.daemon.Daemon daemon = new biz.trustnet.common.daemon.Daemon();
			
		}else if(args.length == 1){
			biz.trustnet.common.daemon.Daemon daemon = new biz.trustnet.common.daemon.Daemon(args[0]);
		}else if(args.length == 2){
			biz.trustnet.common.daemon.Daemon daemon = new biz.trustnet.common.daemon.Daemon(args[0],args[1]);
		}else if(args.length == 3){
			biz.trustnet.common.daemon.Daemon daemon = new biz.trustnet.common.daemon.Daemon(args[0],args[1],args[2]);
		}else{
			System.out.println("biz.trustnet.common.daemon.Daemon 클래스이름 데몬구동간격 로그기록여부(Y,N)");
		}
	}
}
