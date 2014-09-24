
package com.texas.poker.ui.dialog;

import com.texas.poker.R;

import android.content.Context;

/*
 * author FrankChan
 * description 
 * time 2014-9-23
 *
 */
public abstract class BaseDialog {
	
	protected AnimationDialogBuilder builder;
	
	public void show(){
		builder.show();
	};
	public void hide(){
		builder.dismiss();
	};
	public BaseDialog(Context context,int duration,Effectstype type) {
		super();
		// TODO Auto-generated constructor stub
		builder = new AnimationDialogBuilder(context, R.style.dialog_untran);
		builder.withEffect(type);
		builder.withDuration(duration);
		builder.setCanceledOnTouchOutside(true);
	}
}


