/* 
 * Project Name : TN_COMMON
 * File Name	: SAPPoolConfigBean.java
 * Date			: Jan 2, 2009
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :  
 */

package biz.trustnet.common.sap.conf;

public class SAPPoolConfigBean {

	private String poolName		= "";
    private String maxConnection= "";
    private String client		= "";
    private String userId		= "";
    private String password		= "";
    private String language		= "";
    private String hostName		= "";
    private String systemNumber	= "";
    private String gwHost		= "";
    private String gwService	= "";
    private String programId	= "";
    
    public SAPPoolConfigBean(){
    	
    }

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(String maxConnection) {
		this.maxConnection = maxConnection;
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

	public String getGwHost() {
		return gwHost;
	}

	public void setGwHost(String gwHost) {
		this.gwHost = gwHost;
	}

	public String getGwService() {
		return gwService;
	}

	public void setGwService(String gwService) {
		this.gwService = gwService;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}
    
    
}
