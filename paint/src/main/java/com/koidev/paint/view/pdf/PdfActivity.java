package com.koidev.paint.view.pdf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koidev.paint.R;

public class PdfActivity extends AppCompatActivity {

    public static final String KEY_APP_THEME_RES_ID = "key.app.theme.res.id";
    public static final String KEY_APPBAR_TITLE_RES_ID = "key.app.title.res.id";
    public static final String KEY_APPBAR_HOME_ICON_RES_ID = "key.app.home.icon.res.id";
    public static final String KEY_TEXT_FORM_ID = "key.app.textform.id";
    public static final String KEY_TEXT_USERS = "key.app.user";
    public static final String KEY_TEXT_SPOUSES = "key.app.spouse";

    private
    @StyleRes
    int mThemeId;
    private
    @StringRes
    int mAppbarTitleResId;
    private
    @DrawableRes
    int mAppbarHomeIconResId;
    private String stTextForm;
    private String stNameUsers;
    private String stNameSpouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gettingViewStyle();
        setContentView(R.layout.activity_pdf);
        initView();
        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PdfFragment pdfFragment = PdfFragment.newInstance(stTextForm, stNameUsers, stNameSpouses);
        ft.add(R.id.container_pdf, pdfFragment).commit();
    }

    private void gettingViewStyle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get Theme resources id
            if (extras.containsKey(KEY_APP_THEME_RES_ID)) {
                mThemeId = extras.getInt(KEY_APP_THEME_RES_ID, 0);
                if (mThemeId > 0) {
                    setTheme(mThemeId);
                }
            }
            //get appbar title
            if (extras.containsKey(KEY_APPBAR_TITLE_RES_ID)) {
                mAppbarTitleResId = extras.getInt(KEY_APPBAR_TITLE_RES_ID, 0);
            }
            //get home as up indicator icon
            if (extras.containsKey(KEY_APPBAR_HOME_ICON_RES_ID)) {
                mAppbarHomeIconResId = extras.getInt(KEY_APPBAR_HOME_ICON_RES_ID, 0);
            }
            //get text for text form
            if (extras.containsKey(KEY_TEXT_FORM_ID)) {
                stTextForm = extras.getString(KEY_TEXT_FORM_ID);
            }
              //get name Users
            if (extras.containsKey(KEY_TEXT_USERS)) {
                stNameUsers = extras.getString(KEY_TEXT_USERS);
            }
            //get name Spouse's
            if (extras.containsKey(KEY_TEXT_SPOUSES)) {
                stNameSpouses = extras.getString(KEY_TEXT_SPOUSES);
            }

        }
    }

    private void initView() {
        //setup actionbar
        if (mThemeId > 0) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pdf);
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

                super.onActivityResult(requestCode, resultCode, data);
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
        }

}
