package com.wlj.chuangbaba.activity.lvcailingshou;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;
import com.wlj.util.Log;

/**
 * 铝材零售
 * @author wlj
 * 
 */
public class LvCaiLingShou extends  MyBaseActivity  {

	public DrawerLayout drawerlayout;
	public Fragment center_Fragment;
	
	@Override
	protected void beforeTitle() {
		title.setText("铝材零售");
	}

	@Override
	protected int setlayout() {
//		return R.layout.lvcailingshou2;
		return R.layout.lvcailingshou;
	}

	@Override
	protected void initView() {
		callweb();
		
//		drawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
//		drawerlayout.openDrawer(Gravity.LEFT);
//		drawerlayout.setDrawerListener(new DrawerListener() {
//			
//			/**
//			 * 当抽屉滑动状态改变的时候被调用
//			 * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
//			 * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
//			 */
//			@Override
//			public void onDrawerStateChanged(int arg0) {
//				Log.i("drawer", "drawer的状态：" + arg0);
//			}
//			/**
//			 * 当抽屉被滑动的时候调用此方法
//			 * arg1 表示 滑动的幅度（0-1）
//			 */
//			@Override
//			public void onDrawerSlide(View arg0, float arg1) {
//				Log.i("drawer", arg1 + "");
//			}
//			/**
//			 * 当一个抽屉被完全打开的时候被调用
//			 */
//			@Override
//			public void onDrawerOpened(View arg0) {
//				Log.i("drawer", "抽屉被完全打开了！");
//			}
//			/**
//			 * 当一个抽屉完全关闭的时候调用此方法
//			 */
//			@Override
//			public void onDrawerClosed(View arg0) {
//				Log.i("drawer", "抽屉被完全关闭了！");
//			}
//		});
//		
//		setDrawerLeftEdgeSize(this, drawerlayout, 0.5f);
	
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
	}

	@Override
	protected void rightOnClick() {
		Intent right = new Intent(getApplicationContext(), DaiLiShang_GuanLi.class);
		right.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(right);
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		return null;
	}
	
	public void setDrawerLeftEdgeSize(Activity activity,
			DrawerLayout drawerLayout, float displayWidthPercentage) {
		if (activity == null || drawerLayout == null)
			return;
		try {
			// find ViewDragHelper and set it accessible
			Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
					"mLeftDragger");
			leftDraggerField.setAccessible(true);
			ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
					.get(drawerLayout);
			// find edgesize and set is accessible
			Field edgeSizeField = leftDragger.getClass().getDeclaredField(
					"mEdgeSize");
			edgeSizeField.setAccessible(true);
			int edgeSize = edgeSizeField.getInt(leftDragger);
			// set new edgesize
			// Point displaySize = new Point();
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
					(int) (dm.widthPixels * displayWidthPercentage)));
		} catch (NoSuchFieldException e) {
			// ignore
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
	}

	public void changeFragment(Fragment fragment,int position) {
		
		String fragmentName = fragment.getClass().getSimpleName();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment findfragment = fm.findFragmentByTag(fragmentName+position);
		if (center_Fragment != null) {
			ft.detach(center_Fragment);
		}

		if (findfragment != null) {
			ft.attach(findfragment);
			center_Fragment = findfragment;
		} else {
			center_Fragment = fragment;
			ft.add(R.id.content_framelayout, center_Fragment, fragmentName+position);
		}
		ft.commitAllowingStateLoss();
	}
}
