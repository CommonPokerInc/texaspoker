package com.texas.poker.util;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author Frank
 * @created by eclipse
 * @since 2014-4-10
 * 
 */
public class PreferenceHelper {
    private Context context;
    private String name="";
    private SharedPreferences sp;
    
    public PreferenceHelper(Context context){
        this.name = "poker";
        this.context = context;
        sp = getSharedPreferences();
    }
    private SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }
    
    public void setBoolean(String key,boolean value){
        sp.edit().putBoolean(key, value).commit();
    }
    
    public boolean getBoolean(String key,boolean value){
        return sp.getBoolean(key, value);
    }
    
    public boolean getBoolean(String key){
    	return getBoolean(key,false);
    }
    
    public int getInteger(String key){
        return sp.getInt(key, 0);
    }
    
    public void setInteger(String key,int value){
        sp.edit().putInt(key, value).commit();
    }
    public void setString(String key,String value){
        sp.edit().putString(key, value).commit();
    }
    
    public String getString(String key,String value){
        return sp.getString(key, null);
    }
    
    public String getString(String key){
    	return getString(key,null);
    }
    
    
}
