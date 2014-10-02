package com.texas.poker.ui.activity;

import java.util.ArrayList;
import com.texas.poker.Constant;
import com.texas.poker.R;
import com.texas.poker.adapter.RoomAdapter;
import com.texas.poker.adapter.RoomAdapter.OnGridItemClickListener;
import com.texas.poker.entity.SearchResult;
import com.texas.poker.ui.AbsBaseActivity;
import com.texas.poker.wifi.SocketClient;
import com.texas.poker.wifi.SocketClient.ClientConnectListener;
import com.texas.poker.wifi.WifiApConst;
import com.texas.poker.wifi.WifiapBroadcast;
import com.texas.poker.wifi.WifiapBroadcast.EventHandler;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class RoomSearchActivity extends AbsBaseActivity implements OnGridItemClickListener,EventHandler {
	
	private SocketClient client;
	
	private WTSearchProcess mSearchApProcess;
	
	private ConnectProcess mConnectProcess;
	
	private ArrayList<SearchResult> mWifiApList;

	private ProgressDialog mProgressDialog;
	
	private WifiapBroadcast mWifiapBroadcast;
	
	private RoomAdapter mAdapter;
	
	private GridView mRoomView;
	
	private final static int MSG_CONNECT_SOCKET_SUCCESS = 3;
	
	private final static int MSG_CONNECT_SOCKET_FAILURE = 4;

	private final static int MSG_CONNECT_ROOM_OVER_TIME = 5;
	
	private final static int MGS_BEGIN_CONNECT_SERVER_SOCKET = 6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_search);
		initViews();
		initBroadcast();
		startWifiScan();
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mRoomView = (GridView) findViewById(R.id.grid_room);
		mAdapter = new RoomAdapter(this, this);
		mRoomView.setAdapter(mAdapter);
		mWifiApList = new ArrayList<SearchResult>();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mSearchApProcess =new WTSearchProcess();
		mConnectProcess = new ConnectProcess();
		findViewById(R.id.room_btn_refresh).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startWifiScan();
			}
		});
	}
	

	/** 动态注册广播 */
    private void initBroadcast() {
        mWifiapBroadcast = new WifiapBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mWifiapBroadcast, filter);
        //注册Wifi搜索监听器
		mWifiapBroadcast.addehList(this);
    }
    
    private void startWifiScan(){
    	//如果已经有热点打开，关闭热点
    	mProgressDialog.setMessage(getString(R.string.room_searching));
    	if(mWifiUtils.getWifiApState()){
    		mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(
                    mWifiUtils.getApSSID(), WifiApConst.WIFI_AP_PASSWORD,
                    3, "ap"), false);
    	}
    	//如果Wifi未打开，开启Wifi搜索
    	if (!mWifiUtils.isWifiConnect() && !mWifiUtils.getWifiApState()) {
            mWifiUtils.OpenWifi();
        }
        mSearchApProcess.start();
        mWifiUtils.startScan();
        mProgressDialog.show();
    }
    

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mWifiapBroadcast); // 撤销广播
        mWifiapBroadcast.removeehList(this);
        if (mSearchApProcess != null)
            mSearchApProcess.stop();
        if(mConnectProcess!=null)
        	mConnectProcess.stop();
		super.onDestroy();
	}

	@Override
	public void onGridItemClick(String fullName) {
		// 点击开始连接房间
		WifiConfiguration localWifiConfiguration = mWifiUtils.createWifiInfo(fullName,
                WifiApConst.WIFI_AP_PASSWORD, 3, "wt");
        mWifiUtils.addNetwork(localWifiConfiguration);
        mProgressDialog.setMessage(getString(R.string.room_connecting));
        mProgressDialog.show();
        mConnectProcess.start();
	}
	
	/** handler 异步更新UI **/
    @SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            // 搜索超时
            case WifiApConst.ApSearchTimeOut:
            	Log.i("frankchan", "搜索超时无响应");
                mSearchApProcess.stop();
                mProgressDialog.dismiss();
                showToast(R.string.room_search_over_time);
                break;
            // 搜索结果
            case WifiApConst.ApScanResult:
            	Log.i("frankchan", "搜索成功");
                mProgressDialog.dismiss();
                mSearchApProcess.stop();
                int size = mWifiUtils.mWifiManager.getScanResults().size();
                if (size > 0) {
                	mWifiApList.clear();
                    for (int i = 0; i < size; ++i) {
                        String apSSID = mWifiUtils.mWifiManager.getScanResults().get(i).SSID;
                        if (apSSID.startsWith(WifiApConst.WIFI_AP_HEADER)
                                && !mWifiApList.contains(apSSID)) {
                            mWifiApList.add(new SearchResult(apSSID));
                            refreshAdapter(mWifiApList);
                        }
                    }
                }else{
                	showToast(R.string.room_search_nothing);
                }
                break;
            // 连接成功
            case WifiApConst.ApConnectResult:
                mWifiUtils.setNewWifiManagerInfo(); // 更新wifiInfo
                mSearchApProcess.stop();
                mConnectProcess.stop();
                if (mWifiUtils.getSSID().startsWith(WifiApConst.WIFI_AP_HEADER)) {
                	Log.i("frankchan", "连接Wifi成功");
                }else{
                	Log.e("frankchan", "连接的不是本应用的wifi");
                	showToast(R.string.room_connect_failure);
                }
                break;
            case MSG_CONNECT_SOCKET_SUCCESS:
                mProgressDialog.dismiss();
            	//进入游戏界面
            	break;
            case MSG_CONNECT_SOCKET_FAILURE:
                mProgressDialog.dismiss();
                showToast(getString(R.string.room_connect_failure));
            	break;
            case MSG_CONNECT_ROOM_OVER_TIME:
            	mProgressDialog.dismiss();
            	showToast(R.string.room_connect_failure);
            	break;
            case MGS_BEGIN_CONNECT_SERVER_SOCKET:
            	connectServerSocket();
            	break;
            default:
                break;
            }
        }
    };
    
    
    private void connectServerSocket(){
    	client = SocketClient.newInstance("192.168.43.1", Constant.SOCKET_PORT);
		client.connectServer(new SocketListener());
    }
    
	/**
     * 热点搜索线程类
     * 
     * <p>
     * 线程启动后，热点搜索的结果将通过handler更新
     * </p>
     */
    class WTSearchProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;
        private Thread thread = null;

        WTSearchProcess() {
        }

        public void run() {
            while (true) {
                if (!this.running)
                    return;
                if (System.currentTimeMillis() - this.startTime >= 30000L) {
                    Message msg = handler.obtainMessage(WifiApConst.ApSearchTimeOut);
                    handler.sendMessage(msg);
                }
                try {
                    Thread.sleep(100L);
                } catch (Exception localException) {
                }
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.startTime = System.currentTimeMillis();
                this.thread.start();
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
    
    /**
     * 热点连接线程类
     * 
     * <p>
     * 线程启动后，监听是否连接超时无响应
     * </p>
     */
    class ConnectProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;
        private Thread thread = null;

        ConnectProcess() {
        }

        public void run() {
            while (true) {
                if (!this.running)
                    return;
                if (System.currentTimeMillis() - this.startTime >= 10000L) {
                    Message msg = handler.obtainMessage(MSG_CONNECT_ROOM_OVER_TIME);
                    handler.sendMessage(msg);
                }
                try {
                    Thread.sleep(100L);
                } catch (Exception localException) {
                }
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.startTime = System.currentTimeMillis();
                this.thread.start();
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
    
    
	protected void refreshAdapter(ArrayList<SearchResult> mWifiApList) {
		// TODO Auto-generated method stub
		mAdapter.setData(mWifiApList);
		mAdapter.notifyDataSetChanged();
	}

	class SocketListener implements ClientConnectListener{

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			Log.i("frankchan", "连接socket成功");
			handler.sendEmptyMessage(MSG_CONNECT_SOCKET_SUCCESS);
		}

		@Override
		public void onFailure(String errorInfo) {
			// TODO Auto-generated method stub
			Log.e("frankchan", "连接socket失败");
			handler.sendEmptyMessage(MSG_CONNECT_SOCKET_FAILURE);
		}
		
	}
	

	@Override
	public void handleConnectChange() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(WifiApConst.ApConnectResult);
	}


	@Override
	public void scanResultsAvailable() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(WifiApConst.ApScanResult);
	}


	@Override
	public void wifiStatusNotification() {
		// TODO Auto-generated method stub
		
	}
}
