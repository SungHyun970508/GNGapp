package com.example.gngapppro;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    androidx.appcompat.app.ActionBar aBar;
    private static String TAG = "shop_php_test";
    private static String IP_ADDRESS = "10.10.10.23";
    private String userID = "";
    private TextView tv_name;
    private TextView total;

    /////////////////////////////////////////////
    private RecyclerView mRecyclerView = null;
    private Recycler mAdapter = null;
    ArrayList<Shopping_class> mList = new ArrayList<Shopping_class>();

    private RecyclerView.LayoutManager layoutManager;

    private String proName[] = new String[7];
    private String proCost[] = new String[7];
    private String cnt[] = new String[7];
    private JSONArray countriesArray;
    private StringBuilder builder = new StringBuilder();

    //TextView TproName;
    //TextView TproCost;
    //TextView Tcnt;
    private class GetDataFromServer extends AsyncTask<String, String, String> {
        //JSONArray countriesArray;

        @Override
        protected String doInBackground(String... params) {

            String userID = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "userID=" + userID;
            //StringBuilder builder = new StringBuilder();

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    //InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                        Log.e("line is", line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


            } catch (Exception e) {

                Log.d(TAG, "Select Shopping Data: Error ", e);
            }


            try {
                JSONObject root = new JSONObject(builder.toString());

                countriesArray = root.getJSONArray("response");
                int sum=0;
                Log.e("시발제발", Integer.toString(countriesArray.length()));
                for (int i = 0; i < countriesArray.length(); i++) {
                    Shopping_class item = new Shopping_class();
                    JSONObject jObject = countriesArray.getJSONObject(i);
                    proName[i] = jObject.getString("proName");
                    proCost[i] = jObject.getString("proCost");
                    cnt[i] = jObject.getString("cnt");
                    Log.e("물건이름", cnt[i]);
                    item.setCost(proCost[i]);
                    item.setcount(cnt[i]);
                    item.setproName(proName[i]);
                    mList.add(item);
                    sum+=(Integer.parseInt(proCost[i])*Integer.parseInt(cnt[i]));
                }
                total.setText(getString(sum));
                //mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.i("완전짜증나", "Exception try2: " + e.getStackTrace());
                e.printStackTrace();
            }

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TproName = (TextView) findViewById(R.id.product_Name);
        //TproCost = (TextView) findViewById(R.id.product_Cost);
        //Tcnt = (TextView) findViewById(R.id.product_Cnt);

        //aBar=getSupportActionBar();
        //aBar.setTitle("Welcome to GNG"); //타이틀
        //aBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);

        tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();


        userID = intent.getStringExtra("userID");
        //Log.e("이름", userID);
        tv_name.setText(userID);

        Button btn_payment = findViewById(R.id.btn_payment);

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)

                        .setTitle("구매 완료")

                        .setMessage("애플리케이션을 종료하시겠습니까?")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(MainActivity.this, "애플리케이션이 종료되었습니다.", Toast.LENGTH_SHORT).show();

                                finish();

                            }

                        })
                        .setNegativeButton("NO", null)


                        .setIcon(android.R.drawable.ic_dialog_alert)


                        .show();

            }

        });
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new Recycler(mList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        GetDataFromServer getdatafromserver = new GetDataFromServer();
        getdatafromserver.execute("http://" + IP_ADDRESS + "/PHP_select_shop.php", userID);
        Button btn_test = findViewById(R.id.btn_test);
        total = findViewById(R.id.view_total);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.clear();


                /*JSONObject root = null;
                try {
                    root = new JSONObject(builder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    countriesArray = root.getJSONArray("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("시발제발", Integer.toString(countriesArray.length()));
                for (int i = 0; i < countriesArray.length(); i++) {
                    Shopping_class item = new Shopping_class();
                    JSONObject jObject = null;
                    try {
                        jObject = countriesArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        proName[i] = jObject.getString("proName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        proCost[i] = jObject.getString("proCost");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        cnt[i] = jObject.getString("cnt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("물건이름", cnt[i]);
                    item.setCost(proCost[i]);
                    item.setcount(cnt[i]);
                    item.setproName(proName[i]);
                    mList.add(item);
                }*/

            }
        });
    }
}
