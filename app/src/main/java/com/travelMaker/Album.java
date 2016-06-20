package com.travelMaker;

/**
 * Created by yoon on 2016. 5. 29..
 */

public class Album {

    // private variables
    public int _id;
    public String _name;
    public String _path;
    public String _plane;
    public String _maxWeight;
    public String _currWeight;

    public Album() {
    }

    // constructor
    public Album(int id, String name, String _path, String _plane, String _maxWeight) {
        this._id = id;
        this._name = name;
        this._path = _path;
        this._plane = _plane;
        this._maxWeight = _maxWeight;
        this._currWeight = new String("0");
    }

    // constructor
    public Album(String name, String _path, String _plane, String _maxWeight) {
        this._name = name;
        this._path = _path;
        this._plane = _plane;
        this._maxWeight = _maxWeight;
        this._currWeight = new String("0");
    }

    // constructor
    public Album(int id, String name, String _path, String _plane, String _maxWeight, String _currWeight) {
        this._id = id;
        this._name = name;
        this._path = _path;
        this._plane = _plane;
        this._maxWeight = _maxWeight;
        this._currWeight = _currWeight;
    }

    // constructor
    public Album(String name, String _path, String _plane, String _maxWeight, String _currWeight) {
        this._name = name;
        this._path = _path;
        this._plane = _plane;
        this._maxWeight = _maxWeight;
        this._currWeight = _currWeight;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
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

    // getting plane
    public String getPlane() {
        return this._plane;
    }

    // setting plane
    public void setPlane(String plane) {
        this._plane = plane;
    }

    public String get_maxWeight() {
        return _maxWeight;
    }

    public void set_maxWeight(String _maxWeight) {
        this._maxWeight = _maxWeight;
    }

    public String get_currWeight() {
        return _currWeight;
    }

    public void set_currWeight(String _currWeight) {
        this._currWeight = _currWeight;
    }
}
