package com.wlj.chuangbaba.activity.projectxiangqing.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.ui.BaseFragment;
import com.wlj.widget.PullToRefreshListView;
/**
 * 项目详情-基本信息
 * @author wlj
 *
 */
public class Project_JiBenXinXi extends BaseFragment implements OnClickListener {
	
	View view;// infalte的布局
	PullToRefreshListView lv_center;
	@Override
	protected int getlayout() {
		return R.layout.fragment_project_jibenxinxi;
	}
	
	@Override
	protected void initView(View view ) {
		view.setMinimumHeight(20);
		Bundle bundle = getArguments();
		Project project = (Project) bundle.getSerializable("base");
		
		if(project == null)return;
		WebView  webView = (WebView)view.findViewById(R.id.webView1);
		
		WebSettings settings = webView.getSettings();
		settings.setDefaultTextEncodingName("UTF-8");
//		settings.setAllowFileAccess(true);
//		settings.setBuiltInZoomControls(true);//会出现放大缩小的按钮
//		settings.setDisplayZoomControls(true);
		//手势放大网页以后，自动缩小回了原来的尺寸的解决
		settings.setUseWideViewPort(true);//可任意比例缩放。但会造成下部很大空白，"<meta name=\"viewport\" 控制
//		settings.setLoadWithOverviewMode(true);//初始大小为屏幕宽，会导致文字缩小
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		int int1 = bundle.getInt("position");
		String str = "";
		
		switch (int1) {
		case 1:
			str = project.getIntro();
			byte[] byteIcon= Base64.decode(str,Base64.DEFAULT);
			String data = new String(byteIcon);
            		
			webView.loadData(getHtmlData(data), "text/html; charset=UTF-8", "UTF-8");
			break;
		case 2:
			str = project.getContent();
			byte[] byteIcon2= Base64.decode(str,Base64.DEFAULT);
			String data2 = new String(byteIcon2);
			webView.setWebViewClient(new WebViewClient(){
				
				@Override
				public void onLoadResource(WebView view, String url) {
					super.onLoadResource(view, url);
				}

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					view.getSettings().setBlockNetworkImage(false);
					super.onPageStarted(view, url, favicon);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return super.shouldOverrideUrlLoading(view, url);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					view.getSettings().setBlockNetworkImage(true);
					super.onPageFinished(view, url);
				}
				
			});
			webView.loadData(getHtmlData(data2), "text/html; charset=UTF-8", null);
			break;
		case 3:
			settings.setUseWideViewPort(false);
			str = project.getFuwu();
			webView.loadData(str, "text/html; charset=UTF-8", null);
			break;

		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	 private String getHtmlData(String bodyHTML) {
		/**
		 * telephone=no就禁止了把数字转化为拨号链接！
		 * telephone=yes就开启了把数字转化为拨号链接，要开启转化功能，这个meta就不用写了,在默认是情况下就是开启！
		 * viewport的理解:http://www.cnblogs.com/2050/p/3877280.html
		 */
         String head =  "<head>" +
        		    	"<meta charset=\"utf-8\" />"+
        		    	" <meta name=\"format-detection\" content=\"telephone=no\" />"+
        		    	"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1, maximum-scale=2, minimum-scale=1, width=device-width, height=device-height \" />" +
//       <!-- WARNING: for iOS 7, remove the width=device-width and height=device-height attributes. See https://issues.apache.org/jira/browse/CB-4323 -->
						"<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
						"</head>";
         return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
     }
}
