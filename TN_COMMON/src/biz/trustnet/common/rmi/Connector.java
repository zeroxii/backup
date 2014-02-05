/* 
 * Project Name		:   TN_COMMON
 * File Name		:	Connector.java
 * Date				:	2004-06-15 - ¿ÀÀü 9:53:53
 * History			:	2004-06-15
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:    
 */
 
package biz.trustnet.common.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Connector extends Remote{
	
	public String getMessage() throws RemoteException;

	public String getRMIMessage()throws RemoteException;
}
   