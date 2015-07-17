package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.web.RequestWebClient;

public class HeTong extends Base {

	private static final long serialVersionUID = -4164857100950589227L;
	private String bianhao;
	private String money;
	private String phone;
	private String orderbianhao;
	private String beizhu;
	private String state;
	private String pic;
	private String time;
	private String userid;
	private String orderNo;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOrderbianhao() {
		return orderbianhao;
	}

	public void setOrderbianhao(String orderbianhao) {
		this.orderbianhao = orderbianhao;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUser() {
		return userid;
	}

	public void setUser(String user) {
		this.userid = user;
	}

	public String getBianhao() {
		return bianhao;
	}

	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	@Override
	public Base parse(JSONObject tmp) throws JSONException {
		HeTong heTong = new HeTong();
		heTong.setId(RequestWebClient.getJsonString(tmp, "id"));
		heTong.setBianhao(RequestWebClient.getJsonString(tmp, ""));
		heTong.setMoney(RequestWebClient.getJsonString(tmp, ""));
		heTong.setPhone(RequestWebClient.getJsonString(tmp, ""));
		heTong.setOrderbianhao(RequestWebClient.getJsonString(tmp, ""));
		heTong.setBeizhu(RequestWebClient.getJsonString(tmp, ""));
		heTong.setState(RequestWebClient.getJsonString(tmp, ""));
		heTong.setTime(RequestWebClient.getJsonString(tmp, ""));
		return heTong;
	}
}