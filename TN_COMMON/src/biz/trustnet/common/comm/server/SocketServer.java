/* 
 * Project Name	:   TN_COMMON
 * File Name	:	SocketServer.java
 * Date			:	2005. 3. 31. - ¿ÀÈÄ 2:11:37
 * History		:	2005. 3. 31.
 * Version		:	1.0
 * Author		:	
 * Comment      :    
 */
 
package biz.trustnet.common.comm.server;

import java.net.Socket;

public interface SocketServer {
	public void serve(Socket socket);
}

