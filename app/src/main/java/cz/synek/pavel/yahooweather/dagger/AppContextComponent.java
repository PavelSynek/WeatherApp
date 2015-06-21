package cz.synek.pavel.yahooweather.dagger;

import android.content.Context;
import android.location.LocationManager;

import cz.synek.pavel.yahooweather.WeatherApplication;
import dagger.Component;

@Component
public interface AppContextComponent {
    WeatherApplication application();
    Context applicationContext();
    LocationManager locationManager();
}
