package com.wlj.chuangbaba.activity.wenda;

import android.content.Intent;
import android.os.Message;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;

public class WenDa extends  MyBaseActivity  {

	@Override
	protected void beforeTitle() {
		title.setText("意见问答");
//		left.setText("返回");
		right.setText("发布");
		right.setTextSize(15);
		rightDrawable = R.drawable.fabu;
	}

	@Override
	protected int setlayout() {
		return R.layout.wenda;
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
		Intent right = new Intent(getApplicationContext(), WenDa_fawen.class);
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
