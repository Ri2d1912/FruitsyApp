package com.example.fruitidentification.ViewModel;

public class dbShopLocationViewModel {
    private double latitude;
    private double longitude;
    private String region;
    private String address;
    private boolean isPrimary;

    // Constructor to initialize the fields
    public dbShopLocationViewModel(double latitude, double longitude, String region, String address, boolean isPrimary) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
        this.address = address;
        this.isPrimary = isPrimary;
    }

    // Getter for latitude
    public double getLatitude() {
        return latitude;
    }

    // Setter for latitude
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter for longitude
    public double getLongitude() {
        return longitude;
    }

    // Setter for longitude
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Getter for region
    public String getRegion() {
        return region;
    }

    // Setter for region
    public void setRegion(String region) {
        this.region = region;
    }

    // Getter for address
    public String getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter for isPrimary
    public boolean isPrimary() {
        return isPrimary;
    }

    // Setter for isPrimary
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
