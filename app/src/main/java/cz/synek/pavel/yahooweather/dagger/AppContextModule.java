package cz.synek.pavel.yahooweather.dagger;

import android.content.Context;
import android.location.LocationManager;

import cz.synek.pavel.yahooweather.WeatherApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {
    private final WeatherApplication application;

    public AppContextModule(WeatherApplication application) {
        this.application = application;
    }

    @Provides
    public WeatherApplication application() {
        return this.application;
    }

    @Provides
    public Context applicationContext() {
        return this.application;
    }

    @Provides
    public LocationManager locationService(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
