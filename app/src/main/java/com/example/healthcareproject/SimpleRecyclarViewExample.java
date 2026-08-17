//package com.example.healthcareproject;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class SimpleRecyclarViewExample extends AppCompatActivity {
//
//    ArrayList<String> mobileNames = new ArrayList<>(Arrays.asList("IQOO","Vivo","Oppo","Samsung","iPone","onePlus","Nokia","Jio"));
//
//    @Override
//    public void onCreate(Bundle savedInstances){
//
//        super.onCreate(savedInstances);
//        setContentView(R.layout.example_of_recyclar_view);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
////        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
////        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
//        //recyclerView.setLayoutManager(linearLayoutManager);
//        //recyclerView.setLayoutManager(gridLayoutManager);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,linearLayoutManager.HORIZONTAL,false);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        //recyclerView.setLayoutManager(staggeredGridLayoutManager);
//
//
//        RAdapter recycleViewAdapter = new RAdapter(this, mobileNames);
//        recyclerView.setAdapter(recycleViewAdapter);
//    }
//
//}
