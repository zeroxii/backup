package skycom.pbx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionPoolMgr {
	
    DBConnect dbconnect = null;

    String poolName = "jdbc:apache:commons:dbcp:skycom";

    int DBCPMode = 2;  
 
    public ConnectionPoolMgr() {
        super();
    }
    
 
    public ConnectionPoolMgr(int DBCPMode){
        this.DBCPMode = DBCPMode;
    }

    public Connection getConnection() throws Exception{
        Connection con = null;
        
        try {
            if ( DBCPMode == 1){ // JAVA
            	
                dbconnect = new DBConnect();
                con = dbconnect.getConnection(); 
                System.out.println("JAVA Client Connection: " + con.hashCode());
            }
            
            if ( DBCPMode == 2){ // WEB, Servlet, Struts 

                con = DriverManager.getConnection(poolName);
                System.out.println("Skycom Client Connection: " + con.hashCode());
            }
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
        }
        return con;
    }


	public int getDBCPMode() {
		return DBCPMode;
	}


	public void setDBCPMode(int mode) {
		DBCPMode = mode;
	}
    
    
}
