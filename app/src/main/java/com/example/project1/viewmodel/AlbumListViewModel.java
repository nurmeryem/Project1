package com.example.project1.viewmodel;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project1.model.Albums;
import com.example.project1.network.ApiInterface;
import com.example.project1.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumListViewModel extends ViewModel {

    private MutableLiveData<List<Albums>> albumsList;


    public AlbumListViewModel(){
        albumsList = new MutableLiveData<>();

    }
    public MutableLiveData<List<Albums>> getAlbumsListObserver(){
        return albumsList;
    }

    public void makeApiCall(){
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient();
        Call<List<Albums>> call = apiInterface.getAlbums();
        call.enqueue(new Callback<List<Albums>>() {
            @Override
            public void onResponse(Call<List<Albums>> call, Response<List<Albums>> response) {
                Log.e("TAG", "onResponse: makeApiCall title " + response.body().get(0).getTitle() );
                albumsList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Albums>> call, Throwable t) {
                albumsList.postValue(null);
            }
        });

    }
}
