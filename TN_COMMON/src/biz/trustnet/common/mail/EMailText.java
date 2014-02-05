/* 
 * Project Name : TN_COMMON
 * File Name	: EMail.java
 * Date			: 2007. 08. 22
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} 임주섭
 * Comment      :  
 */

package biz.trustnet.common.mail;


import biz.trustnet.common.log.Log;
import biz.trustnet.common.mail.apache.SimpleEmail;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;


public class EMailText {
	
	
	private EMailConfigBean configBean 	= null;						//메일 구성값 
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
	
	//	보낸 사람 정보 입력;
	public void setFrom(String from,String name)throws Exception{
		if(!from.equals("")){
			configBean.setDefualtSender(from);
		}
		email.setFrom(from, name,configBean.getMailCharSet());
		email.setBounceAddress(from);	//메일 반송시 수신 이메일 
		
	}
	

	
	//메세지의 제목 저장.
	public void setToSubject(String to,String subject)throws Exception {
		email.addTo(to,"");
		email.setSubject(subject);
	}

	//텍스트 메세지 전송
	public void setText(String textMessage)throws Exception {
		email.addHeader("Content-Transfer-Encoding", "7bit");
		email.setContent(textMessage,"text/plain; charset="+configBean.getMailCharSet());
	}
	

	//메일 보내기..
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
