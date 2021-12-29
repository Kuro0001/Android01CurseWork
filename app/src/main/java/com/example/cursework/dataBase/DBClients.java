package com.example.cursework.dataBase;

/**
 * Класс представления таблицы Клиенты в БД
 */
public class DBClients {
    public static final String TABLE_NAME = "clients";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";
    public static final String KEY_BIRTHDATE = "birthdate";
    public static final String KEY_SEX = "sex";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_MAIL = "mail";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_SURNAME + " text,"
            + KEY_PATRONYMIC + " text,"
            + KEY_BIRTHDATE + " text,"
            + KEY_SEX + " text,"
            + KEY_PHONE + " text,"
            + KEY_MAIL + " text,"
            + ")";
}
