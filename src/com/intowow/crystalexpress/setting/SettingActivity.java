package com.intowow.crystalexpress.setting;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.intowow.crystalexpress.R;

public class SettingActivity extends Activity {
	
	private EditText mAudienceTargetingTagInput = null;
	private TextView mResultView = null;
	
	private Button mAddBtn = null;
	private Button mClearAllBtn = null;
	
	private ArrayList<String> mTagList = new ArrayList<String>();
	private SharedPreferences mPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		init();
	}
	
	private void init() {
		mPreferences = getSharedPreferences(SettingConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);

		mAudienceTargetingTagInput = (EditText) findViewById(R.id.editText_AudienceTargetingTag);
		mAddBtn = (Button) findViewById(R.id.button_Add);
		mAddBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String inputTag = getProcessInputTag();
				if (inputTag.length() > 0) {
					mTagList.add(inputTag);
				}
				resultShow();
			}
		});
		mClearAllBtn = (Button) findViewById(R.id.button_CleanAll);
		mClearAllBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTagList.clear();
				resultShow();
			}
		});
		mResultView = (TextView) findViewById(R.id.textView_ResultTag);
		
		if (mPreferences != null) {
			String tagString = mPreferences.getString(SettingConfig.PREFERENCES_AUDIENCE_TARGETING_TAGS, "");
			if (tagString.equals("") == false) {
				mTagList.addAll(Arrays.asList(tagString.split(",")));
			}
			resultShow();
		}
	}
	
	private String getProcessInputTag() {
		String returnString = "";
		if (mAudienceTargetingTagInput != null) {
			if (mAudienceTargetingTagInput.getText().length() > 0) {
				String tempString = String.valueOf(mAudienceTargetingTagInput.getText());
				returnString = tempString.replace(" ", "");
				mAudienceTargetingTagInput.setText("");
			}			
		}
		
		return returnString;
	}
	
	private void resultShow() {
		if (mResultView != null) {
			String tagString = "";
			for (int i = 0; i < mTagList.size(); i++) {
				String tag = mTagList.get(i);
				tagString = tagString + tag;
				if (i < (mTagList.size() - 1)) {
					tagString += ",";
				}
			}
			mResultView.setText(tagString);
			setAudienceTargetingTagPreference(tagString);
		}
	}
	
	private void setAudienceTargetingTagPreference(String tagString) {
		mPreferences.edit()
					.putString(SettingConfig.PREFERENCES_AUDIENCE_TARGETING_TAGS, tagString)
					.commit();
	}
	
}