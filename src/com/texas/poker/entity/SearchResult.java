
package com.texas.poker.entity;

import com.texas.poker.util.RoomCreator;
import com.texas.poker.wifi.WifiApConst;

/*
 * author FrankChan
 * description wifi搜索的结果实体
 * time 2014-10-2
 *
 */
public class SearchResult {
	private int type;
	private String name;
	private int money;
	private String fullName;
	
	public SearchResult(String result){
		fullName = result;
		try{
			name = result.substring(WifiApConst.WIFI_AP_HEADER.length()-1);
			type = Integer.parseInt(result.substring((result.length()-1)));
		}catch(Exception ex){
			type = RoomCreator.TYPE_ONE;
		}
		switch(type){
		case RoomCreator.TYPE_ONE:
			money = 1000;
			break;
		default:
			break;
		}
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMoney(){
		return money;
	}
	
	public String getFullName(){
		return fullName;
	}
}


