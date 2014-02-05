/*
 * Project Name	:   TN_COMMON
 * File Name		:	JDBCManager.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 3:09:55
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */
 
package biz.trustnet.common.db.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import biz.trustnet.common.db.DBConfigBean;
import biz.trustnet.common.db.DBConnectionManager;
import biz.trustnet.common.db.DBException;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.util.CommonUtil;

import com.bitmechanic.sql.ConnectionPool;
import com.bitmechanic.sql.ConnectionPoolManager;


public class JDBCManager extends DBConnectionManager {

	
	private ConnectionPoolManager pool;
	private Connection conn				= null;
	private static JDBCManager instance	= null;
	private static String poolAlias     = "TrustNetPool";
	private static int reapConnInterval = 150;
	private static int maxConn 			= 10;
	private static int idleTimeout 		= 50;
	private static int checkoutTimeout 	= 50;
	private static int maxCheckout 		= 15;

	
	public JDBCManager(DBConfigBean db)throws DBException{
		poolAlias 	= db.getPoolName();
		maxConn    	= CommonUtil.parseInt(db.getConnection());
		try{
			Class.forName(db.getJdbcDriver()).newInstance();
			pool = new ConnectionPoolManager(reapConnInterval);
			pool.addAlias(poolAlias, db.getJdbcDriver(), db.getDbUrl(), db.getDbUser(), db.getDbPassword(), maxConn, idleTimeout, checkoutTimeout, maxCheckout);
		}catch(Exception e){
			Log.debug("log.root","JDBCPOOL CONNECTION ERROR ="+e.getMessage(),this);
		}
	}
	
	public static JDBCManager getInstance(DBConfigBean db)throws DBException{
		if (instance == null){	
			instance = new JDBCManager(db);	
		}
		return instance;
	}
	
	
	public Connection getConnection() throws DBException {
 		try {
			conn = DriverManager.getConnection(ConnectionPoolManager.URL_PREFIX + poolAlias, null, null);
			conn.setAutoCommit(false);
			return conn;
		}catch(Exception sql){
			Log.debug("log.root","JDBCPOOL CONNECTION ERROR ="+sql.getMessage(),this);
			Log.debug("log.root","PoolInformation ="+getStatus(),this);
			throw new DBException("JDBCPool DB Pool Connection is not create " +sql.getMessage(), sql);
		}
 		
	} 
	
	public String getStatus() throws DBException 
	{
		StringBuffer sb = new StringBuffer();
		try{
			ConnectionPool p = pool.getPool(poolAlias);
			sb.append("+-------------------------------------------+\n");
			sb.append("| Connection Pools Statistics                               |\n");
			sb.append("+-------------------------------------------+\n");
			sb.append("  * Current size: " + p.size() + " of " + p.getMaxConn() + "\n");
			sb.append("  * Connection requests: " + p.getNumRequests() + "\n");
			sb.append("  * Number of waits: " + p.getNumWaits() + "\n");
			sb.append("  * Number of timeouts: " +p.getNumCheckoutTimeouts() + "\n");
			sb.append("  * Dump Info: " + p.dumpInfo() + "\n");
		}catch(Exception e){
			Log.debug("log.root","PoolInformation ="+pool.dumpInfo(),this);
		}
		return sb.toString();		
	}

	public int preparedExecuteUpdate(String query) throws DBException {
		PreparedStatement pstmt = null;
		Connection 	conn			= null;
		int result = 0;

		try {
			conn		= getConnection();
			pstmt		= conn.prepareStatement(query);
			result  	= pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException t){
			throw new DBException(t);
		}finally {
			close(pstmt);
			close(conn);
		}
		return result;
	}

	public int statementExecuteUpdate(String query)throws DBException {
		Statement stmt 	= null;
		Connection conn = null;
		int result = 0;

		try {
			conn		= getConnection();
			stmt		= conn.createStatement();
			result  	= stmt.executeUpdate(query);
			conn.commit();
		}catch(SQLException t){
			throw new DBException(t);
		}finally {
			close(stmt);
			close(conn);
		}
		return result;
	}
	
}
