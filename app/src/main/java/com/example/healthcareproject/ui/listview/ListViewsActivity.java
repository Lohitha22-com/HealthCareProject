package com.example.healthcareproject.ui.listview;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareproject.BuildConfig;
import com.example.healthcareproject.data.model.MenuModelClass;
import com.example.healthcareproject.ui.backbutton.BaseActivity1;
import com.example.healthcareproject.ui.map.MapsActivity;
import com.example.healthcareproject.R;
import com.example.healthcareproject.ui.login.LoginActivity;
import com.example.healthcareproject.ui.patients.PatientsActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListViewsActivity extends BaseActivity1 {

    List<ListViewDashboard> listViewDashboardList = new ArrayList<>();
    List<MenuModelClass> flatMenuList = new ArrayList<>();
    List<MenuModelClass> topLevelItems = new ArrayList<>();
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView toolbarTile;
    ImageButton patientButton, firebaseNotification;
    EditText etSearchPatient;
    String key;
    String companyId;
    long visitCategory;
    long purposeOfVisit;
    Boolean followUp;
    ImageView map;
    public String token;

    CustomRecyclerViewAdapter recyclerViewAdapter;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    public static final String BASE_URL = BuildConfig.API_BASE_URL;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclar_view_dashboard);

        toolbar = findViewById(R.id.toolBar); //ToolBar for the header
        toolbarTile = findViewById(R.id.toolBarTitle);

        setSupportActionBar(toolbar); //Making the toolbar for supporting the Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("Menu Items");

        drawerLayout = findViewById(R.id.drawerLayout);  //A drawer layout for handling the toolbar, listviews and an expandable list.

        map = findViewById(R.id.imageMap);

        if (map != null) {
            map.setOnClickListener(v -> {
                Intent intent = new Intent(ListViewsActivity.this, MapsActivity.class);
                startActivity(intent);
            });
        }

        patientButton = findViewById(R.id.patientButton);
        etSearchPatient = findViewById(R.id.etSearchPatient);

        RecyclerView recyclerView = findViewById(R.id.recyclarId);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewAdapter = new CustomRecyclerViewAdapter(this, listViewDashboardList);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        ExpandableListView expandableListView = findViewById(R.id.menuExpand); //An expandable list
        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle1);
        toggle1.syncState();

        ExpandableMenuAdapter menuAdapter = new ExpandableMenuAdapter(topLevelItems); //An adapter for the expandable list
        if (expandableListView != null) {
            expandableListView.setAdapter(menuAdapter);
        }

       SharedPreferences sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        key = sharedPreferences.getString("key", "");
        companyId = sharedPreferences.getString("companyId", "");

        visitCategory = getIntent().getLongExtra("iVisitCatergory", 0);
        purposeOfVisit = getIntent().getLongExtra("iPurposeOfVisit", 0);
        followUp = getIntent().getBooleanExtra("bFollowup", false);

        String initialSearchName = getIntent().getStringExtra("patient_name");
        if (initialSearchName == null || initialSearchName.isEmpty()) {
            initialSearchName = getIntent().getStringExtra("sPatientName");
        }
        if (initialSearchName == null) {
            initialSearchName = "";
        }

        if (key != null && !key.isEmpty() && companyId != null && !companyId.isEmpty()) {
            Log.d("Dashboard", " Key: " + key + " | Company: " + companyId);
            fetchPatientVisitsFromWebService(initialSearchName);
        } else {
            Log.e("Dashboard", "Critical Error: Key or CompanyId missing!");
        }

        if (etSearchPatient != null) {
            etSearchPatient.setOnEditorActionListener((v, actionId, event) -> {
                String queryText = etSearchPatient.getText().toString().trim();
                fetchPatientVisitsFromWebService(queryText);
                return true;
            });
        }

        parseMenuItems(); // For reading the JSON file.
        buildMenuItem(); // For building the menu items like a tree by separating the parent and child menus.
        sortMenuItems(); //Sorting the menu based upon to the sort_order key.

        askNotificationPermission(); //Asking that does the app have notification permissions are not.

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> { //Getting the token from the firebase
            if(!task.isSuccessful()){
                Log.w(TAG, "Fetching FCM token failed", task.getException());
                return;
            }
            token = task.getResult();
            Log.d(TAG, "Current FCM token B: " + task.getResult());
        });

        firebaseNotification = findViewById(R.id.firebaseNotificationButton);
        if (firebaseNotification != null) {
            firebaseNotification.setOnClickListener(v -> {
                sendFCMNotification("Health Care App", "First Firebase Notification");
            });
        }

        MenuModelClass signOutItem = new MenuModelClass();
        signOutItem.setMenu_id(9999);
        signOutItem.setId("sign_out_action");
        signOutItem.setTitle("Sign Out");
        signOutItem.setType("item");
        signOutItem.setParent_id(null);
        signOutItem.setSort_order(9999);
        topLevelItems.add(signOutItem);

        menuAdapter.notifyDataSetChanged();

        if (expandableListView != null) {
            expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                MenuModelClass group = topLevelItems.get(groupPosition);

                if("sign_out_action".equals(group.getId())){
                    Intent intent = new Intent(ListViewsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }

                if ("item".equals(group.getType())) {
                    openContentActivity(group);
                    return true;
                }
                return false;
            });

            expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
                MenuModelClass child = topLevelItems.get(groupPosition).getChildren().get(childPosition);
                if(child.getTitle() != null && child.getTitle().toLowerCase().contains("follow-up")){
                    fetchPatientVisitsFromWebService("");
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                openContentActivity(child);
                return true;
            });
        }

        patientsButton();
    }

    private void fetchPatientVisitsFromWebService(String targetPatientName){
        if(key == null || companyId == null){
            Log.e("Dashboard", "Cannot trigger web method: Key is Missing.");
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = BASE_URL + "ws_webrtc/Util.asmx/getSMDashboardCompanyVisitsPatientNameList";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sKey", key);
            jsonObject.put("iCompanyId", companyId);
            jsonObject.put("sPatientName", targetPatientName);
            jsonObject.put("iVisitCatergory", visitCategory);
            jsonObject.put("bFollowup", followUp);
            jsonObject.put("iPurposeOfVisit", String.valueOf(purposeOfVisit));
        }catch (JSONException | NumberFormatException e){
            Log.e("Dashboard", "Failed to build getUser request body", e);
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("Dashboard", "Visits API Array Response: " + response.toString());
                    try {
                        listViewDashboardList.clear();

                        JSONArray array = response.getJSONArray("d");
                        for (int i = 0; i < array.length(); i++) {
                            listViewDashboardList.add(parseDashboardItem(array.getJSONObject(i)));
                        }
                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        Log.e("Dashboard", "Failed to parse reponse", e);
                    }
                },
                volleyError -> {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null) {
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.e("Dashboard", "Network error:" + error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private ListViewDashboard parseDashboardItem(JSONObject innerObj) throws JSONException {
        ListViewDashboard listViewDashboard = new ListViewDashboard();

        Iterator<String> keys = innerObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = String.valueOf(innerObj.get(key));

            listViewDashboard.setItem(key, value);
        }

        Map<String, String> currentData = listViewDashboard.getItem();

        int followup_Count = 0;
        if(currentData != null) {
            String fString = currentData.getOrDefault("Followup_Count", "0");
            try {
                followup_Count = Integer.parseInt(fString);
            } catch (NumberFormatException e) {
                Log.e("Dashboard", "Failed to parse follow-up count string: " + fString);
            }
        }
        listViewDashboard.setFollowUpCount(followup_Count);
        return listViewDashboard;
    }

    private void openContentActivity(MenuModelClass item){
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("menu_title",item.getTitle());
        intent.putExtra("menu_id", item.getMenu_id());
        intent.putExtra("menu_url", item.getUrl());
        startActivity(intent);
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public String loadFromAsset(){
        String json = null;
        try{
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            is.close();
            json = new String(bytes, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void parseMenuItems(){
        try{
            JSONObject outObj = new JSONObject(loadMenuFromAsset());
            JSONArray arr = outObj.getJSONArray("menu_items");
            for(int i = 0; i < arr.length();i++){
                JSONObject inObj = arr.getJSONObject(i);
                MenuModelClass menuModelClass = new MenuModelClass();
                menuModelClass.setMenu_id(inObj.getInt("menu_id"));
                menuModelClass.setId(inObj.getString("id"));
                menuModelClass.setTitle(inObj.getString("title"));
                menuModelClass.setType(inObj.getString("type"));

                if(!inObj.isNull("url")){
                    menuModelClass.setUrl(inObj.getString("url"));
                }
                else {
                    menuModelClass.setUrl(null);
                }
                if(!inObj.isNull("parent_id")){
                    menuModelClass.setParent_id(inObj.getInt("parent_id"));
                }
                else {
                    menuModelClass.setParent_id(null);
                }
                menuModelClass.setSort_order(inObj.getInt("sort_order"));

                flatMenuList.add(menuModelClass);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadMenuFromAsset(){
        String json = null;
        try{
            InputStream is = getAssets().open("menu_items.json");
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            is.close();
            json = new String(bytes, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void buildMenuItem() {
        for(MenuModelClass item: flatMenuList){
            if(item.getParent_id() == null) {
                topLevelItems.add(item);
            }
            else {
                MenuModelClass parent = findMenuById(item.parent_id);
                if(parent != null){
                    parent.getChildren().add(item);
                }
            }
        }
    }

    public MenuModelClass findMenuById(int menuId) {
        for(MenuModelClass item : flatMenuList){
            if(item.getMenu_id() == menuId){
                return item;
            }
        }
        return null;
    }

    public void sortMenuItems() {
        topLevelItems.sort((o1, o2) -> Integer.compare(o1.getSort_order(), o2.getSort_order()));
        for(MenuModelClass parent : topLevelItems){
            parent.getChildren().sort((o1, o2) -> Integer.compare(o1.getSort_order(), o2.getSort_order()));
        }
    }

    private void askNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void sendFCMNotification(String title, String body){
        String CHANNEL_ID = "telemed_health_alerts";
        String CHANNEL_NAME = "Telemed Health Alerts";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Critical Alerts");
            if(manager != null){
                manager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, PatientsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.caduceus_logo_red)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int notificationId = (int) System.currentTimeMillis();
        if(manager != null){
            manager.notify(notificationId, builder.build());
        }
    }

    public void patientsButton(){
        if (patientButton != null) {
            patientButton.setOnClickListener(v -> {
                Intent intent = new Intent(ListViewsActivity.this, PatientsActivity.class);
                intent.putExtra("sKey", key);
                intent.putExtra("iCompanyId", companyId);
                startActivity(intent);
            });
        }
    }
}
