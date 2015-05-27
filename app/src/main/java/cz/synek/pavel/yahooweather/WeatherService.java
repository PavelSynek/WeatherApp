package cz.synek.pavel.yahooweather;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.util.List;

import cz.synek.pavel.yahooweather.rest.CurrentCondition;
import cz.synek.pavel.yahooweather.rest.WeatherAPIService;
import cz.synek.pavel.yahooweather.rest.WeatherForecast;
import cz.synek.pavel.yahooweather.utils.C;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class WeatherService {

    private ReactiveLocationProvider locationProvider;

    private final LocationRequest locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setSmallestDisplacement(C.UPDATE_RADIUS)
            .setInterval(C.UPDATE_INTERVAL_DEBUG);

    public WeatherService(Context context) {
        locationProvider = new ReactiveLocationProvider(context.getApplicationContext());
    }

    public Observable<WeatherData> getWeatherData() {
        return locationProvider.getUpdatedLocation(locationRequest)
                .flatMap(new Func1<Location, Observable<WeatherData>>() {
                    @Override
                    public Observable<WeatherData> call(Location location) {
                        return Observable.zip(
                                getCityName(location),
                                getCurrentCondition(location),
                                new Func2<String, CurrentCondition, WeatherData>() {
                                    @Override
                                    public WeatherData call(String city, CurrentCondition currentCondition) {
                                        return new WeatherData(city, currentCondition);
                                    }
                                });
                    }
                });
    }

    private Observable<String> getCityName(Location location) {
        return locationProvider.getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 1)
                .map(new Func1<List<Address>, String>() {
                    @Override
                    public String call(List<Address> addresses) {
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
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private Observable<CurrentCondition> getCurrentCondition(Location location) {
        return WeatherAPIService.WEATHER_API.getWeather(location.getLatitude() + ", " + location.getLongitude())
                .flatMap(new Func1<WeatherForecast, Observable<CurrentCondition>>() {
                    @Override
                    public Observable<CurrentCondition> call(WeatherForecast weatherForecast) {
                        if (weatherForecast.getData().getCurrentCondition().isEmpty()) {
                            throw new RuntimeException("No weather data");
                        } else {
                            return Observable.just(weatherForecast.getData().getCurrentCondition().get(0));
                        }
                    }
                });
    }
}
