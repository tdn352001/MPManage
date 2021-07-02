package com.example.mpmanage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mpmanage.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_top, R.anim.to_bottom);
    }
}