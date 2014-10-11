package com.texas.poker.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.texas.poker.Constant;
import com.texas.poker.R;
import com.texas.poker.custom.PersonView;
import com.texas.poker.custom.PlayerView;
import com.texas.poker.custom.VerticalSeekBar;
import com.texas.poker.entity.ClientPlayer;
import com.texas.poker.entity.Poker;
import com.texas.poker.entity.Room;
import com.texas.poker.ui.AbsGameActivity;
import com.texas.poker.ui.dialog.ConfirmDialog;
import com.texas.poker.ui.dialog.ConfirmDialog.DialogConfirmInterface;
import com.texas.poker.ui.dialog.Effectstype;
import com.texas.poker.util.AnimationProvider;
import com.texas.poker.util.PokerUtil;
import com.texas.poker.util.SoundPlayer;
import com.texas.poker.util.SystemUtil;
import com.texas.poker.wifi.WifiApConst;
import com.texas.poker.wifi.message.GameMessage;
import com.texas.poker.wifi.message.MessageFactory;
import com.texas.poker.wifi.message.PeopleMessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class GameActivity extends AbsGameActivity implements OnClickListener,DialogConfirmInterface{

	
	private ConfirmDialog mConfirmDialog;
	
	private PopupWindow mPopIntro;
	
	//公共牌区域，操作区域，奖池区域,等待区域
	private View actionView,poolView,waitView,seekView;
	
	//退出和帮助按钮
	private ImageButton btnBack,btnHelp;
	
	//六个奖池
	private TextView txtPools[];
	
	//个人视图
	private PersonView playerView1;
	
	//其余5人视图
	private PlayerView playerView2,playerView3,playerView4,playerView5,playerView6;
	
	//5张工牌,AllIn图片
	private ImageView poker1,poker2,poker3,poker4,poker5;
	
	//3种动作按钮和开始游戏按钮
	private TextView btnStart,txtHint,txtInfo;
	
	private Button btnQuit,btnFollow,btnAdd;
	
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
	
	//局数
	private int aroundIndex = -1;

	private WorkHandler wHandler;
  
	private boolean isAllowKick = false;
	
	private int mMaxAddBetCan,mPreAddBet,mCurAddBet;
	
	private int minChip;
	
	private boolean isAllowClick = false;
	
	private final static int ABANDOM_TRANSPARENT = 180;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		findViews();
		initViews();
		adaptToScreen();
		sendBroadcastToMain();
		registerListener();
		SoundPlayer.init(getApplicationContext());
	}

	private void sendBroadcastToMain(){
		this.sendBroadcast(new Intent(Constant.ACTON_CLOSE_MAIN));
	}
	
	private void findViews(){
		actionView = findViewById(R.id.game_layout_choice);
		poolView = findViewById(R.id.game_layout_pool);
		waitView = findViewById(R.id.game_layout_info);
		seekView = findViewById(R.id.game_layout_seek);
		
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
		
		btnQuit = (Button) findViewById(R.id.game_btn_quit);
		btnFollow = (Button) findViewById(R.id.game_btn_follow);
		btnAdd = (Button) findViewById(R.id.game_btn_add);
		btnStart = (TextView) findViewById(R.id.game_btn_start);
		txtHint = (TextView) findViewById(R.id.game_txt_waiting);
		txtInfo = (TextView) findViewById(R.id.game_room_name);
		
		seekbar = (VerticalSeekBar) findViewById(R.id.game_seekbar);
		
		int[]resIds = {R.id.game_pool1,R.id.game_pool2,R.id.game_pool3,
				R.id.game_pool4,R.id.game_pool5,R.id.game_pool6};
		txtPools = new TextView[resIds.length];
		for(int i= 0;i<resIds.length;i++){
			txtPools[i] = (TextView) findViewById(resIds[i]);
		}
		
		btnBack.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnFollow.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnQuit.setOnClickListener(this);
		btnStart.setOnClickListener(this);
		seekbar.setOnSeekBarChangeListener(mBarChangeListener);
	}
	

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		wHandler = new WorkHandler(getMainLooper());
		room = getIntent().getParcelableExtra("Room");
		chipList = new ArrayList<Integer>();
		playerList = new ArrayList<ClientPlayer>();
		if (!app.isServer()) {
            currentPlayer = app.cp;
            playerList.add(currentPlayer);
            sendMessage(MessageFactory.newPeopleMessage(false, false, playerList, null,null,null));
            btnStart.setVisibility(View.INVISIBLE);
            updateRoom(room, false);
        } else {
            currentPlayer = app.sp;
            currentPlayer.getInfo().setBaseMoney(room.getBasicChips());
            currentPlayer.getInfo().setAroundChip(0);
            playerList.add(currentPlayer); 
            updateRoom(room, true);
        }
		minChip = room.getBasicChips()/100;
		playerView1.showAsFirst(app.user.convertToUserInfo(),room);
		mConfirmDialog = new ConfirmDialog(this, 300, Effectstype.Shake, this);
	}

	private void adaptToScreen(){
		int stardardWidth = SystemUtil.getScreenHeightPx()/5;
		changeLayout(stardardWidth, 0.40f, 0.5f, poker1,poker2,poker3,poker4,poker5);
	}
	
	private void changeLayout(int stardard,float wRate,float hRate,View...views){
		for(View view:views){
			view.getLayoutParams().width= (int) (stardard*wRate);
			view.getLayoutParams().height= (int) (stardard*hRate);
		}
	}
	
	private SeekBar.OnSeekBarChangeListener mBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			seekBar.setMax(playerView1.getMoney()/minChip+1);
			mCurAddBet = seekBar.getProgress();
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
				if(playerView1.getMoney()<=progress*minChip){
					mCurAddBet = playerView1.getMoney();
					btnAdd.setText(getString(R.string.game_all_in));
					SoundPlayer.playMusic(1, false);
				}else{
					mCurAddBet = progress*minChip;
					btnAdd.setText(mCurAddBet+"");
				}
				
			
		}
	};
	
	private void hideRoom(){
		waitView.setVisibility(View.INVISIBLE);
	}
	
	private void updateRoom(Room room,boolean isServer){
		txtInfo.append(room.getName());
		if(isServer){
			btnStart.setVisibility(View.VISIBLE);
			txtHint.setVisibility(View.INVISIBLE);
		}else{
			txtHint.setVisibility(View.VISIBLE);
			btnStart.setVisibility(View.INVISIBLE);
		}
	}
	
	public void chairUpdate(ArrayList<ClientPlayer> playerList){
        int index = findIndexWithIPinList(playerList);
        int chairIndex = 0;
        if(index!=-1){
            updateChairByPlayer(chairIndex++,playerList.get(index));
//            更新自己后面的玩家桌位
            for(int i = index+1;i<playerList.size();i++){
                updateChairByPlayer(chairIndex++,playerList.get(i));
            }
            
//            更新自己前面的玩家桌位
            for(int i = 0;i<index;i++){
                updateChairByPlayer(chairIndex++,playerList.get(i));
            }
            for(int i = chairIndex;i<6;i++){
                hideChairByPlayer(chairIndex++);
            }
        }
        
    }
	
	//隐藏个人某个位置的人
	public void hideChairByPlayer(int index){
        switch (index) {
           case 0:
        	   playerView1.setVisibility(View.INVISIBLE);
        	   playerView1.setTag(-1);
               break;
           case 1:
        	   playerView2.setVisibility(View.INVISIBLE);
        	   playerView2.setTag(-1);
               break;
           case 2:
        	   playerView3.setVisibility(View.INVISIBLE);
        	   playerView3.setTag(-1);
               break;
           case 3:
        	   playerView4.setVisibility(View.INVISIBLE);
        	   playerView4.setTag(-1);
               break;
           case 4:
        	   playerView5.setVisibility(View.INVISIBLE);
        	   playerView5.setTag(-1);
               break;
           case 5:
        	   playerView6.setVisibility(View.INVISIBLE);
        	   playerView6.setTag(-1);
               break;
        }
    }
	
	//在某位置更新单个的个人信息
	public void updateChairByPlayer(int index,ClientPlayer play){
        switch (index) {
           case 0:
               playerView1.showAsFirst(play.getInfo(),room);
               playerView1.setTag(findPlayer(play));
               if(playerView1.getVisibility()!=View.VISIBLE)
            	   playerView1.setVisibility(View.VISIBLE);
               break;
           case 1:
        	   playerView2.showAsNormal(play.getInfo(),room);
               playerView2.setTag(findPlayer(play));
               if(playerView2.getVisibility()!=View.VISIBLE)
            	   playerView2.setVisibility(View.VISIBLE);
               break;
           case 2:
        	   playerView3.showAsNormal(play.getInfo(),room);
               playerView3.setTag(findPlayer(play));
               if(playerView3.getVisibility()!=View.VISIBLE)
            	   playerView3.setVisibility(View.VISIBLE);
               break;
           case 3:
        	   playerView4.showAsNormal(play.getInfo(),room);
               playerView4.setTag(findPlayer(play));
               if(playerView4.getVisibility()!=View.VISIBLE)
            	   playerView4.setVisibility(View.VISIBLE);
               break;
           case 4:
        	   playerView5.showAsNormal(play.getInfo(),room);
               playerView5.setTag(findPlayer(play));
               if(playerView5.getVisibility()!=View.VISIBLE)
            	   playerView5.setVisibility(View.VISIBLE);
               break;
           case 5:
        	   playerView6.showAsNormal(play.getInfo(),room);
               playerView6.setTag(findPlayer(play));
               if(playerView6.getVisibility()!=View.VISIBLE)
            	   playerView6.setVisibility(View.VISIBLE);
               break;
        }
    }
	
	
	private class WorkHandler extends Handler {
        
//      private static final int MSG_CHAIR_UPDATE = 1;
      private static final int MSG_SEND_BOOL = 2;
      private static final int MSG_START_GAME = 3;
      private static final int MSG_ROOM_UPDATE = 4;
      private static final int MSG_CHECKISME = 5;
      private static final int MSG_SHOW_PUBLIC_POKER = 6;
      private static final int MSG_ADD_BET =7;
//    private static final int MSG_UPDATE_MONEY =8;
      private static final int MSG_ACTION_ABANDOM = 8;
      private static final int MSG_NEXT_ROUND = 9;
      private static final int MSG_COUNT_POOL = 10;
      private static final int MSG_UPDATE_CHAIR = 11;
      private static final int MSG_RESET_ROUND = 12;
      private static final int MSG_GAME_OVER = 13;
      private static final int MSG_GAME_ROUND_TEXT = 14;
      private static final int MSG_KICK_MAN = 15;
	    
	    
	    @SuppressLint("HandlerLeak")
		public WorkHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case MSG_CHAIR_UPDATE://新界面已经去除此行为
//                    chairUpdate(playerList);
//                    break;
                case MSG_SEND_BOOL:
                	bottomDeal();//一样
                	break;
                case MSG_START_GAME://一样
                	startGame();
                	break;
                case MSG_ROOM_UPDATE://一样
                    updateRoom(room,app.isServer());
                    break;
                case MSG_CHECKISME:
                    checkIsMeOption();//一样
                    break;
                case MSG_SHOW_PUBLIC_POKER://一样
                    for(int i = 0;i<playerList.size();i++){
                        int ar = playerList.get(i).getInfo().getAroundChip();
                        int asr = playerList.get(i).getInfo().getAroundSumChip();
                        playerList.get(i).getInfo().setAroundSumChip(asr+ar);
                    }
                	resetAllPlayerAroundChip();
                	showPublicPoker();
                	break;
                case MSG_ADD_BET://有差异,这是设置所有人的
                    setChairChip(msg.arg1, msg.arg2, View.VISIBLE);
                    break;
                case MSG_ACTION_ABANDOM://一样
                    playerAbandom(msg.arg1);
                    break;
                case MSG_NEXT_ROUND://一样
                	shareMoney();
                    break;
                case MSG_COUNT_POOL://一样
                	countMoney();
                    break;
                case MSG_UPDATE_CHAIR://差异，原来的缺少点，设置等待界面，酌情
                    //setWaitingPersonView();
                	chairUpdate(playerList);
                    break;
                case MSG_RESET_ROUND://差异，重新设置开局，必须加上
                    resetAllPlayerAroundChip();
                    
                    closeWinView();
                    resetAllPoker();
                    resetChipPool();
                    bottomDeal();
                    break;
                case MSG_GAME_OVER://差异，游戏结束，必须加上
                	Log.i("Rinfon", "Game over");
                    gameOver();
                    break;
                case MSG_GAME_ROUND_TEXT:
                    updateRoundText(); //差异，游戏下一轮，必须加上
                    break;
                default:
                    break;
            }
        }
	}
	
	//------------Handler------------//
	// 发底牌，有差异
    private void bottomDeal() {
    	int index = findIndexWithIPinList(this.playerList);
    	bigBlindIndex = (DIndex+2)%playerList.size();
    	maxChipIndex = bigBlindIndex;
    	//setDIndex(DIndex);
    	setChairChip(bigBlindIndex,room.getMinStake(),View.VISIBLE);
    	setChairChip((DIndex+1)%playerList.size(),room.getMinStake()/2,View.VISIBLE);
    	this.currentOptionPerson = (maxChipIndex+1)%playerList.size();
    	playerView1.setCards(allPokerList.get(index*2), allPokerList.get(index*2+1));
    	PlayerView[] players = {playerView6,playerView2,playerView3,playerView4,playerView5};
    	for(PlayerView pView:players){
    		int mIndex = Integer.parseInt(pView.getTag().toString());
    		if(mIndex==-1){
    			break;
    		}
    		pView.setPoker(allPokerList.get(mIndex*2), allPokerList.get(mIndex*2+1));
    	}
//        seat_one.ownPokerAnim();
//        seat_two.ownPokerAnim();
//        seat_three.ownPokerAnim();
//        seat_four.ownPokerAnim();
//        seat_five.ownPokerAnim();
//        seat_six.ownPokerAnim();
    	//此处设置所有人的出牌动画
        checkIsMeOption();
    }
    //检查是否轮到我操作
    public void checkIsMeOption(){
        if(playerList.get(currentOptionPerson).getInfo().getId().equals(currentPlayer.getInfo().getId())){
            
            currentPlayer.getInfo().setQuit(playerList.get(currentOptionPerson).getInfo().isQuit());
            if(playerList.get(currentOptionPerson).getInfo().isQuit()){
                sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_FINISH_OPTIOIN,
                        -1, null));
                if(app.isServer()){
                    currentOptionPerson = (currentOptionPerson+1)%playerList.size(); 
                    wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
                    wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
                }
                Log.i("checkIsMeOption","currentOptionPerson:"+currentOptionPerson+"--1");
                optionChoice(false);
                return ;
           }else{
        	   //CQF轮到的是未弃权的其他人时
        	   //CQF前一个人头像变小，此人头像变大
           }
            
           if(currentOptionPerson == maxChipIndex){
               isEnd = true;
           }else{
               isEnd = false;
           }
           if(isEnd&&currentPlayCount() == 1){
//             所有玩家都弃牌，只剩一个玩家，进行分钱
               //showToast("一轮游戏结束");
               return;
           }else{
        	   if(poker5.getVisibility()==View.VISIBLE&&isEnd&&maxChipIndex!=findPlayer(currentPlayer)){
        		   optionChoice(false);
        	   }else{
        		   optionChoice(true);
        	   }
           }
       }else{
           optionChoice(false);
       }
   }
    //是否显示出操作栏
    public void optionChoice(boolean choice){
    	if(choice){
    		//CQF如果是轮到自己此处应有震动
    		showActionView();
    		SystemUtil.Vibrate(this, 500);
    	}else{
    		hideActionView();
    	}
    }
    //获取当前尚未弃牌的人数
    public int currentPlayCount(){
        int j = 0;
        for(int i = playerList.size()-1;i>=0;i--){
            if(playerList.get(i).getInfo().isQuit()){
                j++;
            }
        }
        return playerList.size() - j;
    }
    public int findIndexWithIPinList(ArrayList<ClientPlayer> playerList){
        for(int i = playerList.size()-1;i>=0;i--){
            if(currentPlayer.getInfo().getId().equals(playerList.get(i).getInfo().getId())){
                return i;
            }
        }
        return -1;
    }
    //设置所有人的筹码数Y
    public void setChairChip(int playerIndex,int money,int isShow){
        if(money == -1){
        //如果是-1，则不需要设置
            return ;
        }
    	if(money == 0){
    		playerList.get(playerIndex).getInfo().setAroundChip(0);
    	}else{
	        playerList.get(playerIndex).getInfo().setBaseMoney(playerList.get(playerIndex).getInfo().getBaseMoney()
	                +playerList.get(playerIndex).getInfo().getAroundChip() - money);
	        playerList.get(playerIndex).getInfo().setAroundChip(money);
    	}
    	 if(playerIndex == Integer.parseInt(playerView1.getTag().toString())){
    		 playerView1.setBet(money);
    		 playerView1.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
         }
    	 if(playerIndex == Integer.parseInt(playerView2.getTag().toString())){
    		 playerView2.setBet(money);
    		 playerView2.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
    	 }
    	 if(playerIndex == Integer.parseInt(playerView3.getTag().toString())){
    		 playerView3.setBet(money);
    		 playerView3.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
    	 }
    	 if(playerIndex == Integer.parseInt(playerView4.getTag().toString())){
    		 playerView4.setBet(money);
    		 playerView4.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
    	 }
    	 if(playerIndex == Integer.parseInt(playerView5.getTag().toString())){
    		 playerView5.setBet(money);
    		 playerView5.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
    	 }
    	 if(playerIndex == Integer.parseInt(playerView6.getTag().toString())){
    		 playerView6.setBet(money);
    		 playerView6.setMoney(playerList.get(playerIndex).getInfo().getBaseMoney());
    	 }
    	 if(playerIndex == currentPlayIndex){
    		 currentPlayer.setInfo(playerList.get(playerIndex).getInfo());
    	 }
    }
	
    //开始游戏
    public void startGame(){
        hideRoom();
        if(room.getInnings() == -1){
            aroundIndex = -1;
        }else{
            aroundIndex = 0;
        }
        wHandler.removeMessages(WorkHandler.MSG_GAME_ROUND_TEXT);
        wHandler.sendEmptyMessage(WorkHandler.MSG_GAME_ROUND_TEXT);
        currentPlayIndex  = findIndexWithIPinList(playerList);
        app.isGameStarted = true;
        currentPlayer.setInfo(playerList.get(currentPlayIndex).getInfo());
        currentPlayer.getInfo().setBaseMoney(room.getBasicChips());
        currentPlayer.getInfo().setQuit(false);
        if(app.isServer()){
        	forbidJoin();
            if(isInOrOut||DIndex == -1){
    //            重新生成D
            	newDIndex();
            }else {
                DIndex++;
            }
            
            if(DIndex>=playerList.size()){
                DIndex = DIndex%playerList.size();
            }
    //      每次生成D之后都会发底牌
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_SEND_BOOL, -1, String.valueOf(DIndex)));
            wHandler.removeMessages(WorkHandler.MSG_SEND_BOOL);
            wHandler.sendEmptyMessage(WorkHandler.MSG_SEND_BOOL);
        }
    }
    public void newDIndex(){
        Random r = new Random();
        DIndex = r.nextInt(playerList.size());
    }
    
    //重置所有人的本轮下注值
    public void resetAllPlayerAroundChip(){
        for(int i = 0;i<playerList.size();i++){
            setChairChip(i, 0, View.INVISIBLE);
        }
    }
	//展示公共牌
    public void showPublicPoker() {
        if(poker5.getVisibility() == View.VISIBLE){
//            一轮结束，进行下一轮
            optionChoice(false);
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_NEXT_ROUND, -1, null));
            wHandler.removeMessages(WorkHandler.MSG_NEXT_ROUND);
            wHandler.sendEmptyMessage(WorkHandler.MSG_NEXT_ROUND);
        }
        if (poker1.getVisibility() != View.VISIBLE) {
        	poker1.setImageResource(allPokerList.get(allPokerList.size()-5).getPokerImageId());
        	poker2.setImageResource(allPokerList.get(allPokerList.size()-4).getPokerImageId());
        	poker3.setImageResource(allPokerList.get(allPokerList.size()-3).getPokerImageId());
            poker1.setVisibility(View.VISIBLE);
            poker2.setVisibility(View.VISIBLE);
            poker3.setVisibility(View.VISIBLE);
            //CQF此处加上三张牌出现的动画
            wHandler.removeMessages(WorkHandler.MSG_COUNT_POOL);
            wHandler.sendEmptyMessage(WorkHandler.MSG_COUNT_POOL);
            return;
        } else if (poker4.getVisibility() != View.VISIBLE) {
        	poker4.setImageResource(allPokerList.get(allPokerList.size()-2).getPokerImageId());
            poker4.setVisibility(View.VISIBLE);
            //CQF此处加上第四章牌的出现动画
            wHandler.removeMessages(WorkHandler.MSG_COUNT_POOL);
            wHandler.sendEmptyMessage(WorkHandler.MSG_COUNT_POOL);
            return;
        } else if (poker5.getVisibility() != View.VISIBLE) {
        	poker5.setImageResource(allPokerList.get(allPokerList.size()-1).getPokerImageId());
            poker5.setVisibility(View.VISIBLE);
            //CQF此处加上第五张牌出现的动画
            wHandler.removeMessages(WorkHandler.MSG_COUNT_POOL);
            wHandler.sendEmptyMessage(WorkHandler.MSG_COUNT_POOL);
            return;
        } else {
            return;
        }
    }
    
    //弃牌后的界面变化
    @SuppressLint("NewApi")
	public void playerAbandom(int playerIndex){
        playerList.get(playerIndex).getInfo().setQuit(true);
        if(playerIndex == Integer.parseInt(playerView1.getTag().toString())){
            playerView1.setAlpha(ABANDOM_TRANSPARENT);
        }
        else if(playerIndex == Integer.parseInt(playerView2.getTag().toString())){
        	playerView2.setAlpha(ABANDOM_TRANSPARENT);
        }
        else if(playerIndex == Integer.parseInt(playerView3.getTag().toString())){
        	playerView3.setAlpha(ABANDOM_TRANSPARENT);
        }
        else if(playerIndex == Integer.parseInt(playerView4.getTag().toString())){
        	playerView4.setAlpha(ABANDOM_TRANSPARENT);
        }
        else if(playerIndex == Integer.parseInt(playerView5.getTag().toString())){
        	playerView5.setAlpha(ABANDOM_TRANSPARENT);
        }
        else if(playerIndex == Integer.parseInt(playerView6.getTag().toString())){
        	playerView6.setAlpha(ABANDOM_TRANSPARENT);
        }
    }
    
    //分钱
    public void shareMoney(){
        optionChoice(false);
        HashMap<String,ArrayList<ClientPlayer>> winSet = PokerUtil.getWinner(playerList, allPokerList);
        if(winSet.get("0").contains(currentPlayer)){
        	closeWinView();
        }else{
            setWinView(winSet.get("0").get(0));
        }
        for(int i = 0;i<winSet.size();i++){
            for(int j = 0;j<winSet.get(String.valueOf(i)).size();j++){
               int sum = PokerUtil.getWinMoney(winSet.get(String.valueOf(i)).get(0).getInfo().getId(), playerList);
               for(int n = 0;n<winSet.get(String.valueOf(i)).size();n++){
                   setBaseMoney(winSet.get(String.valueOf(i)).get(n), (sum/winSet.get(String.valueOf(i)).size())
                           +winSet.get(String.valueOf(i)).get(n).getInfo().getBaseMoney());
               }
            }
        }
        showAroundMessage(); 
    }
    
    
    //设置所有人的基础钱，CQF应根据女郎判断
    public void setBaseMoney(ClientPlayer player,int money){
   	 int playerIndex = findPlayer(player);
   	 if(playerIndex == Integer.parseInt(playerView1.getTag().toString())){
   		playerView1.setMoney(money);
        }
   	 if(playerIndex == Integer.parseInt(playerView2.getTag().toString())){
   		playerView2.setMoney(money);
   	 }
   	 if(playerIndex == Integer.parseInt(playerView3.getTag().toString())){
   		playerView3.setMoney(money);
   	 }
   	 if(playerIndex == Integer.parseInt(playerView4.getTag().toString())){
   		playerView4.setMoney(money);
   	 }
   	 if(playerIndex == Integer.parseInt(playerView5.getTag().toString())){
   		playerView5.setMoney(money);
   	 }
   	 if(playerIndex == Integer.parseInt(playerView6.getTag().toString())){
   		playerView6.setMoney(money);
   	 }
   	 playerList.get(playerIndex).getInfo().setBaseMoney(money);
   }
    public void setWinView(ClientPlayer cp){
    	//根据玩家信息展示赢家信息和牌型
    	//CQF——PokerUtil.getCardTypeString(p.getInfo().getCardType()
    	
    }
    //找人
    public int findPlayer(ClientPlayer player){
        for(int i = playerList.size()-1;i>=0;i--){
            if(player.getInfo().getId().equals(playerList.get(i).getInfo().getId())){
                return i;
            }
        }
        return -1;
    }
    public void closeWinView(){
    	
    }
    //显示大家牌型,包含在shareMoney中
    public void showAroundMessage(){
        int index = findIndexWithIPinList(playerList);
        //CQF此处显示自己的牌型
        //setText(PokerUtil.getCardTypeString(playerList.get(index).getInfo().getCardType()));
        //setVisibility(View.VISIBLE);
        playerView1.showPokerType();
        PlayerView[] players = {playerView6,playerView2,playerView3,playerView4,playerView5};
    	for(PlayerView pView:players){
    		int mIndex = Integer.parseInt(pView.getTag().toString());
    		if(mIndex==-1){
    			break;
    		}
    		pView.showPoker(PokerUtil.getCardType(playerList.get(mIndex).getInfo().getCardType()));
    	}
        if(room.getInnings()!=-1)
            aroundIndex++;
        wHandler.removeMessages(WorkHandler.MSG_GAME_ROUND_TEXT);
        wHandler.sendEmptyMessage(WorkHandler.MSG_GAME_ROUND_TEXT);
        new Handler().postDelayed(new Runnable(){    
            public void run() {    
                if(app.isServer()){
                	if(room.getInnings() == -1||(aroundIndex<room.getInnings()&&checkMoneyEnough())){
//                        if(room.getInnings()!=-1)
//                            aroundIndex++;
                        if(isInOrOut||DIndex == -1){
                            newDIndex();
                        }else {
                            DIndex++;
                        }
                        
                        if(DIndex>=playerList.size()){
                            DIndex = DIndex%playerList.size();
                        }
                        allPokerList = PokerUtil.getPokers(playerList.size());
                        sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_RESET_ROUND, -1, String.valueOf(DIndex), allPokerList,null));
                        wHandler.removeMessages(WorkHandler.MSG_RESET_ROUND);
                        wHandler.sendEmptyMessage(WorkHandler.MSG_RESET_ROUND);
                    }else{
                        sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_GAME_OVER, -1, null));
                        wHandler.removeMessages(WorkHandler.MSG_GAME_OVER);
                        wHandler.sendEmptyMessage(WorkHandler.MSG_GAME_OVER);
                    }
                }
            }    
         }, 5000);  

    }
    //共有，检查每个人是否有剩下的钱,以确定是否开始下一局，需要移植
    public boolean checkMoneyEnough(){
        int count = 0;
        for(int i = 0;i<playerList.size();i++){
            if(playerList.get(i).getInfo().getBaseMoney() == 0){
                count++;
            }
        }
        if((playerList.size() - count)>1){
            return true;
        }else{
            return false;
        }
    }
    
    public void countMoney(){
        ArrayList<Integer> callBack = new ArrayList<Integer>();
        chipList.clear();
        for(int i = playerList.size()-1;i>=0;i--){
            chipList.add(playerList.get(i).getInfo().getAroundSumChip());
            
        }
        PokerUtil.CountChips(chipList, callBack);
        setChipPool(callBack);
        
    }
    //设置奖池的钱
    public void setChipPool(ArrayList<Integer> chips){
    	//CQF可以添加下注到奖池的动画
        for(int i = 0;i<chips.size();i++){
            txtPools[i].setVisibility(View.VISIBLE);
            txtPools[i].setText(chips.get(i).toString());
            txtPools[i].setVisibility(View.VISIBLE);
        }
    }

    //踢人后重新排布位置
    private void setWaitingPersonView(){
        if (app.isServer()) {
            if(playerList.size()>1){
                btnStart.setVisibility(View.INVISIBLE);
            }
            else{
            	btnStart.setVisibility(View.VISIBLE);
            }
        }
        int index = 0;
        for(int i = 0;i<playerList.size();i++){
//            waitingImg[i].setImageResource(UserUtil.head_img[playerList.get(i).getInfo().getAvatar()]);
//            waitingText[i].setText(playerList.get(i).getInfo().getName());
//            waiting_roomerLayout[i].setVisibility(View.VISIBLE);
        	  //CQF重新排布已有人的位置
            index++;
        }
        for(int i = index;i<6;i++){
//            waiting_roomerLayout[i].setVisibility(View.INVISIBLE);
        	  //CQF其他人的位置消失
        }
    }
    
    //复原poker
    public void resetAllPoker(){
        poker1.setVisibility(View.INVISIBLE);
        poker2.setVisibility(View.INVISIBLE);
        poker3.setVisibility(View.INVISIBLE);
        poker4.setVisibility(View.INVISIBLE);
        poker5.setVisibility(View.INVISIBLE);
        playerView1.hideCards();
        //CQF其他人的牌型也要消失
        PlayerView[] players = {playerView6,playerView2,playerView3,playerView4,playerView5};
    	for(PlayerView pView:players){
    		int mIndex = Integer.parseInt(pView.getTag().toString());
    		if(mIndex==-1){
    			break;
    		}
    		pView.hidePoker();
    	}
    }
    //共有，需要移植，奖池重新出现
    public void resetChipPool(){
        for(int i = 0;i<txtPools.length;i++){
            txtPools[i].setVisibility(View.INVISIBLE);
        }
    }
    
    //游戏完全结束，出现排名
    public void gameOver(){
    	//CQF此处可出现排名
//        Intent intent = new Intent();
//        int index = findIndexWithIPinList(playerList);
//        Bundle bundle = new Bundle();  
//        bundle.putSerializable("arrayList", playerList);  
//        intent.putExtras(bundle);
//        intent.putExtra("index", index);
//        intent.setClass(NewGameActivity.this, RankActivity.class);
//        startActivity(intent);
    }
    
    //设置局数，酌情处理
    public void updateRoundText() {
        // TODO Auto-generated method stub
//        if(aroundIndex!=-1){
//            game_round_txt.setText(aroundIndex+1+"/"+room.getInnings()+"局");
//        }else{
//            game_round_txt.setText("无限局");
//        }
        
    }
  //------------Handler------------//
    
    
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
		Log.e("frankchan", "收到");
		if(msg.isExit()){
            int exitIndex = findPlayer(msg.getPlayerList().get(0));
            if(exitIndex!=-1){
            	sendMessage(msg);
                playerList.remove(exitIndex);
                wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
                wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
            }
        }else if (msg.getPlayerList() != null && msg.getPlayerList().get(0) != null) {
        	msg.getPlayerList().get(0).getInfo().setBaseMoney(room.getBasicChips());
        	msg.getPlayerList().get(0).getInfo().setAroundChip(0);
        	msg.getPlayerList().get(0).getInfo().setAroundSumChip(0);
        	msg.getPlayerList().get(0).getInfo().setQuit(false);
			playerList.add(msg.getPlayerList().get(0));
			msg.setPlayerList(playerList);
			msg.setRoom(room);
			sendMessage(msg);
			Log.i("frankchan", "服务器转发");
            wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
            wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
		}
	}

	@Override
	public void onServerReceive(GameMessage msg) {
		// TODO Auto-generated method stub
		Log.e("frankchan", "收到");
		switch(msg.getAction()){
    	case GameMessage.ACTION_FINISH_OPTIOIN://Y
    		currentOptionPerson = (currentOptionPerson+1)%playerList.size();
    		sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_CURRENT_PERSON,
    				-1, String.valueOf(currentOptionPerson)));
    		wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    		break;
    	case GameMessage.ACTION_SHOW_PUBLIC_POKER://Y
    		sendMessage(msg);
    		DIndex = Integer.parseInt(msg.getExtra().toString());
    		currentOptionPerson = (DIndex+1)%playerList.size();
            int i = DIndex;
            while(playerList.get(i%playerList.size()).getInfo().isQuit()){
                i = Math.abs(i-1);
            }
            maxChipIndex = i;
