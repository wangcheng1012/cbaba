package com.dady.yoyo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wlj.chuangbaba.R;

public class Fragment_4 extends Fragment implements OnClickListener {
	Context mContext;
	View view;// infalte的布局
	LinearLayout myContainer;// 新建容器

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			myContainer = new LinearLayout(mContext);
			view = inflater.inflate(R.layout.fragment_4, null);
			view.setMinimumHeight(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getHeight());
			view.setMinimumWidth(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getWidth());
			initView();
			myContainer.addView(view);
		} else {
			myContainer.removeAllViews();
			myContainer = new LinearLayout(getActivity());
			myContainer.addView(view);
		}
		return myContainer;
	}

	private void initView() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

}
