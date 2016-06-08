package uday360.com.mycart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by uday on 3/14/2016.
 */
public class ItemInfo extends AppCompatActivity implements OnClickListener{

    String code, description, quantity;
    Double totalPrice;
    TextView codeV, descriptionV, quantityV,price;
    ImageButton back,delete;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);
        code = getIntent().getStringExtra("code");
        quantity = getIntent().getStringExtra("quantity");
        description = getIntent().getStringExtra("description");
        totalPrice=getIntent().getDoubleExtra("price",0.0);
        price=(TextView)findViewById(R.id.price);
        codeV = (TextView) findViewById(R.id.code_i);
        descriptionV = (TextView) findViewById(R.id.description_i);
        quantityV = (TextView) findViewById(R.id.quantity_i);
        back=(ImageButton)findViewById(R.id.back_i);
        delete=(ImageButton)findViewById(R.id.delete_i);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        db=new DBHelper(this);
        setFields();

    }
    //Showing the details of the item which is clicked
    private void setFields() {

        codeV.setText(code);
        quantityV.setText(quantity);
        descriptionV.setText(description);
        price.setText("$ "+totalPrice.toString());
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onClick(View v) {
        Intent showIntent=new Intent(v.getContext(),ShowItems.class);
        //On back press go to the list of items
        if (v.getId() == R.id.back_i) {
            startActivity(showIntent);
        }
        //Delete the product from list
        if (v.getId() == R.id.delete_i) {
            if(db.delete(code)>0)
                startActivity(showIntent);
            else {
                Toast.makeText(getApplicationContext(), "item already deleted", Toast.LENGTH_SHORT).show();
                startActivity(showIntent);
            }
        }


    }
}
