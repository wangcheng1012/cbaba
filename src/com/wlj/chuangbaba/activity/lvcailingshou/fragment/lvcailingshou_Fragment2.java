package com.wlj.chuangbaba.activity.lvcailingshou.fragment;

import static com.wlj.chuangbaba.web.MsgContext.key_page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.AdapterView.OnItemClickListener;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.lvcailingshou.LvCaiLingShou;
import com.wlj.chuangbaba.activity.lvcailingshou.LvCaiLingShouErJi;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.util.ExpandableAdapter;
import com.wlj.ui.BaseFragment;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.util.UIHelper;
import com.wlj.chuangbaba.web.URLs;

public class lvcailingshou_Fragment2 extends BaseRefreshFragment   {

	@Override
	public void initCommonAdapter( List<Base> listDate2) {

		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,R.layout.item_textview_black) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				Group item = (Group)base;
				// 类似getview中处理
				viewHolder.setText(R.id.item_text_black, item.getName());// 设置title
				
				view.setTag(R.id.tag_first,item);
				
				return null;
			}
		};
		
		lv_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(view == listview_footer){
					return;
				}
				
				Intent intent = new Intent(lvcailingshou_Fragment2.this.mContext,LvCaiLingShouErJi.class);
				intent.putExtra("base",(Group)view.getTag(R.id.tag_first));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
				
		});
		
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", URLs.lvcaiFenleiList);
		map.put(key_page, pageIndex2);
		map.put("bujiami", "");
		
		return ((ChuangBaBaContext)mContext).getHaveCacheBaseList(getActivity(),new Group(),map,isRefresh);
	}
	
	
}
