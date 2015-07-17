package com.wlj.chuangbaba.web;

import java.io.File;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.util.AppContext;
import com.wlj.util.AppException;
import com.wlj.util.RequestException;

public class MsgContext  {

	public static final String request_false = "1";
	
	public static final String request_success = "2";


	public static final String request_system_error = "3";

	public static final String key_business = "business";
	public static final String key_page = "page";
	public static final String key_pageSize = "pageSize";
	
	public static final int pageSize = 10;

	//热门资讯 、公司动态、行业动态
	public static final String id_hot = "554c8fa8d6c45923169ce936";
	public static final String id_companyDynamic = "554c8fd5d6c45923169ce937";
	public static final String id_hangyeDynamic = "554c8febd6c45923169ce938";
	//窗爸爸
	public static final String id_chuangbabaxuanyan = "554c924fd6c45923169ce93a";
	//新美鱼
	public static final String id_xinmeiyu = "554c93afd6c45923169ce948";
	
	// 获取代理商的所有订单
	public static final String getAllMyOrders_bussness = "getAllMyOrders";
	
	public static final Integer tuijian_yes = 			1;
	public static final Integer tuijian_no = 			2;
	public static final Integer tuijian_tejia = 		3;
	
	public static final String login = "login";
	public static final String key_username = "username";
	public static final String key_pwd = "pwd";
	
	

	/**
	 */
	public static String getFile(File file) {
		return null;
	}

	/**
	 * 将请求的报文发送到服务器
	 * 
	 * @param requestData
	 * @return jsonobject或者jsonarray
	 * @throws AppException
	 */
	public static Object getDataFromWeb(AppContext appContext, String url,
			Map<String, Object> params, Map<String, File> files)
			throws RequestException, AppException {

		if (files != null) {
			Set<String> keySet = files.keySet();

			for (String string : keySet) {
				params.put(string, getFile(files.get(string)));
			}
		}

		String key = "e5d50a5bb7a7e23c5441ef29";
		String name = "facema";
		String randCode = System.currentTimeMillis() + "";

		HttpPost hp = null;

		String tempKey = Md5Util.MD5Normal(key + randCode);

		Encrpt encrpt = new EnAes();

		try {

			hp = new HttpPost(new URL(url));
			hp.addParemeter("name", name);
			hp.addParemeter("randCode", randCode);
			hp.addParemeter("encrpt", "enAes");

			JSONObject jo = new JSONObject(params);

			String reqxml = jo.toString();

			String enStr = encrpt.encrypt(reqxml, tempKey);
			hp.addParemeter("data", enStr);
			String mac = Md5Util.MD5Normal(tempKey + name + enStr);
			hp.addParemeter("mac", mac);

		} catch (Exception e) {
			// 缴费或查询失败 如果是缴费 这个当缴费失败处理
			e.printStackTrace();
		}
		JSONObject jsonobject = null;
		String result;
		try {
			result = hp.getResult();
			if (!result.contains("<")) {
				result = encrpt.decrypt(result, tempKey);
			}
			// String jsonstr = _post(appContext, url, params, files);

			jsonobject = new JSONObject(result);
			// "{state:'1',data:[{},{},{}]}"
			Object jsonElement = jsonobject.get("data");

			String state = jsonobject.get("state").toString();

			String errormsg = jsonobject.get("errormsg").toString();

			if (MsgContext.request_success.equals(state)) {
				return jsonElement;
			} else if (MsgContext.request_false.equals(state)) {
				RequestException requestException = new RequestException(
						errormsg);
				throw requestException;
			} else {
				RequestException requestException = new RequestException("网络繁忙！");
				throw requestException;
			}

		} catch (ConnectException e) {
			// 链接未打开缴费或查询失败 如果是缴费 这个当缴费失败处理
			e.printStackTrace();
		} catch (Exception e) {
			// 解密失败或者通讯失败 如果是缴费两种失败都把这次缴费挂起 处理
			e.printStackTrace();
		}
		return null;

	}

}
