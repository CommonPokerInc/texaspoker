
package com.texas.poker.custom;

import com.texas.poker.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/*
 * author FrankChan
 * description 
 * time 2014-10-3
 *
 */
public class PlayerView extends RelativeLayout {

	private void initView(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.view_player_others, this);
	}
	



	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public PlayerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

}


