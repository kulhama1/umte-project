package com.example.martin.projectskola.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.projectskola.R;
import com.example.martin.projectskola.databaze.PlaceListAdapter;
import com.example.martin.projectskola.databaze.SQLiteHelper;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

public class SinglePlaceActivity extends AppCompatActivity{
    int id;
    ImageView imageView;
    Bitmap bitmap;
    TextView textViewTitle, textViewDescription;
    EditText editTextSinglePlaceTitle, editTextSinglePlaceDescription;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place);

        Intent i = getIntent();

        final int position = i.getExtras().getInt("position");

        Cursor cursor = MapsActivity.sqLiteHelper.getData("SELECT * FROM PLACESCZECHREPUBLIC");

        cursor.moveToPosition(position);
            id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            final String cesta = cursor.getString(6);

        imageView = (ImageView) findViewById(R.id.imageViewSinglePlace);

        File file = new File(cesta);
        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);

        imageView.setImageBitmap(bitmap);

        textViewDescription = (TextView) findViewById(R.id.textViewDescriptionSinglePlace);
        textViewDescription.setText(description);

        textViewTitle = (TextView) findViewById(R.id.txtViewTitleSinglePlace);
        textViewTitle.setText(title);

        Button smazat = (Button) findViewById(R.id.buttonSinglePlaceSmazat);
        smazat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.sqLiteHelper.queryData("DELETE FROM PLACESCZECHREPUBLIC WHERE ID =" + id);

                Intent intent = new Intent(SinglePlaceActivity.this, PlaceListActivity.class);
                startActivity(intent);
            }
        });
        Button upravit = (Button) findViewById(R.id.buttonSinglePlaceUpravit);
        upravit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_update_display_single_image);
                editTextSinglePlaceTitle = (EditText) findViewById(R.id.editTextTitleSinglePlace);
                String defaultText = textViewTitle.getText().toString();
                editTextSinglePlaceTitle.setText(defaultText);
                editTextSinglePlaceDescription = (EditText) findViewById(R.id.editTextDescriptionSinglePlace);
                defaultText = textViewDescription.getText().toString();
                editTextSinglePlaceDescription.setText(defaultText);
                ImageView imageViewUprava = (ImageView) findViewById(R.id.imageViewSinglePlaceUprava);
                imageViewUprava.setImageBitmap(bitmap);

                Button ulozitUpravy = (Button) findViewById(R.id.buttonSinglePlaceUlozit);
                ulozitUpravy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String titleUprava, descriptionUprava;
                        titleUprava = editTextSinglePlaceTitle.getText().toString();
                        descriptionUprava = editTextSinglePlaceDescription.getText().toString();
                        MapsActivity.sqLiteHelper.queryData("UPDATE PLACESCZECHREPUBLIC SET title='" +titleUprava+"' WHERE id=" + id);
                        MapsActivity.sqLiteHelper.queryData("UPDATE PLACESCZECHREPUBLIC SET description='" +descriptionUprava+"' WHERE id=" + id);

                        Intent intent = new Intent(SinglePlaceActivity.this, PlaceListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        Button sdilet = (Button) findViewById(R.id.buttonSinglePlaceSdilet);
        sdilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image_path = cesta;
                File file = new File(image_path);
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent .setType("image/*");
                intent .putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(intent);
            }
        });
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
}
