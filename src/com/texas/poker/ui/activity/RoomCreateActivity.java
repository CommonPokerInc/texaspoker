package com.texas.poker.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.texas.poker.Constant;
import com.texas.poker.R;
import com.texas.poker.entity.Room;
import com.texas.poker.entity.ServerPlayer;
import com.texas.poker.entity.UserInfo;
import com.texas.poker.ui.AbsBaseActivity;
import com.texas.poker.ui.dialog.ConfirmDialog;
import com.texas.poker.ui.dialog.ConfirmDialog.DialogConfirmInterface;
import com.texas.poker.ui.dialog.Effectstype;
import com.texas.poker.util.RoomCreator;
import com.texas.poker.util.SystemUtil;
import com.texas.poker.util.TextUtils;
import com.texas.poker.wifi.SocketServer;
import com.texas.poker.wifi.SocketServer.SocketCreateListener;
import com.texas.poker.wifi.WifiApConst;
import com.texas.poker.wifi.WifiapBroadcast;

public class RoomCreateActivity extends AbsBaseActivity implements DialogConfirmInterface,OnClickListener{

	private ConfirmDialog mDialog;
	
	private ProgressDialog mProgressDialog;
	
	private int wifiapOperateEnum = WifiApConst.NOTHING;
	
	private CreateAPProcess mCreateApProcess;
	
	private WifiapBroadcast mWifiapBroadcast;
	
	private int mRoomType = RoomCreator.TYPE_ONE;
	
	private String mSSID = "";
	
	private Room room;
	
	private boolean isCreating = false;
	
	private final static int MSG_CREATE_SERVER_SOCKET = 8;
	
	private final static int MSG_SUCCESS_JUMP_GAME =10;
	
