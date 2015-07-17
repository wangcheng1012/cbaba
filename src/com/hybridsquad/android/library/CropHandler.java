package com.hybridsquad.android.library;

import android.app.Activity;
import android.net.Uri;

/**
 */
public interface CropHandler {

    void onPhotoCropped(Uri uri);

    void onCropCancel();

    void onCropFailed(String message);

    CropParams getCropParams();

    Activity getContext();
}
