package com.wlj.chuangbaba.activity.projectxiangqing.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.MyMap;
import com.wlj.chuangbaba.activity.personal.OrderActivity;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragment;
import com.wlj.util.ExecutorServices;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.web.URLs;
import com.wlj.widget.PullToRefreshListView;

public class Project_GuiGe extends BaseFragment implements OnClickListener {
	View view;// infalte的布局
	PullToRefreshListView lv_center;
	private TextView caizhi_putong, caizhi_LOW_E,caizhi_dumo, guige_595, guige_696, guige_6126, cankaobaojia;
	
	private TextView fujinmendian;
	private TextView color_4d;
	private TextView color_fss;
	private TextView color_ft;
	private TextView color_hhl;
	private TextView color_hsm;
	private TextView color_jxm;
	private TextView color_kfy;
	private TextView color_sgy;
	private TextView color_sql;
	private TextView color_ytm;

	private TextView menchuang_men;
	private TextView menchuang_chuang;
	
	private String caizhi;
	private String guige;
	private String color;
	private String menchuang;
	private Project project;
	
	private Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 3:
			
				UIHelper.ToastMessage(mContext, "加入订单成功，请联系卖家确认合同金额");
				Intent intent = new Intent(mContext,OrderActivity.class);
				intent.putExtra("type", OrderActivity.huiyuan);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			
				break;
			case 2:
				UIHelper.ToastMessage(mContext, "收藏成功");
				
