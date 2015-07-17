package com.wlj.chuangbaba.activity.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.Main;
import com.wlj.chuangbaba.bean.Banner;
import com.wlj.chuangbaba.web.MsgContext;
import com.wlj.ob.ObserversImageView;
import com.wlj.ui.BaseFragment;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.widget.SwitchViewPagerDemoActivity;

/**
 * 热门质询
 * 
 * @author wlj
 * 
 */
public class ExternalFragment_3_tiantiantejia extends BaseFragment {

	private ObserversImageView tttj_120;
	private ObserversImageView tttj_190;
	private ObserversImageView tttj_tuila80;
	private ObserversImageView tttj_erheyi65;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			Log.e("dd", " isVisible = true;");
			onVisible();
		} else {
			Log.e("dd", "  isVisible = false;");
			onInvisible();
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected void lazyLoad() {
	};

	protected void onInvisible() {
	}

	@Override
	protected int getlayout() {
		return R.layout.activity_main_3_tiantiantejia;
	}

	@Override
	protected void initView(View view) {
		view.setMinimumHeight(LayoutParams.WRAP_CONTENT);
		tttj_120 = (ObserversImageView) view.findViewById(R.id.tttj_120);
		tttj_190 = (ObserversImageView) view.findViewById(R.id.tttj_190);
		tttj_tuila80 = (ObserversImageView) view.findViewById(R.id.tttj_tuila80);
		tttj_erheyi65 = (ObserversImageView) view.findViewById(R.id.tttj_erheyi65);
		
		tttj_120.setOnClickListener(new ProjectClick(getActivity()));
		tttj_190.setOnClickListener(new ProjectClick(getActivity()));
		tttj_tuila80.setOnClickListener(new ProjectClick(getActivity()));
		tttj_erheyi65.setOnClickListener(new ProjectClick(getActivity()));
		ExecutorServices.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				callweb();
			}
		});
	}

	private void callweb() {
		// 天天特价商品
		Message message1_1 = Message.obtain();
		try {
			Map<String, String> paremeter = new HashMap<String, String>();

			paremeter.put(MsgContext.key_page, "1");
			paremeter.put(MsgContext.key_pageSize, "4");
			paremeter.put("type", MsgContext.tuijian_tejia + "");

			BaseList projecttab =( (ChuangBaBaContext)mContext).getProductListByFenleiId(paremeter, true);
			message1_1.what = 11;
			message1_1.obj = projecttab;
			handle.sendMessage(message1_1);
			
		} catch (Exception e) {
			AppException.http(e);// 记录日志
			message1_1.what = -1;
			message1_1.obj = e;
			Main main = (Main)getActivity();
			main.handle.sendMessage(message1_1);
		}
	}

	private Handler handle = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:
				BaseList baselist = (BaseList) msg.obj;
				ImageView[] ImageViews = { tttj_120, tttj_190,tttj_tuila80, tttj_erheyi65 };
				Main main = (Main)getActivity();
				main.initProject(baselist, ImageViews);
				break;
				
			}
			
		};
		
	};
	
}
