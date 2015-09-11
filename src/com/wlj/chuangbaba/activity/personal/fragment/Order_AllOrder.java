package com.wlj.chuangbaba.activity.personal.fragment;

import static com.wlj.chuangbaba.web.MsgContext.key_page;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.pay.PayDemoActivity;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.MyMap;
import com.wlj.chuangbaba.activity.dailishang.GuanLi_2_HeTongUpLoad;
import com.wlj.chuangbaba.activity.personal.OrderActivity;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;


public class Order_AllOrder extends BaseRefreshFragment   {
	
	private DecimalFormat df;
	public final static int payCode = 55;
	public final static int mendianCode = 56;
	public final static int updatehetong = 57;
	
	@Override
	public void onResume() {
		super.onResume();
		df = new DecimalFormat("0.00"); 
	}
	@Override
	public void initCommonAdapter( List<Base> listDate2) {
		commonAdapter = new CommonAdapter<Base>(mContext, listDate2,R.layout.item_allorder) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				
				Order order =(Order)base;
				
				Double cankaoPrice = MathUtil.parseDouble(order.getCankaoPrice());
				
				viewHolder.setText(R.id.orderNo, order.getOrderId()+"");
				viewHolder.setText(R.id.orderstate, order.getStateStr());
				viewHolder.setText(R.id.orderTitle, order.getTitle());//设置title
				viewHolder.setText(R.id.orderModel, order.getChanpinxinghao());
				viewHolder.setText(R.id.orderCanKaoPrice, "￥"+df.format(cankaoPrice/100)+"元/㎡");
				viewHolder.setText(R.id.orderTime, StringUtils.getTime(StringUtils.toLong(order.getTime()),"yyyy-MM-dd") );
				
				ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
				iv_icon.setVisibility(View.INVISIBLE);
				String picurl = order.getPic();
				if("".equals(picurl.trim())){
				}else{
					LoadImage.getinstall().addTask(URLs.HOST+picurl,iv_icon );
				}
				view.setTag(R.id.tag_first,order);
				
				View yuyueSaler = view.findViewById(R.id.yuyueSaler);
				View onlinePay = view.findViewById(R.id.onlinePay);
				Bundle bundle = getArguments();
				if(OrderActivity.huiyuan == bundle.getInt("type")){
					HuiYuan(yuyueSaler, onlinePay, order, view);
				}else if(OrderActivity.dailishang == bundle.getInt("type")){
					Saler(yuyueSaler, onlinePay, order,view);
				}
				return null;
			}
		};
		
		lv_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(view == listview_footer){
					return;
				}
			}
				
		});
	}

	private void Saler(View yuyueSaler,View onlinePay,final Order order,View view){
		
		TextView orderPriceView = (TextView) view.findViewById(R.id.orderPrice);
		
		yuyueSaler.setVisibility(View.GONE);
//		yuyueSaler.setEnabled(false);
//		((TextView)yuyueSaler).setText("合同金额");
//		((TextView)yuyueSaler).setTextColor(getResources().getColor(R.color.black));
//		yuyueSaler.setBackgroundResource(R.color.white);
		
		((TextView)onlinePay).setText("合同上传");
		onlinePay.setEnabled(false);
		onlinePay.setBackgroundResource(R.color.huise_C8C8C8);
		
		if(order.getState() == Order.STATE_create){
			//未支付
			
			//没合同金额 是未上传合同状态
			if("".equals(order.getOrderPrice())){
				onlinePay.setEnabled(true);
				onlinePay.setBackgroundResource(R.color.red_f27e17);
				onlinePay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//上传合同
						Intent intent = new Intent(mContext, GuanLi_2_HeTongUpLoad.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("order", order);
						startActivityForResult(intent, updatehetong);
					}
				});
				
			}
			orderPriceView.setText("");
		}else if(order.getState() == Order.STATE_paying){
			//已支付 首款
			orderPriceView.setText("已支付:￥"+df.format(MathUtil.parseDouble(order.getPriceYizhifu())/100)+"元       待支付:￥"+df.format(MathUtil.parseDouble(order.getDaizhifu())/100)+"元");
			
		}else if(order.getState() == Order.STATE_pay_ok){
			//已完成
			orderPriceView.setText("已支付:￥"+df.format(MathUtil.parseDouble(order.getPriceYizhifu())/100)+"元       待支付:￥"+df.format(MathUtil.parseDouble(order.getDaizhifu())/100)+"元");
		}else{
			
		}
	}
	
	private void HuiYuan(View yuyueSaler,View onlinePay,final Order order,View view){
		
		TextView orderPriceView = (TextView) view.findViewById(R.id.orderPrice);
//		viewHolder.setText(R.id.orderPrice, "￥"+df.format(orderPrice/100)+"元");
		
		if(order.getState() == Order.STATE_create){
			//创建成功
			yuyueSaler.setVisibility(View.VISIBLE);
			yuyueSaler.setEnabled(true);
			yuyueSaler.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,MyMap.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("order", order);
					startActivityForResult(intent,mendianCode);
				}
			});
			//支付按钮
			onlinePay.setEnabled(false);
			onlinePay.setBackgroundResource(R.color.huise_C8C8C8);
			//价格view
			orderPriceView.setText("");
		}else if(order.getState() == Order.STATE_paying){
			//支付中
			
			//隐藏预约按钮
			yuyueSaler.setVisibility(View.GONE);
			yuyueSaler.setEnabled(false);
			//支付按钮
			onlinePay.setEnabled(true);
			onlinePay.setBackgroundResource(R.color.red_f27e17);
			onlinePay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(mContext, PayDemoActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("order", order);
					intent.putExtra("price", MathUtil.parseDouble(df.format(MathUtil.parseDouble(order.getDaizhifu())/100)));
					startActivityForResult(intent, payCode);
				}
			});
			//价格view
			orderPriceView.setText("已支付:￥"+df.format(MathUtil.parseDouble(order.getPriceYizhifu())/100)+"元       待支付:￥"+df.format(MathUtil.parseDouble(order.getDaizhifu())/100)+"元");
			
		}else if(order.getState() == Order.STATE_pay_ok){
			//已完成
			
			//隐藏预约按钮
			yuyueSaler.setVisibility(View.GONE);
			yuyueSaler.setEnabled(false);
			//支付按钮
			onlinePay.setEnabled(false);
			onlinePay.setBackgroundResource(R.color.huise_C8C8C8);
			//价格view
			orderPriceView.setText("已支付:￥"+df.format(MathUtil.parseDouble(order.getPriceYizhifu())/100)+"元       待支付:￥"+df.format(MathUtil.parseDouble(order.getDaizhifu())/100)+"元");
		}else{
			
			//隐藏预约按钮
			yuyueSaler.setVisibility(View.INVISIBLE);
			yuyueSaler.setEnabled(false);
			//支付按钮
			onlinePay.setEnabled(false);
			onlinePay.setBackgroundResource(R.color.huise_C8C8C8);
			//价格view
			orderPriceView.setText("已支付:￥"+df.format(MathUtil.parseDouble(order.getPriceYizhifu())/100)+"元       待支付:￥"+df.format(MathUtil.parseDouble(order.getDaizhifu())/100)+"元");
		}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == resultCode){
			callWeb( 1, UIHelper.LISTVIEW_ACTION_REFRESH);
		}
		
	}
	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,
			int pageIndex2, boolean isRefresh) throws Exception {
		int state = bundle.getInt("state", 0);
		
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put(key_page, pageIndex+"");
		if(state != 0){
			map.put("state", state+"");
		}
		if(OrderActivity.huiyuan == bundle.getInt("type")){
			
			map.put("cachekey", User.type_huiyuan+"_"+state);
			map.put("user_Type", User.type_huiyuan);
			map.put("url", URLs.huiyuanorderList);
		}else if(OrderActivity.dailishang == bundle.getInt("type")){
			
			map.put("cachekey", User.type_dailishang+"_"+ state);
			map.put("user_Type", User.type_dailishang);
			map.put("url", URLs.salerorderList);
		}
		
		return ((ChuangBaBaContext)mContext).getHaveCacheBaseList(getActivity(),new Order(), map, isRefresh);
	}

}
