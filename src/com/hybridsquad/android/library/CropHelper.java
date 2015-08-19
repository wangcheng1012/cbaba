package com.hybridsquad.android.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.wlj.util.img.BitmapUtil;

public class CropHelper {

    public static final String TAG = "CropHelper";

    /**
     * request code of Activities or Fragments
     * You will have to change the values of the request codes below if they conflict with your own.
     */
    public static final int REQUEST_CROP = 127;
    public static final int REQUEST_CAMERA = 128;

    public static final String CROP_CACHE_FILE_NAME = "crop_cache_file.jpg";

    public static Uri buildUri() {
        return Uri
                .fromFile(Environment.getExternalStorageDirectory())
                .buildUpon()
                .appendPath(CROP_CACHE_FILE_NAME)
                .build();
    }

    public static void handleResult(CropHandler handler, int requestCode, int resultCode, Intent data) {
        if (handler == null) return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            CropParams cropParams = handler.getCropParams();
            if (cropParams == null) {
                handler.onCropFailed("CropHandler's params MUST NOT be null!");
                return;
            }
            switch (requestCode) {
                case REQUEST_CROP:
                    if (isPhotoReallyCropped(handler.getCropParams().uri)) {
                        Log.d(TAG, "Photo cropped!");
                        handler.onPhotoCropped(handler.getCropParams().uri);
                        break;
                    } else {
                        Activity context = handler.getContext();
                        if (context != null) {
                            String path = CropFileUtils.getSmartFilePath(context, data.getData());
                            boolean result = CropFileUtils.copyFile(path, handler.getCropParams().uri.getPath());
                            if (!result) {
                                handler.onCropFailed("Unknown error occurred!");
                                break;
                            }
                        } else {
                            handler.onCropFailed("CropHandler's context MUST NOT be null!");
                        }
                    }
                case REQUEST_CAMERA:
                	
                	if(!handler.getCropParams().isCrop){
                		if (isPhotoReallyCropped(handler.getCropParams().uri) ) {
                            Log.d(TAG, "NO Crop Photo");
                            handler.onPhotoCropped(handler.getCropParams().uri);
                            break;
                        }else{
                            Activity context = handler.getContext();
                            if (context != null) {
                                String path = CropFileUtils.getSmartFilePath(context, data.getData());
                                boolean result = CropFileUtils.copyFile(path, handler.getCropParams().uri.getPath());
                                if (!result) {
                                    handler.onCropFailed("Unknown error occurred!");
                                    break;
                                }
                            } else {
                                handler.onCropFailed("CropHandler's context MUST NOT be null!");
                            }
                        }
                	}
                	
                    Intent intent = buildCropFromUriIntent(handler.getCropParams());
                    Activity context = handler.getContext();
                    if (context != null) {
                        context.startActivityForResult(intent, REQUEST_CROP);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
            }
        }
    }

    public static boolean isPhotoReallyCropped(Uri uri) {
        File file = new File(uri.getPath());
        long length = file.length();
        return length > 0;
    }

    public static boolean clearCachedCropFile(Uri uri) {
        if (uri == null) return false;

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                Log.i(TAG, "Cached crop file cleared.");
            else
                Log.e(TAG, "Failed to clear cached crop file.");
            return result;
        } else {
            Log.w(TAG, "Trying to clear cached crop file but it does not exist.");
        }
        return false;
    }

    /**
     * 裁剪
     * @param params
     * @return
     */
    public static Intent buildCropFromUriIntent(CropParams params) {
        return buildCropIntent("com.android.camera.action.CROP", params);
    }

    /**
     * 带裁剪的相册选择Intent
     * @param params
     * @return
     */
    public static Intent buildCropFromGalleryIntent(CropParams params) {
        return buildCropIntent(Intent.ACTION_GET_CONTENT, params);
    }
    /**
     *  拍照
     * @param uri
     * @return
     */
    public static Intent buildCaptureIntent(Uri uri) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }
    
    /**
     *  相册
     * @param uri
     * @return
     */
    public static Intent buildGalleryIntent(Uri uri) {
    	return new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static Intent buildCropIntent(String action, CropParams params) {
        return new Intent(action, null)
                .setDataAndType(params.uri, params.type) //查看类型 详细的类型在 com.google.android.mms.ContentType   
                        //.setType(params.type)
                .putExtra("crop", params.crop)//发送裁剪信号? true才能出剪辑的小方框，不然没有剪辑功能，只能选取图
                .putExtra("scale", params.scale)//是否保留比例?
                .putExtra("aspectX", params.aspectX)// 	X方向上的比例 出现放大和缩小
                .putExtra("aspectY", params.aspectY)//  	Y方向上的比例
                .putExtra("outputX", params.outputX)//裁剪区的宽
                .putExtra("outputY", params.outputY)//裁剪区的高
                .putExtra("return-data", params.returnData)//是否将数据保留在Bitmap中返回?
                .putExtra("outputFormat", params.outputFormat) ////输入文件格式
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
                .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);//将URI指向相应的file:///...，详见代码示例
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
        	InputStream stream = context.getContentResolver().openInputStream(uri);
        	InputStream stream2 = context.getContentResolver().openInputStream(uri);
        	 bitmap = BitmapUtil.decodeSampledBitmapFromStream(stream,stream2, CropParams.outputX, CropParams.outputY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
