
package com.texas.poker.ui.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
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
	
	private boolean isQRCodeSet = false;
	
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
	}
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

	public void setQRCodeBitmap(Bitmap bitmap){
		imgQRCode.setImageBitmap(bitmap);
		isQRCodeSet = true;
	}
	
	public boolean isQRCodeSet(){
		return isQRCodeSet;
	}
}


