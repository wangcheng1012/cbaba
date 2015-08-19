package com.wlj.chuangbaba.activity.lvcailingshou.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;

import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.util.ExpandableAdapter;
import com.wlj.ui.BaseFragment;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.util.UIHelper;
import com.wlj.web.URLs;

public class lvcailingshou_Fragment extends BaseFragment   {

	private ExpandableListView expandable_list_view;
	private LinkedList<Base> groupArray;
    private LinkedList<List<Base>> childArray;
	@Override
	protected int getlayout() {
		
		return R.layout.fragment_expandablelistview;
	}

	@Override
	protected void initView(View view) {
		view.setMinimumHeight(20);
		groupArray = new LinkedList<Base>();
		childArray = new LinkedList<List<Base>>();
		expandable_list_view = (ExpandableListView)view.findViewById(R.id.expandable_list_view);
		
		callweb();
		
	}

	private void callweb() {
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				
				Message obtain = Message.obtain();
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("url", URLs.lvcaiFenleiList);
					map.put("bujiami", "");
					
					obtain.what = 1;
					obtain.obj =((ChuangBaBaContext)mContext).Request(getActivity(),map, new Group());
					
				} catch (Exception e) {
					if(Log.LOG)e.printStackTrace();
					
					obtain.what = -1;
					obtain.obj = e;
				}
				handle.sendMessage(obtain);
				
			}
		});
		
	}
	
	private Handler  handle = new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				BaseList b = (BaseList)msg.obj;
				List<Base> list = b.getBaselist();
				
//				LinkedList<Map<String, String>>
				for (int i = 0; i < list.size(); i++) {
					
					Group project = (Group)list.get(i);
					
					groupArray.add(project);
					
					final int index = i;
					ExecutorServices.getExecutorService().execute(new Runnable() {
						
						@Override
						public void run() {
							
							
							Message obtain = Message.obtain();
							try {
								Map<String, Object> map = new HashMap<String, Object>();
								
								map.put("url", URLs.getProductListByFenleiId);
								map.put("bujiami", "");
								
								BaseList list = ((ChuangBaBaContext)mContext).Request(getActivity(),map, new Project());
								 List<Base> ls = list.getBaselist();
								 childArray.add(index,ls);
								
							} catch (Exception e) {
								if(Log.LOG)e.printStackTrace();
								
								obtain.what = -1;
								obtain.obj = e;
							}
							handle.sendMessage(obtain);
						}
					});
				}
				
				
				expandable_list_view.setAdapter(new ExpandableAdapter(getActivity(), groupArray, childArray));
				break;
		
			case -1:
				Exception exception = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, exception.getMessage());
				break;

			}
			
			
		};
	};
	
}
