/* 
 * Project Name	:   TN_COMMON
 * File Name		:	DBException.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 3:00:55
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:    
 */
 
package biz.trustnet.common.db;

import biz.trustnet.common.log.Log;


public class DBException extends Exception  
{
	
	public DBException() {
		super();
		Log.debug("log.root",getMessage(),this);
	}
	 
	public DBException(String msg) {
		super(msg);
		Log.debug("log.root",msg,this);
	}
	
	public DBException(String msg,Throwable cause){
		super(msg,cause);
		Log.debug("log.root",msg,cause);
	}
	
	public DBException(Throwable cause){
		super(cause);
		Log.debug("log.root","",cause);
	}
	
}
