package com.example.fruitidentification.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "    email TEXT,\n" +
                "    role TEXT CHECK(role IN ('admin', 'vendor', 'customer', 'guest')),\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");");

        db.execSQL("CREATE TABLE vendors (\n" +
                "    vendor_id INTEGER PRIMARY KEY,\n" +
                "    first_name TEXT NOT NULL,\n" +
                "    middle_name TEXT,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    extension_name TEXT,\n" +
                "    date_of_birth DATE,\n" +
                "    gender TEXT CHECK(gender IN ('male', 'female', 'other')),\n" +
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
                "    first_name TEXT NOT NULL,\n" +
                "    middle_name TEXT,\n" +
                "    last_name TEXT NOT NULL,\n" +
                "    extension_name TEXT,\n" +
                "    date_of_birth DATE,\n" +
                "    gender TEXT CHECK(gender IN ('male', 'female', 'other')),\n" +
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

}
