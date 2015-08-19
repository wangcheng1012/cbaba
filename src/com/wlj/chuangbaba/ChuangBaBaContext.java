package com.wlj.chuangbaba;

import static com.wlj.chuangbaba.web.MsgContext.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.activity.dailishang.DaiLiShang;
import com.wlj.chuangbaba.activity.personal.HuiYuanLogin;
import com.wlj.chuangbaba.bean.Banner;
import com.wlj.chuangbaba.bean.HeTong;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.ProjectTab;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.chuangbaba.bean.DaiJinQuan;
import com.wlj.chuangbaba.bean.YuYue;
import com.wlj.chuangbaba.web.HttpGet;
import com.wlj.chuangbaba.web.HttpPost;
import com.wlj.chuangbaba.web.MsgContext;
import com.wlj.chuangbaba.web.RequestWebClient;
import com.wlj.util.AppConfig;
import com.wlj.util.AppContext;
import com.wlj.util.AppException;
import com.wlj.util.Log;
import com.wlj.util.RequestException;
import com.wlj.util.StringUtils;
import com.wlj.util.UIHelper;
import com.wlj.web.URLs;

public class ChuangBaBaContext extends AppContext {
	
	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
	}

	public boolean GPSisOpen() {
		LocationManager locationManager = (LocationManager) getAppContext()
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		return gps;
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public boolean isOPen() {
		LocationManager locationManager = (LocationManager) getAppContext()
				.getSystemService(Context.LOCATION_SERVICE);

		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean AGPS = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (GPSisOpen() || AGPS) {
			return true;
		}
		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public void openGPS() {

		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(getAppContext(), 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			Log.e("dd", e.getMessage());
		}
	}

	/**
	 * 登录
	 * 
	 * @param catalog
	 * @param isRefresh
	 * @return
	 * @throws Exception
	 */
	public boolean Login(User u) throws Exception {
		User user = null;
		if (isNetworkConnected()) {
			try {
				user = RequestWebClient.getInstall().Login(u);

				if (user != null) {
					user.setName(u.getName());
					setProperty(AppConfig.CONF_NAME, user.getName());
					setProperty(AppConfig.CONF_TYPT, user.getType());
					setProperty(AppConfig.CONF_KEY, user.getKey());
					return true;
				}
			} catch (AppException e) {
				throw e;
			}
		}
		throw new RequestException("网络异常");
	}

	public void loginOut() {
		setProperty(AppConfig.CONF_NAME, "");
		setProperty(AppConfig.CONF_TYPT, "");
		setProperty(AppConfig.CONF_KEY, "");
	}


	public boolean uploadHeTong(HeTong hetong) throws Exception {
		
		if (isNetworkConnected()) {
			if(User.type_dailishang.equals(getProperty(AppConfig.CONF_TYPT))){
				
				return RequestWebClient.getInstall().uploadHeTong(hetong,getProperty(AppConfig.CONF_KEY),getProperty(AppConfig.CONF_NAME));
			}else{
				throw new RequestException("请用代理商帐号登录");
			}
		}
		throw new RequestException("网络异常");
	}

	/**
	 * topBanner   tiantiantejia  tuijianshangpin
	 * @return
	 */
	public Map<String,List<Banner>> getBanner() {

		Map<String,List<Banner>> arrayList=  new HashMap<String, List<Banner>>();
		try {
			HttpGet hg = new HttpGet(URLs.indexBanner);

			String result = hg.getResult();

			Log.i("dd", result);

			JSONObject jsonobject = new JSONObject(result);

			JSONObject json_data = jsonobject.getJSONObject("data");

			String state = jsonobject.get("state").toString();

			if (MsgContext.request_success.equals(state) && json_data != null) {
				
				Iterator keys = json_data.keys();
				while (keys.hasNext()) {
					List<Banner> blist = new ArrayList<Banner>();
					Object next = keys.next();
					
					JSONArray jsonArray = json_data.getJSONArray(next+"");
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							String string = jsonArray.getString(i);
							Banner banner = new Banner();
							banner.setPicurl(string);
							blist.add(banner);
						}
					}
					arrayList.put(next+"",blist);
				}
			}

		} catch (Exception e) {
			Log.e("httpget", e.getMessage());
		}

		return arrayList;
	}

	public boolean SubmintYuYue(YuYue yuyue) throws Exception {
		
			HttpPost hp = new HttpPost(new URL(URLs.yuyue));

			hp.addParemeter("yuyuePhone", yuyue.getYuyuePhone());
			hp.addParemeter("yuyueTime", yuyue.getYuyueTime());
			hp.addParemeter("tuijianrenPhone", yuyue.getTuijianrenPhone());
			hp.addParemeter("sheng", yuyue.getSheng());
			hp.addParemeter("shi", yuyue.getShi());
			hp.addParemeter("qu", yuyue.getQu());
			hp.addParemeter("userId", yuyue.getUserid());
			hp.addParemeter("message", yuyue.getMessage());
			hp.addParemeter("orderId", yuyue.getOrderId());
			hp.addParemeter("name", yuyue.getName());

			String result = hp.getResult();
			JSONObject jsonobject = new JSONObject(result);

			String state = jsonobject.get("state").toString();

			if (MsgContext.request_success.equals(state)) {
				return true;
			}else if(MsgContext.request_false.equals(state)){
				throw new RequestException(jsonobject.getString("description"));
			}
			
			return false;
	}

	public BaseList getYuYueList(Base base,int pageIndex, boolean isRefresh)
			throws Exception {

		if (!User.type_dailishang.equals(getProperty(AppConfig.CONF_TYPT))) {
			throw new RequestException("请先登录");
		}
		BaseList list = null;
		String key = "yuyue_" + getProperty(AppConfig.CONF_TYPT) + "_"
				+ pageIndex + "_" + PAGE_SIZE;
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = RequestWebClient.getInstall().getYuYueList(base,
						getProperty(AppConfig.CONF_NAME), pageIndex,
						isRefresh, getProperty(AppConfig.CONF_KEY));
				if (list != null && pageIndex == 0) {
					saveObject(list, key);
				}
			} catch (Exception e) {
				list = (BaseList) readObject(key);
				if (list == null)
					throw new RequestException(e.getMessage());
			}
		} else {
			list = (BaseList) readObject(key);
			if (list == null)
				throw new RequestException("网络异常");
		}
		return list;
	}

	/**
	 * 获取产品分类列表
	 * 
	 * @return
	 * @throws AppException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws JSONException
	 */
	public List<ProjectTab> getProjectTab() throws Exception {
		if (!isNetworkConnected()) {
			throw new RequestException("网络异常");
		}
		HttpPost hp = new HttpPost(new URL(URLs.getProductFenleiList));

		String result = hp.getResult();

		JSONObject jSONObject = new JSONObject(result);

		String state = jSONObject.getString("state");
		List<ProjectTab> list = null;
		if (MsgContext.request_success.equals(state)) {
			JSONArray data = jSONObject.getJSONArray("data");
			list = new ArrayList<ProjectTab>();
			for (int i = 0; i < data.length(); i++) {
				JSONObject tmp = data.getJSONObject(i);
				ProjectTab projectTab = new ProjectTab();
				projectTab.setListid(tmp.getString("id"));
				projectTab.setName(tmp.getString("name"));
				list.add(projectTab);
			}
		}

		return list;
	}


	/**
	 * 根据产品分类id取产品列表
	 * 
	 */
	public BaseList getProductListByFenleiId(Map<String, String> paremeter,
			boolean isRefresh) throws Exception {

		BaseList list = null;
		String key = "projectlist_" + paremeter.get("fenleiid") + "_"
				+ paremeter.get(key_page) + "_" + paremeter.get(key_pageSize);
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {

			try {
				list = RequestWebClient.getInstall().getProductListByFenleiId(paremeter);
				if (list != null && StringUtils.toInt(paremeter.get("pageIndex"), 0) == 1) {
					saveObject(list, key);
				}
			} catch (Exception e) {
				list = (BaseList) readObject(key);
				if (Log.LOG) {
					e.printStackTrace();
				}
				if (list == null)
					throw new RequestException("网络或者数据异常", e);
			}
		} else {
			list = (BaseList) readObject(key);
			if (list == null)
				throw new RequestException("数据为空");
		}
		return list;
	}

	/**
	 * 根据产品id取产品详情
	 * 
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Project getProjectfromid(String id) throws Exception {
		if (isNetworkConnected()) {
			return RequestWebClient.getInstall().getProjectfromid(id);
		}
		throw new RequestException("网络异常");
	};

	/**
	 * 根据分类id取文章列表
	 * 
	 * @param base
	 * @param typeid
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws Exception
	 */
	public BaseList getPubListByFeileiId(Base base, String typeid,
			int pageIndex, boolean isRefresh) throws Exception {

		BaseList list = null;
		Map<String, String> paremeter = new HashMap<String, String>();
		paremeter.put(key_page, pageIndex + "");
		paremeter.put(key_pageSize, MsgContext.pageSize + "");
		paremeter.put("fenleiid", typeid);

		String key = "publist_" + typeid + "_" + pageIndex + "_" + PAGE_SIZE;
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = RequestWebClient.getInstall().getPubListByFeileiId(base, paremeter);
				if (list != null && pageIndex == 0) {
					saveObject(list, key);
				}
			} catch (Exception e) {
				list = (BaseList) readObject(key);
				if (list == null)
					throw new RequestException("网络或者数据异常", e);
			}
		} else {
			list = (BaseList) readObject(key);
			if (list == null) {
				throw new RequestException("数据为空");
			}
		}
		return list;
	}

	/**
	 * 根据文章id取文章
	 * 
	 * @param base
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Base getPubById(Base base, String id) throws Exception {
		if (isNetworkConnected()) {

			return RequestWebClient.getInstall().getPubById(base, id);
		}

		throw new RequestException("网络异常");
	}
	
	/**
	 * 相关阅读
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public BaseList getPubXiangguanByPub(String id) throws Exception {

		if (isNetworkConnected()) {

			BaseList pub = RequestWebClient.getInstall().getPubXiangguanByPub(id);
			return pub;
		}
		throw new RequestException("网络异常");
	}

	/**
	 * 参考报价
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getCanKaoPrice(HashMap<String, String> map) throws Exception {

		if (isNetworkConnected()) {

			String pub = RequestWebClient.getInstall().getCanKaoPrice(map);
			return pub;
		}
		throw new RequestException("网络异常");
	}

	/**
	 * 验证码
	 * 
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public String getLoginRand(String phone) throws Exception {

		if (isNetworkConnected()) {

			String pub = RequestWebClient.getInstall().getLoginRand(phone);
			return pub;
		}
		throw new RequestException("网络异常");

	}

	/**
	 * 需要缓存
	 * @param base 
	 * @param map cachekey
	 * @param isRefresh
	 * @return
	 * @throws Exception
	 */
	public BaseList getHaveCacheBaseList(Activity activity,Base base, Map<String, Object> map,
			boolean isRefresh) throws Exception {

		BaseList list = null;
		String key = 	getProperty(AppConfig.CONF_TYPT) + "_"
							+ getProperty(AppConfig.CONF_NAME) + "_"
							+ base.getClass().getSimpleName() + "_" 
							+ map.get("cachekey")
							+ "_" + map.get(key_page) + "_" 
							+ map.get(key_pageSize);
		Log.w("key", key);
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = Request(activity,map, base);
				if (list != null) {
					saveObject(list, key);
				}
			} catch (AppException e) {
				list = (BaseList) readObject(key);
				if (list == null)
					throw e;
			}
		} else {
			list = (BaseList) readObject(key);
			if (list == null)
				throw new RequestException("数据为空");
		}
		return list;
	}
	
	/**
	 * 
	 * @param base 基础模型
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public BaseList Request(Activity activity, Map<String, Object>  map,Base base) throws Exception{
		Log.w("map", map.toString());
		if (isNetworkConnected()) {
			if (map.containsKey("user_Type")) {
				if ((map.get("user_Type")+"").equals(getProperty(AppConfig.CONF_TYPT))) {
					map.remove("user_Type");
					return RequestWebClient.getInstall().Request(map,base,getProperty(AppConfig.CONF_KEY),getProperty(AppConfig.CONF_NAME));
				} else {
					String str = "请先登录代理商帐号";
					if ((map.get("user_Type")+"").equals(User.type_huiyuan)) {
						str = "请先登录会员帐号";
						activity.startActivityForResult(new Intent(this,HuiYuanLogin.class),11);
					}else{
						activity.startActivityForResult(new Intent(this,DaiLiShang.class),11);
					}
					throw new RequestException(str);
				}
			} else {
				return RequestWebClient.getInstall().Request(map,base,getProperty(AppConfig.CONF_KEY),getProperty(AppConfig.CONF_NAME));
			}
		}
		throw new RequestException("网络异常");
	}

	
}
