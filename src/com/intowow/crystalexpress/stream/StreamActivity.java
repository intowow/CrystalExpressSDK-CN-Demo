package com.intowow.crystalexpress.stream;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.CrystalExpressListView;
import com.intowow.sdk.I2WAPI;

/**
 * 
 * this sample tell you how to integrate the stream ad with common list view.
 * but if your list view uses the PullToRefresh library,
 * then you can refer to the CEStreamActivity or PullToRefreshStreamActivity 
 * 
 * */
public class StreamActivity extends Activity {//XXX#Stream-StreamActivity#
	
	//******************************************
	//	common UI
	//
	private final static int ITEM_SIZE = 200;
	private List<Object> mItems = new ArrayList<Object>(ITEM_SIZE);
	private int[] mPids = new int[5];
	
	//*******************************************
	//	stream ad
	//
	//XXX@Stream-init@#Stream-init#
	private final static String  mPlacement = "STREAM";
	private CrystalExpressListView mListView = null;
	private StreamAdapter mAdapter = null;
	//end
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//**********************************
		//	common UI
		//
		setContentView(R.layout.activity_stream);

		LayoutManager lm = LayoutManager.getInstance(this);
		View title = findViewById(R.id.title);
		title.getLayoutParams().height = lm.getMetric(LayoutID.STREAM_TITLE_HEIGHT);
		
		for(int i=0; i<ITEM_SIZE; i++) {
			mItems.add(new Object());
		}
		
		mPids[0]=R.drawable.business_1;
		mPids[1]=R.drawable.business_2;
		mPids[2]=R.drawable.business_3;
		mPids[3]=R.drawable.business_4;
		mPids[4]=R.drawable.business_5;
		
		
		//***********************************
		//	stream ad
		//
		
		//	adapter
		//
		mAdapter = new StreamAdapter(this, 
				mPlacement, 
				mItems, 
				mPids,
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_LEFT_RIGHT),
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_TOP_BUTTOM),
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_HEIGHT));
		
		//XXX@Stream-active@#Stream-active#
		//	let the SDK know that this placement is active now
		//
		mAdapter.setActive();
		//end
		
		//	ListView
		//
		//XXX@Stream-ListView@#Stream-ListView#
		mListView = (CrystalExpressListView)findViewById(R.id.listView);
		mListView.setAdapter(mAdapter);
		//end
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//	you can call this API only once in your launch flow
		//
		I2WAPI.init(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		I2WAPI.onActivityResume(this);
		//XXX@Stream-onResume@#Stream-onResume#
		if(mAdapter != null) {
			mAdapter.onResume();
		}
		//end
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		I2WAPI.onActivityPause(this);
		//XXX@Stream-onPause@#Stream-onPause#
		if(mAdapter != null) {
			mAdapter.onPause();
		}
		//end
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//XXX@Stream-onDestroy@#Stream-onDestroy#
		if(mAdapter != null) {
			mAdapter.release();
			mAdapter = null;
		}
		//end
	}

}
