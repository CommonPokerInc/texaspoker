
package com.texas.poker.ui.dialog;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.texas.poker.R;
import com.texas.poker.wifi.transfer.WebServer;
import com.texas.poker.wifi.transfer.WebService;

/*
 * author FrankChan
 * description 
 * time 2014-9-24
 *
 */
public class TransferDialog extends BaseDialog implements OnClickListener{

	private View view;
	
	private ScrollView scroller;
	
	private ImageButton btnClose;
	
	private ImageView imgQRCode;
	
	public TransferDialog(Context context, int duration, Effectstype type) {
		super(context, duration, type);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, null);
		scroller = (ScrollView) view.findViewById(R.id.scroll_view);
		imgQRCode = (ImageView) view.findViewById(R.id.img_QRCode);
		btnClose = (ImageButton) view.findViewById(R.id.dialog_btn_close);
		btnClose.setOnClickListener(this);
		builder.setCustomView(view, context);
		handler.sendEmptyMessage(1);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler();
	
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		scroller.scrollTo(10, 10);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn_close:
			hide();
			break;
		default:
			break;
		}
	}

//	private Runnable runWithSDCard = new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//
//			String strDir = Environment.getExternalStorageDirectory().getPath()
//					+"/.poker";
//			String strFile = strDir +"/commonpoker.apk";
//			File target =new File(strFile);
//			createImage("http://192.168.43.1:"+WebService.PORT+target.getPath());
//		}
//	};
    
//    private Runnable runNoSDCard = new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
//			String path = "";
//			for(PackageInfo info:packs){
//				if(info.applicationInfo.packageName.contains("com.poker.common")){
//					path = info.applicationInfo.publicSourceDir;
//					break;
//				}
//			}
//			File file = new File(path);
//			
//			if(file.exists()){
//				createImage("http://192.168.43.1:"+WebService.PORT+path);
//			}
//		}
//	};
	
}


