package com.wlj.chuangbaba;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.widget.Toast;

import com.hybridsquad.android.library.CropHandler;
import com.hybridsquad.android.library.CropHelper;
import com.hybridsquad.android.library.CropParams;
import com.wlj.util.Log;

/**
 * 拍照
 * @author wlj
 *
 */
public abstract class PhotoGraphActivity extends MyBaseActivity implements CropHandler{

	protected Bitmap touxiang;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 CropHelper.handleResult(this, requestCode, resultCode, data);
	}
	/**
	 * 拍照返回的图片
	 * @param touxiang2
	 */
	protected abstract void setImageView(Bitmap touxiang2);
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		 if (getCropParams() != null)
	           CropHelper.clearCachedCropFile(getCropParams().uri);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		// 其实这里什么都不要做
		super.onConfigurationChanged(newConfig);
	}
	
   CropParams mCropParams = new CropParams();
	protected void camera() {
		 Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
        startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
	}
	protected void gallery() {
		CropHelper.clearCachedCropFile(mCropParams.uri);
       startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);
	}
	   @Override
	    public CropParams getCropParams() {
	        return mCropParams;
	    }
	   
	    @Override
	    public void onPhotoCropped(Uri uri) {
	    	
//			try {
//				String imagePath = mCropParams.uri.toString();
//				touxiang = convertBitmap(new File(imagePath));
//				Log.e("getBitmapSize",""+getBitmapSize(touxiang));
//				touxiang = rotaingImageView(readPictureDegree(imagePath), touxiang);
//				setImageView(touxiang);
//				
//			} catch (IOException e) {
//				 if (getCropParams() != null)
//			           CropHelper.clearCachedCropFile(getCropParams().uri);
//			}
	    	touxiang = CropHelper.decodeUriAsBitmap(this, mCropParams.uri);
	    	setImageView(touxiang);
	    }
		@Override
		public void onCropCancel() {
		}

		@Override
		public void onCropFailed(String message) {
		}
	    @Override
		public Activity getContext() {
			return this;
		}
}
