package com.wlj.chuangbaba.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wlj.chuangbaba.MyBaseMoreFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;
import com.wlj.chuangbaba.activity.personal.fragment.Order_AllOrder;
import com.wlj.chuangbaba.activity.personal.fragment.Order_orderQuery;
import com.wlj.chuangbaba.bean.Order;

public class OrderActivity extends MyBaseMoreFragmentActivity implements OnClickListener  {

	private TextView allOrder; 
	private TextView weiZhiFu; 
	private TextView yiZhiFu; 
	private TextView yiComplete; 
	private TextView orderQuery; 
	
	private Fragment personalOrder_AllOrder;
	private Fragment personalOrder_weiZhiFu;
	private Fragment personalOrder_yiZhiFu;
	private Fragment personalOrder_yiComplete;
	private Fragment personalOrder_orderQuery;
	
	public final static int dailishang = 1;
	public final static int huiyuan = 2;
	private int type ;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		type = intent.getIntExtra("type", 0);
		allOrder.performClick();
	}

	@Override
	public void onClick(View v) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		switch (v.getId()) {
		case R.id.allOrder:
			if(oldposition != 0 ){
				movecursor(0);
				if(personalOrder_AllOrder == null){
					personalOrder_AllOrder = new Order_AllOrder();
					bundle.putInt("state", 0);
					personalOrder_AllOrder.setArguments(bundle);
				}
				changeFragment(personalOrder_AllOrder,0);
			}
			break; 
		case R.id.weiZhiFu:
			if(oldposition != 1){
				movecursor(1);
				if(personalOrder_weiZhiFu == null){
					
					personalOrder_weiZhiFu = new Order_AllOrder();
					bundle.putInt("state", Order.STATE_create);
					personalOrder_weiZhiFu.setArguments(bundle);
				}
				changeFragment(personalOrder_weiZhiFu,1);
			}
			break;
		case R.id.yiZhiFu:
			if(oldposition != 2){
				
				movecursor(2);
				if(personalOrder_yiZhiFu == null){
					
					personalOrder_yiZhiFu = new Order_AllOrder();
					bundle.putInt("state", Order.STATE_paying);
					personalOrder_yiZhiFu.setArguments(bundle);
				}
				changeFragment(personalOrder_yiZhiFu,2);
			}
			break;
		case R.id.yiComplete:
			if(oldposition != 3){
				
				movecursor(3);
				if(personalOrder_yiComplete == null){
					
					personalOrder_yiComplete = new Order_AllOrder();
					bundle.putInt("state", Order.STATE_pay_ok);
					personalOrder_yiComplete.setArguments(bundle);
				}
				changeFragment(personalOrder_yiComplete,3);
			}
			
			break;
		case R.id.orderQuery:
//			if(oldposition != 4){
//				
//				movecursor(4);
//				if(personalOrder_orderQuery == null){
//					
//					personalOrder_orderQuery = new Order_orderQuery();
//					personalOrder_orderQuery.setArguments(bundle);
//				}
//				changeFragment(personalOrder_orderQuery,4);
//			}
			break;
		}
	}

	@Override
	protected void beforeTitle() {
		title.setText("订单中心");
	}

	@Override
	protected int setlayout() {
		return R.layout.fragment_persomal;
	}

	@Override
	protected void initView() {
		
		allOrder = (TextView)findViewById(R.id.allOrder);
		weiZhiFu = (TextView)findViewById(R.id.weiZhiFu);
		yiZhiFu = (TextView)findViewById(R.id.yiZhiFu);
		yiComplete = (TextView)findViewById(R.id.yiComplete);
		orderQuery = (TextView)findViewById(R.id.orderQuery);
		
		allOrder.setOnClickListener(this);
		weiZhiFu.setOnClickListener(this);
		yiZhiFu.setOnClickListener(this);
		yiComplete.setOnClickListener(this);
		orderQuery.setOnClickListener(this);		
	}

	@Override
	protected void Switch(Message msg) {
	}

	@Override
	protected void setViewDate(Object obj) {
	}

	@Override
	protected void rightOnClick() {
		Class<?> cls = null;
		if(type == huiyuan){
			cls = Personal_Center.class;
		}else if (type == dailishang){
			cls = DaiLiShang_GuanLi.class;
		}
		Intent right = new Intent(getApplicationContext(),cls);
		right.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(right);
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		return null;
	}
	
}
