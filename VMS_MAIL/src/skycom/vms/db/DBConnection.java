package skycom.vms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import skycom.vms.common.GetProperties;
import skycom.vms.common.LoggerHelper;

import com.bitmechanic.sql.ConnectionPoolManager;

public class DBConnection {
	private static final Logger logger = LoggerHelper.getLogger();
	private static ConnectionPoolManager cpm;
	private static DBConnection instance;
	private static int reapConnInterval = 300;
	private static int maxConn 			= 200;
	private static int idleTimeout 		= 60;
	private static int checkoutTimeout 	= 60;
	private static int maxCheckout 		= 30;	
	
	public DBConnection() {
		try {
			if(cpm == null){
				Class.forName(GetProperties.getInstance().getMessageReturnCode("mysql_driver"));
				cpm = new ConnectionPoolManager(reapConnInterval);
		        cpm.addAlias(
		        		GetProperties.getInstance().getMessageReturnCode("db_alias"),
		        		GetProperties.getInstance().getMessageReturnCode("mysql_driver"),
		        		GetProperties.getInstance().getMessageReturnCode("signal_db"),
		        		GetProperties.getInstance().getMessageReturnCode("username"),
		        		GetProperties.getInstance().getMessageReturnCode("password"), 
		        		maxConn, 
		        		idleTimeout, 
		        		checkoutTimeout,
		        		maxCheckout
		        );
			}			
		} catch (Exception e) {
			logger.info("DBConnectionMgr Load Error "+e.getMessage());
		}
	}
	public static DBConnection getInstance() {
		if(instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
	public Connection getConnection() {
		Connection con = null;
  	   	try {
  	   		con = DriverManager.getConnection(ConnectionPoolManager.URL_PREFIX + GetProperties.getInstance().getMessageReturnCode("db_alias"));
  	   	} catch (SQLException e) {
  	   		logger.error("DBConnection Error "+e.getMessage(), e); 
		}
  	   	return con;
  	   	
	}
	public void close(Connection conn,PreparedStatement pstmt,ResultSet rset){
		close(rset);
		close(pstmt);
		close(conn);
	}
	public void close(Connection conn,PreparedStatement pstmt){	
		close(pstmt);
		close(conn);
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
}
