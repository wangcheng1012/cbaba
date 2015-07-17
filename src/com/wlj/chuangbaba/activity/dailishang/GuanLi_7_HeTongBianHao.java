package com.wlj.chuangbaba.activity.dailishang;

import android.os.Bundle;
import android.os.Message;

import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.fragment._7_HeTongBianHao;

/**
 * 代理商——管理——合同编号
 * 
 * @author wlj
 * 
 */
public class GuanLi_7_HeTongBianHao extends MyBaseFragmentActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void beforeTitle() {
		title.setText("合同编号");
	}

	@Override
	protected int setlayout() {
		return R.layout.dailishang_6_yuyuexinxi;
	}

	@Override
	protected void initView() {
		
		_7_HeTongBianHao yueXinXi = new _7_HeTongBianHao();
		changeFragment(yueXinXi, 1);
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
		finish();
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
