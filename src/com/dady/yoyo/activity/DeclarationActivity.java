package com.dady.yoyo.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dady.yoyo.fragment.Fragment_Declaration;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.Personal_Center;
import com.wlj.chuangbaba.bean.Hot;
import com.wlj.ui.BaseFragmentActivity;
import com.wlj.util.AppException;
import com.wlj.util.DpAndPx;
import com.wlj.util.ExecutorServices;
import com.wlj.util.UIHelper;

/**
 * 宣言
 * 
 * @author Administrator
 */
public class DeclarationActivity extends BaseFragmentActivity implements OnClickListener {
	private ChuangBaBaContext mContext;
	private View view;// infalte的布局
	private LinearLayout myContainer;// 新建容器
	private Fragment Center_mContent;
	private Fragment[] fragment_chuangbaba;
	private	Animation animation = null;
	private ImageView cursor;
	private Intent arguments;
	private int[] arrPosition;
	
	private LinearLayout tablinlay ;
	
	private Handler  handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				Exception e = (Exception)msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				break;
			case 1:
				BaseList baseList =(BaseList)msg.obj;
				
				iniTab(baseList.getBaselist());
				break;
			}
			
		}
		
	};
	
	private void iniTab(final List<Base> list) {
		if(list == null)return;
		final TextView title = (TextView)findViewById(R.id.title);
		for (Base base : list) {
			Hot hot = (Hot)base;
			TextView tabtext = (TextView)getLayoutInflater().inflate(R.layout.tabtext, null);
			LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1.0f);
			tabtext.setLayoutParams(params);
			int dpToPx10 = DpAndPx.dpToPx(mContext, 10);
			tabtext.setPadding(0, dpToPx10, 0, dpToPx10);
			tabtext.setText(hot.getTitle());
			tabtext.setTag(hot);
			tablinlay.addView(tabtext);
			tabtext.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Hot hot = (Hot)v.getTag();
					int curposition = list.indexOf(hot);
					moveCursor(curposition, lastPosition);
					lastPosition = curposition;
					Fragment fragment = fragment_chuangbaba[curposition];
					if (null == fragment) {
						fragment = new Fragment_Declaration();
						Bundle args = new Bundle();
						args.putSerializable("item", hot);
						args.putString("typeid",hot.getId() );
						fragment.setArguments(args);
						fragment_chuangbaba[curposition] = fragment;
						//title
					}
					title.setText(hot.getTitle());
					changeFragment(fragment, curposition);
					
				}
			});
			
		}
		
		initWidth();
		
		//初始化
		if(	fragment_chuangbaba.length > 0){
			moveCursor(0, lastPosition);
			Fragment fragment = fragment_chuangbaba[0];
			if (null == fragment) {
				fragment = new Fragment_Declaration();
				Bundle args = new Bundle();
				args.putSerializable("item", list.get(0));
				args.putString("typeid",list.get(0).getId() );
				fragment.setArguments(args);
				fragment_chuangbaba[0] = fragment;
			}
			title.setText(((Hot)list.get(0)).getTitle());
			changeFragment(fragment, 0);
		}
		
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = (ChuangBaBaContext) getApplicationContext();
		arguments = getIntent();
		view = getLayoutInflater().inflate(R.layout.fragment_declaration, null);
		view.setMinimumHeight(((WindowManager)
				getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight());
		view.setMinimumWidth(((WindowManager)
				getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth());
		setContentView(view);
		initView(view);
		initTitle(view);
		CallWeb();
	}

	private void CallWeb() {
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				Message msg =  new Message();
				try {
					BaseList  list  = mContext.getPubListByFeileiId(new Hot(),arguments.getStringExtra("fenleiid"), 1, true);
					msg.what = 1;
					msg.obj = list;
					
				} catch (Exception e) {
					AppException.http(e);// 记录日志
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * @param position
	 *            新位置
	 * @param oldposition
	 *            上一个位置
	 */
	public void moveCursor(int position, int oldposition) {
		animation = new TranslateAnimation(arrPosition[oldposition],arrPosition[position], 0, 0);
		if (null != animation) {
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
	}

	private void initView(View v) {
		cursor = (ImageView) v.findViewById(R.id.cursor);
		tablinlay = (LinearLayout) v.findViewById(R.id.tablinlay);
	}
	private void initTitle(View view){
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("窗爸爸的宣言");

		TextView right = (TextView) view.findViewById(R.id.right);
		right.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable = getResources().getDrawable(
				R.drawable.rentou_quan_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		right.setCompoundDrawables(drawable, null, null, null);

		TextView left = (TextView) view.findViewById(R.id.left);
		Drawable drawableback = getResources().getDrawable(
				R.drawable.back_white);
		drawableback.setBounds(0, 0, drawableback.getMinimumWidth(),
				drawableback.getMinimumHeight());
		left.setCompoundDrawables(drawableback, null, null, null);

		right.setOnClickListener(this);
		left.setOnClickListener(this);
		
	}

	/**
	 * 设置下方横条长宽
	 */
	private void initWidth() {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int screenW = outMetrics.widthPixels;
		int count = tablinlay.getChildCount();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW / count, DpAndPx.dpToPx(mContext, 3));
		cursor.setLayoutParams(params);
		arrPosition = new int[count];
		fragment_chuangbaba = new Fragment[count];
		int cursorLenth = screenW / count;
		for (int i = 0; i < count; i++) {
			arrPosition[i]= cursorLenth*i;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.left:
			finish();
			break;
		case R.id.right:
			Intent right = new Intent(mContext,Personal_Center.class);
			right.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(right);
			break;
			
		default:
			break;
		}
	}

	/**
	 * 上一次的位置
	 */
	int lastPosition = 0;
	int oldposition = -1;
	Fragment findFragment = null;

	private void changeFragment(Fragment f, int position) {
		if (oldposition != position) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			findFragment = fm.findFragmentByTag("viewid"+ position);
			if (Center_mContent != null) {
				ft.detach(Center_mContent);
			}
			oldposition = position;
			if (findFragment != null) {
				ft.attach(findFragment);
				Center_mContent = findFragment;
			} else {
				Center_mContent = f;
				ft.add(R.id.relative_center, Center_mContent, "viewid"+position);
			}
			ft.commitAllowingStateLoss();
		}
	}
}
