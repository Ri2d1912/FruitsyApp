package com.example.fruitidentification.ViewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class regFrag1VM extends ViewModel {

    private MutableLiveData<Uri> imageUri = new MutableLiveData<>();

    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> fName = new MutableLiveData<>();
    private final MutableLiveData<String> mName = new MutableLiveData<>();
    private final MutableLiveData<String> lName = new MutableLiveData<>();
    private final MutableLiveData<String> exName = new MutableLiveData<>();
    private final MutableLiveData<String> street = new MutableLiveData<>();
    private final MutableLiveData<String> barangay = new MutableLiveData<>();
    private final MutableLiveData<String> city = new MutableLiveData<>();
    private final MutableLiveData<String> province = new MutableLiveData<>();

    private final MutableLiveData<String> postal = new MutableLiveData<>();
    private final MutableLiveData<String> mobile = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();
    private final MutableLiveData<String> bday = new MutableLiveData<>();

    // Getter and Setter methods for each field

    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public LiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword.setValue(confirmPassword);
    }

    public LiveData<String> getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName.setValue(fName);
    }

    public LiveData<String> getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName.setValue(mName);
    }

    public LiveData<String> getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName.setValue(lName);
    }

    public LiveData<String> getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName.setValue(exName);
    }

    public LiveData<String> getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street.setValue(street);
    }

    public LiveData<String> getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay.setValue(barangay);
    }

    public LiveData<String> getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }

    public LiveData<String> getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province.setValue(province);
    }

    public LiveData<String> getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal.setValue(postal);
    }

    public LiveData<String> getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile.setValue(mobile);
    }

    public LiveData<String> getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public LiveData<Uri> getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri uri) {
        imageUri.setValue(uri);
    }

    public LiveData<String> getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday.setValue(bday);
    }
}
