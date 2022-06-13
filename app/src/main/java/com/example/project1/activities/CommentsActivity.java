package com.example.project1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project1.R;
import com.example.project1.adapter.CommentsAdapter;
import com.example.project1.adapter.PostsAdapter;
import com.example.project1.model.Comments;
import com.example.project1.model.Posts;
import com.example.project1.network.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    CommentsAdapter adapter;
    Comments comments;
    int posi;

    List<Comments> commentsList = new ArrayList<>();
    List<Comments> commentsOfPostsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProjeTheme);
        setContentView(R.layout.activity_comments);

        init();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                Log.e("TAG", " extras is null " );
            } else {
                List<Posts> postsList = new ArrayList<Posts>();
                postsList = (ArrayList<Posts>) getIntent().getSerializableExtra("postsList");
                posi = getIntent().getIntExtra("position",0);

                fetchComments(postsList.get(posi).getId());

            }
        } else {
            List<Posts> postsList = new ArrayList<Posts>();
            postsList = (ArrayList<Posts>) getIntent().getSerializableExtra("postsList");
        }


    }

    public void fetchComments(int post_id) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getRetrofitClient().getComments().enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentsList.clear();
                    commentsList.addAll(response.body());

                    if (commentsList.size() > 0) {
                        for (int i = 0; i < commentsList.size(); i++) {
                            if (commentsList.get(i).getPostId() == post_id) {
                                commentsOfPostsList.add(commentsList.get(i));
                            }
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CommentsActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void init() {
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new CommentsAdapter(commentsOfPostsList);

        recyclerView.setAdapter(adapter);

        comments = new Comments();


    }
}