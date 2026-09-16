package com.example.healthcareproject.ui.patients;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareproject.BuildConfig;
import com.example.healthcareproject.ui.backbutton.BaseActivity1;
import com.example.healthcareproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientsActivity extends BaseActivity1 {

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

    FormItem formItem;
    String key;
    String companyId;
    FormAdapter formAdapter;
    public static final String BASE_URL = BuildConfig.API_BASE_URL;
    RequestQueue requestQueue;

    public static final String TAG = "CancelTAG";
    private String selectedValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientslist);
        recyclerView = findViewById(R.id.patientRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //This tells the recycler view how to arrange the data.

        formAdapter = new FormAdapter(new ArrayList<>()); //An adapter
        recyclerView.setAdapter(formAdapter);

        requestQueue = Volley.newRequestQueue(this);

        items = new BuildFormItems().buildFormItems(formAdapter::notifyDataSetChanged);
        formAdapter.setItems(items);

        if(getIntent() != null) {
            key = getIntent().getStringExtra("sKey"); //Getting the sKey from the previous page ListViews.java
            companyId = getIntent().getStringExtra("iCompanyId");
        }

        loadVisitTypeData(); //A visit type drop down method
        loadCountriesData(selectedValue);
        loadLanguagesData(null);
        loadStatesInACountry(null);

        Button visitButton = findViewById(R.id.visitButton); //A visit button
        visitButton.setOnClickListener(v -> { //By clicking on to the visit button it is open the details
            boolean isValid = true; //creating a boolean varibale with value 1
            for(FormItem item : items){ //Looping the items list with the formItem class object.
                if(item.isRequired()){ //checking whether the formitem object is required or not.if it is true then
                    if(item.getValue() == null || item.getValue().isEmpty()){ //checking whether the formitem object getting the value or it is empty or null.
                        item.setError(true); //If the form is not getting the value then there is an error.
                        isValid = false; //So the value of the isValid is set to false.
                    }
                    else{
                        item.setError(false); //or else if the value of the item is not null or empty then it means there are no errors. so it became false.
                    }
                }
            }
            formAdapter.notifyDataSetChanged(); //Notifying the adapter that the data is changed.

            if(isValid){ //if the isValid value true it doesn't contain any error
                Toast.makeText(this,"Start Visit Clicked", Toast.LENGTH_SHORT).show(); //Then the visit button is clicked toast will be visible in the UI
            }else { //If the isValid variable is set to false means contains any error or empty value.
                Toast.makeText(this, "Please Fill the required details", Toast.LENGTH_SHORT).show(); //Then the toast is visible as like this.
            }
        });

        TextView cancelButton = findViewById(R.id.cancelButton); //A cancel button by clicking on it, we will go to the previous page.
        cancelButton.setOnClickListener(v -> {
         finish();
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //Checking the android SDK version is compatible with the version 13 or newer versions.
            if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){ //If it is compatible with the newer versions then checking that the Manifest has post notification permission or not equal to the package manager is granted the permission or not.
                requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS}, 101); //If the user doesn't allow the permission then we will request for the permission.
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null){
            requestQueue.cancelAll(TAG);
        }
    }

        private boolean hasAnyValue(List<FormItem> items){ //
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

    public void loadVisitTypeData(){ //Visit type method for calling the web method.
        if(key == null){ //Checking the key is null or empty or not.
            Log.e("Patients Page", "Cannot trigger web method: Missing Authentication Credential.");
            return;
        }
        Log.d("VisitType", "Key is working: " +key); //If the key is not null then in the logcat we will check the value.

        String url = BASE_URL + "ws_webrtc/Telemed.asmx/GetVisitTypes"; //A webmethod url

        JSONObject object = new JSONObject(); //Creating the json object
        try {
            object.put("sKey", key); //Parsing the key as the parameter to the web method.
        }catch(JSONException e){
            Log.e("VisitType", "Failed to build the request body", e);
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( //A JSONObject Request for to create the json request and parsing the method, url and the object.
                Request.Method.POST, //For posting the web method
                url,
                object,
                response -> {
                    Log.d("VisitType", "Visit Array: " + response.toString()); //If the web method has any data we can see that in logcat as a visit array

                    try {
                        JSONArray array = response.getJSONArray("d"); //A JSONArray with the JSONObject "d".
                        ArrayList<String> visitArray = new ArrayList<>(); //Creating an array list of type string with the name visit array.
                        ArrayList<Integer> visitTypeId = new ArrayList<>();

                        visitArray.add("Select");
                        visitTypeId.add(0);

                        for (int i = 0; i < array.length(); i++) { //Looping the i with the array length.
                            JSONObject jsonObject = array.getJSONObject(i); //JSONObjects inside the array.
                            String visit = jsonObject.getString("VisitType"); //Creating a varaible with the name visit of type string, using the inner json object we are getting the string called "VisitType" from the webmethod.
                            int id = jsonObject.getInt("VisitType_Id"); //getting the VisitType_Id from the web method and assigning the value with the id.

                            Log.d("VisitType", "Visit Id's : " + id);

                            visitArray.add(visit);
                            visitTypeId.add(id);
                        }

//                        loadPurposeOfVisitData(id); //Passing the id value into the dropDownPurposeOfVisit method.

                        for(FormItem item: items){ //Looping the FormItems List with the formItem object item.
                            if("Visit Type".equals(item.getLabel())){ //Checking whether the type of label is visit type or not.
                                item.setOptions(visitArray); //If it is the visit type then the formitem object sets the visitArray values into the setOptions list.
                                item.setOptions2(visitTypeId);
                                break;
                            }
                        }

                        formAdapter.notifyDataSetChanged(); //Notifying the adapter that the data set changed.
                    } catch (JSONException e) {
                        Log.e("VisitType", "Failed to parse response", e);
                    }
                },
                volleyError ->  {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null){
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.e("VisitType", "Network error: " + error);
                }
        ){
            @Override
            public Map<String, String> getHeaders() { //Setting the content type as application json and charset value as utf-8.
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setTag(TAG);
//        Volley.newRequestQueue(this).add(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest); //adding the JSONObjectRequest with the requestQueue.
    }
    public void loadPurposeOfVisitData(ArrayList<Integer> visitList, int position){
        if(key == null){
            Log.e("PurposeOfVisit", "Cannot load the data, key is missing");
            return;
        }

        if (visitList == null || position < 0 || position >= visitList.size()) {
            Log.e("PurposeOfVisit", "Invalid visitList or position: " + position);
            return;
        }

        String url = BASE_URL + "ws_webrtc/Util.asmx/TelemedGlobalUserVisitsPurposesGet";

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("sKey", key);
            jsonObject.put("iVisitTypeID", visitList.get(position));
        }catch (JSONException e){
            Log.e("PurposeOfVisit", "Failed to load the purpose of Visit", e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("PurposeOfVisit", "Purpose of Visit Array: " + response.toString());
                    try {
                        JSONArray array = response.getJSONArray("d");
                        ArrayList<String> purposeArray = new ArrayList<>();
                        purposeArray.add("Select");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            String purposeVisit = jsonObject1.getString("PurposeOfVisit");
                            purposeArray.add(purposeVisit);
                        }

                        for(FormItem item : items){
                            if("Purpose of Visit".equals(item.getLabel())){
                                item.setOptions(purposeArray);
                                break;
                            }
                        }

                        formAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        Log.e("PurposeOfVisit", "Failed to load the purpose of visit data", e);

                    }
                },
                volleyError -> {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null){
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.e("PurposeOfVisit", "Network error: "+ error);
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        request.setTag(TAG);
        requestQueue.add(request);
    }

    public void loadCountriesData(String selectedValue){
        if(key == null){
            Log.e("Countries", "Cannot load the data key is missing");
            return;
        }
        String url = BASE_URL + "ws_webrtc/Telemed.asmx/GetCountries";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sKey", key);
        }catch (JSONException e){
            Log.e("Countries", "Failed to load the data");
            return;
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("Countries", "Country Array : " + response.toString());
                    try{
                        JSONArray array = response.getJSONArray("d");
                        ArrayList<String> countriesArray = new ArrayList<>();
                        String country = "";
                        for(int i = 0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            country = object.getString("Country_Name");
                            countriesArray.add(country);
                        }
//                        if(!country.isEmpty()) {
//
//                            loadLanguagesData(country);
//                            loadStatesInACountry(country);
//                        }

                        for(FormItem item : items){
                            if("Country".equals(item.getLabel())){
                                item.setOptions(countriesArray);
                                break;
                            }
                        }
                        formAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        Log.e("Countries", "Failed to load the Countries data", e);
                    }
                },
                volleyError -> {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null){
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.e("Countries", "Network error: " + error);
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json;charset=utf-8");
                return headers;
            }
        };
        objectRequest.setTag(TAG);

        requestQueue.add(objectRequest);
    }

//    public void dropDownCompanyBranch(){
//        if (key == null) {
//            Log.e("Branch", "Cannot load the data, key is missing");
//            return;
//        }
//
//        String url = "http://161.129.91.101:90/Telemed/ws_webrtc/Telemed.asmx/getTelemedCompanyBranchesPerCompany";
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("sKey", key);
//            jsonObject.put("iCompanyId", companyId);
//        }catch (JSONException e){
//            Log.e("Branch", "Failed to load the data", e);
//            return;
//        }
//
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                jsonObject,
//                response -> {
//                    Log.d("Branch", "Branch Array : " + response.toString());
//                    try {
//                        JSONArray array = response.getJSONArray("d");
//                        ArrayList<Boolean> branchArray = new ArrayList<>();
//                        boolean branch = false;
//                        for(int i = 0; i < array.length(); i++){
//                            JSONObject innerObj = array.getJSONObject(i);
//                            branch = innerObj.getBoolean("ParentForceBranch");
//                            branchArray.add(branch);
//                        }
//
//                        for (FormItem item: items){
//                            if("Branch".equals(item.getLabel())){
//                                item.setOptions(branchArray);
//                                break;
//                            }
//                        }
//                    }catch(JSONException e){
//                        Log.e("Branch", "Failed to load branch data", e);
//                    }
//                },
//                volleyError -> {
//                    String error = volleyError.getMessage();
//                    if(volleyError.networkResponse != null){
//                        error = "Status code: " + volleyError.networkResponse.statusCode;
//                    }
//                    Log.e("Branch", "Network Error: " + error);
//                }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        request.setTag(TAG);
//        requestQueue.add(request);
//    }

    public void loadLanguagesData(String countryName){
        if(key == null ){
            Log.e("Language", "Failed to fetch the key value.");
            return;
        }
        String url = BASE_URL + "ws_webrtc/Util.asmx/populateDDL";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sKey", key);
            jsonObject.put("sTableName", "tbl");
        }catch (JSONException e){
            Log.e("Language", "Failed to load the data", e);
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("Language", "Language Array is: " + response.toString());

                    try{
                        JSONArray jsonArray = response.getJSONArray("d");
                        ArrayList<String> languageArray = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String languageName = object.getString("languageName");
                            languageArray.add(languageName);
                        }
                        for(FormItem item : items){
                            if("Language".equals(item.getLabel())){
                                item.setOptions(languageArray);
                            }
                        }
                        formAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        Log.d("Language", "Failed to load the data in the Language method", e);
                    }

                },
                volleyError -> {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null){
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.d("Language", "Network error : " + error);
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setTag(TAG);

        requestQueue.add(jsonObjectRequest);
    }

    public void loadStatesInACountry(String countryName){
        if(key == null ){
            Log.e("States", "Failed to fetch the key value.");
            return;
        }
        String url = BASE_URL + "ws_webrtc/Util.asmx/populateDDL";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sKey", key);
            jsonObject.put("sTableName", "tbl");
        }catch (JSONException e){
            Log.e("States", "Failed to load the data", e);
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    Log.d("States", "State Array is: " + response.toString());

                    try{
                        JSONArray jsonArray = response.getJSONArray("d");
                        ArrayList<String> stateArray = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String stateName = object.getString("stateName");
                            stateArray.add(stateName);
                        }
                        for(FormItem item : items){
                            if("State".equals(item.getLabel())){
                                item.setOptions(stateArray);
                            }
                        }
                        formAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        Log.d("State", "Failed to load the data in the Language method", e);
                    }

                },
                volleyError -> {
                    String error = volleyError.getMessage();
                    if(volleyError.networkResponse != null){
                        error = "Status Code: " + volleyError.networkResponse.statusCode;
                    }
                    Log.d("State", "Network error : " + error);
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setTag(TAG);

        requestQueue.add(jsonObjectRequest);
    }

    public void selectedCountry(boolean isUS){
        for(int i = 0; i < items.size(); i++){
            FormItem item = items.get(i);
            if("State".equals(item.getLabel()) || "Zip".equals(item.getLabel()) || "City".equals(item.getLabel()) || "Address".equals(item.getLabel()) || "Address 2".equals(item.getLabel())){
                item.setEnabled(isUS);
                formAdapter.notifyDataSetChanged();
            }
        }
    }

}
