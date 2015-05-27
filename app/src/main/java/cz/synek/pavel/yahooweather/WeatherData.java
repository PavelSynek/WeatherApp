package cz.synek.pavel.yahooweather;

import cz.synek.pavel.yahooweather.rest.CurrentCondition;

public class WeatherData {

    private int tempC;

    private int tempF;

    private int windspeedKmph;

    private int windspeedMiles;

    private String city;

    public WeatherData(String city, CurrentCondition currentCondition) {
        this.city = city;

        tempC = currentCondition.getTempC();
        tempF = currentCondition.getTempF();
        windspeedKmph = currentCondition.getWindspeedKmph();
        windspeedMiles = currentCondition.getWindspeedMiles();
    }

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

    public String getCity() {
        return city;
    }
}
