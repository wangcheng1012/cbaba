package com.wlj.chuangbaba.activity.dailishang;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;

/**
 * 代理商登录
 * @author wlj
 *
 */
public class DaiLiShang extends BaseFragmentActivity implements OnClickListener {

	private EditText loginName, logonPassword;
	private ChuangBaBaContext mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailishang);
		mContext = (ChuangBaBaContext) getApplicationContext();
		init_title();

		initView();
	}

	private void initView() {
		((Button) findViewById(R.id.login)).setOnClickListener(this);
		loginName = (EditText) findViewById(R.id.loginName);
		logonPassword = (EditText) findViewById(R.id.logonPassword);
	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("代理商登录");

		TextView left = (TextView) findViewById(R.id.left);

		Drawable drawableback = getResources().getDrawable(
				R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

		// right.setOnClickListener(this);
		left.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			finish();
			break;
		case R.id.login:
			String name = loginName.getText().toString().trim();
			String password = logonPassword.getText().toString().trim();

			if ("".equals(name)) {
				UIHelper.ToastMessage(mContext, "用户名为空");
				return;
			}

			if ("".equals(password)) {
				UIHelper.ToastMessage(mContext, "密码为空");
				return;
			}
			User u = new User();
			u.setName(name);
			u.setPassword(password);
			login(u);
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

					if (user) {
						Intent login = new Intent(getApplicationContext(),DaiLiShang_GuanLi.class);
						login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(login);
						runOnUiThread(new Runnable() {
							public void run() {
								UIHelper.ToastMessage(mContext, "登录成功");
							}
						});
						finish();
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								UIHelper.ToastMessage(mContext, "登录失败");
							}
						});
						
					}
				} catch (final Exception e) {
					AppException.http(e);
					runOnUiThread(new Runnable() {
						public void run() {
							UIHelper.ToastMessage(mContext, e.getMessage());
						}
					});
				}
			}
		});

	}
}
