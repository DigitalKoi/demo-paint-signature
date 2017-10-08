package com.koidev.paint.view.pdf;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public interface IPdfManager {

    /**
     * Start Sign-In to Youtube from fragment
     *
     * @param fragment            Fragment
     * @param themeResId          Theme Resource ID (optional)
     * @param appbarTitleResId    String ID (optional)
     * @param appbarHomeIconResId Icon ID (optional)
     */
    void startPdf(Fragment fragment,
                  @StyleRes int themeResId,
                  @StringRes int appbarTitleResId,
                  @DrawableRes int appbarHomeIconResId,
                  String textForm);

    /**
     * Start Sign-In to Youtube from activity
     *
     * @param activity            Activity
     * @param themeResId          Theme Resource ID (optional)
     * @param appbarTitleResId    String ID (optional)
     * @param appbarHomeIconResId Icon ID (optional)
     */
    void startPdf(Activity activity,
                  @StyleRes int themeResId,
                  @StringRes int appbarTitleResId,
                  @DrawableRes int appbarHomeIconResId,
                  String textForm);


}
