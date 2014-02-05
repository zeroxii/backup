/* 
 * Project Name : TN_COMMON
 * File Name	: FTP.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} 임주섭
 * Comment      :  
 */

package biz.trustnet.common.comm.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

import biz.trustnet.common.io.FileUtil;
import biz.trustnet.common.log.Log;

public class FTP {

	private String server= "";
	private int port = 21;
	private String password = "";
	private String username = "";
	private String folder = "";
	private String destinationFolder = "";



	FTPClient ftp = null;

	public FTP(){
	}

	public void setServer(String server){
		this.server = server;
	}

	public void setServerUNIX(){
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);

    	ftp.configure(conf);

	}
	
	public void setPort(int port){
		this.port = port;
	}

	public void setUser(String username , String password){
		this.username = username;
		this.password = password;
	}

	public void setFolder(String folder){
		this.folder = folder;
	}

	public void setDestinationFolder(String destinationFolder){
		this.destinationFolder = destinationFolder;
	}

	public void setPassiveMode(boolean setPassive) {
		if (setPassive)
			ftp.enterLocalPassiveMode();
		else
			ftp.enterLocalActiveMode();
	}



	public void setBinaryTransfer(){
		try{
			if(ftp != null){
				ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			}
		}catch(Exception e){
			Log.debug("log.root","SET BINARY_FILE_TYPE ERROR = "+e.getMessage(),this);
		}
	}

	public void setTextTransfer(){
		try{
			if(ftp != null){
				ftp.setFileType(org.apache.commons.net.ftp.FTP.NON_PRINT_TEXT_FORMAT);
			}
		}catch(Exception e){
			Log.debug("log.root","SET BINARY_FILE_TYPE ERROR = "+e.getMessage(),this);
		}
	}

	public void setASCIITransfer(){
		try{
			if(ftp != null){
				ftp.setFileType(org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE);
			}
		}catch(Exception e){
			Log.debug("log.root","SET ASCII_FILE_TYPE ERROR = "+e.getMessage(),this);
		}
	}

	public boolean changeWorkingDirectory(String folder){
		boolean result = false;
		try{
			if(!folder.equals("")){
				result = ftp.changeWorkingDirectory(folder);
				int i = ftp.listFiles().length;
				Log.debug("log.root","NOW WORKING DIRECTORY ["+Integer.toString(i)+"]" ,this);
				Log.debug("log.root","NOW WORKING DIRECTORY ["+ftp.printWorkingDirectory()+"]" ,this);
			}
		}catch(Exception e){
			Log.debug("log.root","changeWorkingDirectory Error "+e.getMessage(),this);
		}
		return result;
	}

	public boolean changeToParentDirectory(){
		boolean result = false;
		try{
			result = ftp.changeToParentDirectory();
			Log.debug("log.root","NOW WORKING DIRECTORY ["+ftp.printWorkingDirectory()+"]" ,this);
		}catch(Exception e){
			Log.debug("log.root","changeToParentDirectory Error "+e.getMessage(),this);
		}
		return result;
	}

	/**
	 * FOLDER 로 부터 파일리스트를 가져온다.
	 * @return
	 */
	public FTPFile[] getFileList(){
		try{
			changeWorkingDirectory( folder );
			return ftp.listFiles();
		}catch(Exception e){
			Log.debug("log.root","getFileList Error "+e.getMessage(),this);
			return null;
		}
	}

	public String getFileList(String directory){
		StringBuffer sb = new StringBuffer();
		try{
			FTPFile[] files = ftp.listFiles(directory);
			for(int i=0;i <files.length;i++){
			sb.append("FILE LIST "+ new String[i]);
			sb.append("FILE NAME = "+files[i].getName()+"\t");
			sb.append("FILE SIZE = "+files[i].getSize()+"\t");
			sb.append("FILE TYPE = "+files[i].getType()+"\t\n");
		}
		}catch(Exception e){
			Log.debug("log.root","getFileList Error "+e.getMessage(),this);
			return null;
		}
		return sb.toString();
	}


	public String getFileNameList(){
		StringBuffer sb = new StringBuffer();
		FTPFile[] files = getFileList();
		for(int i=0;i <files.length;i++){
			sb.append("FILE LIST "+ new String[i]);
			sb.append("FILE NAME = "+files[i].getName()+"\t");
			sb.append("FILE SIZE = "+files[i].getSize()+"\t");
			sb.append("FILE TYPE = "+files[i].getType()+"\t\n");
		}

		return sb.toString();
	}


	public boolean saveFileList(FTPFile[] files){
		try{
			for( int i=0; i<files.length; i++ ){
				this.saveFile(files[i]);
			}
		}catch(Exception e){
			Log.debug("log.root","FILE SAVE Error="+e.getMessage(),this);
			return false;
		}
		return true;
	}

	public String listNames(String directory){
		StringBuffer sb = new StringBuffer();
		try{
			String[] list = null;
			if(directory.equals("")){
				ftp.listNames();
			}else{
				ftp.listNames(directory);
			}
			for(int i=0;i <list.length;i++){
			sb.append("FILE LIST "+ new String[i]);
		}
		}catch(Exception e){
			Log.debug("log.root","listNames NLST Error="+e.getMessage(),this);
			return "";
		}

		return sb.toString();
	}

	/** Download a file from the server, and save it to the specified local file */
	public boolean downloadFile (String serverFile, String localFile)
			throws IOException, FTPConnectionClosedException {
		FileOutputStream out = new FileOutputStream(localFile);
		boolean result = ftp.retrieveFile(serverFile, out);
		out.close();
		return result;
	}

	/** Upload a file to the server */
	public boolean uploadFile (String localFile, String serverFile)
			throws IOException, FTPConnectionClosedException {
		FileInputStream in = new FileInputStream(localFile);
		boolean result = ftp.storeFile(serverFile, in);
		in.close();
		return result;
	}

	public boolean appendFile(String serverFile,String appendFile){
		File append_file = null;
		FileInputStream inputStream = null;
		boolean result = false;
		try{
			append_file = new File(appendFile);
    		inputStream = new FileInputStream(append_file);
    		result = ftp.appendFile(serverFile, inputStream);
    		inputStream.close();
    	}catch(Exception e){
    		Log.debug("log.root","APPENDFILE ERROR="+e.getMessage(),this);
    	}

    	return result;
    }

    public boolean rename(String oldName,String newName){
    	boolean result = false;
    	try{
    		Log.debug("log.root","RENAME OLD="+oldName+" NEW="+newName,this);
    		result = ftp.rename(oldName, newName);
    	}catch(Exception e){
    		Log.debug("log.root","RENAME ERROR="+e.getMessage(),this);
    	}
    	return result;
    }

    public boolean deleteFile(String deleteFile){
    	boolean result = false;
    	try{
    		result = ftp.deleteFile(deleteFile);
    	}catch(Exception e){
    		Log.debug("log.root","DELETE FILE ERROR="+e.getMessage(),this);
    	}
    	return result;
    }

    public boolean makeDirectory(String directory){
    	boolean result = false;
    	try{
    		result = ftp.makeDirectory(directory);
    	}catch(Exception e){
    		Log.debug("log.root","MAKE DIRECTORY ERROR="+e.getMessage(),this);
    	}
    	return result;
    }



	public boolean saveFile(FTPFile ffile){
		boolean saveStatus = false;
		try{
			File file = new File( destinationFolder + File.separator + ffile.getName() );
			FileOutputStream fos = new FileOutputStream( file );
			ftp.retrieveFile( ffile.getName(), fos );
			fos.close();
			saveStatus = true;
			Log.debug("log.root","Retrive & Save File NAME="+destinationFolder + File.separator + ffile.getName(),this);
		}catch(Exception e){
			Log.debug("log.root","Retrive & Save File Error NAME="+ffile.getName(),this);
			saveStatus = false;
		}
        return saveStatus;
	}

	public Vector saveSelectedFileList(Calendar start,Calendar end){
		Vector v = new Vector();
		FTPFile[] files = getFileList();
		if(files.length ==0){
			return v;
		}

		DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT );
		for( int i=0; i<files.length; i++ ){
			Date fileDate = files[ i ].getTimestamp().getTime();
			Log.debug("log.root","FILE CREATE TIME = "+ fileDate.toString(),this );
			if( fileDate.compareTo( start.getTime() ) >= 0 ){//&& fileDate.compareTo( end.getTime() ) <= 0 ){
				Log.debug("log.root","GET "+ files[i].getName() +"\t "+df.format(files[i].getTimestamp().getTime()),this);
				Log.debug("log.root","SAVE SELECTED FILELIST",this);
				saveFile(files[i]);
				v.add(files[i].getName());
			}
		}

		return v;

	}

	public Vector saveLastFile(){
		Vector v = new Vector();
		FTPFile[] files = getFileList();
		if(files.length ==0){
			try{
				Log.debug("log.root","NOW WORKING DIRECTORY ["+ftp.printWorkingDirectory()+"]" ,this);
				Log.debug("log.root","FTP FILE ISNOT EXIST!!" ,this);
			}catch(Exception e){}
			return v;
		}

		DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT );
		for( int i=0; i<files.length; i++ ){
			Log.debug("log.root","GET "+ files[i].getName() +"\t "+df.format(files[i].getTimestamp().getTime()),this);
			if(i == (files.length-1)){
				Log.debug("log.root","GET "+ files[i].getName() +"\t "+df.format(files[i].getTimestamp().getTime()),this);
				Log.debug("log.root","SAVE SELECTED FILELIST",this);
				saveFile(files[i]);
				v.add(files[i].getName());
			}

		}

		return v;

	}

	public boolean putFile(File file){
		boolean isSuccess = false;
		try{

			changeWorkingDirectory( folder );
			InputStream input = new FileInputStream(file);
			isSuccess = ftp.storeFile(file.getName(),input);
			if(input != null){
				input.close();
			}
		}catch(Exception e){
			Log.debug("log.root","PUT File Failed ="+e.getMessage(),this);
			
		}
		return isSuccess;
	}

	public boolean putFileList(){
		try{
			File[] files = new FileUtil().getFileList(destinationFolder);
			for(int i=0 ; i <files.length;i++){
				putFile(files[i]);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}


	public boolean login(){
		try{
			ftp = new FTPClient();
			ftp.connect( server ,port);
			ftp.login( username, password );
			Log.debug("log.root","FTP Connected to "+server,this);
			Log.debug("log.root","Reply Message = "+ftp.getReplyString()+ftp.getReplyCode(),this);
			return true;
		}catch(Exception e){
			Log.debug("log.root","Reply Message = "+ftp.getReplyString(),this);
			Log.debug("log.root","FTP Disconnected ,, Login Failed "+e.getMessage(),this);
			return false;
		}
	}

	public boolean loginUNIX(){
		try{
			ftp = new FTPClient();
			setServerUNIX();
			ftp.connect(server,port);
			ftp.login( username, password );
			Log.debug("log.root","FTP Connected to "+server,this);
			Log.debug("log.root","Reply Message = "+ftp.getReplyString()+ftp.getReplyCode(),this);

			FTPListParseEngine engine =
       		ftp.initiateListParsing();

    		while (engine.hasNext()) {
       		FTPFile[] files = engine.getNext(25);  // "page size" you want
	       		for(int i=0;i <files.length;i++){
				System.out.println("FILE LIST "+ new String[i]);
				System.out.println("FILE NAME = "+files[i].getName()+"\t");
				System.out.println("FILE SIZE = "+files[i].getSize()+"\t");
				System.out.println("FILE TYPE = "+files[i].getType()+"\t\n");
				}
			}



			return true;
		}catch(Exception e){
			Log.debug("log.root","Reply Message = "+ftp.getReplyString(),this);
			Log.debug("log.root","FTP Disconnected ,, Login Failed "+e.getMessage(),this);
			return false;
		}
	}

	public void logout(){
		try{
			ftp.logout();
			ftp.disconnect();
			Log.debug("log.root","FTP Disconnect ",this);
		}catch(Exception e){
			Log.debug("log.root","FTP Disconnect Error "+e.getMessage(),this);
		}
	}

	public static void main(String []args){
		biz.trustnet.common.comm.ftp.FTP ftp = new biz.trustnet.common.comm.ftp.FTP();
		ftp.setServer(args[0]);
		ftp.setUser(args[1],args[2]);
		ftp.login();
		System.out.println(ftp.getFileNameList());
		ftp.getFileList();
		ftp.logout();

	}

}