package com.rohit.techniche.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rohit.techniche.R;
import com.rohit.techniche.adapter.TabsPagerAdapter;
import com.rohit.techniche.bean.CategoryItem;
import com.rohit.techniche.parser.TechicheJSONParser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView locationReloadImageView;
    TextView locationTextView;

    public static final int MY_PERMISSION_REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude,longitude;
    private Location mlocation;

    private String currentCity = "";

    private static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String uri = "";

    private List<CategoryItem> categoryItemList;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        View mActionBarView = getLayoutInflater().inflate(R.layout.location_action_bar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        locationTextView = (TextView) mActionBarView.findViewById(R.id.locationTextView);
        locationReloadImageView = (ImageView) mActionBarView.findViewById(R.id.locationReloadImageView);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        uri = getString(R.string.uri);
        System.out.println("uri "+uri);

        locationReloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingLocationParameters();
            }
        });

        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Retreiving location ...");

        //requestData(uri);

//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
//
//        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
//
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);



        settingLocationParameters();



    }

    public void checkIfGPSIsEnabled(){
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }else{
            buildingLocationListener();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void addTabsDynamically(List<CategoryItem> categoryItemList){
        for(int i=0; i < categoryItemList.size(); i++){
            CategoryItem categoryItem = categoryItemList.get(i);
            tabLayout.addTab(tabLayout.newTab().setText(categoryItem.getName()).setTag(categoryItem.get_id()));
        }
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(),categoryItemList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void requestData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        categoryItemList = TechicheJSONParser.parseData(response,currentCity);
                        System.out.println("categoryItemList --- "+categoryItemList);
                        addTabsDynamically(categoryItemList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void settingLocationParameters(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkIfGPSIsEnabled();
    }

    public void buildingLocationListener(){
        progressBar.show();//displays the progress bar

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mlocation = location;
                Toast.makeText(MainActivity.this,"lat "+location.getLatitude()+" lon "+location.getLongitude(),Toast.LENGTH_LONG).show();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getAddress();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        checkLocationPermission();
    }

    public void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
                return;
            }else{
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    }

                }else{
                    Toast.makeText(MainActivity.this,"Permission denied !",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mlocation != null){
            System.out.println("lat "+mlocation.getLatitude()+" lon "+mlocation.getLongitude());
        }else{
            System.out.println("mlocation is null");
            settingLocationParameters();
        }

    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void getAddress()
    {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            currentCity = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

//            String currentLocation;
//
//            if(!TextUtils.isEmpty(address))
//            {
//                currentLocation=address;
//
//                if (!TextUtils.isEmpty(address1))
//                    currentLocation+="\n"+address1;
//
//                if (!TextUtils.isEmpty(city))
//                {
//                    currentLocation+="\n"+city;
//
//                    if (!TextUtils.isEmpty(postalCode))
//                        currentLocation+=" - "+postalCode;
//                }
//                else
//                {
//                    if (!TextUtils.isEmpty(postalCode))
//                        currentLocation+="\n"+postalCode;
//                }
//
//                if (!TextUtils.isEmpty(state))
//                    currentLocation+="\n"+state;
//
//                if (!TextUtils.isEmpty(country))
//                    currentLocation+="\n"+country;
//
//                Log.i(TAG,"currentLocation " +currentLocation);
//
////                tvEmpty.setVisibility(View.GONE);
////                tvAddress.setText(currentLocation);
////                tvAddress.setVisibility(View.VISIBLE);
////
////                if(!btnProceed.isEnabled())
////                    btnProceed.setEnabled(true);
//
//
//            }
            progressBar.dismiss();
            if(!currentCity.isEmpty() && !currentCity.equals("")){
                locationTextView.setText(currentCity);
                requestData(uri);
            }



        }

    }

}
