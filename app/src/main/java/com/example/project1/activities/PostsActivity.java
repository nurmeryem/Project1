package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project1.R;
import com.example.project1.adapter.PostsAdapter;
import com.example.project1.model.Comments;
import com.example.project1.model.Posts;
import com.example.project1.network.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity implements PostsAdapter.OnNoteListener {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    Button btnTryAgain;
    PostsAdapter adapter;
    List<Posts> postsList = new ArrayList<>();

    Posts posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProjeTheme);
        setContentView(R.layout.activity_posts);
        init();
    }

    private void getSharedPref() {
        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
    }

    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        posts = new Posts();

        tryFill();

    }

    public void tryFill() {
        if (checkInternet()) {
            btnTryAgain.setVisibility(View.GONE);
            adapter = new PostsAdapter(postsList, this);
            recyclerView.setAdapter(adapter);
            fetchPosts();
        } else {
            showSweetDialog();
        }
    }

    private void tryAgain() {
        tryFill();

    }
    public void showSweetDialog(){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PostsActivity.this, SweetAlertDialog.ERROR_TYPE);
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

    private void fetchPosts() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getRetrofitClient().getPosts().enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                    Log.e("TAG", "onResponse: " + response.body().get(posts.getPostPosition()).getId());

                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PostsActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("postsList", (ArrayList<Posts>) postsList);
        intent.putExtra("position", position);

        startActivity(intent);
    }
}