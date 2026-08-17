package com.example.healthcareproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class patients extends AppCompatActivity {

    CardView cardView;
    RecyclerView recyclerView;

    TextView textView;

    TextView helperText;
    ImageButton imageButton;
    EditText editText;
    Spinner spinner;
    SearchView searchView;
    RadioButton radioButton1, radioButton2;
    Button button;

    List<FormItem> items = new ArrayList<>();

    FormAdapter formAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientslist);
        recyclerView = findViewById(R.id.patientRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        formAdapter = new FormAdapter(new ArrayList<>());
        recyclerView.setAdapter(formAdapter);

        items = new BuildFormItems().buildFormItems(formAdapter::notifyDataSetChanged);
        formAdapter.setItems(items);

        TextView cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            if(hasAnyValue(items)){
                new AlertDialog.Builder(this)
                        .setTitle("Discard changes?")
                        .setMessage("You have unsaved information. Are you sure you want to discard it?")
                        .setPositiveButton("Discard",(dialog, which) -> finish())
                        .setNegativeButton("Keep Editing", null)
                        .show();
            }
            else {
                finish();
            }
        });

    }

    private boolean hasAnyValue(List<FormItem> items){
        for(FormItem item : items){
            if(item.getValue() != null && !item.getValue().isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("AttachDebug", "requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);

        if(requestCode >= 1000 && resultCode == RESULT_OK && data != null){
            int listIndex = requestCode - 1000;
            if(listIndex >= 0 && listIndex < items.size()){
                FormItem item = items.get(listIndex);
                Uri fileUri = data.getData();
                Log.d("AttachDebug", "fileUri=" + fileUri);
                if(fileUri != null){
                    item.setValue(fileUri.toString());
                }
            }
            formAdapter.notifyDataSetChanged();
        }
    }
}
