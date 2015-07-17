package com.dady.yoyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dady.yoyo.fragment.Fragment_Hot;
import com.wlj.chuangbaba.MyBaseMoreFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.web.MsgContext;

/**
 * 热门tab 2015-5-5
 * 
 * @author Administrator
 */
public class HotActivity extends MyBaseMoreFragmentActivity implements
		OnClickListener {
	Fragment Center_mContent;
	Fragment fragment_7;
	Fragment fragment_companyDynamic;
	Fragment fragment_hangyeDynamic;
	private TextView remenzhixun, companyDynamic, hangyeDynamic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initChoose();
	}

	private void initChoose() {
		int index = getIntent().getIntExtra("index", 0);
		switch (index) {
		case 0:
			remenzhixun.performClick();
			break;
		case 1:
			companyDynamic.performClick();
			break;
		case 2:
			hangyeDynamic.performClick();
			break;
		}

	}

	protected void initView() {
		
		remenzhixun = (TextView) findViewById(R.id.remenzhixun);
		companyDynamic = (TextView) findViewById(R.id.companyDynamic);
		hangyeDynamic = (TextView) findViewById(R.id.hangyeDynamic);
		remenzhixun.setOnClickListener(this);
		companyDynamic.setOnClickListener(this);
		hangyeDynamic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.remenzhixun:
			movecursor(0);
			if (null == fragment_7) {
				fragment_7 = new Fragment_Hot();
				Bundle bundle = new Bundle();
				bundle.putString("typeid", MsgContext.id_hot);
				fragment_7.setArguments(bundle);
			}
			title.setText("热门资讯");
			changeFragment(fragment_7, 0);
			break;
		case R.id.companyDynamic:
			movecursor(1);
			if (null == fragment_companyDynamic) {
				fragment_companyDynamic = new Fragment_Hot();
				Bundle bundle = new Bundle();
				bundle.putString("typeid", MsgContext.id_companyDynamic);
				fragment_companyDynamic.setArguments(bundle);
			}
			title.setText("公司动态");
			changeFragment(fragment_companyDynamic, 1);
			break;
		case R.id.hangyeDynamic:
			movecursor(2);
			if (null == fragment_hangyeDynamic) {
				fragment_hangyeDynamic = new Fragment_Hot();
				Bundle bundle = new Bundle();
				bundle.putString("typeid", MsgContext.id_hangyeDynamic);
				fragment_hangyeDynamic.setArguments(bundle);
			}
			title.setText("行业动态");
			changeFragment(fragment_hangyeDynamic, 2);
			break;
	
		}
	}

	@Override
	protected void beforeTitle() {
		title.setText("热门资讯");
	}

	@Override
	protected int setlayout() {
		return R.layout.fragment_hot;
	}

	@Override
	protected void Switch(Message msg) {
	}

	@Override
	protected void setViewDate(Object obj) {
	}

	@Override
	protected void rightOnClick() {
		Intent right = new Intent(mContext, Personal_Center.class);
		right.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
