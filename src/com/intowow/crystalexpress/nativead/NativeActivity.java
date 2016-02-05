package com.intowow.crystalexpress.nativead;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.Ad;
import com.intowow.sdk.AdListener;
import com.intowow.sdk.NativeAd;
import com.intowow.sdk.NativeAd.Image;
import com.intowow.sdk.NativeAd.MediaView;

@SuppressLint("InlinedApi")
public class NativeActivity extends BaseActivity implements AdListener {
	final static String LOG_TAG 	= "NATIVE_AD";
	final static String PLACEMENT 	= "NATIVE_AD";
	
	// UI
	Button mLoadAdBtn = null;
	Button mPlayBtn   = null;
	Button mStopBtn   = null;
	CheckBox mAutoplayCheckbox = null;
	RelativeLayout mAdView = null;
	
	// States
	boolean mAdLoaded 	= false;
	boolean mIsAutoplay	= true;
	
	NativeAd	  mNativeAd  = null;
	MediaView	  mMediaView = null;
	LayoutManager mLayoutMgr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_native);
		
		mLayoutMgr = LayoutManager.getInstance(this);
		
		setupUI();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (mMediaView != null) {
			mMediaView.stop();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (mMediaView != null && mIsAutoplay) {
			mMediaView.play();
		}
	}
	
	@Override
	protected void onDestroy() {
		if (mMediaView != null) {
			mMediaView.destroy();
			mMediaView = null;
		}
		super.onDestroy();
	}

	private void setupUI() {
		mAdView = (RelativeLayout) findViewById(R.id.adView);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAdView.getLayoutParams();
		params.height = mLayoutMgr.getMetric(LayoutID.AD_BG_HEIGHT);
		params.width = mLayoutMgr.mScreenWidth - (2*mLayoutMgr.getMetric(LayoutID.AD_CARD_MARGIN));
		params.topMargin = mLayoutMgr.getMetric(LayoutID.AD_CARD_MARGIN);
		params.bottomMargin = mLayoutMgr.getMetric(LayoutID.AD_CARD_MARGIN);
		
		mLoadAdBtn = (Button) findViewById(R.id.loadAd);
		mLoadAdBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadAd();
			}
		});
		
		mPlayBtn = (Button) findViewById(R.id.play);
		mPlayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play();
			}
		});
		
		mStopBtn = (Button) findViewById(R.id.stop);
		mStopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stop();
			}
		});
		
		mAutoplayCheckbox = (CheckBox) findViewById(R.id.autoplay);
		mAutoplayCheckbox.setChecked(mIsAutoplay);
		mAutoplayCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				mIsAutoplay = checked;
			}
		});
	}		
	
	private void loadAd() {
		Log.v(LOG_TAG, "LoadAd clicked");
		
		if (mNativeAd != null) {
			mNativeAd = null;
		}
		
		mAdView.setBackgroundColor(Color.WHITE);
		mAdView.getBackground().setAlpha(60);
		
		mAdLoaded = false;
		mNativeAd = new NativeAd(this, PLACEMENT);
		mNativeAd.setAdListener(this);
		mNativeAd.loadAd(0L);
	}
	
	private void play() {
		Log.v(LOG_TAG, "Play clicked");
		if (mMediaView != null) {
			mMediaView.play();
		}
	}
	
	private void stop() {
		Log.v(LOG_TAG, "Stop clicked");
		if (mMediaView != null) {
			mMediaView.stop();
		}
	}

	@Override
	public void onAdLoaded(Ad ad) {
		if (ad != mNativeAd) {
			return;
		}
		
		Log.v(LOG_TAG, "Ad loaded");
		mAdLoaded = true;
		initAdView();
	}
	
	@SuppressLint("InlinedApi")
	private void initAdView() {
		// Remove all children
		if (mMediaView != null) {
			mMediaView.destroy();
		}
		mAdView.removeAllViews();	
		
		RelativeLayout.LayoutParams params = null;
		
		int titleLeftMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
		
		// Icon
		Image iconImage = mNativeAd.getAdIcon();
		if (iconImage != null && iconImage.getUrl() != null) {
			params = new RelativeLayout.LayoutParams(
				mLayoutMgr.getMetric(LayoutID.AD_ICON_SIZE),
				mLayoutMgr.getMetric(LayoutID.AD_ICON_SIZE));
			
			params.topMargin  = mLayoutMgr.getMetric(LayoutID.AD_TITLE_MERGIN);
			params.leftMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
			
			ImageView icon = new ImageView(this);
			icon.setLayoutParams(params);
			mAdView.addView(icon);
		
			NativeAd.downloadAndDisplayImage(iconImage, icon);
			
			titleLeftMargin += mLayoutMgr.getMetric(LayoutID.AD_ICON_SIZE) + mLayoutMgr.getMetric(LayoutID.AD_TITLE_MERGIN);
		}
		
		// Title
		params = new RelativeLayout.LayoutParams(
			mLayoutMgr.getMetric(LayoutID.AD_TITLE_WIDTH),
			mLayoutMgr.getMetric(LayoutID.AD_TITLE_HEIGHT));
		params.topMargin = mLayoutMgr.getMetric(LayoutID.AD_TITLE_MERGIN);
		params.leftMargin = titleLeftMargin;
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		
		TextView adTitle = new TextView(this);
		adTitle.setLayoutParams(params);
		adTitle.setPadding(0, 0, 0, 0);
		adTitle.setText(mNativeAd.getAdTitle());
		adTitle.setMaxLines(1);
		adTitle.setEllipsize(TruncateAt.END);
		adTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
		adTitle.setTextColor(Color.WHITE);
		adTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mLayoutMgr.getMetric(LayoutID.AD_TITLE_TEXT_SIZE));
		mAdView.addView(adTitle);

		// Call to action
		params = new RelativeLayout.LayoutParams(
				mLayoutMgr.getMetric(LayoutID.AD_CALL_TO_ACTION_WIDTH),
				mLayoutMgr.getMetric(LayoutID.AD_CALL_TO_ACTION_HEIGHT));
		params.topMargin = mLayoutMgr.getMetric(LayoutID.AD_CALL_TO_ACTION_MARGIN_TOP);
		
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.rightMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
		
		TextView adCallToAction = new TextView(this);
		adCallToAction.setLayoutParams(params);
		adCallToAction.setMaxLines(1);
		adCallToAction.setEllipsize(TruncateAt.END);
		adCallToAction.setBackgroundResource(R.drawable.cta);
		adCallToAction.setText(mNativeAd.getAdCallToAction());
		adCallToAction.setGravity(Gravity.CENTER);
		adCallToAction.setTextColor(Color.WHITE);
		adCallToAction.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mLayoutMgr.getMetric(LayoutID.AD_CALL_TO_ACTION_TEXT_SIZE));
		mAdView.addView(adCallToAction);
		
		// Media
		params = new RelativeLayout.LayoutParams(mLayoutMgr.getMetric(LayoutID.AD_BODY_WIDTH),  RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = mLayoutMgr.getMetric(LayoutID.AD_TITLE_MERGIN) * 2 + mLayoutMgr.getMetric(LayoutID.AD_ICON_SIZE);
		params.leftMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
		params.rightMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);

		mMediaView = new MediaView(this);
		mMediaView.setLayoutParams(params);
		mMediaView.setNativeAd(mNativeAd);
		mMediaView.setAutoplay(mIsAutoplay);
		
		final int MEDIA_VIEW_ID = 100;
		mMediaView.setId(MEDIA_VIEW_ID);
		
		
		Log.d(LOG_TAG, " ad width = "+((ViewGroup)mMediaView).getChildAt(0).getLayoutParams().width);
		Log.d(LOG_TAG, " ad height = "+((ViewGroup)mMediaView).getChildAt(0).getLayoutParams().height);
		
		mAdView.addView(mMediaView);
		
		int bodyHeight = ((ViewGroup)mMediaView).getChildAt(0).getLayoutParams().height;

		// Body
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bodyHeight);
		params.addRule(RelativeLayout.BELOW, MEDIA_VIEW_ID);
		params.leftMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
		params.rightMargin = mLayoutMgr.getMetric(LayoutID.AD_MARGIN);
		
		TextView adBody = new TextView(this);
		adBody.setLayoutParams(params);
		adBody.setText(mNativeAd.getAdBody());
		adBody.setMaxLines(2);
		adBody.setEllipsize(TruncateAt.END);
		adBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
		adBody.setTextColor(Color.parseColor("#e6e6e6"));
		adBody.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mLayoutMgr.getMetric(LayoutID.AD_BODY_TEXT_SIZE));
		mAdView.addView(adBody);

		mNativeAd.registerViewForInteraction(mAdView, Arrays.asList(new View [] {adCallToAction}));
	}

	@Override
	public void onAdClicked(Ad ad) {
		if (ad != mNativeAd) {
			return;
		}
		Log.v(LOG_TAG, "Ad clicked");	
	}

	@Override
	public void onAdImpression(Ad ad) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onAdImpression : " );
	}

	@Override
	public void onAdMute(Ad ad) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onAdMute : " );
	}

	@Override
	public void onAdUnmute(Ad ad) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onAdUnmute : " );
	}

	@Override
	public void onError(Ad ad, com.intowow.sdk.AdError error) {
		if (ad != mNativeAd) {
			return;
		}
		
		Log.w(LOG_TAG, "Load ad error : " + error.toString());
	}

	@Override
	public void onVideoEnd(Ad arg0) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onVideoEnd : " );
	}

	@Override
	public void onVideoProgress(Ad arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onVideoProgress : " );
	}

	@Override
	public void onVideoStart(Ad arg0) {
		// TODO Auto-generated method stub
		Log.w(LOG_TAG, "onVideoStart : " );
	}

}