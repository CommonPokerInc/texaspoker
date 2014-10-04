package com.texas.poker.ui.activity;

import java.util.ArrayList;
import com.texas.poker.R;
import com.texas.poker.custom.PersonView;
import com.texas.poker.custom.PlayerView;
import com.texas.poker.custom.VerticalSeekBar;
import com.texas.poker.entity.ClientPlayer;
import com.texas.poker.entity.Poker;
import com.texas.poker.entity.Room;
import com.texas.poker.ui.AbsGameActivity;
import com.texas.poker.wifi.message.GameMessage;
import com.texas.poker.wifi.message.PeopleMessage;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class GameActivity extends AbsGameActivity {

	//公共牌区域，操作区域，奖池区域
	private View pokerView,actionView,poolView;
	
	//退出和帮助按钮
	private ImageButton btnBack,btnHelp;
	
	//六个奖池
	private TextView txtPools[];
	
	//个人视图
	private PersonView playerView1;
	
	//其余5人视图
	private PlayerView playerView2,playerView3,playerView4,playerView5,playerView6;
	
	//5张工牌,AllIn图片
	private ImageView poker1,poker2,poker3,poker4,poker5,imgAllIn;
	
	//3种动作按钮
	private TextView btnQuit,btnFollow,btnAdd;
	
	//加注拉动条
	private VerticalSeekBar seekbar;
	
	//房间信息，房主自带，其他人需要等通知
	private Room room;
	
	//当前轮次的所有牌型
	private ArrayList<Poker>allPokerList;
	
	//所有玩家列表
	private ArrayList<ClientPlayer> playerList;
	
	//玩家的籌碼值
	private ArrayList<Integer>chipList;
	
	//本玩家的最大筹码值
	private int max;
	
	//当前的玩家
	private ClientPlayer currentPlayer;
	
	//是否有人退出或者进入
	private boolean isInOrOut = false;
	
	// 是否结束操作
	private boolean isEnd = false;
	
	//庄家下标
	private int DIndex = -1;
	
	//大盲下标
	private int bigBlindIndex = -1;
	
	//停止位、最大下注人的下标
	private int maxChipIndex = -1;
  
	private int currentOptionPerson = -1;
    
	private int currentPlayIndex = -1;
	
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		registerListener();
		findViews();
		initViews();
	}

	
	private void findViews(){
		pokerView = findViewById(R.id.game_layout_poker);
		actionView = findViewById(R.id.game_layout_choice);
		poolView = findViewById(R.id.game_layout_pool);
		
		btnBack = (ImageButton) findViewById(R.id.game_btn_exit);
		btnHelp = (ImageButton) findViewById(R.id.game_btn_help);
		
		playerView1 = (PersonView) findViewById(R.id.player1);
		playerView2 = (PlayerView) findViewById(R.id.player2);
		playerView3 = (PlayerView) findViewById(R.id.player3);
		playerView4 = (PlayerView) findViewById(R.id.player4);
		playerView5 = (PlayerView) findViewById(R.id.player5);
		playerView6 = (PlayerView) findViewById(R.id.player6);
		
		poker1 = (ImageView) findViewById(R.id.game_public_poker1);
		poker2 = (ImageView) findViewById(R.id.game_public_poker2);
		poker3 = (ImageView) findViewById(R.id.game_public_poker3);
		poker4 = (ImageView) findViewById(R.id.game_public_poker4);
		poker5 = (ImageView) findViewById(R.id.game_public_poker5);
		
		imgAllIn = (ImageView) findViewById(R.id.game_img_allin);
		
		btnQuit = (TextView) findViewById(R.id.game_btn_quit);
		btnFollow = (TextView) findViewById(R.id.game_btn_follow);
		btnAdd = (TextView) findViewById(R.id.game_btn_add);
		
		seekbar = (VerticalSeekBar) findViewById(R.id.game_seekbar);
		
		int[]resIds = {R.id.game_pool1,R.id.game_pool2,R.id.game_pool3,
				R.id.game_pool4,R.id.game_pool5,R.id.game_pool6};
		for(int i= 0;i<resIds.length;i++){
			txtPools[0] = (TextView) findViewById(resIds[i]);
		}
	}


	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerSendFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerSendSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClientSendFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClientSendSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientDecrease(String clientName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectFromServer(int sec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerReceive(PeopleMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServerReceive(GameMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClientReceive(PeopleMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClientReceive(GameMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
