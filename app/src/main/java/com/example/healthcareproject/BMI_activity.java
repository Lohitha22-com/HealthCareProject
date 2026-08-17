//package com.example.healthcareproject;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//public class BMI_activity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bmi);
//
//        Toolbar toolbar = findViewById(R.id.bmiToolbar);
//        setSupportActionBar(toolbar);
//
//        EditText heightInput = findViewById(R.id.heightBMI);
//        EditText weightInput = findViewById(R.id.weightBMI);
//        Button calculationButton = findViewById(R.id.calculateButton);
//        TextView resultText = findViewById(R.id.bmiResult);
//        TextView categoryText = findViewById(R.id.bmiCategory);
//
//        calculationButton.setOnClickListener(v -> {
//            String heightStr = heightInput.getText().toString().trim();
//            String weightStr = weightInput.getText().toString().trim();
//
//            if(heightStr.isEmpty() || weightStr.isEmpty()){
//                Toast.makeText(this, "Please enter both height and weight", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            double heightCm = Double.parseDouble(heightStr);
//            double weightKg = Double.parseDouble(weightStr);
//
//            if(heightCm <= 0 || weightKg <= 0){
//                Toast.makeText(this, "Please enter valid positive numbers", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            double height = heightCm/100.0;
//            double bmi = weightKg / (height * height);
//
//            resultText.setText(String.format("Your BMI : %.1f", bmi));
//
//            String category;
//            if(bmi < 18.5){
//                category = "Underweight";
//            } else if (bmi < 25) {
//                category = "Normal weight";
//            } else if (bmi < 30) {
//                category = "Over weight";
//            }
//            else {
//                category = "Obese";
//            }
//            categoryText.setText("Category: " + category);
//        });
//
//    }
//}
