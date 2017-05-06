package com.jacek.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 2017-05-06.
 */

public class PointDbAdapter {
    //region CONSTANTS
    public static final String DATABASE_NAME = "POINT_DATABASE.db";
    public static final String POINT_TABLE = "POINT_TABLE";
    public static final String KEY_ROWID = "_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ID_ROUTE = "id_route";
    public static final String TAG = PointDbAdapter.class.getSimpleName();
    private static final int DATABASE_VERSION = 5;
    private final Context ctx;
    //endregion
    private DatabasePointHelper db_point_helper;
    SQLiteDatabase point_db;

    public static String[] POINT_FIELDS = {
      KEY_ROWID, LATITUDE, LONGITUDE, ID_ROUTE
    };

    public PointDbAdapter(Context mCtx){
        ctx = mCtx;
    }

    public PointDbAdapter open() throws SQLException{
        db_point_helper = new DatabasePointHelper(ctx);
        point_db = db_point_helper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(db_point_helper!=null){
            db_point_helper.close();
        }
    }

    public boolean updatePoint(int id, ContentValues newValues){
        return point_db.update(POINT_TABLE, newValues, KEY_ROWID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public long insertPoint(ContentValues initialValues){
        return point_db.insertWithOnConflict(POINT_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean deleteUser(int id){
        return point_db.delete(POINT_TABLE, KEY_ROWID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    //TODO jeśli coś się wali to tutaj
    public void upgrade() throws SQLException{
        db_point_helper = new DatabasePointHelper(ctx);
        point_db = db_point_helper.getWritableDatabase();
        db_point_helper.onUpgrade(point_db, 1, 0);
    }

    public Cursor getPoints(){
        return point_db.query(POINT_TABLE, POINT_FIELDS, null, null, null, null, null);
    }

    public Point getPointFromCursor(Cursor cursor){
        Point point = new Point();
        point.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
        point.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
        point.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
        point.setId_route(cursor.getInt(cursor.getColumnIndex(ID_ROUTE)));
        return point;
    }

    private static String POINTDB_CREATE_SQLCOMMAND = "CREATE TABLE " + POINT_TABLE +  " ("
            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LATITUDE + " REAL, "
            + LONGITUDE + " REAL, "
            + ID_ROUTE + " INTEGER NOT NULL UNIQUE);";

    private static class DatabasePointHelper extends SQLiteOpenHelper{
        DatabasePointHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(POINTDB_CREATE_SQLCOMMAND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG,  "Aktualizacja bazy z wersji: " + oldVersion + " na: " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + POINT_TABLE);
            onCreate(db);
        }
    }
}
