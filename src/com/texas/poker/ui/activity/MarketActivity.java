package com.texas.poker.ui.activity;

import java.util.ArrayList;

import com.texas.poker.R;
import com.texas.poker.adapter.MarketAdapter;
import com.texas.poker.adapter.MarketAdapter.OnGridItemClickListener;
import com.texas.poker.entity.Property;
import com.texas.poker.ui.AbsBaseActivity;
import com.texas.poker.ui.dialog.ConfirmDialog;
import com.texas.poker.ui.dialog.Effectstype;
import com.texas.poker.util.DatabaseUtil;
import com.texas.poker.util.ImageUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.Activity;

public class MarketActivity extends AbsBaseActivity implements OnGridItemClickListener,ConfirmDialog.DialogConfirmInterface{

	private GridView mMarketView;
	
	private ImageView imgGirl;
	
	private TextView txtName,txtDsc;
	
	private Button btnBuy;
	
	private ImageButton btnClose;
	
	private ArrayList<Property>mList = new ArrayList<Property>();
	
	private MarketAdapter mAdapter;
	
	private int mType = Property.TYPE_ONE;
	
	private ConfirmDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
		mMarketView = (GridView) findViewById(R.id.grid_market);
		imgGirl = (ImageView) findViewById(R.id.market_img_girl);
		txtName = (TextView) findViewById(R.id.market_txt_girl_name);
		txtDsc = (TextView) findViewById(R.id.market_txt_girl_description);
		btnBuy = (Button) findViewById(R.id.market_btn_buy);
		btnClose = (ImageButton) findViewById(R.id.market_btn_close);
		initViews();
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		mList.add(new Property(0));
		mList.add(new Property(1));
		mList.add(new Property(2));
		mList.add(new Property(3));
		mList.add(new Property(4));
		mList.add(new Property(5));
		mAdapter = new MarketAdapter(this,this);
		mMarketView.setAdapter(mAdapter);
		mAdapter.setData(mList);
		mDialog = new ConfirmDialog(this, 500, Effectstype.Fadein, this);
		btnBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(app.user.hasProperty()&&app.user.getProertyNum()==mType){
					showToast(getString(R.string.market_own_property));
					return;
				}
				mDialog.show();
				mDialog.setTopic(getString(R.string.market_confirm_to_buy));
			}
		});
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MarketActivity.this.finish();
			}
		});
	}

	@Override
	public void onGridItemClick(Property property) {
		// TODO Auto-generated method stub
		if(property.getType()<=Property.TYPE_FIVE){
			mType = property.getType();
			imgGirl.setImageResource(ImageUtil.PROPERTY_BIG[property.getType()]);
			txtDsc.setText(property.getDescription());
			txtName.setText(property.getName());
		}
	}

	@Override
	public void onConfirm() {
		// TODO Auto-generated method stub
		Property p = new Property(mType);
		if(app.user.getMoney()<p.getCost()){
			showToast(getString(R.string.market_money_not_enough));
		}else{
			app.user.setMoney(app.user.getMoney()-p.getCost());
			app.user.addPropertyList(new Property(mType));
			showToast(getString(R.string.market_buy_success));
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					DatabaseUtil.storeUsertoDB(app.user);
				}
			}).start();
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
}
