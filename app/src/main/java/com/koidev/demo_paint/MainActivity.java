package com.koidev.demo_paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAINT = 1001;

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
            case REQUEST_CODE_PAINT:
                if (requestCode != Activity.RESULT_OK || data == null) return;
                Bundle extras = data.getExtras();
                //TODO: get extras
                Toast.makeText(
                        this,
                        "Please, leave a signature",
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showPaint() {
        Intent intent = new Intent(this, PaintActivity.class);
        intent.putExtra(PaintActivity.EXTRA_KEY_LAUNCH_FRAGMENT, PaintActivity.EXTRA_KEY_PAINT);
        startActivityForResult(intent, REQUEST_CODE_PAINT);
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
