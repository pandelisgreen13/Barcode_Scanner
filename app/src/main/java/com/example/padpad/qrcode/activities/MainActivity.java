package com.example.padpad.qrcode.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.padpad.qrcode.R;
import com.example.padpad.qrcode.adapter.QrRecyclerViewAdapter;
import com.example.padpad.qrcode.callback.MainCallback;
import com.example.padpad.qrcode.utils.AppSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainCallback {

    public static final int PERMISSION_REQUEST = 200;
    public static final int REQUEST_CODE_QR = 1;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick(R.id.floatingActionButton)
    void onSendButtonClicked() {
        sendListToMail();
    }

    @OnClick(R.id.saveFloatingActionButton)
    void onSaveButtonClicked() {
        saveList(qrCodeList);
    }

    @OnClick(R.id.deleteImageView
    )
    void onDeleteButtonClicked() {
        deleteList();
    }

    @OnClick(R.id.cameraImageView)
    void onCameraClicked() {
        startActivityForResult(new Intent(this, QrActivity.class), REQUEST_CODE_QR);
    }

    private List<String> qrCodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        initLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (TextUtils.isEmpty(bundle.getString("barcode"))) {
                    return;
                }
                qrCodeList.add(bundle.getString("barcode"));
                recyclerView.setAdapter(new QrRecyclerViewAdapter(this, qrCodeList, this));
            }
        }
    }

    private void initLayout() {
        String qrSharedText = AppSharedPreferences.getStringList(this);
        if (!TextUtils.isEmpty(qrSharedText)) {
            qrCodeList.add(qrSharedText);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QrRecyclerViewAdapter(this, qrCodeList, this));
    }

    private void sendListToMail() {
        if (qrCodeList == null || qrCodeList.isEmpty()) {
            Toast.makeText(this, "Δεν έχεις σκανάρει κωδικούς", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String qrText : qrCodeList) {
            stringBuilder.append(qrText);
            stringBuilder.append("\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Barcodes:" + getCurrentTime());
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());

        try {
            startActivity(Intent.createChooser(intent, "How to send mail?"));
        } catch (android.content.ActivityNotFoundException ex) {
            //do something else
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat monthDate = new SimpleDateFormat("MM-dd-yyyy HH:mm");
            return monthDate.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    private void deleteList() {
        if (qrCodeList == null || qrCodeList.isEmpty()) {
            Toast.makeText(this, "Δεν έχεις προστέσει κωδικούς", Toast.LENGTH_SHORT).show();
            return;
        }

        qrCodeList = new ArrayList<>();
        AppSharedPreferences.setStringList(this, null);
        Toast.makeText(this, "Διαγράφηκε η λίστα", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(new QrRecyclerViewAdapter(this, null, this));
    }

    private void saveList(List<String> qrCodeList) {
        if (qrCodeList == null || qrCodeList.isEmpty()) {
            Toast.makeText(this, "Δεν έχεις προστέσει κωδικούς", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String qrText : qrCodeList) {
            stringBuilder.append(qrText);
            stringBuilder.append("\n");
        }

        AppSharedPreferences.setStringList(this, stringBuilder.toString());
        Toast.makeText(this, "Αποθηκεύτηκε η λίστα", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateList(int position) {
        if (qrCodeList == null || qrCodeList.isEmpty()) {
            return;
        }

        if (position != qrCodeList.size()) {
            qrCodeList.remove(position);
        }
    }
}
