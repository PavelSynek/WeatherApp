package cz.synek.pavel.yahooweather;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import cz.synek.pavel.yahooweather.rest.CurrentCondition;
import cz.synek.pavel.yahooweather.rest.WeatherAPIService;
import cz.synek.pavel.yahooweather.utils.C;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

public class WeatherService {

    private ReactiveLocationProvider locationProvider;

    private final LocationRequest locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setSmallestDisplacement(C.UPDATE_RADIUS)
            .setInterval(C.UPDATE_INTERVAL);

    public WeatherService(Context context) {
        locationProvider = new ReactiveLocationProvider(context.getApplicationContext());
    }

    public Observable<WeatherData> getWeatherData() {
        return locationProvider.getUpdatedLocation(locationRequest)
                .flatMap(location -> Observable.zip(
                        getCityName(location),
                        getCurrentCondition(location),
                        WeatherData::new));
    }

    private Observable<String> getCityName(Location location) {
        return locationProvider.getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 1)
                .map(addresses -> {
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        if (address.getLocality() != null) {
                            return address.getLocality();
                        } else if (address.getSubAdminArea() != null) {
                            return address.getSubAdminArea();
                        } else if (address.getMaxAddressLineIndex() > 0) {
                            return address.getAddressLine(1);
                        } else {
                            throw new RuntimeException("Invalid city format");
                        }
                    } else {
                        throw new RuntimeException("No address received");
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private Observable<CurrentCondition> getCurrentCondition(Location location) {
        return WeatherAPIService.WEATHER_API.getWeather(location.getLatitude() + ", " + location.getLongitude())
                .flatMap(weatherForecast -> {
                    if (weatherForecast.getData().getCurrentCondition().isEmpty()) {
                        throw new RuntimeException("No weather data");
                    } else {
                        return Observable.just(weatherForecast.getData().getCurrentCondition().get(0));
                    }
                });
    }
}
