package com.wlj.chuangbaba.activity.wenda;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wlj.chuangbaba.PhotoGraphActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.bean.User;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.util.UIHelper;
import com.wlj.util.img.BitmapUtil;
import com.wlj.web.URLs;

public class WenDa_fawen extends PhotoGraphActivity implements OnClickListener {

	private EditText problemText;
	private EditText phone;
	private ImageView paizhao;
	private ImageView Photo1;
	
	@Override
	protected int setlayout() {
		return R.layout.wenda_fabu;
	}
	@Override
	protected void beforeTitle() {
		title.setText("有问必答");
	}
	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
		if(obj== null){
			UIHelper.ToastMessage(mContext, "提交成功");
			finish();
		}else{
			UIHelper.ToastMessage(mContext, "提交失败");
		}
	}
	@Override
	protected void initView() {
		phone = (EditText)findViewById(R.id.phone);
		problemText = (EditText)findViewById(R.id.problemText);
		paizhao = (ImageView)findViewById(R.id.paizhao);
		Photo1 = (ImageView)findViewById(R.id.Photo1);
	
		((Button)findViewById(R.id.tijiaobutton)).setOnClickListener(this);;
		paizhao.setOnClickListener(this);
	}
	
	@Override
	protected Object callWebMethod() throws Exception{
		
		Map<String, Object>  map= new HashMap<String, Object>();
		map.put("user_Type", User.type_huiyuan);
		map.put("url", URLs.tiwen);
		map.put("wen", problemText.getText()+"");
		map.put("phone", phone.getText()+"");
		Drawable drawable1 = Photo1.getDrawable();
		JSONArray jsonArray = new JSONArray();
		if(drawable1 != null){
			jsonArray.put(BitmapUtil.getInstall().bitmaptoString(drawable1) );
		}else{
			jsonArray.put("" );
		}
		map.put("pics", jsonArray);
		
		return mContext.Request(map, null);
	}
	@Override
	protected void rightOnClick() {
		Intent right = new Intent(getApplicationContext(), Personal_Center.class);
		right.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(right);
	}

	@Override
	protected void liftOnClick() {
//		Intent intent = new Intent(getApplicationContext(), Main.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
		finish();
	}
	@Override
	protected void setImageView(Bitmap touxiang2) {
			Photo1.setImageBitmap(touxiang2);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paizhao:
			camera();
			break;
		case R.id.tijiaobutton:
			callweb();
			break;
		}
	}
	
}
