package com.koidev.paint.view;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koidev.paint.R;
import com.koidev.paint.data.PaintView;
import com.koidev.paint.presenter.BasePaintPresenter;
import com.koidev.paint.presenter.IBasePaint;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasePaintFragment extends Fragment implements IBasePaint.View {

    private IBasePaint.Presenter mPresenter;
    private PaintView mPaintView;

    protected abstract
    @StringRes
    int getAppBarTitle();

    protected abstract
    @ColorRes
    int getAppBarColor();

    protected abstract
    @DrawableRes
    int getHomeAsUpIndicatorIcon();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPresenter = new BasePaintPresenter(getActivity().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_paint, container, false);

        mPaintView = (PaintView) view.findViewById(R.id.canvas_paint);
//        mPaintView.setId(mPaintView.generateViewId());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStartView();
    }

    private void clearCanvas() {
        mPresenter.clearCanvasView(mPaintView);
    }

    private void saveCanvas() throws PackageManager.NameNotFoundException {
        mPresenter.saveSignature(mPaintView);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        @StringRes int titleResId = getAppBarTitle();
        @ColorRes int colorResId = getAppBarColor();
        @DrawableRes int homeIcon = getHomeAsUpIndicatorIcon();

        //set appbar title
        Activity activity = getActivity();
        if (activity instanceof IBasePaintActivity) {

            if (titleResId > 0) {
                ((IBasePaintActivity) activity).setAppBarTitle(activity.getResources().getString(titleResId));
            }
            if (homeIcon > 0) {
                ((IBasePaintActivity) activity).setHomeAsUpIndicatorIcon(homeIcon);
            }

            if (colorResId > 0) {
                ((IBasePaintActivity) activity).setAppBarColor(colorResId);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paint, menu);
        //TODO: change caps

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_select_save) {
            try {
                saveCanvas();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if (i == R.id.action_select_clear) {
            clearCanvas();
            return true;
        } else {
            return true;
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
}
