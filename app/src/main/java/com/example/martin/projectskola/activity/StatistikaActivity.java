package com.example.martin.projectskola.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.TextView;

import com.example.martin.projectskola.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StatistikaActivity extends AppCompatActivity {

    private List<String> mesta = new ArrayList<>();
    private List<Integer> pocetMestek = new ArrayList<>();
    private List<String> mestaVyber = new ArrayList<>();
    List<String> pocetFotekVeMeste = new ArrayList<>();
    List<String> pocetFotekVeMesteUp = new ArrayList<>();
    int pocetFotek = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistika);

        Cursor cursor = MapsActivity.sqLiteHelper.getData("SELECT * FROM PLACESCZECHREPUBLIC");

        while (cursor.moveToNext()) {

            String stateName = cursor.getString(5);

            stateName = stateName.replace("0", "");
            stateName = stateName.replace("1", "");
            stateName = stateName.replace("2", "");
            stateName = stateName.replace("3", "");
            stateName = stateName.replace("4", "");
            stateName = stateName.replace("5", "");
            stateName = stateName.replace("6", "");
            stateName = stateName.replace("7", "");
            stateName = stateName.replace("8", "");
            stateName = stateName.replace("9", "");

            int j = 0;
            char charArray[] = new char[0];
            for (int i = 0; i < stateName.length(); i++) {
                if (stateName.charAt(i) != ' ') {
                    charArray = new char[stateName.length() - i];
                    stateName.getChars(i, stateName.length(), charArray, 0);
                    i = stateName.length();
                }
            }
            stateName = String.copyValueOf(charArray);
            mesta.add(stateName);
            pocetFotek++;
        }
        mesta.add("konec-retezce");

        List<String> subList = mesta.subList(0, mesta.size());
        Collections.sort(subList);

        int pocetMest = 1;
        String nazev, nazev2 = "";
        int g = 0;

        for (int i = 0; i < subList.size() - 1; i++) {
            nazev = subList.get(i);
            nazev2 = subList.get(i + 1);

            if (nazev.equals(nazev2)) {
                pocetMest = pocetMest + 1;

            } else {
                pocetMestek.add(pocetMest);
                mestaVyber.add(nazev);
                g++;
                pocetMest = 1;
            }
            Log.e("subList", String.valueOf(subList.get(i)));
        }
        Log.e("cisla", Arrays.toString(pocetMestek.toArray()));
        Log.e("mesta", Arrays.toString(mestaVyber.toArray()));

        for (int i = 0; i < mestaVyber.size(); i++) {
            pocetFotekVeMeste.add(pocetMestek.get(i) + "/" + mestaVyber.get(i));
        }
        List<String> subList1 = pocetFotekVeMeste.subList(0, pocetFotekVeMeste.size());
        Collections.sort(subList1);

        for (int i = subList1.size() - 1; i >= 0; i--) {
            pocetFotekVeMesteUp.add(subList1.get(i));
        }
        Log.e("nejviceFotekVeMeste:", Arrays.toString(pocetFotekVeMesteUp.toArray()));


        int index = 0;
        if (pocetFotekVeMesteUp.size() > 0) {
            TextView textView1 = (TextView) findViewById(R.id.textViewPrvniLokalita);
            textView1.setText(vratMesto(index));
            TextView textView1Cislo = (TextView) findViewById(R.id.textViewPrvniLokalitaCislo);
            textView1Cislo.setText(vratCislo(index));
        }
        if (pocetFotekVeMesteUp.size() > 1) {
            TextView textView2 = (TextView) findViewById(R.id.textViewDruhaLokalita);
            textView2.setText(vratMesto(index + 1));
            TextView textView2Cislo = (TextView) findViewById(R.id.textViewDruhaLokalitaCislo);
            textView2Cislo.setText(vratCislo(index + 1));
        }
        if (pocetFotekVeMesteUp.size() > 2) {
            TextView textView3 = (TextView) findViewById(R.id.textViewTretiLokalita);
            textView3.setText(vratMesto(index + 2));
            TextView textView3Cislo = (TextView) findViewById(R.id.textViewTretiLokalitaCislo);
            textView3Cislo.setText(vratCislo(index + 2));
        }
        if (pocetFotekVeMesteUp.size() > 3) {
            TextView textView4 = (TextView) findViewById(R.id.textViewCtvrtaLokalita);
            textView4.setText(vratMesto(index + 3));
            TextView textView4Cislo = (TextView) findViewById(R.id.textViewCtvrtaLokalitaCislo);
            textView4Cislo.setText(vratCislo(index + 3));
        }
        if (pocetFotekVeMesteUp.size() > 4) {
            TextView textView5 = (TextView) findViewById(R.id.textViewPataLokalita);
            textView5.setText(vratMesto(index + 4));
            TextView textView5Cislo = (TextView) findViewById(R.id.textViewPataLokalitaCislo);
            textView5Cislo.setText(vratCislo(index + 4));
        }
        TextView pocetFotekCelkem = (TextView) findViewById(R.id.textViewCelkovyPocetFotek);
        pocetFotekCelkem.setText(String.valueOf(pocetFotek));
    }

    private String vratCislo(int index) {
        String mesto = pocetFotekVeMesteUp.get(index);
        char charArray[] = new char[0];
        for (int i = 0; i < mesto.length(); i++) {
            if (mesto.charAt(i) == '/') {
                charArray = new char[i];
                mesto.getChars(0, i, charArray, 0);
            }
        }
        return String.copyValueOf(charArray);
    }
    private String vratMesto(int index) {
        String mesto = pocetFotekVeMesteUp.get(index);
        char charArray[] = new char[0];
        for (int i = 0; i < mesto.length(); i++) {
            if (mesto.charAt(i) == '/') {
                charArray = new char[mesto.length()-i];
                mesto.getChars(i+1, mesto.length(), charArray, 0);
                i = mesto.length();
            }
        }
        return String.copyValueOf(charArray);
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
            case R.id.menu_zobrazit_seznam:
                aktivujSeznamFotek();
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
        Intent intent = new Intent(StatistikaActivity.this, PridatFotkuActivity.class);
        startActivity(intent);
    }
    public void aktivujMapuFotek(){
        Intent intent = new Intent(StatistikaActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void aktivujKontakt(){
        Intent intent = new Intent(StatistikaActivity.this, KontaktActivity.class);
        startActivity(intent);
    }
    public void aktivujOAplikaci(){
        Intent intent = new Intent(StatistikaActivity.this, OAplikaciActivity.class);
        startActivity(intent);
    }
    public void aktivujSeznamFotek(){
        Intent intent = new Intent(StatistikaActivity.this, PlaceListActivity.class);
        startActivity(intent);
    }
}


