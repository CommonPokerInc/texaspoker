
package com.texas.poker.ui.dialog;

import com.texas.poker.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

/*
 * author FrankChan
 * description 
 * time 2014-9-22
 *
 */
public class HelpDialog extends BaseDialog implements OnCheckedChangeListener{
	
	private View view,ruleView,pokerView,poolView,questionView;
	
	private ImageView tab1,tab2,tab3,tab4;
	
	private ImageButton btnClose;
	
	private RadioGroup mGroup;
	
	private ScrollView scroller;
	
	private int latestId = 0;
	
	private View[]tabs;
	
	private View[]layouts;
	
	public HelpDialog(Context context,int duration,Effectstype type) {
		super(context,duration,type);
		// TODO Auto-generated constructor stub
		view = LayoutInflater.from(context).inflate(R.layout.dialog_help, null);
		mBackground = view.findViewById(R.id.dialog_common_bg);
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
		scroller = (ScrollView) view.findViewById(R.id.scroll_view);
		
		btnClose.setOnClickListener(this);
		mBackground.setOnClickListener(this);
		mGroup.setOnCheckedChangeListener(this);
		builder.setCustomView(view, context);
	}
	
	public void show(){
		super.show();
		setTab(R.id.dialog_btn_rule);
		scroller.scrollTo(10, 10);
	}
	
	public void hide(){
		super.hide();
		scroller.scrollTo(10, 10);
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
		case R.id.dialog_common_bg:
			break;
		default:
			break;
		}
		tabs[latestId].setVisibility(View.VISIBLE);
		layouts[latestId].setVisibility(View.VISIBLE);
	}
}


