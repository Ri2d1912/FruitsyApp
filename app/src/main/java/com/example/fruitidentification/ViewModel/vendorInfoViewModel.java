package com.example.fruitidentification.ViewModel;

public class vendorInfoViewModel {

    private long vendorId;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String extensionName;
    private String dateOfBirth;
    private String gender;
    private String streetAddress;
    private String barangay;
    private String city;
    private String province;
    private String postalCode;
    private String mobileNumber;
    private byte[] validId;

    // Constructor to initialize all fields
    public vendorInfoViewModel(long vendorId, String username, String firstName, String middleName,
                               String lastName, String extensionName, String dateOfBirth, String gender,
                               String streetAddress, String barangay, String city, String province,
                               String postalCode, String mobileNumber, byte[] validId) {
        this.vendorId = vendorId;
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.extensionName = extensionName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.streetAddress = streetAddress;
        this.barangay = barangay;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.mobileNumber = mobileNumber;
        this.validId = validId;
    }

    // Getters and Setters for all fields
    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public byte[] getValidId() {
        return validId;
    }

    public void setValidId(byte[] validId) {
        this.validId = validId;
    }
}
