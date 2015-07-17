package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.ChuangBaBaContext;

/**
 * 问题
 * 
 * @author wlj
 * 
 */
public class Wen extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1581460588631878202L;
	public final static int state_shenhezhong = 1;
	public final static int state_tongguo = 2;
	public final static int state_weitongguo = 3;

	private String phone;
	private String wenti;
	private String pic1;
	private String pic2;
	public int state;
	private User user;
	private int answercount;
	
	/**
	 * 回复
	 */
	private List<Base> answerList;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Base> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Base> answerList) {
		this.answerList = answerList;
	}

	/**
	 * 发布时间
	 */
	private String time;

	public String getState() {
		String stateStr = "";
		if(state_shenhezhong == state){
			stateStr = "审核中";
		}else if(state_tongguo == state){
			stateStr = "审核通过";
		}else if(state_weitongguo == state){
			stateStr = "未通过";
		}else {
			stateStr = "未知";
		}
		return stateStr;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWenti() {
		return wenti;
	}

	public void setWenti(String wenti) {
		this.wenti = wenti;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	
	public int getAnswercount() {
		return answercount;
	}

	public void setAnswercount(int answercount) {
		this.answercount = answercount;
	}
//{"id":"559cee9cd6c4595fc5993919","wen":"你的在这呢","phone":"","time":"1436348060000","state":"1","da":{},
//	"pics":["attachFiles/temp/559cee9cd6c4595fc5993918"]}
	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		
		Wen wen = new Wen();
		wen.setId(jsonObject.optString("id"));
		String phonestr = jsonObject.optString(phone);
		if("".equals(phonestr)){
			phonestr =  ChuangBaBaContext.preferences.getString("name", "");
		}
		wen.setPhone(phonestr);
		wen.setWenti(jsonObject.getString("wen"));
		wen.setState(jsonObject.getInt("state"));
		
		wen.setTime(jsonObject.optString("time"));
		JSONArray jsonArray = jsonObject.optJSONArray("pics");
		if(jsonArray != null){
			wen.setPic1(jsonArray.optString(0));
			wen.setPic2(jsonArray.optString(1));
		}
		JSONArray answerArray = jsonObject.optJSONArray("da");
		if(answerArray != null && answerArray.length() > 0 ){
			wen.setAnswercount(answerArray.length());
			List<Base> list = new ArrayList<Base>();
			for (int i = 0; i < answerArray.length(); i++) {
				JSONObject optJSONObject = answerArray.optJSONObject(i);
				if(optJSONObject != null ){
					list.add(new Answer().parse(optJSONObject));
				}
			}
			wen.setAnswerList(list);
		}
		
		return wen;
	}

}