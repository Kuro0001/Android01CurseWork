package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Туры в БД
 */
public class DBVouchers {
    public static final String TABLE_NAME = "vouchers";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "start_date";
    public static final String KEY_PRICE = "price";

    public static final String KEY_ID_TOUR = "id_tour";
    public static final String KEY_ID_EMPLOYEE = "id_employee";
    public static final String KEY_ID_CLIENT = "id_client";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_DATE + " text,"
            + KEY_PRICE + " real,"
            + KEY_ID_TOUR + " integer,"
            + KEY_ID_EMPLOYEE + " integer,"
            + KEY_ID_CLIENT + " integer"
            + ")";
}
