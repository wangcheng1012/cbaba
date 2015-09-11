package com.wlj.chuangbaba.web;

import static com.wlj.chuangbaba.web.MsgContext.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.bean.DaiLiShangDian;
import com.wlj.chuangbaba.bean.HeTong;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.chuangbaba.bean.DaiJinQuan;
import com.wlj.util.AppConfig;
import com.wlj.util.AppContext;
import com.wlj.util.AppException;
import com.wlj.util.Log;
import com.wlj.util.MathUtil;
import com.wlj.util.RequestException;
import com.wlj.util.StringUtils;

/**
 * 
 * 添加请求参数 Map<String, Object> params 处理返回参数
 * 
 * @author wlj
 * 
 */
public class RequestWebClient {

	private static RequestWebClient requestWebClient;

	public static RequestWebClient getInstall() {
		if (requestWebClient == null) {
			requestWebClient = new RequestWebClient();
		}
		return requestWebClient;
	}

	public static String getJsonString(JSONObject tmp, String key) {

		Object object = null;
		try {
			object = tmp.get(key);
		} catch (JSONException e) {
		}
		if (object == null) {
			return null;
		}
		return object.toString();
	}

	public static int getJsonInt(JSONObject tmp, String key) {

		Object object = null;
		try {
			object = tmp.get(key);
		} catch (JSONException e) {
		}
		if (object == null) {
			return 0;
		}

		return StringUtils.toInt(object);
	}

