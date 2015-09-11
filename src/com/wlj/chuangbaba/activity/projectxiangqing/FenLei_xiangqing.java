package com.wlj.chuangbaba.activity.projectxiangqing;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.wlj.adapter.ImagePagerAdapter;
import com.wlj.bean.Base;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.Main;
import com.wlj.chuangbaba.activity.projectxiangqing.fragment.Project_GuiGe;
import com.wlj.chuangbaba.activity.projectxiangqing.fragment.Project_JiBenXinXi;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.util.ListUtils;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;
import com.wlj.widget.AutoScrollViewPager;
import com.wlj.widget.SwitchViewPagerDemoActivity;

/**
 * 
 * @author wlj
 * 
 */
public class FenLei_xiangqing extends MyBaseFragmentActivity implements
		OnClickListener {
	protected Base base;

	private Fragment project_GuiGe;
	private Fragment project_JiBenXinXi;
	private Fragment project_XiangQing;
	private Fragment project_KeHuFuWu;

	private TextView guige;
	private TextView jibenxinxi;
	private TextView shangpinxiangqing;
	private TextView kehufuwu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Project p = ((Project) intent.getSerializableExtra("project"));
		if (p == null) {
			UIHelper.ToastMessage(mContext, "产品数据为空");
			finish();
			return;
		} else {
			p.getThisfromid(mContext, handle);
		}
	}

	protected void initView() {

		guige = (TextView) findViewById(R.id.guige);
		jibenxinxi = (TextView) findViewById(R.id.jibenxinxi);
		shangpinxiangqing = (TextView) findViewById(R.id.shangpinxiangqing);
		kehufuwu = (TextView) findViewById(R.id.kehufuwu);

		guige.setOnClickListener(this);
		jibenxinxi.setOnClickListener(this);
		shangpinxiangqing.setOnClickListener(this);
		kehufuwu.setOnClickListener(this);
	}

	@Override
	protected void setViewDate(final Object obj) {

		Project project = (Project) obj;
		base = project;// 传递下页用
		TextView xiangqing_souc = (TextView) findViewById(R.id.xiangqing_souc);
		TextView xiangqing_xiaol = (TextView) findViewById(R.id.xiangqing_xiaol);
		TextView xiangqing_liulan = (TextView) findViewById(R.id.xiangqing_liulan);

		xiangqing_souc.setText(project.getShoucang());
		xiangqing_xiaol.setText(project.getXiaoliang());
		xiangqing_liulan.setText(project.getLiulan());

		guige.performClick();
		final List<String> list = project.getBannerPic();
		if (list == null || list.size() == 0)
			return;
		
		FrameLayout xiangqingscroll = (FrameLayout) findViewById(R.id.xiangqingscroll);
		xiangqingscroll.addView( new SwitchViewPagerDemoActivity<String>(getApplicationContext(), list){}.createview());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.guige:

			if (oldposition != 0) {
				background(0);
				guige.setBackgroundResource(R.color.red_f27e17);
				if (project_GuiGe == null) {
					project_GuiGe = new Project_GuiGe();

					Bundle args = new Bundle();
					args.putInt("position", 0);
					args.putSerializable("base", base);
					project_GuiGe.setArguments(args);
				}
				changeFragment(project_GuiGe, 0);
			}

			break;
		case R.id.jibenxinxi:

			if (oldposition != 1) {
				background(1);
				jibenxinxi.setBackgroundResource(R.color.red_f27e17);
				if (project_JiBenXinXi == null) {
					project_JiBenXinXi = new Project_JiBenXinXi();
					Bundle args = new Bundle();
					args.putInt("position", 1);
					args.putSerializable("base", base);
					project_JiBenXinXi.setArguments(args);
				}
				changeFragment(project_JiBenXinXi, 1);
			}
			break;
		case R.id.shangpinxiangqing:

			if (oldposition != 2) {
				background(2);
				shangpinxiangqing.setBackgroundResource(R.color.red_f27e17);
				if (project_XiangQing == null) {
					project_XiangQing = new Project_JiBenXinXi();

					Bundle args = new Bundle();
					args.putInt("position", 2);
					args.putSerializable("base", base);
					project_XiangQing.setArguments(args);
				}
				changeFragment(project_XiangQing, 2);
			}
			break;
		case R.id.kehufuwu:

			if (oldposition != 3) {
				background(3);
				kehufuwu.setBackgroundResource(R.color.red_f27e17);
				if (project_KeHuFuWu == null) {
					project_KeHuFuWu = new Project_JiBenXinXi();
					Bundle args = new Bundle();
					args.putInt("position", 3);
					args.putSerializable("base", base);
					project_KeHuFuWu.setArguments(args);
				}
				changeFragment(project_KeHuFuWu, 3);
			}
			break;

		}

	}

	private void background(int cur) {

		oldposition = cur;
		guige.setBackgroundResource(R.color.white);
		jibenxinxi.setBackgroundResource(R.color.white);
		shangpinxiangqing.setBackgroundResource(R.color.white);
		kehufuwu.setBackgroundResource(R.color.white);
	}

	@Override
	protected void rightOnClick() {

		Intent intent = new Intent(getApplicationContext(), Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void beforeTitle() {

		rightDrawable = R.drawable.home_white;
		title.setText("商品类别");
		right.setText("首页");
		right.setTextSize(15);

	}

	@Override
	protected int setlayout() {
		return R.layout.fenlei_xingqing2;
	}

	@Override
	protected void Switch(Message msg) {

	}

	@Override
	protected void liftOnClick() {
		finish();
	}

	@Override
	protected Object callWebMethod() throws Exception {
		return null;
	}

}
