package com.koidev.paint.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.koidev.paint.R;
import com.koidev.paint.data.PaintView;
import com.koidev.paint.view.BasePaintActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class BasePaintPresenter implements IBasePaint.Presenter {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 10001;
    private static final int REQUEST_CODE_PAINT = 1001;
    private Context mContext;
    private IBasePaint.View mView;

    public BasePaintPresenter(Context context, IBasePaint.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void onStartView() {
        checkDeviceStoragePermission();
    }

    private void onSelectPhoto(String fileUrl) {
        Activity activity = mView.getActivity();
        Intent intent = new Intent(String.valueOf(REQUEST_CODE_PAINT));
        intent.putExtra(BasePaintActivity.EXTRA_KEY_SELECTED_FILE_URL, fileUrl);
        activity.setResult(Activity.RESULT_OK, intent);
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
    public void saveSignature(PaintView paintView) throws PackageManager.NameNotFoundException {
        if (checkDeviceStoragePermission()) {
            //save drawing
            paintView.setDrawingCacheEnabled(true);
            try {
                String fileUrl = mContext.getExternalFilesDir("").getAbsolutePath();
                fileUrl += "/" + UUID.randomUUID().toString() + ".png";
                File img = new File(fileUrl);
                if (img.createNewFile()) {
                    FileOutputStream out = new FileOutputStream(img);
                    Bitmap bitmap = paintView.getDrawingCache();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    mView.showToast("Drawing saved to Gallery! " + fileUrl);
                    onSelectPhoto(fileUrl);
                } else {
//                    mView.showToast("Ops! Not saved signature!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Destroy the current cache.
            paintView.destroyDrawingCache();
        } else {
            mView.showToast("Not permission for write file");
        }
    }

    @Override
    public void clearCanvasView(PaintView paintView) {
        paintView.clearCanvas();
    }
}
