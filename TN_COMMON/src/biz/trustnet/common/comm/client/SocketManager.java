/*
 * Project Name	:   TN_COMMON
 * File Name		:	SocketManager.java
 * Date				:	2006. 5. 22. - 오후 9:45:10
 * History			:	2006. 5. 22.
 * Version			:	1.0
 * Author			:   임주섭
 * Comment      	:
 */


package biz.trustnet.common.comm.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import biz.trustnet.common.util.CommonUtil;
import biz.trustnet.common.util.Property;

import com.jscape.inet.util.TimedSocket;

public class SocketManager implements biz.trustnet.common.comm.client.Socket{

	public Socket socket 				= null;
	public InputStream inputStream 		= null;
	public OutputStream outputStream	= null;

	private String host 	= "";
	private int port  		= 0;
	private int socketTimeout   = 30000;			//5초
	private int soTimeout		 = 30000;		//30초

	public SocketManager() {
	}

	public SocketManager(String host , int port) {
		this.host	= host;
		this.port 	= port;
	}


	public void setSocketInfo(String host,int port){
		this.host 	= host;
		this.port 		= port;
	}


	public void setSocketInfo(String host,String port){
		setSocketInfo(host,CommonUtil.parseInt(port));
	}


	public void setSocketTimeout(int socketTimeout ,  int soTimeout) {
		this.socketTimeout = socketTimeout;
		this.soTimeout 	   = soTimeout;
	}

	public boolean isAlive(){
		if(socket.isOutputShutdown()){
			return false;
		}
		if(socket.isClosed()){
			return false;
		}
		return true;

	}


	public void setSocketTimeout(String socketTimeout, String soTimeout) {
		setSocketTimeout(CommonUtil.parseInt(socketTimeout),CommonUtil.parseInt(soTimeout));
	}

	public OutputStream getOutputStream()throws IOException {
		if(socket == null){
			System.out.println("getOutputStream() Error");
			throw new IOException("socket is null");
		}
		return socket.getOutputStream();
	}

	public InputStream getInputStream()throws IOException {
		if(socket == null){
			System.out.println("getInputStream() Error");
			throw new IOException("socket is null");
		}
		return socket.getInputStream();
	}


	public void connect() throws IOException{
		try{

			String version = new Property().getSystemProperties("java.version");


			if(version.startsWith("1.3")){
				socket = TimedSocket.getSocket(host,port,socketTimeout);
			}else{
				socket = new Socket();
				socket.connect(new InetSocketAddress(host,port) ,socketTimeout);
			}

			socket.setSoTimeout(soTimeout);
			inputStream = getInputStream();
			outputStream = getOutputStream();
		}catch(IOException io){
			System.out.println("connect() error");
			throw new IOException(io.getMessage());
		}
	}

