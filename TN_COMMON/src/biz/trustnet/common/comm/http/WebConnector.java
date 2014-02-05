/*
 * Project Name : TN_COMMON
 * File Name	: WebConnector.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ���ּ�
 * Comment      :
 */

package biz.trustnet.common.comm.http;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import biz.trustnet.common.comm.client.Socket;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.util.URLEncDec;




public class WebConnector implements Socket{


	private String METHOD 			= "GET";	//��޹��
	private String URL				= "";		//URL �ּ�
	private String contentType		= "";
	private boolean INPUT			= true;		//��� �Ķ���� ����
	private boolean OUTPUT 			= true;		//���� �Ķ���� ����
	private boolean connect 		= false;	//URL �������� ����
	public int HTTP_CODE			= 0;		//URL ���� �ڵ�
	public String HTTP_STATUS 		= "";		//HTTP ���°�
	public StringBuffer HTTP_MSG 	= new StringBuffer();	//HTTP ���� �ĸ����� ����

	private HttpURLConnection conn 	= null;
    private URL url					= null;
    private int timeout				= 50000;


	public WebConnector(){
	}

	/**
	 * URL �� METHOD �� ���� ���� METHOD �� �⺻������ POST�� �����ȴ�.
	 * @param URL
	 * @param METHOD
	 */
	public void setFormAction(String URL,String METHOD){
		this.URL 	= URL;
		this.METHOD = METHOD;
	}

	/**
	 * URL �� ���� �Ķ���� ��� �� ���ſ� ���� ���� true �� ��� �������� ������.
	 * �⺻���� ��� true �� �����Ǿ� �ִ�.
	 * @param INPUT
	 * @param OUTPUT
	 */
	public void setInOut(boolean INPUT,boolean OUTPUT){
		this.INPUT 	= INPUT;
		this.OUTPUT = OUTPUT;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public void setTimeout(int timeout){
		this.timeout = timeout;
	}

	/**
	 * request �� name&value pair �� �����Ǿ�� �Ѵ�.
	 * GET ��� ���ӽô� URL+?+request �� �����Ͽ� ����Ѵ�.
	 */
	public String connect(String request)throws Exception{
		initURL(request);
		try{
			url = new URL(URL);

			if(URL.toLowerCase().startsWith("https://"))
				conn = (HttpsURLConnection) url.openConnection();
			else
				conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(METHOD);

			conn.setConnectTimeout(timeout);
			if(METHOD.equals("POST")){
				if(contentType.equals("")){
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				}else{
					conn.setRequestProperty("Content-Type", contentType);
				}
				conn.setRequestProperty("Content-Length",CommonUtil.toString(request.getBytes().length));
			}else{
				conn.setRequestProperty("Content-Type", "text/plain; charset=Shift_JIS");
			}
			conn.setDoOutput(OUTPUT);
		    conn.setDoInput(INPUT);

		    if(OUTPUT){
		    	send(request);
		    }
		    getHTTPEnv();
		    if(redirect(HTTP_CODE)) moveURL(conn.getHeaderField("Location"));
		    if(INPUT) 	recv();

		    conn.disconnect();
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
			throw new Exception(e);
		}

		return HTTP_MSG.toString();
	}



	/**
	 * BufferedWriter ����� ���� �Ķ���� ���
	 * @param request
	 * @throws Exception
	 */
	private void send(String request)throws Exception{

		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write(request);
			out.flush();
			out.close();
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
			throw new Exception(e);
		}
	}

	/**
	 * BufferedReader �� ���� �Ķ���� �� ���� �޼��� ����
	 * @throws Exception
	 */
	private void recv()throws Exception{
		BufferedReader in 	= null;
	    try{
	    	in = new BufferedReader( new InputStreamReader( conn.getInputStream()));
	    	String inputLine 	= "";
	        while ((inputLine = in.readLine()) != null) {
	        	HTTP_MSG.append(inputLine);
	        }
	    }catch (IOException e) {
	    	Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
	         throw new Exception(e);
	    }
	}

	/**
	 * HTTP_MEVED_PERM || HTTP_MOVED_TEMP ���Ž� redirect
	 * @param code
	 * @return
	 */
	private boolean redirect(int code) {
		if (code == HttpURLConnection.HTTP_MOVED_PERM ||code == HttpURLConnection.HTTP_MOVED_TEMP)
	        return true;
		else
	        return false;
	}

	/**
	 * redirect �� ���� location �� �� Connection ��ȯ
	 * @param location
	 * @throws IOException
	 */
	private void moveURL(String location)throws IOException {
		url = new URL(location);

		if(location.toLowerCase().startsWith("https://"))
			conn = (HttpsURLConnection) url.openConnection();
		else
			conn = (HttpURLConnection) url.openConnection();
	}


	/**
	 * GET,POST �� �� request ��� �� �� http:// �������� �Է� ���ο� �� �� ����
	 * @param request
	 */
	private void initURL(String request){
		if(!URL.toLowerCase().startsWith("https://")){
			if(!URL.toLowerCase().startsWith("http://")){
				URL = "http://"+URL;
			}
		}

		if(METHOD.equals("GET")){
			OUTPUT = false;
			if(!URL.endsWith(request)){
				URL = URL+"?"+request;
			}
		}

	}

	/**
	 * HTTP ���ῡ ���� ���� �ڵ� �� ���� �޼��� Ȯ��
	 */
	private void getHTTPEnv(){

		try{
			HTTP_CODE = conn.getResponseCode();
			HTTP_STATUS += "HTTP_RESPONSE_CODE="+HTTP_CODE;
			HTTP_STATUS += "&HTTP_RESPONSE_MESSAGE="+URLEncDec.encode(conn.getResponseMessage(),"utf-8");

			Log.debug("log.root","HTTP_STATUS="+HTTP_STATUS,this);
			//System.out.println(HTTP_STATUS);

		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);

		}
	}

	public static void main(String[] args){
		biz.trustnet.common.comm.http.WebConnector w = new biz.trustnet.common.comm.http.WebConnector();

		w.setFormAction("http://app3.kttrust.net/payment/PaymentGate.jsp?REQUEST=REQ","POST");
		w.setInOut(true,true);
		try{
			w.connect("TRN_TYPE=TRN1&TERMINAL_ID=0828900001&MERCHANT_ID=20010001&TRN_DAY=20081119&TRN_TIME=214250&CARDNUMBER=4658874021693069&EXPIRE=1212&TRACK=4658874021693069=12120001&CVV=&AMOUNT=500&RBPERIOD=00&TRXTYPE=S&PAYTELNO=&PAYNAME=&PAYNO=20010001_081119_000045");
		}catch(Exception e){
			System.out.println("EEE = "+e.getMessage());
		}


	}


}