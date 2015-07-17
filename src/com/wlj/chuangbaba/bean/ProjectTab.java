package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.util.AppException;

/**
 * 商品分类
 * 
 * @author wlj
 * 
 */
public class ProjectTab extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162982045382192077L;

	private String listid;
	private String name;

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		return null;
	}

}