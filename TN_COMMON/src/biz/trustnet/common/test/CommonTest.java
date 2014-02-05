/* 
 * Project Name : TN_COMMON
 * File Name	: CommonTest.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ���ּ�
 * Comment      :  
 */

package biz.trustnet.common.test;

import java.util.GregorianCalendar;

import biz.trustnet.common.util.CommonUtil;

public class CommonTest {

	public CommonTest(){
		
	}
	
	public void getBeanFiled(){
		
		  
		System.out.println(CommonUtil.getLastDayOfMonth(CommonUtil.getCurrentDate("yyyyMM")));
		String currentDate = CommonUtil.getCurrentDate("yyyyMMdd");
		String startDate = CommonUtil.getOpDate(GregorianCalendar.DATE,-10,currentDate);	//10���� �� ������
		String endDate = CommonUtil.getOpDate(GregorianCalendar.DATE,-3,currentDate);		//3���� �� �Ͽ���
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println( CommonUtil.getDayOfWeek());
	}
	
	
	
	public static void main(String[] args){
		CommonTest c = new CommonTest();
		c.getBeanFiled();
	}
}
