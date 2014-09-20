
package com.texas.poker.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/*
 * author FrankChan
 * description 
 * time 2014-9-11
 *
 */
public class WifiUtil {
	public static String getLocalIpAddress()  
	    {  
	        try  
	        {  
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
	            {  
	               NetworkInterface intf = en.nextElement();  
	               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
	              {  
	                   InetAddress inetAddress = enumIpAddr.nextElement();  
	                  if (!inetAddress.isLoopbackAddress())  
	                   {  
	                       return inetAddress.getHostAddress().toString();  
	                   }  
	              }  
	           }  
	        }  
	        catch (SocketException ex)  
	        {  
	            Log.e("frankchan", "failure to get IP"+ex.toString());  
	        }  
	        return null;  
	    }  

}


