package skycom.pbx.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	
/*    String driver="org.firebirdsql.jdbc.FBDriver"; 
    String url="jdbc:firebirdsql:127.0.0.1/3050:C:/SKYBILL/MDB/SKYBILLDB.FDB";
    String user="SYSDBA";
    String password = "masterkey";*/
    String driver="core.log.jdbc.driver.PostgresqlDriver"; 
    String url="jdbc:postgresql://localhost:5432/SKYCOM";
    
    
    
    //String url="jdbc:postgresql://192.168.0.209:5432/SIPGW";
    String user="postgres";
    String password = "skycom";
    /*
    String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
    String url="jdbc:sqlserver://localhost:1433;DatabaseName=SMDA_DB;SelectMethod=cursor";
    String user="sa";
    String password = "system";
    */
    
/* String driver="com.microsoft.jdbc.sqlserver.SQLServerDriver"; 
    String url="jdbc:sqlserver://localhost:1433;DatabaseName=BCCARDDB;SelectMethod=cursor";
    String user="sa";
    String password = "system";*/
    
    /**
     * ����
     *
     */
    public DBConnect() {
        super();

    }


    public Connection getConnection(){
        Connection con = null;
    
        try{
            Class.forName(driver);
            con=DriverManager.getConnection(url,user, password);
            System.out.println("DBConnect no poll");
            
        }catch(Exception  e){
            e.printStackTrace();
        }
        
        return con;
    }
    public static void main(String [] args){
    	
    	
    	DBConnect n = new DBConnect();
    	n.getConnection();
    }
}
