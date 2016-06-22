package com.travelMaker;

/**
 * Created by yoon on 2016. 6. 21..
 */
public class plane {

    // private variables

    public String _details;

    public plane() {

    }
    public plane(String _details) {
        this._details = _details;
    }
    public String getDet() {
        return this._details;
    }

    // setting weight
    public void setDet(String details) {
        this._details = details;
    }
}
