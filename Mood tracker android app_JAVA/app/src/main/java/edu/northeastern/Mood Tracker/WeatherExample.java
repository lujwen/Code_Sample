package edu.northeastern.mobileapplicationteam18;
import com.google.gson.annotations.SerializedName;

public class WeatherExample {

    @SerializedName("main")
    WeatherDetails weatherDetails;

    public WeatherDetails getWeatherDetails() {
        return weatherDetails;
    }

    public void setWeatherDetails(WeatherDetails weatherDetails) {
        this.weatherDetails = weatherDetails;
    }
}
