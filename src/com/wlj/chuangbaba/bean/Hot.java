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
 * 热门资讯
 * 
 * @author wlj
 * 
 */
public class Hot extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6048390402507398813L;

	private String id;

	private String title;

	private String time;

	private String jianjie;

	private String itempic;

	/**
	 * 内容
	 */
	private String context;

	private String viewcontext;

	public String getViewcontext() {
		return viewcontext;
	}

	public void setViewcontext(String viewcontext) {
		this.viewcontext = viewcontext;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getJianjie() {
		return jianjie;
	}

	public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
	}

	public String getItempic() {
		return itempic;
	}

	public void setItempic(String itempic) {
		this.itempic = itempic;
	}

	// public String getType() {
	// return type;
	// }
	//
	// public void setType(String type) {
	// this.type = type;
	// }

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public Hot parse(JSONObject jsonObject) throws JSONException {
		Hot hot = new Hot();
		hot.setId(RequestWebClient.getJsonString(jsonObject, "id"));
		hot.setTitle(RequestWebClient.getJsonString(jsonObject, "name"));
		hot.setTime(RequestWebClient.getJsonString(jsonObject, "time"));
		hot.setJianjie(RequestWebClient.getJsonString(jsonObject, "intro"));
		hot.setItempic(RequestWebClient.getJsonString(jsonObject, "pic"));
		hot.setViewcontext(RequestWebClient.getJsonString(jsonObject, "viewcount"));
		hot.setContext(RequestWebClient.getJsonString(jsonObject, "content"));

		return hot;
	}

	public void getThisfromid(final AppContext mContext, final Handler handle){

		ExecutorServices.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				Hot p;
				Message msg = new Message();
				try {
					p = (Hot) ((ChuangBaBaContext)mContext).getPubById(new Hot(), id);
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