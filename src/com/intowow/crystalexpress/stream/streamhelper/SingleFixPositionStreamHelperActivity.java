package com.intowow.crystalexpress.stream.streamhelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.MainActivity;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.sdk.StreamHelper;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class SingleFixPositionStreamHelperActivity extends BaseActivity {//TODO extends BaseActivity

	//***********************************************
	//	common UI
	//
	private final static int ITEM_SIZE = 200;
	private List<Object> mItems = new ArrayList<Object>(ITEM_SIZE);
	private RelativeLayout mTitleLayout = null;
	private int mTitleHeight = 0;
	private StreamHelperAdapter  mStreamHelperAdapter = null;
	
	public interface PagerEventListener {
		public void onPageChanged(int pos);
	}
	
	//***********************************************
	//	stream ad 
	//	TODO
	/**you can setup tag for your channel from the App server*/
	private final static String mTagName = Config.STREAM_TAG_NAME;
	
	//	TODO
	//	setup the stream helper for your list view
	//
	private StreamHelper mStreamHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(createContentView());
	}
	
	private View createContentView() {
		LayoutManager lm = LayoutManager.getInstance(this);

		// list view simulate data
		//
		for (int j = 0; j < ITEM_SIZE; j++) {
			mItems.add(new Object());
		}
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		RelativeLayout contentView = new RelativeLayout(this);
		contentView.setLayoutParams(params);
		contentView.setBackgroundColor(Color.parseColor("#e7e7e7"));

		mTitleHeight = lm.getMetric(LayoutID.STREAM_TITLE_HEIGHT);
		// title
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mTitleHeight);
		mTitleLayout = new RelativeLayout(this);
		mTitleLayout.setId(2000);
		mTitleLayout.setLayoutParams(params);
		mTitleLayout.setBackgroundResource(R.drawable.stream_title);

		// news list
		//
		//XXX@Stream-StreamHelper-TagInit@#Stream-StreamHelper-TagInit#
		mStreamHelper = StreamHelper.getFixPositionStreamHelper(this, mTagName);
		//end
		
		mStreamHelper.setListener(new StreamHelper.ADListener() {
			
			@Override
			public int onADLoaded(int position) {
				//
				// 	when one stream ad has been loaded,
				//	the SDK will need to know which position 
				//	can show this ad in your DataSet.
				//	so the SDK will call onADLoaded(position) for getting 
				//	one position that you have already allocate in your 
				//	DataSet.
				//	then, if you call getAD(position) in the getView() 
				//	later,
				//	the SDK will return one ad or null refer to 
				//	onADLoaded's return value.
				//
				//	if you return "-1", it means that the ad is not added 
				//	in your DataSet.
				//
				
				if (mItems != null && mItems.size() >  position) {				
					// just allocate one position for stream ad
					//
					mItems.add(position, null);
					if(mStreamHelperAdapter != null) {
						mStreamHelperAdapter.notifyDataSetChanged();
					}
					return position;
				}
				else {				
					return -1;
				}
			}
		});

		mStreamHelperAdapter = new StreamHelperAdapter(
				this, 
				mStreamHelper, 
				mItems);


		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.BELOW, 2000);
		
		ListView listView = new ListView(this);
		listView.setLayoutParams(params);
		listView.setBackgroundColor(Color.parseColor("#e7e7e7"));
		listView.setDivider(null);
		listView.setDividerHeight(0);
		
		//	let the SDK know the scroll status
		//
		listView.setOnScrollListener(mStreamHelper);
		
		listView.setAdapter(mStreamHelperAdapter);
		
		//	let the SDK know that this tag is active now
		//
		mStreamHelper.setActive();


		contentView.addView(mTitleLayout);
		contentView.addView(listView);


		return contentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (mStreamHelper != null) {
			mStreamHelper.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mStreamHelper != null) {
			mStreamHelper.onPause();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mStreamHelper != null) {
			mStreamHelper.release();
			mStreamHelper = null;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		
		finish();
	}
	
}
