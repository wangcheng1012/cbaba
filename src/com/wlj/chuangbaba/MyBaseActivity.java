package com.wlj.chuangbaba;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;

/**
 * title、title两边，和一次网络访问
 * 
 * @author wlj
 */
public abstract class MyBaseActivity extends BaseFragmentActivity {

	protected ChuangBaBaContext mContext;
	protected Intent intent;
	protected int rightDrawable = R.drawable.rentou_quan_white;
	protected int lifeDrawable = R.drawable.back_white;
	protected TextView left,title,right;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext =(ChuangBaBaContext) getApplicationContext();
		intent = getIntent();
		setContentView(setlayout());
		initTitle();
		initView();
	}
	/**
	 * 设置title text，rightDrawable，lifeDrawable
	 * 
	 */
	protected abstract void beforeTitle();

	private void initTitle() {
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right);
		left = (TextView) findViewById(R.id.left);
		
		beforeTitle();
 
		right.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(rightDrawable);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);

		Drawable drawableback = getResources().getDrawable(lifeDrawable);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

		right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				rightOnClick();
			}
		});
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				liftOnClick();
			}
		});
	}

	protected Handler handle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				setViewDate(msg.obj);
				UIHelper.loadingClose();
				break;
			case -1:
				Exception e = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				UIHelper.loadingClose();
				break;
			default:
				Switch(msg);
				break;
			}
		}
	};
	
	protected void callweb() {
		UIHelper.loading("提交中……", this);
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Message message = Message.obtain();
				try {
					message.what = 1;
					message.obj = callWebMethod();
				} catch (Exception e) {
					message.what = -1;
					message.obj = e;
				}
				handle.sendMessage(message);
			}
			
		});
	}
	
	protected abstract int setlayout() ;
	protected abstract void initView() ;
	/**
	 * handle 返回 msg处理
	 * @param msg
	 */
	protected abstract void Switch(Message msg);
	/**
	 * 初始化访问网后的返回
	 * @param obj
	 */
	protected abstract void setViewDate(Object obj);
	/**
	 * title右边的点击按钮
	 */
	protected abstract void rightOnClick();
	
	protected abstract void liftOnClick();
	/**
	 * 初始化访问网
	 * 
	 */
	protected abstract Object callWebMethod( )throws Exception;
}
