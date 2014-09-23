
package com.texas.poker.ui.dialog;

import com.texas.poker.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/*
 * author FrankChan
 * description 
 * time 2014-9-23
 *
 */
public class TransferDialog extends BaseDialog{
	
	private AnimationDialogBuilder builder;
	
	public TransferDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		builder = AnimationDialogBuilder.getInstance(context);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, null);
		builder.setCustomView(view, context);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		builder.show();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
}


