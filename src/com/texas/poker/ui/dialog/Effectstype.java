package com.texas.poker.ui.dialog;

import com.texas.poker.ui.dialog.effects.BaseEffects;
import com.texas.poker.ui.dialog.effects.FadeIn;
import com.texas.poker.ui.dialog.effects.FlipH;
import com.texas.poker.ui.dialog.effects.FlipV;
import com.texas.poker.ui.dialog.effects.NewsPaper;
import com.texas.poker.ui.dialog.effects.SideFall;
import com.texas.poker.ui.dialog.effects.SlideLeft;
import com.texas.poker.ui.dialog.effects.SlideRight;
import com.texas.poker.ui.dialog.effects.SlideTop;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(com.texas.poker.ui.dialog.effects.SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(com.texas.poker.ui.dialog.effects.Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(com.texas.poker.ui.dialog.effects.RotateBottom.class),
    RotateLeft(com.texas.poker.ui.dialog.effects.RotateLeft.class),
    Slit(com.texas.poker.ui.dialog.effects.Slit.class),
    Shake(com.texas.poker.ui.dialog.effects.Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
