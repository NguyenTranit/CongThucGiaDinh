package com.nhatnguyen.congthucgiadinh.model;

import java.util.Comparator;

public class CategoryModel {
    public  String name,image;
    String DocumentId;

    public CategoryModel() {
    }


    public CategoryModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
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
}
