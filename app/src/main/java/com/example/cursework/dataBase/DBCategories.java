package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Отели в БД
 */
public class DBCategories {
    public static final String TABLE_NAME = "categories";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDED_VALUE = "added_value";
    public static final String KEY_DISCOUNT = "discount";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_ADDED_VALUE + " real,"
            + KEY_DISCOUNT + " real"
            + ")";
}
