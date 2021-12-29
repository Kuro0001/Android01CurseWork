package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Отели в БД
 */
public class DBHotels {
    public static final String TABLE_NAME = "hotels";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ID_DIRECTION = "direction_id";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_ADDRESS + " text,"
            + KEY_ID_DIRECTION + " integer"
            + ")";

//    public static final String queryCreateTable = TABLE_NAME
//            + "("
//            + KEY_ID + " integer primary key,"
//            + KEY_NAME + " text,"
//            + KEY_ADDRESS + " text,"
//            + KEY_ID_DIRECTION + " integer,"
//            + KEY_ID_DIRECTION + " FOREIGN KEY " + DBDirections.KEY_ID + " REFERENCES"
//            + ")";
}
