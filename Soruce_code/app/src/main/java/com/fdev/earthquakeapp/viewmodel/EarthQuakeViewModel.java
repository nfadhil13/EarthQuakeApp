package com.fdev.earthquakeapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fdev.earthquakeapp.service.model.EarthQuake;
import com.fdev.earthquakeapp.service.repository.EarthQuakeRepository;

import java.util.List;

public class EarthQuakeViewModel extends AndroidViewModel {

    private EarthQuakeRepository earthQuakeRepository;
    private MutableLiveData<List<EarthQuake>> allEarthQuakes;
    private MutableLiveData<String> cityDetail;

    public EarthQuakeViewModel(@NonNull Application application) {
        super(application);
        earthQuakeRepository = new EarthQuakeRepository(application);
        allEarthQuakes = earthQuakeRepository.getAllEarthQuakes();
        cityDetail = earthQuakeRepository.getCurrentDetailUrl();
    }

    public LiveData<List<EarthQuake>> getAllEarthQuakes(){
        return allEarthQuakes;
    }

    public MutableLiveData<String> getCityDetail() { return cityDetail;}

    public void setCityDetail(String url) { earthQuakeRepository.setCityDetail(url);}


}
