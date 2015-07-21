package com.wlj.chuangbaba.bean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.web.RequestWebClient;
import com.wlj.util.AppContext;
import com.wlj.util.AppException;
import com.wlj.util.ExecutorServices;

/**
 * 问题
 * 
 * @author wlj
 * 
 */
public class Project extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1525386978044495919L;
	
	private String id;

	private String name;
	/**
	 * 型号
	 */
	private String xinghao;
	/**
	 * 是否为门窗复选
	 */
	private boolean ismenchuang;
	
	/**
	 * 厂家
	 */
	private String changjia;
	/**
	 * 品牌
	 */
	private String pingpingpai;
	/**
	 * 参考报价
	 */
	private String cankaoPrice;
	/**
	 * item预览小图
	 */
	private String itemPic;
	/**
	 * 滚动图片
	 */
	private List<String> bannerPic;
	/**
	 * 收藏
	 */
	private String shoucang;
	/**
	 * 销量
	 */
	private String xiaoliang;
	/**
	 * 浏览
	 */
	private String liulan;
	// 规格
	/**
	 * 壁厚
	 */
	private List<Float> bihou;
	/**
	 * 重量
	 */
	private List<Float> weight;
	/**
	 * 玻璃材质
	 */
	private List<String> bolicaizhi;
	/**
	 * 
	 */
	private String color;

	private String intro;

	private String content;

	private String fuwu;

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFuwu() {
		return fuwu;
	}

	public void setFuwu(String fuwu) {
		this.fuwu = fuwu;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXinghao() {
		return xinghao;
	}

	public void setXinghao(String xinghao) {
		this.xinghao = xinghao;
	}

	public String getChangjia() {
		return changjia;
	}

	public void setChangjia(String changjia) {
		this.changjia = changjia;
	}

	public String getCankaoPrice() {
		return cankaoPrice;
	}

	public void setCankaoPrice(String cankaoPrice) {
		this.cankaoPrice = cankaoPrice;
	}

	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}

	public List<String> getBannerPic() {
		return bannerPic;
	}

	public void setBannerPic(List<String> bannerPic) {
		this.bannerPic = bannerPic;
	}

	public String getShoucang() {
		return shoucang;
	}

	public void setShoucang(String shoucang) {
		this.shoucang = shoucang;
	}

	public String getXiaoliang() {
		return xiaoliang;
	}

	public void setXiaoliang(String xiaoliang) {
		this.xiaoliang = xiaoliang;
	}

	public String getLiulan() {
		return liulan;
	}

	public void setLiulan(String liulan) {
		this.liulan = liulan;
	}

	public List<Float> getBihou() {
		return bihou;
	}

	public void setBihou(List<Float> bihou) {
		this.bihou = bihou;
	}

	public List<Float> getWeight() {
		return weight;
	}

	public void setWeight(List<Float> weight) {
		this.weight = weight;
	}

	public List<String> getBolicaizhi() {
		return bolicaizhi;
	}

	public void setBolicaizhi(List<String> bolicaizhi) {
		this.bolicaizhi = bolicaizhi;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getPingpingpai() {
		return pingpingpai;
	}

	public void setPingpingpai(String pingpingpai) {
		this.pingpingpai = pingpingpai;
	}
	
	public boolean isIsmenchuang() {
		return ismenchuang;
	}

	public void setIsmenchuang(boolean ismenchuang) {
		this.ismenchuang = ismenchuang;
	}
	
	public void getThisfromid(final AppContext mContext, final Handler handle){
		
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Project p;
				Message msg = new Message();
				try {
					p = ((ChuangBaBaContext)mContext).getProjectfromid(id);
					msg.what = 1;
					msg.obj = p;
				} catch (Exception e) {
					msg.what = -1; 
					msg.obj = e;
				}
				handle.sendMessage(msg);
			}
		});
		
	}
	
	@Override
	public Project parse(JSONObject tmp) throws JSONException {
		Project project = new Project();
		project.setId(RequestWebClient.getJsonString(tmp, "id"));
		project.setName(RequestWebClient.getJsonString(tmp, "name"));
		project.setItemPic(RequestWebClient.getJsonString(tmp, "pic"));
		project.setXinghao(RequestWebClient.getJsonString(tmp, "xinghao"));
		project.setChangjia(RequestWebClient.getJsonString(tmp, "changjia"));
		project.setPingpingpai(tmp.optString("pingpingpai"));
		project.setIsmenchuang(tmp.optBoolean("ismenchuang", false));
		// 规格
		project.setCankaoPrice(RequestWebClient.getJsonString(tmp, "priceFen"));
		
		try {
			JSONArray picsarray = tmp.getJSONArray("pics");
	
			List<String> pics = new ArrayList<String>();
			for (int j = 0; j < picsarray.length(); j++) {
				String tmpstr = picsarray.getString(j);
				pics.add(tmpstr);
			}
			project.setBannerPic(pics);
		} catch (JSONException e) {
			
		}
		project.setShoucang(RequestWebClient.getJsonString(tmp, "shoucang"));
		project.setXiaoliang(RequestWebClient.getJsonString(tmp, "xiaoliang"));
		project.setLiulan(RequestWebClient.getJsonString(tmp, "viewcount"));

		// 基本信息
		project.setIntro(RequestWebClient.getJsonString(tmp, "intro"));
		// 商品详情
		project.setContent(RequestWebClient.getJsonString(tmp, "content"));
		// 客户服务
		project.setFuwu(RequestWebClient.getJsonString(tmp, "fuwu"));

		return project;
	}

}