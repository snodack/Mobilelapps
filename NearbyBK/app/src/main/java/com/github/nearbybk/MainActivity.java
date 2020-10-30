package com.github.nearbybk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        supportMapFragment.getMapAsync(this);

       btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location=" + currentLat + ","
                        + currentLong + "&radius=5000" + "&type=" + "restaurant" + "&keyword=Burger King" + "&name" + "Burger+King" +
                         "&key=" + getResources().getString(R.string.google_map_key);
                //String url  = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.503186,-0.126446&radius=5000&types=hospital&key=" + getResources().getString(R.string.google_map_key);

                new PlaceTask().execute(url);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
    	    private void getCurrentLocation() {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                      // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            44);
           	            // here to request the missing permissions, and then overriding
            	            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            	            //                                          int[] grantResults)
            	            // to handle the case where the user grants the permission. See the documentation
            	            // for ActivityCompat#requestPermissions for more details.
            	            return;
            	        }
        	        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        	        task.addOnSuccessListener(new OnSuccessListener<Location>() {
	            @Override
	            public void onSuccess(Location location) {                if (location != null)
                    	                {
                                       currentLat = location.getLatitude();
                                       currentLong = location.getLongitude();
                                       supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                           	                            map = googleMap;
                           	                            map.getUiSettings().setMyLocationButtonEnabled(true);
                           	                            map.getUiSettings().setZoomControlsEnabled(true);
                           	                            map.setMyLocationEnabled(true);

                           	                           /* LatLng myPosition =  new LatLng(currentLat, currentLong);
                                                        googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
                                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                                       myPosition, 15));*/
                        }});
                    	                }
	            }
        }); }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url =  new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream steam = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(steam));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine())!= null){
            builder.append(line);
        }
        String data = builder.toString();
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //JSON PARSER
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();
            for(int i = 0; i <hashMaps.size(); i++){
                HashMap<String, String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name = hashMapList.get("name");

                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                map.addMarker(options);

            }
        }
    }
}