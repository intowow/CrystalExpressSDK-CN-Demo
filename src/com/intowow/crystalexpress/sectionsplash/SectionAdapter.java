package com.intowow.crystalexpress.sectionsplash;

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

import com.intowow.crystalexpress.R;

public class SectionAdapter extends BaseAdapter {

	List<Object> mList;
	Activity mContext;
	LayoutInflater mInflater;
	int[] mPics = null;
	SparseArray<LinearLayout> adViewList = new SparseArray<LinearLayout>();
	int mStreamListItemPaddingTopButtom = 0;
	int mStreamListItemPaddingLeftRight = 0;
	int mStreamListItemHeight = 0;
	
	public SectionAdapter(Activity c, 
			List<Object> list, 
			int[] pics,
			int streamListItemPaddingLeftRight,
			int streamListItemPaddingTopButtom,
			int streamListItemHeight){
		mContext = c;
		mInflater = LayoutInflater.from(c);
		mList = list;
		mPics = pics;
		
		mStreamListItemPaddingLeftRight = streamListItemPaddingLeftRight;
		mStreamListItemPaddingTopButtom = streamListItemPaddingTopButtom;
		mStreamListItemHeight = streamListItemHeight;
	}
	
	public void destroy() {
		mContext = null;
		mInflater = null;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
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

}
