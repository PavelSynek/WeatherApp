package cz.synek.pavel.yahooweather.utils;

public class C {

    private C() {
        // no instances
    }

    public static final int UPDATE_RADIUS = 100;
    public static final int UPDATE_INTERVAL = 1000 * 60 * 30;
    public static final int UPDATE_INTERVAL_DEBUG = 1000 * 5;

    private static final String WEATHER_API_KEY = "c46c78943dbf33787691b0936c696";
    public static final String WEATHER_API_ENDPOINT = "http://api.worldweatheronline.com/";
    public static final String WEATHER_API_URL = "/free/v2/weather.ashx?fx=no&format=json&key=" + WEATHER_API_KEY;

    public static String METRIC_WIND(long speed) {
        return String.format("%d km/h", speed);
    }

    public static String METRIC_TEMP(long temp) {
        return String.format("%d°C", temp);
    }

    public static String US_WIND(long speed) {
        return String.format("%d mph", speed);
    }

    public static String US_TEMP(long temp) {
        return String.format("%d°F", temp);
    }
}
