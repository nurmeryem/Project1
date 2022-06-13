package com.example.project1.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project1.model.Photos;
import com.example.project1.network.ApiInterface;
import com.example.project1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoListViewModel extends ViewModel {

    private MutableLiveData<List<Photos>> photosList;

    public PhotoListViewModel(){
        photosList = new MutableLiveData<>();

    }
    public MutableLiveData<List<Photos>> getPhotosListObserver(){
        return photosList;
    }

    public void makeApiCall(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient();
        Call<List<Photos>> call = apiInterface.getPhotos();
        call.enqueue(new Callback<List<Photos>>() {
            @Override
            public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                photosList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photos>> call, Throwable t) {
                photosList.postValue(null);
            }
        });

    }
}
