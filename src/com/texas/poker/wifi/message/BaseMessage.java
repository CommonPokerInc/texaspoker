
package com.texas.poker.wifi.message;

import java.io.Serializable;

import com.texas.poker.entity.Room;

/*
 * author FrankChan
 * time 2014-8-16
 *
 */
public abstract class BaseMessage implements Serializable{
	private static final long serialVersionUID = 1L;

	public final static String MESSAGE_SOURCE = "commonpoker";
	
	private String source;
	
	private boolean exit;
	
	private String extra;
	
	private Room room;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	
}


