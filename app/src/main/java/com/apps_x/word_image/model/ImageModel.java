package com.apps_x.word_image.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This data model is used to obtain a response for the server.
 * It supports the embedded DisplaySizes class, which contains the url of the found
 * pictures.
 *
 */
public class ImageModel {

    @SerializedName("id")
    String id;

    @SerializedName("display_sizes")
    List<DisplaySizes> displaySizes;


    public class DisplaySizes {
        @SerializedName("uri")
        String uri;

        public DisplaySizes(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public ImageModel(String id, List<DisplaySizes> displaySizes) {
        this.id = id;
        this.displaySizes = displaySizes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DisplaySizes> getDisplaySizes() {
        return displaySizes;
    }

    public void setDisplaySizes(List<DisplaySizes> displaySizes) {
        this.displaySizes = displaySizes;
    }
}