	public void setSocket(Socket socket)throws IOException{
		this.socket = socket;
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	public String connect(String request) throws IOException{
		BufferedWriter out 	= null;
		BufferedReader in 	= null;
		String response 		= "";
		try{
			connect();
			out = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
			out.write(request);
			out.flush();

			in	= new BufferedReader(new InputStreamReader(getInputStream()));
			String inputLine="";
			while ((inputLine = in.readLine()) != null)
				response += inputLine;
		}catch(IOException io){
			throw new IOException(io.getMessage());
		}finally{
			try{
				in.close();
				out.close();
				socketClose();
			}catch(IOException e){
			}
		}
		return response;
	}

	public byte[] connect(byte[] request, int readLength) throws IOException{
		byte[] response = null;
		try{
			connect();
			outputStream = getOutputStream();
			send(request);
			inputStream = getInputStream();
			response = recv(readLength);
		}catch(IOException io){
			throw new IOException(io.getMessage());
		}finally{
			ioClose();
			socketClose();
		}
		return response;
	}

	/**
	 * readHeader 만큼 데이터를 읽어서 전체의 길이를 리턴 받아 수신하는 경우
	 * KSNET에서 사용
	 *
	 * @param integer readHeader
	 * @param byte[] request
	 * @return
	 * @throws Exception
	 */
	public byte[] connect( int readHeader,byte[] request) throws IOException{
		byte[] response 	= null;
		try{
			connect();
			outputStream = getOutputStream();
			send(request);
			inputStream	=  getInputStream();

			byte[] totalLen = recv(readHeader);
			int expectLength = CommonUtil.parseInt(new String(totalLen));

			response = recv(expectLength);

		}catch(IOException io){
			throw new IOException(io.getMessage());
		}finally{
			ioClose();
			socketClose();
		}
		return response;
	}

	/**
	 * readHeader 만큼 데이터를 읽어서 전체의 길이를 리턴 받아 수신하는 경우
	 *
	 * @param integer readHeader
	 * @param byte[] request
	 * @return
	 * @throws Exception
	 */
	public byte[] connect2( int readHeader,byte[] request) throws IOException{

		byte[] response 	= null;
		try{
			connect();
			outputStream = getOutputStream();
			send(request);
			inputStream	=  getInputStream();

			byte[] lengthLen = recv(readHeader);								//전문의 전체길이를 나타내는 영역 수신
			int expectLength = CommonUtil.parseInt(new String(lengthLen)) ;			//전문의 전체 길이 발췌

			response = new byte[expectLength-readHeader];
			response = recv(response.length);
			response = CommonUtil.byteAppend(lengthLen,response);

		}catch(IOException io){
			throw new IOException(io.getMessage());
		}finally{
			ioClose();
			socketClose();
		}
		return response;
	}

	public byte[] connect3( int readHeader,byte[] request) throws IOException{
		byte[] response 	= null;
		try{
			connect();
			outputStream = getOutputStream();
			send(request);
			inputStream	=  getInputStream();

			byte[] totalLen = recv(readHeader);
			int expectLength = CommonUtil.parseInt(new String(totalLen));

			response = recv(expectLength);
			response = CommonUtil.byteAppend(totalLen,response);
		}catch(IOException io){
			throw new IOException(io.getMessage());
		}finally{
			ioClose();
			socketClose();
		}
		return response;
	}

	public String connect(int readHeader,String request)throws IOException {
		return new String(connect(readHeader,request.getBytes()));
	}


	public byte[] recv(int requestLength)throws IOException{
		byte[] recv = new byte[requestLength];

		boolean run = true;
		int receivedLength = 0;

		while(run){
			byte[] temp = new byte[requestLength-receivedLength];
			int read = inputStream.read(temp);
			CommonUtil.arrayCopy(recv, receivedLength,temp,read);
			receivedLength += read;
			if(receivedLength >= requestLength){
				run = false;
			}
		}
		return recv;
	}

	public byte[] recvAll() throws IOException{

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int bcount = 0;
        byte[] buf = new byte[2048];
        int read_retry_count = 0;
        while(true) {
			int n = inputStream.read(buf);
            if ( n > 0 ) { bcount += n; bout.write(buf,0,n); }
            else if (n == -1) break;
            else  { // n == 0
                if (++read_retry_count >= 5)
                  throw new IOException("inputstream-read-retry-count(5) exceed !");
            }
            if(inputStream.available() == 0){ break; }
        }
        bout.flush();
        byte[] res = bout.toByteArray();
        bout.close();
        return res;

	}

	public String recvString(int requestLength)throws IOException{
		return CommonUtil.toString(recv(requestLength));
	}

	public void send(byte[] response)throws IOException{
		outputStream.write(response,0,response.length);
		outputStream.flush();
	}

	public void ioClose() throws IOException{
		if(inputStream != null){ inputStream.close();}
		if(outputStream != null){ outputStream.close();}
	}


	public void socketClose(){
		if(socket != null){
			try{
				socket.close();
				socket = null;
			}catch(IOException io){
			}
		}
	}














}
