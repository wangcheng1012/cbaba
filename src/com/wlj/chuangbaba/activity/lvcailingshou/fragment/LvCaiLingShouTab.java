package com.wlj.chuangbaba.activity.lvcailingshou.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.wlj.chuangbaba.activity.lvcailingshou.LvCaiLingShou;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.DpAndPx;
import com.wlj.util.StringUtils;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;
import static com.wlj.chuangbaba.web.MsgContext.*;

public class LvCaiLingShouTab extends BaseRefreshFragment {

	@Override
	public void initCommonAdapter( List<Base> listDate2) {
//		view.setMinimumWidth(DpAndPx.dpToPx(mContext, 240));
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
				LvCaiLingShou lvCaiLingShou = (LvCaiLingShou)getActivity();
				LvCaiLingShouContent lvCaiLingShouContent = new LvCaiLingShouContent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("base", (Group)view.getTag(R.id.tag_first));
				lvCaiLingShouContent.setArguments(bundle);
				
				lvCaiLingShou.changeFragment(lvCaiLingShouContent, position);
				
				
				lvCaiLingShou.drawerlayout.closeDrawers();
				
//				Intent intent = new Intent(Fragment_LvCaiLingShouTab.this.mContext,DetailActivity.class);
//				intent.putExtra("hot",(Hot)view.getTag(R.id.tag_first));
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
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
		
//		String typeid = bundle.getString("typeid");
//		return  ((ChuangBaBaContext)mContext).getPubListByFeileiId(new Hot(), typeid, pageIndex, isRefresh);
	}
	
}
