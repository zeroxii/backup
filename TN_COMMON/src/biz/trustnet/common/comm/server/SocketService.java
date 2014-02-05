/*
 * Project Name	:   TN_COMMON
 * File Name		:	SocketService.java
 * Date				:	2005. 3. 31. - ���� 2:11:57
 * History			:	2005. 3. 31.
 * Version			:	1.0
 * Author			:	
 * Comment      	:
 */

package biz.trustnet.common.comm.server;



import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.LinkedList;

public class SocketService {

	private ServerSocket serverSocket 	= null;
	private Thread serviceThread 		= null;
	private boolean running 			= false;
	private SocketServer itsServer		= null;
	private LinkedList threads			= new LinkedList();

	public SocketService(int port , SocketServer server) throws Exception{
		itsServer = server;
		serverSocket 	= new ServerSocket(port);
		serviceThread 	= new Thread( new Runnable() {
			public void run() {
				serviceThread();
			}
		});

		serviceThread.start();
	}

	public void close() throws Exception {
		waitForServiceThreadToStart();
		running = false;
		serverSocket.close();
		serviceThread.join();
		waitForServerThreads();
	}

	private void waitForServiceThreadToStart() {
		while(running == false)
			Thread.yield();
	}

	private void serviceThread() {
		running = true;
		while(running) {
			try {
				Socket socket = serverSocket.accept();
				startServerThread(socket);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startServerThread(Socket socket) {
		Thread serverThread = new Thread(new ServerRunner(socket));
		synchronized(threads) {
			threads.add(serverThread);
		}
		serverThread.start();
	}

	private void waitForServerThreads() throws InterruptedException {
		while(threads.size() > 0) {
			Thread t;
			synchronized(threads) {
				t = (Thread)threads.getFirst();
			}
			t.join();
		}
	}



	public class ServerRunner implements Runnable {

		private Socket itsSocket = null;

		ServerRunner(Socket socket){
			itsSocket = socket;
		}

		public void run() {
			try {
				itsServer.serve(itsSocket);
				synchronized(threads) {
					threads.remove(Thread.currentThread());
				}
				itsSocket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}


}
