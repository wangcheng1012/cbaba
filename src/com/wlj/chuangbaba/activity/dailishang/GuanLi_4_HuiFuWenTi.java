package com.wlj.chuangbaba.activity.dailishang;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;

/**
 * 代理商——管理——回复问题
 * 
 * @author wlj
 */
public class GuanLi_4_HuiFuWenTi extends MyBaseActivity implements OnClickListener {

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left:
			finish();
			break;
		case R.id.right:
			finish();
			break;
		}

	}

	@Override
	protected void beforeTitle() {
		title.setText("回复问题");
	}

	@Override
	protected int setlayout() {
		return R.layout.dailishang_4_huifuwenti;
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
