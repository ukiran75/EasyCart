package uday360.com.mycart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;
import  com.google.zxing.integration.android.IntentIntegrator;
import  com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by uday on 2/13/2016.
 */
public class ScanItem extends Activity  implements View.OnClickListener {

    ImageButton addItem;
    TextView addRes;
    String error,result;
    DBHelper db;
    String rpc_key = "92a2a3514fda130f847279a891a85132bd3aff5d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_list);
        addItem = (ImageButton) findViewById(R.id.add_another);
        addRes = (TextView) findViewById(R.id.item_added);
        addItem.setOnClickListener(this);
        db = new DBHelper(this);
        scanBarcode();
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.add_another) {
            scanBarcode();
        }
    }

    public void scanBarcode() {
       //getProductInfo("078742353388");
       IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
        //if (db.insertItem("639277105851", 1, "Item Description")) {
        //     addRes.setText("Item added to the list");
        // } else {
        //     addRes.setText("Error while adding to the list");
        // }


    }
    //on getting the result from the barcode scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (!result.getContents().isEmpty()) {
            String scanFormat = result.getFormatName();// get the scan format
            String scanContent = result.getContents(); // get the scan content
            //addRes.setText(scanContent);
            getProductInfo(scanContent);

            /*if (db.insertItem(scanContent, 1, scanFormat)) {
                addRes.setText("Item added to the list");
            } else {
                addRes.setText("Error while adding to the list");
            }


        } else {
            addRes.setText("Error while adding to the list");
        }*/
        }
    }


    public void getProductInfo(String scanContent) {

        String restURL = " http://api.walmartlabs.com/v1/items?apiKey=up23a746h2a34t5pm9x2txua&upc="+scanContent;
        new GetInfo().execute(restURL);


    }
    //Getting the details of the product asynchronously
    public class GetInfo extends AsyncTask<String,Void,Void> {

        URL url;
        ProgressDialog progressDialog=new ProgressDialog(ScanItem.this);
        @Override
        protected Void doInBackground(String... params) {

            BufferedReader br=null;
            try {
                url =new URL(params[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder=new StringBuilder();
                String line=null;
                while((line=br.readLine())!=null)
                {
                    stringBuilder.append(line);
                    stringBuilder.append(System.getProperty("line.separator"));
                }
               result=stringBuilder.toString();

            } catch (MalformedURLException e) {
                error+=e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                error+=e.getMessage();
                e.printStackTrace();
            }
            finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Getting the product Info....");
            progressDialog.show();
        }
        //On getting the details from the async task
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            String productDesc="";
            String productRating="5";
            double productPrice=1.11;
            if(error!=null)
            {
               addRes.setText("error while adding");
            }
            else
            {
                JSONObject jsonresp;
                try {
                    jsonresp=new JSONObject(result);
                    JSONArray jsonArray=jsonresp.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject child=jsonArray.getJSONObject(i);
                        productDesc=child.getString("name");
                        if(child.has("customerRating"))
                        productRating=child.getString("customerRating")+"/5";
                        if(child.has("salePrice"))
                        productPrice=child.getDouble("salePrice");
                    }
                    if (db.insertItem(productDesc, 1,productRating,productPrice)) {
                        addRes.setText("Item added to the list");
                    } else {
                        addRes.setText("Error while adding to the list");
                    }
                } catch (JSONException e) {
                    addRes.setText("JSON Parse Error");
                }

            }
        }
    }

}


