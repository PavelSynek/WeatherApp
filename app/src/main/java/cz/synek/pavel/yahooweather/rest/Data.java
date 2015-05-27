package cz.synek.pavel.yahooweather.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("current_condition")
    private List<CurrentCondition> currentCondition = new ArrayList<>();

    public List<CurrentCondition> getCurrentCondition() {
        return currentCondition;
    }
}