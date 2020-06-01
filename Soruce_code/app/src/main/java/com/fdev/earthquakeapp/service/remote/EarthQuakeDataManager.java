package com.fdev.earthquakeapp.service.remote;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fdev.earthquakeapp.service.model.EarthQuake;
import com.fdev.earthquakeapp.util.EarthQuakeConstants;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeDataManager {

    private Context context;
    private MutableLiveData<List<EarthQuake>> allEarthQuakes;
    private MutableLiveData<String> cityDetail;

    public EarthQuakeDataManager(Context context){

        this.context = context;
        cityDetail = new MutableLiveData<>();
    }


    private synchronized void sendVolleyRequest(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                EarthQuakeConstants.URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<EarthQuake> tempEarth = new ArrayList<>();
                        try {
                            //Getting the JSONArray form API
                            JSONArray featureJSONArray = response.getJSONArray(EarthQuakeConstants.FEATURE);

                            for(int i=0; i<150 ; i++){
                                EarthQuake newEarthQuake = new EarthQuake();

                                JSONObject currentObject = featureJSONArray.getJSONObject(i);

                                JSONObject propertiesJSONObject = currentObject.getJSONObject("properties");

                                //Getting Place NAME
                                String placeName = propertiesJSONObject.getString("place");
                                newEarthQuake.setPlace(placeName);

                                double magnitude = 0.0;
                                //Getting Magnitude
                                if(propertiesJSONObject.getString("mag") != null) {
                                    magnitude = propertiesJSONObject.getDouble("mag");
                                }
                                newEarthQuake.setMagnitude(magnitude);

                                //Getting time
                                long time = propertiesJSONObject.getLong("time");
                                newEarthQuake.setTime(time);

                                //Getting detail Link
                                String detailLink = propertiesJSONObject.getString("detail");
                                newEarthQuake.setDetailLink(detailLink);

                                //Getting Type
                                String type = propertiesJSONObject.getString("type");
                                newEarthQuake.setType(type);

                                //Getting latlng
                                JSONArray coodinateJSONArray = currentObject.getJSONObject("geometry")
                                        .getJSONArray("coordinates");

                                double longitudinal = Double.valueOf(coodinateJSONArray.get(1).toString());
                                double lat = Double.valueOf(coodinateJSONArray.get(0).toString());
                                LatLng latLng = new LatLng(longitudinal,
                                            lat);

                                newEarthQuake.setLatLng(latLng);




                                
                                tempEarth.add(newEarthQuake);
                            }
                            allEarthQuakes.postValue(tempEarth);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }






                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        EarthQuakeRemoteSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    private synchronized void sendDetailRequest(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject propertisJSONObject = response.getJSONObject("properties");
                            JSONObject productJSONObject = propertisJSONObject.getJSONObject("products");
                            JSONArray nearbyJSONArray = productJSONObject.getJSONArray("nearby-cities");
                            JSONObject contentJSONObject = nearbyJSONArray.getJSONObject(0)
                                    .getJSONObject("contents");
                            JSONObject nearbyJsonJSONObject = contentJSONObject
                                    .getJSONObject("nearby-cities.json");

                            sendCityListRequest(nearbyJsonJSONObject.getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cityDetail.setValue("");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        EarthQuakeRemoteSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    private synchronized  void sendCityListRequest(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                for(int i=0 ; i<response.length(); i++){
                                JSONObject citiesObject = response.getJSONObject(i);

                                stringBuilder.append("City : " + citiesObject.getString("name")
                                    + "\n" + "Distance: " + citiesObject.getString("distance")
                                    + "\n" + "Direction: " + citiesObject.getString("direction")
                                );
                                    stringBuilder.append("\n\n");
                                }
                                cityDetail.setValue(stringBuilder.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        EarthQuakeRemoteSingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    public MutableLiveData<List<EarthQuake>> getAllEarthQuakes(){
        if(allEarthQuakes == null){
            allEarthQuakes = new MutableLiveData<>();
            allEarthQuakes.postValue(new ArrayList<EarthQuake>());
        }
        if(allEarthQuakes.getValue() != null){
            allEarthQuakes.getValue().clear();
        }
        sendVolleyRequest();
        return allEarthQuakes;
    }

    public void setCityDetail(String url){
        sendDetailRequest(url);
    }

    public MutableLiveData<String> getCityDetail(){
        return this.cityDetail;
    }
}
