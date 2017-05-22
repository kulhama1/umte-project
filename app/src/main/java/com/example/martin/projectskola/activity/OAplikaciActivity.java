package com.example.martin.projectskola.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.martin.projectskola.R;

public class OAplikaciActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oaplikaci);

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
            case R.id.menu_kontakt:
                aktivujKontakt();
                return true;
            case R.id.menu_mapa_fotek:
                aktivujMapuFotek();
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
        menuInflater.inflate(R.menu.activity_oaplikaci, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void aktivujPridatFoto(){
        Intent intent = new Intent(OAplikaciActivity.this, PridatFotkuActivity.class);
        startActivity(intent);
    }
    public void aktivujZobrazSeznam(){
        Intent intent = new Intent(OAplikaciActivity.this, PlaceListActivity.class);
        startActivity(intent);
    }
    public void aktivujKontakt(){
        Intent intent = new Intent(OAplikaciActivity.this, KontaktActivity.class);
        startActivity(intent);
    }
    public void aktivujMapuFotek(){
        Intent intent = new Intent(OAplikaciActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void aktivujStatistiku(){
        Intent intent = new Intent(OAplikaciActivity.this, StatistikaActivity.class);
        startActivity(intent);
    }
}
