package com.koidev.paint.presenter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.koidev.paint.view.paint.PaintView;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public interface IPaint {

    interface View {
        Activity getActivity();

        void showToast(String message);

    }

    interface Presenter {
        void onStartView();

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        String saveSignature(PaintView paintView, int signNumber) throws PackageManager.NameNotFoundException;

        void clearCanvasView(PaintView paintView);

        void sendSignature(String fileUrl, int mSignNumber);
    }
}
