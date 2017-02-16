package mr.famille.myompta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String DEPENSES_TABLE_NAME = "depences";
    public static final String DEPENSES_COLUMN_ID = "id";
    public static final String DEPENSES_COLUMN_DESIGNATION = "designation";
    public static final String DEPENSES_COLUMN_BENEFICIAIRE = "beneficiaire";
    public static final String DEPENSES_COLUMN_MONTANT = "montant";
    public static final String DEPENSES_COLUMN_DATE_OP = "date_op";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table depences " +
                        "(id integer primary key, designation text,montant int, beneficiaire text, date_op text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS depences");
        onCreate(db);
    }

    public boolean insertContact (String designation, String montant, String beneficiaire, String dateOp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designation", designation);
        contentValues.put("montant", montant);
        contentValues.put("beneficiaire", beneficiaire);
        contentValues.put("date_op", dateOp);
        db.insert("depences", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from depences where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DEPENSES_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String designation, String montant,  String beneficiaire, String dateOp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designation", designation);
        contentValues.put("montant", montant);
        contentValues.put("beneficiaire", beneficiaire);
        contentValues.put("date_op", dateOp);
        db.update("depences", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("depences",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from depences", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DEPENSES_COLUMN_DESIGNATION))+"   "+res.getString(res.getColumnIndex(DEPENSES_COLUMN_MONTANT))+"   "+res.getString(res.getColumnIndex(DEPENSES_COLUMN_BENEFICIAIRE))+"   "+res.getString(res.getColumnIndex(DEPENSES_COLUMN_DATE_OP)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllDesign() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select sum(montant) as nb, designation from depences group by designation", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DEPENSES_COLUMN_DESIGNATION))+"   "+res.getString(res.getColumnIndex("nb")));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllDate() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select sum(montant) as nb, date_op from depences group by date_op", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DEPENSES_COLUMN_DATE_OP))+"   "+res.getString(res.getColumnIndex("nb")));
            res.moveToNext();
        }
        return array_list;
    }
}