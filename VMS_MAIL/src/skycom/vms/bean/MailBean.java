package skycom.vms.bean;

import java.io.File;

public class MailBean {
	private Integer idx;
	private String receive;
	private String status;
	private String subject;
	private String textarea;
	private String comment;
	
	
	private String smtpHost;
	private String smtpPort;
	private String sendAuth;
	private String defaultUser;	
	private String defaultPass;
	private String defaultHost;
	private String charset;
	private String useSSL;
	private String sslPort;
	
	private String fileflag_yn;
	private String filepath;
	private String filename;
	private File uploadFile;
	
	public MailBean() {
		// TODO Auto-generated constructor stub
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTextarea() {
		return textarea;
	}
	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public String getDefaultHost() {
		return defaultHost;
	}
	public void setDefaultHost(String defaultHost) {
		this.defaultHost = defaultHost;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
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
	public String getFileflag_yn() {
		return fileflag_yn;
	}
	public void setFileflag_yn(String fileflag_yn) {
		this.fileflag_yn = fileflag_yn;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	
	
}
