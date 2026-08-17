package com.example.healthcareproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class javaApp extends AppCompatActivity {

    EditText etUsername;
    TextInputEditText etPassword, etConfirmPass;
    Button btnRegister, btnLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.healthindex);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String username = etUsername.getText().toString().toLowerCase().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPass.getText().toString().trim();

            if(username.equals("")){
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
            }
            else if(password.equals("")){
                Toast.makeText(this,"Enter password", Toast.LENGTH_SHORT).show();
            }
            else if(confirmPassword.equals("")){
                Toast.makeText(this, "Enter confirm password", Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(confirmPassword)){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
            else {
                //A telemed Login method url
                String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/telemedLogin";

                //Volley request queue which is a traffic manager, it sends network requests to the background
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject requestBody = new JSONObject(); //A Json object initialization
                try {
                    requestBody.put("sUser", username); //adds a key-value pair to the json object
                    requestBody.put("sPass", password);
                } catch (JSONException e) {
                    Log.e("Register", "Failed to build request body", e);
                    return;
                }
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading");
                progressDialog.show();

                //an JSON object Request
                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, //sending the data to the http server.
                        url, //url where the data to sent
                        requestBody, //json request body that has being sent
                        response -> { //reponse to the data
                            Log.d("Register", "Response: " + response.toString());
                            progressDialog.dismiss();

                            JSONObject data = response.optJSONObject("d"); //the serv
                            boolean success = data != null && data.optBoolean("status", false);
                            String message = data != null ? data.optString("message", "Registration failed"): "Registration failed";
                            if (success) {
                                String key = data.optString("key", "");

                                Log.d("Register", "Generated Key: "+key);

                                if(!key.isEmpty()) {
                                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(javaApp.this, ListViews.class);
                                    intent.putExtra("key", key);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(this, "Key was not received from server", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            String errorMsg = error.getMessage();
                            progressDialog.dismiss();
                            if (error.networkResponse != null) {
                                errorMsg = "Status Code: " + error.networkResponse.statusCode;
                            }
                            Log.e("Register", "Network error: " + errorMsg);
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

        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(javaApp.this, login.class);
            startActivity(intent);
        });

    }
}
