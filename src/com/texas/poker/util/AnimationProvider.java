
package com.texas.poker.util;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
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
	
	public final static int TYPE_ANIMATION_TOP = 1;
	
	public final static int TYPE_ANIMATION_LEFT = 2;
	
	public final static int TYPE_ANIMATION_RIGHT = 3;
	
	public final static int TYPE_ANIMATION_BOTTOM =4;
	
	public final static int TYPE = Animation.RELATIVE_TO_SELF;
	
	public static TranslateAnimation getTranslateAnimation(int directionType,
			int multiple,int interplatorType,int duration,boolean isIn){
		
		TranslateAnimation ta = null;
		
		float mFromX =0f,mFromY =0f,mToX= 0f,mToY =0f;
		
		switch(directionType){
		case TYPE_ANIMATION_TOP:
			mFromY = isIn?-multiple:multiple;
			break;
		case TYPE_ANIMATION_LEFT:
			mFromX = isIn?-multiple:multiple;
			break;
		case TYPE_ANIMATION_RIGHT:
			mFromX = isIn?multiple:-multiple;
			break;
		case TYPE_ANIMATION_BOTTOM:
			mFromY = isIn?multiple:-multiple;
			break;
		default:
			break;
		}
		ta = new TranslateAnimation
				(TYPE, mFromX, TYPE, mToX, TYPE, mFromY, TYPE, mToY);
		ta.setInterpolator(getInterpolator(interplatorType));
		ta.setDuration(duration);
		return ta;
	} 
	
	
	public static AlphaAnimation getAlphaAnimation(int interplatorType,int duration){
		return getAlphaAnimation(interplatorType,duration,0.0f,1.0f);
	}
	
	public static AlphaAnimation getAlphaAnimation(int interplatorType,int duration,float from,float to){
		AlphaAnimation aa = new AlphaAnimation(from, to);
		aa.setInterpolator(getInterpolator(interplatorType));
		aa.setDuration(duration);
		return aa;
	}
	
	public static ScaleAnimation getScaleAnimation(int interplatorType,int duration){
		ScaleAnimation sa = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,TYPE,0.5f,TYPE,0.5f);
		sa.setInterpolator(getInterpolator(interplatorType));
		sa.setDuration(duration);
		return sa;
	}
	
	public static AnimationSet getExpandAnimation(int interplatorType,int duration){
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,TYPE,0.5f,TYPE,0.2f);
		sa.setInterpolator(getInterpolator(interplatorType));
		sa.setDuration(duration);
		set.addAnimation(sa);
		return set;
	}
	
	public static AnimationSet getShrankAnimation(int interplatorType,int duration){
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f,TYPE,0.5f,TYPE,0.2f);
		sa.setInterpolator(getInterpolator(interplatorType));
		sa.setDuration(duration);
		set.addAnimation(sa);
		return set;
	}
	
	public static Interpolator getInterpolator(int interplatorType){
		Interpolator interpolator  =null;
		switch(interplatorType){
			case TYPE_INTERPLATOR_ACCELERATE_DECELERATE:
				interpolator = new AccelerateDecelerateInterpolator();
				break;
			case TYPE_INTERPLATOR_ACCELERATE:
				interpolator = new AccelerateInterpolator();
				break;
			default:
				interpolator = new LinearInterpolator();
				break;	
		}
		return interpolator;
	}
}


