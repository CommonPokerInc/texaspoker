
package com.texas.poker.wifi.listener;

/*
 * author FrankChan
 * description ��Ϸͨ�Żص��ӿڣ���Ϣδ����
 * time 2014-8-16
 *
 */
public interface CommunicationListener {
	public void onStringReceive(String strInfo);
	public void onSendSuccess();
	public void onSendFailure(String errInfo);
}


