package com.example.padpad.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST = 200;
    public static final int REQUEST_CODE_QR = 1;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    @OnClick(R.id.cameraImageView)
    void onCameraClicked() {
        startActivityForResult(new Intent(this, QrActivity.class), REQUEST_CODE_QR);
    }

    private String qrCodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initLayout();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle!=null) {
                qrCodeResult = bundle.getString("barcode");
            }
        }
    }

    private void initLayout() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
    }
}
