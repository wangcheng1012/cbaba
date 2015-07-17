package com.wlj.chuangbaba.activity.wenda;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wlj.adapter.CommonAdapter;
import com.wlj.adapter.ViewHolder;
import com.wlj.bean.Base;
import com.wlj.bean.BaseList;
import com.wlj.chuangbaba.MyBaseActivity;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Answer;
import com.wlj.chuangbaba.bean.Wen;
import com.wlj.util.MathUtil;
import com.wlj.util.StringUtils;
import com.wlj.util.img.LoadImage;
import com.wlj.web.URLs;

public class WenDa_xiangqing extends  MyBaseActivity  {

	private ListView answerlist;
	private ImageView wenpic;
	private TextView wenti;
	private TextView time;
	private TextView phone;
	@Override
	protected void beforeTitle() {
		title.setText("问答详情");
		right.setText("发布");
		right.setTextSize(15);
		rightDrawable = R.drawable.fabu;
	}

	@Override
	protected int setlayout() {
		return R.layout.wenda_xiangqing;
	}

	@Override
	protected void initView() {
		Intent intent2 = getIntent();
		Wen wen =(Wen) intent2.getSerializableExtra("base");
		answerlist = 	(ListView)findViewById(R.id.answerlist);
		wenpic = 	(ImageView)findViewById(R.id.wenpic);
		wenti = 	(TextView)findViewById(R.id.wenti);
		time = 	(TextView)findViewById(R.id.time);
		phone = 	(TextView)findViewById(R.id.phone);
		
		LoadImage.getinstall().addTask(URLs.HOST+wen.getPic1(), wenpic);
		LoadImage.getinstall().doTask();
		wenti.setText(wen.getWenti());
		time.setText(StringUtils.getTime(MathUtil.parseLong(wen.getTime())));
		phone.setText(wen.getPhone());
		
		callweb();
	}

	@Override
	protected void Switch(Message msg) {
		
	}

	@Override
	protected void setViewDate(Object obj) {
		if(obj == null)return;
		BaseList baseList = (BaseList)obj;
		List<Base> list = baseList.getBaselist();
		answerlist.setAdapter(new CommonAdapter<Base>(getApplicationContext(),list,R.layout.item_answer) {

			@Override
			public View getListItemview(ViewHolder arg0, View arg1, Base arg2,int arg3, ViewGroup arg4) {
				Answer answer = 	(Answer)arg2;
				arg0.setText(R.id.answer_item, answer.getContext());
				arg0.setText(R.id.answeruser, answer.getUserName());
				
				return null;
			}
		});
	}

	@Override
	protected void rightOnClick() {
		Intent right = new Intent(getApplicationContext(), WenDa_fawen.class);
		right.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(right);
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
