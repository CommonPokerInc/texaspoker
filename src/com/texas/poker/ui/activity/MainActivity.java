package com.texas.poker.ui.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import com.texas.poker.R;
import com.texas.poker.ui.dialog.Effectstype;
import com.texas.poker.ui.dialog.HelpDialog;
import com.texas.poker.ui.dialog.TransferDialog;
import com.texas.poker.util.SettingHelper;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.Activity;
import android.content.pm.PackageInfo;

public class MainActivity extends Activity implements OnClickListener{

	private ImageButton btnHelp,btnCreate,btnJoin;
	
	private TextView btnTransfer,btnMoney,btnMarket;
	
	private ImageView imgTop,imgLeft,imgRight,imgDesk,imgLight;
	
	private HelpDialog mHelpDialog;
	
	private TransferDialog mTransferDialog;
	
	private SettingHelper settingHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settingHelper = new SettingHelper(this);
		initViews();
		startAnimation();
		if(Environment.getExternalStorageState()
        		.equals(android.os.Environment.MEDIA_MOUNTED)){
        	new Thread(copyRunnable).start();
        }
	}
	
	private void initViews(){
		btnHelp = (ImageButton)findViewById(R.id.main_btn_help);
		btnCreate = (ImageButton)findViewById(R.id.main_btn_cretae);
		btnJoin = (ImageButton)findViewById(R.id.main_btn_join);
		btnTransfer = (TextView)findViewById(R.id.main_btn_transfer);
		btnMoney = (TextView)findViewById(R.id.main_btn_money);
		btnMarket = (TextView)findViewById(R.id.main_btn_market);
		imgTop = (ImageView)findViewById(R.id.main_img_curtain_top);
		imgLeft = (ImageView)findViewById(R.id.main_img_curtain_left);
		imgRight = (ImageView)findViewById(R.id.main_img_curtain_right);
		imgDesk = (ImageView)findViewById(R.id.main_img_desk);
		imgLight = (ImageView)findViewById(R.id.main_img_light);

		btnHelp.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		btnTransfer.setOnClickListener(this);
		btnMoney.setOnClickListener(this);
		btnMarket.setOnClickListener(this);
		imgTop.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
		imgRight.setOnClickListener(this);
		imgDesk.setOnClickListener(this);
		imgLight.setOnClickListener(this);
		
	}
	
	private void startAnimation(){
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.main_btn_help:
			if(null==mHelpDialog){
				mHelpDialog =new HelpDialog(this,500,Effectstype.Slideright);
			}
			mHelpDialog.show();
			break;
		case R.id.main_btn_transfer:
			if(null==mTransferDialog){
				mTransferDialog =new TransferDialog(this,500,Effectstype.Slideleft);
			}
			mTransferDialog.show();
			break;
		default:
			break;
		}
	}
	
	private Runnable copyRunnable = new Runnable() {
		
		@Override
		public void run() {
			String strDir = Environment.getExternalStorageDirectory().getPath()
					+"/.texas";
			String strFile = strDir +"/texaspoker.apk";
			File target =new File(strFile);
			
			if(settingHelper.isFirstStart()||(!settingHelper.isFirstStart()&&!target.exists())){
				settingHelper.setFirstStart(false);
				List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
				String path = "";
				for(PackageInfo info:packs){
					if(info.applicationInfo.packageName.contains("com.texas.poker")){
						path = info.applicationInfo.publicSourceDir;
						break;
					}
				}
				File file = new File(path);
				new File(strDir).mkdirs();
				if(file.exists()&&!target.exists()){
					copyFile(file.getPath(), target.getPath());
				}
			}
		}
	};
	
	@SuppressWarnings("resource")
	public void copyFile(String oldPath, String newPath) { 
	       try { 
	           int bytesum = 0; 
	           int byteread = 0; 
	           File oldfile = new File(oldPath); 
	           if (oldfile.exists()) { 
	               InputStream inStream = new FileInputStream(oldPath); 
	               FileOutputStream fs = new FileOutputStream(newPath); 
	               byte[] buffer = new byte[1444]; 
	               while ( (byteread = inStream.read(buffer)) != -1) { 
	                   bytesum += byteread; 
	                   System.out.println(bytesum); 
	                   fs.write(buffer, 0, byteread); 
	               } 
	               inStream.close(); 
	           } 
	           settingHelper.setCopied(true);
	       } 
	       catch (Exception e) { 
	           System.out.println("copy single file faliure"); 
	           e.printStackTrace(); 
	           settingHelper.setCopied(false);
	       } 

	   } 
}
