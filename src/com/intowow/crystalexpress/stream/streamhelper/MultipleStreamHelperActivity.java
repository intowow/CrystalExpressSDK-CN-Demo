package com.intowow.crystalexpress.stream.streamhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.intowow.crystalexpress.Config;
import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.crystalexpress.MainActivity;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.cedemo.BaseActivity;
import com.intowow.sdk.StreamHelper;

public class MultipleStreamHelperActivity extends BaseActivity{
	
	//***********************************************
	//	common UI
	//
	private final static int ITEM_SIZE = 200;
	private List<List<Object>> mItems = new ArrayList<List<Object>>(5);
	private RelativeLayout mTitleLayout = null;
	private BreadcrumbView mBreadcrumbView = null;
	private ArrayList<String> mSections;
	private ViewPager mPager;
	private SectionPagerAdapter mPagerAdapter;
	private int mTitleHeight = 0;
	private int mActiveIndex = -1;
	private int mDeferIndex = 0;
	private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	private Handler mHandler = null;
	
	public interface PagerEventListener {
		public void onPageChanged(int pos);
	}
	
	//***********************************************
	//	stream ad 
	//
	/**you can setup placements for your section from the server*/
	private final static String[] mPlacements = Config.STREAM_PLACEMENTS;
	
	private SparseArray<StreamHelper> mStreamHelpers = new SparseArray<StreamHelper>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(createContentView());
		
