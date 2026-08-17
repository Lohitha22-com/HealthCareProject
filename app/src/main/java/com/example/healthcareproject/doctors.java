//package com.example.healthcareproject;
//
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class doctors extends AppCompatActivity {
//
//    ListView simpleList;
//
//    String[] text = {"Robert","Johnson","Michael","Lisa","Kia"};
//
//    int[] logos = {R.drawable.img_10,R.drawable.img_11,R.drawable.img_12,R.drawable.img_13,R.drawable.img_14};
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.doctorsindex);
//        simpleList = (ListView) findViewById(R.id.list_item);
//        // CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), text, logos);
//        // simpleList.setAdapter(customAdapter);
//
//        simpleList.setOnItemClickListener((parent, view, position, id) -> {
//
//            if(position == 0){
//                Toast.makeText(this,"Robert \n Cardiologist",Toast.LENGTH_SHORT).show();
//            }
//            else if(position == 1){
//                Toast.makeText(this, "Johnson \n Dentist", Toast.LENGTH_SHORT).show();
//            }
//            else if(position == 2){
//                Toast.makeText(this, "Michael \n Gyancologist", Toast.LENGTH_SHORT).show();
//            } else if(position == 3) {
//                Toast.makeText(this, "Lisa \n Surgeon", Toast.LENGTH_SHORT).show();
//            }
//            else if(position == 4){
//                Toast.makeText(this, "Kia \n Neurologist", Toast.LENGTH_SHORT).show();
//            }
//        }) ;
//    }
//}
