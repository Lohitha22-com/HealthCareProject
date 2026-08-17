//package com.example.healthcareproject;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class jsonActivity extends AppCompatActivity {
//
//    String JSON_String = "{\"patients\":[" +
//            "{\"name\":\"John\",\"email\":\"john@gmail.com\",\"phone number\":\"9876543210\",\"problem\":\"toothache\"}," +
//            "{\"name\":\"Mickel\",\"email\":\"mickeljacskon@gmail.com\",\"phone number\":\"9876543220\",\"problem\":\"stomachache\"}," +
//            "{\"name\":\"Arvaan\",\"email\":\"arvaan@gmail.com\",\"phone number\":\"9876543232\",\"problem\":\"earache\"}," +
//            "{\"name\":\"Omega\",\"email\":\"omega@yahoo.com\",\"phone number\":\"9876543310\",\"problem\":\"eyesight\"}," +
//            "{\"name\":\"Alpha\",\"email\":\"alpha@outook.com\",\"phone number\":\"9876543610\",\"problem\":\"body checkup\"}]}";
//    String name, email, phone, problem;
//
//    TextView patientName, patientEmail, patientPhone, patientProblem;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.textview);
//
//        patientName = (TextView) findViewById(R.id.name);
//        patientEmail = (TextView) findViewById(R.id.email);
//        patientPhone = (TextView) findViewById(R.id.phone);
//        patientProblem = (TextView) findViewById(R.id.problem);
//
//        try{
//            JSONObject jsonObject = new JSONObject(JSON_String);
//            JSONArray jsonArray = jsonObject.getJSONArray("patients");
//
//            JSONObject Patients = jsonArray.getJSONObject(0);
//
//            name = Patients.getString("name");
//            email = Patients.getString("email");
//            phone = Patients.getString("phone number");
//            problem = Patients.getString("problem");
//            patientName.setText("Name: " +name);
//            patientEmail.setText("Email:" +email);
//            patientPhone.setText("Phone: " +phone);
//            patientProblem.setText("Problem: " +problem);
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//    }
//}
