package com.intowow.crystalexpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.intowow.crystalexpress.cedemo.CEOpenSplashActivity;
import com.intowow.crystalexpress.content.ContentActivity;
import com.intowow.crystalexpress.flip.FlipActivity;
import com.intowow.crystalexpress.opensplash.OpenSplashActivity;
import com.intowow.crystalexpress.setting.SettingActivity;
import com.intowow.crystalexpress.stream.defer.MultipleDeferAdapterActivity;
import com.intowow.crystalexpress.stream.defer.SingleDeferAdapterActivity;
import com.intowow.crystalexpress.stream.fixposition.MultipleFixPositionAdapterActivity;
import com.intowow.crystalexpress.stream.fixposition.SingleFixPositionAdapterActivity;
import com.intowow.crystalexpress.stream.streamhelper.MultipleFixPositionStreamHelperActivity;
import com.intowow.crystalexpress.stream.streamhelper.MultipleStreamHelperActivity;
import com.intowow.crystalexpress.stream.streamhelper.SingleFixPositionStreamHelperActivity;
import com.intowow.crystalexpress.stream.streamhelper.SingleStreamHelperActivity;

public class MainActivity extends BaseActivity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    
    public void onClickStreamSingleDefer(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SingleDeferAdapterActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickStreamMultipleDefer(View view) {
		Intent intent = new Intent();
		intent.setClass(this, MultipleDeferAdapterActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickSingleStreamHelper(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SingleStreamHelperActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickMultipStreamHelper(View view) {
		Intent intent = new Intent();
		intent.setClass(this, MultipleStreamHelperActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickContent(View view) {
		Intent intent = new Intent();
		intent.setClass(this, ContentActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickFlip(View view) {
		Intent intent = new Intent();
		intent.setClass(this, FlipActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickFixSingleStreamHelper(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SingleFixPositionStreamHelperActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickFixMultipleStreamHelper(View view) {
		Intent intent = new Intent();
		intent.setClass(this, MultipleFixPositionStreamHelperActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickSingleFixAdapter(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SingleFixPositionAdapterActivity.class);
		startActivity(intent);
		finish();
    }
    
    public void onClickMultipleFixAdapter(View view) {
		Intent intent = new Intent();
		intent.setClass(this, MultipleFixPositionAdapterActivity.class);
		startActivity(intent);
		finish();
    }
    
	public void onClickSetting(View view) {
		Intent intent = new Intent();
		intent.setClass(this, SettingActivity.class);
		startActivity(intent);
	}
    
    
}
