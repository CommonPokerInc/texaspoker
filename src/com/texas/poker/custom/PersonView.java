
package com.texas.poker.custom;

import java.util.ArrayList;

import com.texas.poker.R;
import com.texas.poker.entity.LocalUser;
import com.texas.poker.entity.Poker;
import com.texas.poker.entity.Room;
import com.texas.poker.entity.UserInfo;
import com.texas.poker.util.ImageUtil;
import com.texas.poker.util.PokerUtil;
import com.texas.poker.util.SystemUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * author FrankChan
 * description 
 * time 2014-10-3
 *
 */
public class PersonView extends RelativeLayout {
	
	private TextView mBrand,txtBet,mAvatar,imgGirl;
	
	private ImageView mCard1,mCard2;
	
	private String mName;
	
	private int mMoney;
	
	private void initView(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.view_player_me, this);
		mBrand = (TextView) view.findViewById(R.id.game_player_brand_big);
		mAvatar = (TextView) findViewById(R.id.game_player_avatar_big);
		imgGirl = (TextView) findViewById(R.id.game_player_avatar_girl);
		mCard1 = (ImageView) findViewById(R.id.game_me_card1);
		mCard2 = (ImageView) findViewById(R.id.game_me_card2);
		txtBet = (TextView) view.findViewById(R.id.game_me_bet);
		int stardardWidth = SystemUtil.getScreenHeightPx()/5;
		mBrand.getLayoutParams().width = stardardWidth;
		changeLayout(imgGirl, stardardWidth, 0.72f, 0.74f);
		changeLayout(mAvatar, stardardWidth, 0.67f, 0.72f);
		changeLayout(mCard1, stardardWidth, 0.31f, 0.38f);
		changeLayout(mCard2, stardardWidth, 0.31f, 0.38f);
	}
	
	
	private void changeLayout(View view,int stardard,float wRate,float hRate){
		view.getLayoutParams().width= (int) (stardard*wRate);
		view.getLayoutParams().height= (int) (stardard*hRate);
	}
	
	public void showAsFirst(UserInfo info,Room room){
		int avatar = 0;
		if(info.getAvatar()>=0&&info.getAvatar()<ImageUtil.AVATAR_BIG.length){
			avatar = info.getAvatar();
		}
		mAvatar.setBackgroundResource(ImageUtil.AVATAR_BIG[avatar]);
		if(info.hasProperty()){
			int girl = 0;
			if(info.getProertyNum()>=0&&info.getProertyNum()<ImageUtil.GIRLS.length){
				girl = info.getProertyNum();
			}
			imgGirl.setBackgroundResource(ImageUtil.GIRLS[girl]);
		}else{
			imgGirl.setVisibility(View.INVISIBLE);
		}
		int level =0;
		if(info.getLevel()>=0&&info.getLevel()<LocalUser.AVATAR_SUM){
			level = info.getLevel();
		}
		mBrand.setBackgroundResource(ImageUtil.BRAND_BIG[level]);
		mMoney = room.getBasicChips();
		mName = info.getName();
		mBrand.setText(mName+"-"+mMoney);
		txtBet.setText("0");
		hideCards();
	}
	
	public void setBet(int bet){
		txtBet.setText(bet+"");
	}
	
	public void setMoney(int money){
		mMoney = money;
		mBrand.setText(mName+"-"+money);
	}
	
	public void setCards(Poker poker1,Poker poker2){
		mCard1.setBackgroundResource(PokerUtil.pokersImg[poker1.getNumber()]);
		mCard2.setBackgroundResource(PokerUtil.pokersImg[poker2.getNumber()]);
		mCard1.setVisibility(View.VISIBLE);
		mCard2.setVisibility(View.VISIBLE);
	}
	
	public void setCards(ArrayList<Poker>list){
		if(list.size()>=2)
			setCards(list.get(0), list.get(1));
	}
	
	public void hideCards(){
		mCard1.setVisibility(View.INVISIBLE);
		mCard2.setVisibility(View.INVISIBLE);
	}
	
	public void showCards(){
		mCard1.setVisibility(View.VISIBLE);
		mCard2.setVisibility(View.VISIBLE);
	}
	
	public PersonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public PersonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public PersonView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
}


