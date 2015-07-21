package com.wlj.chuangbaba.activity.lvcailingshou.fragment;

import android.view.View;
import android.widget.ExpandableListView;

import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.util.ExpandableAdapter;
import com.wlj.ui.BaseFragment;
import com.wlj.util.ExecutorServices;

public class lvcailingshou_Fragment extends BaseFragment   {

	private ExpandableListView expandable_list_view;
	
	@Override
	protected int getlayout() {
		
		return R.layout.fragment_expandablelistview;
	}

	@Override
	protected void initView(View view) {
		view.setMinimumHeight(20);
		
		expandable_list_view = (ExpandableListView)view.findViewById(R.id.expandable_list_view);
		
		callweb();
		
//		expandable_list_view.setAdapter(new ExpandableAdapter(mContext, courseGroupList, childArray2));
	}

	private void callweb() {
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
		
		
		
	}
	
	
}
