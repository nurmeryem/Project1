package com.example.project1.model;

import java.io.Serializable;

public class Posts implements Serializable {

    private String title;
    private String body;
    private  int id;

    private int postPosition;

    public Posts(){
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostPosition() {
        return postPosition;
    }

    public void setPostPosition(int postPosition) {
        this.postPosition = postPosition;
    }


}
