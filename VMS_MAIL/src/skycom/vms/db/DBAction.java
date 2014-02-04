package skycom.vms.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import skycom.vms.common.LoggerHelper;

public class DBAction {
	private static final Logger logger = LoggerHelper.getLogger();
		
	public DBAction() {
	}

	/**
	 * @param args
	 */
	public void DBConn() {		
		String sql = "select basic_type, basic_key from t_basiccode";
		DBConnection db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			db 		= db.getInstance();
			conn 	= db.getConnection();
			pstmt 	= conn.prepareStatement(sql);
			rs = pstmt.executeQuery();			
			while(rs.next()){
				System.out.println("rs="+rs.getRow());
			}
		} catch (SQLException e) {
			logger.info("QUERY="+sql);
			logger.info(e.getMessage());
		}finally{
			db.close(conn,pstmt,rs);
		}	
	}
	public static void main(String[] args) throws SQLException{
		DBAction ad = new DBAction();
		ad.DBConn();
	}	

}
