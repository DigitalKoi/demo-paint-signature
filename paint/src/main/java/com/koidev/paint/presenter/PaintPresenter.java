package com.koidev.paint.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.koidev.paint.R;
import com.koidev.paint.view.paint.PaintView;

import static com.koidev.paint.presenter.PdfPresenter.EXTRA_KEY_PAINT_SIGN;
import static com.koidev.paint.view.paint.PaintActivity.EXTRA_KEY_SELECTED_FILE_URL;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintPresenter implements IPaint.Presenter {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 10001;
    public static final int REQUEST_CODE_PAINT = 1001;
    private Context mContext;
    private IPaint.View mView;

    public PaintPresenter(Context context, IPaint.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void onStartView() {
        checkDeviceStoragePermission();
    }

    private void onSelectSignature(String fileUrl, int signNumber) {
        Activity activity = mView.getActivity();
        Intent intent = new Intent(String.valueOf(REQUEST_CODE_PAINT));
        intent.putExtra(EXTRA_KEY_SELECTED_FILE_URL, fileUrl);
        intent.putExtra(EXTRA_KEY_PAINT_SIGN, signNumber);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStartView();
                }
                break;
        }
    }

    /**
     * Check local storage permission for Android 6+
     *
     * @return True - access is allowed
     */
    @Override
    public boolean checkDeviceStoragePermission() {
        boolean isAccessAllowed =
                ContextCompat.checkSelfPermission(mView.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!isAccessAllowed) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mView.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(mView.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mView.getActivity(), R.style.Theme_AppCompat);
                alertBuilder.setTitle(R.string.dialog_title_request_permission)
                        .setMessage(R.string.message_need_permission_external_storage)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((Fragment) mView).requestPermissions(
                                        new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        },
                                        REQUEST_CODE_STORAGE_PERMISSION
                                );
                            }
                        })
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ((Fragment) mView).requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
        }
        return isAccessAllowed;
    }

    @Override
    public void saveSignature(PaintView paintView, int signNumber) throws PackageManager.NameNotFoundException {
        if (checkDeviceStoragePermission()) {
            String result = paintView.saveCanvasInFile(mContext);
            if (result.equals(mContext.getString(R.string.not_saved_sign))) {
                mView.showToast(mContext.getString(R.string.not_saved_sign));
            } else {
                onSelectSignature(paintView.saveCanvasInFile(mContext), signNumber);
            }
        } else {
            mView.showToast("Not permission for write file");
        }
    }

    @Override
    public void clearCanvasView(PaintView paintView) {
        paintView.clearCanvas();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PAINT:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    String fileUrl = extras.getString(EXTRA_KEY_SELECTED_FILE_URL);
                    Log.d("TAG", "onActivityResult: " + fileUrl);

                    break;
                } else {
                    return;
                }
        }
    }
}
