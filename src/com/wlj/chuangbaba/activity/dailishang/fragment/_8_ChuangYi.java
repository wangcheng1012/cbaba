package com.wlj.chuangbaba.activity.dailishang.fragment;

import static com.wlj.chuangbaba.web.MsgContext.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wlj.chuangbaba.bean.ChuangYi;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.bean.YuYue;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.DpAndPx;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.chuangbaba.web.URLs;
import com.wlj.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 代理商——管理——回复问题
 * 
 * @author wlj
 * 
 */
public class _8_ChuangYi extends BaseRefreshFragment {

	@Override
	public void initCommonAdapter(final List<Base> listDate2) {

		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,
				R.layout.item_8_chuangyi) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				ChuangYi item = (ChuangYi) base;
				// 类似getview中处理
				viewHolder.setText(R.id.name, item.getName());
				viewHolder.setText(R.id.content, item.getContent());
				viewHolder.setText(R.id.time, StringUtils.getTime(MathUtil.parseLong(item.getTime()), "yyyy/M/d"));

				return null;
			}
		};
		lv_center.setDivider(getResources().getDrawable(R.color.huise_F2F2F2));
		lv_center.setHeaderDividersEnabled(false);
//		lv_center.setDividerHeight(DpAndPx.dpToPx(mContext, 10));

	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", URLs.chuangyilist);
		map.put("user_Type", User.type_dailishang);
		map.put("cachekey", pageIndex2+"");
		map.put(key_page, pageIndex2);
		
		return ((ChuangBaBaContext) mContext).getHaveCacheBaseList(getActivity(), new ChuangYi(), map, isRefresh);
	}

}
