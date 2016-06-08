package uday360.com.mycart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentResult;

import java.math.RoundingMode;
import java.util.HashMap;


/**
 * Created by uday on 1/30/2016.
 */
public class ShowItems extends AppCompatActivity {

    private DBHelper dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private ListView listView;
    private double items_cost;
    private TextView cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);
        cost=(TextView)findViewById(R.id.cost_of_items);
        cost.setText("The items in your cart");
        dbHelper=new DBHelper(this);
        displayItems();
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }
    //Starting the new activity to show thw details of the item clicked
    private AdapterView.OnItemClickListener onListClick= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i= new Intent(view.getContext(),ItemInfo.class);
            Cursor  obj = (Cursor)parent.getItemAtPosition(position);

            i.putExtra("code",obj.getString(2));
            i.putExtra("quantity",Integer.toString(obj.getInt(3)));
            i.putExtra("description",obj.getString(4));
            i.putExtra("price",obj.getDouble(5));
            startActivity(i);

        }
    };
    // Displaying the items in the list
    public void displayItems()
    {
        Cursor cursor=dbHelper.getItems();

        String[] coloumns=new String[]
                {
                        DBHelper.ITEMS_COLUMN_UPC,
                        DBHelper.ITEMS_COLUMN_QUANTITY,
                        DBHelper.ITEMS_COLUMN_DESCRIPTION,
                        DBHelper.ITEMS_COLUMN_PRICE
                };
        int[] to=new int[]
                {
                  R.id.code,R.id.quantity,R.id.description
                };

        if(cursor.getCount()!=0) {
            //calculateCost(cursor);
            dataAdapter = new SimpleCursorAdapter(this, R.layout.item_view, cursor, coloumns, to, 0);
            listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(dataAdapter);
            listView.setOnItemClickListener(onListClick);
        }
        else
        {
            setContentView(R.layout.empty_cart);
        }

    }
    // To calculate the total cost of the items
    private void calculateCost(Cursor cursor) {
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++)
        {
           double cost_i= cursor.getDouble(5);
            items_cost+=cost_i;

        }

        cost.setText("The cost of total items is: $ " + items_cost);

    }


}