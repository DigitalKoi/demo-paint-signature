package com.koidev.demo_paint;

import android.support.v4.app.FragmentTransaction;

import com.koidev.paint.view.BasePaintActivity;

public class PaintActivity extends BasePaintActivity {

    public static final String EXTRA_KEY_LAUNCH_FRAGMENT = "key.launch.fragment";
    public static final int EXTRA_KEY_PAINT = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_paint;
    }

    @Override
    protected int getToolbarResId() {
        return R.id.toolbar;
    }

    @Override
    protected void initPaintFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(
                R.id.fragment_container,
                PaintFragment.newInstance()
        ).commit();
    }


}
