package com.travelMaker;

/**
 * Created by yoon on 2016. 5. 29..
 */

        import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ProductDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "productsManager";

    // Products table name
    private static final String TABLE_PRODUCTS = "products";

    // Products Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PATH = "path";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_PRIORITY = "priority";
    private final ArrayList<Product> product_list = new ArrayList<Product>();

    public ProductDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ALBUM_ID + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_PATH + " TEXT," + KEY_WEIGHT + " TEXT," + KEY_PRIORITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations about ALBUM
     */

    // Adding new product
    public void Add_Product(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALBUM_ID, product.getAlbumID()); // Product Name
        values.put(KEY_NAME, product.getName()); // Product Name
        values.put(KEY_PATH, product.getPath()); // Product Phone
        values.put(KEY_WEIGHT, product.getWeight()); // Product Email
        values.put(KEY_PRIORITY, product.get_priority()); // Product Email
        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single product
    Product Get_Product(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_ID, KEY_ALBUM_ID,
                        KEY_NAME, KEY_PATH, KEY_WEIGHT, KEY_PRIORITY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
        // return product
        cursor.close();
        db.close();

        return product;
    }

    // Getting All Products by Album
    public ArrayList<Product> Get_Products_by_Album(int id) {
        try {
            product_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS+" WHERE album_id = "+id;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setID(Integer.parseInt(cursor.getString(0)));
                    product.setAlbumID(Integer.parseInt(cursor.getString(1)));
                    product.setName(cursor.getString(2));
                    product.setPath(cursor.getString(3));
                    product.setWeight(cursor.getString(4));
                    product.set_priority(Integer.parseInt(cursor.getString(5)));
                    // Adding product to list
                    product_list.add(product);
                } while (cursor.moveToNext());
            }

            // return product list
            cursor.close();
            db.close();
            return product_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_product", "" + e);
        }

        return product_list;
    }

    // Getting All Products
    public ArrayList<Product> Get_Products() {
        try {
            product_list.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setID(Integer.parseInt(cursor.getString(0)));
                    product.setAlbumID(Integer.parseInt(cursor.getString(1)));
                    product.setName(cursor.getString(2));
                    product.setPath(cursor.getString(3));
                    product.setWeight(cursor.getString(4));
                    product.set_priority(Integer.parseInt(cursor.getString(5)));
                    // Adding product to list
                    product_list.add(product);
                } while (cursor.moveToNext());
            }

            // return product list
            cursor.close();
            db.close();
            return product_list;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_product", "" + e);
        }

        return product_list;
    }

    // Updating single product
    public int Update_Product(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ALBUM_ID, product.getAlbumID());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_PATH, product.getPath());
        values.put(KEY_WEIGHT, product.getWeight());
        values.put(KEY_WEIGHT, product.get_priority());

        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getID()) });
    }

    // Deleting single product
    public void Delete_Product(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Getting products Count
    public int Get_Total_Products() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}

