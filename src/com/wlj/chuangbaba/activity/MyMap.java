package com.wlj.chuangbaba.activity;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.navi.sdkdemo.BNavigatorActivity;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.activity.personal.fragment.Order_AllOrder;
import com.wlj.chuangbaba.bean.DaiLiShangDian;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.chuangbaba.bean.YuYue;
import com.wlj.chuangbaba.util.AreaSelect;
import com.wlj.chuangbaba.util.MyOrientationListener;
import com.wlj.chuangbaba.util.MyOrientationListener.OnOrientationListener;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.util.img.ImageFileCache;
import com.wlj.util.img.LoadImage;
import com.wlj.web.URLs;

/**
 * 门店查询（地图）
 * 
 * @author wlj
 * 
 */
public class MyMap extends BaseFragmentActivity implements
		OnMarkerClickListener, BDLocationListener, CloudListener,
		OnClickListener, OnMapClickListener, OnMapTouchListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private SparseArray<CloudPoiInfo> cloudPoiInfos;
	private View map_dialog;
	private RelativeLayout mendian_root;
	private ChuangBaBaContext mContext;
	public ImageView position_my;
	boolean isFirstLoc = true;
	private LatLng locationBack;
	public LocationMode mCurrentMode = LocationMode.NORMAL;
	private MyOrientationListener myOrientationListener;
	private int mXDirection;
	private AreaSelect areaSelect;
	private DaiLiShangDian daiLiShangDian;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d("MenDian", "action: " + s);

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Log.e("MenDian","key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Log.e("MenDian", "网络出错");
			}
		}
	}

	private boolean mIsEngineInitSuccess = false;
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			// 导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {

		}

		public void engineInitFail() {
			UIHelper.ToastMessage(getApplicationContext(), "导航初始化失败");
			mIsEngineInitSuccess = false;
		}
	};
	private SDKReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
		
		/**
		 * 导航
		 */
		initDaoHang();
		
		setContentView(R.layout.mendian);
		mContext = (ChuangBaBaContext) getApplicationContext();
		initMap();
		
		initDingWei();
		
		/**
		 * 传感器
		 */
		initOritationListener();
		
		// 我的位置按钮
		myPosition();
		
		initTitle();
		// mark弹出框
		initDialog();

		if (!mContext.isNetworkConnected()) {
			UIHelper.ToastMessage(mContext, "网络连接失败,不能获取地理位置信息");
		}

	}

	/**
	 * 导航
	 */
	private void initDaoHang(){
		BaiduNaviManager.getInstance().initEngine(this,
				new ImageFileCache().getSDPath(), mNaviEngineInitListener,
				new LBSAuthManagerListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				String str = null;
				if (0 == status) {
					str = "导航key校验成功!";
				} else {
					str = "key校验失败, " + msg + "status: " + status;
				}
				Toast.makeText(getApplicationContext(),str + mIsEngineInitSuccess, Toast.LENGTH_LONG)
						.show();
			}
		});
		
	}
	private void initMap() {
		if (!mContext.isOPen()) {
			UIHelper.GpsOpenTip(this, "请设置手机开启位置服务,否则地图无法定位");
		} else if (!mContext.GPSisOpen()) {
			UIHelper.GpsOpenTip(this,
					"开启GPS后，地图能够在室外开阔地点提供最精准的点位服务\n\n使用GPS不消耗流量");
		}

		CloudManager.getInstance().init(MyMap.this);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		LatLng cenpt = new LatLng(29.572253, 106.502177);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(11).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		mBaiduMap.setOnMarkerClickListener(this);

		mBaiduMap.setOnMapClickListener(this);

		mBaiduMap.setOnMapTouchListener(this);
		
		//
		// 隐藏缩放控件
		int childCount = mMapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				zoom = child;
				break;
			}

		}
		zoom.setVisibility(View.GONE);
	}

	/**
	 * 定位初始化
	 */
	private void initDingWei() {

		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// //Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为5000ms
		option.setAddrType("all");
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		// option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

		mLocClient.setLocOption(option);
	}

	/**
	 * 初始化方向传感器
	 */
	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mXDirection = (int) x;
						// if(location== null)return;
						// // 构造定位数据
						// MyLocationData locData = new MyLocationData.Builder()
						// .accuracy(location.getRadius())
						// // 此处设置开发者获取到的方向信息，顺时针0-360
						// .direction(mXDirection)
						// .latitude(location.getLatitude())
						// .longitude(location.getLongitude()).build();
						// // 设置定位数据
						// mBaiduMap.setMyLocationData(locData);
					}
				});
	}

	private Order order;

	@SuppressLint("InflateParams")
	private void initDialog() {

		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");

		LayoutInflater inf = LayoutInflater.from(getApplicationContext());
		map_dialog = inf.inflate(R.layout.map_dialog, null);
		map_dialog.setVisibility(View.GONE);
		map_dialog.getBackground().setAlpha(150);
		map_dialog.findViewById(R.id.dialog_close_button).setOnClickListener(this);
		map_dialog.findViewById(R.id.dialog_submit).setOnClickListener(this);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mendian_root = (RelativeLayout) findViewById(R.id.mendian_root);
		mendian_root.addView(map_dialog, params);
		if (order != null) {
			map_dialog.findViewById(R.id.order).setVisibility(View.VISIBLE);
			TextView dialog_order = (TextView) map_dialog
					.findViewById(R.id.dialog_order);
			dialog_order.setVisibility(View.VISIBLE);
			dialog_order.setText(order.getOrderId());
		} else {
			map_dialog.findViewById(R.id.order).setVisibility(View.GONE);
			map_dialog.findViewById(R.id.dialog_order).setVisibility(View.GONE);
		}

		map_dialog.findViewById(R.id.comehere).setOnClickListener(this);

	}

	private void initTitle() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("代理商位置");

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

	/**
	 * 我的位置按钮
	 */
	private void myPosition() {
		position_my = (ImageView) findViewById(R.id.position_my);
		position_my.setOnClickListener(this);
	}
	
	private String CloudSearchType = "";
	
	/**
	 * 云检索
	 * 
	 * @param arg0
	 */
	private void regionSearch(LatLng arg0) {
		if (!mContext.isOPen()) {
			return;
		}
		
		CloudSearchType = "regionSearch";
		UIHelper.loading("搜索中……", this);
		NearbySearchInfo info = new NearbySearchInfo();
		info.ak = "3EQkyVB7qqRGAehd7uTmDKUT";
		info.geoTableId = 103236;
		info.radius = 5000;
		info.location = arg0.longitude + "," + arg0.latitude;
		// info.location = "106.502177,29.572253";
		CloudManager.getInstance().nearbySearch(info);
	}

	private String LocalSearchRegion;
	/**
	 * 本地云检索
	 */
	public void LocalSearch(String arg0) {
		Log.i("MyMap", "检索关键字:"+arg0);
		if (!mContext.isOPen()) {
			return;
		}
		LocalSearchRegion = arg0;
		CloudSearchType = "LocalSearch";
		UIHelper.loading("搜索中……", this);
		LocalSearchInfo info = new LocalSearchInfo();
		info.ak = "3EQkyVB7qqRGAehd7uTmDKUT";
		info.geoTableId = 103236;
		// info.tags = "";//标签，可选，空格分隔的多字符串，最长45个字符，样例：美食 小吃
		// info.q = "天安门";//检索关键字，可选。
		info.region = arg0;// 检索区域名称，必选。
		info.pageSize = 50;
		CloudManager.getInstance().localSearch(info);
	}

	/**
	 * 定位返回
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		Log.e("dd", location.getLatitude() + "    " + mXDirection);
		// map view 销毁后不在处理新接收的位置
		if (location == null || mMapView == null)
			return;
		if (location.getLocType() != BDLocation.TypeGpsLocation
				|| location.getLocType() == BDLocation.TypeNetWorkLocation) {

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			locationBack = new LatLng(location.getLatitude(),
					location.getLongitude());

			if (isFirstLoc) {
				isFirstLoc = false;
				// 初始化省市选择狂数据
				areaSelect = new AreaSelect(this, location);

				// 移动到 获取的定位位置
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLng(locationBack);
				mBaiduMap.animateMapStatus(u);

				daiLiShangDian = (DaiLiShangDian)getIntent().getSerializableExtra("DaiLiShangDian");
				
				if(daiLiShangDian == null){//不是重专卖店过来的
					
					regionSearch(locationBack);
				}else{
					
					if (!mContext.isOPen()) {
						return;
					}
					int uid = 	daiLiShangDian.getUid();
					if(uid == 0){
						UIHelper.dialog(this, "位置信息有误", null, null);
						return;
					}
					DetailSearchInfo info = new DetailSearchInfo();
					info.ak = "3EQkyVB7qqRGAehd7uTmDKUT";
					info.geoTableId = 103236;
					info.uid = uid;
					CloudManager.getInstance().detailSearch(info);
				}
				
			}
		} else {
			UIHelper.ToastMessage(mContext, "定位失败（" + location.getLocType() + ")");

		}
	}

	/**
	 * Marker点击
	 */
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// Marker点击
		CloudPoiInfo info = cloudPoiInfos.get(StringUtils.toInt(arg0.getTitle()));
		((TextView) map_dialog.findViewById(R.id.dialog_title)).setText(info.title);
		((TextView) map_dialog.findViewById(R.id.dialog_title)).setTag(info.extras.get("businessid") + "");
		((TextView) map_dialog.findViewById(R.id.dialog_address)).setText(info.address);
		((TextView) map_dialog.findViewById(R.id.dialog_tel)).setText(info.extras.get("tel") + "");

		((TextView) map_dialog.findViewById(R.id.comehere)).setTag(info);
		ImageView dialog_pic = (ImageView)map_dialog.findViewById(R.id.dialog_pic);
		dialog_pic.setVisibility(View.INVISIBLE);
		LoadImage loadImage = LoadImage.getinstall();
		loadImage.addTask(URLs.HOST + info.extras.get("picname"),dialog_pic);
		loadImage.doTask();

		map_dialog.setVisibility(View.VISIBLE);
		
		return false;
	}
	
	/**
	 * 详情云检索返回
	 */
	@Override
	public void onGetDetailSearchResult(DetailSearchResult result, int arg1) {
		if (result != null) {
			if (result.poiInfo != null) {
				Toast.makeText(this, result.poiInfo.title, Toast.LENGTH_SHORT).show();
				CloudPoiInfo info =  result.poiInfo;
				
				((TextView) map_dialog.findViewById(R.id.dialog_title)).setText(info.title);
				((TextView) map_dialog.findViewById(R.id.dialog_title)).setTag(info.extras.get("businessid") + "");
				((TextView) map_dialog.findViewById(R.id.dialog_address)).setText(info.address);
				((TextView) map_dialog.findViewById(R.id.dialog_tel)).setText(info.extras.get("tel") + "");

				((TextView) map_dialog.findViewById(R.id.comehere)).setTag(info);

				LoadImage loadImage = LoadImage.getinstall();
				loadImage.addTask(URLs.HOST + info.extras.get("picname"),(ImageView) map_dialog.findViewById(R.id.dialog_pic));
				loadImage.doTask();

				map_dialog.setVisibility(View.VISIBLE);
				
				
			} else {
				Toast.makeText(this, "status:" + result.status,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * 云检索返回
	 */
	@Override
	public void onGetSearchResult(CloudSearchResult result, int arg1) {
		mBaiduMap.clear();
		UIHelper.loadingClose();
		
		if (result != null && result.poiList != null && result.poiList.size() > 0) {
			Log.d("dd","onGetSearchResult, result length: " + result.poiList.size());

			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.position_red);
			LatLng ll;
			LatLngBounds.Builder builder = new Builder();
			cloudPoiInfos = new SparseArray<CloudPoiInfo>();
			for (CloudPoiInfo info : result.poiList) {

				ll = new LatLng(info.latitude, info.longitude);
				OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).title(info.uid + "");
				mBaiduMap.addOverlay(oo);
				cloudPoiInfos.append(info.uid, info);
				builder.include(ll);
			}

			LatLngBounds bounds = builder.build();
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
			mBaiduMap.animateMapStatus(u);
		} else {
			if("regionSearch".equals(CloudSearchType)){
				UIHelper.ToastMessage(mContext, "5千米内未找到代理商");
			}else if("LocalSearch".equals(CloudSearchType)){
				UIHelper.ToastMessage(mContext, LocalSearchRegion+"未找到代理商");
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		CloudManager.getInstance().destroy();

		mMapView.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		mLocClient.registerLocationListener(this);
		mLocClient.start();
		// 开启方向传感器
		myOrientationListener.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mBaiduMap.setMyLocationEnabled(false);
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		mLocClient.unRegisterLocationListener(this);
		mLocClient.stop();
		// 关闭方向传感器
		myOrientationListener.stop();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.position_my:
			
			UiSettings mUiSettings = mBaiduMap.getUiSettings();
			switch (mCurrentMode) {
			case NORMAL:

				mUiSettings.setCompassEnabled(true);
				mCurrentMode = LocationMode.COMPASS;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								mCurrentMode, true, null));
				// MapStatus COMPASS = new MapStatus.Builder().zoom(13).build();
				// MapStatusUpdate COMPASSMapStatusUpdate =
				// MapStatusUpdateFactory.newMapStatus(COMPASS);
				// 改变地图状态
				// mBaiduMap.setMapStatus(COMPASSMapStatusUpdate);
				break;
			case COMPASS:
				mUiSettings.setCompassEnabled(false);
				mCurrentMode = LocationMode.NORMAL;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								mCurrentMode, true, null));
				// MapStatus mapStatus = new
				// MapStatus.Builder().zoom(13).build();
				// MapStatusUpdate mapStatusUpdate =
				// MapStatusUpdateFactory.newMapStatus(mapStatus);
				// 改变地图状态
				// mBaiduMap.setMapStatus(mapStatusUpdate);
				break;
			case FOLLOWING:

				break;
			}

			break;
		case R.id.dialog_close_button:
			map_dialog.setVisibility(View.GONE);
			break;
		case R.id.dialog_submit:
			submitYuYue();
			break;
		case R.id.comehere:
			// 导航

			boolean isInitSuccess = BaiduNaviManager.getInstance().checkEngineStatus(getApplicationContext());
			
			if (isInitSuccess) {
				launchNavigator2(v.getTag());
			} else {
				initDaoHang();				
				UIHelper.ToastMessage(mContext, "请等待导航初始化，或重新点击地图上的代理商");
			}
			break;
		case R.id.left:
			finish();
			break;
		case R.id.right:
			Intent right = new Intent(getApplicationContext(),
					Personal_Center.class);
			startActivity(right);
			break;
		}
	}

	/**
	 * 指定导航起终点启动GPS导航.起终点可为多种类型坐标系的地理坐标。 前置条件：导航引擎初始化成功
	 */
	private void launchNavigator2(Object obj) {
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		if (locationBack == null) {
			UIHelper.ToastMessage(mContext, "起点不确认，不能导航");
			return;
		}

		CloudPoiInfo endpoint = (CloudPoiInfo) obj;
		BNaviPoint startPoint = new BNaviPoint(locationBack.longitude,
				locationBack.latitude, "我的位置",
				BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(endpoint.longitude,
				endpoint.latitude, endpoint.title,
				BNaviPoint.CoordinateType.BD09_MC);
		BaiduNaviManager.getInstance().launchNavigator(this, startPoint, // 起点（可指定坐标系）
				endPoint, // 终点（可指定坐标系）
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_DIST, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener() { // 跳转监听

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(getApplicationContext(),
								BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
					}

					@Override
					public void onJumpToDownloader() {
					}
				});
	}

	/**
	 * 提交预约
	 */
	private void submitYuYue() {

		EditText dialog_btuijian = (EditText) map_dialog.findViewById(R.id.dialog_btuijian);
		EditText dialog_name = (EditText) map_dialog.findViewById(R.id.dialog_name);
		
		final String name = dialog_name.getText() + "";
		final String btuijian = dialog_btuijian.getText() + "";
		if ("".equals(btuijian)) {
			UIHelper.ToastMessage(mContext, "请填写自己的手机号");
			return;
		}
		if ("".equals(name)) {
			UIHelper.ToastMessage(mContext, "请填写姓名");
			return;
		}
		UIHelper.loading("正在处理", this);

		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				EditText dialog_tuijian = (EditText) map_dialog.findViewById(R.id.dialog_tuijian);
				TextView dialog_title = (TextView) map_dialog
						.findViewById(R.id.dialog_title);

				YuYue yuyue = new YuYue();
				yuyue.setTuijianrenPhone(dialog_tuijian.getText() + "");
				yuyue.setYuyuePhone(btuijian);
				yuyue.setYuyueTime(StringUtils.getTime(System.currentTimeMillis(), "yyyy/MM/dd"));
				yuyue.setUserid(dialog_title.getTag() + "");
				yuyue.setName(name);
				if (order == null) {
					yuyue.setOrderId((System.currentTimeMillis() + "")
							.substring(1));
				} else {
					yuyue.setOrderId(order.getId());
				}
				yuyue.setSheng(areaSelect.mCurrentProviceName);
				yuyue.setShi(areaSelect.mCurrentCityName);
				yuyue.setQu(areaSelect.mCurrentDistrictName);
				if(areaSelect.addr != null){
					yuyue.setMessage(areaSelect.addr);
				}else{
					yuyue.setMessage(areaSelect.mCurrentProviceName+"_"+areaSelect.mCurrentCityName+"_"+areaSelect.mCurrentDistrictName);
				}

				Message msg = Message.obtain();
				try {
					boolean boo = mContext.SubmintYuYue(yuyue);
					msg.what = 1;
					msg.obj = boo;

				} catch (Exception e) {
					if (Log.LOG)
						e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handle.sendMessage(msg);
			}
		});

	}

	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			boolean b = false;
			switch (msg.what) {
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				break;
			case 1:
				b = (Boolean) msg.obj;

				map_dialog.setVisibility(View.GONE);

				if (order != null && b) {
					setResult(Order_AllOrder.mendianCode);
					finish();
				}
				break;
			}
			UIHelper.ToastMessage(mContext, b ? "预约成功" : "预约失败");
			UIHelper.loadingClose();
		};

	};

	@Override
	public void onMapClick(LatLng arg0) {
		//
		regionSearch(arg0);
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public void onTouch(MotionEvent arg0) {
		mBaiduMap.getUiSettings().setCompassEnabled(false);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
	}

}
