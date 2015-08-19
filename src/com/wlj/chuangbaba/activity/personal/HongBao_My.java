package com.wlj.chuangbaba.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.wlj.chuangbaba.MyBaseMoreFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.fragment.hongbao;
import com.wlj.chuangbaba.bean.DaiJinQuan;

public class HongBao_My extends MyBaseMoreFragmentActivity implements
		OnClickListener {

	private Fragment unUseFragment;
	private Fragment overdueFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View unuse = findViewById(R.id.unuse);
		unuse.setOnClickListener(this);
		unuse.performClick();
	}

	@Override
	protected void beforeTitle() {
		title.setText("我的红包");
	}

	@Override
	protected int setlayout() {
		return R.layout.hongbao_my;
	}

	@Override
	protected void initView() {
		
		findViewById(R.id.overdue).setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unuse:
			if(oldposition != 0 ){
				movecursor(0);
				if (unUseFragment == null) {
					unUseFragment = new hongbao();
					Bundle bundle = new Bundle();
					bundle.putInt("state", DaiJinQuan.daijinquan_state_nomal);
					unUseFragment.setArguments(bundle);
				}
				changeFragment(unUseFragment,0);
			}
			break;
		case R.id.overdue:
			if(oldposition != 1 ){
				movecursor(1);
				if (overdueFragment == null) {
					overdueFragment = new hongbao();
					Bundle bundle = new Bundle();
					bundle.putInt("state", DaiJinQuan.daijinquan_state_yiguoqi);
					overdueFragment.setArguments(bundle);
				}
				changeFragment(overdueFragment,1);
			}
			break;
		}
	}
}
