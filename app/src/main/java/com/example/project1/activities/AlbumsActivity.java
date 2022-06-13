package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project1.R;
import com.example.project1.adapter.ALbumsAdapter;
import com.example.project1.model.Albums;
import com.example.project1.model.Posts;
import com.example.project1.viewmodel.AlbumListViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlbumsActivity extends AppCompatActivity implements ALbumsAdapter.OnAlbumListener {

    private List<Albums> albumsModelList;
    ALbumsAdapter adapter;
    AlbumListViewModel viewModel;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProjeTheme);
        setContentView(R.layout.activity_albums);

        init();
        if(checkInternet()){
            fillList();
        }else{
            showSweetDialog();
        }


    }

    public void fillList(){
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new GridLayoutManager(AlbumsActivity.this, 2);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ALbumsAdapter(this, albumsModelList, this);
        recyclerView.setAdapter(adapter);


        viewModel = ViewModelProviders.of(this).get(AlbumListViewModel.class);
        viewModel.getAlbumsListObserver().observe(this, albums -> {
            if (albums != null) {

                albumsModelList = albums;
                Log.e("TAG", "onChanged: " + albumsModelList.get(0).getTitle());
                adapter.setAlbumsList(albums);
                progressBar.setVisibility(View.GONE);
            } else {
                Log.e("TAG", "Album is null ");
            }
        });
        viewModel.makeApiCall();
    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);
    }
    @Override
    public void onAlbumClick(int position) {
        Log.e("TAG", "onAlbumClick: --- " + position);
        Log.e("TAG", "onAlbumClick: title: --- " + albumsModelList.get(position).getTitle());

        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putExtra ("albumList", (ArrayList<Albums>) albumsModelList);
        intent.putExtra("position", position); // tıklanan album id

        startActivity(intent);


    }
    public void showSweetDialog(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AlbumsActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Uyarı");
        sweetAlertDialog.setContentText("Lütfen internet bağlantınızı kontrol ediniz.");
        sweetAlertDialog.setConfirmText("Tamam");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                btnTryAgain.setVisibility(View.VISIBLE);
                btnTryAgain.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        tryAgain();
                    }
                });
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
    public void tryAgain(){
        if(checkInternet()){
            fillList();
        }else {
            showSweetDialog();
        }
    }

}