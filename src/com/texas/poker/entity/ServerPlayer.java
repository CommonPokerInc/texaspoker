
package com.texas.poker.entity;

import com.texas.poker.wifi.SocketClient;
import com.texas.poker.wifi.SocketServer;

/*
 * author FrankChan
 * description ���������
 * time 2014-7-29
 *
 */
public class ServerPlayer extends ClientPlayer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SocketServer getServer() {
		return server;
	}

	public void setServer(SocketServer server) {
		this.server = server;
	}

	private transient SocketServer server;
	
	//forbidden
	public ServerPlayer(UserInfo info, SocketClient socket) {
		super(info, socket);
	}
	
	public ServerPlayer(UserInfo info, SocketServer server) {
		this.info = info;
		this.server = server;
	}

}


