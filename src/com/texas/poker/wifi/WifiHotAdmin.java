package com.texas.poker.wifi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.texas.poker.wifi.SocketServer.WifiCreateListener;

/**
 * �ȵ��������������ر�
 */
public class WifiHotAdmin {

	public static final String TAG = "WifiApAdmin";

	private WifiManager mWifiManager = null;

	private Context mContext = null;

	private static WifiHotAdmin instance;

	public void closeWifiAp() {
		closeWifiAp(mWifiManager);
	}

	public static WifiHotAdmin newInstance(Context context) {
		if (instance == null) {
			instance = new WifiHotAdmin(context);
		}
		return instance;
	}

	private WifiHotAdmin(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		closeWifiAp(mWifiManager);
	}

	public void startWifiAp(String wifiName,WifiCreateListener listener) {
		Log.i(TAG, "into startWifiAp����");
		stratWifiAp(wifiName,listener);
	}

	// ����һ��Wifi ���c
	private boolean stratWifiAp(String wifiName,WifiCreateListener listener) {

		Log.i(TAG, "into startWifiAp���� ����һ��Wifi �ȵ㣡");
		Method method1 = null;
		boolean ret = false;
		try {
			method1 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration apConfig = createPassHotWifiConfig(wifiName, Global.PASSWORD);
			ret = (Boolean) method1.invoke(mWifiManager, apConfig, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() IllegalArgumentException e");
			listener.OnCreateFailure(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() IllegalAccessException e");
			listener.OnCreateFailure(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() InvocationTargetException e");
			listener.OnCreateFailure(e.getMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() SecurityException e");
			listener.OnCreateFailure(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Log.d(TAG, "stratWifiAp() NoSuchMethodException e");
			listener.OnCreateFailure(e.getMessage());
		}
		Log.i(TAG, "out startWifiAp���� ����һ��Wifi �ȵ㣡");
		listener.onCreateSuccess();
		return ret;

	}

	// �P�]Wifi���c
	private boolean closeWifiAp(WifiManager wifiManager) {

		Log.i(TAG, "into closeWifiAp���� �ر�һ��Wifi �ȵ㣡");
		boolean ret = false;
		if (isWifiApEnabled(wifiManager)) {
			try {
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);
				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
				ret = (Boolean) method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "out closeWifiAp���� �ر�һ��Wifi �ȵ㣡");
		return ret;
	}

	// ���Wifi �ȵ��Ƿ����
	public boolean isWifiApEnabled(WifiManager wifiManager) {
		try {
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// wep���뱣�� config.wepKeys[0] = "" + mPasswd + ""; �ǹؼ�
	private WifiConfiguration createPassHotWifiConfig(String mSSID, String mPasswd) {

		Log.i(TAG, "out createPassHotWifiConfig���� �½�һ��Wifi���ã� SSID =" + mSSID + " password =" + mPasswd);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();

		config.SSID = mSSID;
		config.wepKeys[0] = mPasswd;
		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.wepTxKeyIndex = 0;
		config.priority = 0;

		Log.i(TAG, "out createPassHotWifiConfig���� ����һ��Wifi���ã� config.SSID=" + config.SSID + "password =" + config.wepKeys[0]);
		return config;
	}
	
	private String intToIp(int i) {          
        
        return (i & 0xFF ) + "." +          
      ((i >> 8 ) & 0xFF) + "." +          
      ((i >> 16 ) & 0xFF) + "." +          
      ( i >> 24 & 0xFF) ;    
	} 
	
	public String getIpAddress(){
		WifiInfo info = mWifiManager.getConnectionInfo();
		return intToIp(info.getIpAddress());
	}
}
