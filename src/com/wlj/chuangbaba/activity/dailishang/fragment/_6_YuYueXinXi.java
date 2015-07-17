package com.wlj.chuangbaba.activity.dailishang.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dady.yoyo.activity.DetailActivity;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.YuYue;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.DpAndPx;
import com.wlj.util.UIHelper;
import com.wlj.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 代理商——管理——回复问题
 * 
 * @author wlj
 * 
 */
public class _6_YuYueXinXi extends BaseRefreshFragment {

	@Override
	public void initCommonAdapter(final List<Base> listDate2) {

		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,
				R.layout.item_6_yuyuexinxi) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				YuYue item = (YuYue) base;
				// 类似getview中处理
				viewHolder.setText(R.id.yuyuePhone, item.getYuyuePhone());
				viewHolder.setText(R.id.yuyueTime, item.getYuyueTime());
				viewHolder.setText(R.id.yuyueOrder, item.getOrderId());
				viewHolder.setText(R.id.yuyuePosition, item.getYuyuePosition());

				return null;
			}
		};
		lv_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view == listview_footer) {
					return;
				}
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra("yuyue", (YuYue) view.getTag(R.id.tag_first));
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
		// TODO Auto-generated method stub
		return ((ChuangBaBaContext) mContext).getYuYueList(new YuYue(),
				pageIndex, isRefresh);
	}

}
