package skycom.vms.execute;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import skycom.vms.bean.MailBean;
import skycom.vms.common.GetProperties;
import skycom.vms.common.LoggerHelper;
import skycom.vms.dao.MailDAO;
import skycom.vms.mail.EMail;

public class MailDaemon {
	private static final Logger logger = LoggerHelper.getLogger();
	private MailDAO mDAO;
	private MailBean mBean;
	public MailDaemon() {
		mDAO = new MailDAO();				
	}	
	/**
	 * @param args
	 */
	public void execute(){
		
		List<MailBean> list = new ArrayList();
		
		boolean isSuccess = false;		
		try {
			list = mDAO.getMailList();
			logger.info("list size :: "+list.size());			
			if(list.size() > 0){				
				for (int i = 0; i < list.size(); i++) {					
					mBean = new MailBean();			
					mBean = (MailBean)list.get(i);
					
					
					StringBuffer sbf = new StringBuffer();					
					EMail m = new EMail(mBean);
					
					
					if(!appendFileCheck()){
						break;
					}					
					//memory.file_over
					sbf.append(mBean.getTextarea());
					m.setFrom(mBean.getDefaultUser(), GetProperties.getInstance().getMessageReturnCode("subheadline"));					
					m.setToSubject(mBean.getReceive(), mBean.getSubject());					
					m.setText(sbf.toString());
					m.setAppendFile(mBean);
					m.sendEMail();					
					isSuccess = true;
					if(isSuccess){
						isSuccess = false;
						mBean.setStatus("Y");
						mBean.setComment(GetProperties.getInstance().getMessageReturnCode("successMail"));
						if(update()){
							logger.info("[success] e-mail send idx["+mBean.getIdx()+"]");
						}
					}
					
					Thread.sleep(Integer.parseInt(GetProperties.getInstance().getMessageReturnCode("threadSleep")));					
					
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				mBean.setStatus("E");
				mBean.setComment(e.getMessage());
				if(update()){
					logger.info("[fail] e-mail send idx["+mBean.getIdx()+"]");
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}			
			
		}
	}
	public boolean appendFileCheck(){
		boolean flag = true;
		if(!mBean.getFileflag_yn().equals("Y")){
			return flag;
		}
		String file_path = mBean.getFilepath()+File.separator+mBean.getFilename();		
		File appendFile = new File(file_path);
		if(!appendFile.isFile()){
			mBean.setStatus("E");
			mBean.setComment("no file");
			if(update()){
				logger.info("[fail] no file");
			}
			flag = false;
			return flag;
		}
		long limit_size = Long.parseLong(GetProperties.getInstance().getMessageReturnCode("path.limit_size"));		
		long mb_size = limit_size*1024*1024;
		if(appendFile.length()>mb_size){
			//mBean.setStatus("E");
			//mBean.setComment("Too long size (Limit size is "+limit_size+"MB)");
			//if(update()){
				logger.info("[success] Too long size (Limit size is "+limit_size+"MB)");
			//}
			flag = true;
			mBean.setUploadFile(null);
		}else{
			mBean.setUploadFile(appendFile);
		}
		return flag;
	}
	public boolean update(){
		boolean flag = false;
		try {
			if(mDAO.update(mBean)){
				flag = true;				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}
	public static void main(String[] args) {
		skycom.vms.execute.MailDaemon daemon = new skycom.vms.execute.MailDaemon(); 
		System.out.println("MAIL DAEMON START!");
		daemon.execute();
	}
}
