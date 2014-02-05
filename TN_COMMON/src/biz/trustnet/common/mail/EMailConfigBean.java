/* 
 * Project Name : TN_COMMON
 * File Name	: EMailConfigBean.java
 * Date			: 2007. 08. 22
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.mail;

public class EMailConfigBean {

	
	private String smtpHost		= "";
	private String smtpPort		= "";
	private String sendAuth		= "";
	private String defaultUser	= "";
	private String defaultPass	= "";
	private String defualtSender= "";
	private String defaultHost	= "";
	private String mailCharSet	= "";
	private String useSSL		= "";
	private String sslPort		= "";
	
	
	
	public EMailConfigBean(){	
	}



	public String getSmtpHost() {
		return smtpHost;
	}



	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}



	public String getSmtpPort() {
		return smtpPort;
	}



	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}



	public String getSendAuth() {
		return sendAuth;
	}



	public void setSendAuth(String sendAuth) {
		this.sendAuth = sendAuth;
	}



	public String getDefaultUser() {
		return defaultUser;
	}



	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}



	public String getDefaultPass() {
		return defaultPass;
	}



	public void setDefaultPass(String defaultPass) {
		this.defaultPass = defaultPass;
	}



	public String getDefualtSender() {
		return defualtSender;
	}



	public void setDefualtSender(String defualtSender) {
		this.defualtSender = defualtSender;
	}



	public String getMailCharSet() {
		return mailCharSet;
	}



	public void setMailCharSet(String mailCharSet) {
		this.mailCharSet = mailCharSet;
	}



	public String getDefaultHost() {
		return defaultHost;
	}



	public void setDefaultHost(String defaultHost) {
		this.defaultHost = defaultHost;
	}



	public String getUseSSL() {
		return useSSL;
	}



	public void setUseSSL(String useSSL) {
		this.useSSL = useSSL;
	}



	public String getSslPort() {
		return sslPort;
	}



	public void setSslPort(String sslPort) {
		this.sslPort = sslPort;
	}
	
	
}
