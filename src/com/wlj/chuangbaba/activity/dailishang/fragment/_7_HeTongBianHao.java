package com.wlj.chuangbaba.activity.dailishang.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dady.yoyo.activity.DetailActivity;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.HeTong;
import com.wlj.chuangbaba.bean.YuYue;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.DpAndPx;

/**
 * 代理商——管理——合同编号
 * 
 * @author wlj
 * 
 */
public class _7_HeTongBianHao extends BaseRefreshFragment {

	@Override
	public void initCommonAdapter(List<Base> listDate2) {
		
		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,R.layout.item_7_hetongbianhao) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				HeTong item = (HeTong)base;
				// 类似getview中处理
				viewHolder.setText(R.id.htbianhao, item.getBianhao());
				viewHolder.setText(R.id.time, item.getTime());
				viewHolder.setText(R.id.htmoney, item.getMoney());
				viewHolder.setText(R.id.phone, item.getMoney());
				viewHolder.setText(R.id.ddbianhao, item.getOrderbianhao());
				viewHolder.setText(R.id.beizhu, item.getBeizhu());
				viewHolder.setText(R.id.state, item.getState());
				
				return null;
			}
		};
		lv_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(view == listview_footer){
					return;
				}
				Intent intent = new Intent(mContext,DetailActivity.class);
				intent.putExtra("yuyue",(YuYue)view.getTag(R.id.tag_first));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
				
		});
		lv_center.setDivider(getResources().getDrawable(R.color.huise_F2F2F2));
		lv_center.setHeaderDividersEnabled(false);
		lv_center.setDividerHeight(DpAndPx.dpToPx(mContext, 10));
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception {
		return ((ChuangBaBaContext)mContext).getYuYueList(new YuYue(), pageIndex, isRefresh);
	}

}
