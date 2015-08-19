package com.wlj.chuangbaba.activity.dailishang;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.ui.BaseFragmentActivity;

/**
 * 代理商——管理——我提交的创意
 * 
 * @author wlj
 * 
 */
public class GuanLi_8_MyChuangYi extends MyBaseFragmentActivity   {


	@Override
	protected void beforeTitle() {
		title.setText("我提交的创意");
	}

	@Override
	protected int setlayout() {
		return R.layout.dailishang_8_mychuangyi;
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
		Intent intent = new Intent(mContext, DaiLiShang_GuanLi.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
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
