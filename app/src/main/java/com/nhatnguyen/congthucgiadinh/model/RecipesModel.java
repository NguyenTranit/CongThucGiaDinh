package com.nhatnguyen.congthucgiadinh.model;

import java.io.Serializable;
import java.util.Comparator;

public class RecipesModel implements Serializable {


    public String  name, ingredient, youtube,describe,describe2,describe3,describe4,describe5,describe6,
    image, imgStep1,imgStep2,imgStep3,imgStep4,imgStep5,imgStep6,timer,search;
    public String documentId;
    public RecipesModel() {
    }


    public RecipesModel(String name, String ingredient, String youtube, String describe, String describe2, String describe3, String describe4, String describe5, String describe6, String image, String imgStep1, String imgStep2, String imgStep3, String imgStep4, String imgStep5, String imgStep6, String timer, String search) {
        this.name = name;
        this.ingredient = ingredient;
        this.youtube = youtube;
        this.describe = describe;
        this.describe2 = describe2;
        this.describe3 = describe3;
        this.describe4 = describe4;
        this.describe5 = describe5;
        this.describe6 = describe6;
        this.image = image;
        this.imgStep1 = imgStep1;
        this.imgStep2 = imgStep2;
        this.imgStep3 = imgStep3;
        this.imgStep4 = imgStep4;
        this.imgStep5 = imgStep5;
        this.imgStep6 = imgStep6;
        this.timer = timer;
        this.search = search;
    }
    public static final Comparator<RecipesModel> BY_NAME_ALPHABETICAL=new Comparator<RecipesModel>() {
        @Override
        public int compare(RecipesModel o1, RecipesModel o2) {
            return o1.name.compareTo(o2.name);
        }
    };

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe2() {
        return describe2;
    }

    public void setDescribe2(String describe2) {
        this.describe2 = describe2;
    }

    public String getDescribe3() {
        return describe3;
    }

    public void setDescribe3(String describe3) {
        this.describe3 = describe3;
    }

    public String getDescribe4() {
        return describe4;
    }

    public void setDescribe4(String describe4) {
        this.describe4 = describe4;
    }

    public String getDescribe5() {
        return describe5;
    }

    public void setDescribe5(String describe5) {
        this.describe5 = describe5;
    }

    public String getDescribe6() {
        return describe6;
    }

    public void setDescribe6(String describe6) {
        this.describe6 = describe6;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImgStep1() {
        return imgStep1;
    }

    public void setImgStep1(String imgStep1) {
        this.imgStep1 = imgStep1;
    }

    public String getImgStep2() {
        return imgStep2;
    }

    public void setImgStep2(String imgStep2) {
        this.imgStep2 = imgStep2;
    }

    public String getImgStep3() {
        return imgStep3;
    }

    public void setImgStep3(String imgStep3) {
        this.imgStep3 = imgStep3;
    }

    public String getImgStep4() {
        return imgStep4;
    }

    public void setImgStep4(String imgStep4) {
        this.imgStep4 = imgStep4;
    }

    public String getImgStep5() {
        return imgStep5;
    }

    public void setImgStep5(String imgStep5) {
        this.imgStep5 = imgStep5;
    }

    public String getImgStep6() {
        return imgStep6;
    }

    public void setImgStep6(String imgStep6) {
        this.imgStep6 = imgStep6;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
