package com.wlj.chuangbaba.activity;

import static com.wlj.chuangbaba.web.MsgContext.key_page;
import static com.wlj.chuangbaba.web.MsgContext.key_pageSize;
import static com.wlj.chuangbaba.web.MsgContext.pageSize;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.projectxiangqing.FenLei_xiangqing;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.ProjectTab;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppException;
import com.wlj.util.DpAndPx;
import com.wlj.util.ExecutorServices;
import com.wlj.util.MathUtil;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.web.URLs;
import com.wlj.widget.PullToRefreshListView;
import com.wlj.widget.PullToRefreshListView.OnRefreshListener;

/**
 * 门窗展示--分类
 * 
 * @author wlj
 * 
 */
public class FenLei extends BaseFragmentActivity implements OnClickListener {

	private PullToRefreshListView xiangqing;
	private ChuangBaBaContext mContext;
	private LinearLayout tabLinlay;
	private String tagid;
	private View listview_footer;
	private CommonAdapter<Base> commonAdapter;
	private List<Base> listDate = new ArrayList<Base>();
	private TextView listview_foot_more;
	private ProgressBar listview_foot_progress;
	private int pageIndex = 1;
	private FrameLayout frame_listview;

	private final static int lable = 1;
	private final static int lablelist = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fenlei);
		mContext = (ChuangBaBaContext) getApplicationContext();
		initRefreshList();
		init_title();
		
		//分类列表
		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				Message message = new Message();
				try {
					List<ProjectTab> projecttab = mContext.getProjectTab();
					message.what = lable;
					message.obj = projecttab;

				} catch (Exception e) {
					AppException.http(e);
					message.what = -1;
					message.obj = e;
				}
				handle.sendMessage(message);
			}
		});
	}

	private Handler handle = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case lable:

				List<ProjectTab> list = (List<ProjectTab>) msg.obj;
				// 更新分类tab
				iniTabView(list);
				break;
			case lablelist:
				// 更新分类list
				BaseList projectList = (BaseList) msg.obj;
				switch (msg.arg1) {
				case UIHelper.LISTVIEW_ACTION_INIT:

				case UIHelper.LISTVIEW_ACTION_REFRESH:
					// 刷新
					pageIndex = 1;
					listDate.clear();
					listDate.addAll(projectList.getBaselist());
					break;
				case UIHelper.LISTVIEW_ACTION_SCROLL:
					// 加载更多
					pageIndex = projectList.getPageIndex();

					List<Base> projectlists = projectList.getBaselist();
					if (listDate.size() > 0) {

						for (Base project : projectlists) {

							boolean b = false;
							for (Base project2 : listDate) {
								if (project.getId() == project2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								listDate.add(project);
						}
					} else {

						listDate.addAll(projectlists);
					}

					break;
				}

				if (projectList.getBaselist().size() == 0) {
					listview_foot_more.setText(R.string.load_empty);
				} else if (projectList.getPageIndex() == projectList
						.getLastpage()) {
					listview_foot_more.setText(R.string.load_full);
				} else {
					listview_foot_more.setText(R.string.load_more);
				}
				listview_foot_progress.setVisibility(View.GONE);

				if (UIHelper.LISTVIEW_ACTION_REFRESH == msg.arg1) {
					updateUpPic(projectList);
					xiangqing.onRefreshComplete(getString(R.string.pull_to_refresh_update)+ new Date().toLocaleString());
				}
				if (UIHelper.LISTVIEW_ACTION_INIT == msg.arg1) {
					updateUpPic(projectList);
				}
				commonAdapter.notifyDataSetChanged();

				break;
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				listview_foot_progress.setVisibility(View.GONE);
				listview_foot_more.setText(R.string.load_error);
			}
		};

	};

	private void callWeb(final int pageindex, final int action) {

		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				boolean isRefresh = false;
				Message message = new Message();
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH 
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL
						|| action == UIHelper.LISTVIEW_ACTION_INIT) {
					isRefresh = true;
				}
				try {
					
					Map<String, String> paremeter = new HashMap<String, String>();
					paremeter.put("fenleiid", tagid);
					paremeter.put(key_page, pageindex+"");
					paremeter.put(key_pageSize, pageSize+"");
					
					BaseList projecttab = mContext.getProductListByFenleiId(paremeter, isRefresh);
					
					message.what = lablelist;
					message.obj = projecttab;

				} catch (Exception e) {
					AppException.http(e);// 记录日志
					message.what = -1;
					message.obj = e;
				}
				message.arg1 = action;
				handle.sendMessage(message);
			}
		});

	}

	private void initRefreshList() {

		frame_listview = (FrameLayout) findViewById(R.id.frame_listview);
		xiangqing = (PullToRefreshListView) getLayoutInflater().inflate(R.layout.fragment_7, null);

		frame_listview.removeAllViews();
		frame_listview.addView(xiangqing);
		listview_footer = getLayoutInflater().inflate(R.layout.listview_footer,null);
		listview_foot_more = (TextView) listview_footer.findViewById(R.id.listview_foot_more);
		listview_foot_progress = (ProgressBar) listview_footer.findViewById(R.id.listview_foot_progress);
		xiangqing.addFooterView(listview_footer);

		commonAdapter = new CommonAdapter<Base>(getApplicationContext(),listDate, R.layout.item_project) {

			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					Base base, int position, ViewGroup parent) {

				Project item = (Project) base;

				// 类似getview中处理
				viewHolder.setText(R.id.projectTitle, item.getName());// 设置title
				viewHolder.setText(R.id.projectModel, item.getXinghao());
				viewHolder.setText(R.id.projectchangjia, item.getChangjia());
				
				Double cankaoPrice = MathUtil.parseDouble(item.getCankaoPrice());
				DecimalFormat df = new DecimalFormat("#0.00"); 
				viewHolder.setText(R.id.projectCanKaoPrice,"￥"+df.format(cankaoPrice/100)+"元/㎡");

				LoadImage loadImage = LoadImage.getinstall();
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				iv_icon.setVisibility(View.INVISIBLE);
				String picurl = item.getItemPic();
				if ("".equals(picurl.trim())) {

				} else {
					loadImage.addTask(URLs.HOST + picurl, iv_icon);
					loadImage.doTask();
				}
				view.setTag(R.id.tag_first, item);

				return view;
			}
		};
		xiangqing.setAdapter(commonAdapter);

		xiangqing.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view == listview_footer) {
					return;
				}
				Intent intent2 = new Intent(getApplicationContext(),
						FenLei_xiangqing.class);
				intent2.putExtra("project",(Project) view.getTag(R.id.tag_first));
				intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent2);
			}

		});
		xiangqing.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				callWeb(1, UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});

		xiangqing.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				xiangqing.onScrollStateChanged(view, scrollState);

				if (listDate.size() == 0)
					return;
				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(listview_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd && !getString(R.string.load_full).equals(listview_foot_more.getText())) {
					listview_foot_more.setText(R.string.load_more);
					listview_foot_more.setVisibility(View.VISIBLE);
					callWeb( pageIndex + 1,UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				xiangqing.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});

	}

	private void updateUpPic(BaseList obj) {
		// 上面图片
		ImageView headimageview = (ImageView) findViewById(R.id.headimageview);

		if (!"".equals(obj.getPic().trim())) {
			LoadImage loadImage = LoadImage.getinstall();
			loadImage.addTask(URLs.HOST + obj.getPic(), headimageview);
			loadImage.doTask();
		}
	}

	private void iniTabView(List<ProjectTab> projecttab) {

		tabLinlay = (LinearLayout) findViewById(R.id.tabLinlay);
		// 复位
		for (ProjectTab projectTab2 : projecttab) {

			TextView fenleitab = new TextView(getApplicationContext());

			fenleitab.setText(projectTab2.getName());
			fenleitab.setTag(projectTab2.getListid());
			fenleitab.setOnClickListener(new FenLeiOnClick());

			tabLinlay.addView(fenleitab);
		}
		// 设置点击
		int tabLinlaynum = tabLinlay.getChildCount();
		if (tabLinlaynum > 0) {
			View v = tabLinlay.getChildAt(0);
			v.performClick();
		}

	}

	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("商品类别");

		TextView right = (TextView) findViewById(R.id.right);
		right.setText("首页");
		right.setTextSize(15);
		right.setTextColor(getResources().getColor(R.color.white));
		right.setGravity(Gravity.CENTER_VERTICAL);

		Drawable drawable = getResources().getDrawable(R.drawable.home_white);
		// / 这一步必须要做,否则不会显示.
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

	class FenLeiOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int px1 = DpAndPx.dpToPx(getApplicationContext(), 1);
			params2.setMargins(0, px1, px1, px1);
			params1.setMargins(0, px1, px1, 0);
			int huise_F2F2F2 = getResources().getColor(R.color.huise_F2F2F2);
			int black = getResources().getColor(R.color.black);

			int px10 = DpAndPx.dpToPx(getApplicationContext(), 10);
			int px15 = DpAndPx.dpToPx(getApplicationContext(), 15);
			// 复位
			for (int i = 0; i < tabLinlay.getChildCount(); i++) {
				TextView itemview = (TextView) tabLinlay.getChildAt(i);

				int count = tabLinlay.getChildCount() - 1;
				if (i == count) {
					itemview.setLayoutParams(params2);
				} else {
					itemview.setLayoutParams(params1);
				}
				itemview.setBackgroundColor(huise_F2F2F2);
				itemview.setPadding(px10, px15, px10, px15);
				itemview.setTextColor(black);
			}

			TextView clickview = (TextView) v;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = 0;
			params.topMargin = px1;

			Drawable drawable = getResources().getDrawable(
					R.drawable.right_whitebg);
			clickview.setBackgroundDrawable(drawable);
			clickview.setPadding(px10, px15, px10, px15);
			clickview.setTextColor(getResources().getColor(R.color.red_f27e17));
			clickview.setLayoutParams(params);
			clickview.setGravity(Gravity.CENTER_VERTICAL);

			tagid = v.getTag().toString();

			// 先清楚数据，提升用户体验
			listDate.clear();
			commonAdapter.notifyDataSetChanged();

			callWeb( 1, UIHelper.LISTVIEW_ACTION_INIT);
		}

	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.gc();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

}
