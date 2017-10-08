package com.koidev.paint.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.koidev.paint.R;
import com.koidev.paint.view.paint.PaintActivity;
import com.koidev.paint.view.pdf.PdfManager;

import static com.koidev.paint.view.pdf.PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_APPBAR_TITLE_RES_ID;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_APP_THEME_RES_ID;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PdfPresenter implements IPdf.Presenter {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 10001;
    private static final String EXTRA_KEY_PAINT_SIGN = "key.launch.number.sign";

    private Context mContext;
    private IPdf.View mView;

    public PdfPresenter(Context context, IPdf.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO something
                }
                break;
        }
    }

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
    public void callPaintActivity(int keySignFirst) {
        Activity activity = mView.getActivity();
        Intent intent = new Intent(mContext, PaintActivity.class);
        intent.putExtra(EXTRA_KEY_PAINT_SIGN, keySignFirst);
        intent.putExtra(KEY_APP_THEME_RES_ID, PdfManager.getInstance().getThemeResId());
        intent.putExtra(KEY_APPBAR_HOME_ICON_RES_ID, PdfManager.getInstance().getAppbarHomeIconResId());
        intent.putExtra(KEY_APPBAR_TITLE_RES_ID, PdfManager.getInstance().getAppbarTitleResId());
        mContext.startActivity(intent);


    }
}
