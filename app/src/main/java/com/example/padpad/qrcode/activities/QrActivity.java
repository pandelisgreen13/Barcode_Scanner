package com.example.padpad.qrcode.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.padpad.qrcode.R;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.scannerRelativeLayout)
    RelativeLayout scannerRelativeLayout;

    @BindView(R.id.qrEditText)
    AppCompatEditText qrEditText;

    @OnClick(R.id.closeImageView)
    void onBackButtonClicked() {
        finish();
    }

    @OnClick(R.id.sendImageView)
    void onSendButtonClicked() {
        if (qrEditText != null && qrEditText.getText() != null && !TextUtils.isEmpty(qrEditText.getText().toString())) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("barcode", qrEditText.getText().toString() + " : " + result.getText());
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Δεν έχεις συμπληρώσει το πεδίο", Toast.LENGTH_SHORT).show();
        }
    }

    private ZXingScannerView scannerView;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        initLayout();
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

    private void initLayout() {
        scannerView = new ZXingScannerView(this);
        scannerView.setAutoFocus(true);
        scannerView.setAspectTolerance(0.5f);
        scannerRelativeLayout.removeAllViews();
        scannerRelativeLayout.addView(scannerView);
        scannerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onValidBarcode(final Result result) {
        if (scannerView == null) {
            return;
        }
        this.result = result;
        Toast.makeText(this, "Συμπλήρωσε το πεδίο", Toast.LENGTH_SHORT).show();
    }
}
