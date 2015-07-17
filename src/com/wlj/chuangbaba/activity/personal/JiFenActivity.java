package com.wlj.chuangbaba.activity.personal;

import android.content.Intent;
import android.os.Message;

import com.wlj.chuangbaba.MyBaseActivity;

public class JiFenActivity extends MyBaseActivity   {

	@Override
	protected void beforeTitle() {
		title.setText("我的积分");
	}

	@Override
	protected int setlayout() {
		return 0;
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
		
	}

	@Override
	protected void rightOnClick() {
		Intent right = new Intent(getApplicationContext(),Personal_Center.class);
		right.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(right);
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}}
