package com.example.project1.network;

import com.example.project1.model.Albums;
import com.example.project1.model.Comments;
import com.example.project1.model.Photos;
import com.example.project1.model.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/posts")
    Call<List<Posts>> getPosts();

    @GET("/comments")
    Call<List<Comments>> getComments();

    @GET("/albums")
    Call<List<Albums>> getAlbums();

    @GET("/photos")
    Call<List<Photos>> getPhotos();
}
