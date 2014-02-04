package skycom.vms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import skycom.vms.bean.MailBean;
import skycom.vms.common.LoggerHelper;
import skycom.vms.db.DBConnection;

public class MailDAO {
	private static final Logger logger = LoggerHelper.getLogger();
	
	//DBConnection DBFactory;
	public MailDAO() {
		//DBFactory = new DBConnection();
	}
	

	public List<MailBean> getMailList() throws SQLException{
		String sql = " select idx, receive, status, subject, textarea, comment, smtpHost, smtpPort, charset, sendAuth, defaultUser, defaultPass, defaultHost, useSSL, sslPort "
				+ ", fileflag_yn, filepath, filename from t_notify_mail where status = 'N' ";
		List<MailBean> list = new ArrayList<MailBean>();
		
		DBConnection db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{		
			db 		= db.getInstance();
			conn	= db.getConnection();
			pstmt	= conn.prepareStatement(sql);
			rs 	= pstmt.executeQuery();

			while(rs.next()){
				MailBean mBean = new MailBean();
				mBean.setIdx(rs.getInt("idx"));				
				mBean.setReceive(rs.getString("receive"));
				mBean.setSubject(rs.getString("subject"));
				mBean.setStatus(rs.getString("status"));
				mBean.setTextarea(rs.getString("textarea"));
				mBean.setComment(rs.getString("comment"));
				mBean.setSmtpHost(rs.getString("smtpHost"));
				mBean.setSmtpPort(rs.getString("smtpPort"));
				mBean.setCharset(rs.getString("charset"));
				mBean.setSendAuth(rs.getString("sendAuth"));
				mBean.setDefaultUser(rs.getString("defaultUser"));
				mBean.setDefaultPass(rs.getString("defaultPass"));
				mBean.setDefaultHost(rs.getString("defaultHost"));
				mBean.setUseSSL(rs.getString("useSSL"));
				mBean.setSslPort(rs.getString("sslPort"));
				mBean.setFileflag_yn(rs.getString("fileflag_yn"));
				mBean.setFilepath(rs.getString("filepath"));
				mBean.setFilename(rs.getString("filename"));
				list.add(mBean);
			}
		}catch(SQLException e){
			logger.info("QUERY="+sql);
			logger.info(e.getMessage());
		}finally{
			db.close(conn,pstmt,rs);
		}
		return list;
	}
	public boolean update(MailBean mBean) throws SQLException{
		String sql = "update t_notify_mail set comment = ?,status = ? where idx = ? ";

		int result 	= 0;
		DBConnection db = null;
		Connection conn = null;		
		PreparedStatement pstmt = null;
		try{
			db 		= db.getInstance();
			conn	= db.getConnection();
			pstmt	= conn.prepareStatement(sql);

			int idx = 1;
			pstmt.setString(idx++, mBean.getComment());
			pstmt.setString(idx++, mBean.getStatus());
			pstmt.setInt(idx++, mBean.getIdx());
			result = pstmt.executeUpdate();
			//conn.commit();

		}catch(SQLException e){
			logger.error("QUERY="+sql);
			logger.error(e.getMessage(),e);
		}finally{
			db.close(conn,pstmt);
		}

		if(result >= 0){
			return true;
		}else{
			return false;
		}
	}
}
