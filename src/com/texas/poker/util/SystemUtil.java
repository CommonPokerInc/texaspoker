package com.texas.poker.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 *
 * ��˵��
 *
 * @author RinfonChen:
 * @Day 2014��8��27�� 
 * @Time ����4:51:00
 * @Declaration :
 *
 */
public class SystemUtil {

    
//   ��ȡIMEI
 // Requires READ_PHONE_STATE
    public static String getIMEI(Context mContext){
        TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE); 
        return TelephonyMgr.getDeviceId(); 
    }
    
    public static String getID(Context mContext){
        return getIMEI(mContext)+System.currentTimeMillis();
    }
    
}
