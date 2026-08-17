package com.example.healthcareproject;

import android.annotation.SuppressLint;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BaseActivity extends AppCompatActivity {
    private FrameLayout rootLayout;

    @Override
    public void setContentView(int layoutResID) {
        rootLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout screenContainer = rootLayout.findViewById(R.id.screenContainer);
        getLayoutInflater().inflate(layoutResID, screenContainer, true);

        super.setContentView(rootLayout);

        FloatingActionButton btnChat = rootLayout.findViewById(R.id.btnFloatingChat);
        btnChat.setOnClickListener(v -> {
            ChatBottomSheet chatBottomSheet = new ChatBottomSheet();
            chatBottomSheet.show(getSupportFragmentManager(), "ChatBottomSheet");
        });
    }
}
