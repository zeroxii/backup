/*
 * Project Name	:   TN_COMMON
 * File Name		:	JDBCManager.java
 * Date				:	2005. 3. 31. - ¿ÀÈÄ 3:09:55
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */

package biz.trustnet.common.db.web;


import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import biz.trustnet.common.db.DBConfigBean;
import biz.trustnet.common.db.DBConnectionManager;
import biz.trustnet.common.db.DBException;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.BeanUtil;

public class JDBCManager extends DBConnectionManager{

	private static String DATASOURCE_NAME 	= "jdbc/trustnet";
	private static JDBCManager instance 	= null;
	private Connection conn					= null;
	DataSource pool = null;
		
	
	public JDBCManager(DBConfigBean db) throws DBException {
		try {
			DATASOURCE_NAME = db.getPoolName();
			Context env = (Context) new InitialContext().lookup("java:comp/env");
			pool = (DataSource) env.lookup(DATASOURCE_NAME);
			if (pool == null)
				throw new DBException("Can't initiate WebServer DB Connection Manager (pool is null) & datasource ="+DATASOURCE_NAME); 
		} catch (NamingException e) {
			Log.debug("log.root","JDBCWEBPOOL CONNECTION ERROR ="+e.getMessage(),this);
			throw new DBException(e.getMessage(),e);
		}
	}
	
	
	public static JDBCManager getInstance(DBConfigBean db)throws DBException{
		if(instance == null){
			instance = new JDBCManager(db);
		}
		return instance;
	}
	
	public Connection getConnection() throws DBException {
		
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			return conn;
		}catch(SQLException sql){
			throw new DBException("WebServer DB Connection is not create " , sql);
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
		}catch(Throwable t){
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
		}catch(Throwable t){
			throw new DBException(t);
		}finally {
			close(stmt);
			close(conn);
		}
		return result;
	}
}
