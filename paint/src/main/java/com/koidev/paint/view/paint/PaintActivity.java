package com.koidev.paint.view.paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.koidev.paint.Constants;
import com.koidev.paint.R;

/**
 * @author KoiDev
 * @email DevSteelKoi@gmail.com
 */

public class PaintActivity extends AppCompatActivity {
    private
    @StyleRes
    int mThemeId;
    private
    @StringRes
    int mAppbarTitleResId;
    private
    @DrawableRes
    int mAppbarHomeIconResId;
    private int signNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gettingViewStyle();
        setContentView(R.layout.activity_paint);
        initView();
        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaintFragment paintFragment = PaintFragment.newInstance(signNumber);
        ft.add(R.id.container_paint, paintFragment).commit();
    }

    private void gettingViewStyle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get Theme resources id
            if (extras.containsKey(Constants.KEY_APP_THEME_RES_ID)) {
                mThemeId = extras.getInt(Constants.KEY_APP_THEME_RES_ID, 0);
                if (mThemeId > 0) {
                    setTheme(mThemeId);
                }
            }
            //get appbar title
            if (extras.containsKey(Constants.KEY_APPBAR_TITLE_RES_ID)) {
                mAppbarTitleResId = extras.getInt(Constants.KEY_APPBAR_TITLE_RES_ID, 0);
            }
            //get home as up indicator icon
            if (extras.containsKey(Constants.KEY_APPBAR_HOME_ICON_RES_ID)) {
                mAppbarHomeIconResId = extras.getInt(Constants.KEY_APPBAR_HOME_ICON_RES_ID, 0);
            }
            //get id signature
            if (extras.containsKey(Constants.EXTRA_KEY_PAINT_SIGN)) {
                signNumber = extras.getInt(Constants.EXTRA_KEY_PAINT_SIGN);
                Log.d("TAG", "gettingViewStyle: " + signNumber);
            }
        }
    }

    private void initView() {
        //setup actionbar
        if (mThemeId > 0) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_paint);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    if (mAppbarTitleResId > 0) actionBar.setTitle(mAppbarTitleResId);
                    if (mAppbarHomeIconResId > 0) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setHomeAsUpIndicator(mAppbarHomeIconResId);
                    }
                }
            }
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_PAINT:
                if (requestCode == Activity.RESULT_OK || data != null) {
                    Bundle extras = data.getExtras();
                    String fileUrl = extras.getString(Constants.EXTRA_KEY_SELECTED_FILE_URL);
                    Log.d("TAG", "onActivityResult: " + fileUrl);

                    break;
                } else {
                    return;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
