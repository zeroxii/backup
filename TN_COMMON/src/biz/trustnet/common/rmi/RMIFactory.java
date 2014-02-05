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

 
import biz.trustnet.common.rmi.RMIClient;
 
public final class RMIFactory {

	private static RMIFactory rmiFactory;   

	private RMIFactory() {
	}

	public static RMIClient getInstance() throws Exception {
		if (rmiFactory == null) {
			rmiFactory = new RMIFactory();
		}
		return rmiFactory.get();
	}

	private RMIClient get() throws Exception {

		RMIClient rmi= null;
		try {
			rmi = new RMIClient().getRMIClient();
		} catch (Exception se){
			throw new Exception("RMIFactory: " + se.getMessage());
		}
		return rmi; 
	}
}

