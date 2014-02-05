/* 
 * Project Name : TN_COMMON
 * File Name	: EMail.java
 * Date			: 2007. 08. 22
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ���ּ�
 * Comment      :  
 */

package biz.trustnet.common.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;


public class EMail {
	
	
	private EMailConfigBean configBean 	= null;						//���� ������ 
	private Message msg 				= null;						//�޼��� Ŭ������ ��ü �Ҵ�
	private Session session 			= null;						//�޼��� ���� ��ü �Ҵ�
	private Multipart multipart 		= new MimeMultipart();		//÷��ȭ��÷���� ��Ƽ ��Ʈ���·� �޼��� ����
	
	
	public EMail() {
		setConfiguration("EMAIL");
	}
	
	public EMail(String instance){
		setConfiguration(instance);
	}
	
	public void setConfiguration(String instance) {
		configBean = (EMailConfigBean)XMLFactory.getEntity(instance);
		if(configBean.getMailCharSet().equals("")){
			configBean.setMailCharSet("euc-kr");
		}
		Properties props = System.getProperties();
		props.put("mail.smtp.host", configBean.getSmtpHost());	
		props.put("mail.host", configBean.getDefaultHost());
		props.put("mail.from", configBean.getDefaultHost());
		
		if(configBean.getSendAuth().equalsIgnoreCase("Y")){
			props.put("mail.smtp.auth","true");						//������ SMTP ����
		}
		
		if(configBean.getUseSSL().equalsIgnoreCase("Y")){
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			
			props.setProperty("mail.smtp.starttls.enable","true");
			props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			if(configBean.getSslPort().equals("")){
				props.setProperty("mail.smtp.socketFactory.port", "465");
			}else{
				props.setProperty("mail.smtp.socketFactory.port",configBean.getSslPort());
			}
		}else{
			props.put("mail.smtp.port", configBean.getSmtpPort());
		}
	
		session = Session.getDefaultInstance(props,null);
		
		msg = new MimeMessage(session);//���� �޼��� ������ ���� ���� �־���	
	}
	
//	���� ��� ���� �Է�;
	public void setFrom(String from,String name)throws Exception{
		if(!from.equals("")){
			configBean.setDefualtSender(from);
		}
		msg.setFrom(new InternetAddress(configBean.getDefualtSender(),name,configBean.getMailCharSet()));
		msg.setDescription(configBean.getDefaultHost());
		msg.setHeader("X-Mailer", "KT.Trust");

	}
	

	
	//�޼����� ���� ����.
	public void setToSubject(String to,String subject)throws Exception {
		Address[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to,false));
		msg.setSubject(subject);
	}

	//�ؽ�Ʈ �޼��� ����
	public void setText(String textMessage)throws Exception {
		// �Ϲ� �ؽ�Ʈ
		MimeBodyPart part1 = new MimeBodyPart();
		part1.setText(textMessage,configBean.getMailCharSet());
		multipart.addBodyPart(part1);
	}
	
	//HTML �޼��� ����.
	public void setHtml(String HTMLMessage)throws Exception {
		// ������ MIME ���� (HTML ����)
		MimeBodyPart part2 = new MimeBodyPart();
		part2.setDataHandler(new DataHandler( new ByteArrayDataSource(HTMLMessage.getBytes(),"text/html; charset="+configBean.getMailCharSet(), null)));
		multipart.addBodyPart(part2);
	}
	
	// File ÷�ν� ����.
	public void setFile(String FileName) throws Exception {
		MimeBodyPart part3 = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(FileName);
		part3.setDataHandler(new DataHandler(fds));
		part3.setFileName(new String(fds.getName().getBytes(),"iso-8859-1"));
		multipart.addBodyPart(part3);
	}

	//���� ������..
	public void sendEMail()throws Exception {
		msg.setSentDate(new java.util.Date());					//������ ��¥ �Է�
		msg.setContent(multipart);								//���� �޼����� �Է�
		Transport transport =session.getTransport("smtp");
		transport.connect(configBean.getSmtpHost(),CommonUtil.parseInt(configBean.getSmtpPort()),configBean.getDefaultUser(),configBean.getDefaultPass());
		transport.sendMessage(msg,msg.getAllRecipients());
		transport.close();
	}

	public static void main(String[] args) {
		try {
			biz.trustnet.common.mail.EMail m = new biz.trustnet.common.mail.EMail();
			m.setToSubject("ginaida@trustnet.biz","TEXT , biz.trustnet.common.email.EMail");
			m.setText("�׽�Ʈ�޼���");
			m.sendEMail();
			m.setToSubject("ginaida@trustnet.biz","HTML , biz.trustnet.common.email.EMail");
			m.setText("<html><head>����</head><body>������� </body></html>");
			m.sendEMail();
		
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
