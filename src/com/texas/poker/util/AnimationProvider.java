
package com.texas.poker.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/*
 * author FrankChan
 * description AnimationProvider for MainPage
 * time 2014-9-22
 *
 */
public class AnimationProvider {
	
	public final static int TYPE_INTERPLATOR_ACCELERATE_DECELERATE =1;
	
	public final static int TYPE_INTERPLATOR_ACCELERATE =2;
	
	public final static int TYPE_ANIMATION_TOP_IN = 1;
	
	public final static int TYPE_ANIMATION_LEFT_IN = 2;
	
	public final static int TYPE_ANIMATION_RIGHT_IN = 3;
	
	public final static int TYPE_ANIMATION_BOTTOM_IN =4;
	
	public final static int TYPE = Animation.RELATIVE_TO_SELF;
	
	public static TranslateAnimation getTranslateAnimation(int directionType,
			int multiple,int interplatorType,int duration){
		
		TranslateAnimation ta = null;
		float mFromX =0f,mFromY =0f,mToX= 0f,mToY =0f;
		switch(directionType){
		case TYPE_ANIMATION_TOP_IN:
			mFromY = -multiple;
			break;
		case TYPE_ANIMATION_LEFT_IN:
			mFromX = -multiple;
			break;
		case TYPE_ANIMATION_RIGHT_IN:
			mFromX = multiple;
			break;
		case TYPE_ANIMATION_BOTTOM_IN:
			mFromY = multiple;
			break;
		default:
			break;
		}
		ta = new TranslateAnimation
				(TYPE, mFromX, TYPE, mToX, TYPE, mFromY, TYPE, mToY);
		ta.setDuration(duration);
		return ta;
	} 
	
	
	public static AlphaAnimation getAlphaAnimation(int interplatorType,int duration){
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(duration);
		return aa;
	}
}


