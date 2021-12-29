package com.example.cursework.dataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Класс представления таблицы Направления в БД
 */
public class DBDirections {
    public static final String tagDB = "tagDB";
    public static final String TABLE_NAME = "directions";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public static int id = 0;
    public static String name = "direction_0";

    public DBDirections(){

    }
    /**
     * Конструктор
     * @param id
     * @param name
     */
    public DBDirections(int id, String name) {
        Log.d(tagDB, "Вызов вызов конструктора класса DBEmployees с параментрами");
        this.id = id;
        this.name = name;
    }

    public static final String queryCreateTable = TABLE_NAME
            + "("
            + KEY_ID + " integer primary key,"
            + KEY_NAME + " text"
            + ")";

    public static long add(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBDirections.add");
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }
    public static long add(SQLiteDatabase db, DBHelper dbHelper, String name) {
        Log.d(tagDB, "Вызов метода DBDirections.add");
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }
    /**
     * Метод изменения элемента в БД
     * @param db
     * @param dbHelper
     * @return
     */
    public static int edit(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBDirections.edit( " + id + " )");
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
    public static int delete(SQLiteDatabase db, DBHelper dbHelper) {
        Log.d(tagDB, "Вызов метода DBDirections.delete( " + id + " )");
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
