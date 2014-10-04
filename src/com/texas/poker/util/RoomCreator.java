
package com.texas.poker.util;

import com.texas.poker.entity.Room;

/*
 * author FrankChan
 * description 房间生产器
 * time 2014-10-2
 *
 */
public class RoomCreator {
	
	public final static int TYPE_ONE =1;;
	
	public static Room getRoom(int type,String name){
		Room room = new Room();
		switch(type){
		case TYPE_ONE:
			room.setName(name);
			room.setType(Room.TYPE_LIMIT);
			room.setBasicChips(1000);
			room.setCount(6);
			room.setInnings(5);
			room.setMinStake(40);
			break;
		}
		return room;
	}
}


