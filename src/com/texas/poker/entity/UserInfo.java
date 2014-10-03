
package com.texas.poker.entity;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * author FrankChan
 * description 
 * time 2014-8-25
 *
 */
public class UserInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String ip;
	
	private String id;
	
	private int avatar;
	
	private int level;
	
	private int proertyNum = -1;
	
	private boolean isQuit ;
	
	private int baseMoney;
	
	private int aroundChip;
	
	private int aroundSumChip = 0;
	
	private int cardType = 0;
	
	private ArrayList<Poker> pokerBack;
	
	public boolean hasProperty(){
		return proertyNum ==-1;
	}
	
	public int getCardType() {
		return cardType;
	}
	
	public void setCardType(int cardType) {
        this.cardType = cardType;
    }

	public ArrayList<Poker> getPokerBack() {
        return pokerBack;
    }

    public void setPokerBack(ArrayList<Poker> pokerBack) {
        this.pokerBack = pokerBack;
    }

	public int getAroundSumChip() {
        return aroundSumChip;
    }

    public void setAroundSumChip(int aroundSumChip) {
        this.aroundSumChip = aroundSumChip;
    }

    public boolean isQuit() {
		return isQuit;
	}

	public void setQuit(boolean isQuit) {
		this.isQuit = isQuit;
	}

	public int getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(int baseMoney) {
		this.baseMoney = baseMoney;
	}

	public int getAroundChip() {
		return aroundChip;
	}

	public void setAroundChip(int aroundChip) {
		this.aroundChip = aroundChip;
	}

	public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getProertyNum() {
		return proertyNum;
	}

	public void setProertyNum(int proertyNum) {
		this.proertyNum = proertyNum;
	}
}


