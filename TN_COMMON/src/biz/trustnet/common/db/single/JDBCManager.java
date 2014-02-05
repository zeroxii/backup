/*
 * Project Name	:   TN_COMMON
 * File Name		:	JDBCManager.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 3:09:55
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */

package biz.trustnet.common.db.single;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


import biz.trustnet.common.db.DBConfigBean;
import biz.trustnet.common.db.DBException;
import biz.trustnet.common.db.DBConnectionManager;
import biz.trustnet.common.log.Log;



public class JDBCManager extends DBConnectionManager{


	private Connection conn = null;
	private DBConfigBean db				= null;
	
	public JDBCManager(DBConfigBean db){
		this.db = db;
	}



	public Connection getConnection() throws DBException {

		try{
			if(conn == null || conn.isClosed()){
				Class.forName(db.getJdbcDriver());
				conn = DriverManager.getConnection(db.getDbUrl(),db.getDbUser(),db.getDbPassword());
				conn.setAutoCommit(false);
			}
			
			return conn;
		}catch(Exception sql){
			Log.debug("log.root","JDBCPOOL CONNECTION ERROR ="+sql.getMessage(),this);
			throw new DBException("JDBCPool DB StandAlone Connection is not create ");
		}
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
		}catch(Exception t){
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
		}catch(Exception t){
			throw new DBException(t);
		}finally {
			close(stmt);
			close(conn);
		}
		return result;
	}
}
