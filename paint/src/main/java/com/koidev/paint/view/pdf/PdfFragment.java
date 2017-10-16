package com.koidev.paint.view.pdf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.koidev.paint.R;
import com.koidev.paint.presenter.IPdf;
import com.koidev.paint.presenter.PdfPresenter;

import java.io.File;

import static com.koidev.paint.presenter.PaintPresenter.REQUEST_CODE_PAINT;
import static com.koidev.paint.presenter.PdfPresenter.EXTRA_KEY_PAINT_SIGN;
import static com.koidev.paint.view.paint.PaintActivity.EXTRA_KEY_SELECTED_FILE_URL;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_TEXT_FORM_ID;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_TEXT_SPOUSES;
import static com.koidev.paint.view.pdf.PdfActivity.KEY_TEXT_USERS;

/**
 * A placeholder fragment containing a simple view.
 */
public class PdfFragment extends Fragment implements IPdf.View {

    public static final int KEY_SIGN_FIRST = 0;
    public static final int KEY_SIGN_SECOND = 1;
    private static final String KEY_NAMES_SIGN = "key.names.path";
    private static final String KEY_SPOUSES_SIGN = "key.spouses.path";
    private static final String ARTICLE_SCROLL_POSITION = "key.position.path";
    private static String mTextForm;
    private static String mUsersName;
    private static String mSpousesName;
    private IPdf.Presenter mPresenter;

    private ImageView signNamesImg;
    private ImageView signSpousesImg;
    private String signNamesImgPath;
    private String signSpousesImgPath;
    private ProgressBar progressBar;
    private ScrollView mScrollView;

    public static PdfFragment newInstance(String textForm, String usersName, String spousesName) {
        PdfFragment mFragment = new PdfFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT_FORM_ID, textForm);
        args.putString(KEY_TEXT_USERS, usersName);
        args.putString(KEY_TEXT_SPOUSES, spousesName);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTextForm = getArguments().getString(KEY_TEXT_FORM_ID);
            mUsersName = getArguments().getString(KEY_TEXT_USERS);
            mSpousesName = getArguments().getString(KEY_TEXT_SPOUSES);
        }
        mPresenter = new PdfPresenter(getActivity().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pdf, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_view_pdf);
        signNamesImg = (ImageView) view.findViewById(R.id.sign1_im_pdf);
        signSpousesImg = (ImageView) view.findViewById(R.id.sign2_im_pdf);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_pdf);

        TextView textForm = (TextView) view.findViewById(R.id.text_form_tv_pdf);
        textForm.setText(mTextForm);

        TextView userName = (TextView) view.findViewById(R.id.user1_tv_pdf);
        userName.setText(mUsersName);

        TextView spouseName = (TextView) view.findViewById(R.id.user2_tv_pdf);
        spouseName.setText(mSpousesName);

        TextView tvData = (TextView) view.findViewById(R.id.date_text_tv_pdf);

        tvData.setText(tvData.getText() + " " + mPresenter.getCurrentDate());

        TextView tvSing1 = (TextView) view.findViewById(R.id.sign1_ed_tv_pdf);
        tvSing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.callPaintActivity(KEY_SIGN_FIRST);
            }
        });
        TextView tvSing2 = (TextView) view.findViewById(R.id.sign2_ed_tv_pdf);
        tvSing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.callPaintActivity(KEY_SIGN_SECOND);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pdf, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            getActivity().onBackPressed();
        }
        if (i == R.id.action_done) {
            if (mPresenter.checkDeviceStoragePermission()) {
                mPresenter.savePdf(mTextForm, mUsersName, mSpousesName);
            } else {
                Toast.makeText(
                        getContext(),
                        "For saving PDF document need permission for write in storage card",
                        Toast.LENGTH_SHORT
                ).show();
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PAINT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String fileUrl = extras.getString(EXTRA_KEY_SELECTED_FILE_URL);
                    int signNumber = extras.getInt(EXTRA_KEY_PAINT_SIGN);

                    showSignInImageView(fileUrl, signNumber);
                    mPresenter.setSignature(fileUrl, signNumber);
                    break;
                } else {
                    return;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showSignInImageView(String fileUrl, int signNumber) {
        File imgFile = new File(fileUrl);
        int wh = 150;
        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        if (signNumber < 1) {
            signNamesImg.setImageBitmap(Bitmap.createScaledBitmap(bm, wh, wh, false));
            signNamesImgPath = fileUrl;

        } else {
            if (bm != null)
                signSpousesImg.setImageBitmap(Bitmap.createScaledBitmap(bm, wh, wh, false));
            signSpousesImgPath = fileUrl;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(int visible) {
        progressBar.setVisibility(visible);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NAMES_SIGN, signNamesImgPath);
        outState.putString(KEY_SPOUSES_SIGN, signSpousesImgPath);
        outState.putInt(ARTICLE_SCROLL_POSITION, mScrollView.getScrollX());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(KEY_NAMES_SIGN) != null)
                showSignInImageView(savedInstanceState.getString(KEY_NAMES_SIGN), 0);
            if (savedInstanceState.getString(KEY_SPOUSES_SIGN) != null)
                showSignInImageView(savedInstanceState.getString(KEY_SPOUSES_SIGN), 1);
            if (savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION") != null) {
                final int x = savedInstanceState.getInt("ARTICLE_SCROLL_POSITION");
                mScrollView.post(new Runnable() {
                    public void run() { mScrollView.scrollTo(x, 0); }
                });
            }
        }
        super.onActivityCreated(savedInstanceState);
    }
}
