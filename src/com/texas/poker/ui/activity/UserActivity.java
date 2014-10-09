package com.texas.poker.ui.activity;

import com.texas.poker.R;
import com.texas.poker.ui.AbsBaseActivity;
import com.texas.poker.util.ImageUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class UserActivity extends AbsBaseActivity implements OnClickListener{

	
	private ImageView imgAvatar;
	
	private ImageButton btnEditName,btnClose;
	
	private TextView txtName,txtMoney,txtBrand,btnEditAvatar,btnMore,txtNone,txtGirl,txtDes,txtCost,txtManage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		imgAvatar = (ImageView) findViewById(R.id.user_img_avatar);
		txtName = (TextView) findViewById(R.id.user_txt_name);
		txtMoney = (TextView) findViewById(R.id.user_txt_moeny);
		txtBrand = (TextView) findViewById(R.id.user_txt_level);
		txtCost = (TextView) findViewById(R.id.user_txt_cost);
		txtDes = (TextView) findViewById(R.id.user_txt_desc);
		txtGirl = (TextView) findViewById(R.id.user_txt_property);
		txtManage = (TextView) findViewById(R.id.user_btn_property);
		txtNone = (TextView) findViewById(R.id.user_txt_none);
		
		btnEditAvatar = (TextView) findViewById(R.id.user_btn_edit_avatar);
		btnEditName = (ImageButton) findViewById(R.id.user_btn_edit_name);
		btnMore = (TextView) findViewById(R.id.user_txt_more);
		btnClose = (ImageButton) findViewById(R.id.user_btn_close);
		
		btnEditAvatar.setOnClickListener(this);
		btnEditName.setOnClickListener(this);
		btnMore.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		initViews();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		imgAvatar.setImageResource(ImageUtil.A[app.user.getAvatar()]);
		txtName.setText(app.user.getName());
		txtMoney.setText(app.user.getMoney()+"");
		txtBrand.setBackgroundResource(ImageUtil.LEVEL[app.user.getLevel()]);
		if(app.user.hasProperty()){
			txtManage.setText("管理道具");
			txtCost.setVisibility(View.INVISIBLE);
			txtGirl.setVisibility(View.INVISIBLE);
			txtDes.setVisibility(View.INVISIBLE);
			txtNone.setVisibility(View.VISIBLE);
			txtCost.setText(app.user.getProertyNum());
			txtGirl.setBackgroundResource(ImageUtil.A[app.user.getAvatar()]);
			txtDes.setText(app.user.getPropertyList().get(app.user.getProertyNum()).getDescription());
		}else{
			txtCost.setVisibility(View.VISIBLE);
			txtGirl.setVisibility(View.VISIBLE);
			txtDes.setVisibility(View.VISIBLE);
			txtNone.setVisibility(View.INVISIBLE);
			txtManage.setText("去商场逛逛");
		}
	}

}
