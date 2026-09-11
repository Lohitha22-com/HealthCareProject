package com.example.healthcareproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareproject.api.ApiResponse;
import com.example.healthcareproject.api.ApiService;
import com.example.healthcareproject.api.RetrofitClient;
import com.example.healthcareproject.databinding.DialogErrorBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends BaseActivity1 {

    EditText etUsername, etPassword;
    TextView forgotPassword, selfpayVisit;
    Button btnLogin;
    ImageButton btnInfo;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginindex);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnInfo = findViewById(R.id.btnInfo);
        forgotPassword = findViewById(R.id.forgotPassword);
        selfpayVisit = findViewById(R.id.selfpayVisit);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if(username.isEmpty()){
                showErrorDialog("Please enter Email");
            }

            else if(password.isEmpty()){
                showErrorDialog("Please enter Password");
            }
            else {
                String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/telemedLogin";

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("sUser", username);
                    requestBody.put("sPass", password);
                } catch (JSONException e) {
                    Log.e("Register", "Failed to build request body", e);
                    return;
                }

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading.. Please wait!");
                progressDialog.setCancelable(false);
                progressDialog.show();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        requestBody,
                        response -> {
                            Log.d("Login", "Response: " + response.toString());
                            progressDialog.dismiss();

                            JSONObject data = response.optJSONObject("d");
                            Log.d("Login","Full data object: " + (data != null ? data.toString() : "null"));

                            boolean success = data != null && data.optBoolean("status", false);
                            String message = data != null ? data.optString("message", "Login failed"): "Login failed";
                            if (success) {
                                String key = data.optString("key", "");
                                String companyId = data.optString("Company_Id", "");
                                long visitTypeId = data.optLong("VisitTypeId", 1);
                                long purposeOfVisit = data.optLong("PurposeOfVisit_Id", 1);
                                boolean followUp = data.optBoolean("followup_YN", true);

                                Log.d("Login", "Generated Key: "+key);
                                Log.d("Login", "Company Id: " +companyId);

                                if(!key.isEmpty() && !companyId.isEmpty()) {
                                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, ListViews.class);
                                    intent.putExtra("key", key);
                                    intent.putExtra("companyId", companyId);
                                    intent.putExtra("VisitTypeId", visitTypeId);
                                    intent.putExtra("PurposeOfVisit_Id", purposeOfVisit);
                                    intent.putExtra("followup_YN", followUp);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    if(key.isEmpty()) {
                                        Toast.makeText(this, "Key was not received from server", Toast.LENGTH_SHORT).show();
                                    }
                                    if(companyId.isEmpty()){
                                        Toast.makeText(this, "Company id was not received from server", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            String errorMsg = error.getMessage();
                            progressDialog.dismiss();
                            if (error.networkResponse != null) {
                                errorMsg = "Status Code: " + error.networkResponse.statusCode;
                            }
                            Log.e("Login", "Network error: " + errorMsg);
                            Toast.makeText(this, "Network Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };

                requestQueue.add(jsonRequest);
            }

        });

        btnInfo.setOnClickListener(v -> {
            android.view.View view = android.view.LayoutInflater.from(login.this)
                    .inflate(R.layout.view_tooltip, null);

            final android.widget.PopupWindow popupWindow = new  android.widget.PopupWindow(
                    view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true );

            popupWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

            int offSetX = -(int)(24*getResources().getDisplayMetrics().density);
            int offSetY = 4;
            popupWindow.showAsDropDown(v, offSetX, offSetY);
        });

        selfpayVisit.setOnClickListener(v -> {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            android.content.Intent intent = new android.content.Intent(login.this, SelfPayActivity.class);
            startActivity(intent);
            progressDialog.dismiss();
        });

    }

    private void showErrorDialog(String message) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogErrorBinding binding = DialogErrorBinding.inflate(getLayoutInflater());

        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);

        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);

        binding.tvErrorMessage.setText(message);

        binding.imgErrorX.setTranslationY(-250f);
        binding.imgErrorX.setAlpha(0f);
        binding.imgErrorX.setScaleX(1.8f);
        binding.imgErrorX.setScaleY(1.8f);

        binding.cardErrorMessage.setTranslationY(250f);
        binding.cardErrorMessage.setAlpha(0f);
        binding.cardErrorMessage.setScaleX(1.3f);
        binding.cardErrorMessage.setScaleY(1.3f);

        dialog.show();

        binding.imgErrorX.post(() -> binding.imgErrorX.animate()
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator())
                .start());

        binding.cardErrorMessage.animate()
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(450)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(80)
                .start();

        binding.btnErrorOk.setOnClickListener(v -> {
            binding.imgErrorX.post(() -> binding.imgErrorX.animate()
                    .translationY(-250f)
                    .alpha(0f)
                    .scaleX(1.8f)
                    .scaleY(1.8f)
                    .setDuration(300)
                    .setInterpolator(new AccelerateInterpolator())
                    .start());

            binding.cardErrorMessage.post(() -> binding.cardErrorMessage.animate()
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
