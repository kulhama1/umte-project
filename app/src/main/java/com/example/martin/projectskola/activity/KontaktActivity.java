package com.example.martin.projectskola.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.martin.projectskola.R;

public class KontaktActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakt);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pridat_foto:
                aktivujPridatFoto();
                return true;
            case R.id.menu_zobrazit_seznam:
                aktivujZobrazSeznam();
                return true;
            case R.id.menu_mapa_fotek:
                aktivujMapuFotek();
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
        menuInflater.inflate(R.menu.activity_kontakt, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void aktivujPridatFoto(){
        Intent intent = new Intent(KontaktActivity.this, PridatFotkuActivity.class);
        startActivity(intent);
    }
    public void aktivujZobrazSeznam(){
        Intent intent = new Intent(KontaktActivity.this, PlaceListActivity.class);
        startActivity(intent);
    }
    public void aktivujMapuFotek(){
        Intent intent = new Intent(KontaktActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void aktivujOAplikaci(){
        Intent intent = new Intent(KontaktActivity.this, OAplikaciActivity.class);
        startActivity(intent);
    }
    public void aktivujStatistiku(){
        Intent intent = new Intent(KontaktActivity.this, StatistikaActivity.class);
        startActivity(intent);
    }
}
