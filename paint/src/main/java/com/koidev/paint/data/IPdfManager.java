package com.koidev.paint.data;

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
     * @param textForm            Text for form
     * @param usersName           Users name
     * @param spousesName         Spouse's name
     */
    void startPdf(Fragment fragment,
                  @StyleRes int themeResId,
                  @StringRes int appbarTitleResId,
                  @DrawableRes int appbarHomeIconResId,
                  String textForm,
                  String usersName,
                  String spousesName);

    /**
     * Start Sign-In to Youtube from activity
     *  @param activity            Activity
     * @param themeResId          Theme Resource ID (optional)
     * @param appbarTitleResId    String ID (optional)
     * @param appbarHomeIconResId Icon ID (optional)
     * @param textForm            Text for form
     * @param usersName           Users name
     * @param spousesName         Spouse's name
     */
    void startPdf(Activity activity,
                  @StyleRes int themeResId,
                  @StringRes int appbarTitleResId,
                  @DrawableRes int appbarHomeIconResId,
                  String textForm,
                  String usersName,
                  String spousesName);


}
