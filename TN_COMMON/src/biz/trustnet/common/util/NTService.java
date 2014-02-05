/* 
 * Project Name	:   TN_COMMON
 * File Name		:	NTService.java
 * Date				:	2006. 5. 22. - ø¿»ƒ 9:59:05
 * History			:	2006. 5. 22.
 * Version			:	1.0
 * Author			:   ¿”¡÷º∑	
 * Comment      	:    
 */
package biz.trustnet.common.util;


import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.daemon.NTWrapper;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;


class NTService implements WrapperListener{

	private static biz.trustnet.common.daemon.NTWrapper  nt = null;
	private String pid 	= "TRUSTNET";


	public Integer start(String[] args) {
		try{
			if(args.length != 2){
				Log.debug("log.root","NTSERVICE Excute Error args[0] = PID , args[1] = className", this);
			}else{
				pid = args[0];
				if(!loadInstance(args[1])){
					Log.debug("log.root","NTSERVICE ["+args[1]+"] is not load", this);
					stop(-1);
				}else{
					Log.debug("log.root","NTSERVICE "+pid+" is loading" , this);
				}
			}
			
		}catch(Exception e){
			Log.debug("log.root","NTSERVICE ERROR "+CommonUtil.getExceptionMessage(e),this);
		}
		return null;
	}

	public int stop(int exitCode) {
		nt = null;
		return exitCode;
	}

	public boolean loadInstance(String className){
		int cnt = 0;
		boolean load = false;
		while(!load){
			try{
				nt = (NTWrapper)Class.forName(className).newInstance();
				nt.start();
				load = true;
			}catch(Exception e){
				Log.debug("log.root",className+e.getMessage(),this);
				Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
			}
			if(cnt++ ==5){
				load = true;
				stop(-1);
			}
		}
		return load;
	}

	public void controlEvent(int event) {
		if ((event == WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT) && WrapperManager.isLaunchedAsService() ) {
		}else if((event == WrapperManager.WRAPPER_CTRL_C_EVENT) || (event == WrapperManager.WRAPPER_CTRL_CLOSE_EVENT) || (event == WrapperManager.WRAPPER_CTRL_SHUTDOWN_EVENT)){
			WrapperManager.stop(0);
		}else {
			WrapperManager.stop(0);
		}
	}
	

	public static void main(String[] args){
		WrapperManager.start(new biz.trustnet.common.util.NTService(), args);
	}

}
