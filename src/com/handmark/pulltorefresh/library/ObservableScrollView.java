package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView{
	
	private final static int CHECK_SCROLL_STATUS_INTERVAL = 200;

    public interface ScrollViewListener {
        public void onScrollChanged(ObservableScrollView observableScrollView,int x, int y, int oldX, int oldY);
        public void onScrollViewIdle();
    }
    
    protected ScrollViewListener mScrollViewListener = null;
    private Runnable mScrollingRunnable = null;
    private boolean mIsScrolling = false;
    private boolean mIsTouching = false;
    
	public ObservableScrollView(Context context) {
		super(context);
	}
	
	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setScrollViewListener(ScrollViewListener scrollViewListener){
		mScrollViewListener = scrollViewListener;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            mIsTouching = true;
            mIsScrolling = true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (mIsTouching && !mIsScrolling) {
                if (mScrollViewListener != null) {
                	mScrollViewListener.onScrollViewIdle();
                }
            }

            mIsTouching = false;
        }

        return super.onTouchEvent(ev);
    }
	
	@Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        
        if (Math.abs(oldX - x) > 0) {
            if (mScrollingRunnable != null) {
                removeCallbacks(mScrollingRunnable);
            }

            mScrollingRunnable = new Runnable() {
                public void run() {
                    if (mIsScrolling && !mIsTouching) {
                    	if (mScrollViewListener != null) {
                        	mScrollViewListener.onScrollViewIdle();
                        }
                    }

                    mIsScrolling = false;
                    mScrollingRunnable = null;
                }
            };

            postDelayed(mScrollingRunnable, CHECK_SCROLL_STATUS_INTERVAL);
        }

        if (mScrollViewListener != null) {
        	mScrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

}
