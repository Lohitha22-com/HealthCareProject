//package com.example.healthcareproject;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class RecyclarDashboard extends AppCompatActivity {
//
//    //Creating a class level list to hold data model objects on recyclerview dashboard
//    List<RecycleViewDashboard> recycleViewDashboardList = new ArrayList<>();
//
//    // A method from the parent class which manages the UI
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recycledashboard);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);  //finding the recycler view layout by its ID.
//
//        //A linear layout that tells the recycler view how to arrange the data. Returning the app context
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        //Assigning a Layout manager to the recyclerview so it knows how to arrange the data.
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //Creating a vertical divider for understanding the objects.
//        // DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
//        // divider.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(),R.drawable.divider));
//        //For creating a shape used to render the divider line.
//        // recyclerView.addItemDecoration(divider);//attaching the divider to the recyclerview.
//
//
//        //For handling exception
//        try{
//            JSONObject outerObj = new JSONObject(loadFormAsset());//Creating the object name for JSON objects, which was there in the asset's folder.
//
//            JSONArray items = outerObj.getJSONArray("d"); //Creating the object name for the JSON Array in the asset folder which is already named as "d".
//
//            //Looping inner objects to get the keys
//            for(int i = 0; i<items.length();i++){
//                JSONObject data = items.getJSONObject(i);
//                RecycleViewDashboard recycleViewDashboard = new RecycleViewDashboard();
//
//                //Iterator is used to loop over the collection of objects of string type.
//                //data is a JSON object,
//                // .keys() is a JSON Object method to access the keys not values.
//                Iterator<String> keys= data.keys();
//
//                //Looping the keys by checking if there are any keys or elements.
//                while(keys.hasNext()){
//                    //if there are any next keys in the objects it will store in the key.
//                    String key = keys.next(); // a method from the iterator.
//                    String value = String.valueOf(data.get(key)); //to access the keys and values and converts them into the string.
//
//                    if(key.equals("first_name")){
//                        recycleViewDashboard.setFirstName(value);
//                    }
//                    else if(key.equals("last_name")){
//                        recycleViewDashboard.setLastName(value);
//                    }
//                    else if(key.equals("Visit_Completed")){
//                        recycleViewDashboard.setVisit_Completed(value);
//                    }
//                    else if(key.equals("company_name")){
//                        recycleViewDashboard.setCompany_name(value);
//                    }
//                    else if(key.equals("Followup_Count")){
//                        recycleViewDashboard.setFollowUpCount(Integer.parseInt(value));
//                    }
//                    else if(key.equals("Language_Name")){
//                        recycleViewDashboard.setLanguageName(value);
//                    }
//                    else{
//                        recycleViewDashboard.addData(key, value);// adding the key and values to the recyclerview-dashboard.
//                    }
//                }
//
//                recycleViewDashboardList.add(recycleViewDashboard);
//                //appending the data to the list
//            }
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        CustomAdapter customAdapter = new CustomAdapter(this,recycleViewDashboardList);  //Creating a custom adapter and passing list
//        recyclerView.setAdapter(customAdapter); //attaching the adapter to the recyclerview.
//    }
//
//    //Creating a method to access the file in the asset folder.
//    public String loadFormAsset(){
//        String json = null; //Declaring an object of string type. assigning null means there is no object is there initially.
//        try{ //handling exception
//            InputStream is = getAssets().open("data.json");
//            //An input stream is used to read the data in terms of bytes.
//            //getAssets() method taking the files and folders in which asset holds the "data.json" file.
//            int size = is.available(); // available() is a InputStream method which returns how many bytes can take to read the input stream data without blocking the data.
//            byte[] bytes= new byte[size];//creating a byte array which is equal to the length of the size.
//            is.read(bytes); //which is a method from the I/O stream that reads bytes from the stream.
//            is.close();//closing the stream
//            json = new String(bytes, "UTF-8"); // a string constructor which takes a byte array and character encoding to convert into a string.
//            //Now json actually holds the data.json file instead of null.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }
//}
