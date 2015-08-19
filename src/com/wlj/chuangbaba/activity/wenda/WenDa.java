package com.wlj.chuangbaba.activity.wenda;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.HuiYuanLogin;
import com.wlj.chuangbaba.activity.wenda.fragment.WenDa_Fragment;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.AppConfig;
import com.wlj.util.Log;
import com.wlj.util.UIHelper;

public class WenDa extends MyBaseActivity {

	@Override
	protected void beforeTitle() {

		title.setText("意见问答");
		// left.setText("返回");
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

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if( arg1 == 22){
			
			finish();
		}else if(arg1 == 55){
			FragmentManager fragmentManager = getSupportFragmentManager();
			
			WenDa_Fragment wenda_Fragment = (WenDa_Fragment)fragmentManager.findFragmentByTag("wenda_Fragment");
			
			wenda_Fragment.callWeb(1,  UIHelper.LISTVIEW_ACTION_REFRESH);
		}
		
	}
}
