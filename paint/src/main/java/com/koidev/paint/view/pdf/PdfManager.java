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

    public static final int REQUEST_CODE_START_PDF = 5555;

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
        Intent intent = new Intent(fragment.getActivity(), PdfActivity.class);

        if (themeResId > 0) intent.putExtra(PdfActivity.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(PdfActivity.KEY_TEXT_FORM_ID, textForm);
        fragment.startActivityForResult(intent, REQUEST_CODE_START_PDF);
    }

    @Override
    public void startPdf(Activity activity,
                         @StyleRes int themeResId,
                         @StringRes int appbarTitleResId,
                         @DrawableRes int appbarHomeIconResId,
                         String textForm) {
        Intent intent = new Intent(activity, PdfActivity.class);

        if (themeResId > 0) intent.putExtra(PdfActivity.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(PdfActivity.KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(PdfActivity.KEY_TEXT_FORM_ID, textForm);
        activity.startActivityForResult(intent, REQUEST_CODE_START_PDF);
    }

    //TODO: callback
}
