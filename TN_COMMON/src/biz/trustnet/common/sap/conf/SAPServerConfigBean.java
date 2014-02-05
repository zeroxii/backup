/*
 * Project Name : TN_COMMON
 * File Name	: SAPServerConfigBean.java
 * Date			: Jan 2, 2009
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :
 */

package biz.trustnet.common.sap.conf;

public class SAPServerConfigBean {

	private String trace				= "0";
    private String unicode				= "1";
    private String maxStartupDelay		= "1";
    private String dsr					= "1";
    private String handleRequestClass	= "";
    private String poolName				= "";
    private String clientPoolName		= "";

    public SAPServerConfigBean(){

    }

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getUnicode() {
		return unicode;
	}

	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}

	public String getMaxStartupDelay() {
		return maxStartupDelay;
	}

	public void setMaxStartupDelay(String maxStartupDelay) {
		this.maxStartupDelay = maxStartupDelay;
	}

	public String getDsr() {
		return dsr;
	}

	public void setDsr(String dsr) {
		this.dsr = dsr;
	}

	public String getHandleRequestClass() {
		return handleRequestClass;
	}

	public void setHandleRequestClass(String handleRequestClass) {
		this.handleRequestClass = handleRequestClass;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getClientPoolName() {
		return clientPoolName;
	}

	public void setClientPoolName(String clientPoolName) {
		this.clientPoolName = clientPoolName;
	}


}
