package com.intowow.crystalexpress.stream.fixposition;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.intowow.crystalexpress.BaseActivity;
import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.MainActivity;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;

/**
 * to let the SDK know the App status. (foreground or background)
 * you can let your activity extend BaseActivity simply.
 * */
public class SingleFixPositionAdapterActivity extends BaseActivity {//TODO extends BaseActivity
	
	//******************************************
	//	common UI
	//
	private final static int ITEM_SIZE = 200;
	private ListView mListView = null;
	private List<Object> mItems = new ArrayList<Object>(ITEM_SIZE);
	
	//*******************************************
	//	stream ad
	//
	/**
	 *	you can hardcode this tag name value in the source code, 
	 *	or replace it by calling your server API 	
	**/
	private final static String  mTagName = Config.STREAM_TAG_NAME;
	private ExtendFixPositionStreamAdapter mAdapter = null;
	
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
		mListView = (ListView)findViewById(R.id.listView);
		for(int i=0; i<ITEM_SIZE; i++) {
			mItems.add(new Object());
		}
		
		//***********************************
		//	stream ad
		//
		//XXX@Stream-fix-init@#Stream-fix-init#
		mAdapter = new ExtendFixPositionStreamAdapter(
				this, 
				mTagName, 
				mItems);
		//end
		//	let the SDK know that this tag is active now
		//
		mAdapter.setActive();
		
		//	ListView
		//
		//	let the SDK know the scroll status
		//
		mListView.setOnScrollListener(mAdapter);
		mListView.setAdapter(mAdapter);
		
		/*
		//	if you have not implemented setOnItemClickListener,
		//	skip this code
		//
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//	check is this position is a ad first
				//
				if(mAdapter != null && mAdapter.isAd(position)) {
					return;
				}
				
				//	...
				//	then add your original code here
				//	...
				
			}
			
		});
		*/
		
		/**
		 *  if you have already implemented OnScrollListener
		 *  you can use follows
		 *  
		mListView.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// ...
						// if you have already implemented this listener,
						// add your original code here
						// ...

						if (mAdapter != null) {
							mAdapter.onScrollStateChanged(view,
									scrollState);
						}
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// ...
						// if you have already implemented this listener,
						// add your original code here
						// ...

						if (mAdapter != null) {
							mAdapter.onScroll(
									view,
									firstVisibleItem,
									visibleItemCount,
									totalItemCount);
						}
					}
				});
		*/
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(mAdapter != null) {
			mAdapter.onResume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(mAdapter != null) {
			mAdapter.onPause();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(mAdapter != null) {
			mAdapter.release();
			mAdapter = null;
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
