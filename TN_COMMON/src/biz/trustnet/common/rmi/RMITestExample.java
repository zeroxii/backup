/* 
 * Project Name		:   TN_COMMON
 * File Name		:	RMITestExample.java
 * Date				:	2004-06-15 - 오전 9:53:53
 * History			:	2004-06-15
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:    
 */


package biz.trustnet.common.rmi;


import biz.trustnet.common.rmi.RMIClient;
import biz.trustnet.common.rmi.RMIFactory;

public class RMITestExample {
	public static void main(String[] args){
		
		RMIClient rmi = null;
		
		try{
			rmi = RMIFactory.getInstance();
			System.out.println(rmi.getStatus());
			System.out.println(rmi.isConnected());
			System.out.println("서버에서 오는 메세지 "+rmi.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
} 
