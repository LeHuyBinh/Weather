package com.example.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText ecity,ecode;
    TextView tinfo;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "ba163ba5dddb5c47d6b487f9ed81fee1";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecity = findViewById(R.id.CityN);
//        ecode = findViewById(R.id.Code);
        tinfo = findViewById(R.id.i4);
    }

    public void getWeatherNews(View view) {
        // code goi api
        String temp ="";
        String city = ecity.getText().toString().trim();
//        String country = ecode.getText().toString().trim();
        if (city.equals("")) {
            tinfo.setText("Please enter city name");
        } else {
            temp = url + "?q=" + city + "&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,temp,new Response.Listener<String> () {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        tinfo.setTextColor(Color.rgb(68,134,199));
                        output += "Thời tiết ở " + cityName + " (" + countryName + ")"
                                + "\n Nhiệt độ: " + df.format(temp) + " °C"
                                + "\n Cảm giác như: " + df.format(feelsLike) + " °C"
                                + "\n Độ ẩm: " + humidity + "%"
                                + "\n Tốc độ gió: " + wind + "m/s"
                                + "\n Tỉ lệ mây: " + clouds + "%"
                                + "\n Áp suất không khí: " + pressure + " hPa";
                        tinfo.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}