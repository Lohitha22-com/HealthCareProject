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
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelfPayActivity extends BaseActivity1 {

    EditText etEmail, etPhoneNumber;
    Spinner spinner;
    TextView backLink;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfpay_activity);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spinner = findViewById(R.id.spinnerCountry);
        backLink = findViewById(R.id.backLink);
        submit = findViewById(R.id.btnSubmit);

        String[] countryArray = getResources().getStringArray(R.array.country_codes_array);

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, android.R.id.text1, countryArray);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);

        backLink.setOnClickListener(v -> {
            finish();
        });

        submit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String selectedCountry = spinner.getSelectedItem().toString();

            String fullNumber = selectedCountry + phone;

            if(email.isEmpty()) {
                showErrorDialog("Please enter email");
            }
            else if(phone.isEmpty()){
                showErrorDialog("Please enter phone number");
            }
            else {
                Toast.makeText(this,"Submitted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showErrorDialog(String message) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imgX = dialog.findViewById(R.id.imgErrorX);
        View card = dialog.findViewById(R.id.cardErrorMessage);
        TextView tvMessage = dialog.findViewById(R.id.tvErrorMessage);
        Button btnOk = dialog.findViewById(R.id.btnErrorOk);

        tvMessage.setText(message);

        imgX.setTranslationY(-250f);
        imgX.setAlpha(0f);
        imgX.setScaleY(1.8f);
        imgX.setScaleY(1.8f);

        card.setTranslationY(250f);
        card.setAlpha(0f);
        card.setScaleX(1.3f);
        card.setScaleY(1.3f);

        dialog.show();

        imgX.post(() -> imgX.animate()
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start());

        card.post(() -> card.animate()
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(450)
                .setInterpolator(new OvershootInterpolator(1.0f))
                .setStartDelay(80)
                .start());

        btnOk.setOnClickListener(v -> {
            imgX.post(() -> imgX.animate()
                    .translationY(-250f)
                    .alpha(0f)
                    .scaleX(1.8f)
                    .scaleY(1.8f)
                    .setDuration(350)
                    .setInterpolator(new AccelerateInterpolator())
                    .start());

            card.post(() -> card.animate()
                    .translationY(250f)
                    .alpha(0f)
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(350)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(dialog::dismiss)
                    .start());
        });
    }
}
