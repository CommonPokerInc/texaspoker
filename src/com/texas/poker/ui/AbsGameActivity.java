
package com.texas.poker.ui;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.texas.poker.wifi.SocketServer;
import com.texas.poker.wifi.listener.CommunicationListener;
import com.texas.poker.wifi.listener.MessageListener;
import com.texas.poker.wifi.listener.WifiClientListener;
import com.texas.poker.wifi.message.BaseMessage;
import com.texas.poker.wifi.message.GameMessage;
import com.texas.poker.wifi.message.PeopleMessage;

/*
 * author FrankChan
 * description 通信基础抽象类
 * time 2014-8-16
 *
 */
public abstract class AbsGameActivity extends AbsBaseActivity 
		implements CommunicationListener,MessageListener,WifiClientListener{
	
	private String mSSID ;
	
	private Timer mTimer;
	
	private BackHandler mHandler;

	private HandlerThread mThread;
	
	private final static int MSG_SEND_ACK = 1;
	
	private final static int MSG_RECEIVE_ACK = 2;
	
	private final static int INTERVAL_SEND_MSG = 4000;
	
	private final static int INTERVAL_MAX_ACK = 8000;
	
	private final static int INTERVAL_AFTER_START = 1000;
	
	private final static int INTERVAL_AFTER_SEND = 5000;
	
	private boolean allowSend = true;
	
	private boolean allowAck =true;
	
	private long mReceiveTime;
	
	private HashMap<String, Long>timeMap = new HashMap<String,Long>();
	
	protected void registerListener(){
		try{
		mSSID = getIntent().getStringExtra("SSID");
		app.ssid = mSSID;
		if(app.isServer()){
			app.getServer().setListener(this);
			app.getServer().beginListen(this);
			initTimeMap();
		}else{
			app.getClient().beganAcceptMessage(this);
			mReceiveTime = System.currentTimeMillis();
		}
		//initBackHandler();
		}catch(Exception ex){
			Log.i("frankchan", "连接基础出错");
		}
		
	}
	
	@SuppressLint("HandlerLeak")
	class BackHandler extends Handler{
		
		public BackHandler(Looper looper) {
			// TODO Auto-generated constructor stub
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_RECEIVE_ACK:
				String mapKey = (String) msg.obj;
				if(app.isServer()&&timeMap.keySet().contains(mapKey)){
					//update relevant map
					timeMap.put(mapKey, Long.valueOf(System.currentTimeMillis()));
				}else if(!app.isServer()){
					//update client receive time
					mReceiveTime = System.currentTimeMillis();
					//send acknowledge back
					app.getClient().sendMessage(mapKey);
				}
				break;
			case MSG_SEND_ACK:
				break;
			}
		}
		
	}
	
	protected void forbidJoin(){
		app.getServer().setJoinForbidden(true);
	}
	
	@Override
	public void onSendSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStringReceive(String strInfo) {
		// TODO Auto-generated method stub
		if(checkEnvError()){
			Log.e("frankchan", "配置没完成");
			return;
		}
		if(strInfo.length()<=15&&strInfo.startsWith("192.168")){
			Message msg = mHandler.obtainMessage(MSG_RECEIVE_ACK, strInfo);
			mHandler.sendMessage(msg);
			return;
		}
		if(app.isGameStarted){
			GameMessage gm = new Gson().fromJson(strInfo, GameMessage.class);
			if(BaseMessage.MESSAGE_SOURCE.equals(gm.getSource())){
				if(app.isServer()){
					this.onServerReceive(gm);
				}else{
					this.onClientReceive(gm);
				}
			}
		}else{
			PeopleMessage pm = new Gson().fromJson(strInfo, PeopleMessage.class);
			if(BaseMessage.MESSAGE_SOURCE.equals(pm.getSource())){
				if(app.isServer()){
					this.onServerReceive(pm);
				}else{
					this.onClientReceive(pm);
				}
			}
		}
	}
	@Override
	public void onSendFailure(String errInfo) {
		// TODO Auto-generated method stub
		if(checkEnvError()){
			Log.e("frankchan", "配置没完成");
			return;
		}
		if(app.isServer()){
			this.onServerSendFailure();
		}else{
			this.onClientSendFailure();
		}
	}
	
	
	protected boolean checkEnvError(){
		return null==app||app.isConnected==false;
	}
	
	protected void sendMessage(BaseMessage message){
		Log.e("frankchan", "发送");
		String strMessage = null;
		if(message instanceof PeopleMessage){
			strMessage = new Gson().toJson(message,PeopleMessage.class);
		}else if(message instanceof GameMessage){
			strMessage = new Gson().toJson(message,GameMessage.class);
		}else{
			strMessage = "";
		}
		if(app.isServer()){
			app.getServer().sendMessageToAllClients(strMessage);
		}else{
			app.getClient().sendMessage(strMessage);
		}
	}
	
	private void initBackHandler(){
		try{
			mThread = new HandlerThread("BeatDetector");
			mThread.start();
			mHandler = new BackHandler(mThread.getLooper());
			mTimer =new Timer();
		if(app.isServer()){
			mHandler.postDelayed(new SendRunnable(),INTERVAL_AFTER_START);
		}
			mHandler.postDelayed(new AckRunnable(), INTERVAL_AFTER_SEND);
		}catch(Exception ex){
			Log.e("frankchan", "脉搏器出错\n"+ex.getMessage());
		}
	}

	private void initTimeMap(){
		app.getServer();
		for(String key :SocketServer.socketMap.keySet()){
			timeMap.put(key, System.currentTimeMillis());
		}
	}
	
	//通知时间表根据ClientQueue增删做出变化
	private synchronized void addTimeMapByTag(String tag){
		if(!timeMap.keySet().contains(tag)){
			timeMap.put(tag, System.currentTimeMillis());
		}
	}
	
	//根据IP删除ClientQueue里面的失联socket
	private synchronized void removeClientByTag(String tag){
		if(timeMap.keySet().contains(tag)){
			timeMap.remove(tag);
		}
	}
	
	class SendRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mTimer!=null){
				mTimer.schedule(mSendTask, 0,INTERVAL_SEND_MSG);
			}
		}
		
	}
	
	class AckRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mTimer!=null){
				mTimer.schedule(mCheckTask, 0,INTERVAL_MAX_ACK);
			}
		}
		
	}
	

	
	private SendTask mSendTask =new SendTask();
	
	//定时发送心跳包的线程
	class SendTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(allowSend){
				if(app.isServer()){
					app.getServer().sendAcktoAllClients();
				}
			}
		}
		
	}
	
	private void stopBeatSendAndCheck(){
		allowAck = false;
		allowSend = false;
		if(mCheckTask!=null){
			mCheckTask.cancel();
		}
		if(mSendTask!=null){
			mSendTask.cancel();
		}
		if(mTimer!=null){
			mTimer.cancel();
			mTimer= null;
		}
	}
	
	private CheckTask mCheckTask =new CheckTask();
	
	//定时检测消息接收异常的线程
	class CheckTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(allowAck){
				if(app.isServer()){
					for(String key:timeMap.keySet()){
						if(System.currentTimeMillis()-timeMap.get(key)>INTERVAL_MAX_ACK){
							Log.e("frankchan", "用户地址为"+key+"的用户没有及时应答");
							//frankchan 丢失用户的处理Socket和Client回调，交由子类实现
							removeClientByTag(key);
							clientDecrease(key);
						}else{
							Log.i("frankchan", key+"客户端按时应答");
							timeMap.put(key, Long.valueOf(System.currentTimeMillis()));
						}
					}
				}else{
					long mInterval = System.currentTimeMillis()-mReceiveTime;
					if(mInterval>INTERVAL_MAX_ACK){
						Log.e("frankchan", "没有及时收到服务器的消息");
						app.getClient().stopAcceptMessage();
						disconnectFromServer((int)mInterval/1000);
					}else{
						Log.i("frankchan", "Server及时应答");
						
					}
				}
			}
		}
		
	}

	
	@Override
	public void clientIncrease(String clientName) {
		// TODO Auto-generated method stub
		addTimeMapByTag(clientName);
	}

	/**
	 * 服务器丢失对应IP地址的用户
	 * @param clientName
	 */
	public abstract void clientDecrease(String clientName);
	
	/**
	 * 客户端和服务器失联超过了秒
	 */
	public abstract void disconnectFromServer(int sec);
	
	
	public void restartApplication() {  
		stopBeatSendAndCheck();
		if(app.isServer()){
			app.getServer().stopListner();
			app.getServer().clearServer();
			app.setServer(null);
		}else{
			app.getClient().stopAcceptMessage();
			app.getClient().clearClient();
			app.setServer(null);
		}
		android.os.Process.killProcess(android.os.Process.myPid());
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        startActivity(intent);  
        this.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		try{
//		if(null!=mThread){
//			mThread.quit();
//		}
//		if(app.isServer()){
//			app.getServer().stopListner();
//			app.getServer().clearServer();
//			SocketServer.newInstance().freeAll();
//			app.wm.closeAWifiHot();
//			app.setServer(null);
//		}else{
//			app.getClient().stopAcceptMessage();
//			app.getClient().clearClient();
//			//app.getClient().freeAll();
//			app.setClient(null);
//			if(null!=mSSID){
//				app.wm.deleteMoreCon(mSSID);
//			}
//			app.wm.disconnectWifi(mSSID);
//		}
//		app.resetServerState();
//		app.isConnected = false;
//		app.isGameStarted =false;
//		}catch(Exception ex){
//			Log.e("frankchan", "退出游戏界面异常捕捉");
//		}
		super.onDestroy();
	}
}


