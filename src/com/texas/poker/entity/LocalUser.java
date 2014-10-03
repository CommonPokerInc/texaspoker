
package com.texas.poker.entity;

import java.util.ArrayList;
import java.util.Random;

import android.os.Build;

/*
 * author FrankChan
 * description 
 * time 2014-10-3
 *
 */
public class LocalUser {
	
	private String name;
	
	private int money;
	
	private int level;
	
	private int avatar;
	
	private int proertyNum = -1;
	
	public static final int AVATAR_SUM = 6;
	
	private ArrayList<Property>mPropertyList;
	
	public LocalUser(){
		name=Build.BRAND;;
		money=10000;
		level=1;
		avatar = new Random().nextInt(AVATAR_SUM);
		mPropertyList = new ArrayList<Property>();
	}
	
	//根据本地的用户信息转换成通信的用户类型
	public UserInfo convertToUserInfo(){
		UserInfo info = new UserInfo();
		info.setName(this.name);
		info.setAvatar(this.avatar);
		info.setLevel(this.level);
		//转换时id和IP先不加上
		return info;
	}
	
	//是否有道具
	public boolean hasProperty(){
		return mPropertyList.size()>0;
	}

	//是否拥有该道具类型
	public boolean ownProperty(int type){
		for(Property mProperty:mPropertyList){
			if(mProperty.getType()==type){
				return true;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public int getProertyNum() {
		return proertyNum;
	}

	public void setProertyNum(int proertyNum) {
		this.proertyNum = proertyNum;
	}

	public ArrayList<Property> getPropertyList() {
		return mPropertyList;
	}

	public void addPropertyList(Property property) {
		this.mPropertyList.add(property);
	}

	public static int getAvatarSum() {
		return AVATAR_SUM;
	}
	
	
	
}


