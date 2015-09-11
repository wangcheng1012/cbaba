package com.wlj.chuangbaba.activity;

import static com.wlj.chuangbaba.web.MsgContext.key_page;
import static com.wlj.chuangbaba.web.MsgContext.key_pageSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi;
import com.wlj.chuangbaba.activity.main.ProjectClick;
import com.wlj.chuangbaba.activity.personal.OrderActivity;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.activity.wenda.WenDa;
import com.wlj.chuangbaba.bean.Banner;
import com.wlj.chuangbaba.bean.DaiLiShangDian;
import com.wlj.chuangbaba.bean.IndexCity;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.ProjectTab;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.web.MsgContext;
import com.wlj.ob.ObserversImageView;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppConfig;
import com.wlj.util.AppException;
import com.wlj.util.DpAndPx;
import com.wlj.util.ExecutorServices;
import com.wlj.util.GetResourceImage;
import com.wlj.util.Log;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.util.img2.ImageLrucache;
import com.wlj.chuangbaba.web.URLs;
import com.wlj.widget.AutoScrollViewPager;
import com.wlj.widget.SwitchViewPagerDemoActivity;

/**
 * 登录界面
 * 
 * @author wlj
 * 
 */
public class Main extends BaseFragmentActivity implements OnClickListener,
		 BDLocationListener {

	private ImageView menchuangzhanshi, mendianchaxun, dingdan, dailishang,
			wenda, hongbao, jieshaopengyou;
	TextView home, fenlei, dailishang_bottom, my_bottom, more;
	private ChuangBaBaContext mContext;
	private AutoScrollViewPager topBannner, tttjBannner, tjBanner;
	private ObserversImageView tj_120, tj_80, tj_80_2, tj_65;
	private ImageView tuijianMore;
	private GridView gridview1_tj;
	private CharSequence city;
	private List<Base> zmdlist1;
	private TextView left;
	
	@SuppressLint("HandlerLeak")
	public Handler handle = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			Object obj = msg.obj;
			if (obj == null) {
				return;
			}
			
			switch (msg.what) {
			case -1:
				Exception e = (Exception) obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				break;
			case 0:
				Map<String, List<Banner>> list = (Map<String, List<Banner>>) obj;
				initBanner(list);
				break;
			case 3:
				List<ProjectTab> ProjectTablist = (List<ProjectTab>) obj;
				iniTabView(ProjectTablist);
				break;
			case 31:
				BaseList baselist31 = (BaseList)obj;
				ImageView[] ImageViews31 = { tj_120, tj_80, tj_80_2, tj_65 };

				initProject(baselist31, ImageViews31);
				break;
			case 41:
				BaseList baselist1 = (BaseList) obj;
				zmdlist1 = baselist1.getBaselist();
				
				ExecutorServices.getExecutorService().execute(new Runnable() {
					
					@Override
					public void run() {
						// 推荐-专卖店
						Message zmdMessage = Message.obtain();
						try {
							Map<String,Object> map = new HashMap<String, Object>();
						        
							map.put("url", URLs.dailishangList);
							map.put("bujiami","");
							
							map.put("tuijian","yes");
							map.put("city",city);
							
							map.put(key_page,"1");
							map.put(key_pageSize,"2");
							
							zmdMessage.what = 4;
							zmdMessage.obj =  mContext.Request(Main.this,map, new DaiLiShangDian());
						} catch (Exception e) {
							AppException.http(e);
							zmdMessage.what = -1;
							zmdMessage.obj = e;
						}
						handle.sendMessage(zmdMessage);
						
					}
				});
				
				break;
			case 4:
				//专卖店      推荐    
				BaseList baselist = (BaseList) obj;
				List<Base> zmdlist = baselist.getBaselist();
			
				if(zmdlist.size() > 0 && zmdlist1.size() > 0){
					
					String id2= zmdlist.get(0).getId();
					String id1= zmdlist1.get(0).getId();
					if(!id1.equals(id2)){
						
						zmdlist.addAll(0, zmdlist1);
					}
					
				}
			
				gridview1_tj.setAdapter(new CommonAdapter<Base>(mContext, zmdlist,R.layout.item_zhuanmaidian) {
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
				
				gridview1_tj.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent2 = new Intent(mContext,MyMap.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent2.putExtra("DaiLiShangDian", (DaiLiShangDian)view.getTag(R.id.tag_first));
						startActivity(intent2);
					}
				});
				
				break;
			case 5:
				
				BaseList indexcitybaselist = (BaseList) obj;
				List<Base> indexcitylist = indexcitybaselist.getBaselist();
				
				String[] ic = 	new String[indexcitylist.size()];
				
				for (int i = 0; i < indexcitylist.size(); i++) {
					ic[i] = ((IndexCity)indexcitylist.get(i)).getName();
				}
				
				
				break;
			}
		}

	};

	@SuppressLint("WorldReadableFiles")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDingWei();
		mContext = (ChuangBaBaContext) getApplication();
		initview();
		initTitle();
	}

	protected void initBanner(Map<String, List<Banner>> map) {
		Set<String> keySet = map.keySet();
		for (String s : keySet) {
			List<Banner> list = map.get(s);

			List<String> arrayList = new ArrayList<String>();
			for (Banner banner : list) {
				String picurl = banner.getPicurl();
//				if (picurl.contains(URLs.HOST)) {
//					picurl = picurl.replace(URLs.HOST, "");
//				}
				arrayList.add(picurl);
			}

			if ("topBanner".equals(s)) {

				View view = new SwitchViewPagerDemoActivity<String>(mContext,
						arrayList) {
					@Override
					protected void scrollPoint(RelativeLayout layout) {
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, DpAndPx.dpToPx(
										getApplicationContext(), 45));
						params.setMargins(0, 0, 0, 0);
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						setParams(params);
						super.scrollPoint(layout);
						scrollPoint_1_scorll(layout, this, params);
						topBannner = getAutoScrollViewPager();
					}

				}.createview();
				((RelativeLayout) findViewById(R.id.autoscorll)).addView(view);
			} else if ("tiantiantejia".equals(s)) {
				View view = new SwitchViewPagerDemoActivity<String>(mContext,
						arrayList) {
					@Override
					protected void addSwitchPage(
							AutoScrollViewPager autoviewPager) {
						super.addSwitchPage(autoviewPager);
						tttjBannner = autoviewPager;
					}
				}.createview();
				((RelativeLayout) findViewById(R.id.autoscroll_3))
						.addView(view);
			} else if ("tuijianshangpin".equals(s)) {
				View view = new SwitchViewPagerDemoActivity<String>(mContext,
						arrayList) {
					@Override
					protected void addSwitchPage(
							AutoScrollViewPager autoviewPager) {
						super.addSwitchPage(autoviewPager);
						int dpToPx = DpAndPx.dpToPx(mContext, 250);
						setScrollLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT, dpToPx));
						tjBanner = autoviewPager;
					}
				}.createview();
				((RelativeLayout) findViewById(R.id.autoscorll_4))
						.addView(view);
			}

		}

	}

	private void initTitle() {
		// 右
		TextView right = (TextView) findViewById(R.id.right);
		// Drawable drawabler = (BitmapDrawable)GetResourceImage.get(mContext,
		// R.drawable.rentou_quan_white);
		Drawable drawabler = getResources().getDrawable(R.drawable.rentou_quan_white);
		// / 这一步必须要做,否则不会显示.
		drawabler.setBounds(0, 0, drawabler.getMinimumWidth(),
				drawabler.getMinimumHeight());
		right.setCompoundDrawables(drawabler, null, null, null);
		
		right.setOnClickListener(this);
		
		left = (TextView) findViewById(R.id.left);
		
		Drawable drawablerleft = getResources().getDrawable(R.drawable.dingwei);
		
		drawablerleft.setBounds(0, 0, drawablerleft.getMinimumWidth(), drawablerleft.getMinimumHeight());
		
		left.setCompoundDrawables(drawablerleft, null, null, null);
		
		
	}

	/**
	 * 初始化产品
	 */
	public void initProject(BaseList baselist, ImageView[] ImageViews) {

		if (baselist != null && baselist.getBaselist() != null) {

			List<Base> baselist2 = baselist.getBaselist();
			LoadImage loadImage = LoadImage.getinstall();
			for (int i = 0; i < 4; i++) {
				ImageView tmp = ImageViews[i];
				if (tmp != null) {
					tmp.setImageDrawable(null);
				}
				if (i >= baselist2.size()) {
					tmp.setImageBitmap(ImageLrucache.getInstance().getResourceBitmap(R.drawable.project_bg, mContext));
					tmp.setTag(null);
					tmp.setClickable(false);
				} else {
					List<String> bannerPic = ((Project) baselist2.get(i))
							.getBannerPic();
					if (bannerPic.size() > 0) {
						tmp.setClickable(true);
						loadImage.addTask(URLs.HOST + bannerPic.get(0), tmp);
						tmp.setTag(R.id.tag_first, baselist2.get(i));
						loadImage.doTask();
					} else {
						tmp.setClickable(false);
						tmp.setTag(null);
						tmp.setImageBitmap(ImageLrucache.getInstance().getResourceBitmap(
								R.drawable.project_bg, mContext));
					}
				}
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ImageLrucache.getInstance().clearCache();
		System.gc();
		if (topBannner != null) {
			topBannner.stopAutoScroll();
		}
		if (tttjBannner != null) {
			tttjBannner.stopAutoScroll();
		}
		if (tjBanner != null) {
			tjBanner.stopAutoScroll();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (topBannner != null) {
			topBannner.startAutoScroll();
		}
		if (tttjBannner != null) {
			tttjBannner.startAutoScroll();
		}
		if (tjBanner != null) {
			tjBanner.startAutoScroll();
		}

	}

	private void initview() {
		// 顶部导航
		menchuangzhanshi = (ImageView) findViewById(R.id.menchuangzhanshi);
		mendianchaxun = (ImageView) findViewById(R.id.mendianchaxun);
		dingdan = (ImageView) findViewById(R.id.dingdan);
		dailishang = (ImageView) findViewById(R.id.dailishang);
		wenda = (ImageView) findViewById(R.id.wenda);

		menchuangzhanshi.setImageBitmap(GetResourceImage.get(mContext,
				R.drawable.menchuangzhanshi));
		mendianchaxun.setImageBitmap(GetResourceImage.get(mContext,
				R.drawable.mendianchaxun));
		dingdan.setImageBitmap(GetResourceImage.get(mContext,
				R.drawable.dingdan));
		dailishang.setImageBitmap(GetResourceImage.get(mContext,
				R.drawable.dailishang));
		wenda.setImageBitmap(GetResourceImage.get(mContext, R.drawable.wenda));

		menchuangzhanshi.setOnClickListener(this);
		mendianchaxun.setOnClickListener(this);
		dingdan.setOnClickListener(this);
		dailishang.setOnClickListener(this);
		wenda.setOnClickListener(this);
		// 红包、介绍喷油
		hongbao = (ImageView) findViewById(R.id.hongbao);
		jieshaopengyou = (ImageView) findViewById(R.id.jieshaopengyou);

		// hongbao.setImageDrawable(GetResourceImage.get(mContext,
		// R.drawable.hongbao));
		// jieshaopengyou.setImageDrawable(GetResourceImage.get(mContext,
		// R.drawable.jieshaopengyou));

		hongbao.setOnClickListener(this);
		jieshaopengyou.setOnClickListener(this);

		// bottom
		home = (TextView) findViewById(R.id.home);
		fenlei = (TextView) findViewById(R.id.fenlei);
		dailishang_bottom = (TextView) findViewById(R.id.dailishang_bottom);
		my_bottom = (TextView) findViewById(R.id.my_bottom);
		more = (TextView) findViewById(R.id.more);

		home.setOnClickListener(this);
		fenlei.setOnClickListener(this);
		dailishang_bottom.setOnClickListener(this);
		my_bottom.setOnClickListener(this);
		more.setOnClickListener(this);

		// 推荐商品
		findViewById(R.id.textView13).setOnClickListener(this);
		findViewById(R.id.textView132).setOnClickListener(this);

		tuijianMore = (ImageView) findViewById(R.id.tuijianMore);
		tuijianMore.setOnClickListener(this);

		tj_120 = (ObserversImageView) findViewById(R.id.tj_120);
		tj_80 = (ObserversImageView) findViewById(R.id.tj_80);
		tj_80_2 = (ObserversImageView) findViewById(R.id.tj_80_2);
		tj_65 = (ObserversImageView) findViewById(R.id.tj_65);
		tj_120.setOnClickListener(new ProjectClick(this));
		tj_80.setOnClickListener(new ProjectClick(this));
		tj_80_2.setOnClickListener(new ProjectClick(this));
		tj_65.setOnClickListener(new ProjectClick(this));

		// 专卖店
		((ImageView) findViewById(R.id.zhuanmaidian_more)).setOnClickListener(this);
		gridview1_tj = (GridView)findViewById(R.id.gridview1_tj);
		callWeb();
	}

	private LinearLayout tabLinlay;

	private void iniTabView(List<ProjectTab> projecttab) {

		tabLinlay = (LinearLayout) findViewById(R.id.tabLinlay);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		int px5 = DpAndPx.dpToPx(getApplicationContext(), 5);
		int px3 = DpAndPx.dpToPx(getApplicationContext(), 3);
		params1.setMargins(px3, 0, 0, 0);
		// 复位
		for (ProjectTab projectTab2 : projecttab) {

			TextView fenleitab = new TextView(getApplicationContext());

			fenleitab.setText(projectTab2.getName());
			fenleitab.setTag(projectTab2.getListid());
			fenleitab.setOnClickListener(new MyOnClick());
			fenleitab.setGravity(Gravity.CENTER);
			fenleitab.setLayoutParams(params1);
			fenleitab.setPadding(px5, 0, px5, 0);

			tabLinlay.addView(fenleitab);
		}
		// 设置点击
		int tabLinlaynum = tabLinlay.getChildCount();
		if (tabLinlaynum > 0) {
			View v = tabLinlay.getChildAt(0);
			v.performClick();
		}

	}

	private void callWeb() {
		setProgressBarIndeterminateVisibility(true);
		ExecutorServices.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				// 顶部banner
				Map<String, List<Banner>> list = mContext.getBanner();
				Message message = Message.obtain();
				message.what = 0;
				message.obj = list;
				handle.sendMessage(message);
				
				// 天天特价商品
				Message message1_1 = Message.obtain();
				try {
					Map<String, String> paremeter = new HashMap<String, String>();

					paremeter.put(MsgContext.key_page, "1");
					paremeter.put(MsgContext.key_pageSize, "4");
					paremeter.put("type", MsgContext.tuijian_tejia + "");

					BaseList projecttab = mContext.getProductListByFenleiId(
							paremeter, true);
					message1_1.what = 11;
					message1_1.obj = projecttab;

				} catch (Exception e) {
					AppException.http(e);// 记录日志
					message1_1.what = -1;
					message1_1.obj = e;
				}
				handle.sendMessage(message1_1);

				// 推荐-商品类别
				Message tabMessage = Message.obtain();
				try {
					List<ProjectTab> projecttab = mContext.getProjectTab();
					tabMessage.what = 3;
					tabMessage.obj = projecttab;
				} catch (Exception e) {
					AppException.http(e);
					tabMessage.what = -1;
					tabMessage.obj = e;
				}
				handle.sendMessage(tabMessage);

//				// 城市选择
//				Message ICMessage = Message.obtain();
//				try {
//					Map<String,Object> map = new HashMap<String, Object>();
//				        
//					map.put("url", URLs.indexCity);
//					map.put("bujiami", true);
//					
//					ICMessage.what = 5;
//					ICMessage.obj =  mContext.Request(Main.this,map, new IndexCity());
//				} catch (Exception e) {
//					AppException.http(e);
//					ICMessage.what = -1;
//					ICMessage.obj = e;
//				}
//				handle.sendMessage(ICMessage);
				 setProgressBarIndeterminateVisibility(false);
			}
		});

	}

	private void scrollPoint_1_scorll(RelativeLayout layout,
			SwitchViewPagerDemoActivity<String> switchview, LayoutParams params) {

		LinearLayout pointlinearLayout = switchview.getPointlinearLayout();
		pointlinearLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		pointlinearLayout.setBackgroundResource(R.drawable.point_bg);

		ImageView[] getmImageViews = switchview.getmImageViews();

		for (ImageView imageView : getmImageViews) {
			imageView.setPadding(6, 6, 6, 0);
			imageView.setAdjustViewBounds(true);
		}

		TextView textView = new TextView(getApplicationContext());
		textView.setText("窗爸爸贴心到家服务正式启动");
		textView.setTextSize(16f);
		textView.setTextColor(Color.WHITE);
		textView.setPadding(6, 0, 0, 0);
		textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

		layout.addView(textView, params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menchuangzhanshi:
			Intent intent = new Intent(getApplicationContext(), FenLei.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.mendianchaxun:
			Intent intent2 = new Intent(getApplicationContext(), MyMap.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
		case R.id.dingdan:
			
			Intent dingdan = new Intent(getApplicationContext(), OrderActivity.class);
			dingdan.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			dingdan.putExtra("type", OrderActivity.huiyuan);
			startActivity(dingdan);
			break;
		case R.id.dailishang:
			Class<?> cls = null;
			if (!User.type_dailishang.equals(mContext.getProperty(AppConfig.CONF_TYPT))) {
				// 登录
				cls = DaiLiShang.class;
			} else {
				cls = DaiLiShang_GuanLi.class;
			}
			Intent intentdailishang = new Intent(getApplicationContext(), cls);
			intentdailishang.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentdailishang);
			break;
		case R.id.wenda:
			Intent intentwenda = new Intent(getApplicationContext(), WenDa.class);
			intentwenda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentwenda);
			break;
		case R.id.hongbao:
			Intent hongbao = new Intent(getApplicationContext(), HongBao.class);
			hongbao.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(hongbao);
			break;
		case R.id.jieshaopengyou:
			Intent jieshaopengyou = new Intent(getApplicationContext(),
					JieShaoPengYou.class);
			jieshaopengyou.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(jieshaopengyou);
			break;
		// bottom
		case R.id.home:
			break;
		case R.id.fenlei:
			Intent fenlei = new Intent(getApplicationContext(), FenLei.class);
			fenlei.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(fenlei);
			break;
		case R.id.dailishang_bottom:
			Intent home = new Intent(getApplicationContext(), MyMap.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
			break;
		case R.id.my_bottom:
			Intent my_bottom = new Intent(getApplicationContext(), Personal_Center.class);
			my_bottom.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(my_bottom);
			break;
		case R.id.more:

			break;

		// 天天特价
		case R.id.tuijianMore:
			Intent tuijianMore = new Intent(getApplicationContext(),
					FenLei.class);
			tuijianMore.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tuijianMore);
			break;

		case R.id.right:
			Intent intenthy = new Intent(mContext, Personal_Center.class);
			intenthy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intenthy);
			break;

		case R.id.zhuanmaidian_more:
			Intent zhuanmaidian1 = new Intent(mContext, ZhuanMaiDian.class);
			zhuanmaidian1.putExtra("city", city);
			zhuanmaidian1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(zhuanmaidian1);
			break;
		}
	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(final View v) {
			int count = tabLinlay.getChildCount();

			if (count > 0) {
				// 背景颜色
				int black = getResources().getColor(R.color.black);
				int white = getResources().getColor(R.color.white);

				for (int i = 0; i < count; i++) {
					TextView childAt = (TextView) tabLinlay.getChildAt(i);
					childAt.setBackgroundDrawable(null);
					childAt.setTextColor(black);
				}
				v.setBackgroundResource(R.drawable.tabtxtbg);
				((TextView) v).setTextColor(white);

				// 访问网路 取main推荐4图
				ExecutorServices.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						Map<String, String> paremeter = new HashMap<String, String>();
						paremeter.put("fenleiid", v.getTag() + "");
						paremeter.put("type", MsgContext.tuijian_yes + "");
						paremeter.put(MsgContext.key_page, "1");
						paremeter.put(MsgContext.key_pageSize, "4");
						Message obtain = Message.obtain();
						try {
							BaseList baseList = mContext.getProductListByFenleiId(paremeter, true);
							obtain.what = 31;
							obtain.obj = baseList;

						} catch (Exception e) {
							obtain.what = -1;
							obtain.obj = e;
						}
						handle.sendMessage(obtain);
					}
				});

			}

		}

	}
	private LocationClient mLocClient;
	/**
	 * 定位初始化
	 */
	private void initDingWei() {

		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// //Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
		option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setAddrType("all");
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		// option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(this);
		mLocClient.start();
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null )
			return;
		Log.i("main", "位置："+location.getAddrStr());
		if (location.getLocType() == BDLocation.TypeGpsLocation
				|| location.getLocType() == BDLocation.TypeNetWorkLocation) {

			city = location.getProvince();
			// 左
			left.setText(city);
			
			mLocClient.unRegisterLocationListener(this);
			ExecutorServices.getExecutorService().execute(new Runnable() {
				@Override
				public void run() {
					
					// 推荐1-专卖店
					Message zmd1Message = Message.obtain();
					try {
						Map<String,Object> map = new HashMap<String, Object>();
					        
						map.put("url", URLs.dailishangList);
						map.put("bujiami","");
						map.put("tanzikou","true");
						
						map.put(key_page,"1");
						map.put(key_pageSize,"1");
						
						zmd1Message.what = 41;
						zmd1Message.obj =  mContext.Request(Main.this,map, new DaiLiShangDian());
					} catch (Exception e) {
						AppException.http(e);
						zmd1Message.what = -1;
						zmd1Message.obj = e;
					}
					handle.sendMessage(zmd1Message);
				}
			});
		} else {
			UIHelper.ToastMessage(this, "定位失败（" + location.getLocType() + ")");

		}
	}

	private long mExitTime;

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 800) {

			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		mLocClient.unRegisterLocationListener(this);
		super.onDestroy();
	}

}
