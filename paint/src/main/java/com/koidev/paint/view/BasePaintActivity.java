package com.koidev.paint.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public abstract class BasePaintActivity extends AppCompatActivity implements IBasePaintActivity {

    private static final String EXTRA_KEY_LAUNCH_FRAGMENT = "key.launch.fragment";
    public static final String EXTRA_KEY_SELECTED_FILE_URL = "key.selected.file.url";
    public static final int EXTRA_KEY_PAINT = 1;
    private ActionBar mActionBar;

    @LayoutRes
    protected abstract int getLayoutResId();

    @IdRes
    protected abstract int getToolbarResId();

    /**
     * Instantiate fragment for Paint screen
     */
    protected abstract void initPaintFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initToolbar();
        populateView();
    }

    private void populateView() {
        Intent intent = getIntent();
        int launchFragmentCode = 0;
        if (intent != null && intent.getExtras() != null) {
            launchFragmentCode = intent.getExtras().getInt(EXTRA_KEY_LAUNCH_FRAGMENT, 0);
        }

        switch (launchFragmentCode) {
            case EXTRA_KEY_PAINT:
                initPaintFragment();
                break;
            default:
                Toast.makeText(this, "Wrong action: fragment code - " + launchFragmentCode, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void initToolbar() {
        @IdRes int toolbarResId = getToolbarResId();
        if (toolbarResId == 0 ) return;

        Toolbar toolbar = (Toolbar) findViewById(toolbarResId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        mActionBar = getSupportActionBar();
    }

    @Override
    public void setAppBarColor(@ColorRes int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(
                    getColorCompatible(this, colorResId)
            );
        }
    }

    @Override
    public void setAppBarTitle(@NonNull String titleString) {
        if (mActionBar != null)
            mActionBar.setTitle(titleString);
    }

    @Override
    public void setHomeAsUpIndicatorIcon(@DrawableRes int drawable) {
        if (mActionBar != null) {
            if (drawable > 0) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setHomeAsUpIndicator(drawable);
            } else {
                mActionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @ColorInt
    private static int getColorCompatible(Context context, @ColorRes int id) {
        if (context == null) {
            throw new RuntimeException("Context can not be NULL");
        }
        if (id == 0) {
            throw new RuntimeException("Color Resource ID can not be 0");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
