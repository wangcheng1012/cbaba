package com.wlj.chuangbaba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.hybridsquad.android.library.CropHandler;
import com.hybridsquad.android.library.CropHelper;
import com.hybridsquad.android.library.CropParams;
import com.wlj.util.DpAndPx;

/**
 * 拍照
 * 
 * @author wlj
 * 
 */
public abstract class PhotoGraphActivity extends MyBaseActivity implements CropHandler {

	protected Bitmap touxiang;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CropHelper.handleResult(this, requestCode, resultCode, data);
	}

	/**
	 * 拍照返回的图片
	 * 
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

	protected CropParams mCropParams = new CropParams();

	protected void camera() {
		Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
		startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
	}

	protected void gallery(boolean isCrop) {
		CropHelper.clearCachedCropFile(mCropParams.uri);
		startActivityForResult(isCrop?
				CropHelper.buildCropFromGalleryIntent(mCropParams):CropHelper.buildGalleryIntent(mCropParams.uri),
				CropHelper.REQUEST_CROP);
	}
	
	protected void cameraAndGallery(){
		mCropParams.outputY= DpAndPx.dpToPx(mContext, 400);
		mCropParams.outputX = DpAndPx.dpToPx(mContext, 720);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		final AlertDialog dialog = builder.create();
		dialog.show();
		Window window = dialog.getWindow();
		
		window.setContentView(getLayoutInflater().inflate(R.layout.piccomechoose,null));
		window.findViewById(R.id.camera).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				camera();
				dialog.dismiss();
			}
		});
		
		window.findViewById(R.id.gallery).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gallery(mCropParams.isCrop);
				dialog.dismiss();
			}
		});
		
	}

	@Override
	public CropParams getCropParams() {
		return mCropParams;
	}

	@Override
	public void onPhotoCropped(Uri uri) {

		// try {
		// String imagePath = mCropParams.uri.toString();
		// touxiang = convertBitmap(new File(imagePath));
		// Log.e("getBitmapSize",""+getBitmapSize(touxiang));
		// touxiang = rotaingImageView(readPictureDegree(imagePath), touxiang);
		// setImageView(touxiang);
		//
		// } catch (IOException e) {
		// if (getCropParams() != null)
		// CropHelper.clearCachedCropFile(getCropParams().uri);
		// }
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
