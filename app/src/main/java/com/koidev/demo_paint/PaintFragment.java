package com.koidev.demo_paint;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintFragment extends com.koidev.paint.view.paint.PaintFragment {

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
