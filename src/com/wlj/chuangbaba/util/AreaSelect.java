package com.wlj.chuangbaba.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.activity.MyMap;
import com.wlj.util.Log;

public class AreaSelect implements OnItemSelectedListener, OnTouchListener {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * 当前省的名称
	 */
	public String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	public String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	public String mCurrentDistrictName;
	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode ="";
	/**
	 * 零时市
	 */
	private String[] Citis ;
	
	private Spinner mViewProvince;
	private Spinner mViewCity;
	private Spinner mViewDistrict;
	private MyMap mContext;
//	private boolean firstone = false;
	
	private String fristProviceName;
	private String fristCityName;
	private String fristDistrictName;
	
	public AreaSelect (MyMap mContext, BDLocation location){
		
		this.mContext = mContext;
		
		initProviceCityDistrictView();
		
		//只有使用网络定位的情况下，才能获取当前位置的反地理编码描述
//		mCurrentProviceName = location.getProvince(); 
//		mCurrentCityName  = location.getCity (); 
//		mCurrentDistrictName  = location.getDistrict (); 
//		
//		fristProviceName = mCurrentProviceName;
//		fristCityName = mCurrentCityName;
//		fristDistrictName = mCurrentDistrictName;
		
		fristProviceName = location.getProvince();
		fristCityName = location.getCity ();
		fristDistrictName = location.getDistrict ();
		
		
//		firstone = true;
		
		if(fristProviceName != null){
			
			List<String> asList = Arrays.asList(mProvinceDatas);
	    	
	    	if(asList.contains(mCurrentProviceName)){
	    		mViewProvince.setSelection(asList.indexOf(mCurrentProviceName)); 
	    		fristProviceName = null;
	    	}
		}
	}
	/**
	 * 解析省市区的XML数据
	 */
    protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
    	AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			
			mProvinceDatas = new String[provinceList.size()];
        	for (int i=0; i< provinceList.size(); i++) {
        		// 遍历所有省的数据
        		mProvinceDatas[i] = provinceList.get(i).getName();
        		List<CityModel> cityList = provinceList.get(i).getCityList();
        		String[] cityNames = new String[cityList.size()];
        		for (int j=0; j< cityList.size(); j++) {
        			// 遍历省下面的所有市的数据
        			cityNames[j] = cityList.get(j).getName();
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			String[] distrinctNameArray = new String[districtList.size()];
        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
        			for (int k=0; k<districtList.size(); k++) {
        				// 遍历市下面所有区/县的数据
        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				// 区/县对于的邮编，保存到mZipcodeDatasMap
        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				distrinctArray[k] = districtModel;
        				distrinctNameArray[k] = districtModel.getName();
        			}
        			// 市-区/县的数据，保存到mDistrictDatasMap
        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
        		}
        		// 省-市的数据，保存到mCitisDatasMap
        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        	}
        } catch (Throwable e) {  
            e.printStackTrace();  
        } finally {
        	
        } 
	}
	
    
	
    private void initProviceCityDistrictView(){
    	
    	initProvinceDatas();
    	
    	mViewProvince = (Spinner) mContext.findViewById(R.id.id_province);
		mViewCity = (Spinner) mContext.findViewById(R.id.id_city);
		mViewDistrict = (Spinner) mContext.findViewById(R.id.id_district);
		
		// 添加change事件
    	mViewProvince.setOnItemSelectedListener(this);
    	// 添加change事件
    	mViewCity.setOnItemSelectedListener(this);
    	// 添加change事件
    	mViewDistrict.setOnItemSelectedListener(this);
    	
    	mViewProvince.setOnTouchListener(this);
    	mViewCity.setOnTouchListener(this);
    	mViewDistrict.setOnTouchListener(this);
    	
    	ArrayAdapter<String>  adapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.item_spinner, mProvinceDatas);
    	mViewProvince.setAdapter(adapter);
    	
    }
    /**
	 * spinner选择事件
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		//转为正常模式  方便用户下拉选择
		mContext.mCurrentMode = LocationMode.COMPASS;
		mContext.position_my.performClick();
		
		switch (parent.getId()) {
		
		case R.id.id_province:
			Citis =  mCitisDatasMap.get(mViewProvince.getItemAtPosition(position));
			
			ArrayAdapter<String>  adapter = new ArrayAdapter<String>(mContext.getApplicationContext(),R.layout.item_spinner, Citis);
			mViewCity.setAdapter(adapter);
			
			if(fristCityName != null){
				
				List<String> asList = Arrays.asList(Citis);
				
				if(asList.contains(mCurrentCityName)){
					mViewCity.setSelection(asList.indexOf(mCurrentCityName)); 
//					fristCityName = null;
				}
			}
			mCurrentProviceName = ((TextView) view).getText()+"";
			
//			if(b_province){
//				mContext.LocalSearch(mCurrentProviceName);
//			}
			
			break;
		case R.id.id_city:
			String[] Districts =  mDistrictDatasMap.get(mViewCity.getItemAtPosition(position));
			
			ArrayAdapter<String>  Districtsadapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.item_spinner, Districts);
			mViewDistrict.setAdapter(Districtsadapter);
			
			if(fristDistrictName != null){
				
				List<String> asList = Arrays.asList(Districts);
				
				if(asList.contains(fristDistrictName)){
					mViewDistrict.setSelection(asList.indexOf(fristDistrictName)); 
					fristDistrictName = null;
				}
			}
			mCurrentCityName = ((TextView) view).getText()+"";
			if(b_city || (Citis.length == 1 && b_province)){
				mContext.LocalSearch(mCurrentCityName);
			}
			break;
			
		case R.id.id_district:
			
			mCurrentDistrictName = ((TextView) view).getText()+"";
			if("其他".equals(mCurrentDistrictName)){
				break;
			}
			if(b_district){
				mContext.LocalSearch(mCurrentDistrictName);
			}
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	private boolean b_province,b_city,b_district;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {

			case R.id.id_province:
				b_province = true;
				b_city = false;
				b_district = false;
				break;
			case R.id.id_city:
				b_province = false;
				b_city = true;
				b_district = false;
				break;

			case R.id.id_district:
				b_province = false;
				b_city = false;
				b_district = true;
				
				break;
			}

		}
		
		return false;
	}
}
