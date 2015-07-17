package com.wlj.chuangbaba.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.web.RequestWebClient;

/**
 * 问题
 * 
 * @author wlj
 * 
 */
public class ProjectShouCang extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4243290611701420303L;

	/**
	 * 型号
	 */
	private String memo;

	private Project project;


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
//{"shoucangId":"559cfb50d6c4595fc599391a","memo":"","productId":"55463490d105fc1e93208b16",
//	"name":"120重型推拉门","xinghao":"推拉门窗系列MYZXM-01","priceFen":"98500","pic":"attachFiles/temp/5576b589d6c4591744669942","pingpingpai":"窗爸爸","changjia":"","jijiadanwei":"平方米"}
	@Override
	public ProjectShouCang parse(JSONObject tmp) throws JSONException {
		ProjectShouCang projectsc = new ProjectShouCang();
		projectsc.setId(tmp.optString("shoucangId"));
		projectsc.setMemo(tmp.optString("memo"));
		projectsc.setProject(new Project().parse(tmp));
		
		return projectsc;
	}

}