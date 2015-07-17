package com.wlj.chuangbaba.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.ui.BaseFragmentActivity;

public class JieShaoPengYou extends BaseFragmentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jieshaopengyou);
		
		init_title();
		init_view();
	}

	private void init_view() {
		TextView text = (TextView)findViewById(R.id.textView1);
		String formattedText = getString(R.string.htmlFormattedText);
		
		text.setText(Html.fromHtml(formattedText));
		
		
	}
	
	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("近期活动");

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
			Intent right = new Intent(getApplicationContext(),Personal_Center.class);
			startActivity(right);
			break;

		}
	}
}
