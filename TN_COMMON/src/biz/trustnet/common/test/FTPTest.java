/*
 * Prodject Name	:   TN_COMMON
 * File Name		:	FTPTest.java
 * Date				:	2006. 1. 11 - 오전 2:46:59
 * History			:	2006. 1. 11
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net (임주섭)
 * Comment      	:
 */

package biz.trustnet.common.test;
import java.io.File;
import biz.trustnet.common.comm.ftp.FTP;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.log.Log;
/**
 * @author 임주섭
 *
 */
public class FTPTest {
	public FTPTest(){
		getFile();
	}

	public boolean moveFile(String destFile){

		String FTP_ADDR = "211.115.112.250";
		String FTP_USER = "administrator";
		String FTP_PWD  = "dlawntjq";
		String FTP_MOVE_FOLDER ="bakdat";
		String FTP_GET_FOLDER ="snddat";


		boolean result = false;

		FTP ftp = new FTP();
		ftp.setServer(FTP_ADDR);
		ftp.setUser(FTP_USER,FTP_PWD);
		if(ftp.login()){
			Log.debug("log.root","E>>H CONNECT",this);
		}else{
			Log.debug("log.root","E>>H DISCONNECT",this);
		}
		destFile = destFile.replaceAll(";","");
		ftp.setFolder(FTP_MOVE_FOLDER);
		ftp.setBinaryTransfer();
		if(ftp.putFile(new File(destFile))){
			Log.debug("log.root","E>>H MOVE FILE UPLOCAD COMPLETE",this);
			ftp.changeToParentDirectory();
			ftp.changeWorkingDirectory(FTP_GET_FOLDER);

			if(ftp.deleteFile(destFile.substring(destFile.indexOf("CHS"),destFile.length()))){
				Log.debug("log.root","E>>H PREVIOUS FILE DELETE COMPLETE",this);
				Log.debug("log.root","삼성 택배 요구 사항 수렴 완료",this);
				result = true;
			}else{
				Log.debug("log.root","E>>H PREVIOUS FILE DELETE FAILURE",this);
			}
		}else{
			Log.debug("log.root","E>>H MOVE FILE UPLOCAD FAILURE",this);
		}


		ftp.logout();
		return result;

	}

	public void getFile(){
		String FTP_USER 		= "taxsave";
		String FTP_PWD  		= "taxsave123";
		String FTP_ADDR 		= "210.98.159.189";
		String FTP_GET_FOLDER 	= "snddat";
		String FTP_PUT_FOLDER 	= "rcvdat";
		String FTP_STORAGE 		= "D:\\src.ginaida.net\\data";
		String FTP_MOVE_FOLDER  = "/users/interface/taxsave/bakdat";

		FTP ftp = new FTP();
		ftp.setServer(FTP_ADDR);
		ftp.setUser(FTP_USER,FTP_PWD);
		ftp.setASCIITransfer();
		//ftp.setBinaryTransfer();
		if(ftp.loginUNIX()){
			Log.debug("log.root","FTP 접속성공",this);
		}else{
			Log.debug("log.root","FTP 접속실패",this);
		}
		ftp.setFolder(FTP_GET_FOLDER);

		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		try{
		System.out.println("DOWN ="+ftp.downloadFile("KE20060116124353.dat","D:\\KE20060116124353.dat"));
		}catch(Exception e){System.out.println(e.getMessage());}

		System.out.println(ftp.listNames(""));
		System.out.println(ftp.listNames(FTP_GET_FOLDER));
		ftp.changeToParentDirectory();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.setBinaryTransfer();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.setTextTransfer();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.changeToParentDirectory();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.setBinaryTransfer();
		try{
		ftp.downloadFile("KE20060116124353.dat","D:\\KE20060116124353.dat");
		}catch(Exception e){System.out.println(e.getMessage());}
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.setTextTransfer();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.changeWorkingDirectory("snddat");
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		try{
		ftp.downloadFile("KE20060116124353.dat","D:\\KE20060116124353.dat");
		}catch(Exception e){System.out.println(e.getMessage());}
		ftp.setBinaryTransfer();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.setTextTransfer();
		Log.debug("log.root","FTP="+ftp.getFileNameList(),this);
		ftp.changeToParentDirectory();
		Log.debug("log.root","FTP="+ftp.getFileList(FTP_GET_FOLDER),this);

		ftp.logout();
		/**
		//시간 지정. 해당일 01~07
		int year = CommonUtil.parseInt(CommonUtil.getCurrentDate("yyyy"));
		int month= CommonUtil.parseInt(CommonUtil.getCurrentDate("MM"))-1;
		int date = CommonUtil.parseInt(CommonUtil.getCurrentDate("dd"));
		Calendar start 	= Calendar.getInstance();;
		Calendar end	= Calendar.getInstance();;
		start.set(year,month,date,GET_START,00);
		end.set(year,month,date,GET_END,00);
		**/
/*
		ftp.setDestinationFolder(FTP_STORAGE+File.separator+"getke");
		Vector vFile = ftp.saveLastFile();
		ftp.logout();

		if(vFile.size() == 0){
			cTrn.setGetMsg("FTP 접속 성공 ["+"파일 없음]");
			cTrn.setFileName("");
		}else{
			cTrn.setGetMsg("FTP 접속 성공 ["+"파일 있음]");
			String fileName = "";
			for(int i=0 ;i<vFile.size();i++){
				fileName += FTP_STORAGE +"\\getke\\"+(String)vFile.get(i)+";";
			}
			cTrn.setFileName(fileName);
		}
**/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		biz.trustnet.common.test.FTPTest ftp = new biz.trustnet.common.test.FTPTest();


	}

}
