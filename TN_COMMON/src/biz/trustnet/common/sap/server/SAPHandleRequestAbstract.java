/*
 * Project Name : TN_COMMON
 * File Name	: SAPHandleRequestAbstract.java
 * Date			: Jan 2, 2009
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} Administrator
 * Comment      :
 */

package biz.trustnet.common.sap.server;

import biz.trustnet.common.sap.conf.SAPServerConfigBean;

import com.sap.mw.jco.JCO;

public abstract class SAPHandleRequestAbstract {

	public abstract boolean execute(JCO.Function function,SAPServerConfigBean configBean);
}
