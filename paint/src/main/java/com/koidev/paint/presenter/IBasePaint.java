package com.koidev.paint.presenter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.koidev.paint.data.PaintView;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public interface IBasePaint {

    interface View {
        Activity getActivity();

        void showToast(String message);

    }

    interface Presenter {
        void onStartView();

        void onStopView();

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        //TODO: this
        boolean checkDeviceStoragePermission();

        void saveSignature(PaintView paintView) throws PackageManager.NameNotFoundException;

        void clearCanvasView(PaintView paintView);
    }
}
