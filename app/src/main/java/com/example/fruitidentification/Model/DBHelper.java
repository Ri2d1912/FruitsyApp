package com.example.fruitidentification.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.fruitidentification.ViewModel.shopInfoViewModel;
import com.example.fruitidentification.ViewModel.vendorInfoViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    // Define the database name
    public static final String dbase = "FruitsyApp";

    // Constructor for DBHelper, initializes the database
    public DBHelper(Context context) {
        super(context, dbase, null, 1);
    }

    // Create the tables when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (\n" +
                "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    username TEXT NOT NULL UNIQUE,\n" +
                "    password TEXT NOT NULL,\n" +
                "    role TEXT CHECK(role IN ('admin', 'vendor', 'customer', 'guest')),\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");");

        db.execSQL("CREATE TABLE vendors (\n" +
                "    vendor_id INTEGER PRIMARY KEY,\n" +
                "    username TEXT NOT NULL UNIQUE,\n" +
                "    first_name TEXT NOT NULL,\n" +
                "    middle_name TEXT,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    extension_name TEXT,\n" +
                "    date_of_birth DATE,\n" +
                "    gender TEXT ,\n" +
                "    street_address TEXT,\n" +
                "    barangay TEXT,\n" +
                "    city TEXT,\n" +
                "    province TEXT,\n" +
                "    postal_code TEXT,\n" +
                "    mobile_number TEXT,\n" +
                "    valid_id BLOB,\n" +
                "    FOREIGN KEY (vendor_id) REFERENCES users (user_id)\n" +
                ");");

        db.execSQL("CREATE TABLE customers (\n" +
                "    customer_id INTEGER PRIMARY KEY,\n" +
                "    username TEXT NOT NULL UNIQUE,\n" +
                "    first_name TEXT NOT NULL,\n" +
                "    middle_name TEXT,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    extension_name TEXT,\n" +
                "    date_of_birth DATE,\n" +
                "    gender TEXT ,\n" +
                "    phone_number TEXT,\n" +
                "    street_address TEXT,\n" +
                "    barangay TEXT,\n" +
                "    city TEXT,\n" +
                "    province TEXT,\n" +
                "    postal_code TEXT,\n" +
                "    profile_picture BLOB,\n" +
                "    FOREIGN KEY (customer_id) REFERENCES users (user_id)\n" +
                ");");

        db.execSQL("CREATE TABLE guest (\n" +
                "    guest_id INTEGER PRIMARY KEY,\n" +
                "    session_id TEXT NOT NULL,\n" +
                "    FOREIGN KEY (guest_id) REFERENCES users (user_id)\n" +
                ");");

        db.execSQL("CREATE TABLE fruit_shop (\n" +
                "    shop_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    vendor_id INTEGER NOT NULL,\n" +
                "    shop_name TEXT NOT NULL,\n" +
                "    shop_street TEXT,\n" +
                "    shop_barangay TEXT,\n" +
                "    shop_city TEXT,\n" +
                "    shop_province TEXT,\n" +
                "    shop_postal TEXT,\n" +
                "    mobile_number TEXT,\n" +
                "    telephone_number TEXT,\n" +
                "    email TEXT,\n" +
                "    description TEXT,\n" +
                "    registration_date DATE,\n" +
                "    opening_hours TEXT,\n" +
                "    status TEXT CHECK(status IN ('open', 'closed')),\n" +
                "    immediate_order_policy TEXT CHECK(immediate_order_policy IN ('Payment Upon Pickup', 'Deposit Required', 'Full Payment in Advance', 'Flexible')),\n" +
                "    advance_reservation_policy TEXT CHECK(advance_reservation_policy IN ('Payment Upon Pickup', 'Deposit Required', 'Full Payment in Advance', 'Flexible')),\n" +
                "    Shop_profile_picture BLOB,\n" +
                "    shop_header_picture BLOB NOT NULL,\n" +
                "    dtiImage BLOB NOT NULL,\n" +
                "    birImage BLOB NOT NULL,\n" +
                "    FOREIGN KEY (vendor_id) REFERENCES vendors (vendor_id)\n" +
                ");");


        db.execSQL("CREATE TABLE shop_locations (\n" +
                "    location_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    shop_id INTEGER NOT NULL,\n" +
                "    latitude DECIMAL(10, 8),\n" +
                "    longitude DECIMAL(11, 8),\n" +
                "    region TEXT,\n" +
                "    address TEXT,\n" +
                "    is_primary BOOLEAN,\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    FOREIGN KEY (shop_id) REFERENCES fruit_shop (shop_id)\n" +
                ");");

        db.execSQL("CREATE TABLE fruits (\n" +
                "    fruit_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    fruit_name TEXT NOT NULL,\n" +
                "    category TEXT,\n" +
                "    description TEXT\n" +
                ");");

        db.execSQL("CREATE TABLE fruit_shop_inventory (\n" +
                "    inventory_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    shop_id INTEGER NOT NULL,\n" +
                "    fruit_id INTEGER NOT NULL,\n" +
                "    quantity INTEGER NOT NULL,\n" +
                "    price_per_unit DECIMAL(10, 2) NOT NULL,\n" +
                "    unit TEXT CHECK(unit IN ('kg', 'piece')),\n" +
                "    farm_origin TEXT,\n" +
                "    status TEXT CHECK(status IN ('live', 'sold out', 'delisted')),\n" +
                "    FOREIGN KEY (shop_id) REFERENCES fruit_shop (shop_id),\n" +
                "    FOREIGN KEY (fruit_id) REFERENCES fruits (fruit_id)\n" +
                ");");

        db.execSQL("CREATE TABLE orders (\n" +
                "    order_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    customer_id INTEGER NOT NULL,\n" +
                "    shop_id INTEGER NOT NULL,\n" +
                "    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    pickup_date DATETIME,\n" +
                "    status TEXT CHECK(status IN ('Pending', 'Confirmed', 'Awaiting Customer Action', 'Cancelled by Customer', 'Expired', 'Cancelled by Shop')),\n" +
                "    total_amount DECIMAL(10, 2),\n" +
                "    confirmation_date DATETIME,\n" +
                "    expiration_date DATETIME,\n" +
                "    cancellation_date DATETIME,\n" +
                "    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),\n" +
                "    FOREIGN KEY (shop_id) REFERENCES fruit_shop (shop_id)\n" +
                ");");

        db.execSQL("CREATE TABLE orders_items (\n" +
                "    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    order_id INTEGER NOT NULL,\n" +
                "    inventory_id INTEGER NOT NULL,\n" +
                "    quantity INTEGER NOT NULL,\n" +
                "    FOREIGN KEY (order_id) REFERENCES orders (order_id),\n" +
                "    FOREIGN KEY (inventory_id) REFERENCES fruit_shop_inventory (inventory_id)\n" +
                ");");

        db.execSQL("CREATE TABLE shop_reports (\n" +
                "    concern_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_id INTEGER NOT NULL,\n" +
                "    shop_id INTEGER,\n" +
                "    concern_type TEXT NOT NULL,\n" +
                "    concern_description TEXT NOT NULL,\n" +
                "    status TEXT CHECK(status IN ('open', 'in_progress', 'resolved', 'closed')),\n" +
                "    priority TEXT CHECK(priority IN ('low', 'medium', 'high')),\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    updated_at TIMESTAMP,\n" +
                "    resolved_at TIMESTAMP,\n" +
                "    resolution_notes TEXT,\n" +
                "    assigned_to INTEGER,\n" +
                "    FOREIGN KEY (user_id) REFERENCES users (user_id),\n" +
                "    FOREIGN KEY (shop_id) REFERENCES fruit_shop (shop_id),\n" +
                "    FOREIGN KEY (assigned_to) REFERENCES users (user_id)\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database version upgrade (if needed)
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS vendors");
        db.execSQL("DROP TABLE IF EXISTS customers");
        db.execSQL("DROP TABLE IF EXISTS guest");
        db.execSQL("DROP TABLE IF EXISTS fruit_shop");
        db.execSQL("DROP TABLE IF EXISTS shop_locations");
        db.execSQL("DROP TABLE IF EXISTS fruits");
        db.execSQL("DROP TABLE IF EXISTS fruit_shop_inventory");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS orders_items");
        db.execSQL("DROP TABLE IF EXISTS shop_reports");
        onCreate(db);
    }

    // --------------------------------------------------- Customer Side ------------------------------------------------------------


    // ------------------ Account Data -------------------------------------

    public String getPassword(String username) {
        // Get a readable database instance to perform the query
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;
        // Define the SQL query to retrieve the password from the user_account table based on the provided username
        String query = "SELECT password FROM users WHERE username = ?";
        // Specify the username as the selection argument for the query
        String[] selectionArgs = { username };
        // Execute the raw SQL query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        try {
            // Check if the cursor contains any results
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the password from the cursor
                password = cursor.getString(0);
            }
        } finally {
            // Close the cursor to release resources
            if (cursor != null) {
                cursor.close();
            }
        }
        // Return the retrieved password, or null if the username was not found in the database
        return password;
    }
    public String getRole(String username) {
        // Get a readable database instance to perform the query
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;
        // Define the SQL query to retrieve the password from the user_account table based on the provided username
        String query = "SELECT role FROM users WHERE username = ?";
        // Specify the username as the selection argument for the query
        String[] selectionArgs = { username };
        // Execute the raw SQL query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        try {
            // Check if the cursor contains any results
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the password from the cursor
                role = cursor.getString(0);
            }
        } finally {
            // Close the cursor to release resources
            if (cursor != null) {
                cursor.close();
            }
        }
        // Return the retrieved role, or null if the username was not found in the database
        return role;
    }

    public long getVendorId(String username) {
        // Get a readable database instance to perform the query
        SQLiteDatabase db = this.getReadableDatabase();
        long vendorID = -1;
        String query = "SELECT vendor_id FROM vendors WHERE username = ?";
        // Specify the username as the selection argument for the query
        String[] selectionArgs = { username };
        // Execute the raw SQL query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        try {
            // Check if the cursor contains any results
            if (cursor != null && cursor.moveToFirst()) {
                vendorID = cursor.getLong(0); // getLong(0) retrieves the first column, which is vendor_id
            }
        } finally {
            // Close the cursor to release resources
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return the retrieved vendor ID, or -1 if the username was not found in the database
        return vendorID;
    }



    public boolean isUsernameExists(String username) {
        // Get a readable database instance to perform the query
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists = false;

        // Define the SQL query to check if the username exists in the users table
        String query = "SELECT username FROM users WHERE username = ?";

        // Specify the username as the selection argument for the query
        String[] selectionArgs = { username };

        // Execute the raw SQL query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        try {
            // Check if the cursor contains any results
            if (cursor != null && cursor.moveToFirst()) {
                exists = true;  // Username exists
            }
        } finally {
            // Close the cursor to release resources
            if (cursor != null) {
                cursor.close();
            }
        }
        return exists;
    }


    public long insertUsers(Context context, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        String timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());

        try {
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("password", password);
            cv.put("role", role);
            cv.put("created_at", timestamp);

            // Insert and retrieve the vendorId of the new row
            long vendorId = db.insertOrThrow("users", null, cv);
            return vendorId;
        } catch (Exception e) {
            Toast.makeText(context, "Failed to insert user account: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        } finally {
            db.close();
        }
    }



    public boolean insertCustomerInfo(Context context, String username, String firstName, String middleName, String lastName, String extensionName,
                                      String dateOfBirth, String gender, String phoneNumber, String streetAddress, String barangay, String city,
                                      String province, String postalCode, byte[] profilePicture) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Prepare the data to be inserted
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("first_name", firstName);
            cv.put("middle_name", middleName);
            cv.put("last_name", lastName);
            cv.put("extension_name", extensionName);
            cv.put("date_of_birth", dateOfBirth);
            cv.put("gender", gender);
            cv.put("phone_number", phoneNumber);
            cv.put("street_address", streetAddress);
            cv.put("barangay", barangay);
            cv.put("city", city);
            cv.put("province", province);
            cv.put("postal_code", postalCode);
            cv.put("profile_picture", profilePicture);

            // Insert the data into the table
            long result = db.insertOrThrow("customers", null, cv);

            // Return true if insertion was successful
            return result != -1;
        } catch (Exception e) {
            // Log the error message
            Toast.makeText(context, "Failed to insert customer info: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Return false to indicate failure
            return false;
        } finally {
            // Close the database connection
            db.close();
        }
    }

    // --------------------------------------------------- Vendor Side ------------------------------------------------------------

    public boolean insertVendorInfo(Context context, String username, String firstName, String middleName, String lastName, String extensionName,
                                    String dateOfBirth, String gender, String mobileNumber, String streetAddress, String barangay, String city,
                                    String province, String postalCode, byte[] validId) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Prepare the data to be inserted
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("first_name", firstName);
            cv.put("middle_name", middleName);
            cv.put("last_name", lastName);
            cv.put("extension_name", extensionName);
            cv.put("date_of_birth", dateOfBirth);
            cv.put("gender", gender);
            cv.put("mobile_number", mobileNumber);
            cv.put("street_address", streetAddress);
            cv.put("barangay", barangay);
            cv.put("city", city);
            cv.put("province", province);
            cv.put("postal_code", postalCode);
            cv.put("valid_id", validId);  // Store as BLOB

            // Insert the data into the vendors table
            long result = db.insertOrThrow("vendors", null, cv);

            // Return true if insertion was successful
            return result != -1;
        } catch (Exception e) {
            // Log the error message
            Toast.makeText(context, "Failed to insert vendor info: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Return false to indicate failure
            return false;
        } finally {
            // Close the database connection
            db.close();
        }
    }

    public boolean insertFruitShopInfo(Context context, int vendorId, String shopName, String shopStreet, String shopBarangay,
                                       String shopCity, String shopProvince, String shopPostal, String mobileNumber,
                                       String telephoneNumber, String email, String description, String openingHours,
                                       String status, String immediateOrderPolicy, String advanceReservationPolicy,
                                       byte[] shopProfilePicture, byte[] dtiImage, byte[] birImage, byte[] shop_header_picture) {

        String registrationDate = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("vendor_id", vendorId);
            cv.put("shop_name", shopName);
            cv.put("shop_street", shopStreet);
            cv.put("shop_barangay", shopBarangay);
            cv.put("shop_city", shopCity);
            cv.put("shop_province", shopProvince);
            cv.put("shop_postal", shopPostal);
            cv.put("mobile_number", mobileNumber);
            cv.put("telephone_number", telephoneNumber);
            cv.put("email", email);
            cv.put("description", description);
            cv.put("registration_date", registrationDate);
            cv.put("opening_hours", openingHours);
            cv.put("status", status);
            cv.put("immediate_order_policy", immediateOrderPolicy);
            cv.put("advance_reservation_policy", advanceReservationPolicy);
            cv.put("Shop_profile_picture", shopProfilePicture);
            cv.put("shop_header_picture", shop_header_picture);  // Store as BLOB
            cv.put("dtiImage", dtiImage);
            cv.put("birImage", birImage);

            long result = db.insertOrThrow("fruit_shop", null, cv);

            return result != -1;
        } catch (Exception e) {
            Toast.makeText(context, "Failed to insert fruit shop info: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("VendorRegistration", "Failed to insert fruit shop info: " + e.getMessage());

            return false;
        } finally {
            db.close();
        }
    }

    // --------------------------------------------------- Vendor Profile Activity ------------------------------------------------------------

    public List<shopInfoViewModel> getFruitShopInfo(long vendorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<shopInfoViewModel> shopInfo = new ArrayList<>();

        // SQL query to select the required fields from the fruit_shop table where vendor_id matches
        String query = "SELECT shop_name, description, shop_street, shop_barangay, shop_city, shop_province, email, " +
                "mobile_number, telephone_number,opening_hours, " +
                "immediate_order_policy, advance_reservation_policy " +
                "FROM fruit_shop WHERE vendor_id = ?";
        String[] selectionArgs = { String.valueOf(vendorId) };  // Ensure vendorId is correctly converted to String
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Check if the cursor contains data and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve the relevant columns from the cursor
                String shopName = cursor.getString(cursor.getColumnIndexOrThrow("shop_name"));
                String shopDesc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String shopStreet = cursor.getString(cursor.getColumnIndexOrThrow("shop_street"));
                String shopBarangay = cursor.getString(cursor.getColumnIndexOrThrow("shop_barangay"));
                String shopCity = cursor.getString(cursor.getColumnIndexOrThrow("shop_city"));
                String shopProvince = cursor.getString(cursor.getColumnIndexOrThrow("shop_province"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String mobileNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobile_number"));
                String telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("telephone_number"));
                String openingHrs = cursor.getString(cursor.getColumnIndexOrThrow("opening_hours"));
                String immediateOrderPolicy = cursor.getString(cursor.getColumnIndexOrThrow("immediate_order_policy"));
                String advanceReservationPolicy = cursor.getString(cursor.getColumnIndexOrThrow("advance_reservation_policy"));

                // Create a shopInfoViewModel object with the retrieved data
                shopInfoViewModel shopInfos = new shopInfoViewModel(shopName, shopDesc, shopStreet, shopBarangay, shopCity,
                        shopProvince, email, mobileNumber, telephoneNumber,openingHrs,
                        immediateOrderPolicy, advanceReservationPolicy);

                // Add the shopInfos object to the list
                shopInfo.add(shopInfos);

            } while (cursor.moveToNext());

            // Close the cursor after retrieving all data
            cursor.close();
        }

        // Close the database connection
        db.close();

        // Return the list of shopInfo
        return shopInfo;
    }


    public List<vendorInfoViewModel> getVendorInfo(long vendorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<vendorInfoViewModel> vendorInfo = new ArrayList<>();

        // SQL query to select all fields from vendors table where vendor_id matches
        String query = "SELECT * FROM vendors WHERE vendor_id = ?";
        String[] selectionArgs = { String.valueOf(vendorId) };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Check if the cursor contains data and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve all the columns from the vendors table
                long vendorIdFromDb = cursor.getLong(cursor.getColumnIndexOrThrow("vendor_id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String middleName = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String extensionName = cursor.getString(cursor.getColumnIndexOrThrow("extension_name"));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String streetAddress = cursor.getString(cursor.getColumnIndexOrThrow("street_address"));
                String barangay = cursor.getString(cursor.getColumnIndexOrThrow("barangay"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                String province = cursor.getString(cursor.getColumnIndexOrThrow("province"));
                String postalCode = cursor.getString(cursor.getColumnIndexOrThrow("postal_code"));
                String mobileNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobile_number"));

                // Retrieve the valid ID as a BLOB
                byte[] validId = cursor.getBlob(cursor.getColumnIndexOrThrow("valid_id"));

                // Create a vendorInfoViewModel object with the retrieved data
                vendorInfoViewModel vendorInfos = new vendorInfoViewModel(
                        vendorIdFromDb, username, firstName, middleName, lastName, extensionName,
                        dateOfBirth, gender, streetAddress, barangay, city, province, postalCode,
                        mobileNumber, validId
                );

                // Add the vendorInfos object to the list
                vendorInfo.add(vendorInfos);

            } while (cursor.moveToNext());

            // Close the cursor after retrieving all data
            cursor.close();
        }

        // Close the database connection
        db.close();

        // Return the list of vendorInfo
        return vendorInfo;
    }


}
