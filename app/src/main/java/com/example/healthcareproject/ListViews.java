package com.example.healthcareproject;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ListViews extends AppCompatActivity {

    List<ListViewDashboard> listViewDashboardList = new ArrayList<>();
    List<MenuModelClass> flatMenuList = new ArrayList<>();

    List<MenuModelClass> topLevelItems = new ArrayList<>();
    DrawerLayout drawerLayout;

    Toolbar toolbar;

    TextView toolbarTile;

//    TextView cancelButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewdashboard);
        //boolean fromDashboard = getIntent().getBooleanExtra("fromDashboard", false);
//        cancelButton = findViewById(R.id.cancelButton);
        toolbar = findViewById(R.id.toolBar); //ToolBar for the header
        toolbarTile = findViewById(R.id.toolBarTitle);
        setSupportActionBar(toolbar); //Making the toolbar for supporting the Action Bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Menu Items");
        drawerLayout = findViewById(R.id.drawerLayout);  //A drawer layout for handling the toolbar, listviews and an expandable list.

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

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

        ExpandableListView expandableListView =  findViewById(R.id.menuExpand); //An expandable list

        ListView listView = findViewById(R.id.list_dashboard); // Accessing the ListView using the id.

            ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle1);
            toggle1.syncState();


//        try {
//            JSONObject outerObj = new JSONObject(loadFromAsset()); //Accessing the outer JSON Object.
//            JSONArray items = outerObj.getJSONArray("d"); //Array in the JSON file.
//
//            for(int i = 0; i < items.length();i++){ //Looping through all the JSON Objects.
//                JSONObject innerObj = items.getJSONObject(i);
//                listViewDashboardList.add(parseDashboardItem(innerObj)); //Creating object for the ListViewDashboard Class
//            }
//        } catch (JSONException e) { //Catching the exception
//            e.printStackTrace();
//        }

        ExpandableMenuAdapter menuAdapter = new ExpandableMenuAdapter(topLevelItems); //An adapter for the expandable list, it contains top level items.
        expandableListView.setAdapter(menuAdapter);

        CustomListAdapter customListAdapter = new CustomListAdapter(this,listViewDashboardList); //A custom adapter for listViewDashboardList.
        listView.setAdapter(customListAdapter);
//
//        EditText etPurposeOfVisit = findViewById(R.id.etPurposeOfVisit);
//        EditText etVisitCategory = findViewById(R.id.etVisitCategory);
//        EditText etPatientName = findViewById(R.id.etPatientName);
//        Switch switchFollowup = findViewById(R.id.switchFollowup);
//        Button btnSearch = findViewById(R.id.btnSearch);
//
//        btnSearch.setOnClickListener(v -> {
//                    int purposeOfVisit = parseIntOrDefault(etPurposeOfVisit.getText().toString(), 0);
//                    int visitCategory = parseIntOrDefault(etVisitCategory.getText().toString(), 0);
//                    String patientName = etPatientName.getText().toString().trim();
//                    boolean followup = switchFollowup.isChecked();

                    String key = getIntent().getStringExtra("key");
                    String companyId = getIntent().getStringExtra("companyId");
                    long visitCategory = getIntent().getLongExtra("VisitTypeId", 0);
                    long purposeOfVisit = getIntent().getLongExtra("PurposeOfVisit_Id", 0);
                    Boolean followUp = getIntent().getBooleanExtra("followup_YN", false);

                    String searchPatientName = getIntent().getStringExtra("patient_name");
                    if(searchPatientName == null){
                        searchPatientName ="";
                    }

                    if (key != null && !key.isEmpty() && companyId != null && !companyId.isEmpty()) {
                        Log.d("Dashboard", "Received Key: " + key);
                        Log.d("Dashboard", "Received Company ID: " +companyId);
                        Log.d("Dashboard", "VisitCategory ID: " + visitCategory);
                        Log.d("Dashboard", "Purpose Of visit: "+purposeOfVisit);
                        Log.d("Dashboard", "FollowUp: " + followUp);

                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Util.asmx/getSMDashboardCompanyVisitsPatientNameList";

                        JSONObject jsonObject = new JSONObject();


                        try {
                            jsonObject.put("sKey", key);
                            jsonObject.put("iCompanyId", companyId);
                            jsonObject.put("sPatientName", searchPatientName);
                            jsonObject.put("iVisitCatergory", visitCategory);
                            jsonObject.put("bFollowup", followUp);
                            jsonObject.put("iPurposeOfVisit", purposeOfVisit);
                        }catch (JSONException e){
                            Log.e("Dashboard", "Failed to build getUser request body", e);
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
                                        customListAdapter.notifyDataSetChanged();
                                        fetchUser(key, customListAdapter);
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
                                    fetchUser(key, customListAdapter);
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
                    } else {
                        Log.e("Dashboard", "Key or CompanyId is missing");
                    }

        parseMenuItems(); // For reading the JSON file.
        buildMenuItem(); // For building the menu items like a tree by separating the parent and child menus.
        sortMenuItems(); //Sorting the menu based upon to the sort_order key.


        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> { //A parent group listener when clicking on it will show child menus.
            MenuModelClass group = topLevelItems.get(groupPosition);

            if("item".equals(group.getType())){ //If the key type is item then it will redirect to the openContentActivity() method. If not then it will return false.
                openContentActivity(group);
                return true;
            }
            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> { //A child group Listener runs whenever a child is clicked.
            MenuModelClass child = topLevelItems.get(groupPosition).getChildren().get(childPosition);
            openContentActivity(child);
            return true;
        });

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Logout");
            alertDialogBuilder.setIcon(R.drawable.img_16);
            alertDialogBuilder.setMessage("Are you sure, you want to Logout");
            alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

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

    private void fetchUser(String key, CustomListAdapter adapter){
        String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/getUser";
        Map<String, String> params = new HashMap<>();
        params.put("sKey", key);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("Dashboard", "Response: " + response.toString());

                    try {
                        if(response.has("d")){
                            Object value = response.get("d");

                            if(value instanceof  JSONArray){
                                JSONArray array = (JSONArray) value;
                                for(int i = 0; i < array.length(); i++){
                                    listViewDashboardList.add(parseDashboardItem(array.getJSONObject(i)));
                                }
                            }
                            else if(value instanceof JSONObject){
                                JSONObject userObj = (JSONObject) value;
                                listViewDashboardList.add(parseDashboardItem(userObj));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        Log.e("Dashboard", "Failed to parse response", e);
                    }
                },
                volleyError -> {
                    String errorMsg = volleyError.getMessage();
                    if (volleyError.networkResponse != null) {
                        errorMsg = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.e("Dashboard", "Network error: " + errorMsg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

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
}
