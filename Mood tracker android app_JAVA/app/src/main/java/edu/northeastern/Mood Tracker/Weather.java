package edu.northeastern.mobileapplicationteam18;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;

import java.sql.SQLOutput;

public class Weather extends AppCompatActivity {

    EditText et;
    TextView tv;
    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}";
    String apikey = "eea3ae443837e9afaabb58ba6ea17b13";
    LocationManager manager;
    LocationListener locationListener;

    private ProgressBar mProgressBar;
    private TextView mLoadingText;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        Button getWeatherBtn = findViewById(R.id.get_weather_btn) ;



        getWeatherBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getWeather(et);
                mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
                mLoadingText = (TextView) findViewById(R.id.LoadingCompleteTV);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mProgressStatus < 100){
                            mProgressStatus++;
                            android.os.SystemClock.sleep(2);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(mProgressStatus);
                                }
                            });
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingText.setVisibility(View.VISIBLE);
                            }
                        });
                        mProgressStatus = 0;
                    }
                }).start();

            }
        });


    }
    // end of onCreate()

    public void getWeather(View v){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi myapi=retrofit.create(WeatherApi.class);
        Call<WeatherExample> exampleCall=myapi.getWeather(et.getText().toString().trim(),apikey);
        System.out.println("et.getText().toString().trim()________" + et.getText().toString().trim());
        exampleCall.enqueue(new Callback<WeatherExample>() {
            @Override
            public void onResponse(Call<WeatherExample> call, Response<WeatherExample> response) {

                if(response.toString().contains("Not Found")){
                    tv.setText("NA");
                    Toast.makeText(Weather.this,"Please Enter a valid City",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    tv.setText("NA");
                    Toast.makeText(Weather.this,response.code()+" ",Toast.LENGTH_LONG).show();
                    return;
                }
                else{

                WeatherExample myData=response.body();
                WeatherDetails main=myData.getWeatherDetails();
                Double temp=main.getTemp();
                Integer temperature=(int)(temp-273.15);
                tv.setText(String.valueOf(temperature)+"C");
            }}

            @Override
            public void onFailure(Call<WeatherExample> call, Throwable t) {
                Toast.makeText(Weather.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    // end of getWeather()

}