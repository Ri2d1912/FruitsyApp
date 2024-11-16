package com.example.fruitidentification.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class shopLocationViewModel extends ViewModel {
    private MutableLiveData<Double> latitude = new MutableLiveData<>();
    private MutableLiveData<Double> longitude = new MutableLiveData<>();
    private MutableLiveData<String> region = new MutableLiveData<>();
    private MutableLiveData<String> address = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPrimary = new MutableLiveData<>();
    private Long shopId;

    public void setLatitude(double lat) {
        latitude.setValue(lat);
    }

    public void setLongitude(double lon) {
        longitude.setValue(lon);
    }

    public void setRegion(String region) {
        this.region.setValue(region);
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary.setValue(isPrimary);
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId; // Directly assign the value.
    }

    public LiveData<Double> getLatitude() {
        return latitude;
    }

    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public LiveData<String> getRegion() {
        return region;
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public LiveData<Boolean> getIsPrimary() {
        return isPrimary;
    }

    public Long getShopId() {
        return shopId; // Return the plain Long value.
    }

    public void updateLocation(double lat, double lng, String addr) {
        latitude.setValue(lat);
        longitude.setValue(lng);
        address.setValue(addr);
    }
}
