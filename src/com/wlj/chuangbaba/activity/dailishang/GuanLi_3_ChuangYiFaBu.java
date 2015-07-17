package com.wlj.chuangbaba.activity.dailishang;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.HeTong;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;

/**
 * 代理商——管理——发布创意
 * 
 * @author wlj
 * 
 */
public class GuanLi_3_ChuangYiFaBu extends BaseFragmentActivity implements OnClickListener {

	private EditText chuangyitext;
	private Button publish;
	private ChuangBaBaContext mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailishang_3_chuangyifabu);
		mContext = (ChuangBaBaContext) getApplicationContext();
		init_title();
		initView();
	}

	private void initView() {
		chuangyitext = (EditText)findViewById(R.id.chuangyitext);
		publish = (Button)findViewById(R.id.publish);
		publish.setOnClickListener(this);
	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("发布创意");

		TextView right = (TextView) findViewById(R.id.right);
		right.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(
				R.drawable.rentou_quan_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);

		TextView left = (TextView) findViewById(R.id.left);
		Drawable drawableback = getResources().getDrawable(
				R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

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
			finish();
			break;
		case R.id.publish:
			publish();
			break;
		}

	}

	private void publish() {
		UIHelper.loading("正在提交……", this);
		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					final boolean b = mContext.publishChuangYi(chuangyitext.getText()+"");
					
					runOnUiThread( new Runnable() {
						public void run() {
							UIHelper.loadingClose();
							UIHelper.ToastMessage(mContext, b?"提交成功":"提交失败");
							finish();
						}
					});
					
				} catch (Exception e) {
					UIHelper.ToastMessage(mContext,e.getMessage() );
				}
				
			}
		});
		
	}

}
