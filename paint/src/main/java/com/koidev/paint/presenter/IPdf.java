package com.koidev.paint.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public interface IPdf {
    interface View {
        Activity getActivity();
    }

    interface Presenter {
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        boolean checkDeviceStoragePermission();


        void callPaintActivity(int keySignFirst);
    }
}
