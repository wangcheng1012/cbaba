package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.web.RequestWebClient;
import com.wlj.util.AppContext;
import com.wlj.util.ExecutorServices;

/**
 * banner and tab
 * 
 * @author wlj
 * 
 */
public class Banner extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4470635070037957458L;

	
	private String title;

	private String picurl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	@Override
	public Banner parse(JSONObject jsonObject) throws JSONException {
		Banner banner = new Banner();
		banner.setId(RequestWebClient.getJsonString(jsonObject, "id"));
		banner.setTitle(RequestWebClient.getJsonString(jsonObject, "name"));
		banner.setPicurl(RequestWebClient.getJsonString(jsonObject, "pic"));
		
		return banner;
	}

	@Override
	public List<Base> parseList(JSONObject jsonObject) throws JSONException {
		
		List<Base> list = new ArrayList<Base>();

		JSONArray data = jsonObject.getJSONArray("data");

		for (int i = 0; i < data.length(); i++) {
			JSONObject tmp = data.getJSONObject(i);

			list.add(parse(tmp));
		}

		return list;
	}
	@Override
	public void getThisfromid(final AppContext mContext, final Handler handle){
		
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Banner p;
				Message msg = new Message();
				try {
					p = (Banner) ((ChuangBaBaContext)mContext).getPubById(new Banner(),getId());
					msg.what = 1;
					msg.obj = p;
				} catch (Exception e) {
					msg.what = -1; 
					msg.obj = e;
				}
				handle.sendMessage(msg);
			}
		});
		
	}
	
}