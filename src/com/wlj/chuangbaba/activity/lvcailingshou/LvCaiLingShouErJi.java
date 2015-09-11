package com.wlj.chuangbaba.activity.lvcailingshou;

import java.io.Serializable;
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

import com.wlj.bean.Base;
import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.util.Log;

/**
 * 铝材零售
 * @author wlj
 * 
 */
public class LvCaiLingShouErJi extends  MyBaseActivity implements DataPass {

	public DrawerLayout drawerlayout;
	public Fragment center_Fragment;
	public Group group;
	@Override
	protected void beforeTitle() {
		
		group = (Group)intent.getSerializableExtra("base");
		title.setText(group.getName());
	}

	@Override
	protected int setlayout() {
		return R.layout.lvcailingshouerji;
	}

	@Override
	protected void initView() {
		callweb();
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

	@Override
	public Base getGoup() {
		if(group == null){
			group = (Group)intent.getSerializableExtra("base");
			Log.i("LvCaiLingShouErJi", group+"");
		}
		return group;
	}
	
}
