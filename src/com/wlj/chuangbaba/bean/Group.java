package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;

/**
 * 铝材零售 分组
 * 
 * @author wlj
 * 
 */
public class Group extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063381269778270746L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
//{"data":[{"id":"55b1f33fd6c459501b581c23","name":"铝材3"},{"id":"55b1f33bd6c459501b581c22","name":"铝材2"},{"id":"55b1f337d6c459501b581c21","name":"铝材1"}],"state":2}
	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		Group ic = new Group();
		ic.setId(jsonObject.optString("id"));
		ic.setName(jsonObject.optString("name"));
		
		return ic;
	}
	
	
}