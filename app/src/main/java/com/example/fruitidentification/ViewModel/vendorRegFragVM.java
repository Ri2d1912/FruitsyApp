package com.example.fruitidentification.ViewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class vendorRegFragVM extends ViewModel {

    private MutableLiveData<Uri> validIdImageUri = new MutableLiveData<>();
    private MutableLiveData<Uri> shopProfileImageUri = new MutableLiveData<>();

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


    // -------------------------------------------------------------------shop info----------------------------------------------

    private final MutableLiveData<String> shopName = new MutableLiveData<>();
    private final MutableLiveData<String> shopMobile = new MutableLiveData<>();

    private final MutableLiveData<String> telNo = new MutableLiveData<>();
    private final MutableLiveData<String> shopEmail = new MutableLiveData<>();

    private final MutableLiveData<String> storeHrs = new MutableLiveData<>();
    private final MutableLiveData<String> editDesc = new MutableLiveData<>();
    private final MutableLiveData<String> shopStreet = new MutableLiveData<>();
    private final MutableLiveData<String> shopBarangay = new MutableLiveData<>();
    private final MutableLiveData<String> shopCity = new MutableLiveData<>();
    private final MutableLiveData<String> shopProvince = new MutableLiveData<>();
    private final MutableLiveData<String> shopPostal = new MutableLiveData<>();
    private final MutableLiveData<String> orderPolicy = new MutableLiveData<>();
    private final MutableLiveData<String> reservePolicy = new MutableLiveData<>();





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

    public LiveData<Uri> getValidIdImageUri() {
        return validIdImageUri;
    }

    public void setValidIdImageUri(Uri uri) {
        validIdImageUri.setValue(uri);
    }

    public LiveData<String> getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday.setValue(bday);
    }


    // ---------------------------------------------------------------------------shop info---------------------------------------------------------------
    public LiveData<Uri> getshopProfileImageUri() {
        return shopProfileImageUri;
    }

    public void setshopProfileImageUri(Uri uri) {
        shopProfileImageUri.setValue(uri);
    }
    public LiveData<String> getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName.setValue(shopName);
    }

    public LiveData<String> getShopStreet() {
        return shopStreet;
    }

    public void setShopStreet(String shopStreet) {
        this.shopStreet.setValue(shopStreet);
    }

    public LiveData<String> getShopBarangay() {
        return shopBarangay;
    }

    public void setShopBarangay(String shopBarangay) {
        this.shopBarangay.setValue(shopBarangay);
    }

    public LiveData<String> getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity.setValue(shopCity);
    }

    public LiveData<String> getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince.setValue(shopProvince);
    }

    public LiveData<String> getShopPostal() {
        return shopPostal;
    }

    public void setShopPostal(String shopPostal) {
        this.shopPostal.setValue(shopPostal);
    }

    public LiveData<String> getshopNo() {
        return shopMobile;
    }

    public void setshopNo(String shopMobile) {
        this.shopMobile.setValue(shopMobile);
    }

    public LiveData<String> getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo.setValue(telNo);
    }

    public LiveData<String> getShopEmail() {return shopEmail;}

    public void setShopEmail(String shopEmail) {
        this.shopEmail.setValue(shopEmail);
    }

    public LiveData<String> getStoreHrs() {
        return storeHrs;
    }

    public void setStoreHrs(String storeHrs) {
        this.storeHrs.setValue(storeHrs);
    }

    public LiveData<String> getEditDesc() {
        return editDesc;
    }

    public void setEditDesc(String editDesc) {
        this.editDesc.setValue(editDesc);
    }

    public LiveData<String> getOrderPolicy() {
        return orderPolicy;
    }

    public void setOrderPolicy(String orderReservePolicy) {
        this.orderPolicy.setValue(orderReservePolicy);
    }

    public LiveData<String> getReservePolicy() {
        return reservePolicy;
    }

    public void setReservePolicy(String orderReservePolicy) {
        this.reservePolicy.setValue(orderReservePolicy);
    }




}
