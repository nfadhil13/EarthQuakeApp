package com.fdev.earthquakeapp.service.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.fdev.earthquakeapp.service.model.EarthQuake;
import com.fdev.earthquakeapp.service.remote.EarthQuakeDataManager;

import java.util.List;

public class EarthQuakeRepository {

    private MutableLiveData<List<EarthQuake>> allEarthQuakes;
    private MutableLiveData<String> cityDetail;
    private EarthQuakeDataManager earthQuakeDataManager;

    public EarthQuakeRepository(Application application){
        earthQuakeDataManager = new EarthQuakeDataManager(application);
        allEarthQuakes = earthQuakeDataManager.getAllEarthQuakes();
        cityDetail = earthQuakeDataManager.getCityDetail();
    }

    public MutableLiveData<List<EarthQuake>> getAllEarthQuakes(){
        return allEarthQuakes;
    }

    public MutableLiveData<String> getCurrentDetailUrl(){
        return cityDetail;
    }

    public void setCityDetail(String url){
        earthQuakeDataManager.setCityDetail(url);
    }


}
