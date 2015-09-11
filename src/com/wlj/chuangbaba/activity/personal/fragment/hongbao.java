package com.wlj.chuangbaba.activity.personal.fragment;

import static com.wlj.chuangbaba.web.MsgContext.key_page;
import static com.wlj.chuangbaba.web.MsgContext.key_pageSize;

import java.text.DecimalFormat;
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
import com.wlj.chuangbaba.bean.DaiJinQuan;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;

public class hongbao extends BaseRefreshFragment  {

	private DecimalFormat df;
	
	@Override
	public void onResume() {
		super.onResume();
		df = new DecimalFormat("0.00"); 
	}
	
	@Override
	public void initCommonAdapter(List<Base> listDate2) {
		
		commonAdapter = new CommonAdapter<Base>(mContext,listDate2,R.layout.item_p_4_hongbao) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,Base item, int position, ViewGroup parent) {
				DaiJinQuan wen =  (DaiJinQuan)item;
				
				Double money = MathUtil.parseDouble(wen.getMoney());
				
				viewHolder.setText(R.id.youxiaoqi,"有效期:"+StringUtils.getTime(MathUtil.parseLong(wen.getStartTime()),"yyyy/m/d")+"—"+
						StringUtils.getTime(MathUtil.parseLong(wen.getEndTime()),"yyyy/m/d"));
				viewHolder.setText(R.id.money, "金额："+"￥"+df.format(money/100)+"元");
				viewHolder.setText(R.id.name, wen.getTitle());
//				
				view.setTag(R.id.tag_first,item);
				
//				ImageView tiwenpic = (ImageView)view.findViewById(R.id.youhuiquan);
//				String pic1 = wen.getPic();
//				tiwenpic.setVisibility(View.INVISIBLE);
//				if(pic1 != null && !"".equals(pic1.trim())){
//					LoadImage.getinstall().addTask(URLs.HOST+pic1, tiwenpic);
//				}
				
				return null;
			}
		};
		
	}

	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("state", bundle.getInt("state") + "");

		map.put(key_page, "1");
		map.put(key_pageSize, "100");
		map.put("user_Type", User.type_huiyuan);
		map.put("url", URLs.list_daijinquan);

		BaseList list = ((ChuangBaBaContext) mContext).Request(getActivity(),map, new DaiJinQuan());
		return list;
	}

}
