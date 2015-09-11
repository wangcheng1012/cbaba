package com.wlj.chuangbaba;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wlj.util.DpAndPx;

/**
 * <li>与MyBaseFragmentActivity比 多了个 移动cursor的方法</li>
 * <li>注意 xml 里面的 cursor的id为cursor，与tab选项卡LinearLayout布局id为tab_linlayout，这个应该要想办法优化</li>
 * @author wlj
 */
public abstract class MyBaseMoreFragmentActivity extends MyBaseFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		cursor = (ImageView)findViewById(R.id.cursor);
		initWidth();
	}
	private ImageView cursor;
	
	protected void movecursor(int position){
		TranslateAnimation animation = new TranslateAnimation(arrPosition[oldposition== -1?0:oldposition], arrPosition[position], 0, 0);
		if (null != animation) {
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
		oldposition = position;
	}
	
	private int[] arrPosition;
	protected void initWidth() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int screenW = outMetrics.widthPixels;
		int count = (((LinearLayout)findViewById(R.id.tab_linlayout)).getChildCount()+1)/2;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW / count, DpAndPx.dpToPx(getApplicationContext(), 3));
		cursor.setLayoutParams(params);
		arrPosition = new int[count];
		int cursorLenth = screenW / count;
		for (int i = 0; i < count; i++) {
			arrPosition[i]= cursorLenth*i;
		}
	}

}
