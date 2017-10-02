package com.koidev.demo_paint;

import com.koidev.paint.view.BasePaintFragment;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintFragment extends BasePaintFragment {

    public static PaintFragment newInstance() {
        return new PaintFragment();
    }

    @Override
    protected int getAppBarTitle() {
        return R.string.app_title_set_signature;
    }

    @Override
    protected int getAppBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    protected int getHomeAsUpIndicatorIcon() {
        return R.drawable.ic_arrow_back_black_24dp;
    }

}
