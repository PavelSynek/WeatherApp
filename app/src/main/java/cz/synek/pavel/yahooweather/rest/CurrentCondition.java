package cz.synek.pavel.yahooweather.rest;

import com.google.gson.annotations.SerializedName;

public class CurrentCondition {

    @SerializedName("temp_C")
    private int tempC;

    @SerializedName("temp_F")
    private int tempF;

    private int windspeedKmph;

    private int windspeedMiles;

    public int getTempC() {
        return tempC;
    }

    public int getTempF() {
        return tempF;
    }

    public int getWindspeedKmph() {
        return windspeedKmph;
    }

    public int getWindspeedMiles() {
        return windspeedMiles;
    }
}