//    		sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_SHOW_PUBLIC_POKER,
//    				-1, String.valueOf(DIndex)));N
    		wHandler.removeMessages(WorkHandler.MSG_SHOW_PUBLIC_POKER);
            wHandler.sendEmptyMessage(WorkHandler.MSG_SHOW_PUBLIC_POKER);
    		wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    		break;
    	case GameMessage.ACTION_ADD_BET://Y
    	    maxChipIndex = Integer.parseInt(msg.getExtra());
    	    sendMessage(msg);
    	    Message message = new Message();
    	    message.what = WorkHandler.MSG_ADD_BET;
    	    message.arg1 = currentOptionPerson;//差异点！！！Y
    	    message.arg2 = msg.getAmount();
            wHandler.sendMessage(message);
            
//            加注后继续判断下一家
            currentOptionPerson = (currentOptionPerson+1)%playerList.size();
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_CURRENT_PERSON,
                    -1, String.valueOf(currentOptionPerson)));
            wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    	    break;
    	case GameMessage.ACTION_UPDATE_MONEY://Y
    	    sendMessage(msg);
    	    
    	    Message message2 = new Message();
    	    message2.what = WorkHandler.MSG_ADD_BET;
    	    message2.arg1 = Integer.parseInt(msg.getExtra());
    	    message2.arg2 = msg.getAmount();
            wHandler.sendMessage(message2);
            break;
        case GameMessage.ACTION_ABANDOM://一样
            sendMessage(msg);
            Message message3 = new Message();
            message3.what = WorkHandler.MSG_ACTION_ABANDOM;
            message3.arg1 = Integer.parseInt(msg.getExtra());
            wHandler.sendMessage(message3);
