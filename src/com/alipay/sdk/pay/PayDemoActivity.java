package com.alipay.sdk.pay;

import static com.wlj.chuangbaba.web.MsgContext.key_page;
import static com.wlj.chuangbaba.web.MsgContext.key_pageSize;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.activity.personal.fragment.Order_AllOrder;
import com.wlj.chuangbaba.bean.DaiJinQuan;
import com.wlj.chuangbaba.bean.Order;
import com.wlj.chuangbaba.bean.User;
import com.wlj.util.ExecutorServices;
import com.wlj.util.Log;
import com.wlj.util.MathUtil;
import com.wlj.util.UIHelper;
import com.wlj.web.URLs;

public class PayDemoActivity extends MyBaseFragmentActivity {

	// 商户PID
	public static final String PARTNER = "2088711930055953";
	// 商户收款账号
	public static final String SELLER = "zgmybyly@126.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK2JqQxBmhaZnAsEXX1QRIIxwtFDBWYkpIN0AmoIMzYCLXmVtYnjXiEFabcRuNdF14EQgUIDNhr1mijP5ii6Ab3AlXTi7Rjp+8cPqQby4Lb/Zk7nKbvwZZxVsn7J2YWTng7ahoRdM3hgDZ8gGycsIP9OcsqgLyqWXk/nnd1BTShFAgMBAAECgYBkIQfPMJLt81fb63rd8VB2JqwX/inBU+cwieFSuMSDpyICA2Wp1+zZ9LuYi7sxSvUbXSCXrC6tw4RdhAdy/FA/7m+xAmuWG/Qs7nuFXuD46OndD7+fW9cbO984YeuO+eZn74YVP/HedDrdI5qDQNQiZ+oOCuwBrdm9f6CPqfp3iQJBAOG0KxrlRlb5xPXmWvoZJaVZkwS+aLq6ufF95bQbXMc5+nC+j3DOvbUHfvejOcTJE2EAkSItpWkTi1MIoX7aMbsCQQDE1OqbtdIr0WFWE/CJEpShEzu3w4RPJYWi1pac6aFOxXlJT+f+GJ5NGhMB8xusl0e93RiEC1+M41ql5m/3S23/AkEA1YzanAACpZh9OwxRWAEjFWfDKuHdxY+XD17HT7nz9qxswPAMV/fEltwYzcWqiv6VT1px95yovNq5ID8yTgu0sQJAa4L1BspAm2iJpy/LAcQhZP4Rb2WfHnAbV3+o0tqWHmWkwxOfX15pESo92aHFqvcS27Upj+56aYVB3T5KSCRuVwJBAKhpktFyaN7RlKqEwmOFauYob0AZnOe4dop9Udo1ndUdaFJOf7CstLOHWRQ4v7jZd0WTtWc7tW5gGejz5Dpf5kA=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private static final String notify_url = "http://121.40.177.251:8810/notify_url.jsp";
	private Order order;
	private double price;
	private double youhuiprice;
	private DecimalFormat df;
	private CommonAdapter<DaiJinQuan> adapter;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayDemoActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					setResult(Order_AllOrder.payCode);
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayDemoActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayDemoActivity.this,
								payResult.getMemo(), Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			}
		};
	};
	private TextView product_totalprice;
	private TextView product_youhuiprice;
	private TextView product_yinfuprice;
	private TextView product_shifuprice;

	private List<DaiJinQuan> mdate;
	private String shifuprice;
	private Spinner hongbaospinner;
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		if(MathUtil.parseDouble(shifuprice) <= 0 ){
			UIHelper.ToastMessage(mContext, "支付金额不能小于等于0");
			return;
		}
		
		// 订单
		String orderInfo = getOrderInfo(order.getTitle(),order.getChanpinxinghao(), shifuprice);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);                                                                 
		try { 
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayDemoActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				//
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
		
		LockDaiJinQuan();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		// Runnable checkRunnable = new Runnable() {
		//
		// @Override
		// public void run() {
		// // 构造PayTask 对象
		// PayTask payTask = new PayTask(PayDemoActivity.this);
		// // 调用查询接口，获取查询结果
		// boolean isExist = payTask.checkAccountIfExist();
		//
		// Message msg = new Message();
		// msg.what = SDK_CHECK_FLAG;
		// msg.obj = isExist;
		// mHandler.sendMessage(msg);
		// }
		// };
		// Thread checkThread = new Thread(checkRunnable);
		// checkThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + order.getId() + "_"
				+ (int) (MathUtil.parseDouble(price) * 100) + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"1d\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	protected void beforeTitle() {

	}

	@Override
	protected int setlayout() {
		return R.layout.pay_main;
	}

	@Override
	protected void initView() {
		
		df = new DecimalFormat("0.00");
		youhuiprice = 0.00d;
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
		price = intent.getDoubleExtra("price", 0.00d);

		((TextView) findViewById(R.id.product_subject)).setText(order.getTitle());
		((TextView) findViewById(R.id.product_price)).setText(price + "元");

		hongbaospinner = (Spinner) findViewById(R.id.hongbaospinner);
		mdate = new ArrayList<DaiJinQuan>();
		
		DaiJinQuan daiJinQuan = new DaiJinQuan();
		
		daiJinQuan.setTitle("不使用优惠券");
		daiJinQuan.setMoney("0.00");
		mdate.add(daiJinQuan);

		adapter = new CommonAdapter<DaiJinQuan>(mContext, mdate,R.layout.item_textview_black) {

			@Override
			public View getListItemview(ViewHolder viewHolder, View view,
					DaiJinQuan item, int position, ViewGroup parent) {

				viewHolder.setText(R.id.item_text_black, item.getshow() );
				view.setTag(R.id.tag_first, item);

				return null;
			}
		};
		hongbaospinner.setAdapter(adapter);

		product_totalprice = (TextView) findViewById(R.id.product_totalprice);
		product_youhuiprice = (TextView) findViewById(R.id.product_youhuiprice);
		product_yinfuprice = (TextView) findViewById(R.id.product_yinfuprice);
		product_shifuprice = (TextView) findViewById(R.id.product_shifuprice);

		product_totalprice.setText("￥" + price);

		hongbaospinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				DaiJinQuan daijinquan = mdate.get(position);
				
				String string = daijinquan.getshow();
				
				int start = string.indexOf("(");
				int end = string.indexOf("元)");
				if (start != -1 && end != -1) {
					String substring = string.substring(start+1,end);
					youhuiprice = MathUtil.parseDouble(substring);
				} else {
					youhuiprice = 0.00;
				}
				int pricefen = (int) (price*100);
				int youhuipricefen =  (int) (youhuiprice*100);
				
				shifuprice = df.format(((double)(pricefen - youhuipricefen))/100);
				product_youhuiprice.setText("-￥" + df.format(youhuiprice));
				product_yinfuprice.setText("￥" + shifuprice);
				product_shifuprice.setText("实付总额：￥" +shifuprice+ "元");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		callweb();
	}

	@Override
	protected void Switch(Message msg) {
		if(msg.what == 2){
			Object obj = msg.obj;
			
		}
	}

	@Override
	protected void setViewDate(Object obj) {
		BaseList baseList = (BaseList) obj;
		List<Base> list = baseList.getBaselist();
		
		mdate.clear();
		DaiJinQuan daiJinQuan = new DaiJinQuan();
		daiJinQuan.setId("");
		daiJinQuan.setTitle("不使用优惠券");
		daiJinQuan.setMoney("0.00");
		mdate.add(daiJinQuan);
		
		for (Base base : list) {
			DaiJinQuan youhui = (DaiJinQuan) base;
			mdate.add(youhui);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void rightOnClick() {
		// 个人
		Intent intent = new Intent(mContext, Personal_Center.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		
		 Map<String, Object>  map= new HashMap<String, Object>();
		map.put("state", DaiJinQuan.daijinquan_state_nomal+"");
		
		map.put(key_page, "1");
		map.put(key_pageSize, "1000");
		map.put("user_Type",User.type_huiyuan);
		map.put("url", URLs.list_daijinquan);
		
		BaseList list = (BaseList) ((ChuangBaBaContext) getApplicationContext()).Request(this,map, new DaiJinQuan());
		return list;
	}

	/**
	 * 锁定代金券
	 */
	private void LockDaiJinQuan(){
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				//代金卷系统id
				 Map<String, Object>  map= new HashMap<String, Object>();
				 map.put("daijinjuanId",mdate.get(hongbaospinner.getSelectedItemPosition()).getId());
				 map.put("hetongid",order.getId() );
				 
				 map.put("url", URLs.orderUseDaijinQuan);
				 map.put("user_Type",User.type_huiyuan);
				 Message message = Message.obtain();
				try {
					message.what = 2;
					message.obj = mContext.Request(PayDemoActivity.this,map, null);
				} catch (Exception e) {
					message.what = -1;
					message.obj = e;
				}
				handle.sendMessage(message);
			}
		});
		
	}
	
}
