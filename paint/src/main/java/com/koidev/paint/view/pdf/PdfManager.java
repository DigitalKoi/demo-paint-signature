package com.koidev.paint.view.pdf;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PdfManager implements IPdfManager {

    public static final int REQUEST_CODE_PDF = 1001;

    @StyleRes int mThemeResId;
    @StringRes int mAppbarTitleResId;

    @DrawableRes int mAppbarHomeIconResId;

    private static PdfManager sManager;

    public static PdfManager getInstance() {
        if (sManager == null) {
            sManager = new PdfManager();
        }

        return sManager;
    }

    @Override
    public void startPdf(Fragment fragment,
                         @StyleRes int themeResId,
                         @StringRes int appbarTitleResId,
                         @DrawableRes int appbarHomeIconResId,
                         String textForm) {

        setViewStyle(themeResId, appbarTitleResId, appbarHomeIconResId);
        Intent intent = new Intent(fragment.getActivity(), PdfActivity.class);

        if (themeResId > 0) intent.putExtra(PdfActivity.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(PdfActivity.KEY_TEXT_FORM_ID, textForm);
        fragment.startActivityForResult(intent, REQUEST_CODE_PDF);
    }

    @Override
    public void startPdf(Activity activity,
                         @StyleRes int themeResId,
                         @StringRes int appbarTitleResId,
                         @DrawableRes int appbarHomeIconResId,
                         String textForm) {
        setViewStyle(themeResId, appbarTitleResId, appbarHomeIconResId);
        Intent intent = new Intent(activity, PdfActivity.class);

        if (themeResId > 0) intent.putExtra(PdfActivity.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(PdfActivity.KEY_TEXT_FORM_ID, textForm);
        //TODO:
        activity.startActivityForResult(intent, REQUEST_CODE_PDF);
    }

    //TODO: callback

    private void setViewStyle(@StyleRes int themeResId, @StringRes int appbarTitleResId, @DrawableRes int appbarHomeIconResId) {
        mThemeResId = themeResId;
        mAppbarTitleResId = appbarTitleResId;
        mAppbarHomeIconResId = appbarHomeIconResId;
    }


    public int getThemeResId() {
        return mThemeResId;
    }

    public int getAppbarTitleResId() {
        return mAppbarTitleResId;
    }

    public int getAppbarHomeIconResId() {
        return mAppbarHomeIconResId;
    }
}
