package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;

/**
 * 专卖店
 * 
 * @author wlj
 * 
 */
public class DaiLiShangDian extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4921977182496567010L;

	private String name;
	private String addr;
	private String phone;
	private String xingji;
	private String pic;
	/**
	 * 地图id
	 */
	private int uid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getXingji() {
		return xingji;
	}

	public void setXingji(String xingji) {
		this.xingji = xingji;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	// {"name":"厦门市山头铝业有限公司","addr":"福建省厦门市同安区下溪头村南大铝材仓库","phone":"13906037257","xingji":"1","pic":""}
	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {

		DaiLiShangDian zhuanMaiDian = new DaiLiShangDian();
		zhuanMaiDian.setId(jsonObject.optString("id"));
		zhuanMaiDian.setName(jsonObject.optString("name"));
		zhuanMaiDian.setAddr(jsonObject.optString("addr"));
		zhuanMaiDian.setPhone(jsonObject.optString("phone"));
		zhuanMaiDian.setXingji(jsonObject.optString("xingji"));
		zhuanMaiDian.setPic(jsonObject.optString("pic"));
		zhuanMaiDian.setUid(jsonObject.optInt("baiduId"));

		return zhuanMaiDian;
	}

}