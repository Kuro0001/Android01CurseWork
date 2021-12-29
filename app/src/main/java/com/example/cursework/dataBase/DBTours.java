package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Туры в БД
 */
public class DBTours {
    public static final String TABLE_NAME = "tours";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_DAYS_COUNT = "days_count";
    public static final String KEY_OFFERS_ALL = "offers_all";
    public static final String KEY_PRICE = "price";

    public static final String KEY_ID_TOUR_OPERATOR = "id_tour_operator";
    public static final String KEY_ID_KIND = "id_kind";
    public static final String KEY_ID_CATEGORY = "id_category";
    public static final String KEY_ID_HOTEL = "id_hotel";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_START_DATE + " text,"
            + KEY_DAYS_COUNT + " integer,"
            + KEY_OFFERS_ALL + " integer,"
            + KEY_PRICE + " real,"
            + KEY_ID_TOUR_OPERATOR + " integer,"
            + KEY_ID_KIND + " integer,"
            + KEY_ID_CATEGORY + " integer,"
            + KEY_ID_HOTEL + " integer"
            + ")";
}
