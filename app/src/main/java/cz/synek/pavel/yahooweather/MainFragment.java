package cz.synek.pavel.yahooweather;


import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cz.synek.pavel.yahooweather.utils.C;
import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class MainFragment extends BaseFragment {

    @InjectView(R.id.city)
    TextView cityText;

    @InjectView(R.id.temperatureText)
    TextView temperatureText;

    @InjectView(R.id.windText)
    TextView windText;

    @InjectView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @InjectView(R.id.weatherContainer)
    LinearLayout weatherContainer;

    @InjectView(R.id.unitSwitch)
    SwitchCompat unitSwitch;

    @InjectView(R.id.error)
    TextView errorText;

    private WeatherData currentWeatherData;

    private RxLoader<WeatherData> weatherLoader;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RxLoaderManager loaderManager = RxLoaderManagerCompat.get(this);

        WeatherService weatherService = new WeatherService(getActivity());

        weatherLoader = loaderManager.create(
                weatherService.getWeatherData(),
                new RxLoaderObserver<WeatherData>() {
                    @Override
                    public void onStarted() {
                        progressWheel.setVisibility(View.VISIBLE);
                        weatherContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        progressWheel.setVisibility(View.GONE);
                        weatherContainer.setVisibility(View.VISIBLE);
                        currentWeatherData = weatherData;
                        updateUI();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressWheel.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        Timber.e(throwable.getMessage());
                    }
                });

        weatherLoader.restart();
    }

    private void updateUI() {
        if (unitSwitch.isChecked()) {
            // US units
            windText.setText(C.US_WIND(currentWeatherData.getWindspeedMiles()));
            temperatureText.setText(C.US_TEMP(currentWeatherData.getTempF()));
        } else {
            // metric units
            windText.setText(C.METRIC_WIND(currentWeatherData.getWindspeedKmph()));
            temperatureText.setText(C.METRIC_TEMP(currentWeatherData.getTempC()));
        }
        cityText.setText(currentWeatherData.getCity());
    }

    @OnCheckedChanged(R.id.unitSwitch)
    void unitChanged() {
        updateUI();
    }

    @OnClick(R.id.error)
    void errorClicked() {
        errorText.setVisibility(View.GONE);
        weatherLoader.restart();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_main;
    }
}
