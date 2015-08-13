package com.intowow.crystalexpress.stream.streamhelper;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.intowow.crystalexpress.LayoutManager;
import com.intowow.crystalexpress.R;
import com.intowow.crystalexpress.LayoutManager.LayoutID;
import com.intowow.sdk.StreamHelper;

/**
 *	you can let your adapter (or parent adapter) extend the DeferStreamAdapter,
 * 	
 * */
public class StreamHelperAdapter extends BaseAdapter {

	List<Object> mList;
	Activity mContext;
	LayoutInflater mInflater;
	int[] mPics = null;
	SparseArray<LinearLayout> adViewList = new SparseArray<LinearLayout>();
	int mStreamListItemPaddingTopButtom = 0;
	int mStreamListItemPaddingLeftRight = 0;
	int mStreamListItemHeight = 0;
	
	
	//	TODO
	//	stream helper
	private StreamHelper mStreamHelper = null;
	
	public StreamHelperAdapter(Activity c, 
			StreamHelper streamHelper, 
			List<Object> list){
		
		//	TODO
		//
		mStreamHelper = streamHelper;
		
		mContext = c;
		mInflater = LayoutInflater.from(c);
		mList = list;
		mPics = new int[5];
		mPics[0]=R.drawable.business_1;
		mPics[1]=R.drawable.business_2;
		mPics[2]=R.drawable.business_3;
		mPics[3]=R.drawable.business_4;
		mPics[4]=R.drawable.business_5;
		
		LayoutManager lm = LayoutManager.getInstance(mContext);
		
		mStreamListItemPaddingLeftRight = 
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_LEFT_RIGHT);
		mStreamListItemPaddingTopButtom = 
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_PADDING_TOP_BUTTOM);
		mStreamListItemHeight = 
				lm.getMetric(LayoutID.STREAM_LIST_ITEM_HEIGHT);
	}
	
	//XXX@Stream-StreamHelper-notifyDataSetChanged@#Stream-StreamHelper-notifyDataSetChanged#
	@Override
	public void notifyDataSetChanged() {
		
		//	TODO
		//	if your DataSet has been changed
		//	the SDK will need to re-allocate the ad 
		//	which you have added in the DataSet before
		//
		if(mStreamHelper != null && mList != null) {
			for (Integer pos : mStreamHelper.getAddedPosition()) {
				if(pos > mList.size()) {
					return;
				}
				
				//	check ad case
				//
				if(mList.get(pos) == null || mList.get(pos).equals("null")){
					continue;
				}
				
				mList.add(pos , null);
			}
		}
		
		super.notifyDataSetChanged();
	}
	//end

	//XXX@Stream-StreamHelper-getItemViewType@#Stream-StreamHelper-getItemViewType#
	@Override
	public int getItemViewType(int position) {
		
		if(mStreamHelper != null && mStreamHelper.isAd(position)) {
			return mStreamHelper.getItemViewType(position);
		}
		
		//	return your view type here
		//
		//
		return super.getItemViewType(position);
		
	}
	//end
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		// Get ad view if possible
		//XXX@Stream-StreamHelper-getView@#Stream-StreamHelper-getView#
		View adView =  null;
		if(mStreamHelper != null) {
			adView = mStreamHelper.getAD(position);
			
			//	or you can resize the ad width by this way
			//	adView =  mStreamHelper.getAD(position, someIntWidth);
			//
		}
		
		if(adView != null) {
			//	you can set the background
			//	such as
			//	adView.setBackgroundColor(Color.BLACK);
			//	adView.setBackgroundResource(your resid);
			//	adView.setBackground(your background drawable);
			//	adView.setBackgroundDrawable(your background drawable);
			return adView;
		}
		//end
				
		Holder holder = null;
		View view = convertView;
        if (view == null || view.getTag() == null) { 
        	holder = new Holder();
        	view = holder.inflate(mInflater);
        	view.setTag(holder);
        }
        else{
        	holder = (Holder) view.getTag();
        }
        view.setTag(holder);
        
        holder.setData(position);        
		return view;
	}
	
	@SuppressLint("UseSparseArrays") 
	SparseArray<Integer> mResizeHistory = new SparseArray<Integer>();

	class Holder {				
		ImageView testA ;
		RelativeLayout relativeLayout;
		boolean hasSetHeight = false;
		
		public void setData(int position) {	
			
			int index =  (position%mPics.length) -1;
			if(index<0){
				index = mPics.length-1;
			}
			
			testA.setBackgroundResource(mPics[index]);
			
			if(!hasSetHeight){
				testA.getLayoutParams().height  = mStreamListItemHeight;
				hasSetHeight = true;
			}
		}
		
		@SuppressLint("InflateParams")
		public View inflate(LayoutInflater inflater){
			relativeLayout = (RelativeLayout)inflater.inflate(R.layout.adapter_instream, null);
			testA = (ImageView)relativeLayout.findViewById(R.id.testA);
			testA.setScaleType(ScaleType.MATRIX);
			relativeLayout.setPadding(
					mStreamListItemPaddingLeftRight, mStreamListItemPaddingTopButtom,
					mStreamListItemPaddingLeftRight, mStreamListItemPaddingTopButtom);
			return relativeLayout;
		}
	}
	
	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList != null ? mList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
