package com.example.healthcareproject.ui.listview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.healthcareproject.R;
import com.example.healthcareproject.ui.dashboard.DashboardActivity;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        Toolbar toolbar = findViewById(R.id.contentToolbar);//Accessing the toolbar from the content_activity.xml
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView titleText = findViewById(R.id.contentTitle);
        TextView urlText = findViewById(R.id.contentUrl);


        String title = getIntent().getStringExtra("menu_title");
        String url = getIntent().getStringExtra("menu_url");
        String id = getIntent().getStringExtra("menu_id");

        titleText.setText(title != null ? title: "Untitled");
        if("Dashboard".equals(title)){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        urlText.setText(url != null ? "Route: " + url : "Route: (none)");

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
