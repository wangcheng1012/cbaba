package com.wlj.chuangbaba.activity.dailishang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.Main;
import com.wlj.chuangbaba.activity.personal.HuiYuanLogin;
import com.wlj.chuangbaba.activity.personal.OrderActivity;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppConfig;
import com.wlj.util.AppManager;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;

public class DaiLiShang_GuanLi extends BaseFragmentActivity implements OnClickListener {

	private RelativeLayout dailishang_jibenxinxi, dailishang_hetongshangchuan,
			dailishang_chuangyifabu, dailishang_huifuwenti,
//			dailishang_huifupinglu,
			dailishang_yuyuexinxi,
			dailishang_hetongbianhao, dailishang_wotijiaodechuangyi
//			,dailishang_rongyudu, dailishang_wodezhangfu
			;
//	private TextView home, tuijian, set, about;
	private ImageView dailishangPic;
//	private User user;
	private TextView hetongshu;
	private TextView money;
	private TextView yuyuenum;
	private ChuangBaBaContext mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailishang_guanli);
		mContext = (ChuangBaBaContext) getApplicationContext();
		if(!User.type_dailishang.equals(mContext.getProperty(AppConfig.CONF_TYPT))){
			//登录
			Intent intentwenda = new Intent(getApplicationContext(),DaiLiShang.class);
			intentwenda.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentwenda);
		}
		init_title();
		init_view();
	}

	private void init_view() {
		dailishang_jibenxinxi = (RelativeLayout) findViewById(R.id.dailishang_jibenxinxi);
		dailishang_hetongshangchuan = (RelativeLayout) findViewById(R.id.dailishang_hetongshangchuan);
		dailishang_chuangyifabu = (RelativeLayout) findViewById(R.id.dailishang_chuangyifabu);
		dailishang_huifuwenti = (RelativeLayout) findViewById(R.id.dailishang_huifuwenti);
//		dailishang_huifupinglu = (RelativeLayout) findViewById(R.id.dailishang_huifupinglu);
		dailishang_yuyuexinxi = (RelativeLayout) findViewById(R.id.dailishang_yuyuexinxi);
		dailishang_hetongbianhao = (RelativeLayout) findViewById(R.id.dailishang_hetongbianhao);
		dailishang_wotijiaodechuangyi = (RelativeLayout) findViewById(R.id.dailishang_wotijiaodechuangyi);
//		dailishang_rongyudu = (RelativeLayout) findViewById(R.id.dailishang_rongyudu);
//		dailishang_wodezhangfu = (RelativeLayout) findViewById(R.id.dailishang_wodezhangfu);
		//
		((ImageView)findViewById(R.id.dailishang_changpic)).setOnClickListener(this);
		dailishangPic = (ImageView)findViewById(R.id.dailishangPic);
		hetongshu = (TextView)findViewById(R.id.hetongshu);
		money = (TextView)findViewById(R.id.money);
		yuyuenum = (TextView)findViewById(R.id.yuyuenum);

		dailishang_jibenxinxi.setOnClickListener(this);
		dailishang_hetongshangchuan.setOnClickListener(this);
		dailishang_chuangyifabu.setOnClickListener(this);
		dailishang_huifuwenti.setOnClickListener(this);
//		dailishang_huifupinglu.setOnClickListener(this);
		dailishang_yuyuexinxi.setOnClickListener(this);
		dailishang_hetongbianhao.setOnClickListener(this);
		dailishang_wotijiaodechuangyi.setOnClickListener(this);
//		dailishang_rongyudu.setOnClickListener(this);
//		dailishang_wodezhangfu.setOnClickListener(this);

		initWeb();
	}
	
	private void initWeb() {

		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Message message = Message.obtain();
				try {
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url",  URLs.getUserInfo);
					map.put("user_Type",  User.type_dailishang);
					
					BaseList baseList = mContext.Request(DaiLiShang_GuanLi.this,map,new User());
					
					message.what = 1;
					message.obj = baseList;
				} catch (Exception e) {
					message.what = -1;
					message.obj = e;
				}
				handle.sendMessage(message);
			}
		});
		
	}
	private User user;
	private void setViewDate(Object obj ){
		BaseList baselist = (BaseList)obj;
		 List<Base> list = baselist.getBaselist();
		if(list==null)return;
		user =(User) list.get(0);
		LoadImage loadImage = LoadImage.getinstall();
		loadImage.addTask(URLs.HOST+user.getPic(), dailishangPic);
		loadImage.doTask();
		
		hetongshu.setText(user.getHetongshu()+"份");
		money.setText(user.getMoney()+"元");
		yuyuenum.setText(user.getYuyuenum()+"份");
	}
	private void init_title() {

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("代理商中心");

		TextView right = (TextView) findViewById(R.id.right);
		right.setText("安全退出");
		right.setTextSize(15);
		right.setTextColor(getResources().getColor(R.color.white));
		right.setGravity(Gravity.CENTER_VERTICAL);
		
		TextView left = (TextView) findViewById(R.id.left);

		Drawable drawableback = getResources().getDrawable(
				R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);
		left.setText("返回");

		right.setOnClickListener(this);
		left.setOnClickListener(this);

	}
	public final static int  JiBenXinXi_requestCode= 41;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			finish();
			break;
		case R.id.right://退出登录
			mContext.loginOut();
			finish();
			break;
		case R.id.dailishang_changpic:
			Intent changpic = new Intent(getApplicationContext(),GuanLi_1_JiBenXinXi.class);
			changpic.putExtra("user", user);
			changpic.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(changpic, JiBenXinXi_requestCode);
			break;
		case R.id.dailishang_jibenxinxi:
			Intent jibenxinxi = new Intent(getApplicationContext(),GuanLi_1_JiBenXinXi.class);
			jibenxinxi.putExtra("user", user);
			jibenxinxi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(jibenxinxi, JiBenXinXi_requestCode);
			break;
		case R.id.dailishang_hetongshangchuan:
			Intent hetongshangchuan = new Intent(getApplicationContext(),OrderActivity.class);
			hetongshangchuan.putExtra("type", OrderActivity.dailishang);
			hetongshangchuan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(hetongshangchuan);
			break;
		case R.id.dailishang_chuangyifabu:
			Intent chuangyifabu = new Intent(getApplicationContext(),GuanLi_3_ChuangYiFaBu.class);
			chuangyifabu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(chuangyifabu);
			break;
		case R.id.dailishang_huifuwenti:
			Intent huifuwenti = new Intent(getApplicationContext(),GuanLi_4_HuiFuWenTi.class);
			huifuwenti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(huifuwenti);
			break;
