package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.web.RequestWebClient;

public class User extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3842540083509586600L;

	public final static int catalog = 1;
	public final static String type_dailishang = "3";
	public final static String type_huiyuan = "1";

	private String name;
	private String realname;
	private String password;
	/**
	 * 会员类型
	 */
	private String type;
	/**
	 * 头像图片地址
	 */
	private String pic;
	/**
	 * 合同数
	 */
	private int hetongshu;
	/**
	 * 到账金额
	 */
	private String money;
	/**
	 * 预约数量
	 */
	private int yuyuenum;
	/**
	 * 公司名字
	 */
	private String companyName;
	/**
	 * 公司地址
	 */
	private String companyAddr;
	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 短信验证码
	 */
	private String randCode;
	/**
	 * 随机码
	 */
	private String rand;
	/**
	 * 等级
	 */
	private String dengji;
	
	
	public String getDengji() {
		return dengji;
	}

	public void setDengji(String dengji) {
		this.dengji = dengji;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRandCode() {
		return randCode;
	}

	public void setRandCode(String randCode) {
		this.randCode = randCode;
	}

	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getHetongshu() {
		return hetongshu;
	}

	public void setHetongshu(int hetongshu) {
		this.hetongshu = hetongshu;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getYuyuenum() {
		return yuyuenum;
	}

	public void setYuyuenum(int yuyuenum) {
		this.yuyuenum = yuyuenum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		User user = new User();
		user.setHetongshu(jsonObject.optInt("hetongcount"));
		user.setMoney(jsonObject.optString("daozhang"));
		user.setYuyuenum(jsonObject.optInt("yuyue"));
		user.setPhone(jsonObject.optString("phone"));
		user.setRealname(jsonObject.optString("realname"));
		user.setCompanyAddr(jsonObject.optString("addr"));
		user.setPic(jsonObject.optString("picname"));
		user.setDengji(jsonObject.optString("dengji"));
		
		return user;
	}

	@Override
	public Base parseBean(JSONObject jsonObject)throws JSONException {
		
		JSONObject object = jsonObject.optJSONObject("user");
		if (object == null)
			return null;
		
		return parse(object);
	}

	
}
