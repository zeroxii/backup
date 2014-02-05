/* 
 * Project Name		:   TN_COMMON
 * File Name		:	ConnectorImpl.java
 * Date				:	2004-06-15 - ¿ÀÀü 9:53:53
 * History			:	2004-06-15
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:    
 */
 
package biz.trustnet.common.rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import biz.trustnet.common.log.Log;




  


public class ConnectorImpl extends UnicastRemoteObject implements Connector {

	public ConnectorImpl() throws RemoteException{
	}
	
	public String getMessage() throws RemoteException {
		return "WelCome to RMI WORLD";
	}
	
	
	public String getRMIMessage()throws RemoteException{
		return "GINAIDA => RMI IS RUNNING";
	}
		
}
