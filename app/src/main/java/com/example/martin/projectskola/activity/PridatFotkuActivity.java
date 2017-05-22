package com.example.martin.projectskola.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.projectskola.R;
import com.example.martin.projectskola.databaze.SQLiteHelper;
import com.example.martin.projectskola.gps.GPS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PridatFotkuActivity extends AppCompatActivity {
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    ImageView imageViewPorizenaFotka;
    Button pridatFotku, poriditFotku;
    EditText title, description;
    private Location mLocation;
    double latitude, longitude;
    String cesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pridat_fotku);

        GPS gps = new GPS(getApplicationContext());
        mLocation = gps.getLocation();

        try{
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }
        catch (NullPointerException e){
            new AlertDialog.Builder(PridatFotkuActivity.this)
                    .setTitle("GPS nenalezena")
                    .setMessage("Zapněte na mobilním zařízení GPS a Internet, jinak nebude aplikace fungovat správně!")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }

        imageViewPorizenaFotka = (ImageView) findViewById(R.id.imgPhotoFromCamera);
        pridatFotku = (Button) findViewById(R.id.btnAddPhoto);
        poriditFotku = (Button) findViewById(R.id.btnMakePhoto);
        title = (EditText) findViewById(R.id.editTitlePhoto);
        description = (EditText) findViewById(R.id.editDescriptionPhoto);

        poriditFotku.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File imageStorageFolder = new File(Environment.getExternalStorageDirectory()+File.separator+"AppFotky");
                if (!imageStorageFolder.exists())
                {
                    imageStorageFolder.mkdirs();
                    Log.d("folder created" , "Folder created at: "+imageStorageFolder.toString());
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                cesta = Environment.getExternalStorageDirectory()+File.separator + "AppFotky/" + timeStamp +".jpg";
                File file = new File(cesta);
                Log.e("cesta",cesta);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
            }
        });

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();
        final String mesto = addresses.get(0).getAddressLine(maxAddressLine-1);

        pridatFotku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    MapsActivity.sqLiteHelper.insertData(
                            title.getText().toString().trim(),
                            description.getText().toString().trim(),
                    latitude, longitude, mesto, cesta);
                    Toast.makeText(getApplicationContext(), "Fotka úspěšně přidána!!", Toast.LENGTH_SHORT).show();
                    title.setText("");
                    description.setText("");
                    imageViewPorizenaFotka.setImageResource(R.mipmap.ic_launcher);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE)
        {
            File file = new File(cesta);
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            imageViewPorizenaFotka.setImageBitmap(bitmap);
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
        switch (item.getItemId()){
            case R.id.menu_zobrazit_seznam:
                aktivujSeznamFotek();
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
    public void aktivujSeznamFotek(){
        Intent intent = new Intent(PridatFotkuActivity.this, PlaceListActivity.class);
        startActivity(intent);
    }
    public void aktivujMapuFotek(){
        Intent intent = new Intent(PridatFotkuActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void aktivujKontakt(){
        Intent intent = new Intent(PridatFotkuActivity.this, KontaktActivity.class);
        startActivity(intent);
    }
    public void aktivujOAplikaci(){
        Intent intent = new Intent(PridatFotkuActivity.this, OAplikaciActivity.class);
        startActivity(intent);
    }
    public void aktivujStatistiku(){
        Intent intent = new Intent(PridatFotkuActivity.this, StatistikaActivity.class);
        startActivity(intent);
    }
}
