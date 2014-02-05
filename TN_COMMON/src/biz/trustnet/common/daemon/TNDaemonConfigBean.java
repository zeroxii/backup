/* 
 * Project Name : TN_COMMON
 * File Name	: TNDaemonConfigBean.java
 * Date			: 2008. 08. 01
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :  
 */

package biz.trustnet.common.daemon;

public class TNDaemonConfigBean {
	
	private String loadClass	= "";
	private String interval		= "";
	private String executeLog	= "";
	
	
	public TNDaemonConfigBean(){	
	}


	public String getLoadClass() {
		return loadClass;
	}


	public void setLoadClass(String loadClass) {
		this.loadClass = loadClass;
	}


	public String getInterval() {
		return interval;
	}


	public void setInterval(String interval) {
		this.interval = interval;
	}


	public String getExecuteLog() {
		return executeLog;
	}


	public void setExecuteLog(String executeLog) {
		this.executeLog = executeLog;
	}
	
	
	
}
