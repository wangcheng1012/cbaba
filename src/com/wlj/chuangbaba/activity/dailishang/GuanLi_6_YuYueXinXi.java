package com.wlj.chuangbaba.activity.dailishang;

import android.os.Message;

import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.fragment._6_YuYueXinXi;

/**
 * 
 * @author wlj
 * 
 */
public class GuanLi_6_YuYueXinXi extends MyBaseFragmentActivity{

	@Override
	protected void beforeTitle() {
		title.setText("预约信息");
	}

	@Override
	protected int setlayout() {
		return R.layout.dailishang_6_yuyuexinxi;
	}

	@Override
	protected void initView() {
		_6_YuYueXinXi yueXinXi = new _6_YuYueXinXi();
		changeFragment(yueXinXi, 1);
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
		
	}

	@Override
	protected void rightOnClick() {
		finish();
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		return null;
	}


}
