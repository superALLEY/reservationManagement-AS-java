package com.example.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and Table Information
    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RESERVATIONS = "reservations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PEOPLE_COUNT = "people_count";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_TOTAL_DAYS = "total_days";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_SERVICES = "services";

    // SQL to create the reservations table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PEOPLE_COUNT + " INTEGER, " +
            COLUMN_START_DATE + " TEXT, " +
            COLUMN_END_DATE + " TEXT, " +
            COLUMN_TOTAL_DAYS + " INTEGER, " +
            COLUMN_TOTAL_PRICE + " INTEGER, " +
            COLUMN_SERVICES + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }

    // Insert a new reservation
    public long insertReservation(String name, String email, int peopleCount, String startDate, String endDate, int totalDays, int totalPrice, String services) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PEOPLE_COUNT, peopleCount);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_TOTAL_DAYS, totalDays);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_SERVICES, services);

        long id = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return id;
    }

    // Retrieve a reservation by ID
    public Reservation getReservationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RESERVATIONS,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Reservation reservation = new Reservation(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEOPLE_COUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_DAYS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICES))
            );
            cursor.close();
            return reservation;
        }

        return null; // Return null if no reservation found
    }
    public void clearReservationsTable() {
        SQLiteDatabase db = this.getWritableDatabase(); // Ouvre la base de données en mode écriture
        db.delete(TABLE_RESERVATIONS, null, null); // Supprime toutes les lignes de la table
        db.close(); // Ferme la base de données
    }
    public Reservation getReservationByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RESERVATIONS,
                null,
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Reservation reservation = new Reservation(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEOPLE_COUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_DAYS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICES))
            );
            cursor.close();
            return reservation;
        }

        return null; // Return null if no reservation found
    }


    // Optional: Retrieve all reservations (for future use or debugging)
    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_RESERVATIONS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " ASC"
        );
    }

    // Optional: Delete a reservation by ID
    public boolean deleteReservation(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RESERVATIONS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Optional: Update a reservation by ID
    public boolean updateReservation(long id, String name, String email, int peopleCount, String startDate, String endDate, int totalDays, int totalPrice, String services) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PEOPLE_COUNT, peopleCount);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_TOTAL_DAYS, totalDays);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_SERVICES, services);

        int rowsUpdated = db.update(TABLE_RESERVATIONS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated > 0;
    }
    public List<Reservation> getAllReservationsList() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, null, null, null, null, COLUMN_ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                reservations.add(new Reservation(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEOPLE_COUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_DAYS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICES))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return reservations;
    }

}
