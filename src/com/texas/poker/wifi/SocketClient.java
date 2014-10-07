package com.texas.poker.wifi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.texas.poker.wifi.listener.CommunicationListener;

import android.util.Log;

public class SocketClient {
	static Socket client;

	static BufferedReader in;

	private static SocketClient socketClient;

	private static final String TAG = "SocketClient";

	private String site;

	private int port;

	private boolean onGoinglistner = true;
	
	private CommunicationListener listener;
	
	private ClientConnectListener connListener;

	public void freeAll(){
		client = null;
		socketClient = null;
		onGoinglistner = true;
	}
	
	public static interface ClientConnectListener {
		public void onSuccess();
		public void onFailure(String errorInfo);
	}

	public static synchronized SocketClient newInstance(String site, int port) {

		if (socketClient == null) {
			socketClient = new SocketClient(site, port);
		}
		Log.i(TAG, "socketClient =" + socketClient);
		return socketClient;
	}

	private SocketClient(String site, int port) {

		this.site = site;
		this.port = port;
	}

	public void connectServer(final ClientConnectListener connListener) {
		Log.i(TAG, "into connectServer()");
		this.setConnListener(connListener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client = new Socket(site, port);
					Log.i(TAG, "Client is created! site:" + site + " port:" + port);
					connListener.onSuccess();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					connListener.onFailure(e.getMessage());
					Log.d(TAG, "UnknownHostException");
				} catch (IOException e) {
					e.printStackTrace();
					connListener.onFailure(e.getMessage());
					Log.d(TAG, "IOException");
				}
			}
		}).start();
		Log.i(TAG, "out connectServer()");
	}

	public String sendMessage(final String msg) {
		Log.i(TAG, "into sendMsgsendMsg(final ChatMessage msg)  msg =" + msg);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (client != null && client.isConnected()) {
						if (!client.isOutputShutdown()) {
							PrintWriter out = new PrintWriter(client.getOutputStream());
							out.println(msg);
							out.flush();
							if(null!=listener)
								listener.onSendSuccess();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					if(null!=listener)
						listener.onStringReceive(e.getMessage());
					Log.d(TAG, "client snedMsg error!");
				}
			}
		}).start();
		return "";
	}

	private void closeConnection() {
		try {
			if (client != null && client.isConnected()) {
				client.close();
				client = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void beganAcceptMessage(final CommunicationListener listener) {
		setCommunicationListener(listener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (onGoinglistner) {
					if (client != null) {
						if (client.isConnected()) {
							if (!client.isInputShutdown()) {
								try {
									in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
									String chatMsg = in.readLine();
									if(chatMsg==null){
										stopAcceptMessage();
									}
									Log.i(TAG, "into acceptMsg()  chatMsg =" + chatMsg);
									if (chatMsg != null && !chatMsg.equals("")) {
										listener.onStringReceive(chatMsg);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}).start();
	}

	public String getLocalAddress(){
		return client.getLocalAddress().toString();
	}
	
	public void clearClient() {
		closeConnection();
	}

	public void stopAcceptMessage() {
		onGoinglistner = false;
	}

	public ClientConnectListener getConnListener() {
		return connListener;
	}

	public void setConnListener(ClientConnectListener connListener) {
		this.connListener = connListener;
	}

	public CommunicationListener getCommunicationListener() {
		return listener;
	}

	public void setCommunicationListener(CommunicationListener listener) {
		this.listener = listener;
	}
}