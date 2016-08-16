package com.manhdong.sono.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saphiro on 6/24/2016.
 */
public class DBContext extends SQLiteOpenHelper{

    public final static String DB_NAME = "DebtInfo.db";
    final static int DB_VERSION = 1;

    public static String TABLE_NAME = "debtData";
    public final static String COL_ID = "_id";
    public final static String COL_PERSON = "personName";
    public final static String COL_AMOUNT = "amount";
    public final static String COL_CURRENCY = "currency";
    public final static String COL_DEBT_TYPE = "debtType";
    public final static String COL_DESCRIPTION = "description";
    public final static String COL_START_DATE = "startDate";
    public final static String COL_EXPIRE_DATE = "expDate";

    public String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_PERSON + " TEXT,"
            + COL_DEBT_TYPE + " TEXT,"
            + COL_AMOUNT + " REAL," + COL_CURRENCY + " TEXT,"
            + COL_DESCRIPTION + " TEXT,"
            + COL_START_DATE + " TEXT," + COL_EXPIRE_DATE + " TEXT)";
    final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBContext(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: xử lý upgrade cho những phiên bản sau như thế nào?

    }
}
