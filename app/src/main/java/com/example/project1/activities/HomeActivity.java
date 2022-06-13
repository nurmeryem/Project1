package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project1.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    Button btnListPosts, btnListAlbums;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ProjeTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

            btnListPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkInternet()){
                        listPosts();
                    }else{
                        showSweetDialog();
                    }
                }
            });

            btnListAlbums.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkInternet()){
                        listAlbums();
                    }else{
                        showSweetDialog();
                    }

                }
            });

    }

    public void init() {
        btnListPosts = findViewById(R.id.btnListPosts);
        btnListAlbums = findViewById(R.id.btnListAlbums);
    }

    private void listPosts() {

        intent = new Intent(HomeActivity.this, PostsActivity.class);
        startActivity(intent);
    }

    private void listAlbums() {
        intent = new Intent(HomeActivity.this, AlbumsActivity.class);
        startActivity(intent);
    }

    public void showSweetDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Uyarı");
        sweetAlertDialog.setContentText("Lütfen internet bağlantınızı kontrol ediniz.");
        sweetAlertDialog.setConfirmText("Tamam");
        sweetAlertDialog.setCancelable(false);
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