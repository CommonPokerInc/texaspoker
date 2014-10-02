
package com.texas.poker.ui.dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.texas.poker.R;
import com.texas.poker.ui.BaseDialog;

/*
 * author FrankChan
 * description 
 * time 2014-9-24
 *
 */
public class TransferDialog extends BaseDialog implements OnKeyListener{

	private View view;
	
	private ScrollView scroller;
	
	private ImageButton btnClose;
	
	private ImageView imgQRCode;
	
	private OnBackCallback mCallback;
	
	public TransferDialog(Context context, int duration, Effectstype type,OnBackCallback callback) {
		super(context, duration, type);
		// TODO Auto-generated constructor stub
		mCallback = callback;
		view = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, null);
		mBackground = view.findViewById(R.id.dialog_common_bg);
		scroller = (ScrollView) view.findViewById(R.id.scroll_view);
		imgQRCode = (ImageView) view.findViewById(R.id.img_QRCode);
		btnClose = (ImageButton) view.findViewById(R.id.dialog_btn_close);
		btnClose.setOnClickListener(this);
		mBackground.setOnClickListener(this);
		builder.setCustomView(view, context);
		builder.setOnKeyListener(this);
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
			mCallback.onBack();
			break;
		case R.id.dialog_common_bg:
			break;
		default:
			break;
		}
	}

	public interface OnBackCallback{
		void onBack();
	}
	
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_BACK){
			mCallback.onBack();
		}
		return false;
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


