package com.example.martin.projectskola.databaze;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Cursor queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        return null;
    }

    public void insertData(String title, String description, double latitude, double longitude, String mesto, String cesta){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PLACESCZECHREPUBLIC VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, title);
        statement.bindString(2, description);
        statement.bindDouble(3, latitude);
        statement.bindDouble(4, longitude);
        statement.bindString(5, mesto);
        statement.bindString(6, cesta);

        statement.executeInsert();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
