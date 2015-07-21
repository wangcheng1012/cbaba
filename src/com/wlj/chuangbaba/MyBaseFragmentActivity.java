package com.wlj.chuangbaba;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;

import com.wlj.bean.Base;

/**
 * rightDrawable，lifeDrawable 要先设置再initTitle
 * 实现了title,changfragment , handle
 * @author wlj
 */
public abstract class MyBaseFragmentActivity extends MyBaseActivity {

	/**
	 * 点击的上次位置
	 * //防止重复点击
	 */
	protected int oldposition = -1;
	protected Fragment findFragment = null;
	protected Fragment center_Fragment;
	
	protected void changeFragment(Fragment fragment,int position) {
	
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
			ft.add(R.id.framelay_center, center_Fragment, fragmentName+position);
		}
		ft.commitAllowingStateLoss();
	}
}
