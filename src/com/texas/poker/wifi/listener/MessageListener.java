
package com.texas.poker.wifi.listener;


/*
 * author FrankChan
 * description 
 * time 2014-8-16
 *
 */
public interface MessageListener {
	
	public void onServerReceive(PeopleMessage msg);
	
	public void onServerReceive(GameMessage msg);
	
	public void onClientReceive(PeopleMessage msg);
	
	public void onClientReceive(GameMessage msg);
	
	public void onServerSendFailure();
	
	public void onServerSendSuccess();
	
	public void onClientSendFailure();
	
	public void onClientSendSuccess();
	
}


