
package com.texas.poker.util;

import com.texas.poker.entity.Room;

/*
 * author FrankChan
 * description 房间生产器
 * time 2014-10-2
 *
 */
public class RoomCreator {
	
	public final static int TYPE_ONE =1;
	
	public final static int TYPE_TWO =2;
	
	public final static int TYPE_THREE =3;
	
	public static Room getRoom(int type,String name){
		Room room = new Room();
		switch(type){
		case TYPE_ONE:
			room.setName(name);
			room.setType(Room.TYPE_RANK);
			room.setBasicChips(1000);
			room.setCount(6);
			room.setInnings(-1);
			room.setMinStake(20);
			break;
		case TYPE_TWO:
			room.setName(name);
			room.setType(Room.TYPE_RANK);
			room.setBasicChips(10000);
			room.setCount(6);
			room.setInnings(-1);
			room.setMinStake(60);
			break;
		case TYPE_THREE:
			room.setName(name);
			room.setType(Room.TYPE_RANK);
			room.setBasicChips(4000);
			room.setCount(6);
			room.setInnings(-1);
			room.setMinStake(40);
			break;
		}
		return room;
	}
}


