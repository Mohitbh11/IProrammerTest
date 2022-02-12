package com.mohit.iprogrammertest.manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.mohit.iprogrammertest.model.ImageModel;

import java.util.ArrayList;

public class DatabaseInstance extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "programmer.db";
    public static final String IMAGES = "image_table";
    public static final String ID = "id";
    public static final String ALBUMID = "album_id";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String THUMBNAILURL = "thumbnail_url";

    public DatabaseInstance(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_sql = "CREATE TABLE IF NOT EXISTS " + IMAGES + "("
                + ID + " INTEGER PRIMARY KEY,"
                + ALBUMID + " INTEGER  ," + TITLE + " TEXT ,"
                + URL + " TEXT ," + THUMBNAILURL + " TEXT )";
        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IMAGES);
    }

    public boolean insertImages (String id, String albumId, String title, String url, String thumbnailUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(ALBUMID, albumId);
        contentValues.put(TITLE, title);
        contentValues.put(URL, url);
        contentValues.put(THUMBNAILURL, thumbnailUrl);

        db.insert(IMAGES, null, contentValues);
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<ImageModel> getAllImages() {
        ArrayList<ImageModel> array_list = new ArrayList<ImageModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from image_table", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new ImageModel(String.valueOf(res.getInt(res.getColumnIndex(ID))), String.valueOf(res.getInt(res.getColumnIndex(ALBUMID))),
                    res.getString(res.getColumnIndex(TITLE)), res.getString(res.getColumnIndex(URL)),
                    res.getString(res.getColumnIndex(THUMBNAILURL))));
            res.moveToNext();
        }
        return array_list;
    }
    public void deleteImageData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(IMAGES, "id=?", new String[]{id});
        db.close();
    }
}
