
package com.texas.poker.custom;

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
import android.widget.*;

/*
 * author FrankChan
 * description 
 * time 2014-10-3
 *
 */
public class PlayerView extends RelativeLayout {

	private View innerView,outView;
	private TextView bigBrand,smallBrand,txtBet,txtMoney,bigAvatar,smallAvatar,imgGirl;
	private ImageView c1,c2,imgType;
	
	private void initView(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.view_player_others, this);
		innerView = view.findViewById(R.id.game_player_surface);
		outView = view.findViewById(R.id.game_player_inner);
		bigAvatar = (TextView) view.findViewById(R.id.game_player_avatar_big);
		smallAvatar = (TextView) view.findViewById(R.id.game_player_avatar_small);
		bigBrand = (TextView) view.findViewById(R.id.game_player_brand_big);
		smallBrand = (TextView) view.findViewById(R.id.game_player_brand_small);
		txtBet =(TextView) view.findViewById(R.id.game_player_round_bet);
		txtMoney = (TextView) view.findViewById(R.id.game_player_money);
		imgGirl = (TextView) view.findViewById(R.id.game_player_avatar_girl);
		c1 = (ImageView) view.findViewById(R.id.game_card1);
		c2= (ImageView) view.findViewById(R.id.game_card2);
		imgType = (ImageView) view.findViewById(R.id.game_card_type);
		
		
		int stardardWith = SystemUtil.getScreenHeightPx()/5;
		changeLayout(bigAvatar, stardardWith, 0.63f, 0.63f);
		changeLayout(smallAvatar,stardardWith,0.40f,0.44f);
		changeLayout(imgGirl, stardardWith, 0.60f, 0.56f);
		changeLayout(smallBrand, stardardWith, 0.56f, 0.17f);
		changeLayout(txtMoney, stardardWith, 0.30f, 0.14f);
		changeLayout(c1, stardardWith, 0.31f, 0.38f);
		changeLayout(c1, stardardWith, 0.31f, 0.38f);
	}
	



	private void changeLayout(View txtView,int stardard,float wRate,float hRate){
		txtView.getLayoutParams().width= (int) (stardard*wRate);
		txtView.getLayoutParams().height= (int) (stardard*hRate);
	}

	public void showAsNormal(){
		innerView.setVisibility(View.VISIBLE);
	}
	
	//仅用于第一次显示
	public void showAsNormal(UserInfo info,Room room){
		int avatar = 0;
		if(info.getAvatar()>=0&&info.getAvatar()<ImageUtil.AVATAR_BIG.length){
			avatar = info.getAvatar();
		}
		bigAvatar.setBackgroundResource(ImageUtil.AVATAR_BIG[avatar]);
		smallAvatar.setBackgroundResource(ImageUtil.AVATAR_SMALL[avatar]);
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
		bigBrand.setBackgroundResource(ImageUtil.BRAND_BIG[level]);
		bigBrand.setText("等待中");
		smallBrand.setBackgroundResource(ImageUtil.BRAND_SMALL[level]);
		smallBrand.setText(info.getName());
		
		txtMoney.setText(String.valueOf(room.getBasicChips()));
		txtBet.setText("0");
		setVisibility(View.VISIBLE);
		hidePoker();
	}

	public void setPoker(Poker p1,Poker p2){
		c1.setBackgroundResource(PokerUtil.pokersImg[p1.getPokerImageId()]);
		c2.setBackgroundResource(PokerUtil.pokersImg[p2.getPokerImageId()]);
	}
	
	public void showPoker(int type){
		c1.setVisibility(View.VISIBLE);
		c2.setVisibility(View.VISIBLE);
		imgType.setBackgroundResource(ImageUtil.TYPE[type]);
		imgType.setVisibility(View.VISIBLE);
	}
	
	public void hidePoker(){
		c1.setVisibility(View.INVISIBLE);
		c2.setVisibility(View.INVISIBLE);
		imgType.setVisibility(View.INVISIBLE);
	}
	
	public void showAsFocus(){
		innerView.setVisibility(View.INVISIBLE);
		outView.setVisibility(View.VISIBLE);
	}
	
	public void setMoney(int money){
		txtMoney.setText(String.valueOf(money));
	}
	
	public void setBet(int money){
		txtBet.setText(String.valueOf(money));
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


