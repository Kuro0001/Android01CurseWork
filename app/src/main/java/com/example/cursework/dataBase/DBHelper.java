package com.example.cursework.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Класс для работы с БД
 * наследует функционал класса SQLiteOpenHelper
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String tagDB = "tagDB";
    public static final int DATABASE_VERSION = 17;
    public static final String DATABASE_NAME = "tourist_agency";

    public static final String TABLE_NAME_DIRECTIONS = "directions";
    public static final String KEY_DIRECTIONS_ID = "_id";
    public static final String KEY_DIRECTIONS_NAME = "name";

    public static final String TABLE_NAME_HOTELS = "hotels";
    public static final String KEY_HOTELS_ID = "_id";
    public static final String KEY_HOTELS_NAME = "name";
    public static final String KEY_HOTELS_ADDRESS = "address";
    public static final String KEY_HOTELS_ID_DIRECTION= "direction_id";

    public static final String TABLE_NAME_KINDS = "kinds";
    public static final String KEY_KINDS_ID = "_id";
    public static final String KEY_KINDS_NAME = "name";

    public static final String TABLE_NAME_CATEGORIES = "categories";
    public static final String KEY_CATEGORIES_ID = "_id";
    public static final String KEY_CATEGORIES_NAME = "name";
    public static final String KEY_CATEGORIES_ADDED_VALUE = "added_value";
    public static final String KEY_CATEGORIES_DISCOUNT = "discount";

    public static final String TABLE_NAME_TOUR_OPERATORS = "tour_operators";
    public static final String KEY_TOUR_OPERATORS_ID = "_id";
    public static final String KEY_TOUR_OPERATORS_NAME = "name";
    public static final String KEY_TOUR_OPERATORS_PHONE = "phone";
    public static final String KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER = "unique_company_number";
    public static final String KEY_TOUR_OPERATORS_MAIL = "mail";

    public static final String TABLE_NAME_EMPLOYEES = "employees";
    public static final String KEY_EMPLOYEES_ID = "_id";
    public static final String KEY_EMPLOYEES_NAME = "name";
    public static final String KEY_EMPLOYEES_SURNAME = "surname";
    public static final String KEY_EMPLOYEES_PATRONYMIC = "patronymic";
    public static final String KEY_EMPLOYEES_MAIL = "mail";
    public static final String KEY_EMPLOYEES_LOGIN = "login";
    public static final String KEY_EMPLOYEES_PASSWORD = "password";

    public static final String TABLE_NAME_CLIENTS = "clients";
    public static final String KEY_CLIENTS_ID = "_id";
    public static final String KEY_CLIENTS_NAME = "name";
    public static final String KEY_CLIENTS_SURNAME = "surname";
    public static final String KEY_CLIENTS_PATRONYMIC = "patronymic";
    public static final String KEY_CLIENTS_BIRTHDATE = "birthdate";
    public static final String KEY_CLIENTS_SEX = "sex";
    public static final String KEY_CLIENTS_PHONE = "phone";
    public static final String KEY_CLIENTS_MAIL = "mail";
    public static final String KEY_CLIENTS_PASSPORT = "passport";

    public static final String TABLE_NAME_TOURS = "tours";
    public static final String KEY_TOURS_ID = "_id";
    public static final String KEY_TOURS_NAME = "name";
    public static final String KEY_TOURS_START_DATE = "start_date";
    public static final String KEY_TOURS_DAYS_COUNT = "days_count";
    public static final String KEY_TOURS_OFFERS_ALL = "offers_all";
    public static final String KEY_TOURS_PRICE = "price";
    public static final String KEY_TOURS_ID_TOUR_OPERATOR = "id_tour_operator";
    public static final String KEY_TOURS_ID_KIND = "id_kind";
    public static final String KEY_TOURS_ID_CATEGORY = "id_category";
    public static final String KEY_TOURS_ID_HOTEL = "id_hotel";

    public static final String TABLE_NAME_VOUCHERS = "vouchers";
    public static final String KEY_VOUCHERS_ID = "_id";
    public static final String KEY_VOUCHERS_NAME = "name";
    public static final String KEY_VOUCHERS_DATE = "start_date";
    public static final String KEY_VOUCHERS_PRICE = "price";
    public static final String KEY_VOUCHERS_ID_TOUR = "id_tour";
    public static final String KEY_VOUCHERS_ID_EMPLOYEE = "id_employee";
    public static final String KEY_VOUCHERS_ID_CLIENT = "id_client";

    /**
     * Конструктор
     * @param context
     */
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(tagDB, "Вызов конструктора DbHelper");
    }

    /**
     * Метод создания базы данных (создание таблиц)
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tagDB, "Вызов метода onCreate класса DBHelper");
        db.execSQL("create table "  + TABLE_NAME_DIRECTIONS
                + "("
                + KEY_DIRECTIONS_ID + " integer primary key autoincrement,"
                + KEY_DIRECTIONS_NAME + " text"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_HOTELS
                + "("
                + KEY_HOTELS_ID + " integer primary key autoincrement,"
                + KEY_HOTELS_NAME + " text,"
                + KEY_HOTELS_ADDRESS + " text,"
                + KEY_HOTELS_ID_DIRECTION + " integer"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_KINDS
                + "("
                + KEY_KINDS_ID + " integer primary key autoincrement,"
                + KEY_KINDS_NAME + " text"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_CATEGORIES
                + "("
                + KEY_CATEGORIES_ID + " integer primary key autoincrement,"
                + KEY_CATEGORIES_NAME + " text,"
                + KEY_CATEGORIES_ADDED_VALUE + " real,"
                + KEY_CATEGORIES_DISCOUNT + " real"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_TOUR_OPERATORS
                + "("
                + KEY_TOUR_OPERATORS_ID + " integer primary key autoincrement,"
                + KEY_TOUR_OPERATORS_NAME + " text,"
                + KEY_TOUR_OPERATORS_PHONE + " text,"
                + KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER + " text,"
                + KEY_TOUR_OPERATORS_MAIL + " text"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_EMPLOYEES
                + "("
                + KEY_EMPLOYEES_ID + " integer primary key autoincrement,"
                + KEY_EMPLOYEES_NAME + " text,"
                + KEY_EMPLOYEES_SURNAME + " text,"
                + KEY_EMPLOYEES_PATRONYMIC + " text,"
                + KEY_EMPLOYEES_MAIL + " text,"
                + KEY_EMPLOYEES_LOGIN + " text,"
                + KEY_EMPLOYEES_PASSWORD + " text"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_CLIENTS
                + "("
                + KEY_CLIENTS_ID + " integer primary key autoincrement,"
                + KEY_CLIENTS_NAME + " text,"
                + KEY_CLIENTS_SURNAME + " text,"
                + KEY_CLIENTS_PATRONYMIC + " text,"
                + KEY_CLIENTS_BIRTHDATE + " integer,"
                + KEY_CLIENTS_SEX + " text,"
                + KEY_CLIENTS_PHONE + " text,"
                + KEY_CLIENTS_MAIL + " text,"
                + KEY_CLIENTS_PASSPORT + " text"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_TOURS
                + "("
                + KEY_TOURS_ID + " integer primary key autoincrement,"
                + KEY_TOURS_NAME + " text,"
                + KEY_TOURS_START_DATE + " integer,"
                + KEY_TOURS_DAYS_COUNT + " integer,"
                + KEY_TOURS_OFFERS_ALL + " integer,"
                + KEY_TOURS_PRICE + " real,"
                + KEY_TOURS_ID_TOUR_OPERATOR + " integer,"
                + KEY_TOURS_ID_KIND + " integer,"
                + KEY_TOURS_ID_CATEGORY + " integer,"
                + KEY_TOURS_ID_HOTEL + " integer"
                + ")");
        db.execSQL("create table "  + TABLE_NAME_VOUCHERS
                + "("
                + KEY_VOUCHERS_ID + " integer primary key autoincrement,"
                + KEY_VOUCHERS_NAME + " text,"
                + KEY_VOUCHERS_DATE + " integer,"
                + KEY_VOUCHERS_PRICE + " real,"
                + KEY_VOUCHERS_ID_TOUR + " integer,"
                + KEY_VOUCHERS_ID_EMPLOYEE + " integer,"
                + KEY_VOUCHERS_ID_CLIENT + " integer"
                + ")");
    }

    /**
     * Метод обновления базы данных (удаление существующих таблиц и создание заного)
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(tagDB, "Вызов метода onUpgrade класса DBHelper");
        db.execSQL("drop table if exists " + TABLE_NAME_DIRECTIONS);
        db.execSQL("drop table if exists " + TABLE_NAME_HOTELS);
        db.execSQL("drop table if exists " + TABLE_NAME_KINDS);
        db.execSQL("drop table if exists " + TABLE_NAME_CATEGORIES);
        db.execSQL("drop table if exists " + TABLE_NAME_TOUR_OPERATORS);
        db.execSQL("drop table if exists " + TABLE_NAME_EMPLOYEES);
        db.execSQL("drop table if exists " + TABLE_NAME_CLIENTS);
        db.execSQL("drop table if exists " + TABLE_NAME_TOURS);
        db.execSQL("drop table if exists " + TABLE_NAME_VOUCHERS);
        onCreate(db);
    }
}
