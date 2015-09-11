package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;

/**
 * 创意
 * 
 * @author wlj
 * 
 */
public class ChuangYi extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7003215615004162041L;
	private String name;
	private String content;
	private String time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		ChuangYi ic = new ChuangYi();
		ic.setId(jsonObject.optString("id"));
		ic.setName(jsonObject.optString("name"));
		ic.setContent(jsonObject.optString("content"));
		ic.setTime(jsonObject.optString("time"));
		return ic;
	}

}