package com.koidev.paint.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.koidev.paint.Constants;
import com.koidev.paint.R;
import com.koidev.paint.view.paint.PaintView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.koidev.paint.R.string.not_saved_sign;
import static com.koidev.paint.R.string.sign_is_clear;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintPresenter implements IPaint.Presenter {

    private IPaint.View mView;
    private String mPathToFile;
    private int recordedState;

    public PaintPresenter(IPaint.View view) {
        mView = view;
    }

    @Override
    public void onStartView() {
        checkDeviceStoragePermission();
    }

    private void onSelectSignature(String fileUrl, int signNumber) {
        Activity activity = mView.getActivity();
        Intent intent = new Intent(String.valueOf(Constants.REQUEST_CODE_PAINT));
        intent.putExtra(Constants.EXTRA_KEY_SELECTED_FILE_URL, fileUrl);
        intent.putExtra(Constants.EXTRA_KEY_PAINT_SIGN, signNumber);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStartView();
                } else {
                    mView.getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void saveSignature(final int signNumber) {
        if (!checkDeviceStoragePermission()) return;

        new SavePaintAsync(signNumber).execute();
    }

    @Override
    public void clearCanvasView(PaintView paintView) {
        paintView.clearCanvas();
    }

    @Override
    public void sendSignature(String result, int mSignNumber) {
        if (recordedState == Constants.KEY_SIGN_NOT_RECORDER) {
            mView.showToast(mView.getActivity().getResources().getString(not_saved_sign));
        } else if (recordedState == Constants.KEY_SIGN_CLEAR) {
            mView.showToast(mView.getActivity().getResources().getString(sign_is_clear));
        } else if (recordedState == Constants.KEY_SIGN_RECORDER){
            onSelectSignature(result, mSignNumber);
        }
    }

    /**
     * Check local storage permission for Android 6+
     *
     * @return True - access is allowed
     */
    private boolean checkDeviceStoragePermission() {
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
                                        Constants.REQUEST_CODE_STORAGE_PERMISSION
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
                        Constants.REQUEST_CODE_STORAGE_PERMISSION
                );
            }
        }
        return isAccessAllowed;
    }


    private class SavePaintAsync extends AsyncTask<Void, Void, String> {

        private boolean mIsEventListEmpty;
        private int mSignNumber;
        private String mFileUrl;
        private Bitmap mCanvasBitMap;

        SavePaintAsync(int signNumber) {
            mSignNumber = signNumber;

            File file = mView.getActivity().getExternalFilesDir("");
            if (file != null) {
                if (!file.exists()) file.mkdir();
                mFileUrl = file.getAbsolutePath();
            }

            PaintView paintView = mView.getPaintView();
            mIsEventListEmpty = paintView.isEventListEmpty();
            mCanvasBitMap = paintView.getCanvasBitmap();
        }

        @Override
        protected void onPreExecute() {
            mView.showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {
            recordedState = saveCanvasInFile(mFileUrl, mSignNumber);
            return mPathToFile;
        }

        @Override
        protected void onPostExecute(String result) {
            sendSignature(result, mSignNumber);
            mView.hideProgressBar();
        }

        private int saveCanvasInFile(String fileUrl, int signNumber) {
            fileUrl += "/" + UUID.randomUUID().toString() + ".png";
            if (mIsEventListEmpty && signNumber == 0) {
                return Constants.KEY_SIGN_CLEAR;
            } else
                try {
                    File img = new File(fileUrl);
                    if (img.createNewFile()) {
                        FileOutputStream out = new FileOutputStream(img);
                        Bitmap bitmap = mCanvasBitMap;
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        mPathToFile = fileUrl;
                        return Constants.KEY_SIGN_RECORDER;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "saveCanvasInFile: Ops! Problem with writing to storage!");                }
            return Constants.KEY_SIGN_NOT_RECORDER;
        }
    }
}