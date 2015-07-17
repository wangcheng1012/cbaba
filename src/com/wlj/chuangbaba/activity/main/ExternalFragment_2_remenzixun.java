package com.wlj.chuangbaba.activity.main;

import com.dady.yoyo.activity.DeclarationActivity;
import com.dady.yoyo.activity.HotActivity;
import com.wlj.chuangbaba.MyBaseFragmentActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.web.MsgContext;
import com.wlj.ui.BaseFragment;
import com.wlj.util.Log;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 热门质询
 * @author wlj
 *
 */
public class ExternalFragment_2_remenzixun  extends BaseFragment implements OnClickListener {

	  @Override
	    public void setUserVisibleHint(boolean isVisibleToUser) {
	        super.setUserVisibleHint(isVisibleToUser);
	        if(isVisibleToUser) {
	        	Log.e("dd", " isVisible = true;");
	            onVisible();
	        } else {
	        	Log.e("dd", "  isVisible = false;");
	            onInvisible();
	        }
	    }
	    protected void onVisible(){
	        lazyLoad();
	    }
	    
	    protected  void lazyLoad(){};
	    protected void onInvisible(){}

		@Override
		protected int getlayout() {
			return R.layout.activity_main_2_remenzixun;
		}

		@Override
		protected void initView(View view) {
			
			view.setMinimumHeight(LayoutParams.WRAP_CONTENT);
			view. findViewById(R.id.remenzhixun).setOnClickListener(this);
			view. findViewById(R.id.gognsidongtai).setOnClickListener(this);
			view. findViewById(R.id.hangyedongxiang).setOnClickListener(this);
			view. findViewById(R.id.chuangbaba).setOnClickListener(this);
			view. findViewById(R.id.xinmeiyu).setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.gognsidongtai:// 公司动态
				Intent gognsidongtai = new Intent(getActivity(),
						HotActivity.class);
				gognsidongtai.putExtra("index", 1);
				gognsidongtai.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(gognsidongtai);
				break;
			case R.id.remenzhixun:
				Intent remenzhixun = new Intent(getActivity(),HotActivity.class);
				remenzhixun.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				remenzhixun.putExtra("index", 0);
				startActivity(remenzhixun);
				break;
			case R.id.hangyedongxiang:
				break;
			case R.id.chuangbaba:
				Intent chuangbaba = new Intent(getActivity(),
						DeclarationActivity.class);
				chuangbaba.putExtra("fenleiid", MsgContext.id_chuangbabaxuanyan);
				chuangbaba.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(chuangbaba);
				break;
			case R.id.xinmeiyu:
				Intent xinmeiyu = new Intent(getActivity(),
						DeclarationActivity.class);
				xinmeiyu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				xinmeiyu.putExtra("fenleiid", MsgContext.id_xinmeiyu);
				startActivity(xinmeiyu);
				break;
			}
		}
	
}
