/* 
 * Project Name : TN_COMMON
 * File Name	: DBConnection.java
 * Date			: Dec 17, 2008
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :  
 */

package biz.trustnet.common.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;

import com.bitmechanic.sql.ConnectionPool;
import com.bitmechanic.sql.ConnectionPoolManager;

public class DBConnection {

	private ConnectionPoolManager pool;
	private Connection conn			= null;
	private String poolAlias     	= "TrustNetPool";
	private int reapConnInterval 	= 150;
	private int maxConn 			= 10;
	private int idleTimeout 		= 50;
	private int checkoutTimeout 	= 50;
	private int maxCheckout 		= 15;
	private DBConfigBean db			= null;
	
	public DBConnection(){
		this("DEFAULT_DB");
	}
	
	public DBConnection(String dbName){
		db = (DBConfigBean)XMLFactory.getEntity(dbName);
		createInstance(db);
	}
	
	public void createInstance(DBConfigBean db){
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
	
	
	public Connection getConnection() throws DBException {
 		try {
 			conn = pool.getPool(poolAlias).getConnection();
			if(conn == null){
				conn = pool.getPool(poolAlias).getConnection();
				conn = DriverManager.getConnection(ConnectionPoolManager.URL_PREFIX + poolAlias, null, null);
			}
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
	
	public void close(Connection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(ResultSet rSet){
		if(rSet != null){
			try{
				rSet.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Statement stmt){
		if(stmt != null){
			try{
				stmt.close();
			}catch(SQLException e){}
		}
	}
		
	public void close(PreparedStatement pstmt){
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(CallableStatement cstmt){
		if(cstmt != null){
			try{
				cstmt.close();
			}catch(SQLException e){}
		}
	}
	
	public void close(Connection conn,PreparedStatement pstmt,ResultSet rset){
		close(rset);
		close(pstmt);
		close(conn);
	}
	
	public void close(Connection conn,Statement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	
	public void close(Connection conn,CallableStatement stmt,ResultSet rset){
		close(rset);
		close(stmt);
		close(conn);
	}
	

}
