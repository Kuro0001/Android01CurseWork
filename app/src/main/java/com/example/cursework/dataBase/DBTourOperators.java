package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Туроператоры в БД
 */
public class DBTourOperators {
    public static final String TABLE_NAME = "tour_operators";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_UNIQUE_COMPANY_NUMBER = "unique_company_number";
    public static final String KEY_MAIL = "mail";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_PHONE + " text,"
            + KEY_UNIQUE_COMPANY_NUMBER + " text,"
            + KEY_MAIL + " text"
            + ")";
}
