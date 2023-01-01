package edu.northeastern.mobileapplicationteam18;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface WeatherApi {

//    @GET("http://api.openweathermap.org/data/2.5/weather?q=Karachi&appid=eea3ae443837e9afaabb58ba6ea17b13&units=metric")
    @GET("weather")
    Call<WeatherExample> getWeather(@Query("q") String cityname,
                             @Query("appid") String apikey);
}
