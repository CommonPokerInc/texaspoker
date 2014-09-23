
package com.texas.poker.ui.dialog;

import com.texas.poker.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

/*
 * author FrankChan
 * description 
 * time 2014-9-22
 *
 */
public class HelpDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	
	private AnimationDialogBuilder builder;
	
	private View view,ruleView,pokerView,poolView,questionView;
	
	private ImageView tab1,tab2,tab3,tab4;
	
	private ImageButton btnClose;
	
	private RadioGroup mGroup;
	
	private int latestId = 0;
	
	private View[]tabs;
	
	private View[]layouts;
	
	public HelpDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		builder = AnimationDialogBuilder.getInstance(context);
		view = LayoutInflater.from(context).inflate(R.layout.dialog_help, null);
		ruleView= view.findViewById(R.id.dialog_layout_rule);
		pokerView= view.findViewById(R.id.dialog_layout_poker);
		poolView= view.findViewById(R.id.dialog_layout_pool);
		questionView= view.findViewById(R.id.dialog_layout_question);
		layouts = new View[]{ruleView,pokerView,poolView,questionView};
		
		tab1 = (ImageView) view.findViewById(R.id.dialog_img_indicatior_rule);
		tab2 = (ImageView) view.findViewById(R.id.dialog_img_indicatior_poker);
		tab3 = (ImageView) view.findViewById(R.id.dialog_img_indicatior_pool);
		tab4 = (ImageView) view.findViewById(R.id.dialog_img_indicatior_question);
		tabs = new View[]{tab1,tab2,tab3,tab4};
		
		btnClose = (ImageButton) view.findViewById(R.id.dialog_btn_close);
		mGroup = (RadioGroup) view.findViewById(R.id.dialog_radio_group);
		
		btnClose.setOnClickListener(this);
		mGroup.setOnCheckedChangeListener(this);
		builder.setCustomView(view, context);
		builder.withEffect(Effectstype.Fadein);
		builder.withDuration(500);
		builder.setCanceledOnTouchOutside(true);
	}
	
	public void show(){
		setTab(R.id.dialog_btn_rule);
		builder.show();
	}
	
	public void hide(){
		builder.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn_close:
			hide();
			break;
		default:
			break;
		}
	}

	private void setTab(int tabId){
		mGroup.check(tabId);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		tabs[latestId].setVisibility(View.INVISIBLE);
		layouts[latestId].setVisibility(View.INVISIBLE);
		switch(checkedId){
		case R.id.dialog_btn_rule:
			latestId = 0;
			break;
		case R.id.dialog_btn_poker:
			latestId = 1;
			break;
		case R.id.dialog_btn_pool:
			latestId = 2;
			break;
		case R.id.dialog_btn_question:
			latestId = 3;
			break;
		default:
			break;
		}
		tabs[latestId].setVisibility(View.VISIBLE);
		layouts[latestId].setVisibility(View.VISIBLE);
	}
}


