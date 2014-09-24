
package com.texas.poker.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.texas.poker.R;

/*
 * author FrankChan
 * description 
 * time 2014-9-24
 *
 */
public class TransferDialog extends BaseDialog {

	private View view;
	
	private ScrollView scroller;
	
	public TransferDialog(Context context, int duration, Effectstype type) {
		super(context, duration, type);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(R.layout.dialog_transfer, null);
		scroller = (ScrollView) view.findViewById(R.id.scroll_view);
		builder.setCustomView(view, context);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		scroller.scrollTo(10, 10);
	}

	
}


