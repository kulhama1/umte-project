package com.example.martin.projectskola.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.projectskola.R;
import com.example.martin.projectskola.databaze.SQLiteHelper;
import com.example.martin.projectskola.gps.GPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap googleMaps;
    private Spinner typMapy;
    private GPS gps;
    private boolean podminka;
    double latitude, longitude;
    private List<String> description;
    private List<String> title;
    private List<String> cesty;
    private Location mLocation;
    private int typMapyList[] = {
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN
    };
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        description = new ArrayList<>();
        title = new ArrayList<>();
        cesty = new ArrayList<>();

        typMapy = (Spinner) findViewById(R.id.spiner_vyber_typu_mapy);
        typMapy.setOnItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sqLiteHelper = new SQLiteHelper(this, "PlacesDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PLACESCZECHREPUBLIC(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, description VARCHAR, latitude DOUBLE, longitude DOUBLE, city VARCHAR, cesta VARCHAR)");

        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps = googleMap;

        GPS gps = new GPS(getApplicationContext());
        mLocation = gps.getLocation();


        try{
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }
        catch (NullPointerException e){
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("GPS nenalezena")
                    .setMessage("Zapněte na mobilním zařízení GPS a Internet, jinak nebude aplikace fungovat správně!")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }


        LatLng sydney = new LatLng(latitude, longitude);
        googleMaps.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

        googleMaps.addMarker(new MarkerOptions().position(sydney)
                .title("12345678987654321")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM PLACESCZECHREPUBLIC");
        int index = 0;
        title.clear();
        description.clear();
        cesty.clear();
        while (cursor.moveToNext()) {

            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            String titleText = cursor.getString(1);
            String descriptionText = cursor.getString(2);
            String cesta = cursor.getString(6);
            title.add(index,titleText);
            description.add(index,descriptionText);
            cesty.add(index, cesta);

            LatLng place = new LatLng(latitude, longitude);

            googleMaps.addMarker(new MarkerOptions().position(place)
                    .title(String.valueOf(index))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            index++;
           googleMaps.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
               @Override
               public void onInfoWindowClick(Marker marker) {

                      if (podminka == true) {
                          Intent i = new Intent(getApplicationContext(), SinglePlaceActivity.class);
                          i.putExtra("position", Integer.parseInt((marker.getTitle()).toString()));
                          startActivity(i);
                      }
               }
           });
        }
        if (googleMaps != null){
            googleMaps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.marker_info, null);
                    ImageView imageViewMarkerInfo = (ImageView) v.findViewById(R.id.imageViewMarkerInfo);
                    TextView textViewTitleMarkerInfo = (TextView) v.findViewById(R.id.textViewTitleMarkerInfo);
                    TextView textViewDescriptionMarkerInfo = (TextView) v.findViewById(R.id.textViewDescriptionMarkerInfo);

                    Log.e("marker", marker.getTitle().toString());
                    String cislo = marker.getTitle().toString();
                    if(cislo.equals("12345678987654321")){
                        textViewDescriptionMarkerInfo.setText("Zde se nacházíte");
                        textViewTitleMarkerInfo.setText("Aktuální pozice");
                        podminka = false;

                    }else {
                        int id = Integer.parseInt(cislo);

                        textViewDescriptionMarkerInfo.setText(title.get(id));
                        textViewTitleMarkerInfo.setText(description.get(id));

                        File file = new File(cesty.get(id));
                        Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                        imageViewMarkerInfo.setImageBitmap(bitmap);
                        podminka = true;
                    }
                    return v;
                }
            });
        }
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pridat_foto:
                aktivujPridatFoto();
                return true;
            case R.id.menu_zobrazit_seznam:
                aktivujZobrazSeznam();
                return true;
            case R.id.menu_kontakt:
                aktivujKontakt();
                return true;
            case R.id.menu_o_aplikaci:
                aktivujOAplikaci();
                return true;
            case R.id.menu_statistika:
                aktivujStatistiku();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        googleMaps.setMapType(typMapyList[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void aktivujPridatFoto() {
        Intent intent = new Intent(MapsActivity.this, PridatFotkuActivity.class);
        startActivity(intent);
    }

    public void aktivujZobrazSeznam() {
        Intent intent = new Intent(MapsActivity.this, PlaceListActivity.class);
        startActivity(intent);
    }

    public void aktivujKontakt() {
        Intent intent = new Intent(MapsActivity.this, KontaktActivity.class);
        startActivity(intent);
    }

    public void aktivujOAplikaci() {
        Intent intent = new Intent(MapsActivity.this, OAplikaciActivity.class);
        startActivity(intent);
    }
    public void aktivujStatistiku(){
        Intent intent = new Intent(MapsActivity.this, StatistikaActivity.class);
        startActivity(intent);
    }
}

