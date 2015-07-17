package com.hybridsquad.android.library;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 */
public class CropParams {

    public static final String CROP_TYPE = "image/*";
    public static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();

    public static final int DEFAULT_ASPECT_W = 720;//选择框比例
    public static final int DEFAULT_ASPECT_H = 400;
    
    public static final int DEFAULT_OUTPUT_W= 720;//输出图片大小
    public static final int DEFAULT_OUTPUT_H = 400;

    public Uri uri;

    public String type;
    public String outputFormat;
    public String crop;

    public boolean scale;
    public boolean returnData;
    public boolean noFaceDetection;
    public boolean scaleUpIfNeeded;

    public int aspectX;
    public int aspectY;

    public int outputX;
    public int outputY;

    public CropParams() {
        uri = CropHelper.buildUri();
        type = CROP_TYPE;
        outputFormat = OUTPUT_FORMAT;
        crop = "true";
        scale = true;
        returnData = false;
        noFaceDetection = true;
        scaleUpIfNeeded = true;
        aspectX = DEFAULT_ASPECT_W;
        aspectY = DEFAULT_ASPECT_H;
        outputX = DEFAULT_OUTPUT_W;
        outputY = DEFAULT_OUTPUT_H;
    }
}
