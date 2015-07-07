package com.intowow.crystalexpress.stream;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.R;
import com.intowow.sdk.I2WAPI;

public class PullToRefreshStreamActivity extends Activity {//XXX#Stream-PullToRefreshStreamActivity#
	
	//*********************************************
	//	common UI
	//
	private final static int ITEM_SIZE = 10;
	private final static int REFRESH_ITEM_SIZE = 20;
	private List<Object> mItems = new ArrayList<Object>(ITEM_SIZE);
	private int[] mPids = new int[5];
	
	//*********************************************
	//	stream ad
	//
	private final static String  mPlacement = "STREAM";
	//XXX@Stream-pullinit@#Stream-pullinit#
	private final static int FIRST_VISIBLE_ITEM_OFFSET = -1;
	//end
	private PullToRefreshListView mPullToRefreshListView = null;
	private StreamAdapter mAdapter = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//*********************************************
		//	common UI
		//
		setContentView(R.layout.activity_pull_to_refresh_stream);
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
		
		//*********************************************
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
		
		//	let the SDK know that this placement is active now
		//
		mAdapter.setActive();
		
		//	ListView
		//
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.listView);
		mPullToRefreshListView
		.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		
		mPullToRefreshListView.setAdapter(mAdapter);
		
		/**
		 * 
		 * important !
		 * if you use PullToRefreshListView,
		 * the position which callback by the listener will be shifted,
		 * so you should additionally check OnItemClickListener and OnScrollListener
		 * 
		 * */
		//XXX@Stream-onItemClickListener@#Stream-onItemClickListener#
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				position = position + FIRST_VISIBLE_ITEM_OFFSET;
				//end
				
				Toast.makeText(getApplicationContext(), 
						String.format("you click %d ", position), 
						Toast.LENGTH_SHORT).show();
				
				if(mAdapter != null && mAdapter.isAd(position)) {
					return;
				}
				
				//	...
				//	if you have already implemented this listener,
				//	add your original code here 
				//	...
				
			}});
		//end
		
		//XXX@Stream-onScroll@#Stream-onScroll#
		mPullToRefreshListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//	...
				//	if you have already implemented this listener,
				//	add your original code here 
				//	...
				
				if(mAdapter != null) {
					mAdapter.onScrollStateChanged(view, scrollState);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//	...
				//	if you have already implemented this listener,
				//	add your original code here 
				//	...
				
				if(mAdapter != null) {
					//	pass the right position on to the SDK
					//
					mAdapter.onScroll(view, 
							firstVisibleItem + FIRST_VISIBLE_ITEM_OFFSET, 
							visibleItemCount + FIRST_VISIBLE_ITEM_OFFSET, 
							totalItemCount);
					//end
				}
			}});
		//end
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		I2WAPI.init(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		I2WAPI.onActivityResume(this);
		if(mAdapter != null) {
			mAdapter.onResume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		I2WAPI.onActivityPause(this);
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
	
	private class GetDataTask extends AsyncTask<Void, Void, List<Object>> {

		@Override
		protected List<Object> doInBackground(Void... params) {
			// Simulates a background job.
			return mItems;
		}

		@Override
		protected void onPostExecute(List<Object> result) {
			
			List<Object> newItems = new ArrayList<Object>(REFRESH_ITEM_SIZE);
			
			for(int i=0; i<REFRESH_ITEM_SIZE; i++) {
				newItems.add(new Object());
			}
			
			mAdapter.setList(newItems);
			
			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
