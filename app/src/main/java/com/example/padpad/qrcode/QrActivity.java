package com.example.padpad.qrcode;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.padpad.qrcode.MainActivity.REQUEST_CODE_QR;

public class QrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.scannerRelativeLayout)
    RelativeLayout scannerRelativeLayout;

    @OnClick(R.id.closeImageView)
    void onBackButtonClicked() {
        finish();
    }

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);

        scannerView = new ZXingScannerView(this);
        scannerView.setAutoFocus(true);
        scannerRelativeLayout.removeAllViews();
        scannerRelativeLayout.addView(scannerView);
        scannerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if (result == null) {
            return;
        }

        if (TextUtils.isEmpty(result.getText())) {
            Toast.makeText(this, "Δεν πέτυχε η αναγνώριση", Toast.LENGTH_SHORT).show();
            return;
        }
        onValidBarcode(result);
    }

    private void onValidBarcode(final Result result) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scannerView == null) {
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("barcode", result.getText());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }, 2000);
    }
}
