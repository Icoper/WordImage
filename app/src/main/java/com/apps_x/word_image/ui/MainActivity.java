package com.apps_x.word_image.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps_x.word_image.R;
import com.apps_x.word_image.model.ImageModel;
import com.apps_x.word_image.model.RealmImageModel;
import com.apps_x.word_image.model.ResponseBodyModel;
import com.apps_x.word_image.server.Server;
import com.apps_x.word_image.ui.list.ImgListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final String LOG_TAG = "MainActivity";
    private Realm realm;
    // View
    private EditText enteredTitleET;
    private LinearLayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private ProgressBar searchProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /**
     * Use once on onCreate method
     * It is necessary to initialize all view the elements on MainActivity.class
     */
    private void initialize() {
        // initialise Realm database
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        enteredTitleET = (EditText) findViewById(R.id.ma_image_title_et);
        mRecyclerView = (RecyclerView) findViewById(R.id.ma_history_list);
        searchProgress = (ProgressBar) findViewById(R.id.ma_searchProgress);
        enteredTitleET.setOnEditorActionListener(this);

        setupArrayAdapter();

    }

    /**
     * Initialize or setup RecyclerView
     */
    private void setupArrayAdapter() {

        if (mRecyclerView == null || layoutManager == null) {
            layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        mRecyclerView.setAdapter(new ImgListAdapter(realm.where(RealmImageModel.class).findAllAsync()));

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // We process the click of a search button
            if (enteredTitleET.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.msg_empty_edit_text), Toast.LENGTH_LONG).show();
            } else {
                // make the search EditText inactive and show progress bar
                enteredTitleET.setEnabled(false);
                searchProgress.setVisibility(View.VISIBLE);
                // start search image from our title
                searchImage(enteredTitleET.getText().toString());
            }
            return true;
        }


        return false;
    }

    /**
     * We use to search for pictures by title
     * @param title entered user title
     */
    private void searchImage(final String title) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging); // <-- this is the important line!

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL) // Server usl
                .addConverterFactory(GsonConverterFactory.create(gson)) // We'll say that it's necessary to use GSON for serialization
                .client(httpClient.build())
                .build();


        Server service = retrofit.create(Server.class);
        Call<ResponseBodyModel> call = service.getImage(
                getString(R.string.api_key),
                "best_match",
                title);

        call.enqueue(new Callback<ResponseBodyModel>() {
            @Override
            public void onResponse(Call<ResponseBodyModel> call, Response<ResponseBodyModel> response) {
                // make the search EditText active and stop show progress bar
                enteredTitleET.setEnabled(true);
                searchProgress.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    // the query succeeded, the server returned Status 200
                    String url = "";
                    try {
                        //We receive server data, process it and store it
                        List<ImageModel> images = new ArrayList<>();
                        images = response.body().getImages();
                        List<ImageModel.DisplaySizes> displaySizes = images.get(0).getDisplaySizes();
                        // get just first img on returned list
                        url = displaySizes.get(0).getUri();

                        saveNewImage(title, url);

                    } catch (IndexOutOfBoundsException e) {
                        // Image not found
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_not_found_img), Toast.LENGTH_SHORT).show();
                    }


                    Log.d(LOG_TAG, "getImage " + String.valueOf(response.code()) + "\n" + url);

                } else {
                    // server returned an error
                    Log.d(LOG_TAG, "getImage " + String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(Call<ResponseBodyModel> call, Throwable t) {
                // error at runtime
                Log.d(LOG_TAG, "getImage fail " + t.getMessage());
            }

        });

    }

    /**
     * Save image on Realm DB
     * @param title entered user title
     * @param url url from load image
     */
    private void saveNewImage(String title, String url) {
        realm.beginTransaction();
        RealmImageModel imageModel = realm.createObject(RealmImageModel.class);

        imageModel.setTitle(title);
        imageModel.setUrl(url);
        realm.commitTransaction();
        setupArrayAdapter();
    }

    @Override

    public void onDestroy() {

        super.onDestroy();

        realm.close();

    }

}
