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

        void showToast(String message);

        void showProgressBar(int visible);
    }

    interface Presenter {
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        boolean checkDeviceStoragePermission();

        void callPaintActivity(int keySignFirst);

        void setSignature(String fileUrl, int signNumber);

        void savePdf(String stTextForm, String userName, String spouseName);

        String getCurrentDate();

    }
}
