package com.example.citycyclerentalsnew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CityCycleDBNew";
    private static final int DATABASE_VERSION = 5;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BIKES = "bikes";
    private static final String TABLE_STATIONS = "stations";
    private static final String TABLE_RENTALS = "rentals";
    private static final String TABLE_PAYMENTS = "payments";
    private static final String TABLE_RESERVATIONS = "reservations";

    // Users Table
    private static final String COL_USER_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_PAYMENT_INFO = "payment_info";
    private static final String COL_ROLE = "role";
    private static final String COL_REG_DATE = "reg_date";

    // Bikes Table
    private static final String COL_BIKE_ID = "bike_id";
    private static final String COL_TYPE = "type";
    private static final String COL_STATION_ID_FK = "station_id";
    private static final String COL_AVAILABLE = "available";
    private static final String COL_ICON_URL = "icon_url";

    // Stations Table
    private static final String COL_STATION_ID = "station_id";
    private static final String COL_STATION_NAME = "name";
    private static final String COL_LOCATION = "location";
    private static final String COL_CAPACITY = "capacity";

    // Rentals Table
    private static final String COL_RENTAL_ID = "rental_id";
    private static final String COL_USER_ID_FK = "user_id";
    private static final String COL_BIKE_ID_FK = "bike_id";
    private static final String COL_START_TIME = "start_time";
    private static final String COL_END_TIME = "end_time";
    private static final String COL_DURATION = "duration";
    private static final String COL_STATUS = "status"; // "active", "completed"

    // Payments Table
    private static final String COL_PAYMENT_ID = "payment_id";
    private static final String COL_RENTAL_ID_FK = "rental_id";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_PAYMENT_DATE = "payment_date";
    private static final String COL_PAYMENT_STATUS = "payment_status"; // "pending", "paid"

    // Reservations Table
    private static final String COL_RESERVATION_ID = "reservation_id";
    private static final String COL_USER_ID_FK_RES = "user_id";
    private static final String COL_BIKE_ID_FK_RES = "bike_id";
    private static final String COL_RESERVE_TIME = "reserve_time";
    private static final String COL_EXPIRY_TIME = "expiry_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_PAYMENT_INFO + " TEXT, " +
                COL_ROLE + " TEXT DEFAULT 'user', " +
                COL_REG_DATE + " TEXT)");

        // Stations Table
        db.execSQL("CREATE TABLE " + TABLE_STATIONS + " (" +
                COL_STATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STATION_NAME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CAPACITY + " INTEGER)");

        // Bikes Table
        db.execSQL("CREATE TABLE " + TABLE_BIKES + " (" +
                COL_BIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE + " TEXT, " +
                COL_STATION_ID_FK + " INTEGER, " +
                COL_AVAILABLE + " INTEGER, " +
                COL_ICON_URL + " TEXT, " +
                "FOREIGN KEY(" + COL_STATION_ID_FK + ") REFERENCES " + TABLE_STATIONS + "(" + COL_STATION_ID + "))");

        // Rentals Table
        db.execSQL("CREATE TABLE " + TABLE_RENTALS + " (" +
                COL_RENTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_BIKE_ID_FK + " INTEGER, " +
                COL_START_TIME + " TEXT, " +
                COL_END_TIME + " TEXT, " +
                COL_DURATION + " INTEGER, " +
                COL_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), " +
                "FOREIGN KEY(" + COL_BIKE_ID_FK + ") REFERENCES " + TABLE_BIKES + "(" + COL_BIKE_ID + "))");

        // Payments Table
        db.execSQL("CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COL_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RENTAL_ID_FK + " INTEGER, " +
                COL_AMOUNT + " REAL, " +
                COL_PAYMENT_DATE + " TEXT, " +
                COL_PAYMENT_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COL_RENTAL_ID_FK + ") REFERENCES " + TABLE_RENTALS + "(" + COL_RENTAL_ID + "))");

        // Reservations Table
        db.execSQL("CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                COL_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK_RES + " INTEGER, " +
                COL_BIKE_ID_FK_RES + " INTEGER, " +
                COL_RESERVE_TIME + " TEXT, " +
                COL_EXPIRY_TIME + " TEXT, " +
                "FOREIGN KEY(" + COL_USER_ID_FK_RES + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), " +
                "FOREIGN KEY(" + COL_BIKE_ID_FK_RES + ") REFERENCES " + TABLE_BIKES + "(" + COL_BIKE_ID + "))");

        // Sample Data
        // Insert Sample Data
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insert 10 Users
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_USERS + " (" + COL_EMAIL + ", " + COL_PASSWORD + ", " + COL_NAME + ", " + COL_PHONE + ", " + COL_ROLE + ", " + COL_REG_DATE + ") " +
                    "VALUES ('user" + i + "@example.com', 'password" + i + "', 'User " + i + "', '123456789" + i + "', 'user', '2023-01-0" + i + "')");
        }

        // Insert 10 Stations
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_STATIONS + " (" + COL_STATION_NAME + ", " + COL_LOCATION + ", " + COL_CAPACITY + ") " +
                    "VALUES ('Station " + i + "', 'Location " + i + "', " + (10 + i) + ")");
        }

        // Insert 10 Bikes with the same image URL
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                    "VALUES ('" + (i % 2 == 0 ? "Mountain" : "City") + "', " + i + ", 1, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");
        }

