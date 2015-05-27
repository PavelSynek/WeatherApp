package cz.synek.pavel.yahooweather;

import android.app.Application;

import timber.log.Timber;

public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