				break;
			case 1:
				String cankaoprice = (String)msg.obj;
				Double cankaoPrice = MathUtil.parseDouble(cankaoprice);
				DecimalFormat df = new DecimalFormat("0.00"); 
				cankaobaojia.setText("￥"+df.format(cankaoPrice/100)+"元/㎡");
				break;
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				cankaobaojia.setText("￥0.00 元/㎡");
				break;
			}
			
		};
	};
	
	@Override
	protected int getlayout() {
		return R.layout.fragment_project_guige;
	}

	@Override
	protected void initView(View view) {
		view.setMinimumHeight(20);
		
		Bundle bundle = getArguments();
		project = (Project) bundle.getSerializable("base");
		if(project == null){
			UIHelper.ToastMessage(mContext, "数据为空");
			getActivity().finish();
		}
		cankaobaojia = (TextView) view.findViewById(R.id.cankaobaojia);
		Double cankaoPrice = MathUtil.parseDouble(project.getCankaoPrice());
		DecimalFormat df = new DecimalFormat("0.00"); 
		
		cankaobaojia.setText("￥"+df.format(cankaoPrice/100)+"元/㎡");
		// 玻璃材质
		caizhi_putong = (TextView) view.findViewById(R.id.caizhi_putong);
		caizhi_LOW_E = (TextView) view.findViewById(R.id.caizhi_LOW_E);
		caizhi_dumo = (TextView) view.findViewById(R.id.caizhi_dumo);
		List<View> listcaizhi = new ArrayList<View>();
		listcaizhi.add(caizhi_putong);
		listcaizhi.add(caizhi_LOW_E);
		listcaizhi.add(caizhi_dumo);
		caizhi_putong.setOnClickListener(new GuiGeOnClick(listcaizhi,"caizhi"));
		caizhi_LOW_E.setOnClickListener(new GuiGeOnClick(listcaizhi,"caizhi"));
		caizhi_dumo.setOnClickListener(new GuiGeOnClick(listcaizhi,"caizhi"));
		// 玻璃规格
		guige_595 = (TextView) view.findViewById(R.id.guige_595);
		guige_696 = (TextView) view.findViewById(R.id.guige_696);
		guige_6126 = (TextView) view.findViewById(R.id.guige_6126);
		List<View> guige = new ArrayList<View>();
		guige.add(guige_595);
		guige.add(guige_696);
		guige.add(guige_6126);
		guige_595.setOnClickListener(new GuiGeOnClick(guige,"guige"));
		guige_696.setOnClickListener(new GuiGeOnClick(guige,"guige"));
		guige_6126.setOnClickListener(new GuiGeOnClick(guige,"guige"));
		
		if(project.isIsmenchuang()){
			//门窗
			menchuang_chuang = (TextView) view.findViewById(R.id.menchuang_chuang);
			menchuang_men = (TextView) view.findViewById(R.id.menchuang_men);
			List<View> mc = new ArrayList<View>();
			mc.add(menchuang_chuang);
			mc.add(menchuang_men);
			menchuang_chuang.setOnClickListener(new GuiGeOnClick(mc,"menchuang"));
			menchuang_men.setOnClickListener(new GuiGeOnClick(mc,"menchuang"));
		}else{
			view.findViewById(R.id.menchuang).setVisibility(View.GONE);
		}
		
		//颜色
		color_4d = (TextView) view.findViewById(R.id.color_4d);
		color_fss = (TextView) view.findViewById(R.id.color_fss);
		color_ft = (TextView) view.findViewById(R.id.color_ft);
		color_hhl = (TextView) view.findViewById(R.id.color_hhl);
		color_hsm = (TextView) view.findViewById(R.id.color_hsm);
		color_jxm = (TextView) view.findViewById(R.id.color_jxm);
		color_kfy = (TextView) view.findViewById(R.id.color_kfy);
		color_sgy = (TextView) view.findViewById(R.id.color_sgy);
		color_sql = (TextView) view.findViewById(R.id.color_sql);
		color_ytm = (TextView) view.findViewById(R.id.color_ytm);
		
		List<TextView> color = new ArrayList<TextView>();
		color.add(color_4d);
		color.add(color_fss);
		color.add(color_ft );
		color.add(color_hhl);
		color.add(color_hsm);
		color.add(color_jxm);
		color.add(color_kfy);
		color.add(color_sgy);
		color.add(color_sql);
		color.add(color_ytm);
		
		color_4d.setOnClickListener(new colorOnClick(color));
		color_fss.setOnClickListener(new colorOnClick(color));
		color_ft.setOnClickListener(new colorOnClick(color));
		color_hhl.setOnClickListener(new colorOnClick(color));
		color_hsm.setOnClickListener(new colorOnClick(color));
		color_jxm.setOnClickListener(new colorOnClick(color));
		color_kfy.setOnClickListener(new colorOnClick(color));
		color_sgy.setOnClickListener(new colorOnClick(color));
		color_sql.setOnClickListener(new colorOnClick(color));
		color_ytm.setOnClickListener(new colorOnClick(color));
		
		// 附近门店
		fujinmendian = (TextView) view.findViewById(R.id.fujinmendian);
		view.findViewById(R.id.joinSouCang).setOnClickListener(this);;
		view.findViewById(R.id.joinOrder).setOnClickListener(this);;
		fujinmendian.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fujinmendian:
			Intent intent = new Intent(mContext, MyMap.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.joinSouCang:
			ExecutorServices.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					Message obtain = Message.obtain();
					try {
						Map<String, Object>  map= new HashMap<String, Object>();
						map.put("pubId", project.getId());
						map.put("user_Type", User.type_huiyuan);
						map.put("url", URLs.shoucang);
						
						((ChuangBaBaContext)mContext).Request(getActivity(),map,null);
						obtain.what = 2;
					} catch (Exception e) {
						obtain.what = -1;
						obtain.obj = e;
					}
					handler.sendMessage(obtain);
				}
			});
			break;
		case R.id.joinOrder:
			ExecutorServices.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					Message obtain = Message.obtain();
					try {
						Map<String, Object>  map= new HashMap<String, Object>();
						String orderId = ("" + System.currentTimeMillis()).substring(1);
						
						map.put("productId", project.getId());
						map.put("orderId", orderId );
						
						map.put("user_Type", User.type_huiyuan);
						map.put("url", URLs.createOrder);
						
						((ChuangBaBaContext)mContext).Request(getActivity(),map,null);
						obtain.what = 3;
					} catch (Exception e) {
						obtain.what = -1;
						obtain.obj = e;
					}
					handler.sendMessage(obtain);
				}
			});
			break;
		
		}
	}

	class GuiGeOnClick implements OnClickListener {
		
		private List<View> list = null;
		private String strs;
		public GuiGeOnClick(List<View> list ,String strs) {
			this.list = list;
			this.strs = strs;
		}
		
		@Override
		public void onClick(View v) {
			
			if (list != null) {
				for (View view : list) {
					view.setBackgroundResource(R.drawable.hui_white);
				}
				v.setBackgroundResource(R.drawable.red_red);
				if("caizhi".equals(strs)){
					caizhi= ((TextView)v).getText()+"";
				}else if("guige".equals(strs)){
					guige= ((TextView)v).getText()+"";
				}else if("menchuang".equals(strs)){
					menchuang= ((TextView)v).getText()+"";
					menchuang = menchuang.substring(1,2);
				}
				callWeb();
			}
			
		}
		
	}

	class colorOnClick implements OnClickListener {

		private List<TextView> list = null;

		public colorOnClick(List<TextView> list) {
			this.list = list;
		}

		@Override
		public void onClick(View vie) {
			TextView v = (TextView)vie;
			if (list != null) {
				for (TextView view : list) {
					view.setTextColor(Color.BLACK);
				}
				v.setTextColor(getResources().getColor(R.color.red_f27e17));
				color = ((TextView)v).getText()+"";
				callWeb();
			}

		}

	}
	
	public void callWeb() {

		if (!StringUtils.isnull(caizhi)
				&& !StringUtils.isnull(guige)
				&& !StringUtils.isnull(color)
				&& (project.isIsmenchuang() ? !StringUtils.isnull(menchuang) : true)) {

			ExecutorServices.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					HashMap<String, String> map = new HashMap<String, String>();

					map.put("productId", project.getId());
					map.put("biaomian", color);
					map.put("guige", guige);
					map.put("caizhi", caizhi);
					if(project.isIsmenchuang()){
						map.put("fangdaoshachuang", menchuang.trim());
					}
					Message message = new Message();

					try {
						String cankaobaojia = ((ChuangBaBaContext) mContext)
								.getCanKaoPrice(map);
						message.what = 1;
						message.obj = cankaobaojia;
					} catch (Exception e) {
						message.what = -1;
						message.obj = e;
					}
					handler.sendMessage(message);
				}
			});
		}

	}

}
