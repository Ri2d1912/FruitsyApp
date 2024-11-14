package com.example.fruitidentification.ViewModel;

public class vendorInfoViewModel {
    private String Username;

    private String firstName;
    private String middleName;
    private String lastName;
    private String extensionName;

    private String streetAddress;
    private String barangay;
    private String city;
    private String province;
    private String mobileNumber;


    // Constructor to initialize all fields
    public vendorInfoViewModel(String Username, String firstName, String middleName,
                               String lastName, String extensionName,String streetAddress, String barangay, String city, String province,
                               String mobileNumber) {
        this.Username = Username;

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.extensionName = extensionName;
        this.streetAddress = streetAddress;
        this.barangay = barangay;
        this.city = city;
        this.province = province;
        this.mobileNumber = mobileNumber;
    }
    public String getUserName() {return Username;}

    public String getFirstName() {return firstName;}

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }


}
