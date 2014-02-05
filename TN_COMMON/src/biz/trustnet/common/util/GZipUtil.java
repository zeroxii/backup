/*
 * Project      : TN_COMMON
 * File Name    : biz.trustnet.common.util.GZipUtil.java
 * Date         : 2010. 7. 5.
 * Version      : 1.0
 * Author       : ginaida@trustmate.net
 * Comment      :
 */

package biz.trustnet.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import biz.trustnet.common.log.Log;

public class GZipUtil {

	private static final int BUFFER_SIZE = 1024 * 10;

	public GZipUtil(){
	}

	public boolean compress(String destFile,String zipFile){
		boolean isCompressed = false;

		BufferedInputStream bis 	= null;
		BufferedOutputStream bos 	= null;

		try{
			bis = new BufferedInputStream(new FileInputStream(destFile));
	        bos = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zipFile)));

	        byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, cnt);
            }
            bos.flush();
	        bos.close();
	        bis.close();
	        isCompressed = true;
	        Log.debug("log.root","gz "+destFile +" -> "+zipFile,this);
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		return isCompressed;
	}

	public boolean decompress(String zipFile,String unzipFile){
		boolean isCompressed = false;

		BufferedInputStream bis 	= null;
		BufferedOutputStream bos 	= null;

		try{
			bis = new BufferedInputStream(new GZIPInputStream(new FileInputStream(zipFile)));
	        bos = new BufferedOutputStream(new FileOutputStream(unzipFile));

	        byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, cnt);
            }
            bos.flush();
	        bos.close();
	        bis.close();
	        isCompressed = true;
	        Log.debug("log.root","gz "+zipFile +" -> "+unzipFile,this);
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		return isCompressed;
	}


}
