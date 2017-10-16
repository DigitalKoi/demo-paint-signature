package com.koidev.paint.view.paint;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.koidev.paint.Constants;
import com.koidev.paint.R;
import com.koidev.paint.presenter.IPaint;
import com.koidev.paint.presenter.PaintPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaintFragment extends Fragment implements IPaint.View {

    private ProgressBar mProgressBar;
    private int mSignNumber;
    private IPaint.Presenter mPresenter;
    private PaintView mPaintView;


    public static PaintFragment newInstance(int signNumber) {
        PaintFragment fragment = new PaintFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_SIGN_NUMBER, signNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mSignNumber = getArguments().getInt(Constants.KEY_SIGN_NUMBER);
        }
        mPresenter = new PaintPresenter(getActivity().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_paint, container, false);
        mPaintView = (PaintView) view.findViewById(R.id.canvas_paint);
        intiView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStartView();
    }


    private void intiView(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_paint);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        if (w > h) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(h, h);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mPaintView.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, w);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mPaintView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paint, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (i == R.id.action_select_save) {
            mPresenter.saveSignature(mSignNumber);
            return true;
        } else if (i == R.id.action_select_clear) {
            mPresenter.clearCanvasView(mPaintView);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public PaintView getPaintView() {
        return mPaintView;
    }
}
