package com.koidev.paint.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.koidev.paint.R;
import com.koidev.paint.data.FormPdfHelper;
import com.koidev.paint.data.PdfManager;
import com.koidev.paint.view.paint.PaintActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.koidev.paint.data.PdfManager.REQUEST_CODE_PDF;
import static com.koidev.paint.presenter.PaintPresenter.REQUEST_CODE_PAINT;
import static com.koidev.paint.view.paint.PaintActivity.EXTRA_KEY_SELECTED_FILE_URL;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_APPBAR_TITLE_RES_ID;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_APP_THEME_RES_ID;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PdfPresenter implements IPdf.Presenter {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 10001;
    public static final String EXTRA_KEY_PAINT_SIGN = "key.launch.number.sign";

    private Context mContext;
    private IPdf.View mView;
    private ArrayList<String> signatureList = new ArrayList<>(2);

    public PdfPresenter(Context context, IPdf.View view) {
        mView = view;
        mContext = context;
        signatureList.add(0,"");
        signatureList.add(1,"");

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
        activity.startActivityForResult(intent, REQUEST_CODE_PAINT);


    }

    @Override
    public void setSignature(String fileUrl, int signNumber) {
        signatureList.set(signNumber, fileUrl);
        for (String sign : signatureList) {
            Log.d("TAG", "setSignature: " + sign);
        }
    }

    @Override
    public void savePdf(String stTextForm, String usersName, String spousesName) {
        String urlToDir = mContext.getExternalFilesDir("").getAbsolutePath();
        if (signatureList.get(0).equals("")) {
            mView.showToast("Please write signature");
        } else {
            mView.showProgressBar(0);
            final FormPdfHelper pdfHelper = new FormPdfHelper(
                    signatureList, urlToDir, stTextForm, usersName, spousesName, getCurrentDate());
            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    return pdfHelper.createPdf();
                }

                @Override
                protected void onPostExecute(String s) {
                    mView.showProgressBar(8);
                    returnToMainPathToPdf(s);
                }
            }.execute();
        }
    }

    private void returnToMainPathToPdf(String pdf) {
        Activity activity = mView.getActivity();
        Intent intent = new Intent(String.valueOf(REQUEST_CODE_PDF));
        intent.putExtra(EXTRA_KEY_SELECTED_FILE_URL, pdf);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
        return sdf.format(cal.getTime());
    }
}
