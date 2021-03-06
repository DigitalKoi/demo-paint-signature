package com.koidev.paint.data;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import com.koidev.paint.Constants;
import com.koidev.paint.view.pdf.PdfActivity;

import static com.koidev.paint.Constants.KEY_APPBAR_HOME_ICON_RES_ID;
import static com.koidev.paint.Constants.KEY_APPBAR_TITLE_RES_ID;
import static com.koidev.paint.Constants.REQUEST_CODE_PDF;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PdfManager implements IPdfManager {
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
                         String textForm,
                         String usersName,
                         String spousesName) {

        setViewStyle(themeResId, appbarTitleResId, appbarHomeIconResId);
        Intent intent = new Intent(fragment.getActivity(), PdfActivity.class);

        if (themeResId > 0) intent.putExtra(Constants.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(Constants.KEY_TEXT_FORM_ID, textForm);
        if (!usersName.isEmpty()) intent.putExtra(Constants.KEY_TEXT_USERS, usersName);
        if (!usersName.isEmpty()) intent.putExtra(Constants.KEY_TEXT_SPOUSES, spousesName);
        fragment.startActivityForResult(intent, Constants.REQUEST_CODE_PDF);
    }

    @Override
    public void startPdf(Activity activity,
                         @StyleRes int themeResId,
                         @StringRes int appbarTitleResId,
                         @DrawableRes int appbarHomeIconResId,
                         String textForm,
                         String usersName,
                         String spousesName) {
        setViewStyle(themeResId, appbarTitleResId, appbarHomeIconResId);
        Intent intent = new Intent(activity, PdfActivity.class);

        if (themeResId > 0) intent.putExtra(Constants.KEY_APP_THEME_RES_ID, themeResId);
        if (appbarTitleResId > 0) intent.putExtra(Constants.KEY_APPBAR_TITLE_RES_ID, appbarTitleResId);
        if (appbarHomeIconResId > 0) intent.putExtra(Constants.KEY_APPBAR_HOME_ICON_RES_ID, appbarHomeIconResId);
        if (!textForm.isEmpty()) intent.putExtra(Constants.KEY_TEXT_FORM_ID, textForm);
        if (!usersName.isEmpty()) intent.putExtra(Constants.KEY_TEXT_USERS, usersName);
        if (!usersName.isEmpty()) intent.putExtra(Constants.KEY_TEXT_SPOUSES, spousesName);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_PDF);
    }

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
