
package com.texas.poker.entity;

import com.texas.poker.R;

import com.texas.poker.PokerApplication;

/*
 * author FrankChan
 * description 道具
 * time 2014-10-3
 *
 */
public class Property {
	
	public static final int TYPE_ONE = 0;
	
	public static final int TYPE_TWO = 1;
	
	public static final int TYPE_THREE = 2;
	
	public static final int TYPE_FOUR = 3;
	
	public static final int TYPE_FIVE = 4;
	
	private final float[]RATES = {1f,1.05f, 1.2f,0.8f,1.25f};
	
	private final int[]COSTS ={30000,100000,200000,200000,300000};
	
	private final String[]DESCRIPTION = PokerApplication.
					getContext().getResources().getStringArray(R.array.property_descrption);
	
	private final String[]NAME = PokerApplication
			.getContext().getResources().getStringArray(R.array.property_name);
	
	private int type;
	
	private int cost;
	
	private String name;
	
	private String description;

	public Property(int type){
		if(type>RATES.length){
			return;
		}
		this.type = type;
		switch(type){
		case TYPE_ONE:
			name= "德州小妹";
			break;
		case TYPE_TWO:
			break;
		case TYPE_THREE:
			break;
		case TYPE_FOUR:
			break;
		case TYPE_FIVE:
			break;
		}
		cost = COSTS[type];
		name = NAME[type];
		description = DESCRIPTION[type];
	}
	
	public int getType() {
		return type;
	}

	public int getCost() {
		return cost;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}


