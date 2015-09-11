package com.wlj.chuangbaba.activity.lvcailingshou.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import com.wlj.chuangbaba.activity.lvcailingshou.DataPass;
import com.wlj.chuangbaba.activity.lvcailingshou.LvCaiLingShou;
import com.wlj.chuangbaba.activity.lvcailingshou.LvCaiLingShouErJi;
import com.wlj.chuangbaba.activity.projectxiangqing.FenLei_xiangqing;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.DpAndPx;
import com.wlj.util.Log;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;
import static com.wlj.chuangbaba.web.MsgContext.*;

public class LvCaiLingShouContent extends BaseRefreshFragment {

	private DataPass datapass; 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		datapass =  (DataPass)activity;
	}
	@Override
	public void initCommonAdapter( List<Base> listDate2) {

		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,R.layout.item_textview_black) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				Project item = (Project)base;
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
				
				Intent intent = new Intent(mContext,FenLei_xiangqing.class);
				intent.putExtra("project",(Project)view.getTag(R.id.tag_first));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
				
		});
		
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		String id = datapass.getGoup().getId();
		Log.i("dd",((Group)datapass.getGoup()).getName());
		Log.i("dd",id );
		
		map.put("url", URLs.getProductListByFenleiId);
		map.put("fenleiid",id);
		map.put(key_page, pageIndex2);
		map.put("bujiami", "");
		map.put("cachekey", id);
		
		return ((ChuangBaBaContext)mContext).getHaveCacheBaseList(getActivity(),new Project(),map,isRefresh);
	}
	
}
