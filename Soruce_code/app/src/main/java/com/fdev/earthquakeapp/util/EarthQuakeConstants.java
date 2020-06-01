package com.fdev.earthquakeapp.util;

import java.util.Random;

public class EarthQuakeConstants {
    public static final String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    public static final String FEATURE = "features";

    public static int randomInt(int max, int min) {
        return new Random().nextInt(max - min) + min;

    }
}