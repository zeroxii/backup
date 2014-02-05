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


import biz.trustnet.common.log.Log;
import biz.trustnet.common.mail.apache.SimpleEmail;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;


public class EMailText {
	
	
	private EMailConfigBean configBean 	= null;						//���� ������ 
	private SimpleEmail email = new SimpleEmail();
	
	public EMailText() {
		setConfiguration("EMAIL");
	}
	
	public EMailText(String instance){
		setConfiguration(instance);
	}
	
	public void setConfiguration(String instance) {
		configBean = (EMailConfigBean)XMLFactory.getEntity(instance);
		try{
			//iso-2022-jp
			email.setHostName(configBean.getSmtpHost());
			email.setSmtpPort(CommonUtil.parseInt(configBean.getSmtpPort()));
			email.setCharset(configBean.getMailCharSet());
			if(configBean.getSendAuth().equalsIgnoreCase("Y")){
				email.setAuthentication(configBean.getDefaultUser(),configBean.getDefaultPass());
			}
			
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	//	���� ��� ���� �Է�;
	public void setFrom(String from,String name)throws Exception{
		if(!from.equals("")){
			configBean.setDefualtSender(from);
		}
		email.setFrom(from, name,configBean.getMailCharSet());
		email.setBounceAddress(from);	//���� �ݼ۽� ���� �̸��� 
		
	}
	

	
	//�޼����� ���� ����.
	public void setToSubject(String to,String subject)throws Exception {
		email.addTo(to,"");
		email.setSubject(subject);
	}

	//�ؽ�Ʈ �޼��� ����
	public void setText(String textMessage)throws Exception {
		email.addHeader("Content-Transfer-Encoding", "7bit");
		email.setContent(textMessage,"text/plain; charset="+configBean.getMailCharSet());
	}
	

	//���� ������..
	public void sendEMail()throws Exception {
		Log.debug("log.root",email.send(),this);
	}

	public static void main(String[] args) {
		try {
			EMailText m = new EMailText();
			m.setToSubject(args[0],args[1]);
			m.setText(args[2]);
			m.setFrom("info@kt-trust.com","KT.Trust Test");
			m.sendEMail();
		
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
