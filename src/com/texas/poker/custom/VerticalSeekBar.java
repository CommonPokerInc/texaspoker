/*package com.example.seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsSeekBar;


public class VerticalSeekBar extends AbsSeekBar {
    private Drawable mThumb;
    private int height;
    private int width;
    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser);
        void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar);
        void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    
    public VerticalSeekBar(Context context) {
        this(context, null);
    }
    
    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }
    
    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }
    
    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }
    
    
    void onProgressRefresh(float scale, boolean fromUser) {
        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (mOnSeekBarChangeListener != null) {
        	mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
        }
    }

        private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
            int available = w+getPaddingLeft()-getPaddingRight();
            int thumbWidth = thumb.getIntrinsicWidth();
            int thumbHeight = thumb.getIntrinsicHeight();
            available -= thumbWidth;
            // The extra space for the thumb to move on the track
            available += getThumbOffset() * 2;
            int thumbPos = (int) (scale * available);
            int topBound, bottomBound;
            if (gap == Integer.MIN_VALUE) {
                Rect oldBounds = thumb.getBounds();
                topBound = oldBounds.top;
                bottomBound = oldBounds.bottom;
            } else {
                topBound = gap;
                bottomBound = gap + thumbHeight;
            }
            thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
        }
        protected void onDraw(Canvas c)
        {
                c.rotate(-90);
                c.translate(-height,0);
                super.onDraw(c);
        }
        protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
        		width = 4;
      		    height = 400;
//                height = View.MeasureSpec.getSize(heightMeasureSpec);
//                width = View.MeasureSpec.getSize(widthMeasureSpec);
                this.setMeasuredDimension(width, height);


        }
	@Override
	public void setThumb(Drawable thumb)
	{
        mThumb = thumb;
		super.setThumb(thumb);
	}
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
            super.onSizeChanged(h, w, oldw, oldh);
    }   
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
                
            case MotionEvent.ACTION_MOVE:
                trackTouchEvent(event);
                attemptClaimDrag();
                break;
                
            case MotionEvent.ACTION_UP:
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                break;
                
            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                setPressed(false);
                break;
        }
        return true;
    }
    private void trackTouchEvent(MotionEvent event) {
        final int Height = getHeight();
        final int available = Height - getPaddingBottom() - getPaddingTop();
        int Y = (int)event.getY();
        float scale;
        float progress = 0;
        if (Y > Height - getPaddingBottom()) {
            scale = 0.0f;
        } else if (Y  < getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = (float)(Height - getPaddingBottom()-Y) / (float)available;
        }
        
        final int max = getMax();
        progress = scale * max;
        
        setProgress((int) progress);
    }
    
    private void attemptClaimDrag() {
        if (getParent() != null) {
        	getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if(event.getAction()==KeyEvent.ACTION_DOWN)
    	{
    		KeyEvent newEvent = null;
    		switch(event.getKeyCode())
    		{
    			case KeyEvent.KEYCODE_DPAD_UP:
    				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_RIGHT);
    				break;
    			case KeyEvent.KEYCODE_DPAD_DOWN:
    				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_LEFT);
    				break;
    			case KeyEvent.KEYCODE_DPAD_LEFT:
    				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_DOWN);
    				break;
    			case KeyEvent.KEYCODE_DPAD_RIGHT:
    				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DPAD_UP);
    				break;
    			default:
    				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,event.getKeyCode());
					break;
    		}
    		return newEvent.dispatch(this);
    	}
    	return false;
     	}
}

*/

package com.texas.poker.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar
{
    private boolean mIsDragging;
    private float mTouchDownY;
    private int mScaledTouchSlop;
    private boolean isInScrollingContainer = false;

    public boolean isInScrollingContainer()
    {
        return isInScrollingContainer;
    }

    public void setInScrollingContainer(boolean isInScrollingContainer)
    {
        this.isInScrollingContainer = isInScrollingContainer;
    }

    /**
     * On touch, this offset plus the scaled value from the position of the
     * touch will form the progress value. Usually 0.
     */
    float mTouchProgressOffset;

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public VerticalSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context)
    {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        super.onSizeChanged(h, w, oldh, oldw);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
            int heightMeasureSpec)
    {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        canvas.rotate(-90);
        canvas.translate(-getHeight(), 0);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isEnabled())
        {
            return false;
        }

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer())
                {

                    mTouchDownY = event.getY();
                }
                else
                {
                    setPressed(true);

                    invalidate();
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();

                    onSizeChanged(getWidth(), getHeight(), 0, 0);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging)
                {
                    trackTouchEvent(event);

                }
                else
                {
                    final float y = event.getY();
                    if (Math.abs(y - mTouchDownY) > mScaledTouchSlop)
                    {
                        setPressed(true);

                        invalidate();
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();

                    }
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging)
                {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);

                }
                else
                {
                    // Touch up when we never crossed the touch slop threshold
                    // should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();

                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;
        }
        return true;

    }

    private void trackTouchEvent(MotionEvent event)
    {
        final int height = getHeight();
        final int top = getPaddingTop();
        final int bottom = getPaddingBottom();
        final int available = height - top - bottom;

        int y = (int) event.getY();

        float scale;
        float progress = 0;

        // ��������Сֵ
        if (y > height - bottom)
        {
            scale = 0.0f;
        }
        else if (y < top)
        {
            scale = 1.0f;
        }
        else
        {
            scale = (float) (available - y + top) / (float) available;
            progress = mTouchProgressOffset;
        }

        final int max = getMax();
        progress += scale * max;

        setProgress((int) progress);

    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch()
    {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch()
    {
        mIsDragging = false;
    }

    private void attemptClaimDrag()
    {
        ViewParent p = getParent();
        if (p != null)
        {
            p.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public synchronized void setProgress(int progress)
    {

        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);

    }

}