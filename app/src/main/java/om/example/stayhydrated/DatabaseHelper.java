package om.example.stayhydrated;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String Database_Name = "Water";
    public static final int Database_version = 1;

    public static final String Table_Water_Quantity = "waterQuantity";
    public static final String Key_ID = "id";
    public static final String Key_Quantity = "quantity";
    public static final String Key_TimeStamp = "Date";
    public static final String Key_RegularDate = "RegularDate";

    String Create_Table = "Create Table " + Table_Water_Quantity +
            " (" + Key_ID + " Integer PRIMARY KEY, "
            + Key_Quantity + " TEXT, "
            + Key_TimeStamp + " TEXT, "
            + Key_RegularDate + " TEXT"
            + " )";

    String Drop_Table = "DROP TABLE IF EXISTS " + Table_Water_Quantity;

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Drop_Table);
        onCreate(sqLiteDatabase);
    }

    public void addQuantity(String name, String timeStamp,String RegularDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Quantity, name);
        values.put(Key_TimeStamp, timeStamp);
        values.put(Key_RegularDate,RegularDate);
        db.insert(Table_Water_Quantity, null, values);
        db.close();
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = " drop table " + Table_Water_Quantity;
        db.execSQL(Drop_Table);
        db.execSQL(Create_Table);
    }

    public void deleteQuantity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteWhere = "id=?";
        db.delete(Table_Water_Quantity, deleteWhere, new String[]{String.valueOf(id)});
    }

    public List<ThreeQuantities> getAllQuantities() {
        List<ThreeQuantities> QuantityList = new ArrayList<ThreeQuantities>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Table_Water_Quantity;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //looping through all rows

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String quantity = cursor.getString(1);
                String timeStamp = cursor.getString(2);
                String regularDate=cursor.getString(3);

                ThreeQuantities threeQuantities = new ThreeQuantities(id, quantity, timeStamp,regularDate);

                QuantityList.add(threeQuantities);
            } while (cursor.moveToNext());
        }
        return QuantityList;
    }

    public ThreeQuantities lastItem() {
        ThreeQuantities threeQuantity = null;
        // Select All Query
        String selectQuery = "SELECT * FROM " + Table_Water_Quantity;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //looping through all rows

        if (cursor.moveToLast()) {
            int id = Integer.parseInt(cursor.getString(0));
            String quantity = cursor.getString(1);
            String timeStamp = cursor.getString(2);
            String regularDate=cursor.getString(3);

            threeQuantity = new ThreeQuantities(id, quantity, timeStamp,regularDate);
        }
        return threeQuantity;
    }

}
