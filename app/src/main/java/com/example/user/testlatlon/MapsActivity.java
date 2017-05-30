package com.example.user.testlatlon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    Geocoder geocoder;
    List<Address> addresses;
    double zoomLevel = 10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.e("status : ", "Recieved !");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(23.026473, 72.57219, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
/*--------------------------------------------------------------------------------------*/
        /*String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*/
        String premises = addresses.get(0).getPremises();
        String phone = addresses.get(0).getPhone();
        String featureName = addresses.get(0).getFeatureName();
        Bundle extras = addresses.get(0).getExtras();
        //To-do extra : getUrl for restaurant website
        Log.e("#", "#########################################################" + "\n");
        Log.e("premises : ", premises + "\n");
        Log.e("phone : ", phone + "\n");
        Log.e("featureName : ", featureName + "\n");
        Log.e("extras : ", extras + "\n");
        Log.e("#", "#########################################################" + "\n");
        //Log.e("Address : ",address + "\n"+city+ "\n"+state+ "\n"+country+ "\n"+postalCode+ "\n"+knownName);
/*--------------------------------------------------------------------------------------*/
        ArrayList<String> latiList = getIntent().getStringArrayListExtra("latiList");
        ArrayList<String> longList = getIntent().getStringArrayListExtra("longList");
        ArrayList<String> nameList = getIntent().getStringArrayListExtra("nameList");
        ArrayList<String> ratingList = getIntent().getStringArrayListExtra("ratingList");
        ArrayList<String> vicinityList = getIntent().getStringArrayListExtra("vicinityList");

        String pos = getIntent().getStringExtra("selectedCity");
        LatLng[] items = new LatLng[latiList.size()];
        // Add a marker in Sydney and move the camera
        LatLng ahmedabad = new LatLng(23.0225, 72.5714);
        LatLng vadodara = new LatLng(22.3072, 73.1812);
        for (int i = 0; i < latiList.size(); i++) {
            items[i] = new LatLng(Double.valueOf(latiList.get(i)), Double.valueOf(longList.get(i)));
        }
        for (int i = 0; i < latiList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(items[i]).title(nameList.get(i))).setSnippet("Address : " + vicinityList.get(i)+"\nRating : " +ratingList.get(i) +" / 5");
        }
        if (Integer.valueOf(pos) == 0) {
            Marker mahmedabad = mMap.addMarker(new MarkerOptions().position(ahmedabad).title("Welcome to Ahmedabad !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mahmedabad.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ahmedabad));
        }
        if (Integer.valueOf(pos) == 1) {
            Marker mvadodara = mMap.addMarker(new MarkerOptions().position(vadodara).title("Welcome to Vadodara !").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mvadodara.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vadodara));
        }
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.setOnInfoWindowClickListener(MapsActivity.this);


        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Welcome to Rajwadu !"))
                {
                    final Dialog dialog = new Dialog(MapsActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    dialog.setTitle("Rajwadu");
                    ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
                    ivIcon.setImageResource(R.drawable.rajwadu);
                    dialog.setCancelable(true);
                    dialog.show();
                }
                if (marker.getTitle().equals("Welcome to Gordhan Thal !"))
                {
                    final Dialog dialog = new Dialog(MapsActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    dialog.setTitle("Gordhan Thal !");
                    ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
                    ivIcon.setImageResource(R.drawable.gordhan);
                    dialog.setCancelable(true);
                    dialog.show();
                }
                if (marker.getTitle().equals("Welcome to Rasna !"))
                {
                    final Dialog dialog = new Dialog(MapsActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    dialog.setTitle("Rasna");
                    ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
                    ivIcon.setImageResource(R.drawable.rasna);
                    dialog.setCancelable(true);
                    dialog.show();
                }

                return true;
            }
        });*/
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.custom_dialog);

        TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) dialog.findViewById(R.id.tvAddress);


        tvName.setText(marker.getTitle().toString());
        tvAddress.setText(marker.getSnippet().toString());

        dialog.setCancelable(true);
        dialog.show();
    }
}
