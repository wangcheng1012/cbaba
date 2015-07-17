package com.wlj.chuangbaba.activity.personal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppManager;

/**
 * 会员个人中心
 * @author wlj
 *
 */
public class Personal_Center extends BaseFragmentActivity implements OnClickListener {

	private ChuangBaBaContext mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geren_center);
		mContext = (ChuangBaBaContext)getApplicationContext();
		if(!User.type_huiyuan.equals(ChuangBaBaContext.preferences.getString("type", ""))){
			//登录
			Intent intentwenda = new Intent(getApplicationContext(),HuiYuanLogin.class);
			intentwenda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentwenda);
			finish();
		}
		init_title();
		initView();
	}
	
	private void initView() {
		findViewById(R.id.Personal_dingdan).setOnClickListener(this);;
		findViewById(R.id.Personal_jifen).setOnClickListener(this);
		findViewById(R.id.personal_soucang).setOnClickListener(this);
		findViewById(R.id.personal_mytiwen).setOnClickListener(this);
		
	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("个人中心");
		//
		TextView left = (TextView) findViewById(R.id.left);
		Drawable drawableback = getResources().getDrawable(R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);
//		left.setText("返回");
		//
		TextView right = (TextView) findViewById(R.id.right);
		right.setGravity(Gravity.CENTER_VERTICAL);
//		Drawable drawable = getResources().getDrawable(R.drawable.rentou_quan_white);
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
//		right.setCompoundDrawables(drawable, null, null, null);
		right.setText("安全退出");
		
		right.setOnClickListener(this);
		left.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			finish();
			break;
		case R.id.right:
			((ChuangBaBaContext)getApplication()).loginOut();
			finish();
			break;
		case R.id.Personal_dingdan:
			Intent order = new Intent(getApplicationContext(), OrderActivity.class);
			order.putExtra("type", OrderActivity.huiyuan);
			order.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(order);
			break;
		case R.id.Personal_jifen:
//			Intent jifen = new Intent(getApplicationContext(), JiFenActivity.class);
//			jifen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(jifen);
			break;
		case R.id.personal_soucang:
			Intent soucang = new Intent(getApplicationContext(), ShouCangActivity.class);
			soucang.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(soucang);
			break;
		case R.id.personal_mytiwen:
			Intent mytiwen = new Intent(getApplicationContext(), MyTiWenActivity.class);
			mytiwen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mytiwen);
			break;

		}
	}
}