	public User Login(User u) throws Exception {

		String pwd = u.getPassword();

		String randCode = u.getRandCode();

		HttpPost hp = new HttpPost(new URL(URLs.login ));

		if (randCode != null && !"".equals(randCode)) {// 验证码登录
			hp.addParemeter("randCode", randCode);
			hp.addParemeter("username", u.getName());
		} else {
			String rand = System.currentTimeMillis() + "";
			hp.addParemeter("username", u.getName());
			hp.addParemeter("rand", rand);
			hp.addParemeter("pwd",Md5Util.MD5Normal(Md5Util.MD5Normal(pwd) + rand));
		}

		String result = hp.getResult();
		Log.e("Login", result);
		JSONObject jSONObject = new JSONObject(result);
		String state = jSONObject.getString("state");
		// {"type":3,"key":"de3572e4391c92db60ad4ab4","state":2}
		if (MsgContext.request_success.equals(state)) {
			User user = new User();
			user.setKey(jSONObject.getString("key"));
			user.setType(jSONObject.getString("type"));
			JSONObject userjSONObject = jSONObject.getJSONObject("user");
			user.setRealname(userjSONObject.getString("realname"));
			return user;
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	public BaseList getProductListByFenleiId(Map<String, String> paremeter)
			throws Exception {

		HttpPost hp = new HttpPost(new URL( URLs.getProductListByFenleiId));

		Set<String> keySet = paremeter.keySet();
		for (String string : keySet) {
			hp.addParemeter(string, paremeter.get(string));
		}

		// hp.addParemeter("fenleiid", paremeter.get("fenleiid"));
		// hp.addParemeter(key_page, paremeter.get(key_page) );
		// hp.addParemeter(key_pageSize, paremeter.get(key_pageSize));
		// hp.addParemeter("type", paremeter.get("type"));

		long mill = System.currentTimeMillis();
		String result = hp.getResult();
		Log.e("getProductListByFenleiId", result);
		Log.d("dd", (System.currentTimeMillis() - mill) + "  result");

		JSONObject jSONObject = new JSONObject(result);

		String state = jSONObject.getString("state");

		BaseList projectList = new BaseList();

		if (MsgContext.request_success.equals(state)) {
			long millis = System.currentTimeMillis();
			projectList
					.setPageIndex(StringUtils.toInt(paremeter.get(key_page)));
			BaseList vv = projectList.parse(jSONObject, new Project());
			Log.d("dd", (System.currentTimeMillis() - millis) + "  parse");
			return vv;
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	/**
	 * 根据Project id取Project详情
	 * 
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Project getProjectfromid(String id) throws Exception {

		HttpPost hp = new HttpPost(new URL(URLs.getProjectfromid));

		hp.addParemeter("id", id);
		String result = hp.getResult();
		Log.e("getProjectfromid", result);
		JSONObject object = new JSONObject(result);

		String state = object.getString("state");

		if (MsgContext.request_success.equals(state)) {
			return new Project().parse(object.getJSONObject("data"));
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(object.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	/**
	 * 热门资讯、公司动态、行业动态
	 * 
	 * @param typeid
	 *            热门资讯、公司动态、行业动态的id
	 * @param chuangBaBaContext
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public BaseList getPubListByFeileiId(Base base,Map<String, String> paremeter) throws Exception {

		HttpPost hp = new HttpPost(new URL(URLs.getPubByFenleiid));

		Set<String> keySet = paremeter.keySet();
		for (String string : keySet) {
			hp.addParemeter(string, paremeter.get(string));
		}

		// HttpPost hp = new HttpPost(new URL(URLs.HOST+
		// URLs.getPubByFenleiid));
		// hp.addParemeter(key_page, pageIndex + "");
		// hp.addParemeter(key_pageSize, pageSize + "");
		// hp.addParemeter("fenleiid", typeid);

		String result = hp.getResult();
		Log.e("getPubListByFeileiId", result);
		JSONObject jSONObject = new JSONObject(result);

		String state = jSONObject.getString("state");
		BaseList baseList = new BaseList();
		if (MsgContext.request_success.equals(state)) {
			baseList.setPageIndex(StringUtils.toInt(paremeter.get(key_page)));
			return baseList.parse(jSONObject, base);
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	/**
	 * 根据getPubListByFeileiId 返回的list里面的id 取pub详情
	 * 
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Base getPubById(Base base, String id) throws Exception {

		HttpPost hp = new HttpPost(new URL(URLs.getPubById));

		hp.addParemeter("id", id);
		String result = hp.getResult();
		Log.e("getPubById", result);
		JSONObject object = new JSONObject(result);

		String state = object.getString("state");

		if (MsgContext.request_success.equals(state)) {
			return base.parse(object.getJSONObject("data"));
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(object.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	/**
	 * 相关阅读
	 * 
	 * @param base
	 * @param id
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public BaseList getPubXiangguanByPub(String id) throws Exception {

		HttpPost hp = new HttpPost(new URL(URLs.getPubXiangguanByPub));

		hp.addParemeter("id", id);

		String result = hp.getResult();
		Log.e("getPubXiangguanByPub", result);
		JSONObject jSONObject = new JSONObject(result);

		String state = jSONObject.getString("state");

		if (MsgContext.request_success.equals(state)) {

			return new BaseList().parse(jSONObject, new Hot());
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");

	}

	public String getCanKaoPrice(HashMap<String, String> map) throws Exception {

		HttpPost hp = new HttpPost(new URL(URLs.getCanKaoPrice));
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
			hp.addParemeter(string, map.get(string));
		}
		Log.e("getCanKaoPrice1", map.toString());
		String result = hp.getResult();// {"description":"未找到此商品价格配置!","state":"1"}
		Log.e("getCanKaoPrice", result);

		JSONObject jSONObject = new JSONObject(result);
		String state = jSONObject.getString("state");

		if (MsgContext.request_success.equals(state)) {

			return jSONObject.getString("priceFen");
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	public String getLoginRand(String phone) throws Exception {

		HttpPost hp = new HttpPost(new URL( URLs.getLoginRand));

		hp.addParemeter("username", phone);

		String result = hp.getResult();
		Log.e("getLoginRand", result);
		JSONObject jSONObject = new JSONObject(result);

		String state = jSONObject.getString("state");

		if (MsgContext.request_success.equals(state)) {

			return jSONObject.getString("description");
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jSONObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 上传合同
	 * 
	 * @param chuangBaBaContext
	 * 
	 * @param hetong
	 * @param chuangBaBaContext
	 * @return
	 * @throws Exception
	 */
	public boolean uploadHeTong(HeTong hetong,String key,String name) throws Exception {

		JSONObject json = new JSONObject();
		json.put("hetongbianhao", hetong.getBianhao());
		json.put("priceYuan", hetong.getMoney());
		json.put("memo", hetong.getBeizhu());
		json.put("pic", hetong.getPic());
		json.put("id", hetong.getOrderNo());

		String request_data = BaseWebMain.request_data( URLs.uploadHetong, key, name, json.toString());
		if (request_data == null) {
			throw new RequestException("数据异常");
		}
		Log.e("uploadHeTong", request_data);
		JSONObject jsonObject = new JSONObject(request_data);
		String state = jsonObject.getString("state");

		if (MsgContext.request_success.equals(state)) {

			return true;
		} else if (MsgContext.request_false.equals(state)) {
			// {"state":1,"description":"登录信息过期,请重新登录!"}
			throw new RequestException(jsonObject.getString("description"));
		}

		return false;
	}

	public BaseList getYuYueList(Base base, String name, int pageIndex,
			boolean isRefresh, String userkey) throws Exception {

		String key = userkey;
		String json = "{'page':'" + pageIndex + "','pageSize':'" + pageSize + "'}";
		String request_data = BaseWebMain.request_data(URLs.getYuyueList, key, name,json);

		JSONObject jsonObject = new JSONObject(request_data);

		String state = jsonObject.getString("state");
		BaseList projectList = new BaseList();
		if (MsgContext.request_success.equals(state)) {
			long millis = System.currentTimeMillis();
			projectList.setPageIndex(pageIndex);
			BaseList vv = projectList.parse(jsonObject, base);
			Log.d("dd", (System.currentTimeMillis() - millis) + "  parse");
			return vv;
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {
			throw new RequestException(jsonObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}

	public boolean wenDa(Wen wen) throws Exception {

		JSONObject json = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		jsonArray.put(wen.getPic1());
		jsonArray.put(wen.getPic2());

		json.put("pics", jsonArray);

		json.put("wen", wen.getWenti());
		json.put("phone", wen.getPhone());

		User user = wen.getUser();

		String request_data = BaseWebMain.request_data( URLs.tiwen,user.getKey(), user.getName(), json.toString());
		if (request_data == null) {
			throw new RequestException("数据异常");
		}
		Log.e("soucang", request_data);
		JSONObject jsonObject = new JSONObject(request_data);
		String state = jsonObject.getString("state");

		if (MsgContext.request_success.equals(state)) {
			return true;
		} else if (MsgContext.request_false.equals(state)) {
			// {"state":1,"description":"登录信息过期,请重新登录!"}
			throw new RequestException(jsonObject.getString("description"));
		}
		return false;
	}
	
	private JSONObject request(Map<String, Object> map,String key,String name) throws Exception {
		String request_data = null;
		
		if (map == null)
			throw new RequestException("参数为空");
		Object url = map.get("url");
		map.remove("url");
		if (url == null)
			throw new RequestException("地址为空");

		//是否需要加密
		if (map.containsKey("bujiami")) {
			// map 有不加密 key
			HttpPost hp = new HttpPost(new URL(url + ""));

			Set<String> set = map.keySet();
			for (String string : set) {
				hp.addParemeter(string, map.get(string)+"");
			}
			
			if (map.get(key_page) != null) {
				hp.addParemeter(key_page, map.get(key_page)+"");

				if (map.get(key_pageSize) == null) {
					hp.addParemeter(key_pageSize, pageSize + "");
				}
			}
			
			request_data = hp.getResult();

		} else {
			// map 没有不加密 key，不管vaule是什么，都认为需要加密
			JSONObject json = new JSONObject();

			Set<String> set = map.keySet();
			for (String string : set) {
				json.put(string, map.get(string));
			}

			if (map.get(key_page) != null) {
				json.put(key_page, map.get(key_page));

				if (map.get(key_pageSize) == null) {
					json.put(key_pageSize, pageSize + "");
				}
			}

			request_data = BaseWebMain.request_data(url + "", key, name,
					json.toString());
		}

		if (request_data == null) {
			throw new RequestException("数据异常");
		}
		Log.w(url.toString().substring(url.toString().lastIndexOf("/")),request_data);

		return new JSONObject(request_data);
	}

	/**
	 * 自己定义返回
	 * 
	 * @param base 为null 则反回null
	 * @param map
	 * @throws Exception
	 */
	public BaseList Request(Map<String, Object> map, Base base,String key,String name)throws Exception {

		JSONObject jsonObject = request(map,key,name);

		String state = jsonObject.optString("state");

		if (MsgContext.request_success.equals(state)) {
			if (base == null){
				return null;
			}

			BaseList projectList = new BaseList();
			long millis = System.currentTimeMillis();
			projectList.parse(jsonObject, base);
			Log.d("dd", (System.currentTimeMillis() - millis) + "  parse");
			return projectList;
		} else if (MsgContext.request_false.equals(state)
				|| MsgContext.request_system_error.equals(state)) {

			throw new RequestException(jsonObject.getString("description"));
		}
		throw new RequestException("获取数据失败");
	}


}
