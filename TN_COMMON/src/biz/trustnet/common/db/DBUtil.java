/* 
 * Project Name : TN_COMMON
 * File Name	: DBUtil.java
 * Date			: 2007. 08. 21
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} 임주섭
 * Comment      :  
 */

package biz.trustnet.common.db;

import java.sql.Timestamp;

import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;

public class DBUtil {

	String dbType =  "";
	public DBUtil(){
		dbType = ((DBConfigBean)XMLFactory.getEntity().get(DBFactory.DBNAME)).getDbType();
	}
	
	public DBUtil(String dbType){
		this.dbType = dbType;
	}
	
	public String getCountQuery(String query,long count){
		
		
		
		if(dbType.equals("MSSQL")){
			query = query.replaceAll("SELECT", "SELECT TOP "+CommonUtil.toString(count)+" ");
		}else if(dbType.equals("INFORMIX")){
			query = query.replaceAll("SELECT", "SELECT FIRST "+CommonUtil.toString(count)+" ");
		}else if(dbType.equals("MYSQL")){
			query = query.replaceAll(" DESC"," DESC LIMIT "+CommonUtil.toString(count));
			query = query.replaceAll(" ASC"," ASC LIMIT "+CommonUtil.toString(count));
			if(query.toUpperCase().indexOf(" DESC") <0 && query.toUpperCase().indexOf("ASC") <0){
				query +=" ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count);
			}
		}else if(dbType.equals("ORACLE")){
			query = query.replaceAll("SELECT","SELECT * FROM (SELECT rownum,TMP1.* FROM ( SELECT ");
			query = query.replaceAll(" DESC"," DESC ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count));
			query = query.replaceAll(" ASC"," ASC ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count));
			if(query.toUpperCase().indexOf(" DESC") <0 && query.toUpperCase().indexOf(" ASC") <0){
				query +=" ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count);
			}
		}else{
			 
		}
		
		return query;			
	}
	
	public String getSystemTime(){
		String dbType = ((DBConfigBean)XMLFactory.getEntity().get(DBFactory.DBNAME)).getDbType();
		
		if(dbType.equals("MSSQL") || dbType.equals("SYBASE")){
			return " getdate() ";
		}else if(dbType.equals("MYSQL")){
			return " now() ";
		}else if(dbType.equals("ORACLE")){
			return "SYSDATE";
		}else{
			return "'"+CommonUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss")+"'";
		}
		
	}
	
	
	public String insertSequenceIdx(String query ,String table){
		
		if(dbType.equals("ORACLE")){
			query = query.toUpperCase();
			String values = "VALUES";
			
			StringBuffer sb = new StringBuffer();

			int insert	= query.indexOf(table) + table.length();
			int value	=  query.indexOf(values) + values.length()-1;
			
			sb.append(query.substring(0,insert));
			sb.append(CommonUtil.replace(query.substring(insert,value),"("," (IDX,"));
			sb.append(CommonUtil.replace(query.substring(value,query.length())," ("," (SEQ_"+table+".NEXTVAL,"));
			
			return sb.toString();
		}else{
			return query;
		}
		
	}
	
	
	public StringBuffer dateSearchQuery(StringBuffer query, String start, String end, String field){
		
		if(dbType.equals("ORACLE")){
            //검색 날짜 조건들 
            if(!start.equals("")){ 
                if(!end.equals("")){   
                	query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                            "AND to_date('"+end+"','YYYY-MM-DD')+1 ");             
                }else{                  
                	query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                            "AND to_date('"+start+"','YYYY-MM-DD')+1 ");
                }       
            }
            
        }else{
            if(!start.equals("")){
            	query.append(" AND "+field+" >= '"+start+" 00:00:00' ");
     		}
     		if(!end.equals("")){
     			query.append(" AND "+field+" <= '"+end+" 23:59:59' ");
     		}
        }
		
		return query;
	}
	
	
	public StringBuffer dateOneDaySearchQuery(StringBuffer query, String start, String field){
		
		if(dbType.equals("ORACLE")){
			query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " + "AND to_date('"+start+"','YYYY-MM-DD')+1 ");
            
        }else{
            if(!start.equals("")){
            	query.append(" AND "+field+" >= '"+start+" 00:00:00' ");
     		}
        }
		
		return query;
	}
	
	public StringBuffer dateSearchQuery2(StringBuffer query, String start, String end, String field){
		
		if(dbType.equals("ORACLE")){
            //검색 날짜 조건들 
            if(!start.equals("")){ 
                if(!end.equals("")){   
                	query.append(field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                            "AND to_date('"+end+"','YYYY-MM-DD')+1 AND ");             
                }else{                  
                	query.append(field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                            "AND to_date('"+start+"','YYYY-MM-DD')+1 AND");
                }       
            }
            
        }else{
            if(!start.equals("")){
            	query.append(field+" >= '"+start+" 00:00:00' AND ");
     		}
     		if(!end.equals("")){
     			query.append(field+" <= '"+end+" 23:59:59' AND ");
     		}
        }
		
		return query;
	}
	
	public String getDateQuery(String field){
		
	  String qDate = "";
        if(dbType.equals("ORACLE")){
        	qDate = "to_char("+field+",'yyyy-mm-dd hh24:mi:ss') AS "+field;
        }else if(dbType.equals("MYSQL")){
        	qDate = "date_format("+field+", '%Y-%m-%d') AS "+field;
        }else{
        	qDate = "convert(char(10), "+field+",120) AS "+field;
        }
	        
        return qDate;
	}
	
	public String getToDate(String date){
		
		String qDate = "";
		date = CommonUtil.timestampToString(CommonUtil.stringToTimestamp(date));
	  
	  
        if(dbType.equals("ORACLE")){
        	qDate = "to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')";
        }else if(dbType.equals("MYSQL")){
        	qDate = "date_format('"+date+"', '%Y-%m-%d %H:%i:%s')";
        }else{
        	qDate = date;
        }
	        
        return qDate;
	}
	
	public String getToDate(Timestamp date){
		
		String qDate = "";
	
		String reqDate = CommonUtil.timestampToString(date);
	  
	   
        if(dbType.equals("ORACLE")){
        	qDate = "to_date('"+reqDate+"','yyyy-mm-dd hh24:mi:ss')";
        }else if(dbType.equals("MYSQL")){
        	qDate = "date_format('"+reqDate+"', '%Y-%m-%d %H:%i:%s')";
        }else{
        	qDate = "'"+reqDate+"'";
        }
	        
        return qDate;
	}
	
}
