/* 
 * Project Name : TN_COMMON
 * File Name	: DBTest.java
 * Date			: 2007. 08. 17
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      :  
 */

package biz.trustnet.common.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import biz.trustnet.common.util.Property;
import biz.trustnet.common.db.DBFactory;
import biz.trustnet.common.db.DBConnectionManager;;
public class DBTest {

	public DBTest(){
		
	}
	
	public void test(){
		//biz.trustnet.common.util.Property.setTNLocation("D:\\Develop\\eclipse\\workspace\\TN_COMMON\\conf\\");
		DBConnectionManager db = null;
		Connection conn		= null;
		Statement stmt		= null;
		ResultSet rset		= null;
		
		try{
			System.out.println("1");
			//String a = System.getenv("TN_ENV");
			
			
			String classPath = System.getProperty("TN_ENV");
			System.out.println(classPath);
			

			System.out.println("11");
			db = DBFactory.getInstance();
			System.out.println("2");
			conn = db.getConnection();
			System.out.println("3");
			stmt = conn.createStatement();
			System.out.println("4");
			rset = stmt.executeQuery("SELECT * FROM FB_00 ");
			
			while(rset.next()){
				System.out.println(rset.getLong("IDX"));
			}
		}catch(Exception e){
			
		}finally{
			db.close(conn, stmt, rset);
		}
		
		
	}
	
	public static void main(String[] args){
		biz.trustnet.common.test.DBTest db = new biz.trustnet.common.test.DBTest();
		db.test();
	}
}
