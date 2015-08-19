package com.wlj.chuangbaba.activity.personal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;
/**
 * 会员登录
 * @author wlj
 *
 */
public class HuiYuanLogin extends BaseFragmentActivity implements OnClickListener {

	private EditText phone, randcodeView;
	private ChuangBaBaContext mContext;
	private Button getrandbut;
	private String description;
	private Handler handle = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 3:
				if(msg.arg1 == 0){
					getrandbut.setText("重新获取");
					
					getrandbut.setEnabled(true);
					
				}else{
					getrandbut.setText(msg.arg1+"");
				}
				
				break;
			case 1:
				String description = (String)msg.obj;
				UIHelper.ToastMessage(mContext, description);
				
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
					}});
				break;
			case 2:
				Boolean b = (Boolean)msg.obj;
			
				if (b) {
					
					
//					Intent login = new Intent(getApplicationContext(),Personal_Center.class);
//					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(login);
//					finish();
					
					UIHelper.ToastMessage(mContext, "登录成功");
					setResult(55);
					finish();
				} 
				break;
			case -1:
				Exception e = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				
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
					}});
				
				break;
			case -2:
				Exception e1 = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, e1.getMessage());
				break;
			}
		};
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.huiyuandenglu);
		mContext = (ChuangBaBaContext) getApplicationContext();
		init_title();
		initView();
	}
	
	private void initView() {
		((Button) findViewById(R.id.login)).setOnClickListener(this);
		phone = (EditText) findViewById(R.id.phone);
		randcodeView = (EditText) findViewById(R.id.randcode);
		getrandbut = (Button) findViewById(R.id.getrandbut);
		getrandbut.setOnClickListener(this);
	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("会员登录");

		TextView left = (TextView) findViewById(R.id.left);

		Drawable drawableback = getResources().getDrawable(R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

		left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			
			Intent intent = getIntent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			setResult(22, intent);
			finish();
			
			break;
		case R.id.login:
			String name = phone.getText().toString().trim();
			String password = randcodeView.getText().toString().trim();

			if ("".equals(name)) {
				UIHelper.ToastMessage(mContext, "用户名为空");
				return;
			}

			if ("".equals(password)) {
				UIHelper.ToastMessage(mContext, "验证码为空");
				return;
			}
			
			User u = new User();
			u.setName(name);
			u.setRandCode(password);
			login(u);
			break;
		case R.id.getrandbut:
			final String name2 = phone.getText().toString().trim();
			
			if ("".equals(name2)) {
				UIHelper.ToastMessage(mContext, "用户名为空");
				return;
			}
			if(name2.length() != 11){
				UIHelper.ToastMessage(mContext, "请输入11位手机号码");
				return;
			}
			getrandbut.setText("发送中……");
			getrandbut.setEnabled(false);
			
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
		}

	}

	private void login(final User u) {

		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				Message obtain = Message.obtain();
				try {
					boolean user = mContext.Login(u);
					obtain.what = 2;
					obtain.obj = user;
				} catch (Exception e) {
					e.printStackTrace();
					obtain.what = -2;
					obtain.obj = e;
				}
				handle.sendMessage(obtain);
			}
		});

	}
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		Intent intent = getIntent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setResult(22, intent);
		finish();
	}
	
	
}
