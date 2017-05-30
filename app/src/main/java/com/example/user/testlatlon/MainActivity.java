package com.example.user.testlatlon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listCity;
    TextView tvData;
    String[] cities;
    int pos;
    Context context = this;
    double[] longitude = new double[10];
    double[] latitude = new double[10];
    public List<String> latiList = new ArrayList<String>();
    public List<String> longList = new ArrayList<String>();
    public List<String> nameList = new ArrayList<String>();
    public List<String> ratingList = new ArrayList<String>();
    public List<String> vicinityList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listCity = (ListView) findViewById(R.id.listCity);
        tvData = (TextView) findViewById(R.id.tvData);

        cities = getResources().getStringArray(R.array.cities);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, cities);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listCity.setAdapter(adapter);

        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //Ahmedabad ID : ChIJN1t_tDeuEmsRUsoyG83frY4
                    pos = position;
                    new JSONTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=23.0225,72.5714&keyword=food&radius=1000&type=restaurant&key=AIzaSyACU2rnlOPfkSpEL-n5giMJtRJadcQWsSw");
                } else if (position == 1) {
                    //Vadodara ID : ChIJq92jkavIXzkRuD9H4b_TOaw
                    pos = position;
                    new JSONTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.3072,73.1812&keyword=eat&keyword=food&radius=1000&type=restaurant&key=AIzaSyACU2rnlOPfkSpEL-n5giMJtRJadcQWsSw");
                }
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("results");
                latiList.clear();
                longList.clear();
                Log.e("parent object length : ", String.valueOf(parentArray.length()));
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject childObject = parentArray.getJSONObject(i);
                    JSONObject geometryObject = childObject.getJSONObject("geometry");
                    JSONObject locationObject = geometryObject.getJSONObject("location");
                    latiList.add(locationObject.getString("lat"));
                    Log.e("#", "\n##########################################################");
                    Log.e("lat : ", String.valueOf(latitude));
                    longList.add(locationObject.getString("lng"));
                    Log.e("long : ", String.valueOf(longitude));

                    String name = childObject.getString("name");
                    nameList.add(name);
                    Log.e("name : ", name);

                    if (childObject.isNull("rating")) {
                        ratingList.add("No rating available !");
                        Log.e("rating : ", "Not available !");
                    } else {
                        String rating = childObject.getString("rating");
                        ratingList.add(rating);
                        Log.e("rating : ", rating);
                    }
                    if (childObject.isNull("vicinity")){
                        ratingList.add("No Address available !");
                        Log.e("vicinity : ", "Not available !");
                    }
                    else {
                        String vicinity = childObject.getString("vicinity");
                        vicinityList.add(vicinity);
                    }

                    Log.e("#", "##########################################################\n");

                }

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putStringArrayListExtra("latiList", (ArrayList<String>) latiList);
                intent.putStringArrayListExtra("longList", (ArrayList<String>) longList);
                intent.putExtra("selectedCity", String.valueOf(pos));
                intent.putStringArrayListExtra("nameList", (ArrayList<String>) nameList);
                intent.putStringArrayListExtra("ratingList", (ArrayList<String>) ratingList);
                intent.putStringArrayListExtra("vicinityList", (ArrayList<String>) vicinityList);
                Log.e("status : ", " Sent !!");
                startActivity(intent);
//                return latiList+"\t"+longList +"\n";
//                return latiList + "\n" + longList + "\n";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }
}
