/*
 * Project Name : TN_COMMON
 * Project      : ARSJCO
 * File Name    : biz.trustnet.common.sap.client.CommunicationSap.java
 * Date	        : Dec 30, 2008
 * Version      : 2.0
 * Author       : ginaida@ginaida.net
 * Comment      :
 */

package biz.trustnet.common.sap.client;

import biz.trustnet.common.log.Log;
import biz.trustnet.common.sap.conf.SAPConfigBean;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;


public class CommunicationSap {

	private JCO.Repository mRepository;
	private JCO.Client mConnection;
	private String message;

	public CommunicationSap(){
	}

	public synchronized boolean createConnection(String POOL_NAME){
		boolean isCreated = false;
		try{
			if(JCO.getClientPoolManager().getPool(POOL_NAME) == null){
				Log.debug("log.day","CREATE CONNECTION POOL =["+POOL_NAME+"]",this);
				SAPConfigBean sapConfig = (SAPConfigBean)XMLFactory.getEntity(POOL_NAME);
				JCO.addClientPool(POOL_NAME,CommonUtil.parseInt(sapConfig.getMaxConnection()),sapConfig.getClient(), sapConfig.getUserId(), sapConfig.getPassword(),sapConfig.getLanguage(),sapConfig.getHostName(),sapConfig.getSystemNumber());
			}
			JCO.Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
			mRepository = StandardRepositoryManager.getSingleInstance().getRepository(pool,true);
			mConnection = JCO.getClient(POOL_NAME);
			if(mConnection != null){
				isCreated = true;
			}
		}catch(Exception e){
			message = "JCO.Client Connect Error = "+e.getMessage();
			Log.debug("log.day","JCO.Client Connect Error ="+CommonUtil.getExceptionMessage(e),this);
		}
		return isCreated;
	}

	public JCO.Function getFunction(String name)  {
	    try {
	    	IFunctionTemplate ft = mRepository.getFunctionTemplate(name.toUpperCase ());
		    if (ft == null)
		    	return null;
		    return ft.getFunction ();
	    }
	    catch (JCO.Exception ex) {
	    	message = "Problem retrieving JCO.Function "+ex.getMessage();
	    	Log.debug("log.day","Problem retrieving JCO.Function "+name.toUpperCase()+" object." +CommonUtil.getExceptionMessage(ex),this);
		    return null;
	   }
	}

	public boolean executeFunction(JCO.Function function){
		boolean isSuccess = false;
		try{
			mConnection.execute(function);
			isSuccess = true;
		}catch(Exception e){
			message = "Problem execute JCO.Function "+e.getMessage();
			Log.debug("log.day","Problem execute JCO.Function" +CommonUtil.getExceptionMessage(e),this);
		}
		return isSuccess;
	}

	public void disconnect(){
		JCO.releaseClient(mConnection);
	}

	public String getErrorMessage(){
		return message;
	}

}

