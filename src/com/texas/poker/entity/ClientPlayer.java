
package com.texas.poker.entity;

import java.io.Serializable;

import com.texas.poker.wifi.SocketClient;

/*
 * author FrankChan
 * description �ͻ������
 * time 2014-7-29
 *
 */
public class ClientPlayer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected UserInfo info;
	
	private transient SocketClient socket;
	
	public ClientPlayer(UserInfo info,SocketClient socket){
		this.setInfo(info);
		this.setSocket(socket);
	}
	
	public ClientPlayer(){}
	public UserInfo getInfo() {
		return info;
	}
	public void setInfo(UserInfo info) {
		this.info = info;
	}
	public SocketClient getSocket() {
		return socket;
	}
	public void setSocket(SocketClient socket) {
		this.socket = socket;
	}

}


