package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project1.R;
import com.example.project1.adapter.ALbumsAdapter;
import com.example.project1.adapter.CommentsAdapter;
import com.example.project1.adapter.PhotosAdapter;
import com.example.project1.model.Albums;
import com.example.project1.model.Comments;
import com.example.project1.model.Photos;
import com.example.project1.model.Posts;
import com.example.project1.network.RetrofitClient;
import com.example.project1.viewmodel.AlbumListViewModel;
import com.example.project1.viewmodel.PhotoListViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosActivity extends AppCompatActivity implements PhotosAdapter.OnPhotoListener {

    int posi;
    List<Photos> photosOfAlbumsList = new ArrayList<>();
    public List<Photos> photosOfAlbumsListGlobal = new ArrayList<>();
    private List<Photos> photosModelList;
    List<Albums> albumList = new ArrayList<Albums>();

    List<Photos> photosList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    PhotosAdapter adapter;
    PhotoListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProjeTheme);
        setContentView(R.layout.activity_photos);
        init();
        if (checkInternet()) {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();

                if (extras == null) {
                    Log.e("TAG", " extras is null ");
                } else {
                    albumList = (ArrayList<Albums>) getIntent().getSerializableExtra("albumList");

                    posi = getIntent().getIntExtra("position", 0);

                    Log.e("TAG", "position: " + posi);


                    getPhotos(albumList.get(posi).getId());
                }
            } else {
                List<Albums> albumList = new ArrayList<Albums>();
                albumList = (ArrayList<Albums>) getIntent().getSerializableExtra("albumList");
            }
        } else {
            showSweetDialog();
        }

    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerViewPhotos);
    }

    public void fetchPhotos() {
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(PhotosActivity.this, 2);


        if (photosOfAlbumsListGlobal.size() > 0) {
            Log.e("TAG", "photosOfAlbumsListGlobal size: " + photosOfAlbumsListGlobal.size());


        } else {
            Log.e("TAG", "photosOfAlbumsListGlobal size :  0");
        }

        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotosAdapter(this, photosList, this);
        recyclerView.setAdapter(adapter);


        viewModel = ViewModelProviders.of(this).get(PhotoListViewModel.class);
        viewModel.getPhotosListObserver().observe(this, albums -> {
            if (albums != null) {
                photosModelList = photosOfAlbumsListGlobal;
                adapter.setAlbumsList(photosModelList);
                progressBar.setVisibility(View.GONE);

            } else {
                Log.e("TAG", "Album is null ");
            }
        });

        viewModel.makeApiCall();

    }

    public void getPhotos(int album_id) {
        RetrofitClient.getRetrofitClient().getPhotos().enqueue(new Callback<List<Photos>>() {
            @Override
            public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                Log.e("TAG", "onResponse: album_id " + album_id);
                if (response.isSuccessful() && response.body() != null) {
                    photosList.clear();
                    photosList.addAll(response.body());

                    if (photosList.size() > 0) {
                        for (int i = 0; i < photosList.size(); i++) {
                            if (photosList.get(i).getAlbumId() == album_id) {
                                photosOfAlbumsList.add(photosList.get(i));
                            }
                        }
                    }

                    photosOfAlbumsListGlobal.clear();

                    photosOfAlbumsListGlobal.addAll(photosOfAlbumsList);
                    Log.e("TAG", "onResponse: photosOfAlbumsListGlobal: " + photosOfAlbumsListGlobal.size());

                    for (int j = 0; j < photosOfAlbumsListGlobal.size(); j++) {
                        Log.e("TAG", "photosOfAlbumsListGlobal element: " + photosOfAlbumsListGlobal.get(j).getTitle());
                    }

                    fetchPhotos();

                } else {
                    showSweetDialogError();
                }
            }


            @Override
            public void onFailure(Call<List<Photos>> call, Throwable t) {
                Toast.makeText(PhotosActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPhotosClick(int position) {
        Log.e("TAG", "onfotoClick: --- " + position);
    }

    public void showSweetDialogError() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PhotosActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Uyarı");
        sweetAlertDialog.setContentText("Bir hata oluştu.");
        sweetAlertDialog.setConfirmText("Tamam");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public void showSweetDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PhotosActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Uyarı");
        sweetAlertDialog.setContentText("Lütfen internet bağlantınızı kontrol ediniz.");
        sweetAlertDialog.setConfirmText("Tamam");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();
    }

    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;
    }

}