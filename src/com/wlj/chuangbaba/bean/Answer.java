package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;

/**
 * 回答
 * 
 * @author wlj
 * 
 */
public class Answer extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162982045382192077L;

	/**
	 * 内容
	 */
	private String context;

	private String time;
	/**
	 * 回答者的id
	 */
	private String userName;


	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		Answer answer = new Answer();
		answer.setContext(jsonObject.optString("Context"));
		answer.setId(jsonObject.optString("id"));
		answer.setTime(jsonObject.optString("time"));
		answer.setUserName(jsonObject.optString("username"));
		return answer;
	}

}