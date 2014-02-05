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
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import biz.trustnet.common.rmi.Connector;




public  class RMIClient {

 	private static final String RMI_HOST = "192.168.2.104";
 	private static final String RMI_PORT = "1099";
 	private static final String REMOTE_OBJECT = "Connector";
 	private static RMIClient instance = null;
 	private static Connector conn = null;
 	private static boolean isConnect = false;
 	private static String status = "connected to server";
	
	public RMIClient(){ 
		init();
	}
	
	public void init(){
		try{
			//System.setProperty("java.security.policy","D:\\WebShare\\Properties\\rmiclient.policy");
			//System.setSecurityManager(new RMISecurityManager());
			conn = (Connector)Naming.lookup("rmi://" + RMI_HOST + "/" + REMOTE_OBJECT);
			isConnect = true;
		}catch(RemoteException re) {
			isConnect = false;
			status = "RemoteException = " + re.getMessage();
		}catch(NotBoundException nbe) {
			isConnect = false;
			status = "NotBoundException = " + nbe.getMessage();
		}catch(MalformedURLException mfe){
			isConnect = false;
			status = "MalformedURLException = " + mfe.getMessage();
		}
	}
	
	
	public RMIClient getRMIClient()throws Exception{
		if(instance == null) {
			initInstance();
		}
		return instance;
	}
	
	
	public void initInstance() throws Exception{
		synchronized(RMIClient.class) {
			if(instance == null) {
				try {
					instance = new RMIClient();
				}catch(Throwable ex){
						throw new Exception("#### Can't initiate RMI Connection Manager",ex);
				}
			}
		}
	}
	
	
	public boolean isConnected(){
		return isConnect;
	}
	
	
	public String getStatus(){
		return status;
	}
	
	public String getMessage() throws Exception{
		String message = "";
		try {
				message = conn.getMessage() + conn.getRMIMessage();
				isConnect = true;
			}catch(RemoteException e){
				isConnect = false;
				status = "RemoteException = " + e.getMessage();
				throw new Exception(status);
			}
			return message;
	}
	

}
