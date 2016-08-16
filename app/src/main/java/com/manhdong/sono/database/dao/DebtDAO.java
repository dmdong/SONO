package com.manhdong.sono.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.manhdong.sono.database.DBContext;
import com.manhdong.sono.model.Debt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by Saphiro on 6/24/2016.
 */
public class DebtDAO  {

    Context mContext;
    DBContext dbContext;

    public DebtDAO(Context context) {
        mContext = context;
        dbContext = new DBContext(context);
    }

    public long createDebt(Debt debt){

        SQLiteDatabase database = dbContext.getWritableDatabase();
        long rowID = database.insert(DBContext.TABLE_NAME, null, getContentValue(debt));
        database.close();
        return rowID;
    }

    public void createDebtTable(){
        SQLiteDatabase database = dbContext.getWritableDatabase();
        database.execSQL(dbContext.CREATE_TABLE);
        database.close();
    }

    public void deleteDebt (int debtRow){
        SQLiteDatabase database = dbContext.getWritableDatabase();

        //Tìm ID của dòng muốn xóa
//        Cursor cursor = database.query(DBContext.TABLE_NAME, null, null, null, null, null, null);
//        int rowID = -1;
//        if (cursor.moveToFirst()){
//            cursor.moveToPosition(debtRow);
//            rowID = cursor.getInt(cursor.getColumnIndex(DBContext.COL_ID));
//
//        }
//        cursor.close();
        database.delete(DBContext.TABLE_NAME, DBContext.COL_ID + "=?", new String[]{debtRow+""});
        Toast.makeText(mContext, "Deleted successful!", Toast.LENGTH_SHORT).show();;
        database.close();

    }

    public void updateDebt(Debt debt, int debtRow){
        SQLiteDatabase database = dbContext.getWritableDatabase();
        ContentValues values = getContentValue(debt);
//        Cursor cursor = database.query(DBContext.TABLE_NAME, null, null, null, null, null, null);
//        int rowID = -1;
//        if (cursor.moveToFirst()){
//            cursor.moveToPosition(debtRow);
//            rowID = cursor.getInt(cursor.getColumnIndex(DBContext.COL_ID));
//        }
//        cursor.close();
        database.update(DBContext.TABLE_NAME, values, DBContext.COL_ID + "=?",
                new String[] {String.valueOf(debtRow)});
        Toast.makeText(mContext, "Update successful!", Toast.LENGTH_SHORT).show();
//        Toast.makeText(mContext, "rowID:" +debtRow, Toast.LENGTH_LONG).show();
        database.close();
//        try {
//            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//
//            if (sd.canWrite()) {
//                String currentDBPath = "/data/" + mContext.getPackageName() + "/databases/"+ DBContext.DB_NAME;
//                String backupDBPath = "backupname.db";
//                File currentDB = new File(currentDBPath);
//                File backupDB = new File(sd, backupDBPath);
//
//                if (currentDB.exists()) {
//                    FileChannel src = new FileInputStream(currentDB).getChannel();
//                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//                }
//            }
//        } catch (Exception e) {
//
//        }
    }



    public void clearData(){
        SQLiteDatabase database = dbContext.getWritableDatabase();
        database.delete(DBContext.TABLE_NAME,null,null);
        //mContext.deleteDatabase(DBContext.DB_NAME);
        database.close();

    }

    public Double getDebts(List<Debt> debts){
        Double sum = 0.0;
        SQLiteDatabase database = dbContext.getReadableDatabase();
        Cursor cursor = database.query(DBContext.TABLE_NAME, null, null, null, null, null, null);
        if(cursor!=null){
            if (cursor.moveToFirst()) {

                do{
                    Debt debt = new Debt();
                    debt.setPerson(cursor.getString(cursor.getColumnIndex(DBContext.COL_PERSON)));
                    debt.setDebtType(cursor.getString(cursor.getColumnIndex(DBContext.COL_DEBT_TYPE)));
                    debt.setAmount(cursor.getDouble(cursor.getColumnIndex(DBContext.COL_AMOUNT)));
                    debt.setReason(cursor.getString(cursor.getColumnIndex(DBContext.COL_DESCRIPTION)));
                    debt.setStartDate(cursor.getString(cursor.getColumnIndex(DBContext.COL_START_DATE)));
                    debt.setExpDate(cursor.getString(cursor.getColumnIndex(DBContext.COL_EXPIRE_DATE)));
                    debt.setCurrency(cursor.getString(cursor.getColumnIndex(DBContext.COL_CURRENCY)));
                    debt.setColumnID(cursor.getInt(cursor.getColumnIndex(DBContext.COL_ID)));
                    debts.add(debt);
                    if(debt.getDebtType().equals("MONEY")){
                        sum+=debt.getAmount();
                    }

                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        database.close();


        return sum;
    }

    public Double getDebts(List<Debt> debts, TextView tvSum){
        Double sum = 0.0;
        SQLiteDatabase database = dbContext.getReadableDatabase();
        Cursor cursor = database.query(DBContext.TABLE_NAME, null, null, null, null, null, null);
        if(cursor!=null){
            if (cursor.moveToFirst()) {

                do{
                    Debt debt = new Debt();
                    debt.setPerson(cursor.getString(cursor.getColumnIndex(DBContext.COL_PERSON)));
                    debt.setDebtType(cursor.getString(cursor.getColumnIndex(DBContext.COL_DEBT_TYPE)));
                    debt.setAmount(cursor.getDouble(cursor.getColumnIndex(DBContext.COL_AMOUNT)));
                    debt.setReason(cursor.getString(cursor.getColumnIndex(DBContext.COL_DESCRIPTION)));
                    debt.setStartDate(cursor.getString(cursor.getColumnIndex(DBContext.COL_START_DATE)));
                    debt.setExpDate(cursor.getString(cursor.getColumnIndex(DBContext.COL_EXPIRE_DATE)));
                    debt.setCurrency(cursor.getString(cursor.getColumnIndex(DBContext.COL_CURRENCY)));
                    debt.setColumnID(cursor.getInt(cursor.getColumnIndex(DBContext.COL_ID)));
                    debts.add(debt);
                    if(debt.getDebtType().equals("MONEY")){
                        sum+=debt.getAmount();
                    }

                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        database.close();
        tvSum.setText(sum.toString());

        return sum;
    }

    public void clearDebts(List<Debt> debts){
        SQLiteDatabase database = dbContext.getWritableDatabase();

        for (int debt = 0; debt < debts.size(); debt++) {
            database.delete(DBContext.TABLE_NAME, DBContext.COL_ID + "=?",
                    new String[] {String.valueOf(debts.get(debt).getColumnID())});
        }

        database.close();

    }

    public ContentValues getContentValue(Debt debt) {
        ContentValues values = new ContentValues();
        values.put(DBContext.COL_PERSON, debt.getPerson());
        values.put(DBContext.COL_DEBT_TYPE, debt.getDebtType());
        values.put(DBContext.COL_AMOUNT, debt.getAmount());
        values.put(DBContext.COL_CURRENCY, debt.getCurrency());
        values.put(DBContext.COL_DESCRIPTION, debt.getReason());
        values.put(DBContext.COL_START_DATE, debt.getStartDate());
        values.put(DBContext.COL_EXPIRE_DATE, debt.getExpDate());
        return values;
    }
}
