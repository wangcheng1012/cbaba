package com.wlj.chuangbaba.activity.dailishang;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlj.chuangbaba.PhotoGraphActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.fragment.Order_AllOrder;
import com.wlj.chuangbaba.bean.HeTong;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.util.RequestException;
import com.wlj.util.UIHelper;
import com.wlj.util.img.BitmapUtil;

/**
 * 代理商——管理——合同上传
 * 
 * @author wlj
 * 
 */
public class GuanLi_2_HeTongUpLoad extends PhotoGraphActivity implements
		OnClickListener {

	private EditText hetongNum;
	private EditText hetongMoney;
	private EditText beizhu;
	private ImageView Photograph;
	private ImageView photo;
	private Button upload;

	private Order order;
	
	@Override
	protected void initView() {
		order =  (Order) intent.getSerializableExtra("order");
		
		((TextView) findViewById(R.id.orderNO)).setText(order.getOrderId());
		
		hetongNum = (EditText) findViewById(R.id.hetongNum);
		hetongMoney = (EditText) findViewById(R.id.hetongMoney);
		beizhu = (EditText) findViewById(R.id.beizhu);
		Photograph = (ImageView) findViewById(R.id.Photograph);
		photo = (ImageView) findViewById(R.id.photo);

		upload = (Button) findViewById(R.id.upload);
		upload.setOnClickListener(this);
		Photograph.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.upload:
			callweb();
			break;
		case R.id.Photograph:
			mCropParams.isCrop = false;
			cameraAndGallery();
			break;
		}

	}

	@Override
	protected void setImageView(Bitmap touxiang2) {
		photo.setImageBitmap(touxiang2);		
	}

	@Override
	protected int setlayout() {
		return R.layout.dailishang_2_hetongupload;
	}

	@Override
	protected void beforeTitle() {
		title.setText("合同上传");
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
		Boolean b = (Boolean)obj;
		if(b){
			UIHelper.ToastMessage(mContext, "提交成功");
			setResult(Order_AllOrder.updatehetong);
			finish();
		}else{
			UIHelper.ToastMessage(mContext, "提交失败");
		}
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
		HeTong hetong = new HeTong();
		String bianhao = hetongNum.getText() + "";
		String money = hetongMoney.getText() + "";
		if("".equals(bianhao)){
			throw new RequestException("合同编号不能为空");
		}
		if("".equals(money)){
			throw new RequestException("合同金额不能为空");
		}
		if(touxiang == null){
			throw new RequestException("合同照不能为空");
		}
		hetong.setBeizhu(beizhu.getText() + "");
		hetong.setBianhao(bianhao);
		hetong.setMoney(money);
		
		hetong.setOrderNo(order.getId());
		
		Drawable bitmap = photo.getDrawable();
		
		if(bitmap != null){
			hetong.setPic(BitmapUtil.getInstall().bitmaptoString(bitmap));
		}else{
			hetong.setPic("");
		}
		return mContext.uploadHeTong(hetong);
	}

}
