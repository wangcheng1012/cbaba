package com.wlj.chuangbaba.activity;

import static com.wlj.chuangbaba.web.MsgContext.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.HuiYuanLogin;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.bean.DaiLiShangDian;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.AppConfig;
import com.wlj.util.ExecutorServices;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;

public class ZhuanMaiDian extends MyBaseFragmentActivity {

	private GridView gridview1;
	List<Base> list1;
	
	
	@Override
	protected void setViewDate(Object obj) {
		if (obj == null) {
			return;
		}
		BaseList baselist = (BaseList) obj;
		list1 = baselist.getBaselist();
		
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {

				Map<String,Object> map = new HashMap<String, Object>();
				String city = getIntent().getStringExtra("city");
				map.put("city", city);
				map.put(key_page, 1);
				map.put(key_pageSize, 500);
				map.put("url", URLs.dailishangList);
				map.put("bujiami","");
				
				Message obtain = Message.obtain();
				try {
					obtain.what = 2;
					obtain.obj = mContext.Request(ZhuanMaiDian.this,map, new DaiLiShangDian());
				} catch (Exception e) {
					obtain.what = -1;
					obtain.obj = e;
				}
				handle.sendMessage(obtain);
				
			}
		});
	
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		LoadImage.getinstall().doTask();
	}

	@Override
	protected void rightOnClick() {
		Intent my_bottom = new Intent(getApplicationContext(), Personal_Center.class);
		my_bottom.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(my_bottom);
	}

	@Override
	protected void beforeTitle() {
		title.setText("代理商中心");
	}

	@Override
	protected int setlayout() {
		return R.layout.zhuangmaidian;
	}

	@Override
	protected void initView() {

		gridview1 = (GridView) findViewById(R.id.gridview1);
		callweb();
	}

	@Override
	protected void Switch(Message msg) {
		if(msg.what == 2){
			Object obj = msg.obj;
			if(obj == null){
				return;
			}
			String id1 = "0";
			if(list1 != null && list1.size() > 0 ){
				id1 = list1.get(0).getId();
			}
			List<Base> list2 = ((BaseList)obj).getBaselist();
			for (Base base : list2) {
				String id2 = base.getId();
				if(id1.equals(id2)){
					list2.remove(base);
					break;
				}
			}
			list2.addAll(0, list1);
			
			gridview1.setAdapter(new CommonAdapter<Base>(mContext, list2,R.layout.item_zhuanmaidian) {
				@Override
				public View getListItemview(ViewHolder viewHolder, View view,
						Base item, int position, ViewGroup parent) {
					com.wlj.chuangbaba.bean.DaiLiShangDian zhuanMaiDian = (com.wlj.chuangbaba.bean.DaiLiShangDian) item;
					viewHolder.setText(R.id.name, zhuanMaiDian.getName());
					viewHolder.setText(R.id.addr, "地址:"+zhuanMaiDian.getAddr());
					viewHolder.setText(R.id.phone,"电话:"+zhuanMaiDian.getPhone());
					ImageView pic = (ImageView) view.findViewById(R.id.pic);
					pic.setVisibility(View.INVISIBLE);
					String picstr = zhuanMaiDian.getPic();
					if (!"".equals(picstr)) {
						LoadImage.getinstall().addTask(URLs.HOST + picstr, pic);
						LoadImage.getinstall().doTask();
					}
					view.setTag(R.id.tag_first, zhuanMaiDian);
					return null;
				}
			});

			gridview1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent2 = new Intent(mContext,MyMap.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent2.putExtra("DaiLiShangDian", (DaiLiShangDian)view.getTag(R.id.tag_first));
					startActivity(intent2);
				}
			});
		}
		
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		map.put("url", URLs.dailishangList);
		map.put("bujiami","");
		map.put("tanzikou","true");
		
		map.put(key_page,"1");
		map.put(key_pageSize,"1");
		
		return mContext.Request(this,map, new DaiLiShangDian());
	}

}
