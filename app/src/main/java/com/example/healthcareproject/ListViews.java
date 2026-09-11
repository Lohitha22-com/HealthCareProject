package com.example.healthcareproject;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListViews extends BaseActivity1 {

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

        map.setOnClickListener(v -> {
            Intent intent = new Intent(ListViews.this, MapsActivity.class);
            startActivity(intent);
        });

        patientButton = findViewById(R.id.patientButton);
        etSearchPatient = findViewById(R.id.etSearchPatient);

        RecyclerView recyclerView = findViewById(R.id.recyclarId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        NavigationView navigationView = (NavigationView) findViewById(R.id.menu_nav);
//        navigationView.setNavigationItemSelectedListener( item -> {
//            int id = item.getItemId();
//            if(id == R.id.dashboard){
//                Toast.makeText(this,"Dashboard",Toast.LENGTH_SHORT).show();
//            }
//            else if(id == R.id.logout){
//                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
//            }
//            drawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        });

        ExpandableListView expandableListView = findViewById(R.id.menuExpand); //An expandable list
        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle1);
        toggle1.syncState();

        ExpandableMenuAdapter menuAdapter = new ExpandableMenuAdapter(topLevelItems); //An adapter for the expandable list, it contains top level items.
        expandableListView.setAdapter(menuAdapter);

        recyclerViewAdapter = new CustomRecyclerViewAdapter(this, listViewDashboardList);
        recyclerView.setAdapter(recyclerViewAdapter);

        key = getIntent().getStringExtra("key");
        companyId = getIntent().getStringExtra("companyId");
        visitCategory = getIntent().getLongExtra("VisitTypeId", 0);
        purposeOfVisit = getIntent().getLongExtra("PurposeOfVisit_Id", 0);
        followUp = getIntent().getBooleanExtra("followup_YN", false);

        String initialSearchName = getIntent().getStringExtra("patient_name");
        if (initialSearchName == null) {
            initialSearchName = "";
        }

        if (key != null && !key.isEmpty() && companyId != null && !companyId.isEmpty()) {
            Log.d("Dashboard", "Initializing auto-load with Key: " + key + " | Company: " + companyId);
            fetchPatientVisitsFromWebService(initialSearchName);
        } else {
            Log.e("Dashboard", "Critical Error: Key or CompanyId token parameters are completely missing!");
            Toast.makeText(this, "Authentication tokens missing.", Toast.LENGTH_SHORT).show();
        }

        etSearchPatient.setOnEditorActionListener((v, actionId, event) -> {
            String queryText = etSearchPatient.getText().toString().trim();
            fetchPatientVisitsFromWebService(queryText);
            return true;
        });

        parseMenuItems(); // For reading the JSON file.
        buildMenuItem(); // For building the menu items like a tree by separating the parent and child menus.
        sortMenuItems(); //Sorting the menu based upon to the sort_order key.
//
//        FirebaseApp.initializeApp();
//
//        FileInputStream refreshToken = new FileInputStream("C:/Users/lohitha.yerra/AndroidStudioProjects/HealthCareProject/app/src/main/assets/health-care-project-2207-firebase-adminsdk-fbsvc-7eee320af3.json");
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
//                .build();
//
//        FirebaseApp.initializeApp(options);

        askNotificationPermission(); //Asking that does the app have notification permissions are not.

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> { //Getting the token from the firebase
            if(!task.isSuccessful()){ //If the firebase had gave a token or not. If it is not succesfully return the token means then print the exception.
                Log.w(TAG, "Fetching FCM token failed", task.getException());
                return;
            }
            token = task.getResult(); //If it is successfully return a token means it will store in the variable name token.
            Log.d(TAG, "Current FCM token B: " + task.getResult());

//            sendTokenToBackend(token); //The returned token now is passed into this method.
        });

        firebaseNotification = findViewById(R.id.firebaseNotificationButton); //An image button for getting the notification
        firebaseNotification.setOnClickListener(new View.OnClickListener() { //On clicking the button it will generate or push notification.
            @Override
            public void onClick(View view) {
               sendFCMNotification("Health Care App", "First Firebase Notification"); //In the send notification passing the title and body of the notification.
            }
        });

        MenuModelClass signOutItem = new MenuModelClass();
        signOutItem.setMenu_id(9999);
        signOutItem.setId("sign_out_action");
        signOutItem.setTitle("Sign Out");
        signOutItem.setType("item");
        signOutItem.setParent_id(null);
        signOutItem.setSort_order(9999);
        topLevelItems.add(signOutItem);

        menuAdapter.notifyDataSetChanged();

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> { //A parent group listener when clicking on it will show child menus.
            MenuModelClass group = topLevelItems.get(groupPosition);

            if("sign_out_action".equals(group.getId())){
                Intent intent = new Intent(ListViews.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }

            if ("item".equals(group.getType())) { //If the key type is item then it will redirect to the openContentActivity() method. If not then it will return false.
                openContentActivity(group);
                return true;
            }
            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> { //A child group Listener runs whenever a child is clicked.
            MenuModelClass child = topLevelItems.get(groupPosition).getChildren().get(childPosition);
            if(child.getTitle().toLowerCase().contains("follow-up")){
                fetchPatientVisitsFromWebService("");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            openContentActivity(child);
            return true;
        });

        patientsButton();
    }

    private void fetchPatientVisitsFromWebService(String targetPatientName){
        if(key == null || companyId == null){
            Log.e("Dashboard", "Cannot trigger web method: Missing Authentication Credentials.");
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://161.129.91.101:90/ws_webrtc/Util.asmx/getSMDashboardCompanyVisitsPatientNameList";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sKey", key);
            jsonObject.put("iCompanyId", "42444");
            jsonObject.put("sPatientName", targetPatientName);
            jsonObject.put("iVisitCatergory", 0);
            jsonObject.put("bFollowup", false);
            jsonObject.put("iPurposeOfVisit", "0");
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
                        recyclerViewAdapter.notifyDataSetChanged();
//                        fetchUser(key, recyclerViewAdapter);
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
//                    fetchUser(key, recyclerViewAdapter);
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

//    private void fetchUser(String key, CustomRecyclerViewAdapter adapter) {
//        String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/getUser";
//        Map<String, String> params = new HashMap<>();
//        params.put("sKey", key);
//
//        JSONObject jsonObject = new JSONObject(params);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                jsonObject,
//                response -> {
//                    Log.d("Dashboard", "Response: " + response.toString());
//
//                    try {
//                        if(response.has("d")){
//                            Object value = response.get("d");
//
////                            if(value instanceof  JSONArray){
////                                JSONArray array = (JSONArray) value;
////                                for(int i = 0; i < array.length(); i++){
////                                    listViewDashboardList.add(parseDashboardItem(array.getJSONObject(i)));
////                                }
////                            }
//                            if(value instanceof JSONObject){
//                                JSONObject userObj = (JSONObject) value;
//                                String doctorName = userObj.optString("UserName", "Doctor");
//                                Log.d("Dashboard", "Welcome active doctor session profile: "+doctorName);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//
//                    } catch (JSONException e) {
//                        Log.e("Dashboard", "Failed to parse response", e);
//                    }
//                },
//                volleyError -> {
//                    String errorMsg = volleyError.getMessage();
//                    if (volleyError.networkResponse != null) {
//                        errorMsg = "Status Code: " + volleyError.networkResponse.statusCode;
//                    }
//                    Log.e("Dashboard", "Network error: " + errorMsg);
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        Volley.newRequestQueue(this).add(jsonObjectRequest);
//    }

//    private int parseIntOrDefault(String text, int defaultValue) {
//        try {
//            return Integer.parseInt(text.trim());
//        } catch (NumberFormatException e) {
//            return defaultValue;
//        }
//    }

    private void openContentActivity(MenuModelClass item){
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("menu_title",item.getTitle());
        intent.putExtra("menu_id", item.getMenu_id());
        intent.putExtra("menu_url", item.getUrl());
//      toolbarTitle.setText(item.getTitle());
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START); //closing the drawer from start in all the languages.
    }

    public String loadFromAsset(){ //Accessing the data.json file from the asset folder.
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

    public void parseMenuItems(){ //
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
        for(MenuModelClass item: flatMenuList){ //Looping through every menu item.
            if(item.getParent_id() == null) { //If parent id is null it means it doesn't have any child menus.
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

    public MenuModelClass findMenuById(int menuId) { //for searching the menu by its menu_id.
        for(MenuModelClass item : flatMenuList){ //Looping through all the menu items.
            if(item.getMenu_id() == menuId){ // if Item id is equals to the menuId then it will return the item.
                return item;
            }
        }
        return null;
    }
    public void sortMenuItems() { //for sorting the child and parent menu items.
        topLevelItems.sort(new Comparator<MenuModelClass>() { //sorting the entire top level list and compares the objects.
            @Override
            public int compare(MenuModelClass o1, MenuModelClass o2) { //Passing 2 parameters for sorting the objects.
                return Integer.compare(o1.getSort_order(), o2.getSort_order()); //comparing the index of the objects or keys.
            }
        });
        for(MenuModelClass parent : topLevelItems){ //Looping over the parent menus
            parent.getChildren().sort(new Comparator<MenuModelClass>() {
                @Override
                public int compare(MenuModelClass o1, MenuModelClass o2) { //Comparing the 2 objects.
                    return Integer.compare(o1.getSort_order(), o2.getSort_order());
                }
            });
        }
    }

    private void askNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //Checking the app is running in the android version 13 or newer.
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { //Checking the app's current position. It verifies if the post-notification is equal to the package manager.permision granted or not
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS},// If the permission in the phone not granted, then a dilaog or pop up in the device ask the user to allow or deny notifications for the app.
                        NOTIFICATION_PERMISSION_CODE); //An integer, it serves as a unique request id, when the user clicks allow or deny the system returns this code.
            }
        }
    }

    private void sendFCMNotification(String title, String body){ //A sendNotification method by parsing the title and body
        String CHANNEL_ID = "telemed_health_alerts"; //A channel id
        String CHANNEL_NAME = "Telemed Health Alerts"; //A channel name
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); //A notification manager as the notification controller for managing the notification and tells the android show the notification.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //Checking the app is running in the android version 13 or newer.
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH); //Creating the notification channel for the telemed health alerts with the high importance.
            channel.setDescription("Critical Alerts"); //Setting the description for the channel.
            if(manager != null){ //If the notification manger has any notification or not null.
                manager.createNotificationChannel(channel); //Creating the notification channel and passing that to the notificatio manager.
            }
        }

        Intent intent = new Intent(this, patients.class); //An intent current page to patient's.class java page, means which activity has to open.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //Adding flags to the intent, the flags instruct the android system how to display them.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); //Creating pending intent, this calls the activity later. The activity initialized by the intent is call later in the Pending Intent.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID) //NotificationCompat Builder for creating and customizing the notifications.
                .setContentTitle(title) //Setting the title
                .setContentText(body) //Setting the body
                .setSmallIcon(R.drawable.caduceus_logo_red) //Setting the icon
                .setColor(Color.RED) //Setting the background color of the image
                .setAutoCancel(true) //User can remove or display the notificaiton by single tap
                .setPriority(NotificationCompat.PRIORITY_HIGH) //Setting notification priority as high
                .setContentIntent(pendingIntent); //Setting the pending intent activity.
//               .addAction(0, "Click to open", pendingIntent); // adding an action button, clicking on it, it will open.
        int notificationId = (int) System.currentTimeMillis(); //A normal notification id or unique code by that every system contains
        if(manager != null){ //Checking the notification manager is null or not
            manager.notify(notificationId, builder.build()); //If the manager is not null then it will create the notification and notify the user.
        }
    }

    public void patientsButton(){
        patientButton.setOnClickListener(v -> {

            Intent intent = new Intent(ListViews.this, patients.class);
            intent.putExtra("sKey", key);
            intent.putExtra("iCompanyId", companyId);
            startActivity(intent);
        });
    }
}
