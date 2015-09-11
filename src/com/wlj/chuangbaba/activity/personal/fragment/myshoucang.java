package com.wlj.chuangbaba.activity.personal.fragment;

import static com.wlj.chuangbaba.web.MsgContext.key_page;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.ChuangBaBaContext;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.personal.OrderActivity;
import com.wlj.chuangbaba.bean.Project;
import com.wlj.chuangbaba.bean.ProjectShouCang;
import com.wlj.chuangbaba.bean.User;
import com.wlj.ui.BaseRefreshFragment;
import com.wlj.util.ExecutorServices;
import com.wlj.util.MathUtil;
import com.wlj.util.UIHelper;
import com.wlj.util.img.LoadImage;
import com.wlj.chuangbaba.web.URLs;

public class myshoucang extends BaseRefreshFragment  {

	private DecimalFormat df;
	@Override
	public void onResume() {
		super.onResume();
		
		df = new DecimalFormat("0.00");
	}
	@Override
	public void initCommonAdapter(List<Base> listDate2) {
		
		commonAdapter = new CommonAdapter<Base>(mContext,listDate2,R.layout.item_soucang) {
			@Override
			public View getListItemview(ViewHolder viewHolder, View view,Base item, int position, ViewGroup parent) {
				ProjectShouCang projectsc =  (ProjectShouCang)item;
				final Project project = projectsc.getProject();
				view.setTag(R.id.tag_first,item);
				
				ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
				iv_icon.setVisibility(View.INVISIBLE);
				String itemPic = project.getItemPic();
				if(itemPic !=  null && !"".equals(itemPic.trim())){
					LoadImage.getinstall().addTask(URLs.HOST+itemPic, iv_icon);
				}
				
				viewHolder.setText(R.id.projectOrder, project.getPingpingpai());
				viewHolder.setText(R.id.projectName, project.getName());
				viewHolder.setText(R.id.projectType, project.getXinghao());
				viewHolder.setText(R.id.cankaoprice, df.format(MathUtil.parseDouble(project.getCankaoPrice())/100)+"元/㎡");
				
				view.findViewById(R.id.joinorder).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						UIHelper.loading("提交中……", myshoucang.this.getActivity());
						ExecutorServices.getExecutorService().execute(new Runnable() {
							
							@Override
							public void run() {
								Message obtain = Message.obtain();
								try {
//									ProjectShouCang tag = (ProjectShouCang)v.getTag(R.id.tag_first);
									Map<String, Object>  map= new HashMap<String, Object>();
									String orderId = ("" + System.currentTimeMillis()).substring(1);
									
									map.put("productId", project.getId());
									map.put("orderId", orderId );
									
									map.put("user_Type", User.type_huiyuan);
									map.put("url", URLs.createOrder);
									
									obtain.obj = ((ChuangBaBaContext)mContext).Request(getActivity(),map,null);
									obtain.what = 3;
								} catch (Exception e) {
									obtain.what = -1;
									obtain.obj = e;
								}
								mHandler.sendMessage(obtain);
							}
						});
					}
				});
				
				return null;
			}
		};
		lv_center.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					final android.view.View arg1, int arg2, long arg3) {
				UIHelper.dialog(getActivity(), "确认移除收藏",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								RemoveShouCang(arg1.getTag(R.id.tag_first));
								dialog.dismiss();
							}
						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				return true;
			}
		});
	}
	private void RemoveShouCang(Object obj) {
		final ProjectShouCang tag = (ProjectShouCang)obj;
		UIHelper.loading("移除中……", getActivity());
		ExecutorServices.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				
				Message obtain = Message.obtain();
				try {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("pubId", tag.getProject().getId());
					hashMap.put("url", URLs.shoucang_remove);
					obtain.obj =  ((ChuangBaBaContext)mContext).Request(getActivity(),hashMap,null);
					obtain.what = 1;
				} catch (Exception e) {
					obtain.what = -1;
					obtain.obj = e;
				}
				mHandler.sendMessage(obtain);
				
			}
		});
	}
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			UIHelper.loadingClose();
			switch (msg.what) {
			case 3:

				UIHelper.ToastMessage(mContext, "加入订单成功，请联系卖家确认合同金额",Toast.LENGTH_LONG);
				Intent intent = new Intent(mContext, OrderActivity.class);
				intent.putExtra("type", OrderActivity.huiyuan);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

				break;
			case 1:
				UIHelper.loadingClose();
				UIHelper.ToastMessage(getActivity(), "移除成功");
				//刷新列表
				callWeb(1, UIHelper.LISTVIEW_ACTION_REFRESH);
				break;
			case -1:
				Exception e = (Exception) msg.obj;
				UIHelper.ToastMessage(mContext, e.getMessage());
				break;
			}

		};
	};
	
	@Override
	protected BaseList webMethod(Context mContext, Bundle bundle,int pageIndex2, boolean isRefresh) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key_page, pageIndex2+"");
		map.put("url", URLs.shoucangList);
		return ((ChuangBaBaContext)mContext).getHaveCacheBaseList(getActivity(),new ProjectShouCang(),map,isRefresh);
	}

}
