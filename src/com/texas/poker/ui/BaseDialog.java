
package com.texas.poker.ui;

import com.texas.poker.R;
import com.texas.poker.ui.dialog.AnimationDialogBuilder;
import com.texas.poker.ui.dialog.Effectstype;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * author FrankChan
 * description 
 * time 2014-9-23
 *
 */
public abstract class BaseDialog implements OnClickListener{
	
	protected AnimationDialogBuilder builder;
	
	protected View mBackground;
	
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
		builder.setCanceledOnTouchOutside(false);
	}
	public boolean isShown() {
		return builder.isShowing();
	}
}


