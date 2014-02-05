/*
 * Project Name : TN_COMMON
 * Project      : ARSJCO
 * File Name    : com.lsyesco.conf.SAPConfigBean.java
 * Date	        : Nov 26, 2008
 * Version      : 1.0
 * Author       : ginaida@empal.com
 * Comment      :
 */

package biz.trustnet.common.sap.conf;

public class SAPConfigBean {
	private String client 			= "";
	private String userId			= "";
	private String password			= "";
	private String language			= "";
	private String hostName			= "";
	private String systemNumber		= "";
	private String maxConnection	= "";


	public SAPConfigBean(){
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getSystemNumber() {
		return systemNumber;
	}

	public void setSystemNumber(String systemNumber) {
		this.systemNumber = systemNumber;
	}

	public String getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(String maxConnection) {
		this.maxConnection = maxConnection;
	}


}
