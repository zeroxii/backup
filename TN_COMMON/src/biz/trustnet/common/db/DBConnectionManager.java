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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.xml.XMLFactory;


public abstract class DBConnectionManager {

	private static DBConfigBean db = null;
	private static DBConnectionManager instance = null;
	
	
	public static DBConnectionManager getDBConnectionManager(String dbName) throws DBException {
		db = (DBConfigBean)XMLFactory.getEntity(dbName);
		if(instance == null) {
			initInstance();
		}
		return instance;
	}
	
	public static void initInstance() throws DBException {
		synchronized(DBConnectionManager.class) {
			
			try{
				if(db.getDbType().equals("MSSQL")){
					instance = new biz.trustnet.common.db.single.JDBCManager(db);
				}else if(db.getDbType().equals("ORACLE") || db.getDbType().equals("MYSQL") || db.getDbType().equals("INFORMIX")){
					instance = biz.trustnet.common.db.pool.JDBCManager.getInstance(db);
				}else if(db.getDbType().equals("WEB")){
					instance = biz.trustnet.common.db.web.JDBCManager.getInstance(db);
				}else{
					instance = new biz.trustnet.common.db.single.JDBCManager(db);
				}
			}catch(Throwable ex){
				Log.debug("log.root","JDBCPOOL SET BEAN ="+BeanUtil.beanToString(db),null);
				throw new DBException("#### Can't initiate DB Connection Manager",ex);
			}
			
		}
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
	
	
	public abstract Connection getConnection() throws DBException;
	public abstract int preparedExecuteUpdate(String query) throws DBException;
	public abstract int statementExecuteUpdate(String query) throws DBException;
	
}

