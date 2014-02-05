/* 
 * Project Name		:   TN_COMMON
 * File Name		:	RMIClient.java
 * Date				:	2004-06-15 - ¿ÀÀü 9:53:53
 * History			:	2004-06-15
 * Version			:	1.0
 * Author			:	ginaida@ginaida.net
 * Comment      	:    
 */
 
package biz.trustnet.common.rmi;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming; 
import java.util.Date;
import biz.trustnet.common.rmi.ConnectorImpl;
import biz.trustnet.common.log.Log;



public class RMIServer {
	
	public RMIServer(){
		start();
		Log.debug("log.rmi","RMI SERVER START",this);
	}
	
	public void connect() throws RemoteException {
	}
	
	public void start(){
		try {
			System.setProperty("java.security.policy","D:\\WebShare\\Properties\\rmiserver.policy");
		//	System.setProperty("java.rmi.server.codebase","file:D:\\Communicator\\classes\\moveon\\common\\rmi");
			ConnectorImpl connectorObject = new ConnectorImpl();
			Naming.rebind("rmi:///Connector",connectorObject);
		}catch(RemoteException re) {
			Log.debug("log.rmi","RemoteException: " + re.getMessage(),this);
		}catch(MalformedURLException mfe){
			Log.debug("log.rmi","MalformedURLException: "+mfe.getMessage(),this);
		}

		Log.debug("log.rmi","RMI Server run at " + new Date().toString(),this);
	}
	
	public static void main(String[] args) {
		biz.trustnet.common.rmi.RMIServer rmi = new biz.trustnet.common.rmi.RMIServer();
	}
}
 