	private final static int MSG_SHOW_CREATE_SOCKET_ERROR =11;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_cretae);
		registerBroadcast();
		initViews();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 如果不支持热点创建
        if (mWifiUtils.getWifiApStateInt() == 4) {
            showToast(R.string.room_create_not_support);
            return;
        }

        // 如果wifi正打开着的，就提醒用户
        if (mWifiUtils.mWifiManager.isWifiEnabled()) {
            wifiapOperateEnum = WifiApConst.CREATE;
            mDialog.setTopic(getString(R.string.room_create_close_wifi));
            mDialog.show();
            return;
        }

        // 如果已经存在一个其他的共享热点
        if (((mWifiUtils.getWifiApStateInt() == 3) || (mWifiUtils.getWifiApStateInt() == 13))
                && (!mWifiUtils.getApSSID().startsWith(
                        WifiApConst.WIFI_AP_HEADER))) {
            wifiapOperateEnum = WifiApConst.CREATE;
            mDialog.setTopic(getString(R.string.room_create_close_current_ap));
            mDialog.show();
            return;
        }

        // 如果存在一个同名的共享热点
        if (((mWifiUtils.getWifiApStateInt() == 3) || (mWifiUtils.getWifiApStateInt() == 13))
                && (mWifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER))) {
            wifiapOperateEnum = WifiApConst.CLOSE;
            mDialog.setTopic(getString(R.string.room_create_close_same_ap));
            mDialog.show();
            return;
        }
        
        mWifiUtils.closeWifi();
        wifiapOperateEnum = WifiApConst.CREATE;
        mDialog.setTopic(getString(R.string.room_create_close_wifi));
        mDialog.show();
        return;
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_create_room).setOnClickListener(this);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(getString(R.string.room_create_waiting));
		mDialog = new ConfirmDialog(this, 500, Effectstype.Fadein, this);
	}
	
	/** 动态注册广播 */
    public void registerBroadcast() {
        mWifiapBroadcast = new WifiapBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mWifiapBroadcast, filter);
    }
	
	/**
     * 创建热点线程类
     * 
     * <p>
     * 线程启动后，热点创建的结果将通过handler更新
     * </p>
     */
    class CreateAPProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;
        private Thread thread = null;

        CreateAPProcess() {
        }

        public void run() {
            while (true) {
                if (!this.running)
                    return;
                if ((mWifiUtils.getWifiApStateInt() == 3)
                        || (mWifiUtils.getWifiApStateInt() == 13)
                        || (System.currentTimeMillis() - this.startTime >= 30000L)) {
                    Message msg = handler.obtainMessage(WifiApConst.ApCreateAPResult);
                    handler.sendMessage(msg);
                }
                try {
                    Thread.sleep(5L);
                } catch (Exception localException) {
                }
            }
        }

        public void start() {
            try {
                thread = new Thread(this);
                running = true;
                startTime = System.currentTimeMillis();
                thread.start();
            } finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
                this.startTime = 0L;
            } finally {
            }
        }
    }
    
    
    /** handler 异步更新UI **/
    @SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            // 热点创建结果
            case WifiApConst.ApCreateAPResult:
                mCreateApProcess.stop();
                if (((mWifiUtils.getWifiApStateInt() == 3) || (mWifiUtils.getWifiApStateInt() == 13))
                        && (mWifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER))) {
                	//创建了属于本应用的热点
                	Log.i("frankchan", "Success in creating Ap");
                	mProgressDialog.setMessage(getString(R.string.room_create_build_server));
                	sendEmptyMessage(MSG_CREATE_SERVER_SOCKET);
                	//CQF按钮不可用，防止手快者点击多一次按钮
                    findViewById(R.id.btn_create_room).setEnabled(false);
                } else {
                    //创建失败
                	Log.i("frankchan", "fail to create Ap");
                	mProgressDialog.dismiss();
                	showToast(R.string.room_create_failure);
                }
                mCreateApProcess.stop();
                break;
            //开始创建套接字
            case MSG_CREATE_SERVER_SOCKET:
            	new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i("frankchan", "开始创建服务器socket");
						SocketServer server = SocketServer.newInstance(Constant.SOCKET_PORT);
						server.createServerSocket(new SocketListener());
					}
				}).start();
            	break;
            case MSG_SUCCESS_JUMP_GAME:
            	mProgressDialog.dismiss();
            	Log.i("frankchan","服务器Socket创建成功");
            	app.setServer(SocketServer.newInstance());
            	UserInfo info = app.user.convertToUserInfo();
            	info.setIp("192.168.43.1");
        		info.setId(SystemUtil.getIMEI(getApplicationContext()));
        		app.sp = new ServerPlayer(info,app.getServer());
        		showToast(R.string.room_create_success);
            	//游戏跳转
        		Intent intent = new Intent(RoomCreateActivity.this,GameActivity.class);
        		intent.putExtra("Room", room);
        		intent.putExtra("SSID", mSSID);
        		startActivity(intent);
            	RoomCreateActivity.this.finish();
            	break;
            case MSG_SHOW_CREATE_SOCKET_ERROR:
            	Log.i("frankchan","服务器Socket创建失败");
            	mProgressDialog.dismiss();
            	showToast(R.string.room_create_failure);
            	break;
            default:
                break;
            }
        }
    };

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mWifiapBroadcast); // 撤销广播
		if (mCreateApProcess != null)
            mCreateApProcess.stop();
		super.onDestroy();
	}

	@Override
	public void onConfirm() {
		// TODO Auto-generated method stub
		switch (wifiapOperateEnum){
		case WifiApConst.CLOSE:
            mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(
                    mWifiUtils.getApSSID(), WifiApConst.WIFI_AP_PASSWORD,
                    3, "ap"), false);
            showToast(R.string.room_cretae_close_wifi_toast);
            break;
		case WifiApConst.CREATE:
			isCreating = true;
			mWifiUtils.closeWifi();
			String roomName = ""+mRoomType;
			mSSID = WifiApConst.WIFI_AP_HEADER + roomName;
			room = RoomCreator.getRoom(mRoomType, roomName);
            mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(
                    mSSID,WifiApConst.WIFI_AP_PASSWORD, 3, "ap"), true);
            if (mCreateApProcess == null) {
                mCreateApProcess = new CreateAPProcess();
            }
            mCreateApProcess.start();
            mProgressDialog.show();
			break;
		}
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
	
	class SocketListener implements SocketCreateListener{

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			Log.i("frankchan", "创建Socket成功");
			app.setServer(SocketServer.newInstance());
			handler.sendEmptyMessage(MSG_SUCCESS_JUMP_GAME);
		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			Log.i("frankchan", "创建socket失败");
			handler.sendEmptyMessage(MSG_SHOW_CREATE_SOCKET_ERROR);
		}
		
	}
    
	public String getLocalHostName() {
        String str1 = Build.BRAND;
        String str2 = TextUtils.getRandomNumStr(2);
        return str1 + "_" + str2;
    }
    
}
