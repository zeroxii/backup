/* 
 * Project Name	:   TN_COMMON
 * File Name		:	Socket.java
 * Date				:	2006. 5. 22. - 오후 9:44:48
 * History			:	2006. 5. 22.
 * Version			:	1.0
 * Author			:   임주섭	
 * Comment      	:    
 */
 
package biz.trustnet.common.comm.client;
public interface Socket {
  
	public String connect(String request) throws Exception;
}