//          弃牌后继续判断下一家
          currentOptionPerson = (currentOptionPerson+1)%playerList.size();
          sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_CURRENT_PERSON,
                  -1, String.valueOf(currentOptionPerson)));
          wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
          wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
            break;
        case GameMessage.ACTION_CLIENT_EXIT:
    		if(msg.isExit()){
                int exitIndex = findPlayer(msg.getPlayerList().get(0));
                if(exitIndex!=-1){
                	sendMessage(msg);
                    playerList.remove(exitIndex);
                    wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
                    wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
                }
    		}
        	break;
    	}
	}

	@Override
	public void onClientReceive(PeopleMessage msg) {
		Log.e("frankchan", "收到");
		// TODO Auto-generated method stub
		if (msg.isExit()) {
        	if("server exit".equals(msg.getExtra())){
        		Toast.makeText(getApplicationContext(), "Server exit", 1000).show();
        		restartApplication();
        	}
        	playerList.remove(msg.getPlayerList().get(0));
            wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
            wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
        }
        if (msg.isStart()) {
//          游戏开始
            app.isGameStarted = true;
        	this.allPokerList = msg.getPokerList();
        	wHandler.removeMessages(WorkHandler.MSG_START_GAME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_START_GAME);
        } else if(msg.getExtra()!=null&&msg.getExtra().toString().equals("KICK_MAN")){
            ArrayList<ClientPlayer> kickMan =  msg.getPlayerList();
            if(kickMan.get(0).getInfo().getId().equals(currentPlayer.getInfo().getId())){
                showToast(getString(R.string.game_kick_by_owner));
        		restartApplication();
            }else{
                playerList.remove(findPlayer(kickMan.get(0)));
                wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
                wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
            }
        } else {
            this.playerList.clear();
            this.playerList.addAll(msg.getPlayerList());
            if(this.room == null){
            	this.room = msg.getRoom();
            	wHandler.removeMessages(WorkHandler.MSG_ROOM_UPDATE);
                wHandler.sendEmptyMessage(WorkHandler.MSG_ROOM_UPDATE);
                aroundIndex = this.room.getInnings();
                wHandler.removeMessages(WorkHandler.MSG_GAME_ROUND_TEXT);
                wHandler.sendEmptyMessage(WorkHandler.MSG_GAME_ROUND_TEXT);
            }   
            wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
            wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
        }
	}

	@Override
	public void onClientReceive(GameMessage msg) {
		Log.e("frankchan", "收到");
		// TODO Auto-generated method stub
		int cmd = msg.getAction();
    	Log.i("onClientReceive","action id :"+cmd);
    	switch(cmd){
    	case GameMessage.ACTION_SEND_BOOL://一样
    		 if(msg.getExtra()!=null){
     	        this.DIndex = Integer.parseInt(msg.getExtra());
     	    }
     		wHandler.removeMessages(WorkHandler.MSG_SEND_BOOL);
             wHandler.sendEmptyMessage(WorkHandler.MSG_SEND_BOOL);
    		break;
    	case GameMessage.ACTION_CURRENT_PERSON://一样
    		currentOptionPerson = Integer.parseInt(msg.getExtra().toString());
    		wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    		break;
    	case GameMessage.ACTION_FINISH_OPTIOIN://一样
    		currentOptionPerson = (currentOptionPerson+1)%playerList.size();
    		wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    		break;
    	case GameMessage.ACTION_SHOW_PUBLIC_POKER://一样
    		DIndex = Integer.parseInt(msg.getExtra().toString());
    		currentOptionPerson = (DIndex+1)%playerList.size();
    		int i = DIndex;
            while(playerList.get(i%playerList.size()).getInfo().isQuit()){
                i = Math.abs(i-1);
            }
            maxChipIndex = i;
			wHandler.removeMessages(WorkHandler.MSG_SHOW_PUBLIC_POKER);
	        wHandler.sendEmptyMessage(WorkHandler.MSG_SHOW_PUBLIC_POKER);
			wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
	        wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
			break;
    	case GameMessage.ACTION_ADD_BET://一样有差异Y
    	    maxChipIndex = Integer.parseInt(msg.getExtra());
            Message message = new Message();
            message.what = WorkHandler.MSG_ADD_BET;
            message.arg1 = currentOptionPerson;//！！！差异Y
            message.arg2 = msg.getAmount();
            wHandler.sendMessage(message);
            
            currentOptionPerson = (currentOptionPerson+1)%playerList.size();
            wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
	        wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
    	    break;
        case GameMessage.ACTION_UPDATE_MONEY://差异巨大Y
            Message message2 = new Message();
            message2.what = WorkHandler.MSG_ADD_BET;
            message2.arg1 = Integer.parseInt(msg.getExtra());
            message2.arg2 = msg.getAmount();
            wHandler.sendMessage(message2);
            break;
        case GameMessage.ACTION_ABANDOM://差异巨大
            Message message3 = new Message();
            message3.what = WorkHandler.MSG_ACTION_ABANDOM;
            message3.arg1 = Integer.parseInt(msg.getExtra());
            wHandler.sendMessage(message3);
            //差异点，必须判断下家
            currentOptionPerson = (currentOptionPerson+1)%playerList.size();
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_CURRENT_PERSON,
                  -1, String.valueOf(currentOptionPerson)));
            wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
            break;
        case GameMessage.ACTION_NEXT_ROUND:
        	//CQF收到本轮结束的消息
            wHandler.removeMessages(WorkHandler.MSG_NEXT_ROUND);
            wHandler.sendEmptyMessage(WorkHandler.MSG_NEXT_ROUND);
            break;
        case GameMessage.ACTION_RESET_ROUND://缺少，必须加上
            this.allPokerList = msg.getPokerList();
            DIndex = Integer.parseInt(msg.getExtra());
            wHandler.removeMessages(WorkHandler.MSG_RESET_ROUND);
            wHandler.sendEmptyMessage(WorkHandler.MSG_RESET_ROUND);
            break;
        case GameMessage.ACTION_GAME_OVER://缺少，必须加上
        	wHandler.removeMessages(WorkHandler.MSG_GAME_OVER);
            wHandler.sendEmptyMessage(WorkHandler.MSG_GAME_OVER);
        	break;    
        case GameMessage.ACTION_CLIENT_EXIT:
        	if (msg.isExit()&&msg.getExtra().equals("client exit")) {
            	playerList.remove(msg.getPlayerList().get(0));
                wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
                wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
        	}
        	break;
        case GameMessage.ACTION_SERVER_EXIT:
        	if (msg.isExit()&&msg.getExtra().equals("server exit")) {
            	showToast("服务器退出游戏");
            	wHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						restartApplication();
					}
				}, 2000);
        	}
        	break;
        }
	}

	public void doOption(int cmd){
    	if(!currentPlayer.getInfo().isQuit()){
    		if(currentPlayer.getInfo().getBaseMoney()>0){
    			if(cmd == R.id.game_btn_follow&&isAllowClick){
    				followAction();
    			}else if(cmd == R.id.game_btn_add&&isAllowClick){
    				addChipAction();
    			}else if(cmd == R.id.game_btn_quit&&isAllowClick){
    				doQuit();
    			}
    		}
    	}
	}
	
    public void followAction(){
    	SoundPlayer.playMusic(2, false);
    	 int money = -1;
		   if(currentPlayer.getInfo().getAroundChip()<playerList.get(maxChipIndex).getInfo().getAroundChip()){
		        if(currentPlayer.getInfo().getBaseMoney()<(playerList.get(maxChipIndex).getInfo().getAroundChip()
		                -currentPlayer.getInfo().getAroundChip())){
		            money = currentPlayer.getInfo().getBaseMoney();
		        }else{
		            money = playerList.get(maxChipIndex).getInfo().getAroundChip();
		        }
		        
		        
		    }
		   sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_UPDATE_MONEY, 
	                money, String.valueOf(currentOptionPerson)));
		   Log.i("followAction","isEnd"+isEnd);
			if(isEnd){
				sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_SHOW_PUBLIC_POKER,
						-1,  String.valueOf(DIndex)));
				if(app.isServer()){
					 Log.i("followAction","one");
	                   Message message = new Message();
	                   message.what = WorkHandler.MSG_ADD_BET;
	                   message.arg1 = currentOptionPerson;
	                   message.arg2 = money;
	                   wHandler.sendMessage(message);
	                   currentOptionPerson = (DIndex+1)%playerList.size();
	                   int i = DIndex;
	                   while(playerList.get(i%playerList.size()).getInfo().isQuit()){
	                       i = Math.abs(i-1);
	                   }
	                   Log.i("followAction","two");
	                   maxChipIndex = i;
	                   wHandler.removeMessages(WorkHandler.MSG_SHOW_PUBLIC_POKER);
	                   wHandler.sendEmptyMessage(WorkHandler.MSG_SHOW_PUBLIC_POKER);
	                   wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
	                   wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
				}
			}else{
				 Log.i("followAction","three");
				sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_FINISH_OPTIOIN,
						-1, null));
				if(app.isServer()){
					Log.i("followAction","four");
				    Message message2 = new Message();
	                 message2.what = WorkHandler.MSG_ADD_BET;
	                 message2.arg1 = currentOptionPerson;
	                 message2.arg2 = money;
	                 wHandler.sendMessage(message2);
					currentOptionPerson = (currentOptionPerson+1)%playerList.size();
					Log.i("followAction","five");
					wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
		            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
				}
			}
    }
	
    public void addChipAction(){
    	//CQF不用下面的预设变量
    	int money = mCurAddBet;
    	if(money == 0){
    	    followAction();
    	}else{
    		SoundPlayer.playMusic(0, false);
    	    money += playerList.get(maxChipIndex).getInfo().getAroundChip();
            if(playerList.get(currentOptionPerson).getInfo().getBaseMoney()<=money){
                money = playerList.get(currentOptionPerson).getInfo().getBaseMoney()+playerList.get(currentOptionPerson).getInfo().getAroundChip();
                if(money>playerList.get(maxChipIndex).getInfo().getAroundChip()){
                    maxChipIndex = currentOptionPerson;
                }
            }else{
                maxChipIndex = currentOptionPerson;
                isEnd = false;
            }
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_ADD_BET, money, String.valueOf(maxChipIndex)));
            if(app.isServer()){
         	    Message message = new Message();
         	    message.what = WorkHandler.MSG_ADD_BET;
         	    message.arg1 = currentOptionPerson;
         	    message.arg2 = money;
                wHandler.sendMessage(message);
                currentOptionPerson = (currentOptionPerson+1)%playerList.size();
                wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
                wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
            }
    	}
    }
    
    //CQF踢人行为，根据PlayerView设置的长按时间
    public void doKick(View v){
    	if(app.isServer()){
            int index = Integer.parseInt(v.getTag().toString());
            ArrayList<ClientPlayer> kickMan = new ArrayList<ClientPlayer>();
            kickMan.add(playerList.get(index));
            sendMessage(MessageFactory.newPeopleMessage(false, false, kickMan, null, room, "KICK_MAN"));
            playerList.remove(index);
            wHandler.removeMessages(WorkHandler.MSG_UPDATE_CHAIR);
            wHandler.sendEmptyMessage(WorkHandler.MSG_UPDATE_CHAIR);
        }
    }
	
    public void doQuit(){
        Log.i("Rinfon", currentOptionPerson+"");
        SoundPlayer.playMusic(3, false);
        //CQF此处加以震动
        sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_ABANDOM, -1, String.valueOf(currentOptionPerson)));
        if(app.isServer()){
            Message message = new Message();
            message.what = WorkHandler.MSG_ACTION_ABANDOM;
            message.arg1 = findPlayer(currentPlayer);
            wHandler.sendMessage(message);
            
            currentOptionPerson = (currentOptionPerson+1)%playerList.size();
            sendMessage(MessageFactory.newGameMessage(false, GameMessage.ACTION_CURRENT_PERSON,
                    -1, String.valueOf(currentOptionPerson)));
            wHandler.removeMessages(WorkHandler.MSG_CHECKISME);
            wHandler.sendEmptyMessage(WorkHandler.MSG_CHECKISME);
        }
    }
	
    private void hideIntroView(){
	//CQF关闭牌型介绍	
    }
	
	private void showIntroView(){
		//开启牌型介绍
	}
	
	//操作栏滑入
	private void showActionView(){
		Log.i("frankchan", "操作栏滑入");
		isAllowClick = true;
		actionView.setVisibility(View.VISIBLE);
		btnAdd.setText(getString(R.string.game_action_add));
	}
	//操作栏滑出
	private void hideActionView(){
		Log.i("frankchan", "操作栏滑出");
		isAllowClick = true;
		actionView.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.game_btn_add:
			if(seekView.getVisibility()==View.GONE){
				seekView.setVisibility(View.VISIBLE);
				seekbar.setProgress(1);
			}else{
				btnAdd.setText(getString(R.string.game_action_add));
				seekView.setVisibility(View.GONE);
				addChipAction();
			}
			seekbar.setProgress(0);
			break;
		case R.id.game_btn_follow:
			doOption(v.getId());
			break;
		case R.id.game_btn_quit:
			doOption(v.getId());
			break;
		case R.id.game_btn_start:
            if(this.playerList.size()>=2){
                allPokerList = PokerUtil.getPokers(playerList.size());
                sendMessage(MessageFactory.newPeopleMessage(true, false, playerList, allPokerList,null,null));
                wHandler.removeMessages(WorkHandler.MSG_START_GAME);
                wHandler.sendEmptyMessage(WorkHandler.MSG_START_GAME);
            }else{
                showToast(getString(R.string.game_player_not_enough));
            }
			break;
		case R.id.game_btn_help:
			//CQF牌型界面滑入
			break;
		case R.id.game_btn_exit:
			//CQF弹窗提示后进行下面的代码
			mConfirmDialog.setTopic(app.isGameStarted?getString(R.string.game_exit_open):getString(R.string.game_exit_stop));
			mConfirmDialog.show();
			break;
		default:
			break;
		}
	}

	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)  
        {  
			mConfirmDialog.setTopic(app.isGameStarted?getString(R.string.game_exit_open):getString(R.string.game_exit_stop));
			mConfirmDialog.show();
        }
		return false;
	}


	@Override
	public void onConfirm() {
		// TODO Auto-generated method stub
		playerList.clear();
        playerList.add(currentPlayer);
        if(app.isServer()){
        	if(!app.isGameStarted){
        		sendMessage(MessageFactory.newPeopleMessage(false, true, playerList, null,null,"server exit"));
        	}else{
        		sendMessage(MessageFactory.newGameMessage(true, GameMessage.ACTION_SERVER_EXIT,-1,"server exit", playerList));
        	}
        	if(mWifiUtils.getWifiApState()){
        		mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(
                        mWifiUtils.getApSSID(), WifiApConst.WIFI_AP_PASSWORD,
                        3, "ap"), false);
        	}
        }
        else{
            if(!app.isGameStarted){
                sendMessage(MessageFactory.newPeopleMessage(false, true, playerList, null,null,"client exit"));
        	}else{
        		sendMessage(MessageFactory.newGameMessage(true, GameMessage.ACTION_CLIENT_EXIT,-1,"client exit", playerList));
        	}
        }
		restartApplication();
	}

	private void doServerExit(){
		
	}
	
	private void doClientExit(){
		
	}
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

}
