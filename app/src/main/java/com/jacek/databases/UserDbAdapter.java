package com.jacek.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jacek on 2017-04-27.
 */

public class UserDbAdapter {
    //region CONSTANTS
    private static final String DATABASE_NAME = "USER_DATABASE.db";
    private static final String USER_TABLE = "USER_TABLE";
    public static final String KEY_ROWID = "_id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String SURNAME = "surname";
    public static final String POINTS = "points";
    public static final String AGE = "age";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String LEVEL = "level";
    public static String TAG = UserDbAdapter.class.getSimpleName();
    private static final int DATABASE_VERSION = 11;
    private final Context ctx;
    //endregion
    private DatabaseHelper dbHelper;
    SQLiteDatabase user_db;

    public UserDbAdapter(Context ctx){
        this.ctx = ctx;
    }

    public UserDbAdapter open() throws SQLException{
        dbHelper = new DatabaseHelper(ctx);
        user_db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(dbHelper!=null){
            dbHelper.close();
        }
    }

    public void upgrade() throws SQLException{
        dbHelper = new DatabaseHelper(ctx);
        user_db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(user_db, 1 , 0);
    }

    public boolean updateUser(int id, ContentValues newValues){
        String[] selectionArgs = {String.valueOf(id)};
        return user_db.update(USER_TABLE, newValues, KEY_ROWID + "=?", selectionArgs) > 0;
    }

    public long insertUser(ContentValues initialValues){
        return user_db.insertWithOnConflict(USER_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean deleteUser(int id){
        String[] selectionArgs = {String.valueOf(id)};
        return user_db.delete(USER_TABLE, KEY_ROWID + "=?", selectionArgs) > 0;
    }

    public boolean loginUser(String name, String password){
        String sqlQuery = "SELECT * FROM " + USER_TABLE + " WHERE (name = ? AND password = ?);";
        Cursor cursor = user_db.rawQuery(sqlQuery, new String[]{name, password});
        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    public static final String[] USER_FIELDS = new String[]{
            KEY_ROWID, NAME, PASSWORD, SURNAME, POINTS, AGE, WEIGHT, HEIGHT, LEVEL
    };

    public Cursor getUsers(){
        return user_db.query(USER_TABLE, USER_FIELDS, null, null, null, null, null);
    }

    public User getUserFromCursor(Cursor cursor){
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
        user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        user.setSurname(cursor.getString(cursor.getColumnIndex(SURNAME)));
        user.setPoints(cursor.getDouble(cursor.getColumnIndex(POINTS)));
        user.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
        user.setWeight(cursor.getInt(cursor.getColumnIndex(WEIGHT)));
        user.setHeight(cursor.getInt(cursor.getColumnIndex(HEIGHT)));
        user.setLevel(cursor.getInt(cursor.getColumnIndex(LEVEL)));
        return user;
    }

    private static final String CREATE_USER_TABLE_SQLCommand = "CREATE TABLE " + USER_TABLE + " ("
            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL UNIQUE, "
            + PASSWORD + " TEXT NOT NULL UNIQUE, "
            + SURNAME + " TEXT NOT NULL UNIQUE, "
            + POINTS + " INTEGER, "
            + AGE + " INTEGER NOT NULL, "
            + WEIGHT + " INTEGER NOT NULL, "
            + HEIGHT + " INTEGER NOT NULL, "
            + LEVEL + " INTEGER NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_USER_TABLE_SQLCommand);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Aktualizacja bazy danych z wersji: " + oldVersion + " na: " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
        }
    }
}
