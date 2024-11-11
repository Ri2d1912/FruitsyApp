package com.example.fruitidentification.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
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
                "    valid_id TEXT,\n" +
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
                "    profile_picture TEXT,\n" +
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
                "    address TEXT,\n" +
                "    mobile_number TEXT,\n" +
                "    telephone_number TEXT,\n" +
                "    email TEXT,\n" +
                "    description TEXT,\n" +
                "    registration_date DATE,\n" +
                "    opening_hours TEXT,\n" +
                "    status TEXT CHECK(status IN ('open', 'closed')),\n" +
                "    immediate_order_policy TEXT CHECK(immediate_order_policy IN ('Payment Upon Pickup', 'Deposit Required', 'Full Payment in Advance', 'Flexible')),\n" +
                "    advance_reservation_policy TEXT CHECK(advance_reservation_policy IN ('Payment Upon Pickup', 'Deposit Required', 'Full Payment in Advance', 'Flexible')),\n" +
                "    profile_picture TEXT,\n" +
                "    shop_picture TEXT,\n" +
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

    public boolean insertUsers(Context context, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Format the current date and time in the desired format
        String timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());

        try {
            // Prepare the data to be inserted
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("password", password);
            cv.put("role", role);
            cv.put("created_at", timestamp);  // Use "created_at" to match the column name in onCreate

            // Insert the data into the table
            long result = db.insertOrThrow("users", null, cv);

            // If we reach this point, insertion was successful
            return result != -1; // Inserting returns -1 on failure, so we check for success
        } catch (Exception e) {
            // Log the error message
            Toast.makeText(context, "Failed to insert user account: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } finally {
            // Close the database to release resources
            if (db != null) {
                db.close();
            }
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



}