// New bikes with the same image URL
        db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                "VALUES ('Road', 11, 1, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");

        db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                "VALUES ('Electric', 12, 0, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");

        db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                "VALUES ('Hybrid', 13, 1, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");

        db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                "VALUES ('Mountain', 14, 0, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");

        db.execSQL("INSERT INTO " + TABLE_BIKES + " (" + COL_TYPE + ", " + COL_STATION_ID_FK + ", " + COL_AVAILABLE + ", " + COL_ICON_URL + ") " +
                "VALUES ('City', 15, 1, 'https://images.pexels.com/photos/210095/pexels-photo-210095.jpeg')");

        // Insert 10 Rentals
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_RENTALS + " (" + COL_USER_ID_FK + ", " + COL_BIKE_ID_FK + ", " + COL_START_TIME + ", " + COL_END_TIME + ", " + COL_DURATION + ", " + COL_STATUS + ") " +
                    "VALUES (" + i + ", " + i + ", '2023-01-0" + i + " 10:00:00', '2023-01-0" + i + " 12:00:00', 120, 'completed')");
        }

        // Insert 10 Payments
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_PAYMENTS + " (" + COL_RENTAL_ID_FK + ", " + COL_AMOUNT + ", " + COL_PAYMENT_DATE + ", " + COL_PAYMENT_STATUS + ") " +
                    "VALUES (" + i + ", " + (10.0 * i) + ", '2023-01-0" + i + " 12:00:00', 'paid')");
        }

        // Insert 10 Reservations
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO " + TABLE_RESERVATIONS + " (" + COL_USER_ID_FK_RES + ", " + COL_BIKE_ID_FK_RES + ", " + COL_RESERVE_TIME + ", " + COL_EXPIRY_TIME + ") " +
                    "VALUES (" + i + ", " + i + ", '2023-01-0" + i + " 09:00:00', '2023-01-0" + i + " 10:00:00')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    // User Operations
    public boolean registerUser(String email, String password, String name, String phone, String paymentInfo, String role, String regDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_PAYMENT_INFO, paymentInfo);
        values.put(COL_ROLE, role);
        values.put(COL_REG_DATE, regDate);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Bike and Station Operations
    public Cursor getAllStations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STATIONS, null);
    }

    public Cursor getAvailableBikes(String stationFilter) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (stationFilter.isEmpty()) {
            return db.rawQuery("SELECT b.*, s.name FROM " + TABLE_BIKES + " b JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " WHERE b." + COL_AVAILABLE + "=1", null);
        } else {
            return db.rawQuery("SELECT b.*, s.name FROM " + TABLE_BIKES + " b JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " WHERE b." + COL_AVAILABLE + "=1 AND s." + COL_STATION_NAME + "=?", new String[]{stationFilter});
        }
    }

    public Cursor getAllBikes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT b.*, s.name FROM " + TABLE_BIKES + " b JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID, null);
    }

    public boolean addBike(String type, int stationId, String iconUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_STATION_ID_FK, stationId);
        values.put(COL_AVAILABLE, 1);
        values.put(COL_ICON_URL, iconUrl);
        long result = db.insert(TABLE_BIKES, null, values);
        return result != -1;
    }

    public boolean updateBike(int bikeId, String type, int stationId, int available, String iconUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_STATION_ID_FK, stationId);
        values.put(COL_AVAILABLE, available);
        values.put(COL_ICON_URL, iconUrl);
        int rows = db.update(TABLE_BIKES, values, COL_BIKE_ID + "=?", new String[]{String.valueOf(bikeId)});
        return rows > 0;
    }

    public boolean deleteBike(int bikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BIKES, COL_BIKE_ID + "=?", new String[]{String.valueOf(bikeId)});
        return rows > 0;
    }

    // Reservation Operations
    public boolean reserveBike(int userId, int bikeId, String reserveTime, String expiryTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK_RES, userId);
        values.put(COL_BIKE_ID_FK_RES, bikeId);
        values.put(COL_RESERVE_TIME, reserveTime);
        values.put(COL_EXPIRY_TIME, expiryTime);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        if (result != -1) {
            ContentValues bikeValues = new ContentValues();
            bikeValues.put(COL_AVAILABLE, 0);
            db.update(TABLE_BIKES, bikeValues, COL_BIKE_ID + "=?", new String[]{String.valueOf(bikeId)});
            return true;
        }
        return false;
    }

    public Cursor getActiveReservation(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " + COL_USER_ID_FK_RES + "=? AND " + COL_EXPIRY_TIME + ">datetime('now')", new String[]{String.valueOf(userId)});
    }

    // Rental Operations
    public boolean startRental(int userId, int bikeId, String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userId);
        values.put(COL_BIKE_ID_FK, bikeId);
        values.put(COL_START_TIME, startTime);
        values.put(COL_STATUS, "active");
        long result = db.insert(TABLE_RENTALS, null, values);
        if (result != -1) {
            db.delete(TABLE_RESERVATIONS, COL_USER_ID_FK_RES + "=? AND " + COL_BIKE_ID_FK_RES + "=?", new String[]{String.valueOf(userId), String.valueOf(bikeId)});
            return true;
        }
        return false;
    }

    public Cursor getActiveRental(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT r.*, b.type, s.name FROM " + TABLE_RENTALS + " r JOIN " + TABLE_BIKES + " b ON r." + COL_BIKE_ID_FK + "=b." + COL_BIKE_ID + " JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " WHERE r." + COL_USER_ID_FK + "=? AND r." + COL_STATUS + "='active'", new String[]{String.valueOf(userId)});
    }

    public boolean endRental(int rentalId, String endTime, int duration, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rentalValues = new ContentValues();
        rentalValues.put(COL_END_TIME, endTime);
        rentalValues.put(COL_DURATION, duration);
        rentalValues.put(COL_STATUS, "completed");
        int rows = db.update(TABLE_RENTALS, rentalValues, COL_RENTAL_ID + "=?", new String[]{String.valueOf(rentalId)});
        if (rows > 0) {
            Cursor cursor = db.rawQuery("SELECT " + COL_BIKE_ID_FK + " FROM " + TABLE_RENTALS + " WHERE " + COL_RENTAL_ID + "=?", new String[]{String.valueOf(rentalId)});
            if (cursor.moveToFirst()) {
                int bikeId = cursor.getInt(0);
                ContentValues bikeValues = new ContentValues();
                bikeValues.put(COL_AVAILABLE, 1);
                db.update(TABLE_BIKES, bikeValues, COL_BIKE_ID + "=?", new String[]{String.valueOf(bikeId)});
            }
            cursor.close();

            ContentValues paymentValues = new ContentValues();
            paymentValues.put(COL_RENTAL_ID_FK, rentalId);
            paymentValues.put(COL_AMOUNT, amount);
            paymentValues.put(COL_PAYMENT_DATE, endTime);
            paymentValues.put(COL_PAYMENT_STATUS, "pending");
            db.insert(TABLE_PAYMENTS, null, paymentValues);
            return true;
        }
        return false;
    }

    public Cursor getAllRentals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT r.*, b.type, s.name, u.name AS user_name FROM " + TABLE_RENTALS + " r JOIN " + TABLE_BIKES + " b ON r." + COL_BIKE_ID_FK + "=b." + COL_BIKE_ID + " JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " JOIN " + TABLE_USERS + " u ON r." + COL_USER_ID_FK + "=u." + COL_USER_ID, null);
    }

    public Cursor getRentalHistory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT r.*, b.type, s.name, p.amount FROM " + TABLE_RENTALS + " r JOIN " + TABLE_BIKES + " b ON r." + COL_BIKE_ID_FK + "=b." + COL_BIKE_ID + " JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " LEFT JOIN " + TABLE_PAYMENTS + " p ON r." + COL_RENTAL_ID + "=p." + COL_RENTAL_ID_FK + " WHERE r." + COL_USER_ID_FK + "=?", new String[]{String.valueOf(userId)});
    }

    // Payment Operations
    public Cursor getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT p.*, r.start_time, r.end_time, b.type, s.name, u.name AS user_name FROM " + TABLE_PAYMENTS + " p JOIN " + TABLE_RENTALS + " r ON p." + COL_RENTAL_ID_FK + "=r." + COL_RENTAL_ID + " JOIN " + TABLE_BIKES + " b ON r." + COL_BIKE_ID_FK + "=b." + COL_BIKE_ID + " JOIN " + TABLE_STATIONS + " s ON b." + COL_STATION_ID_FK + "=s." + COL_STATION_ID + " JOIN " + TABLE_USERS + " u ON r." + COL_USER_ID_FK + "=u." + COL_USER_ID, null);
    }

    public boolean updatePaymentStatus(int paymentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PAYMENT_STATUS, status);
        int rows = db.update(TABLE_PAYMENTS, values, COL_PAYMENT_ID + "=?", new String[]{String.valueOf(paymentId)});
        return rows > 0;
    }
}