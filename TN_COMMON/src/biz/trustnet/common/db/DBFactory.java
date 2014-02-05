/* 
 * Project Name	:   TN_COMMON
 * File Name		:	DBFactory.java
 * Date				:	2004-05-04 - ¿ÀÈÄ 10:30:56
 * History			:	2004-05-04
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:   
 */
 
package biz.trustnet.common.db;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;



public final class DBFactory {

	private static DBFactory dbFactory;  
	public static String DBNAME = "DEFAULT_DB"; 
	private DBFactory() {
	}

	public static DBConnectionManager getInstance() throws DBException {
		if (dbFactory == null) {
			dbFactory = new DBFactory();
		}
		
		return dbFactory.get();
	}
	
	public static DBConnectionManager getInstance(String dbName) throws DBException {
		DBNAME = dbName;
		if (dbFactory == null) {
			dbFactory = new DBFactory();
		}
		return dbFactory.get();
	}

	private DBConnectionManager get() throws DBException {
		
		DBConnectionManager dbcm = null;
		try {
			dbcm = DBConnectionManager.getDBConnectionManager(DBNAME);
		} catch (DBException se){
			Log.debug("log.root",CommonUtil.getExceptionMessage(se),this);
			throw new DBException("DBFactory: " + se.getMessage());
		}
		return dbcm;
	}
	
	
}

