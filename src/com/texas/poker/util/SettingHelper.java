
package com.texas.poker.util;

import android.content.Context;

/**
 * 
 * author FrankChan
 * description �û�����
 * time 2014-8-26
 *
 */
public class SettingHelper {
	
	private final static String COPY_DONE = "copy_done";
	
	private final static String FIRST_START = "first_start";
	
	private final static String NICKNAME = "nickname";
	
	private final static String AVATAR_NUMBER = "avatar_number";
	
	private final static String WIN_TIMES = "win_times";
	
	private final static String PLAY_TIMES = "play_times";
	
	private final static String MAX_POKER ="max_poker";
	
	private final static String POWER = "power";
	
	private final static String VOICE ="voice";
	
	private final static String VIBRATION = "VIBRATION";
	
	private PreferenceHelper helper;
	
	public SettingHelper(Context ctx){
		helper = new PreferenceHelper(ctx);
	}
	
	public boolean hasCopy(){
		return helper.getBoolean(COPY_DONE);
	}
	
	public synchronized void setCopied(boolean value){
		helper.setBoolean(COPY_DONE, value);
	}
	
	public boolean isFirstStart(){
		return helper.getBoolean(FIRST_START,true);
	}
	
	public void setFirstStart(boolean first){
		helper.setBoolean(FIRST_START, first);
	}
	
	public String getNickname(){
		return helper.getString(NICKNAME);
	}

	public void setNickname(String name){
		helper.setString(NICKNAME, name);
	}
	
	public int getAvatarNumber(){
		return helper.getInteger(AVATAR_NUMBER);
	}
	
	public void setAvatarNumber(int avatar){
		helper.setInteger(AVATAR_NUMBER, avatar);
	}

	public void setWinTimes(int times){
		helper.setInteger(WIN_TIMES, times);
	}
	
	public int getWinTimes(){
		return helper.getInteger(WIN_TIMES);
	}
	
	/**
	 * ʤ�����һ
	 */
	public void addWinTimes(){
		int times = helper.getInteger(WIN_TIMES);
		setWinTimes(++times);
	}
	
	public void setPlayTimes(int times){
		helper.setInteger(PLAY_TIMES, times);
	}
	
	public int getPlayTimes(){
		return helper.getInteger(PLAY_TIMES);
	}
	
	/**
	 * ���泡���һ
	 */
	public void addPlayTimes(){
		int times = helper.getInteger(PLAY_TIMES);
		setPlayTimes(++times);
	}
	
	
	/** 
	 * 
	 * ��ȡ��������ַ��ö��Ÿ���
	 * ���� "5,6,7,8,9"
	 */
	public String getMaxPoker() {
		return helper.getString(MAX_POKER,"�����Ϸ����Ϊ��");
	}

	/** 
	 * ����Ϊ��������˳���м���Ӣ�Ķ��Ÿ���
	 * ���� "5,6,7,8,9" �� "6,7,8,9,10"
	 * @param strPokerList
	 */
	public void setMaxPoker(String strPokerList){
		helper.setString(MAX_POKER, strPokerList);
	}
	
	public int getPower() {
		return helper.getInteger(POWER);
	}

	public void setPower(int power){
		helper.setInteger(POWER, power);
	}
	
	/**
	 * �Ƿ�����������Ч��Ĭ��Ϊ��
	 */
	public boolean getVoiceStatus() {
		return helper.getBoolean(VOICE, true);
	}
	
	public void setVoiceStatus(boolean status){
		helper.setBoolean(VOICE, status);
	}

	/**
	 * �Ƿ����������𶯣�Ĭ��Ϊ��
	 */
	public boolean getVibrationStatus() {
		return helper.getBoolean(VIBRATION,true);
	}
	
	public void setVibrationStatus(boolean status){
		helper.setBoolean(VIBRATION, status);
	}
}


