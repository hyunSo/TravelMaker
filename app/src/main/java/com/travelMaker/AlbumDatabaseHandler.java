package com.travelMaker;

/**
 * Created by yoon on 2016. 5. 29..
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlbumDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "albumsManager";

    // Albums table name
    private static final String TABLE_ALBUMS = "albums";
    private static final String TABLE_PRODUCTS = "products";


    // Albums Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private final ArrayList<Album> album_list = new ArrayList<Album>();

    ProductDatabaseHandler p_db;

    public AlbumDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        p_db = new ProductDatabaseHandler(context);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALBUMS_TABLE = "CREATE TABLE " + TABLE_ALBUMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_ALBUMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations about ALBUM
     */

    // Adding new album
    public void Add_Album(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, album.getName()); // Album Name
        values.put(KEY_PH_NO, album.getPath()); // Album Phone
        values.put(KEY_EMAIL, album.getPlane()); // Album Email
        // Inserting Row
        db.insert(TABLE_ALBUMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single album
    Album Get_Album(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALBUMS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO, KEY_EMAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Album album = new Album(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return album
        cursor.close();
        db.close();

        return album;
    }

    // Getting All Albums
    public ArrayList<Album> Get_Albums() {
        try {
            album_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_ALBUMS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Album album = new Album();
                    album.setID(Integer.parseInt(cursor.getString(0)));
                    album.setName(cursor.getString(1));
                    album.setPath(cursor.getString(2));
                    album.setPlane(cursor.getString(3));
                    // Adding album to list
                    album_list.add(album);
                } while (cursor.moveToNext());
            }

            // return album list
            cursor.close();
            db.close();
            return album_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_album", "" + e);
        }

        return album_list;
    }

    // Updating single album
    public int Update_Album(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, album.getName());
        values.put(KEY_PH_NO, album.getPath());
        values.put(KEY_EMAIL, album.getPlane());

        // updating row
        return db.update(TABLE_ALBUMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(album.getID()) });
    }

    // Deleting single album
    public void Delete_Album(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase pdb = p_db.getWritableDatabase();
        pdb.delete(TABLE_PRODUCTS, "album_id" + " = ?",
                new String[]{String.valueOf(id)});
        db.delete(TABLE_ALBUMS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Getting albums Count
    public int Get_Total_Albums() {
        String countQuery = "SELECT  * FROM " + TABLE_ALBUMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
