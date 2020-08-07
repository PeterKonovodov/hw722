package com.example.hw722;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "HW722";
    static final int REQUEST_CODE_PERMISSION_SEND_SMS = 100;
    static final int REQUEST_CODE_PERMISSION_CALL_PHONE = 101;
    static final int REQUEST_READ_PHONE_STATE = 102;

    private EditText editTextSms;
    private EditText editTextPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextSms = findViewById(R.id.editTextSms);


        findViewById(R.id.callBtn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE);

                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_PERMISSION_CALL_PHONE);
                } else {
                    makeCall();
                }
            }
        });

        findViewById(R.id.smsBtn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS);

                if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            REQUEST_CODE_PERMISSION_SEND_SMS);
                } else {
                    sendSms();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_PERMISSION_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }


    }

    public void makeCall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + editTextPhoneNumber.getText().toString());
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
    }

    public void sendSms() {
        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(editTextPhoneNumber.getText().toString(), null, editTextSms.getText().toString(), null, null);
            Toast.makeText(this, getString(R.string.sms_sent), Toast.LENGTH_SHORT).show();
            return;
        } catch (Exception e) {
            Toast.makeText(this, "Exception!!!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Exception!!!");
            if (e.toString().contains(Manifest.permission.READ_PHONE_STATE) && ContextCompat
                    .checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission
                        .READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                return;
            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }
}