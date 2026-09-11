package com.example.healthcareproject;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showInforDialog("Telemed: Back Button is disabled");
            }
        });
    }
    private void showInforDialog(String message){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_info);

        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.imgInfo);
        TextView textView = dialog.findViewById(R.id.infoMessage);
        View view = dialog.findViewById(R.id.cardInfoMessage);
        Button button = dialog.findViewById(R.id.btnInfo);

        textView.setText(message);

        imageView.setTranslationY(-250f);
        imageView.setAlpha(0f);
        view.setTranslationY(250f);
        view.setAlpha(0f);

        dialog.show();

        imageView.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(450)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(450)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        button.setOnClickListener(v -> {
            imageView.post(() -> imageView.animate()
                    .translationY(-250f)
                    .alpha(0f)
                    .scaleX(1.8f)
                    .scaleY(1.8f)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .start());

            view.post(() -> view.animate()
                    .translationY(250f)
                    .alpha(0f)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(dialog::dismiss)
                    .start());
        });
    }
}
