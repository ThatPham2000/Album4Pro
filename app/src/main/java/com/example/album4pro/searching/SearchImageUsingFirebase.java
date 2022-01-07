package com.example.album4pro.searching;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.album4pro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchImageUsingFirebase extends AppCompatActivity {

    private ImageView captureIV;
    private Button btnSnap, btnResults;
    private RecyclerView recyclerView;

    private SearchLVAdapter searchLVAdapter;
    private ArrayList<SearchLVModel> listSearchLVModels;

    int REQUEST_CODE = 1;
    private ProgressBar progressBar;

    private Bitmap imageBitmap;

    String title, link, displayedLink, snippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image_using_firebase);

        captureIV = (ImageView) findViewById(R.id.imageSearch);
        btnSnap = (Button) findViewById(R.id.btn_snap);
        btnResults = (Button) findViewById(R.id.btn_result);
        recyclerView = (RecyclerView) findViewById(R.id.id_recycle_view_search_results);
        progressBar = (ProgressBar)findViewById(R.id.id_pb_loading);

        listSearchLVModels = new ArrayList<>();
        searchLVAdapter = new SearchLVAdapter(this, listSearchLVModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchImageUsingFirebase.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(searchLVAdapter);

        btnResults.setEnabled(false);

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listSearchLVModels.clear();
                searchLVAdapter.notifyDataSetChanged();

                takePictureIntent();
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listSearchLVModels.clear();
                searchLVAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                getResult();
            }
        });
    }

    private void takePictureIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            captureIV.setImageBitmap(imageBitmap);

            btnResults.setEnabled(true);
        }
    }

    private void getResult(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();

        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                for (int i = 0; i < firebaseVisionImageLabels.size(); i++){
                    Log.e("KEY", firebaseVisionImageLabels.get(i).getText());
                }
                String searchQuery = "";
                if (firebaseVisionImageLabels.size() > 1){
                    searchQuery = firebaseVisionImageLabels.get(1).getText();
                }
                else  {
                    searchQuery = firebaseVisionImageLabels.get(0).getText();
                }
                getSearchQuery(searchQuery);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchImageUsingFirebase.this, R.string.search_firebase_search_fail_noti, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchQuery(String searchQuery){
        Log.e("SEARCH QUERY", searchQuery);
        String url = "https://serpapi.com/search.json?q="+ searchQuery +"&location=Vietnam&hl=en&gl=us&google_domain=google.com&api_key=86b5b501b184941799e788ef7e669d334cafd8f320855d15e78af198f0ae8658";
        RequestQueue queue = Volley.newRequestQueue(SearchImageUsingFirebase.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray organicArray = response.getJSONArray("organic_results");

                    for(int i = 0; i < organicArray.length(); i++){
                        JSONObject organicObj = organicArray.getJSONObject(i);
                        if (organicObj.has("title")){
                            title = organicObj.getString("title");
                        }
                        if (organicObj.has("link")){
                            link = organicObj.getString("link");
                        }
                        if (organicObj.has("displayed_link")){
                            displayedLink = organicObj.getString("displayed_link");
                        }
                        if (organicObj.has("snippet")){
                            snippet = organicObj.getString("snippet");
                        }

                        listSearchLVModels.add(new SearchLVModel(title,link, displayedLink, snippet));
                    }
                    searchLVAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchImageUsingFirebase.this, R.string.search_firebase_search_empty_noti, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}