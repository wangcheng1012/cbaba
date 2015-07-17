package com.wlj.chuangbaba.activity.personal.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dady.yoyo.activity.DetailActivity;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.dady.yoyo.fragment.Fragment_Hot;
import com.wlj.bean.Base;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Order_orderQuery extends Fragment implements OnClickListener {
	Context mContext;
	View view;// infalte的布局
	ListView lv_center;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (null == view) {
			view = inflater.inflate(R.layout.fragment_7, null);
			view.setMinimumHeight(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getHeight());
			view.setMinimumWidth(((WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getWidth());
			initView(view);
		}else{
			FrameLayout viewParent = (FrameLayout)view.getParent();
			viewParent.removeAllViews();
			viewParent = new FrameLayout(mContext);
			viewParent.addView(view);
			return viewParent;
		}
		return view;
	}

	private void initView(View view) {
		List<Base> list=new ArrayList<Base>();
		for(int i=0;i<10;i++){
			Base item=new User();
			// TODO 需重写加载user数据
			list.add(item);
		}
		lv_center=(ListView) view.findViewById(R.id.lv_center);
		lv_center.setAdapter(new CommonAdapter<Base>(mContext,list,R.layout.item_allorder) {

			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base item, int position, ViewGroup parent) {
				// TODO getListItemview
				//类似getview中处理
				User u =(User)item;
//				viewHolder.setText(R.id.tv_title, item.title);//设置title
//				viewHolder.setText(R.id.tv_date, item.date);
//				viewHolder.setText(R.id.tv_content, item.content);
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(mContext, DetailActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});
				return null;
			}
		});
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}
}
