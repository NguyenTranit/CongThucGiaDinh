package com.example.congthucgiadinh.model;

import java.io.Serializable;

public class ViewAllModel implements Serializable {


    public String image, name, timer, ingredient, describe, youtube;
    public String documentId;
    public ViewAllModel() {
    }


    public ViewAllModel(String image, String name, String timer, String ingredient, String describe, String youtube) {
        this.image = image;
        this.name = name;
        this.timer = timer;
        this.ingredient = ingredient;
        this.describe = describe;
        this.youtube = youtube;

    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
