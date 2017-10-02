package com.koidev.paint.view;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public interface IBasePaintActivity {
    void setAppBarTitle(@NonNull String titleString);

    void setAppBarColor(@ColorRes int colorResId);

    void setHomeAsUpIndicatorIcon(@DrawableRes int drawable);
}
