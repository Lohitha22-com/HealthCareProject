package com.example.healthcareproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {

    EditText etUsername;

    TextInputEditText etPassword;
    Button btnLogin;
    TextView textLink;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginindex);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textLink = findViewById(R.id.textLink);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().toLowerCase().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if(username.isEmpty()){
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            }

            else if(password.isEmpty()){
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
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
                progressDialog.show();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        requestBody,
                        response -> {
                            Log.d("Login", "Response: " + response.toString());
                            progressDialog.dismiss();

                            JSONObject data = response.optJSONObject("d");
                            boolean success = data != null && data.optBoolean("status", false);
                            String message = data != null ? data.optString("message", "Registration failed"): "Registration failed";
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


//        textLink.setPaintFlags(textLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        textLink.setTextColor(Color.GREEN);
        textLink.setOnClickListener(v -> {
//            textLink.setTextColor(Color.BLUE);
            Intent intent = new Intent(login.this, javaApp.class);
            startActivity(intent);
            finish();
        });
    }
}
