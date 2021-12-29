package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Виды туров в БД
 */
public class DBKinds {
    public static final String TABLE_NAME = "kinds";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text"
            + ")";
}
