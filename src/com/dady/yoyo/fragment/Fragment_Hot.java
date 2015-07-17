package com.dady.yoyo.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.dady.yoyo.activity.DetailActivity;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.StringUtils;
import com.wlj.util.img.LoadImage;
import com.wlj.web.URLs;

public class Fragment_Hot extends BaseRefreshFragment {

	@Override
	public void initCommonAdapter( List<Base> listDate2) {
		
		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,R.layout.inflater_hot) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				Hot item = (Hot)base;
				// 类似getview中处理
				viewHolder.setText(R.id.tv_title, item.getTitle());// 设置title
				String time = StringUtils.getTime(StringUtils.toLong(item.getTime()), "yyyy年MM月dd日");
				viewHolder.setText(R.id.tv_date, time);
				viewHolder.setText(R.id.tv_content, new String(Base64.decode(item.getJianjie(), Base64.DEFAULT)));
				
				ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
				iv_icon.setVisibility(View.INVISIBLE);
				String picurl = item.getItempic();
				if("".equals(picurl.trim())){
				}else{
					LoadImage.getinstall().addTask(URLs.HOST+picurl,iv_icon );
//					LoadImage.getinstall().doTask(); 
				}
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
				Intent intent = new Intent(Fragment_Hot.this.mContext,DetailActivity.class);
				intent.putExtra("hot",(Hot)view.getTag(R.id.tag_first));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
				
		});
		
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception{
		String typeid = bundle.getString("typeid");
		return  ((ChuangBaBaContext)mContext).getPubListByFeileiId(new Hot(), typeid, pageIndex, isRefresh);
	}

}
