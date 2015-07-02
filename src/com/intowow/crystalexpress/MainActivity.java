package com.intowow.crystalexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.intowow.crystalexpress.cedemo.CEOpenSplashActivity;
import com.intowow.crystalexpress.content.ContentActivity;
import com.intowow.crystalexpress.flip.FlipActivity;
import com.intowow.crystalexpress.opensplash.OpenSplashActivity;
import com.intowow.crystalexpress.sectionsplash.SectionSplashActivity;
import com.intowow.crystalexpress.stream.PullToRefreshStreamActivity;
import com.intowow.crystalexpress.stream.StreamActivity;
import com.intowow.sdk.I2WAPI;

public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	//	to initial the IntoWoW SDK
    	//
    	I2WAPI.init(this);
    }
    
    public void onResume() {
    	super.onResume();
    	I2WAPI.onActivityResume(this);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	I2WAPI.onActivityPause(this);
    }
    
    public void onClickCEDemo(View view) {
		Intent intent = new Intent();
		intent.setClass(this, CEOpenSplashActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickOpenSplash(View view) {
		Intent intent = new Intent();
		intent.setClass(this, OpenSplashActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickSectionSplash(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SectionSplashActivity.class);
		startActivity(intent);
    }
    
    public void onClickStream(View view) {
		Intent intent = new Intent();
		intent.setClass(this, StreamActivity.class);
		startActivity(intent);
    }
    
    public void onClickPullToRefreshStream(View view) {
		Intent intent = new Intent();
		intent.setClass(this, PullToRefreshStreamActivity.class);
		startActivity(intent);
    }
    
    public void onClickContent(View view) {
		Intent intent = new Intent();
		intent.setClass(this, ContentActivity.class);
		startActivity(intent);
    }
    
    public void onClickFlip(View view) {
		Intent intent = new Intent();
		intent.setClass(this, FlipActivity.class);
		startActivity(intent);
    }
    
}
