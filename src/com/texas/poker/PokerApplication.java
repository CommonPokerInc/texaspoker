package com.texas.poker;


import java.util.LinkedList;
import java.util.List;

import com.texas.poker.entity.ClientPlayer;
import com.texas.poker.entity.LocalUser;
import com.texas.poker.entity.ServerPlayer;
import com.texas.poker.wifi.SocketClient;
import com.texas.poker.wifi.SocketServer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class PokerApplication extends Application{
	public ServerPlayer sp;
	public ClientPlayer cp;
	private SocketServer server;
	private SocketClient client;
	public String ssid;
	private boolean isServer =false; 
	public boolean isConnected = false;
	public boolean isGameStarted =false;
	public static PokerApplication app;
	public LocalUser user;
	private List<Activity> activitys = null;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		app = this;
		activitys = new LinkedList<Activity>();
	}

	public static Context getContext(){
		return app;
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	public SocketServer getServer(){
		return server;
	}
	public void setServer(SocketServer server){
		this.server = server;
		this.isConnected = true;
		setServer(true);
	}
	
	public SocketClient getClient(){
		return client;
	}
	
	public void setClient(SocketClient client){
		this.client = client;
		this.isConnected = true;
		setServer(false);
	}

	public boolean isServer() {
		return isServer;
	}
	
	private void setServer(boolean isServer) {
		this.isServer = isServer;
	}
	
	public void resetServerState(){
		this.isServer =false;
	}
	
	public void addActivity(Activity activity) {

        if (activitys != null && activitys.size() > 0) {

            if(!activitys.contains(activity)){

                activitys.add(activity);

            }

        }else{

            activitys.add(activity);

        }
	}
	
	public void exit() {

        if (activitys != null && activitys.size() > 0) {

            for (Activity activity : activitys) {

                activity.finish();

            }
        }
        System.exit(0);

    }
}
