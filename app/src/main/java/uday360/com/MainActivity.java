package uday360.com.mycart;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by uday on 1/30/2016.
 */

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ImageButton scanButton, showItems,Ailisten;
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        scanButton = (ImageButton) findViewById(R.id.barcodeButton);
        showItems = (ImageButton) findViewById(R.id.shopping_cart_button);
        Ailisten = (ImageButton) findViewById(R.id.mic_btn);
        scanButton.setOnClickListener(this);
        showItems.setOnClickListener(this);
        Ailisten.setOnClickListener(this);


    }
    //Start the activity based on the button clicked
    @Override
    public void onClick(View v) {
        //Start the activity to scan the barcode
        if (v.getId() == R.id.barcodeButton) {
            Intent scanIntent=new Intent(v.getContext(),ScanItem.class);
            scanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            scanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(scanIntent);


        }
        //Start the activity to show the items in the cart
        if (v.getId() == R.id.shopping_cart_button) {
            Intent sItems = new Intent(v.getContext(), ShowItems.class);
            sItems.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sItems.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            sItems.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(sItems);
        }
        //Start the activity to start recording the voice for processing
        if (v.getId() == R.id.mic_btn) {
            Intent ai = new Intent(v.getContext(), AIListen.class);
            ai.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ai.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ai.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(ai);
        }

    }
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        }
        //to close the app press back twice  within 3 sec
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


}
