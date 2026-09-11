//package com.example.healthcareproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.widget.ArrayAdapter;
//import android.widget.ExpandableListView;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.android.volley.Request;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//public class SelectCompanyGlobalUser extends BaseActivity1{
//    List<MenuModelClass> flatMenuList = new ArrayList<>();
//    List<MenuModelClass> topLevelItems = new ArrayList<>();
//    DrawerLayout drawerLayout;
//    Toolbar toolbar;
//    RadioGroup visitOrServiceRequest;
//    Spinner spinnerVisitType, spinnerPurposeOfVisit;
//    String key, companyId;
//    long visitTypeId, purposeOfVisit;
//    boolean followup;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_company_global_user);
//
//        toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        visitOrServiceRequest = findViewById(R.id.radioGroupServiceRequest);
//        spinnerVisitType = findViewById(R.id.spinnerVisitType);
//        spinnerPurposeOfVisit = findViewById(R.id.spinnerPurposeOfVisit);
//
//        ExpandableListView expandableListView = findViewById(R.id.menuExpand);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        ExpandableMenuAdapter menuAdapter = new ExpandableMenuAdapter(topLevelItems);
//        expandableListView.setAdapter(menuAdapter);
//
//        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
//            MenuModelClass menuModelClass = topLevelItems.get(groupPosition);
//            if("item".equals(menuModelClass.getType())){
//                openContentActivity(menuModelClass);
//                return true;
//            }
//            return false;
//        });
//
//        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//            MenuModelClass menuModelClass = topLevelItems.get(groupPosition).getChildren().get(childPosition);
//            openContentActivity(menuModelClass);
//            return true;
//        });
//
//        key = getIntent().getStringExtra("key");
//        companyId = getIntent().getStringExtra("companyId");
//        visitTypeId = getIntent().getLongExtra("VisitTypeId", 0);
//        purposeOfVisit = getIntent().getLongExtra("PurposeOfVisit_Id", 0);
//        followup = getIntent().getBooleanExtra("followup_YN", false);
//
//        parseMenuItems();
//        buildMenuItems();
//        sortMenuItems();
//
//        fetchVisitTypes();
//        fetchPurposeOfVisitOptions();
//    }
//
//    private void fetchVisitTypes() {
//        String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/getGlobalSMVisitTypeList";
//        JSONObject jsonObject = new JSONObject();
//        try{
//            jsonObject.put("sKey", key);
//        } catch (JSONException e) {
//            Log.e("SelectCompanyGlobal", "Failed to build getGlobalSMVisitTypeList request", e);
//            return;
//        }
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                jsonObject,
//                response -> {
//                    Log.d("SelectCompanyGlobal", "VisitTypeList Response: " + response.toString());
//                    List<String> labels = new ArrayList<>();
//                    try{
//                        JSONArray array = response.getJSONArray("d");
//                        for(int i = 0; i < array.length(); i++){
//                            JSONObject item = array.getJSONObject(i);
//                            labels.add(item.optString("VisitTypeName", "Unknown"));
//                        }
//                    }catch(JSONException e){
//                        Log.e("SelectCompanyGlobal", "Failed to parse VisitTypeList", e);
//                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labels);
//                    spinnerVisitType.setAdapter(adapter);
//                },
//                volleyError -> Log.e("SelectCompanyGlobal", "getGlobalSMVisitTypeList error: " + volleyError.getMessage())
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        Volley.newRequestQueue(this).add(request);
//    }
//
//    private void fetchPurposeOfVisitOptions() {
//        String url = "https://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/getPurposeOfVisitRoles";
//        JSONObject jsonObject = new JSONObject();
//        try{
//            jsonObject.put("sKey", key);
//        }catch (JSONException e){
//            Log.e("SelectedCompanyGlobal", "Failed to build getPurposeOfVisitRoles request", e);
//            return;
//        }
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                jsonObject,
//                response -> {
//                    Log.e("SelectedCompanyGlobal", "PurposeOfVisit Response: " + response.toString());
//                    List<String> labels = new ArrayList<>();
//                    try {
//                        JSONArray array = response.getJSONArray("d");
//                        for(int i = 0; i < array.length(); i++){
//                            JSONObject item = array.getJSONObject(i);
//                            labels.add(item.optString("PurposeOfVisitName", "Unknown"));
//                        }
//                    } catch (JSONException e) {
//                        Log.e("SelectedCompanyGlobal", "Failed to parse PurposeOfVisit list", e);
//                    }
//                    ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labels);
//                    spinnerPurposeOfVisit.setAdapter(adapter);
//                },
//                volleyError -> Log.e("SelectedCompanyGlobal", "getPurposeOfVisitsRoles error: " + volleyError.getMessage())
//        ){
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        Volley.newRequestQueue(this).add(request);
//    }
//
//    private void openContentActivity(MenuModelClass item){
//        Intent intent = new Intent(this, ContentActivity.class);
//        intent.putExtra("menu_title", item.getTitle());
//        intent.putExtra("menu_id", item.getMenu_id());
//        intent.putExtra("menu_url", item.getUrl());
//        intent.putExtra("key", key);
//        intent.putExtra("companyId", companyId);
//        startActivity(intent);
//        drawerLayout.closeDrawer(GravityCompat.START);
//    }
//
//    public String loadMenuFromAsset(){
//        String json = null;
//        try {
//            InputStream inputStream = getAssets().open("menu_items_global.json");
//            int size = inputStream.available();
//            byte[] bytes = new byte[size];
//            inputStream.read(bytes);
//            inputStream.close();
//            json = new String(bytes, StandardCharsets.UTF_8);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return json;
//    }
//
//    public void parseMenuItems() {
//        try {
//            JSONObject outObj = new JSONObject(loadMenuFromAsset());
//            JSONArray array = outObj.getJSONArray("menu_items");
//            for(int i = 0; i < array.length(); i++){
//                JSONObject innerObj = array.getJSONObject(i);
//                MenuModelClass menuModelClass = new MenuModelClass();
//                menuModelClass.setMenu_id(innerObj.getInt("menu_id"));
//                menuModelClass.setId(innerObj.getString("id"));
//                menuModelClass.setTitle(innerObj.getString("title"));
//                menuModelClass.setType(innerObj.getString("type"));
//                menuModelClass.setUrl(innerObj.isNull("url") ? null : innerObj.getString("url"));
//                menuModelClass.setParent_id(innerObj.isNull("parent_id") ? null : innerObj.getInt("parent_id"));
//                menuModelClass.setSort_order(innerObj.getInt("sort_order"));
//                flatMenuList.add(menuModelClass);
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//
//    public  void buildMenuItems() {
//        for(MenuModelClass item : flatMenuList){
//            if(item.getParent_id() == null){
//                topLevelItems.add(item);
//            } else {
//                MenuModelClass parent = findMenuById(item.getParent_id());
//                if(parent != null){
//                    parent.getChildren().add(item);
//                }
//            }
//        }
//    }
//
//    public MenuModelClass findMenuById(int menuId){
//        for(MenuModelClass item : flatMenuList){
//            if(item.getMenu_id() == menuId){
//                return item;
//            }
//        }
//        return null;
//    }
//    public void sortMenuItems(){
//        topLevelItems.sort(Comparator.comparingInt(MenuModelClass::getSort_order));
//        for(MenuModelClass parent : topLevelItems){
//            parent.getChildren().sort(Comparator.comparingInt(MenuModelClass::getSort_order));
//        }
//    }
//}
