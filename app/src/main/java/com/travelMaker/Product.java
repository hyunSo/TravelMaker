package com.travelMaker;

/**
 * Created by yoon on 2016. 5. 29..
 */

public class Product {

    // private variables
    public int _id;
    public int _album_id;
    public String _name;
    public String _path;
    public String _weight;

    public Product() {
    }

    // constructor
    public Product(int id, int _album_id, String name, String _path, String _weight) {
        this._id = id;
        this._album_id = _album_id;
        this._name = name;
        this._path = _path;
        this._weight = _weight;
    }

    // constructor
    public Product(int _album_id, String name, String _path, String _weight) {
        this._album_id = _album_id;
        this._name = name;
        this._path = _path;
        this._weight = _weight;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting album id
    public int getAlbumID() {
        return this._album_id;
    }

    // setting album id
    public void setAlbumID(int album_id) {
        this._album_id = album_id;
    }


    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public String getPath() {
        return this._path;
    }

    // setting phone number
    public void setPath(String path) {
        this._path = path;
    }

    // getting weight
    public String getWeight() {
        return this._weight;
    }

    // setting weight
    public void setWeight(String weight) {
        this._weight = weight;
    }

}
