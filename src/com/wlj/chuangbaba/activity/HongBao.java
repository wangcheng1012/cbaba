package com.wlj.chuangbaba.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.HongBao_My;
import com.wlj.chuangbaba.activity.personal.HuiYuanLogin;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.bean.DaiJinQuan;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;

public class HongBao extends MyBaseFragmentActivity implements OnClickListener {

	private Button lijichoujiang;
	private Button choujiang;
	private TextView getrand;
	private EditText phone;
	private EditText rand;
	@Override
	protected void beforeTitle() {
		title.setText("近期活动");
	}

	@Override
	protected int setlayout() {
		return R.layout.hongbao;
	}

	@Override
	protected void initView() {
		
		lijichoujiang = (Button)findViewById(R.id.lijichoujiang);
		choujiang = (Button)findViewById(R.id.choujiang);
		getrand = (TextView)findViewById(R.id.getrand);
		phone = (EditText)findViewById(R.id.phone);
		rand = (EditText)findViewById(R.id.rand);
		
		
		lijichoujiang.setOnClickListener(this);
		getrand.setOnClickListener(this);
	}

	@Override
	protected void Switch(Message msg) {
		switch (msg.what) {
		case 3:
			if(msg.arg1 == 0){
				getrand.setText("重新获取");
				
				getrand.setEnabled(true);
				
			}else{
				getrand.setText(msg.arg1+"");
			}
			
			break;
		}
	}

	@Override
	protected void setViewDate(Object obj) {

		UIHelper.ToastMessage(mContext, "发送成功");

		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				int i = 60;
				while (i >= 0) {
					Message obtain = Message.obtain();

					obtain.what = 3;
					obtain.arg1 = i;
					handle.sendMessage(obtain);
					i--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}

	@Override
	protected void rightOnClick() {
		Class<?> clas = null;
		if (!User.type_huiyuan.equals(ChuangBaBaContext.preferences.getString("type", ""))) {
			// 登录
			clas = HuiYuanLogin.class;
		} else {
			clas = Personal_Center.class;
		}
		Intent right = new Intent(getApplicationContext(),clas);
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
		
		case R.id.lijichoujiang:
			
			String name = phone.getText().toString().trim();
			String password = rand.getText().toString().trim();

			if ("".equals(name)) {
				UIHelper.ToastMessage(mContext, "电话号码为空");
				return;
			}

			if ("".equals(password)) {
				UIHelper.ToastMessage(mContext, "验证码为空");
				return;
			}
			
			Message message = Message.obtain();
			try {
				Map<String, String>  map= new HashMap<String, String>();
				map.put("name", name);
				map.put("password", password);
				
//				DaiJinQuan daijinquan = mContext.getDaiJinQuan(base, pageindex);
				message.what = 4;
				message.obj =  null;
				
			} catch (Exception e1) {
				message.what = -1;
				message.obj = e1;
			}
			handle.sendMessage(message);
			
			Intent ntent = new Intent(getApplicationContext(), HongBao_My.class);
			ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ntent);
			
			break;
		case R.id.getrand:
			final String name2 = phone.getText().toString().trim();
			
			if ("".equals(name2)) {
				UIHelper.ToastMessage(mContext, "手机号为空");
				return;
			}
			if(name2.length() != 11){
				UIHelper.ToastMessage(mContext, "请输入11位手机号码");
				return;
			}
			getrand.setText("发送中……");
			getrand.setEnabled(false);
			
			ExecutorServices.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					Message obtain = Message.obtain();
					try {
						String rand = mContext.getLoginRand(name2);
						obtain.what = 1;
						obtain.obj = rand;
					} catch (Exception e) {
						obtain.what = -1;
						obtain.obj = e;
					}
					handle.sendMessage(obtain);
				}
			});
			break;
		case R.id.choujiang:
			
			
			break;
		}
	}

}
