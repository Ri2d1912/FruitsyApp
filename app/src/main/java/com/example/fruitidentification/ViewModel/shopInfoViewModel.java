
package com.example.fruitidentification.ViewModel;

public class shopInfoViewModel {
    private String shopName;
    private String shopDesc;

    private String shopStreet;
    private String shopBarangay;
    private String shopCity;
    private String shopProvince;
    private String email;
    private String mobileNumber;
    private String telephoneNumber;
    private String openingHrs;
    private String immediateOrderPolicy;
    private String advanceReservationPolicy;

    public shopInfoViewModel(String shopName, String shopDesc, String shopStreet, String shopBarangay, String shopCity,
                             String shopProvince, String email, String openingHrs,
                             String mobileNumber, String telephoneNumber,
                             String immediateOrderPolicy, String advanceReservationPolicy) {
        this.openingHrs = openingHrs;
        this.shopDesc = shopDesc;
        this.shopName = shopName;
        this.shopStreet = shopStreet;
        this.shopBarangay = shopBarangay;
        this.shopCity = shopCity;
        this.shopProvince = shopProvince;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.telephoneNumber = telephoneNumber;
        this.immediateOrderPolicy = immediateOrderPolicy;
        this.advanceReservationPolicy = advanceReservationPolicy;
    }

    public String getShopName() {
        return shopName;
    }
    public String getShopDesc() {
        return shopDesc;
    }

    public String getShopStreet() {
        return shopStreet;
    }

    public String getShopBarangay() {
        return shopBarangay;
    }

    public String getShopCity() {
        return shopCity;
    }

    public String getShopProvince() {
        return shopProvince;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public String getOpeningHrs() {
        return openingHrs;
    }

    public String getImmediateOrderPolicy() {
        return immediateOrderPolicy;
    }

    public String getAdvanceReservationPolicy() {
        return advanceReservationPolicy;
    }
}
