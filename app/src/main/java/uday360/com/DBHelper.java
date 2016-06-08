package uday360.com.mycart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ItemsDb.db";
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ITEMS_COLUMN_UPC = "upc";
    public static final String ITEMS_COLUMN_QUANTITY = "quantity";
    public static final String ITEMS_COLUMN_DESCRIPTION = "description";
    public static final String ITEMS_COLUMN_PRICE="price";
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);

    }
    //Intialization of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists items " +
                        "(_id integer primary key autoincrement,upc text, quantity integer,description text,price double)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }
    //To insert the item into the database
    public boolean insertItem(String upc,int quantity,String description,double price )
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("upc",upc);
        values.put("quantity",quantity);
        values.put("description", description);
        values.put("price", price);
        long insertResult=db.insert(ITEMS_TABLE_NAME, null, values);
        return insertResult != -1;
    }
    //To get the item from the database based on the code
    public Cursor getItem(String upc)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor item=db.rawQuery("select * from items where upc=" + upc + "", null);
        return item;
    }
    //To delete a single item
    public Integer delete(String upc)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(ITEMS_TABLE_NAME,"upc=?",new String[]{upc});
    }
    //To delete all the Items
    public Integer deleteAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(ITEMS_TABLE_NAME,"1",null);
    }
    //To get all the items from the database
    public Cursor getItems()
    {
        ArrayList<Item> itemsList=new ArrayList<Item>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor result=db.rawQuery("select rowid _id, * from items",null);
        result.moveToFirst();
        /*while(result.isAfterLast()!=true)
        {
            itemsList.add((Item)result);
            result.moveToNext();
        }
        return  itemsList;*/
        return result;

    }
    //To update the item in the database
    public boolean updateItem(String upc,int quantity,String description ,double price)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("upc",upc);
        values.put("quantity",quantity);
        values.put("description", description);
        values.put("price", price);
        long updateResult=db.update(ITEMS_TABLE_NAME,values,"upc=?",new String[]{upc});
        return updateResult != -1;
    }


}

