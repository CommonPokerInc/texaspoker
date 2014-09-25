package com.texas.poker.ui.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.texas.poker.R;
import com.texas.poker.ui.AbsBaseActivity;
import com.texas.poker.ui.dialog.Effectstype;
import com.texas.poker.ui.dialog.HelpDialog;
import com.texas.poker.ui.dialog.TransferDialog;
import com.texas.poker.util.AnimationProvider;
import com.texas.poker.util.SettingHelper;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;
import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;

public class MainActivity extends AbsBaseActivity implements OnClickListener {

	private ImageButton btnSelf, btnHelp, btnCreate, btnJoin;

	private TextView btnTransfer, btnMoney, btnMarket;

	private ImageView imgTop, imgLeft, imgRight, imgDesk, imgLight;

	private View mView;
	
	private View[] mViews;

	private HelpDialog mHelpDialog;

	private TransferDialog mTransferDialog;

	private SettingHelper settingHelper;

	private ExecutorService mPool;

	private final static int MSG_SHOW_BACKGROUND = 0;

	private final static int MSG_SHOW_CURTAINS = 1;

	private final static int MSG_SHOW_LIGHTS = 2;

	private final static int MSG_SHOW_BOTTOM = 3;

	private final static int MSG_SHOW_EXTRA_BUTTONS = 4;

