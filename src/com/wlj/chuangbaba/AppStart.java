package com.wlj.chuangbaba;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.Login;
import com.wlj.chuangbaba.activity.Main;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.update.Update;
import com.wlj.update.Update.NextStep;
import com.wlj.update.Update.state;
import com.wlj.util.DpAndPx;
import com.wlj.util.GetResourceImage;
import com.wlj.util.UIHelper;
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
	boolean isnext = false;
	//2.检查更新
	private void CheckVersion(String path){
		
		new Update(this, path, new NextStep() {
			
			@Override
			public void next(state what) {
				System.out.println(what.name()+"  "+what.ordinal());
				
				if(what == state.no_update){
					
				}else if(what == state.service_timeout){
					
				}else if(what == state.download_failed){
					
				}else if(what == state.version_exception){
					
				}
				
				isnext = true;
			}
		}){}.check();
		
	}
	
	private void GoToMain() {
		if(!isnext){
			UIHelper.ToastMessage(getApplicationContext(), "正在检查更新");
			 new Handler().postDelayed(new  Runnable() {
				public void run() {
					isnext = true;
				}
			},3000);
			return;
		}
		Intent intent = new Intent(getApplicationContext(),Main.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CheckVersion("http://www.chuangbb.com/update.xml");
	}
	@Override
	protected void onResume() {
		super.onResume();
		
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
	}
	
}
