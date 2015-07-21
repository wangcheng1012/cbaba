package com.wlj.chuangbaba.activity.dailishang;

import static com.wlj.chuangbaba.activity.dailishang.DaiLiShang_GuanLi.JiBenXinXi_requestCode;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wlj.chuangbaba.PhotoGraphActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.DpAndPx;
import com.wlj.util.UIHelper;
import com.wlj.util.img.BitmapUtil;
import com.wlj.web.URLs;

/**
 * 代理商——管理——基本信息
 * 
 * @author wlj
 * 
 */
public class GuanLi_1_JiBenXinXi extends PhotoGraphActivity implements
		OnClickListener {

	private ImageView Photograph;
	private ImageView photo;
	private EditText phone;
	private EditText companyAddr;
	private EditText companyName;
	private Button submit;

	@Override
	protected int setlayout() {
		return R.layout.dailishang_1_jibenxinxi;
	}
	
	@Override
	protected void initView() {
		Photograph = (ImageView) findViewById(R.id.Photograph);
		photo = (ImageView) findViewById(R.id.photo);
		
		phone = (EditText) findViewById(R.id.phone);
		companyAddr = (EditText) findViewById(R.id.companyAddr);
		companyName = (EditText) findViewById(R.id.companyName);
		
		submit = (Button) findViewById(R.id.submit);
		
		Photograph.setOnClickListener(this);
		submit.setOnClickListener(this);
		Intent intent = getIntent();
		User user = (User)intent.getSerializableExtra("user");
		if(user == null)return;
		((TextView)findViewById(R.id.phone_textview)).setText(user.getPhone());
		((TextView)findViewById(R.id.companyAddr_textview)).setText(user.getCompanyAddr());
		((TextView)findViewById(R.id.companyName_textview)).setText(user.getRealname());
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.Photograph:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			final AlertDialog dialog = builder.create();
			dialog.show();
			Window window = dialog.getWindow();
			
			window.setContentView(getLayoutInflater().inflate(R.layout.piccomechoose,null));
			window.findViewById(R.id.camera).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					camera();
					dialog.dismiss();
				}
			});
			
			window.findViewById(R.id.gallery).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					gallery();
					dialog.dismiss();
				}
			});
			
			break;
		case R.id.submit:
			callweb();
			break;
		}
	}

	@Override
	protected void setImageView(Bitmap touxiang2) {
		photo.setImageBitmap(touxiang2);
	}

	@Override
	protected void beforeTitle() {
		title.setText("代理商资料");
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
		
		Drawable bitmap = photo.getDrawable();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("url", URLs.updateUser);
		map.put("user_Type", User.type_dailishang);
		
		String cn = companyName.getText() + "";
		if(!"".equals(cn)){
			map.put("realname", cn);
		}
		String ca = companyAddr.getText() + "";
		if(!"".equals(ca)){
			map.put("addr", ca);
		}
		String p = phone.getText() + "";
		if(!"".equals(p)){
			map.put("phone", p);
		}
		
		if(bitmap != null && touxiang != null){
			map.put("pic",BitmapUtil.getInstall().bitmaptoString(touxiang));
		}else{
//			map.put("pic","");
		}
		
		return  mContext.Request(map, null);
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
