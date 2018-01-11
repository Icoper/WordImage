package com.apps_x.word_image.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This model is used to receive and process data from the server.
 * In the future, the RealmImageModel.class model is used to work with images.
 *
 */

public class ResponseBodyModel {
    @SerializedName("result_count")
    String result_count;

    @SerializedName("images")
    List<ImageModel> images;

    public ResponseBodyModel(String result_count, List<ImageModel> images) {
        this.result_count = result_count;
        this.images = images;
    }

    public String getResult_count() {
        return result_count;
    }

    public void setResult_count(String result_count) {
        this.result_count = result_count;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }
}
