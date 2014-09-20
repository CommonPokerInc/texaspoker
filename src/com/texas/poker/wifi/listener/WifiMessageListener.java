package com.texas.poker.wifi.listener;

/**
 * 
 * @author frankchan
 * ��Ϣ���ͽ����������
 */
public interface WifiMessageListener {
	void sendFailure(String msg);
	void sendSuccess(String msg);
}
