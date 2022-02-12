package com.mohit.iprogrammertest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mohit.iprogrammertest.R;
import com.mohit.iprogrammertest.adapter.CompareTableAdapter;
import com.mohit.iprogrammertest.adapter.ImageAdapter;
import com.mohit.iprogrammertest.manager.DatabaseInstance;
import com.mohit.iprogrammertest.manager.GetDataService;
import com.mohit.iprogrammertest.model.ImageModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {
    public static final String  TAG = "MainActivity";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    List<ImageModel> imagesFromDatabase;
    DatabaseInstance databaseInstance;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseInstance = new DatabaseInstance(MainActivity.this);
        imagesFromDatabase = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        relativeLayout = findViewById(R.id.parent_layout);
        listView = findViewById(R.id.list_comparison_table);
        imagesFromDatabase = databaseInstance.getAllImages();
        apiCallForGetImages();
    }


    void apiCallForGetImages(){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        GetDataService service = retrofit.create(GetDataService.class);
        Call<List<ImageModel>> call = service.getPhotos();
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                try {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                        List<ImageModel> imageModels = response.body();

                        fillDataInRecyclerView(imageModels);
                    }
                    else{
                        showSnacksWithRelativeLayout(MainActivity.this, "Data Not Found!!", relativeLayout);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void showSnacksWithRelativeLayout(Context context, String message,
                                             RelativeLayout layoutParent) {
        Snackbar snackbar = Snackbar
                .make(layoutParent, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
        params1.gravity = Gravity.BOTTOM;
        view.setLayoutParams(params1);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.red_alert));
        snackbar.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fillDataInRecyclerView(List<ImageModel> imageModels){
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, imageModels, imagesFromDatabase, listView);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        /*imagesFromDatabase = imageAdapter.getSavedImagesFromRecycler();
        getSavedImages();*/

    }


}