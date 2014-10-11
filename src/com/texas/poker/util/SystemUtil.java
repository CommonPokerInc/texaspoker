package com.texas.poker.util;

import com.texas.poker.PokerApplication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class SystemUtil {
    public static String getIMEI(Context mContext){
        TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE); 
        return TelephonyMgr.getDeviceId(); 
    }
    
    public static String getID(Context mContext){
        return getIMEI(mContext)+System.currentTimeMillis();
    }
    
    /**
     * 获取屏幕高度dip
     */
    public static int getScreenHeightPx(){
    	DisplayMetrics dm = PokerApplication.getContext().getResources().getDisplayMetrics();
    	return dm.heightPixels>dm.widthPixels?dm.heightPixels:dm.widthPixels;
    }
    
    /**
     * 获取屏幕高度px
     */
    public static int getScreenHeightDip(){
    	return px2dip(getScreenHeightPx());
    }
    /**  
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  
     */  
    public static int dip2px(float dpValue) {   
        final float scale = PokerApplication.getContext().getResources().getDisplayMetrics().density;   
        return (int) (dpValue * scale + 0.5f);   
    }   
    
    /**  
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp  
     */  
    public static int px2dip(float pxValue) {   
        final float scale = PokerApplication.getContext().getResources().getDisplayMetrics().density;   
        return (int) (pxValue / scale + 0.5f);   
    }  
    
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

}
