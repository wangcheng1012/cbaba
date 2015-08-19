package com.wlj.chuangbaba;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wlj.chuangbaba.activity.Main;
import com.wlj.chuangbaba.services.CheckUpdate;
import com.wlj.chuangbaba.web.HttpPost;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppConfig;
import com.wlj.util.AppContext;
import com.wlj.util.DpAndPx;
import com.wlj.util.ExecutorServices;
import com.wlj.util.GetResourceImage;
import com.wlj.util.UIHelper;
import com.wlj.web.URLs;
import com.wlj.widget.MyScrollLayout;
import com.wlj.widget.SwitchViewDemoActivity;

public class AppStart extends BaseFragmentActivity {

	private MyScrollLayout mScrollLayout;
	
	//1.设置layout
	protected void addSwitchPage_(MyScrollLayout mScrollLayout) {
		this.mScrollLayout = mScrollLayout;
		ImageView fl1 = new ImageView(getApplicationContext());
		ImageView fl2 = new ImageView(getApplicationContext());
    	RelativeLayout fl3 = new RelativeLayout(getApplicationContext());
    	fl1.setScaleType(ScaleType.FIT_XY);
    	fl2.setScaleType(ScaleType.FIT_XY);
    	
    	fl1.setImageBitmap(GetResourceImage.get(getApplicationContext(), R.drawable.loading1));
    	fl2.setImageBitmap(GetResourceImage.get(getApplicationContext(), R.drawable.loading2));
    	fl3.setBackgroundDrawable(new BitmapDrawable(getResources(), GetResourceImage.get(getApplicationContext(), R.drawable.loading3)));
    	fl3.setGravity(Gravity.CENTER_HORIZONTAL);
    	//立即体验
    	TextView textView = new TextView(getApplicationContext());
    	textView.setPadding(DpAndPx.dpToPx(getApplicationContext(), 150), DpAndPx.dpToPx(getApplicationContext(), 10), DpAndPx.dpToPx(getApplicationContext(), 150), DpAndPx.dpToPx(getApplicationContext(), 10));
//    	textView.setBackgroundResource(R.color.white);
    	textView.setGravity(Gravity.CENTER_HORIZONTAL);
    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	layoutParams.bottomMargin= DpAndPx.dpToPx(getApplicationContext(), 80);
    	
    	layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	fl3.addView(textView,layoutParams);
    	
//    	mScrollLayout.setPrescroll(false);
    	mScrollLayout.addView(fl1);
    	mScrollLayout.addView(fl2);
    	mScrollLayout.addView(fl3);
//    	mScrollLayout.startAutotPay();
    	
    	textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//登录跳转
				GoToMain();
			}

		});
    	
	}
	
	private void GoToMain() {
		Intent intent = new Intent(getApplicationContext(), Main.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startService(new Intent(getApplicationContext(),CheckUpdate.class));
		final ChuangBaBaContext context = (ChuangBaBaContext)getApplicationContext();
		
		if(context.containsProperty(AppConfig.CONF_FIRSTSTART)){
			GoToMain();
		}else{
			View view = new SwitchViewDemoActivity(getApplicationContext(),null) {
				
				@Override
				protected void addSwitchPage(MyScrollLayout mScrollLayout) {
					addSwitchPage_(mScrollLayout);
				}
				@Override
				protected void scrollPoint(RelativeLayout layout) {
					setPointResId(R.drawable.point_selector);
					super.scrollPoint(layout);
				}
			}.createview();
			
			setContentView(view);
			
			ExecutorServices.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					
					String urlString = "baba/installTongji.do";
			        
			        HttpPost hp;
					try {
						
						hp = new HttpPost(new URL(URLs.HOST+urlString));
				        //手机号码
				        hp.addParemeter("phone", "");
				        //手机型号
				        hp.addParemeter("xinghao", "Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")" );
				        //手机硬件编码
				        hp.addParemeter("hardId",context.getDevicNO());
				        
				        String result = hp.getResult();
				        
				        JSONObject jsonObject = new JSONObject(result);
				        int state = jsonObject.optInt("state");
				        if(state == 2){
							new Handler(getMainLooper()).postDelayed(new Runnable() {
								public void run() {
									context.setProperty(AppConfig.CONF_FIRSTSTART,System.currentTimeMillis()+"");
								}
							}, 100);
				        }
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
		}
		
		
		
	}
	
}
