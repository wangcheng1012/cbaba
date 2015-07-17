package com.wlj.chuangbaba.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.web.RequestWebClient;


public class Order extends Base{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2734282862020654511L;

	/**
	 * 订单创建成功（未支付）
	 */
	public static final Integer STATE_create = 2;
	/**
	 * 支付中 上传合同和部分支付
	 */
	public static final Integer STATE_paying = 3;
	/**
	 * 完成
	 */
	public static final Integer STATE_pay_ok = 1;
//	/**
//	 * 支付失败 
//	 */
//	public static final Integer order_STATE_false = 4;
	
	private int state;
	private String orderId;
	private String productId;
	private String title;
	private String saleUser;
	/**
	 * 产品型号
	 */
	private String chanpinxinghao;
	/**
	 * 参考报价
	 */
	private String cankaoPrice;
	/**
	 * 提交时间
	 */
	private String time;
	
	private String pic;
	/**
	 * 订单价格
	 */
	private String orderPrice;
	/**
	 * 已支付
	 */
	private String priceYizhifu;
	/**
	 * 待支付
	 */
	private String daizhifu;
	
	
	public String getPriceYizhifu() {
		return priceYizhifu;
	}
	public void setPriceYizhifu(String priceYizhifu) {
		this.priceYizhifu = priceYizhifu;
	}
	public String getDaizhifu() {
		return daizhifu;
	}
	public void setDaizhifu(String daizhifu) {
		this.daizhifu = daizhifu;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSaleUser() {
		return saleUser;
	}
	public void setSaleUser(String saleUser) {
		this.saleUser = saleUser;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getState() {
	
		return state;
	}
	public String getStateStr() {
		String strstate="";
		if(state == STATE_paying){
			strstate = "支付中";
		}else if(state == STATE_pay_ok){
			
			strstate = "支付完成";
		}else if(state == STATE_create){
			strstate = "创建成功";
		}else{
			strstate = "其他";
		}
		
		return strstate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getChanpinxinghao() {
		return chanpinxinghao;
	}
	public void setChanpinxinghao(String chanpinxinghao) {
		this.chanpinxinghao = chanpinxinghao;
	}
	public String getCankaoPrice() {
		return cankaoPrice;
	}
	public void setCankaoPrice(String cankaoPrice) {
		this.cankaoPrice = cankaoPrice;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		
		Order order = new Order();
		order.setId(RequestWebClient.getJsonString(jsonObject, "id"));
		order.setOrderId(RequestWebClient.getJsonString(jsonObject, "orderId"));
		order.setTitle(RequestWebClient.getJsonString(jsonObject, "productName"));
		order.setTime(RequestWebClient.getJsonString(jsonObject, "time"));
		order.setOrderPrice(RequestWebClient.getJsonString(jsonObject, "hetongPriceFen"));
		order.setPriceYizhifu(RequestWebClient.getJsonString(jsonObject, "priceYizhifu"));
		order.setDaizhifu(RequestWebClient.getJsonString(jsonObject, "daizhifu"));
		order.setPic(RequestWebClient.getJsonString(jsonObject, "pic"));
		order.setCankaoPrice(RequestWebClient.getJsonString(jsonObject, "cenkaoPriceFen"));
		order.setChanpinxinghao(RequestWebClient.getJsonString(jsonObject, "productXinghao"));
		order.setState(RequestWebClient.getJsonInt(jsonObject, "state"));
		order.setProductId(RequestWebClient.getJsonString(jsonObject, "product"));
		order.setSaleUser(RequestWebClient.getJsonString(jsonObject, "saleUser"));

		return order;
		
	}
}
