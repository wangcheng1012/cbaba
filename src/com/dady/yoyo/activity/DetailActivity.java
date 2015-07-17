package com.dady.yoyo.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dady.yoyo.activity.view.CustomListView;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;

/**
 * 资讯详情 date 2015-5-5
 * 
 * @author Administrator
 */
public class DetailActivity extends BaseFragmentActivity implements
		OnClickListener, OnItemClickListener {
	CustomListView lv_custom;
	private ChuangBaBaContext mContext;

	private TextView tv_title, tv_date;
	private WebView webview_context;

	private Hot hot;
	
	private Handler handle = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 2:
				initData((BaseList) msg.obj);
				break;
			case 1:
				hot = (Hot) msg.obj;
				setViewDate(hot);
				break;
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				break;

			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		mContext = (ChuangBaBaContext) getApplicationContext();
		Intent intent = getIntent();
		Hot hot = (Hot) intent.getSerializableExtra("hot");
		hot.getThisfromid(mContext, handle);

		initView();
		initTitle();
	}

	private void initView() {
		lv_custom = (CustomListView) findViewById(R.id.lv_custom);
		lv_custom.setFocusable(false);
		
		lv_custom.setOnItemClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_date = (TextView) findViewById(R.id.tv_date);
		webview_context = (WebView) findViewById(R.id.webview_context);
	}

	private void setViewDate(Hot hot) {
		tv_title.setText(hot.getTitle());
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(hot.getTitle());
		
		String time = hot.getTime();
		if (time == null || "".equals(time.trim())) {
			tv_date.setText("");
		} else {
			tv_date.setText(StringUtils.getTime(Long.parseLong(time),"yyyy-MM-dd HH:mm"));
		}
		WebSettings settings = webview_context.getSettings();
		settings.setDefaultTextEncodingName("UTF-8");
		byte[] byteIcon = Base64.decode(hot.getContext(), Base64.DEFAULT);
		String data = new String(byteIcon);
		webview_context.loadData(data, "text/html; charset=UTF-8", null);

		// 相关阅读
		callweb(hot);

	}

	private void callweb(final Hot hot) {

		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					BaseList list = mContext.getPubXiangguanByPub(hot.getId());
					msg.what = 2;
					msg.obj = list;
					
				} catch (Exception e) {
					AppException.http(e);
					msg.what = -1;
					msg.obj = e;
				}
				handle.sendMessage(msg);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initData(BaseList list) {
		if(list == null || list.getBaselist() == null){
			return;
		}
		lv_custom.setAdapter(new CommonAdapter<Base>(this, (List<Base>)list.getBaselist(),R.layout.inflater_detail) {

			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {
				Hot item= (Hot)base;
				 viewHolder.setText(R.id.tv_title, item.getTitle());
				 view.setTag(R.id.tag_first, item);
				return view;
			}
		});
	}

	private void initTitle() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(" ");

		TextView right = (TextView) findViewById(R.id.right);
		right.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(
				R.drawable.rentou_quan_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);

		TextView left = (TextView) findViewById(R.id.left);
		Drawable drawableback = getResources().getDrawable(
				R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

		right.setOnClickListener(this);
		left.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			finish();
			break;
		case R.id.right:
			Intent right = new Intent(getApplicationContext(),Personal_Center.class);
			right.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(right);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Hot hot = (Hot)view.getTag(R.id.tag_first);
		
		hot.getThisfromid(mContext, handle);
		
	}

}
