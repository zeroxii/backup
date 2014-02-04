package skycom.vms.mail;

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
import javax.mail.internet.MimeUtility;

import skycom.vms.bean.MailBean;

public class EMail {
	//private EMailConfigBean configBean 	= null;
	private Message msg 				= null;					
	private Session session 			= null;						
	private Multipart multipart 		= new MimeMultipart();
	private MailBean configBean 	= null;
	public EMail(MailBean mBean) {
		this.configBean = mBean;
		setConfiguration();
	}
	
	public void setConfiguration() {
		//configBean = (EMailConfigBean)XMLFactory.getEntity(instance);
		//if(configBean.getMailCharSet().equals("")){
		//	configBean.setMailCharSet("euc-kr");
		//}
		Properties props = System.getProperties();
		props.put("mail.smtp.host", configBean.getSmtpHost());	
		props.put("mail.host", configBean.getDefaultHost());
		props.put("mail.from", configBean.getDefaultHost());
		
		if(configBean.getSendAuth().equalsIgnoreCase("Y")){			
			props.put("mail.smtp.auth","true");						
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
			//props.setProperty("mail.smtp.starttls.enable","true");
			//props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");		
			props.put("mail.smtp.port", configBean.getSmtpPort());
		}	
		session = Session.getDefaultInstance(props,null);		
		msg = new MimeMessage(session);	
	}
	public void setFrom(String from,String name)throws Exception{
		//if(!from.equals("")){
		//	configBean.setDefualtSender(from);
		//}
		msg.setFrom(new InternetAddress(configBean.getDefaultUser(),name,configBean.getCharset()));//sender
		msg.setDescription(configBean.getDefaultHost());
		msg.setHeader("X-Mailer", "skycom.ne.kr");

	}
	public void setToSubject(String to,String subject)throws Exception {
		Address[] address = { new InternetAddress(to) };
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to,false));
		msg.setSubject(subject);
	}
	public void setText(String textMessage)throws Exception {
		MimeBodyPart part1 = new MimeBodyPart();
		part1.setText(textMessage,configBean.getCharset());		
		multipart.addBodyPart(part1);
	}
	public void setAppendFile(MailBean mBean)throws Exception {
		if(mBean.getFileflag_yn().equals("Y")){
			if(mBean.getUploadFile() != null){
				MimeBodyPart mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(mBean.getUploadFile());
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(MimeUtility.encodeText(fds.getName(), "KSC5601", "B"));
				multipart.addBodyPart(mbp);
			}
		}
	}
	public void sendEMail()throws Exception {
		msg.setSentDate(new java.util.Date());					
		msg.setContent(multipart);								
		Transport transport = session.getTransport("smtp");
		transport.connect(configBean.getSmtpHost(),Integer.parseInt(configBean.getSmtpPort()),configBean.getDefaultUser(),configBean.getDefaultPass());
		transport.sendMessage(msg,msg.getAllRecipients());
		//transport.send(msg);
		transport.close();
	}
}
