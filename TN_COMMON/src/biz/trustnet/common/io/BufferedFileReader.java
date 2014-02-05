/*
 * Project Name	:   TN_COMMON
 * File Name		:	BufferedFileReader.java
 * Date				:	¿ÀÈÄ 8:31:30
 * History			:	&{date}
 * Version			:	1.0
 * Author			:   ÀÓÁÖ¼·
 * Comment      	:
 */
package biz.trustnet.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import biz.trustnet.common.log.Log;


public class BufferedFileReader {



	public BufferedFileReader(){
	}

	public StringBuffer getStringBuffer(String fileLocation){
		return read(fileLocation);
	}

	public String getString(String fileLocation){
		return read(fileLocation).toString();
	}

	public StringBuffer read(String fileName){

		StringBuffer file = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));

			String str;

			while ((str = in.readLine()) != null) {
				if(str != null){
					if(!str.trim().equals("")){
						file.append(str);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			Log.debug("log.root","FILE READ ERROR = "+e.getMessage(),this);
			return new StringBuffer(e.getMessage());
		}
		return file;
	}

	public static List getLineFromFile(String fileName)throws IOException{
		List list = new ArrayList();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));

			String str;
			while ((str = in.readLine()) != null) {
				if(str != null){
					if(!str.trim().equals("")){
						list.add(str);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			Log.debug("log.root","FILE READ ERROR = "+e.getMessage(),null);
		}

		return list;
	}

	public static List getLineFromFile(String fileName,String charsetName)throws IOException{
		List list = new ArrayList();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));

			String str;
			while ((str = in.readLine()) != null) {
				if(str != null){
					if(!str.trim().equals("")){
						list.add(str);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			Log.debug("log.root","FILE READ ERROR = "+e.getMessage(),null);
		}

		return list;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		 InputStream is = new FileInputStream(file);

		 long length = file.length();

		 if (length > Integer.MAX_VALUE) {
			 throw new IOException("excess Integer.MAX_VALUE");
		 }

		 byte[] bytes = new byte[(int)length];

		 int offset = 0;
		 int numRead = 0;
		 while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			 offset += numRead;
		 }

		 if (offset < bytes.length) {
			 throw new IOException("Could not completely read file "+file.getName());
		 }

		 is.close();
		 return bytes;
	 }


	 public static byte[] getBytesFromFile(String fileName)throws IOException{
			return getBytesFromFile(new File(fileName));
	 }



	 public static StringBuffer getEncodedStrFormFile(String fileName,String charsetName)throws IOException{
		StringBuffer file = new StringBuffer();
		try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));

			String str;
		   	while ((str = in.readLine()) != null) {
			   file.append(str);
		   	}
		 } catch (UnsupportedEncodingException e) {
		 } catch (IOException e) {
		 }

		return file;
	 }

	 public static StringBuffer getEncodedStrPutLineFormFile(String fileName,String charsetName)throws IOException{
			StringBuffer file = new StringBuffer();
			try {
				 BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName));

				String str;
			   	while ((str = in.readLine()) != null) {
				   file.append(str).append("\r\n");
			   	}
			 } catch (UnsupportedEncodingException e) {
				 Log.debug("log.day",e.getMessage(),null);
			 } catch (IOException ei) {
				 Log.debug("log.day",ei.getMessage(),null);
			 }
			return file;
	}

	 public String[] getArrayFromFile(String fileName)throws IOException{
		 String[] fileArray = null;
		 try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

			String str ="";
			Vector v = new Vector();
		   	while ((str = in.readLine()) != null) {
			   v.add(str);
		   	}

		   	fileArray = new String[v.size()];
		   	for(int i=0 ; i< v.size() ;i++){
		   		fileArray[i] = (String)v.get(i);
		   	}


		 } catch (UnsupportedEncodingException e) {
			 throw new UnsupportedEncodingException(e.getMessage());
		 } catch (IOException e) {
			 throw new IOException(e.getMessage());
		 }

		 return fileArray;
	 }
}