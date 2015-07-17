package com.dady.yoyo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.ui.BaseFragment;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;
/**
 * 宣言
 * @author wlj
 *
 */
public class Fragment_Declaration extends BaseFragment{
	private Bundle bundle;
	private TextView title_ch,title_en;
	private WebView context;
	private ImageView pic;
	
	@Override
	protected int getlayout() {
		return R.layout.fragment_1;
	}

	@Override
	protected void initView(View view) {
		bundle = getArguments();
		title_ch = (TextView)view.findViewById(R.id.title_ch);
		title_en = (TextView)view.findViewById(R.id.title_en);
		context = (WebView)view.findViewById(R.id.context);
		pic = (ImageView)view.findViewById(R.id.pic);
		
		callWeb();
		
	}
	
	private Handler Handler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case -1:
				Exception exception = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, exception.getMessage());
				break;
			case 1:
				Hot hot = (Hot)msg.obj;
				setInitView(hot);
				break;

			}
			
		};
		
	};
	
	private void setInitView(Hot hot){
		
		title_ch.setText(hot.getTitle());
		WebSettings settings = context.getSettings();
		settings.setDefaultTextEncodingName("UTF-8");
		
		String date = new String(Base64.decode(hot.getContext(), Base64.DEFAULT));
		if (date.contains("<img")) {
//			settings.setUseWideViewPort(true);// 设置图片调整到适合webview大小

			settings.setLoadWithOverviewMode(true);

			settings.setBuiltInZoomControls(true);

			settings.setSupportZoom(true);// 设定支持缩放
		}
		context.loadData(date, "text/html; charset=UTF-8", null);
	}
		
	private void callWeb(){
		
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				try {
					Hot hot = (Hot) ((ChuangBaBaContext)mContext).getPubById(new Hot(),bundle.getString("typeid"));
					msg.what= 1;
					msg.obj= hot;
					
				} catch (Exception e) {
					AppException.http(e);
					msg.what= -1;
					msg.obj= e;
				}
				Handler.sendMessage(msg);
				
			}
		});
		
	}

}
