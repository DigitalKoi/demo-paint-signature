package com.koidev.paint.view.pdf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.koidev.paint.R;
import com.koidev.paint.presenter.IPdf;
import com.koidev.paint.presenter.PdfPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.koidev.paint.presenter.PaintPresenter.REQUEST_CODE_PAINT;
import static com.koidev.paint.view.paint.PaintActivity.EXTRA_KEY_SELECTED_FILE_URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class PdfFragment extends Fragment implements IPdf.View {

    public static final int KEY_SIGN_FIRST = 1;
    public static final int KEY_SIGN_SECOND = 1;
    private static PdfFragment mFragment;
    private static String stTextForm;
    private IPdf.Presenter mPresenter;

    private TextView tvData;
    private TextView tvSing1;
    private TextView tvSing2;
    private TextView textForm;

    public static PdfFragment newInstance(String textForm) {
        stTextForm = textForm;
        if (mFragment == null) {
            mFragment = new PdfFragment();
        }
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
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
        textForm = (TextView) view.findViewById(R.id.text_form_tv_pdf);
        textForm.setText(stTextForm);

        tvData = (TextView) view.findViewById(R.id.date_text_tv_pdf);

        tvData.setText(tvData.getText() + " " + getCurrentDate());

        tvSing1 = (TextView) view.findViewById(R.id.sign1_ed_tv_pdf);
        tvSing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: call paint activity from presenter with parameters for second first
                mPresenter.callPaintActivity(KEY_SIGN_FIRST);
//                tvSing1.setVisibility(View.INVISIBLE);
            }
        });
        tvSing2 = (TextView) view.findViewById(R.id.sign2_ed_tv_pdf);
        tvSing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: call paint activity from presenter with parameters for second signature
                mPresenter.callPaintActivity(KEY_SIGN_SECOND);
//                tvSing2.setVisibility(View.INVISIBLE);
            }
        });

    }

    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy");
        return sdf.format(cal.getTime());
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
                //TODO: save pdf
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
                if (requestCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String fileUrl = extras.getString(EXTRA_KEY_SELECTED_FILE_URL);
                    Log.d("TAG", "onActivityResult: " + fileUrl);
                    Toast.makeText(
                            getActivity(),
                            "Path to signature" + fileUrl,
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                } else {
                    return;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
