package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;

/**
 * 首页城市
 * 
 * @author wlj
 * 
 */
public class IndexCity extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5318483906706284882L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		IndexCity ic = new IndexCity();
		ic.setId(jsonObject.optString("id"));
		ic.setName(jsonObject.optString("name"));
		
		return ic;
	}
//{"data":["重庆","四川","贵州","云南","上海","浙江","福建","广东","湖南"],"state":2}
	@Override
	public List<Base> parseList(JSONObject jsonObject) throws JSONException {
		
		List<Base> list = new ArrayList<Base>();
		
		JSONArray data = jsonObject.optJSONArray("data");
	
		if(data != null){
			for (int i = 0; i < data.length(); i++) {
				IndexCity indexCity = new IndexCity();
				indexCity.setName(data.optString(i));
				list.add(indexCity);
			}
		}
		return list;
	
	}
	
	
}