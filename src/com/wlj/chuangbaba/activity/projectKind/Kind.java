package com.wlj.chuangbaba.activity.projectKind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.Main;

public class Kind extends MyBaseFragmentActivity {

	@Override
	protected void beforeTitle() {
		rightDrawable = R.drawable.home_white;
		title.setText("商品类别");
		right.setText("首页");
		right.setTextSize(15);
	}

	@Override
	protected int setlayout() {
		return R.layout.kind;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void Switch(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setViewDate(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void rightOnClick() {
		Intent intent = new Intent(getApplicationContext(), Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void liftOnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object callWebMethod() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
