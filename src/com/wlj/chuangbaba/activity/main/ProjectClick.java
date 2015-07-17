package com.wlj.chuangbaba.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.projectxiangqing.FenLei_xiangqing;
import com.wlj.chuangbaba.bean.Project;

/**
 * 产品点击
 * 
 * @author wlj
 */
public class ProjectClick implements OnClickListener {
	private Activity mContext;

	public ProjectClick(Activity mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(mContext, FenLei_xiangqing.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("project", (Project) arg0.getTag(R.id.tag_first));
		mContext.startActivity(intent);
	}

}