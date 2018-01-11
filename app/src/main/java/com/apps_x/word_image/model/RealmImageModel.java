package com.apps_x.word_image.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * This data model is used to store and work with stored images
 * Used Realm database
 */

public class RealmImageModel extends RealmObject {
    // Contains the search word
    @Required
    private String title;
    // Contains url for an image
    @Required
    private String url;

    public RealmImageModel() {
    }

    public RealmImageModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
