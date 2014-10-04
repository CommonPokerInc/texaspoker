
package com.texas.poker.wifi.message;

import java.util.ArrayList;

import com.texas.poker.entity.Poker;

/*
 * author FrankChan
 * description ��Ϸ��ʼ�����Ϣ����
 * time 2014-8-16
 *
 */
public class GameMessage extends BaseMessage{

	private static final long serialVersionUID = 1L;
	
	//����
	public final static int ACTION_ABANDOM = 1;
	//����
	public final static int ACTION_GIVE_IN = 2;
	//��ע
	public final static int ACTION_FOLLOW =3;
	//��ע
	public final static int ACTION_ADD_BET =4;

	public final static int ACTION_SHOW_PUBLIC_POKER =5;

	public final static int ACTION_SEND_BOOL = 6;

	public final static int ACTION_SEND_FOUR = 7;

	public final static int ACTION_SEND_FIVE = 8;

	public final static int ACTION_CURRENT_PERSON = 9;

	public final static int ACTION_FINISH_OPTIOIN = 10;

	public final static int ACTION_UPDATE_MONEY = 11;
	
	public final static int ACTION_NEXT_ROUND = 12;
	
	public final static int ACTION_RESET_ROUND = 13;

	public final static int ACTION_GAME_OVER = 14;

	
	private int id;
	
	private int action;
	
	private ArrayList<Poker>pokerList;

	private int amount;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public ArrayList<Poker> getPokerList() {
        return pokerList;
    }

    public void setPokerList(ArrayList<Poker> pokerList) {
        this.pokerList = pokerList;
    }
	
}


