package com.intowow.crystalexpress.stream;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.intowow.crystalexpress.R;
import com.intowow.sdk.DeferStreamAdapter;

public class StreamAdapter extends DeferStreamAdapter {//XXX#Stream-DeferStreamAdapter#

	List<Object> mList;
	Activity mContext;
	LayoutInflater mInflater;
	int[] mPics = null;
	SparseArray<LinearLayout> adViewList = new SparseArray<LinearLayout>();
	int mStreamListItemPaddingTopButtom = 0;
	int mStreamListItemPaddingLeftRight = 0;
	int mStreamListItemHeight = 0;
	
	public StreamAdapter(Activity c, 
			String placement, 
			List<Object> list, 
			int[] pics,
			int streamListItemPaddingLeftRight,
			int streamListItemPaddingTopButtom,
			int streamListItemHeight){//XXX#Stream-placement#
		
		super(c, placement);
		
		mContext = c;
		mInflater = LayoutInflater.from(c);
		mList = list;
		mPics = pics;
		
		mStreamListItemPaddingLeftRight = streamListItemPaddingLeftRight;
		mStreamListItemPaddingTopButtom = streamListItemPaddingTopButtom;
		mStreamListItemHeight = streamListItemHeight;
	}
	
	public void setList(List<Object> list) {
		mList = list;
	}

	//TODO
/***********************************************************/
/*	
	//XXX@Stream-getItemViewType@#Stream-getItemViewType#
	@Override
	public int getItemViewType(int position) {
		//	if you have implemented getItemViewType(), 
		//	be sure to check if the item is an ad 
		//	in this position.
		if(isAd(position)) {
			return super.getItemViewType(position);
		}else{
			//	return your view type here
			//
			//
		}
	}
	//end
*/
/***********************************************************/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//XXX@Stream-getView@#Stream-getView#
		// Get ad view if possible
		final View adView =  getStreamAd(position);	
		
		//	or you can resize the ad width by this way
		//	final View adView =  getStreamAd(position, 700);
		//
		
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
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * you should implement this method to tell the SDK which position you want to add
	 * */
	//XXX@Stream-initADListener@#Stream-initADListener#
	@Override
	public void initADListener() {
		setAdListener(new com.intowow.sdk.StreamHelper.ADListener() {

			@Override
			public int onADLoaded(int position) {
				// 	when the SDK load one stream ad,
				//	it will call this callback for getting 
				//	the position you add in the DataSet.
				//
				//	if you call getStreamAd in the getView(),
				//	the SDK will return the ad refer to this position.
				//
				//	if you return "-1", it means that the ad is not added in your DataSet
				//
				
				position = getDefaultMinPosition(position);
				
				if (mList.size() >  position) {				
					// just allocate one position for stream ad
					//
					mList.add(position, null);
					notifyDataSetChanged();
					return position;
				}
				else {				
					return -1;
				}
			}
			
		});
	}
	//end

	/**
	 * you should implement this method to tell the SDK that your DataSet has been changed
	 * */
	//XXX@Stream-initStreamADListener@#Stream-initStreamADListener#
	@Override
	public void initStreamADListener() {
		setStreamADListener(new StreamADListener() {

			@Override
			public void onDataSetChanged() {
				for (Integer pos : getAddedStreamAdPosition()) {
					if(pos > mList.size()) {
						return;
					}
					
					//	check ad case
					//
					if(mList.get(pos) == null || mList.get(pos).equals("null")) {
						continue;
					}
					
					mList.add(pos , null);
				}
			}});
	}
	//end
	
	//XXX@Stream-getDefaultMinPosition@#Stream-getDefaultMinPosition#
	@Override
	public int getDefaultMinPosition(int position) {
		// Don't place ad at the first place
		return Math.max(1, position);
	}
	//end

}
