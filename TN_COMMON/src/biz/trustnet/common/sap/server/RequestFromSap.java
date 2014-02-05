/*
 * Project Name : TN_COMMON
 * File Name	: RequestFromSap.java
 * Date			: Jan 2, 2009
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :
 */

package biz.trustnet.common.sap.server;


import biz.trustnet.common.daemon.NTWrapper;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.sap.conf.SAPPoolConfigBean;
import biz.trustnet.common.sap.conf.SAPServerConfigBean;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.xml.XMLFactory;

import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;

public class RequestFromSap extends NTWrapper implements JCO.ServerExceptionListener, JCO.ServerErrorListener {


	/**
     *  This is the actual Server (Listener) object (InnerClass Start)
     */
    static public class Server extends JCO.Server{

    	public SAPServerConfigBean sapServerConfigBean = null;

        public Server(String gwHost,String gwService,String programId,IRepository repository,SAPServerConfigBean sapServerConfigBean){
            super(gwHost, gwService, programId, repository);
            this.sapServerConfigBean = sapServerConfigBean;
	  		this.setProperty("jco.server.unicode",sapServerConfigBean.getUnicode());
	  		this.setProperty("jco.server.trace",sapServerConfigBean.getTrace());
	  		this.setProperty("jco.server.max_startup_delay",sapServerConfigBean.getMaxStartupDelay());
	  		this.setProperty("jco.server.dsr",sapServerConfigBean.getDsr());
        }

        /**
         *  This function will be invoked when a transactional RFC is being called from a
         *  SAP R/3 system. The function has to store the TID in permanent storage and return <code>true</code>.
         *  The method has to return <code>false</code> if the a transaction with this ID has already
         *  been process. Throw an exception if anything goes wrong. The transaction processing will be
         *  aborted thereafter.<b>
         *  Derived servers must override this method to actually implement the transaction ID management.
         *  @param tid the transaction ID
         *  @return <code>true</code> if the ID is valid and not in use otherwise, <code>false</code> otherwise
         */
        protected boolean onCheckTID(String tid){
            return true;
        }

        /**
         *  This function will be called after the <em>local</em> transaction has been completed.
         *  All resources assiciated with this TID can be released.<b>
         *  Derived servers must override this method to actually implement the transaction ID management.
         *  @param tid the transaction ID
         */
        protected void onConfirmTID(String tid){
        }

        /**
         *  This function will be called after <em>all</em> RFC functions belonging to a certain transaction
         *  have been successfully completed. <b>
         *  Derived servers can override this method to locally commit the transaction.
         *  @param tid the transaction ID
         */
        protected void onCommit(String tid){
        }

        /**
         *  This function will be called if an error in one of the RFC functions belonging to
         *  a certain transaction has occurred.<b>
         *  Derived servers can override this method to locally rollback the transaction.
         *  @param tid the transaction ID
         */
        protected void onRollback(String tid){
        }

        /*****************************************************************************
         ** Called upon an incoming requests From SAP SYSTEMS
         ****************************************************************************/
        protected void handleRequest(JCO.Function function){
        	try{
        		SAPHandleRequestAbstract request = getHandleClass(sapServerConfigBean.getHandleRequestClass());
        		request.execute(function,sapServerConfigBean);
        	}catch(Exception e){
        		Log.debug("log.day","Handle Request Error ="+CommonUtil.getExceptionMessage(e),this);
        	}
        }

        public SAPHandleRequestAbstract getHandleClass(String className){
        	SAPHandleRequestAbstract object = null;
    		try{
    			object = (SAPHandleRequestAbstract)Class.forName(className).newInstance();
    		}catch(Exception e){
    			Log.debug("log.day","ClassLoad Error ="+e.getMessage(),this);
    		}
    		return object;
    	}
    }

	/**
	 *  Called if an exception was thrown anywhere in our server
	 */
	public void serverExceptionOccurred(JCO.Server srv, Exception ex){
		Log.debug("log.day","Exception in Server :[" + srv.getProgID()+"]:"+CommonUtil.getExceptionMessage(ex),this);
		ex.printStackTrace();
	}

	/**
	 *  Called if an error was thrown anywhere in our server
	 */
	public void serverErrorOccurred(JCO.Server srv, Error err){
		Log.debug("log.day","Error in Server :[" + srv.getProgID()+"]:"+err.getMessage(),this);
		err.printStackTrace();
	}

	// The server objects that actually handles the request

	private int MAX_SERVERS = 1;
	public Server servers[] = null;

	/**
	 *  Constructor. Creates a client pool, the repository and a server.
	 */
	public RequestFromSap(){
		IRepository repository;

		try{
			SAPServerConfigBean config = (SAPServerConfigBean)XMLFactory.getEntity("SAPSERVER");
			String[] POOL_NAME = CommonUtil.split(config.getPoolName(),",", true);
			MAX_SERVERS = POOL_NAME.length;
			servers = new Server[MAX_SERVERS];

			for(int i=0 ;i < MAX_SERVERS ; i++){
				SAPPoolConfigBean pConfig = (SAPPoolConfigBean)XMLFactory.getEntity(POOL_NAME[i]);
				JCO.addClientPool(pConfig.getPoolName(),CommonUtil.parseInt(pConfig.getMaxConnection()),pConfig.getClient(),pConfig.getUserId(),pConfig.getPassword(),pConfig.getLanguage(),pConfig.getHostName(),pConfig.getSystemNumber());
				repository = JCO.createRepository(pConfig.getPoolName(),pConfig.getPoolName() );
				servers[i] = new Server(pConfig.getGwHost(), pConfig.getGwService(), pConfig.getProgramId(), repository,config);
				Log.debug("log.day","addClientPool =["+pConfig.getPoolName()+"]",this);
			}
		}catch (JCO.Exception ex) {
			Log.debug("log.day","Exception :" + CommonUtil.getExceptionMessage(ex),this);
		}

		JCO.addServerExceptionListener(this);
		JCO.addServerErrorListener(this);
		Log.debug("log.day","initialized.....",this);
	}

	public boolean start(){
		try{
			for(int i = 0; i < MAX_SERVERS; i++){
				Log.debug("log.day","server ["+i+"] started.....",this);
				servers[i].start();
			}
		}
		catch (Exception ex) {
			Log.debug("log.day","Could not start servers :" + CommonUtil.getExceptionMessage(ex),this);
			return false;
		}
		return true;
	}

	public static void main(String[] args){
		biz.trustnet.common.sap.server.RequestFromSap sap = new biz.trustnet.common.sap.server.RequestFromSap();
		sap.start();
	}
}