//		case R.id.dailishang_huifupinglu:
//			Intent huifupinglu = new Intent(getApplicationContext(),
//					GuanLi_5_HuiFuPingLu.class);
//			huifupinglu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(huifupinglu);
//			break;
		case R.id.dailishang_yuyuexinxi:
			Intent yuyuexinxi = new Intent(getApplicationContext(),
					GuanLi_6_YuYueXinXi.class);
			yuyuexinxi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(yuyuexinxi);
			break;
		case R.id.dailishang_hetongbianhao:
//			Intent hetongbianhao = new Intent(getApplicationContext(),
//					GuanLi_7_HeTongBianHao.class);
//			hetongbianhao.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(hetongbianhao);
			break;
		case R.id.dailishang_wotijiaodechuangyi:
			Intent wotijiaodechuangyi = new Intent(getApplicationContext(),
					GuanLi_8_MyChuangYi.class);
			wotijiaodechuangyi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(wotijiaodechuangyi);
			break;
//		case R.id.dailishang_rongyudu:
//			Intent rongyudu = new Intent(getApplicationContext(),GuanLi_9_RongYuDu.class);
//			rongyudu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(rongyudu);
//			break;
//		case R.id.dailishang_wodezhangfu:
//			Intent wodezhangfu = new Intent(getApplicationContext(),GuanLi_91_MyZhangHu.class);
//			wodezhangfu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(wodezhangfu);
//			break;
		}
	}
	
	private Handler handle = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:
				setViewDate(msg.obj);
				break;
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				if("登录信息过期,请重新登录!".equals(e.getMessage())){
					mContext.loginOut();
					Intent intent = new Intent(DaiLiShang_GuanLi.this,DaiLiShang.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
				break;
			}
		};
		
	};
	
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg1 == JiBenXinXi_requestCode){
			initWeb();
		}
		
	};
}
