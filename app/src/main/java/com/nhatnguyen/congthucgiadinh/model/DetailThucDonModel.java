package com.nhatnguyen.congthucgiadinh.model;

import java.io.Serializable;

public class DetailThucDonModel implements Serializable {
    public  String name,image,ingredient;

    public DetailThucDonModel() {
    }


    public DetailThucDonModel(String name, String image, String ingredient) {
        this.name = name;
        this.image = image;
        this.ingredient = ingredient;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
