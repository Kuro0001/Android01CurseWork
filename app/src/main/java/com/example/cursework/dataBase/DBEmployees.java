package com.example.cursework.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cursework.R;

/**
 * Класс представления таблицы Сотрудники в БД
 */
public class DBEmployees {
//    public static final String tagDB = "tagDB.employees";
    public static final String tagDB = "tagDB";
    public static final String TABLE_NAME = "employees";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";
    public static final String KEY_MAIL = "mail";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public static int id = 0;
    public static String name = "name_admin";
    public static String surname = "surname_admin";
    public static String patronymic = "patronymic_admin";
    public static String mail = "mail-admin";
    public static String login = "login_Admin_01";
    public static String password = "password_Admin_01";

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text,"
            + KEY_SURNAME + " text,"
            + KEY_PATRONYMIC + " text,"
            + KEY_MAIL + " text,"
            + KEY_LOGIN + " text,"
            + KEY_PASSWORD + " text"
            + ")";

    /**
     * Конструктор
     */
    public DBEmployees() {
    }

    /**
     * Конструктор
     * @param id
     * @param name
     * @param surname
     * @param patronymic
     * @param mail
     * @param login
     * @param password
     */
    public DBEmployees(int id, String name, String surname, String patronymic, String mail, String login, String password) {
        Log.d(tagDB, "Вызов вызов конструктора класса DBEmployees с параментрами");
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.mail = mail;
        this.login = login;
        this.password = password;
    }

//    public static ContentValues setContent(int id, String name, String surname, String patronymic, String mail, String login, String password){
//        ContentValues contentValues = setContent(name,surname, patronymic,mail,login,password);
//        contentValues.put(KEY_ID,id);
//        return contentValues;
//
//    }
//    public static ContentValues setContent(String name, String surname, String patronymic, String mail, String login, String password){
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(KEY_NAME, name);
//        contentValues.put(KEY_SURNAME, surname);
//        contentValues.put(KEY_PATRONYMIC, patronymic);
//        contentValues.put(KEY_MAIL, mail);
//        contentValues.put(KEY_LOGIN, login);
//        contentValues.put(KEY_PASSWORD, password);
//
//        return contentValues;
//    }
//    public static ContentValues getContent(){
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(KEY_ID, id);
//        contentValues.put(KEY_NAME, name);
//        contentValues.put(KEY_SURNAME, surname);
//        contentValues.put(KEY_PATRONYMIC, patronymic);
//        contentValues.put(KEY_MAIL, mail);
//        contentValues.put(KEY_LOGIN, login);
//        contentValues.put(KEY_PASSWORD, password);
//
//        return contentValues;
//    }

    /**
     * Метод добавления элемента в БД
     * @param db
     * @param dbHelper
     * @return
     */
    public long add(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBEmployees.add");
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SURNAME, surname);
        contentValues.put(KEY_PATRONYMIC, patronymic);
        contentValues.put(KEY_MAIL, mail);
        contentValues.put(KEY_LOGIN, login);
        contentValues.put(KEY_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

//    public long add(SQLiteDatabase db, DBHelper dbHelper) {
//        Log.d(tagDB, "Вызов метода DBEmployees.add");
//        db = dbHelper.getReadableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(KEY_NAME, name);
//        contentValues.put(KEY_SURNAME, surname);
//        contentValues.put(KEY_PATRONYMIC, patronymic);
//        contentValues.put(KEY_MAIL, mail);
//        contentValues.put(KEY_LOGIN, login);
//        contentValues.put(KEY_PASSWORD, password);
//
//        long result = db.insert(TABLE_NAME, null, contentValues);
//        return result;
//    }

    /**
     * Метод изменения элемента в БД
     * @param db
     * @param dbHelper
     * @return
     */
    public int edit(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBEmployees.edit( " + id + " )");
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                null,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, name);
            contentValues.put(KEY_SURNAME, surname);
            contentValues.put(KEY_PATRONYMIC, patronymic);
            contentValues.put(KEY_MAIL, mail);
            contentValues.put(KEY_LOGIN, login);
            contentValues.put(KEY_PASSWORD, password);

            String where = KEY_ID + "=" + cursor.getInt(id);

            return db.update(TABLE_NAME, contentValues, where, null);
        }
        return 0;
    }

    /**
     * Метод удаления элемента в БД
     * @param db
     * @param dbHelper
     * @return
     */
    public int delete(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBEmployees.delete( " + id + " )");
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor.moveToFirst() == true){
            return db.delete(TABLE_NAME, KEY_ID +" = ?", new String[] {String.valueOf(id)});
        }
        return 0;
    }
}
