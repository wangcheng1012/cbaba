package com.wlj.chuangbaba.bean;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.web.RequestWebClient;
import com.wlj.util.MathUtil;

public class DaiJinQuan extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3503592793374262079L;

	/**
	 * 可用
	 */
	public static final Integer daijinquan_state_nomal = 			1;
	/**
	 * 不可用
	 */
	public static final Integer daijinquan_state_false = 			2;
	/**
	 * 已经使用
	 */
	public static final Integer daijinquan_state_yiyong		 = 3;
	/**
	 * 使用中
	 */
	public static final Integer daijinquan_state_doing		 =    4;
	/**
	 * 过期优惠券
	 */
	public static final Integer daijinquan_state_yiguoqi		 =    5;
	
	/**
	 * 奖券
	 */
	public static final Integer daijinquan_state_choujiang		 =    6;

	private String pic;
	private String title;
	private String time;
	private String money;
	private String startTime;
	private String endTime;
	private String bianma;
	private int type;
	
	private DecimalFormat  df = new DecimalFormat("0.00");;
	
	public String getshow(){
		
		return (getTitle() + "   ("+ df.format(MathUtil.parseDouble(getMoney()) / 100)+ "元)");
	}
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBianma() {
		return bianma;
	}
	public void setBianma(String bianma) {
		this.bianma = bianma;
	}
	@Override
	public Base parse(JSONObject tmp) throws JSONException {
		DaiJinQuan heTong = new DaiJinQuan();
		heTong.setId(RequestWebClient.getJsonString(tmp, "id"));
		heTong.setPic(RequestWebClient.getJsonString(tmp, "pic"));
		heTong.setTitle(RequestWebClient.getJsonString(tmp, "description"));
		heTong.setTime(RequestWebClient.getJsonString(tmp, "time"));
		heTong.setEndTime(RequestWebClient.getJsonString(tmp, "endTime"));
		heTong.setStartTime(RequestWebClient.getJsonString(tmp, "startTime"));
		heTong.setBianma(RequestWebClient.getJsonString(tmp, "bianma"));
		heTong.setMoney(RequestWebClient.getJsonString(tmp, "moneyFen"));

		return heTong;
	}
}