package com.example.am_projekt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import com.example.am_projekt.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private final WeakReference<Context> weakContext; //Avoid memory leak


    private static final String DATABASE_NAME = "MultiApp_DB";
    private static final String CALCULATOR_TABLE_NAME = "Calculator_Results";
    private static final String CALCULATOR_UNSENT_TABLE_NAME = "Calculator_Unsent_Results";
    private static final String WEATHER_TABLE_NAME = "Weather_Results";
    private static final String WEATHER_UNSENT_TABLE_NAME = "Weather_Unsent_Results";

    private static final String ID_COLUMN = "id";
    private static final String USERNAME = "USERNAME";
    private static final String FIRST_NUMBER = "FIRST_NUMBER";
    private static final String SECOND_NUMBER = "SECOND_NUMBER";
    private static final String OPERATION = "OPERATION";
    private static final String RESULT = "RESULT";
    private static final String QUERIED_CITY = "QUERIED_CITY";
    private static final String TEMPERATURE = "TEMPERATURE";
    private static final String PRESSURE = "PRESSURE";
    private static final String HUMIDITY = "HUMIDITY";

    private static final String CREATE_CALCULATOR_TABLE = "CREATE TABLE " + CALCULATOR_TABLE_NAME
            + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + USERNAME + " TEXT, " + FIRST_NUMBER + " DECIMAL(10,10), " + SECOND_NUMBER
            + " DECIMAL(10,10), " + OPERATION + " TEXT, " + RESULT + " DECIMAL(10,10))";

    private static final String CREATE_CALCULATOR_UNSENT_TABLE = "CREATE TABLE " + CALCULATOR_UNSENT_TABLE_NAME
            + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + USERNAME + " TEXT, " + FIRST_NUMBER + " DECIMAL(10,10), " + SECOND_NUMBER
            + " DECIMAL(10,10), " + OPERATION + " TEXT, " + RESULT + " DECIMAL(10,10))";

    private static final String CREATE_WEATHER_TABLE = "CREATE TABLE " + WEATHER_TABLE_NAME
            + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + USERNAME + " TEXT, " + QUERIED_CITY + " TEXT, "
            + TEMPERATURE + " DECIMAL(3,2), " + PRESSURE + " INTEGER, " + HUMIDITY + " DECIMAL(3,2))";

    private static final String CREATE_WEATHER_UNSENT_TABLE = "CREATE TABLE " + WEATHER_UNSENT_TABLE_NAME
            + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + USERNAME + " TEXT, " + QUERIED_CITY + " TEXT, "
            + TEMPERATURE + " DECIMAL(3,2), " + PRESSURE + " INTEGER, " + HUMIDITY + " DECIMAL(3,2))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        weakContext = new WeakReference<>(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate called");
        sqLiteDatabase.execSQL(CREATE_CALCULATOR_TABLE);
        sqLiteDatabase.execSQL(CREATE_CALCULATOR_UNSENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_WEATHER_TABLE);
        sqLiteDatabase.execSQL(CREATE_WEATHER_UNSENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade called");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_UNSENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WEATHER_UNSENT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void dropCalculatorTable() {
        Log.d(TAG, "Dropping calculator table");
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_UNSENT_TABLE_NAME);
    }

    private void dropWeatherTable() {
        Log.d(TAG, "Dropping weather table");
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + WEATHER_UNSENT_TABLE_NAME);
    }

    private void createTables() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CALCULATOR_UNSENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WEATHER_UNSENT_TABLE_NAME);
    }

    public void restartTable() {
        dropCalculatorTable();
        dropWeatherTable();
        createTables();
        Log.i(TAG, "Database version: " + getWritableDatabase().getVersion());
    }

    public void addCalculatorItem(String username, float firstNumber, float secondNumber, String operation, float result) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = createCalculatorContentValues(username, firstNumber, secondNumber, operation, result);
        long insertingResult = database.insert(CALCULATOR_TABLE_NAME, null, contentValues);
        if (insertingResult == -1) {
            Log.e(TAG, "Inserting to database failed");
        }
    }

    public void addUnsentCalculatorItem(String username, float firstNumber, float secondNumber, String operation, float result) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = createCalculatorContentValues(username, firstNumber, secondNumber, operation, result);
        long insertingResult = database.insert(CALCULATOR_UNSENT_TABLE_NAME, null, contentValues);
        if (insertingResult == -1) {
            Log.e(TAG, "Inserting to database failed");
        }
    }

    public void deleteUnsentCalculatorItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + CALCULATOR_UNSENT_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";");
    }

    public void addWeatherItem(String username, String queriedCity, float temperature, float humidity, float pressure) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = createWeatherContentValues(username, queriedCity, temperature, humidity, pressure);
        long insertingResult = database.insert(WEATHER_TABLE_NAME, null, contentValues);
        if (insertingResult == -1) {
            Log.e(TAG, "Inserting to database failed");
        }
    }

    public void addUnsentWeatherItem(String username, String queriedCity, float temperature, float humidity, float pressure) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = createWeatherContentValues(username, queriedCity, temperature, humidity, pressure);
        long insertingResult = database.insert(WEATHER_UNSENT_TABLE_NAME, null, contentValues);
        if (insertingResult == -1) {
            Log.e(TAG, "Inserting to database failed");
        }
    }

    public void deleteUnsentWeatherItem(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + WEATHER_UNSENT_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";");
    }

    private ContentValues createCalculatorContentValues(String username, float firstNumber, float secondNumber, String operation, float result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(FIRST_NUMBER, firstNumber);
        contentValues.put(SECOND_NUMBER, secondNumber);
        contentValues.put(OPERATION, operation);
        contentValues.put(RESULT, result);
        return contentValues;
    }

    private ContentValues createWeatherContentValues(String username, String queriedCity, float temperature, float humidity, float pressure) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(QUERIED_CITY, queriedCity);
        contentValues.put(TEMPERATURE, temperature);
        contentValues.put(HUMIDITY, humidity);
        contentValues.put(PRESSURE, pressure);
        return contentValues;
    }

    public Cursor getCalculatorItems() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + CALCULATOR_TABLE_NAME;
        return database.rawQuery(query, null);
    }

    public Cursor getUnsentCalculatorItems() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + CALCULATOR_UNSENT_TABLE_NAME;
        return database.rawQuery(query, null);
    }

    public Cursor getWeatherItems() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + WEATHER_TABLE_NAME;
        return database.rawQuery(query, null);
    }

    public Cursor getUnsentWeatherItems() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + WEATHER_UNSENT_TABLE_NAME;
        return database.rawQuery(query, null);
    }

    private void makeToast(String text) {
        ((MainActivity) weakContext.get()).runOnUiThread(() -> Toast.makeText(weakContext.get(), text, Toast.LENGTH_SHORT).show());
    }
}
