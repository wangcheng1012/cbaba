package com.wlj.chuangbaba.activity.personal;

import static com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi.JiBenXinXi_requestCode;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wlj.chuangbaba.PhotoGraphActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.UIHelper;
import com.wlj.chuangbaba.web.URLs;

/**
 *个人中学——基本信息
 * 
 * @author wlj
 * 
 */
public class ChangeJiBenXinXi extends PhotoGraphActivity implements
		OnClickListener {

//	private ImageView Photograph;
//	private ImageView photo;
//	private EditText phone;
	private EditText addr;
	private EditText name;
	private Button submit;
	private TextView  phone,dengji;

	@Override
	protected int setlayout() {
		return R.layout.personal_jibenxinxi;
	}
	
	@Override
	protected void initView() {
		phone = (TextView)findViewById(R.id.phone);
		dengji = (TextView)findViewById(R.id.dengji);
		addr = (EditText) findViewById(R.id.addr);
		name = (EditText) findViewById(R.id.name);
		
		submit = (Button) findViewById(R.id.submit);
		
//		Photograph.setOnClickListener(this);
		submit.setOnClickListener(this);
		Intent intent = getIntent();
		User user = (User)intent.getSerializableExtra("base");
		if(user == null)return;
		phone.setText(user.getPhone());
		dengji.setText(user.getDengji());
		addr.setText(user.getCompanyAddr());
		name.setText(user.getRealname());
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
//		case R.id.Photograph:
//			
//			cameraAndGallery();
//			
//			break;
		case R.id.submit:
			callweb();
			break;
		}
	}

	@Override
	protected void setImageView(Bitmap touxiang2) {
//		photo.setImageBitmap(touxiang2);
	}

	@Override
	protected void beforeTitle() {
		title.setText("更改资料");
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
			UIHelper.ToastMessage(mContext, "提交成功");
			setResult(JiBenXinXi_requestCode);
			finish();
	}

	@Override
	protected void rightOnClick() {
		finish();
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		
//		Drawable bitmap = photo.getDrawable();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("url", URLs.updateUser);
		map.put("user_Type", User.type_huiyuan);
		
		String cn = name.getText() + "";
		if(!"".equals(cn)){
			map.put("realname", cn);
		}
		String ca = addr.getText() + "";
		if(!"".equals(ca)){
			map.put("addr", ca);
		}
//		String p = phone.getText() + "";
//		if(!"".equals(p)){
//			map.put("phone", p);
//		}
//		
//		if(bitmap != null && touxiang != null){
//			map.put("pic",BitmapUtil.getInstall().bitmaptoString(touxiang));
//		}else{
////			map.put("pic","");
//		}
		
		return  mContext.Request(this,map, null);
	}

	@Override
	public void onCropCancel() {
		Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCropFailed(String message) {
		Toast.makeText(this, "Crop Failed!", Toast.LENGTH_LONG).show();
	}
	
}
