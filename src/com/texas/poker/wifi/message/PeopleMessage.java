
package com.texas.poker.wifi.message;

import java.util.ArrayList;

import com.texas.poker.entity.ClientPlayer;
import com.texas.poker.entity.Poker;

/*
 * author FrankChan
 * description 
 * time 2014-8-16
 *
 */
public class PeopleMessage extends BaseMessage {

	private static final long serialVersionUID = 1L;
	

	private boolean start;
	

	private ArrayList<ClientPlayer>playerList;
	
	private ArrayList<Poker>pokerList;

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public ArrayList<ClientPlayer> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(ArrayList<ClientPlayer> playerList) {
		this.playerList = playerList;
	}

	public ArrayList<Poker> getPokerList() {
		return pokerList;
	}

	public void setPokerList(ArrayList<Poker> pokerList) {
		this.pokerList = pokerList;
	}
}


