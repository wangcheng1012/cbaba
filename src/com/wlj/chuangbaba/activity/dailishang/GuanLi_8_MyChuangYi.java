package com.wlj.chuangbaba.activity.dailishang;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wlj.chuangbaba.R;
import com.wlj.ui.BaseFragmentActivity;

/**
 * 代理商——管理——我提交的创意
 * 
 * @author wlj
 * 
 */
public class GuanLi_8_MyChuangYi extends BaseFragmentActivity implements OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailishang_8_mychuangyi);
		init_title();
	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("我提交的创意");

		TextView right = (TextView) findViewById(R.id.right);
		right.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(
				R.drawable.rentou_quan_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
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

		}

	}

}
