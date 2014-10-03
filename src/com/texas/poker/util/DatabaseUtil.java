
package com.texas.poker.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import com.google.gson.Gson;
import com.texas.poker.Constant;
import com.texas.poker.entity.LocalUser;

import android.os.Environment;
import android.util.Log;

/*
 * author FrankChan
 * description 数据的读写操作
 * time 2014-10-3
 *
 */
public class DatabaseUtil {
	
	public static boolean isSDCardExists(){
		return Environment.getExternalStorageState()
        		.equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isUserExists(){
		File file = new File(Constant.DIRECTORY+Constant.USER_INFO_NAME);
		return file.exists();
	}
	
	public static boolean createUserFile(){
		File file = new File(Constant.DIRECTORY);
		File targetFile = new File(Constant.DIRECTORY+Constant.USER_INFO_NAME);
		if(!file.exists()){
			file.mkdirs();
		}
		if(!targetFile.exists()){
			try {
				targetFile.createNewFile();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static LocalUser getUserFromDB(){
		LocalUser user = null;
		try {
			String result = readFileSdcardFile(Constant.DIRECTORY+Constant.USER_INFO_NAME);
			user = new Gson().fromJson(result, LocalUser.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("frankchan", "读出用户数据异常");
			e.printStackTrace();
			user = new LocalUser();
		}
		return user;
	}
	
	public static void storeUsertoDB(LocalUser user){
		String result = new Gson().toJson(user, LocalUser.class);
		try {
			writeFileSdcardFile(Constant.DIRECTORY+Constant.USER_INFO_NAME, result);
			Log.i("frankchan", "保存用户数据成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("frankchan", "保存用户数据失败");
			e.printStackTrace();
		}
	}
	
	
	//写内容到SD
	public static void writeFileSdcardFile(String fileName,String write_str) throws IOException{ 
		 try{ 
			   //此处必须覆盖
		       FileOutputStream fout = new FileOutputStream(fileName,false); 
		       byte [] bytes = write_str.getBytes(); 

		       fout.write(bytes); 
		       fout.close(); 
		     }catch(Exception e){ 
		        e.printStackTrace(); 
		     } 
	} 
	
	//读SD中的文件
	public static String readFileSdcardFile(String fileName) throws IOException{ 
	  String res=""; 
	  try{ 
	         FileInputStream fin = new FileInputStream(fileName); 

	         int length = fin.available(); 

	         byte [] buffer = new byte[length]; 
	         fin.read(buffer);     

	         res = EncodingUtils.getString(buffer, "UTF-8"); 

	         fin.close();     
	        } 

	        catch(Exception e){ 
	         e.printStackTrace(); 
	        } 
	        return res; 
	} 
}


