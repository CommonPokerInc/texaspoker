
package com.texas.poker;

import android.os.Environment;

/*
 * author FrankChan
 * description static values
 * time 2014-8-7
 *
 */
public class Constant {
	
	public final static String LOCAL_HOST = "http://192.168.43.1:";
	
	public final static String WIFI_SUFFIX = "Poker_";
	
	public final static int SOCKET_PORT = 9527; 
	
	public final static String  DIRECTORY = Environment.getExternalStorageDirectory().getPath()+"/.texas/";
	
	public final static String USER_INFO_NAME = "userinfo";
	
	public final static String APK_NAME = "TexasPoker.apk";
	
	public final static String APP_PACKAGE_NAME = "com.texas.poker";
	
	public final static String WIFI_NAME_TRANSFER = "Dezhou_Joinme";
	
	
	
	public final static String ACTON_CLOSE_MAIN = "Action.intent.close.MainPage";
}


