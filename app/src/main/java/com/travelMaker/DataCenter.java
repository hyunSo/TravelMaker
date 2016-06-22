package com.travelMaker;

/**
 * Created by yoon on 2016. 6. 21..
 */
public class DataCenter {
    // about album private variables
    private static int albumId = 0;
    private static String albumName = null;
    private static String albumPath = null;
    private static String albumPlane = null;

    // about product private variables
    private static int productId = 0;
    private static String productName = null;
    private static String productPath = null;
    private static String productWeight = null;

    public static int getAlbumId() {
        return albumId;
    }

    public static void setAlbumId(int albumId) {
        DataCenter.albumId = albumId;
    }

    public static String getAlbumName() {
        return albumName;
    }

    public static void setAlbumName(String albumName) {
        DataCenter.albumName = albumName;
    }

    public static String getAlbumPath() {
        return albumPath;
    }

    public static void setAlbumPath(String albumPath) {
        DataCenter.albumPath = albumPath;
    }

    public static String getAlbumPlane() {
        return albumPlane;
    }

    public static void setAlbumPlane(String albumPlane) {
        DataCenter.albumPlane = albumPlane;
    }

    public static int getProductId() {
        return productId;
    }

    public static void setProductId(int productId) {
        DataCenter.productId = productId;
    }

    public static String getProductName() {
        return productName;
    }

    public static void setProductName(String productName) {
        DataCenter.productName = productName;
    }

    public static String getProductPath() {
        return productPath;
    }

    public static void setProductPath(String productPath) {
        DataCenter.productPath = productPath;
    }

    public static String getProductWeight() {
        return productWeight;
    }

    public static void setProductWeight(String productWeight) {
        DataCenter.productWeight = productWeight;
    }
}
