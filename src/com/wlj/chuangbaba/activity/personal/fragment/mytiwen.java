package com.wlj.chuangbaba.activity.personal.fragment;

import static com.wlj.chuangbaba.web.MsgContext.key_page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.wenda.WenDa_xiangqing;
import com.wlj.chuangbaba.bean.Answer;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;

public class mytiwen extends BaseRefreshFragment  {

	@Override
	public void initCommonAdapter(List<Base> listDate2) {
		
		commonAdapter = new CommonAdapter<Base>(mContext,listDate2,R.layout.item_mytiwen) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,Base item, int position, ViewGroup parent) {
				Wen wen =  (Wen)item;
				viewHolder.setText(R.id.time,StringUtils.getTime(MathUtil.parseLong(wen.getTime())) );
				viewHolder.setText(R.id.wenti, wen.getWenti());
				viewHolder.setText(R.id.answernum, "问答数："+wen.getAnswercount()+"条");
				viewHolder.setText(R.id.wentisatte, "审核状态："+wen.getState());
				
				view.setTag(R.id.tag_first,item);
				
				ImageView tiwenpic = (ImageView)view.findViewById(R.id.tiwenpic);
				String pic1 = wen.getPic1();
				tiwenpic.setVisibility(View.INVISIBLE);
				if(pic1 != null && !"".equals(pic1.trim())){
					LoadImage.getinstall().addTask(URLs.HOST+pic1, tiwenpic);
				}
				
				return null;
			}
		};
		
//		lv_center.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				Intent xiangqing = new Intent(mContext, WenDa_xiangqing.class);
//				xiangqing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				xiangqing.putExtra("base",(Base) view.getTag(R.id.tag_first));
//				startActivity(xiangqing);
//			}
//		});
		
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,int pageIndex2, boolean isRefresh) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key_page, pageIndex2+"");
		map.put("url", URLs.tiwenList);
		map.put("my", "true");
		return ((ChuangBaBaContext)mContext).getHaveCacheBaseList(getActivity(),new Wen(),map,isRefresh);
	}

}