	private final static int MSG_SHOW_MAIN_BUTTONS = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settingHelper = new SettingHelper(this);
		mPool = Executors.newFixedThreadPool(5);
		initViews();
		startAnimation();
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			mPool.execute(copyRunnable);
		}
	}

	private void initViews() {
		btnHelp = (ImageButton) findViewById(R.id.main_btn_help);
		btnCreate = (ImageButton) findViewById(R.id.main_btn_cretae);
		btnJoin = (ImageButton) findViewById(R.id.main_btn_join);
		btnSelf = (ImageButton) findViewById(R.id.main_btn_self);
		btnTransfer = (TextView) findViewById(R.id.main_btn_transfer);
		btnMoney = (TextView) findViewById(R.id.main_btn_money);
		btnMarket = (TextView) findViewById(R.id.main_btn_market);
		imgTop = (ImageView) findViewById(R.id.main_img_curtain_top);
		imgLeft = (ImageView) findViewById(R.id.main_img_curtain_left);
		imgRight = (ImageView) findViewById(R.id.main_img_curtain_right);
		imgDesk = (ImageView) findViewById(R.id.main_img_desk);
		imgLight = (ImageView) findViewById(R.id.main_img_light);
		mView = findViewById(R.id.main_view);
		
		mViews = new View[]{btnTransfer,btnHelp,btnSelf,btnMoney,btnMarket};
		
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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SHOW_BACKGROUND:
				post(mBackgroundRunnable);
				break;
			case MSG_SHOW_CURTAINS:
				post(mCurtainsRunnable);
				break;
			case MSG_SHOW_BOTTOM:
				post(mBottomRunnable);
				break;
			case MSG_SHOW_LIGHTS:
				post(mLightRunnable);
				break;
			case MSG_SHOW_EXTRA_BUTTONS:
				post(mExtraBtnRunnable);
				break;
			case MSG_SHOW_MAIN_BUTTONS:
				post(mMainBtnRunnable);
				break;
			default:
				break;
			}
		}

	};

	private void startAnimation() {
		mHandler.sendEmptyMessage(MSG_SHOW_BACKGROUND);
	}

	private Runnable mBackgroundRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startShowAnimation(mView, AnimationProvider.getAlphaAnimation(
					AnimationProvider.TYPE_INTERPLATOR_ACCELERATE_DECELERATE,
					2500));
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_CURTAINS, 500);
		}
	};

	private Runnable mCurtainsRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startShowAnimation(imgTop, AnimationProvider.getTranslateAnimation(
					AnimationProvider.TYPE_ANIMATION_TOP_IN, 1,
					AnimationProvider.TYPE_INTERPLATOR_ACCELERATE, 1000));
			startShowAnimation(
					imgLeft,
					AnimationProvider
							.getTranslateAnimation(
									AnimationProvider.TYPE_ANIMATION_LEFT_IN,
									1,
									AnimationProvider.TYPE_INTERPLATOR_ACCELERATE_DECELERATE,
									1500));
			startShowAnimation(
					imgRight,
					AnimationProvider
							.getTranslateAnimation(
									AnimationProvider.TYPE_ANIMATION_RIGHT_IN,
									1,
									AnimationProvider.TYPE_INTERPLATOR_ACCELERATE_DECELERATE,
									1500));
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_BOTTOM, 500);
		}
	};

	private Runnable mLightRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startShowAnimation(imgLight, AnimationProvider.getAlphaAnimation(
					AnimationProvider.TYPE_INTERPLATOR_ACCELERATE, 300));
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_EXTRA_BUTTONS, 300);
		}
	};

	private Runnable mBottomRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startShowAnimation(
					imgDesk,
					AnimationProvider
							.getTranslateAnimation(
									AnimationProvider.TYPE_ANIMATION_BOTTOM_IN,
									3,
									AnimationProvider.TYPE_INTERPLATOR_ACCELERATE,
									1000));
			mHandler.sendEmptyMessageDelayed(MSG_SHOW_LIGHTS, 1100);
		}
	};

	private int index = 0;
	
	private Runnable mExtraBtnRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(index<mViews.length){
				startShowAnimation(mViews[index],
						AnimationProvider.getScaleAnimation(AnimationProvider.TYPE_INTERPLATOR_ACCELERATE, 200));
				mHandler.sendEmptyMessageDelayed(MSG_SHOW_EXTRA_BUTTONS, 200);
				index ++;
			}else{
				index = 0;
				mHandler.sendEmptyMessageDelayed(MSG_SHOW_MAIN_BUTTONS, 200);
			}
		}
	};

	/**
	 * 
	 */
	private Runnable mMainBtnRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startShowAnimation(AnimationProvider.getAlphaAnimation
					(AnimationProvider.TYPE_INTERPLATOR_ACCELERATE, 800), btnCreate,btnJoin);
		}
	};

	private void startShowAnimation(View view, Animation animation) {
		view.clearAnimation();
		animation.setFillAfter(true);
		view.startAnimation(animation);
	}

	private void startShowAnimation(Animation animation,View...views){
		for(View view:views){
			startShowAnimation(view, animation);
		}
	}
	
	private void startHideAnimation(final View view, Animation animation) {
		view.clearAnimation();
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				view.setEnabled(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.setVisibility(View.INVISIBLE);
			}
		});
		view.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_btn_help:
			if (null == mHelpDialog) {
				mHelpDialog = new HelpDialog(this, 500, Effectstype.Slideright);
			}
			mHelpDialog.show();
			break;
		case R.id.main_btn_transfer:
			if (null == mTransferDialog) {
				mTransferDialog = new TransferDialog(this, 500,
						Effectstype.Slideleft);
			}
			mTransferDialog.show();
			break;
		case R.id.main_btn_cretae:
			startAnimation();
			break;
		default:
			break;
		}
	}

	private Runnable copyRunnable = new Runnable() {

		@Override
		public void run() {
			String strDir = Environment.getExternalStorageDirectory().getPath()
					+ "/.texas";
			String strFile = strDir + "/texaspoker.apk";
			File target = new File(strFile);

			if (settingHelper.isFirstStart()
					|| (!settingHelper.isFirstStart() && !target.exists())) {
				settingHelper.setFirstStart(false);
				List<PackageInfo> packs = getPackageManager()
						.getInstalledPackages(0);
				String path = "";
				for (PackageInfo info : packs) {
					if (info.applicationInfo.packageName
							.contains("com.texas.poker")) {
						path = info.applicationInfo.publicSourceDir;
						break;
					}
				}
				File file = new File(path);
				new File(strDir).mkdirs();
				if (file.exists() && !target.exists()) {
					copyFile(file.getPath(), target.getPath());
				}
			}
		}
	};

	@SuppressWarnings("resource")
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			settingHelper.setCopied(true);
		} catch (Exception e) {
			System.out.println("copy single file faliure");
			e.printStackTrace();
			settingHelper.setCopied(false);
		}

	}
}
