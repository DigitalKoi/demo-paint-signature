package com.koidev.demo_paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koidev.paint.view.pdf.PdfManager;

import butterknife.BindView;

import static com.koidev.paint.view.pdf.PdfManager.REQUEST_CODE_PDF;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_KEY_SELECTED_FILE_URL = "key.selected.file.url";

    @BindView(R.id.tv_contact_us)
    TextView tvContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PDF:
                if (resultCode == Activity.RESULT_OK || data != null) {
                    String fileUrl = data.getExtras().getString(EXTRA_KEY_SELECTED_FILE_URL);
                    Log.d("TAG", "onActivityResult: " + fileUrl);
                    Toast.makeText(
                            this,
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

    private void showPaint() {
//        Intent intent = new Intent(this, PaintActivity.class);
//        intent.putExtra(EXTRA_KEY_LAUNCH_FRAGMENT, PaintActivity.EXTRA_KEY_PAINT);
//        startActivityForResult(intent, REQUEST_CODE_PDF);
        PdfManager.getInstance().startPdf(
                this,
                R.style.AppTheme_NoActionBar /*Theme Resource ID (optional)*/,
                R.string.app_title_set_signature /*String Resource ID (optional)*/,
                R.drawable.ic_arrow_back_black_24dp /*Icon Resource ID (optional)*/,
                getResources().getString(R.string.text_form)
        );
    }

    private void initView() {
        if (tvContactUs != null)
            tvContactUs.setMovementMethod(LinkMovementMethod.getInstance());

        Button button = (Button) findViewById(R.id.btn_paint_lib);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaint();
            }
        });
    }


}
