package com.texas.poker.ui.activity;

import com.texas.poker.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener{

	private ImageButton btnHelp,btnCreate,btnJoin;
	private TextView btnTransfer,btnMoney,btnMarket;
	private ImageView imgTop,imgLeft,imgRight,imgDesk,imgLight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		startAnimation();
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
			break;
		default:
				break;
		}
	}
}
