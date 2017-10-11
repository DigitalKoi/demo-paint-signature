package com.koidev.paint.view.paint;


import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.koidev.paint.R;
import com.koidev.paint.presenter.IPaint;
import com.koidev.paint.presenter.PaintPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaintFragment extends Fragment implements IPaint.View {

    private static PaintFragment mFragment;
    private static int mSignNumber;
    private IPaint.Presenter mPresenter;
    private PaintView mPaintView;


    public static PaintFragment newInstance(int signNumber) {
        mSignNumber = signNumber;
        if (mFragment == null) {
            mFragment = new PaintFragment();
        }
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPresenter = new PaintPresenter(getActivity().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_paint, container, false);
        mPaintView = (PaintView) view.findViewById(R.id.canvas_paint);
        return view;
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
        mPresenter.saveSignature(mPaintView, mSignNumber);
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
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: this
        super.onActivityResult(requestCode, resultCode, data);
    }
}
