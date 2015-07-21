package com.wlj.chuangbaba.activity.lvcailingshou;

import android.content.Intent;
import android.os.Message;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;

/**
 * 铝材零售
 * @author wlj
 * 
 */
public class LvCaiLingShou extends  MyBaseActivity  {

	@Override
	protected void beforeTitle() {
		title.setText("铝材零售");
	}

	@Override
	protected int setlayout() {
		return R.layout.lvcailingshou;
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
}
