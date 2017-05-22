package com.example.martin.projectskola.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.martin.projectskola.R;
import com.example.martin.projectskola.databaze.Place;
import com.example.martin.projectskola.databaze.PlaceListAdapter;
import com.example.martin.projectskola.databaze.SQLiteHelper;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Place> list;
    PlaceListAdapter adapter = null;
    int idTabulka;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zobrazit_seznam);

        listView = (ListView) findViewById(R.id.list_view_of_places);
        list = new ArrayList<>();
        adapter = new PlaceListAdapter(this, R.layout.place_list, list);
        listView.setAdapter(adapter);

        Cursor cursor = MapsActivity.sqLiteHelper.getData("SELECT * FROM PLACESCZECHREPUBLIC");
        list.clear();
        while (cursor.moveToNext()){
            idTabulka = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            String city = cursor.getString(5);
            String cesta = cursor.getString(6);

            list.add(new Place(idTabulka, title, description, latitude, longitude, city, cesta));
        }
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SinglePlaceActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pridat_foto:
                aktivujPridatFoto();
                return true;
            case R.id.menu_mapa_fotek:
                aktivujMapuFotek();
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_zobrazit_seznam, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void aktivujPridatFoto(){
        Intent intent = new Intent(PlaceListActivity.this, PridatFotkuActivity.class);
        startActivity(intent);
    }
    public void aktivujMapuFotek(){
        Intent intent = new Intent(PlaceListActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void aktivujKontakt(){
        Intent intent = new Intent(PlaceListActivity.this, KontaktActivity.class);
        startActivity(intent);
    }
    public void aktivujOAplikaci(){
        Intent intent = new Intent(PlaceListActivity.this, OAplikaciActivity.class);
        startActivity(intent);
    }
    public void aktivujStatistiku(){
        Intent intent = new Intent(PlaceListActivity.this, StatistikaActivity.class);
        startActivity(intent);
    }
}
