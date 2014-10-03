package com.texas.poker.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.texas.poker.wifi.listener.CommunicationListener;
import com.texas.poker.wifi.listener.WifiClientListener;

import android.util.Log;

public class SocketServer {

	private ServerSocket server;

	private static SocketServer serverSocket;

	//public static List<Socket> socketQueue = new ArrayList<Socket>();

	public static HashMap<String,Socket>socketMap = new HashMap<String,Socket>();
	
	private static final String TAG = "SocketServer";

	private int mPort;

	private CommunicationListener listener;
	
	private WifiCreateListener createListener;
	
	private boolean joinForbidden = false;
	
	public interface WifiCreateListener{
		void onCreateSuccess();
		void OnCreateFailure(String strError);
	}
	

	public void freeAll(){
		serverSocket = null;
		server = null;
		joinForbidden = false;
		onGoinglistner =true;
		socketMap.clear();
	}
	
	private boolean onGoinglistner = true;

	public static synchronized SocketServer newInstance(int port) {
		if (serverSocket == null) {
			serverSocket = new SocketServer(port);
		}
		return serverSocket;
	}
	
	public static synchronized SocketServer newInstance() {
		return serverSocket;
	}
	
	public void setClientListener(WifiClientListener clientListener){
	}
	
	private void closeConnection() {
		Log.i(TAG, "into closeConnection()...................................");
		if (server != null) {
			try {
				server.close();
				server = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i(TAG, "out closeConnection()...................................");
	}

	public void clearServer() {
		closeConnection();
	}

	private SocketServer(final int port) {
		this.mPort = port;
	}

	public interface SocketCreateListener{
		void onSuccess();
		void onFailure();
	}
	
	
	public void createServerSocket(SocketCreateListener cListener){
		try {
			server = new ServerSocket();
			server.setReuseAddress(true);
			InetSocketAddress address = new InetSocketAddress(mPort);
			server.bind(address);
			cListener.onSuccess();
			Log.i(TAG, "server  =" + server);
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.d(TAG, "server int fail ");
			cListener.onFailure();
		}
	}
	
	public void beginListen(final WifiClientListener clientListener) {
		setClientListener(clientListener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (server != null) {
					while (onGoinglistner) {
						try {
							final Socket socket = server.accept();
							if (socket != null) {
								if (!joinForbidden&&!socketMap.containsValue(socket)) {
									//socketQueue.add(socket);
									socketMap.put(socket.getInetAddress().getHostName(), socket);
									if(null!=clientListener){
										clientListener.clientIncrease(socket.getInetAddress().getHostName());
									}
								}
								serverAcceptMsg(socket);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	//�Ե����ͻ��˷�����Ϣ,�����ڿ�ʼ������Ϊ��ſ�ִ��
	public void sendMessage(final Socket client, final String msg) {
		Log.i(TAG, "into sendMsg(final Socket client,final ChatMessage msg) msg = " + msg);
		PrintWriter out = null;
		if (client.isConnected()) {
			if (!client.isOutputShutdown()) {
				try {
					out = new PrintWriter(client.getOutputStream());
					out.println(msg);
					out.flush();
					if(listener!=null){
						listener.onSendSuccess();
					}
					Log.i(TAG, "into sendMsg(final Socket client,final ChatMessage msg) msg = " + msg + " success!");
				} catch (IOException e) {
					e.printStackTrace();
					if(listener!=null){
						listener.onSendFailure(e.getMessage());
					}
					Log.d(TAG, "into sendMsg(final Socket client,final ChatMessage msg) fail!");
				}
			}
		}
		Log.i(TAG, "out sendMsg(final Socket client,final ChatMessage msg) msg = " + msg);
	}

	public CommunicationListener getListener() {
		return listener;
	}

	public void setListener(CommunicationListener listener) {
		this.listener = listener;
	}

	public void sendMessageToAllClients(final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(socketMap.size()>0){
					for(String str:socketMap.keySet()){
						sendMessage(socketMap.get(str),msg);
					}
				}
			}
		}).start();
	}

	private void serverAcceptMsg(final Socket socket) {
		new Thread(new Runnable() {
			public void run() {
				BufferedReader in;
				try {
					while (!socket.isClosed()) {
						in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
						String str = in.readLine();
						if (str == null || str.equals("")) {
							break;
						}
						Log.i(TAG, "client" + socket + "str =" + str);
						if(listener!=null){
							listener.onStringReceive(str);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void sendAcktoAllClients(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(socketMap.size()>0){
					for(String str:socketMap.keySet()){
						sendMessage(socketMap.get(str),str);
					}
				}
			}
		}).start();
	}
	
	public int getConnectNumber() {
		return socketMap.size();
	}

	public void stopListner() {
		onGoinglistner = false;
	}

	public WifiCreateListener getCreateListener() {
		return createListener;
	}

	public void setCreateListener(WifiCreateListener createListener) {
		this.createListener = createListener;
	}

	public boolean isJoinForbidden() {
		return joinForbidden;
	}

	public void setJoinForbidden(boolean joinForbidden) {
		this.joinForbidden = joinForbidden;
	}
	
}