		mHandler = new Handler();
	}
	
	private View createContentView() {
		LayoutManager lm = LayoutManager.getInstance(this);

		// list view simulate data
		//
		List<Object> items = null;
		for(int i = 0 ; i < mPlacements.length ; i++) {
			items = new ArrayList<Object>(ITEM_SIZE);
			for (int j = 0; j< ITEM_SIZE; j++) {
				items.add(new Object());
			}
			mItems.add(items);
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

		// menu
		//
		mSections = new ArrayList<String>();
		mSections.add("menuA");
		mSections.add("menuB");
		mSections.add("menuC");
		mSections.add("menuD");
		mSections.add("menuE");

		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				lm.getMetric(LayoutID.STREAM_TITLE_HEIGHT));
		params.addRule(RelativeLayout.BELOW, 2000);
		mBreadcrumbView = new BreadcrumbView(this);
		mBreadcrumbView.setId(1002);
		mBreadcrumbView.setLayoutParams(params);
		mBreadcrumbView.setSectionList(mSections);

		// menu bottom line
		//
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				lm.getMetric(LayoutID.STREAM_MENU_BOTTOM_LINE_HEIGHT));
		params.addRule(RelativeLayout.BELOW, 1002);
		final View menuBottomLine = new View(this);
		menuBottomLine.setBackgroundColor(Color.parseColor("#d2d2d2"));
		menuBottomLine.setLayoutParams(params);
		menuBottomLine.setId(5000);

		// news list
		//
		mPagerAdapter = new SectionPagerAdapter(this, mSections);

		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.BELOW, 5000);

		mPager = new ViewPager(this);
		mPager.setLayoutParams(params);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				mScrollState = state;

				if (state == OnScrollListener.SCROLL_STATE_IDLE) {
					if (mActiveIndex != mDeferIndex) {

						// you can set the right adapter to active
						// after the status is idle
						//
						performDeferUpdate();
					}
				}

			}

			@Override
			public void onPageScrolled(int pos, float positionOffset,
					int positionOffsetPixels) {
				mBreadcrumbView.select(pos + positionOffset);
			}

			@Override
			public void onPageSelected(final int pos) {
				mDeferIndex = pos;

				if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE) {

					// you can set the right adapter to active
					// after the status is idle
					//
					performDeferUpdate();
				}
			}
		});

		contentView.addView(mTitleLayout);
		contentView.addView(mBreadcrumbView);
		contentView.addView(menuBottomLine);
		contentView.addView(mPager);

		mBreadcrumbView.setOnSectionSelecteListener(new PagerEventListener() {
			@Override
			public void onPageChanged(int pos) {
				mPager.setCurrentItem(pos);
			}
		});

		final int ACTIVE_INDEX = 0;
		mBreadcrumbView.select(ACTIVE_INDEX);
		mPager.setCurrentItem(ACTIVE_INDEX);
		mPagerAdapter.refreshAd(ACTIVE_INDEX);

		return contentView;
	}

	private void performDeferUpdate() {
		mActiveIndex = mDeferIndex;
		mPagerAdapter.refreshAd(mActiveIndex);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (mPagerAdapter != null) {
			mPagerAdapter.resumeAd();
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mPagerAdapter != null) {
			mPagerAdapter.stopAd();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mPagerAdapter != null) {
			mPagerAdapter.release();
			mPagerAdapter = null;
		}
		
		if (mPager != null) {
			mPager.setOnPageChangeListener(null);
			mPager = null;
		}
	}

	class BreadcrumbView extends LinearLayout implements OnClickListener {
		static private final int ID_TEXTBOX = 1001;

		int mSectionWidth;
		int mScreenWidth;
		int mCenter;

		Context mContext;
		int mSectionPadding;

		View v;
		HorizontalScrollView mController;
		LinearLayout mTextBox;
		View mIndicator;

		PagerEventListener mOnSectionSelectListener;

		LayoutManager mlm;

		public BreadcrumbView(Context context) {
			super(context);

			mlm = LayoutManager.getInstance(MultipleStreamHelperActivity.this);

			setBackgroundColor(Color.WHITE);
			mContext = context;

			DisplayMetrics metrics = mContext.getResources()
					.getDisplayMetrics();
			mScreenWidth = metrics.widthPixels;
			mSectionWidth = metrics.densityDpi / 2;
			mSectionPadding = (int) (8 * metrics.density);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					mlm.getMetric(LayoutID.STREAM_TITLE_HEIGHT));
			mController = new HorizontalScrollView(mContext);
			mController.setHorizontalScrollBarEnabled(false);
			mController.setLayoutParams(params);

			params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			RelativeLayout outerWrapper = new RelativeLayout(mContext);
			outerWrapper.setLayoutParams(params);

			RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					mlm.getMetric(LayoutID.STREAM_INDICATOR_HEIGHT));
			rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			mIndicator = new View(mContext);
			mIndicator.setLayoutParams(rParams);
			mIndicator.setBackgroundColor(Color.parseColor("#ea5a31"));

			rParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			mTextBox = new LinearLayout(mContext);
			mTextBox.setId(ID_TEXTBOX);
			mTextBox.setOrientation(LinearLayout.HORIZONTAL);
			mTextBox.setLayoutParams(rParams);

			outerWrapper.addView(mTextBox);
			outerWrapper.addView(mIndicator);

			mController.addView(outerWrapper);

			addView(mController);
		}

		public void setColor(int bg, int highlight) {
			mController.setBackgroundColor(bg);
			mIndicator.setBackgroundColor(highlight);
		}

		public void setSectionList(ArrayList<String> sections) {
			if (sections.size() == 0) {
				return;
			}

			if (mScreenWidth / mSectionWidth > sections.size()) {
				mSectionWidth = mScreenWidth / sections.size();
			}
			mCenter = (mScreenWidth - mSectionWidth) / 2;

			LayoutParams lp = new LayoutParams(mSectionWidth,
					LayoutParams.WRAP_CONTENT);

			for (int i = 0; i < sections.size(); i++) {
				TextView tv = new TextView(mContext);
				tv.setText(sections.get(i));
				tv.setPadding(0, mSectionPadding, 0, mSectionPadding);
				tv.setGravity(Gravity.CENTER);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setTextColor(Color.parseColor("#ea5a31"));
				tv.setTag(i);
				tv.setOnClickListener(this);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						mlm.getMetric(LayoutID.STREAM_MENU_TEXT_SIZE));

				mTextBox.addView(tv, lp);
			}

			ViewGroup.LayoutParams indicatorLp = (ViewGroup.LayoutParams) mIndicator
					.getLayoutParams();
			indicatorLp.width = mSectionWidth;
		}

		public void select(float position) {
			final int indicatorPos = (int) (mSectionWidth * position);

			// Move indicator
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndicator
					.getLayoutParams();
			lp.setMargins(indicatorPos, 0, 0, 0);
			mIndicator.setLayoutParams(lp);

			// reset text color
			//
			for (int i = 0; i < mTextBox.getChildCount(); i++) {
				TextView tv = (TextView) mTextBox.getChildAt(i);
				if (position != i) {
					tv.setTextColor(Color.parseColor("#b4b4b4"));
				} else {
					tv.setTextColor(Color.parseColor("#ea5a31"));
				}
			}

			// Move scroller
			mController.post(new Runnable() {
				public void run() {
					if (indicatorPos > mCenter) {
						mController.scrollTo(indicatorPos - mCenter, 0);
					} else {
						mController.scrollTo(0, 0);
					}
				}
			});
		}

		@Override
		public void onClick(View v) {
			select((Integer) v.getTag());
			if (mOnSectionSelectListener != null) {
				mOnSectionSelectListener.onPageChanged((Integer) v.getTag());
			}
		}

		public void setOnSectionSelecteListener(
				final PagerEventListener listener) {
			mOnSectionSelectListener = listener;
		}
	}

	class SectionPagerAdapter extends PagerAdapter {
		ArrayList<String> mSections;
		HashMap<Integer, RelativeLayout> mCanvas;
		SparseArray<StreamHelperAdapter> mAdapters;

		Context mContext;
		int mAdPos = 0;

		@SuppressLint("UseSparseArrays")
		public SectionPagerAdapter(final Context c, ArrayList<String> sections) {
			mContext = c;
			mSections = sections;
			mCanvas = new HashMap<Integer, RelativeLayout>();
			mAdapters = new SparseArray<StreamHelperAdapter>();
		}

		public void release() {
			mContext = null;
			mCanvas = null;
			mAdapters = null;
		}

		public void refreshAd(int position) {
			//XXX@Stream-StreamHelper-active@#Stream-StreamHelper-active#
			if (mStreamHelpers.get(position) != null) {
				mStreamHelpers.get(position).setActive();
			}
			//end

			mAdPos = position;
		}

		public void stopAd() {
			if (mStreamHelpers != null) {
				for (int i = 0 ; i < mStreamHelpers.size(); ++i) {
					mStreamHelpers.valueAt(i).onPause();				
				}
			}
		}

		public void resumeAd() {
			if (mStreamHelpers != null) {
				
				for (int i = 0 ; i < mStreamHelpers.size(); ++i) {
					mStreamHelpers.valueAt(i).onResume();				
				}
			}
		}

		@Override
		public int getCount() {
			return mSections.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public Object instantiateItem(View collection, final int position) {
			RelativeLayout canvas = null;
			if (mCanvas.containsKey(position)) {
				canvas = mCanvas.get(position);
			} else {

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);

				canvas = new RelativeLayout(mContext);
				canvas.setLayoutParams(params);
				mCanvas.put(position, canvas);

				//	XXX
				final StreamHelper helper = new StreamHelper(MultipleStreamHelperActivity.this, mPlacements[position]);

				mStreamHelpers.put(position, helper);
				
				final List<Object> items = mItems.get(position);
				// adapter
				//
				final StreamHelperAdapter adapter = new StreamHelperAdapter(
						MultipleStreamHelperActivity.this,
						helper,
						items);

				//	set heler listener
				//
				helper.setListener(new StreamHelper.ADListener() {
					
					@Override
					public int onADLoaded(int position) {
						// 	when the SDK load one stream ad,
						//	it will call this callback for getting 
						//	the position you add in the DataSet.
						//
						//	if you call getAD() in the getView(),
						//	the SDK will return the ad refer to this position.
						//
						//	if you return "-1", it means that the ad is not added in your DataSet
						//
						position = getDefaultMinPosition(position);
						
						if (items != null && items.size() >  position) {				
							// just allocate one position for stream ad
							//
							items.add(position, null);
							adapter.notifyDataSetChanged();
							return position;
						}
						else {				
							return -1;
						}
					}
				});
				
				// put the adapter
				//
				mAdapters.put(position, adapter);

				// ListView
				//
				RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				final PullToRefreshListView pullToRefreshListView = new PullToRefreshListView(
						MultipleStreamHelperActivity.this);
				pullToRefreshListView.setBackgroundColor(Color
						.parseColor("#e7e7e7"));
				pullToRefreshListView.setLayoutParams(rParams);
				ListView inner = pullToRefreshListView.getRefreshableView();
				inner.setDivider(null);
				inner.setDividerHeight(0);
				
				pullToRefreshListView
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
								//
								if(mHandler != null) {
									mHandler.postDelayed(
											new RefreshList(pullToRefreshListView), 
											2000);
								}
							}
						});

				pullToRefreshListView.setAdapter(adapter);

				/**
				 * 
				 * important ! if you use PullToRefreshListView, the position
				 * which callback by the listener will be shifted, so you should
				 * additionally check OnItemClickListener and OnScrollListener
				 * 
				 * */
				//	if you use PullToRefresh library, 
				//	then you should check the position offset 
				//	in the scroll listener and item click listener
				//
				pullToRefreshListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								
								final int FIRST_VISIBLE_ITEM_OFFSET = -1;
								position = position + FIRST_VISIBLE_ITEM_OFFSET;

								//	you should check is this position is ad first
								//	then do your original logic later
								//
								if (helper != null && helper.isAd(position)) {
									return;
								}

								// ...
								// if you have already implemented this listener,
								// add your original code here
								// ...

							}
						});

				pullToRefreshListView
						.setOnScrollListener(new OnScrollListener() {

							@Override
							public void onScrollStateChanged(AbsListView view,
									int scrollState) {
								// ...
								// if you have already implemented this listener,
								// add your original code here
								// ...

								if (helper != null) {
									helper.onScrollStateChanged(view,
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

								if (helper != null) {
									// pass the right position on to the SDK
									//
									final int FIRST_VISIBLE_ITEM_OFFSET = -1;
									
									helper.onScroll(
											view,
											firstVisibleItem
													+ FIRST_VISIBLE_ITEM_OFFSET,
											visibleItemCount
													+ FIRST_VISIBLE_ITEM_OFFSET,
											totalItemCount);
								}
							}
						});

				canvas.addView(pullToRefreshListView);
			}

			((ViewPager) collection).addView(canvas);

			if (mAdPos == position) {
				// let the SDK know that this placement is active now
				//
				refreshAd(mAdPos);
			}

			return canvas;
		}

		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
			view = null;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	public int getDefaultMinPosition(int position) {
		// Don't place ad at the first place
		return Math.max(1, position);
	}
	
	class RefreshList implements Runnable{
		private PullToRefreshListView mListView = null;
		
		public RefreshList(PullToRefreshListView listView) {
			mListView = listView;
		}
		
		@Override
		public void run() {
			if(mListView != null) {
				mListView.onRefreshComplete();
			}
		}
	}
}
