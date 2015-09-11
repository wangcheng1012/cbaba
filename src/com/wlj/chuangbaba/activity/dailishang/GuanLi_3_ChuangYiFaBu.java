package com.wlj.chuangbaba.activity.dailishang;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.UIHelper;
import com.wlj.chuangbaba.web.URLs;

/**
 * 代理商——管理——发布创意
 * 
 * @author wlj
 * 
 */
public class GuanLi_3_ChuangYiFaBu extends MyBaseActivity implements OnClickListener  {

	private EditText chuangyitext;
	private EditText chuangyiname;
	private Button publish;
	@Override
	protected void beforeTitle() {
		title.setText("发布创意");
	}
	@Override
	protected int setlayout() {
		return R.layout.dailishang_3_chuangyifabu;
	}
	@Override
	protected void initView() {
		chuangyitext = (EditText)findViewById(R.id.chuangyitext);
		chuangyiname = (EditText)findViewById(R.id.chuangyiname);
		
		publish = (Button)findViewById(R.id.publish);
		publish.setOnClickListener(this);
		
	}
	@Override
	protected void Switch(Message msg) {
		
	}
	@Override
	protected void setViewDate(Object obj) {
		UIHelper.ToastMessage(this, "发布成功");
		chuangyitext.setText("");
	}
	@Override
	protected void rightOnClick() {
		Intent intent = new Intent(mContext, DaiLiShang_GuanLi.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	@Override
	protected void liftOnClick() {
		finish();
	}
	@Override
	protected Object callWebMethod() throws Exception {
		
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("url", URLs.chuangyifabu);
		hashMap.put("content", chuangyi);
		hashMap.put("user_Type", User.type_dailishang);
		hashMap.put("name", name);
		
		return mContext.Request(this, hashMap, null);
	}
	

	String chuangyi="";
	String name ="";
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.publish:
			chuangyi = chuangyitext.getText()+"";
			name = chuangyiname.getText()+"";
			
			if("".equals(chuangyi.trim())){
				UIHelper.ToastMessage(this, "创意内容不能为空");
				break;
			}
			if("".equals(name.trim())){
				UIHelper.ToastMessage(this, "创意标题不能为空");
				break;
			}
			callweb();
			break;
		}

	}

}
