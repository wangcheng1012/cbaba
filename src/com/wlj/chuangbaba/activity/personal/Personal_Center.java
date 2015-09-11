package com.wlj.chuangbaba.activity.personal;

import static com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi.JiBenXinXi_requestCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppConfig;
import com.wlj.chuangbaba.web.URLs;

/**
 * 会员个人中心
 * @author wlj
 *
 */
public class Personal_Center extends MyBaseFragmentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!User.type_huiyuan.equals(mContext.getProperty(AppConfig.CONF_TYPT))){
			//登录
			Intent intentwenda = new Intent(getApplicationContext(),HuiYuanLogin.class);
			intentwenda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intentwenda,11);
		}else{
			callweb();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Personal_changeziliao:
			Intent chang = new Intent(getApplicationContext(), ChangeJiBenXinXi.class);
			chang.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			chang.putExtra("base", user);
			startActivity(chang);
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
		case R.id.personal_hongbao:
			Intent hongbao = new Intent(getApplicationContext(), HongBao_My.class);
			hongbao.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(hongbao);
			break;
		case R.id.personal_mytiwen:
			Intent mytiwen = new Intent(getApplicationContext(), MyTiWenActivity.class);
			mytiwen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mytiwen);
			break;

		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data!= null && requestCode == 11 && resultCode == 22){
			// esultCode == 22 没登录
			finish();
		}else if(JiBenXinXi_requestCode == resultCode){
			callweb();
		}else if(55  == resultCode){
			finish();
		}
		
	}

	@Override
	protected void beforeTitle() {
		title.setText("个人中心");
		right.setText("安全退出");
	}

	@Override
	protected int setlayout() {
		return R.layout.geren_center;
	}

	@Override
	protected void initView() {
		right.setCompoundDrawables(null, null, null, null);
		
		findViewById(R.id.Personal_changeziliao).setOnClickListener(this);;
		findViewById(R.id.Personal_dingdan).setOnClickListener(this);;
		findViewById(R.id.Personal_jifen).setOnClickListener(this);
		findViewById(R.id.personal_soucang).setOnClickListener(this);
		findViewById(R.id.personal_hongbao).setOnClickListener(this);
		findViewById(R.id.personal_mytiwen).setOnClickListener(this);
	}

	@Override
	protected void Switch(Message msg) {
		
	}
	private User user;
	@Override
	protected void setViewDate(Object obj) {
		BaseList baselist = (BaseList)obj; 
		List<Base> list = baselist.getBaselist();
		if(list==null)return;
		user =(User) list.get(0);

		((TextView)findViewById(R.id.name)).setText(user.getRealname());
		((TextView)findViewById(R.id.phone)).setText(user.getPhone());
		((TextView)findViewById(R.id.addr)).setText(user.getCompanyAddr());
		((TextView)findViewById(R.id.dengji)).setText(user.getDengji());
	}

	@Override
	protected void rightOnClick() {
		((ChuangBaBaContext)getApplication()).loginOut();
		finish();
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url",  URLs.getUserInfo);
		map.put("user_Type",  User.type_huiyuan);
		
		BaseList baseList = mContext.Request(this,map,new User());
		
		return baseList;
	}
}
