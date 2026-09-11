package com.example.healthcareproject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.healthcareproject.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends BaseActivity1 implements OnMapReadyCallback {

    private GoogleMap mMap; //Object for Google Maps
    FusedLocationProviderClient providerClient; //Prevents to stop the maps has not been freez, and helps to retrieve the devices location.
    Location currentLocation; //Getting current location
    SearchView searchView; //SearchView
    int FINE_PERMISSION_CODE = 1;
    private Marker searchMarker; //Marker object
    ImageButton imageButton;
    private ActivityMapsBinding binding; //View Binding for accessing the activity.xml without using the ID.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        EdgeToEdge.enable(this); //Enabling edge to edge layout for the app.
//
//     binding = ActivityMapsBinding.inflate(getLayoutInflater()); //Creating a View Binding, in normal for setting an activity_XML files we use findViewById(), where as ViewBinding automatically generates a binding class for the XML file.It creates instance of a binding class so you can easily reference your UI.
//     setContentView(binding.getRoot());//It retrieves the topmost view of the layout XML file.

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); //It goes through the XML layout and find the specific fragment element designed for holding the map and casts it into a SupportMapFragment, which is required to manage a Google Maps.

        Toolbar toolbar = findViewById(R.id.myToolbar); // A toolbar with the name MyGoogleMaps
        setSupportActionBar(toolbar); //Setting the toolbar

        providerClient = LocationServices.getFusedLocationProviderClient(this); // The location services class is used to access the particular API service for a task which was given by the getFusedLocationProviderClient.
        getLastLocation();// Calling the getLastLocation() method.

        if(getSupportActionBar() != null){ //To gain the support from the action bar
            getSupportActionBar().setTitle("");//Setting the title to the action bar
        }

        imageButton = findViewById(R.id.backArrow); //A backArrow array button, to go back to the listviews.java
        imageButton.setOnClickListener(v -> { //By clicking on to the button it is going back to the normal listview.java
            finish();
        });

        searchView = findViewById(R.id.search_bar); //A searchbar for searching the location.

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //Calling the searchView onQueryListener method().
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            } //typing the searchbar it will navigateto the location

            @Override
            public boolean onQueryTextSubmit(String newText) { //After entering the address by clicking on the search or submit then only the entered address will search.
                String location = searchView.getQuery().toString(); //Creating a variable location for the searchView to get the user entered address as a query and convert that into string even the numbers.

                if(location != null && !location.trim().isEmpty()){ //Checking that if the location is null or empty and at the end or starting removing the spaces.
                    Geocoder geocoder = new Geocoder(MapsActivity.this); //Creating Geocoder class object
                    List<Address> addressList = null; //Creating a list with the class address and making it as null
                    try {
                        addressList = geocoder.getFromLocationName(location, 1); //Accessing the name of the location from the method call getFromLocationName and making the possible number of searches value set to 1.
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    if(addressList != null && !addressList.isEmpty()) { //Checking if the generated addresslist is null or empty or not.
                        Address address = addressList.get(0); //getting the 1st index value from the address list, and storing it in the Address class object.
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude()); //Creating a latitude and longitude class object, and getting the address interms of latitude and longitude.

                        if(searchMarker != null){ //Checking if the entered address are marked in 2 to 3 places.
                            searchMarker.remove(); //If there are selected 2 or more than remove them.
                        }

                        searchMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Location")); //Initialzing the searchMarker with the add marker with the position and the title.
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));//setting the cameras zoom level.
                    }
                }
                return false;
            }
        });

        mapFragment.getMapAsync(this); //Loads the map in the background. After finishing, it loads the onMapReady method.
    }

    private void getLastLocation() { //A method which checks thus the user allowed permission to access the location or not.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        //Checking if the manifest has the access fine location method is there or if the user has granted the permission or not. And manifest has coarse location or the user granted the permission.
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE); //If the user has not given the permission then it will show a pop-up or text message as accept or deny the permission.
            return;
        }
        if(mMap != null){ //Checking the mMap is null or not.
            mMap.setMyLocationEnabled(true); // using that mMap object it is setting the location enabled or not.
        }

        Task<Location> task = providerClient.getLastLocation(); //Creating the Task for the location
        task.addOnSuccessListener(new OnSuccessListener<Location>() { //calling the task on success listener method to get the current location
            @Override
            public void onSuccess(Location location) { //If the method is success
                if(location != null){ //If the location is not null or not.
                    currentLocation = location; //If the location is not null then storing the location with the currentLocation
                    if(mMap != null){ //Checking whether the mMap is null or not
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); //Creating the latitude and longitude
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18)); //setting the camera
                    }
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) { //It will run automatically when the map is fully loaded and ready to use. It gives the googleMap object to control the map.
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //Setting the type of camera to normal.

        mMap = googleMap; //Saving the googleMap object into the variable mMap of type GoogleMap.

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){ //Again checking the self permission
            mMap.setMyLocationEnabled(true); //And setting the current location by making it enable.
            getLastLocation(); //calling the last location method.
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE); //Request for permission
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //For checking that the user granted permission to access their location.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){ //Checking the request code is equals to finer pemission code or not
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //If it is same means checking the grantresult value greater than 0 or not and grantResult value of the 1st index is equal to the permission granted or not.
                getLastLocation();//Calling the last location method
            }else {
                Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //getting the menu folder method for accessing the main menu file for creating the menu toolbar.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId(); //Creating the id for the items in the menu with the getItemId();

        if(mMap == null) return false;

        if(id == R.id.menuNone){ //Checking the type of menu we have clicked. does it match to any option in the code.
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            return true;
        }
        else if(id == R.id.menuNormal){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }
        else if(id == R.id.menuHybrid){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }
        else if(id == R.id.menuSatellite){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }
        else if(id == R.id.menuTerrain){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}