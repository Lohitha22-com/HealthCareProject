package com.example.healthcareproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    ListView simpleList;

    List<DashboardItem> dashboardItemList= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardindex);

        Toolbar toolbar = findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        simpleList = findViewById(R.id.list_dashboard1);


        String JSON_String = "{\"dashboard\":[" +
                "{\"title\":\"Patients\"}," +
                "{\"title\":\"Doctors\"}," +
                "{\"title\":\"Appointments\"}," +
                "{\"title\":\"Lab reports\"}]}";

        int[] logos = {R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,R.drawable.img_4};


        try{
            JSONObject outerObj = new JSONObject(JSON_String);
            JSONArray titleArray = outerObj.getJSONArray("dashboard");

            for(int i = 0; i < titleArray.length();i++){
                JSONObject innerObje = titleArray.getJSONObject(i);

                String title = innerObje.getString("title");

                dashboardItemList.add(new DashboardItem(title, logos[i]));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DashboardAdapter adapter = new DashboardAdapter(this, dashboardItemList);
        simpleList.setAdapter(adapter);

        simpleList.setOnItemClickListener((parent, view, position, id) -> {
            DashboardItem tappedItem = dashboardItemList.get(position);
            if("Patients".equals(tappedItem.getTitle())){
                Intent intent = new Intent(this, patients.class);
                intent.putExtra("fromDashboard",true);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
