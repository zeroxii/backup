/* 
 * Project      : TN_COMMON
 * File Name    : biz.trustnet.common.db.oracle.DBUtil.java
 * Date         : Jul 16, 2009
 * Version      : 1.0
 * Author       : ginaida@trustmate.net
 * Comment      :  
 */

package biz.trustnet.common.db.oracle;

import java.sql.Timestamp;

import biz.trustnet.common.util.CommonUtil;

public class DBUtil {

	public DBUtil(){
	}
	
	
	public String getCountQuery(String query,long count){
		query = query.replaceAll("SELECT","SELECT * FROM (SELECT rownum,TMP1.* FROM ( SELECT ");
		query = query.replaceAll(" DESC"," DESC ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count));
		query = query.replaceAll(" ASC"," ASC ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count));
		if(query.toUpperCase().indexOf(" DESC") <0 && query.toUpperCase().indexOf(" ASC") <0){
			query +=" ) TMP1 ) TMP2 	WHERE rownum <="+CommonUtil.toString(count);
		}
		return query;			
	}
	
	public String getSystemTime(){
		return "SYSDATE";	
	}
	
	
	public String insertSequenceIdx(String query ,String table){
		query = query.toUpperCase();
		String values = "VALUES";
		
		StringBuffer sb = new StringBuffer();

		int insert	= query.indexOf(table) + table.length();
		int value	=  query.indexOf(values) + values.length()-1;
		
		sb.append(query.substring(0,insert));
		sb.append(CommonUtil.replace(query.substring(insert,value),"("," (IDX,"));
		sb.append(CommonUtil.replace(query.substring(value,query.length())," ("," (SEQ_"+table+".NEXTVAL,"));
		
		return sb.toString();
	
		
	}
	
	
	public StringBuffer dateSearchQuery(StringBuffer query, String start, String end, String field){ 
        if(!start.equals("")){ 
            if(!end.equals("")){   
            	query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                        "AND to_date('"+end+"','YYYY-MM-DD')+1 ");             
            }else{                  
            	query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " +
                        "AND to_date('"+start+"','YYYY-MM-DD')+1 ");
            }       
        }	
		return query;
	}
	
	
	public StringBuffer dateOneDaySearchQuery(StringBuffer query, String start, String field){
		query.append(" AND "+field+" BETWEEN to_date('"+start+"','YYYY-MM-DD') " + "AND to_date('"+start+"','YYYY-MM-DD')+1 ");
		return query;
	}
	
	public StringBuffer dateSearchQuery2(StringBuffer query, String start, String end, String field){
	
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
		return query;
	}
	
	public String getDateQuery(String field){
		
		return "to_char("+field+",'yyyy-mm-dd hh24:mi:ss') AS "+field;
	}
	
	public String getToDate(String date){
		date = CommonUtil.timestampToString(CommonUtil.stringToTimestamp(date));
	 	 
		return "to_date('"+date+"','yyyy-mm-dd hh24:mi:ss')";
 
	}
	
	public String getToDate(Timestamp date){
		String reqDate = CommonUtil.timestampToString(date);
		return "to_date('"+reqDate+"','yyyy-mm-dd hh24:mi:ss')";
	}
}
