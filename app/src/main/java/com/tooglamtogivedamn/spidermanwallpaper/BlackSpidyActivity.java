package com.tooglamtogivedamn.spidermanwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BlackSpidyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_spidy);
        recyclerView = findViewById(R.id.recyclerBlackSpidy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        progressBar=findViewById(R.id.progressBar);

        fetchImageUrlsFromFirebaseStorage();

    }

    private void fetchImageUrlsFromFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("blackspidy");
        progressBar.setVisibility(View.VISIBLE);

        imagesRef.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    List<ImageItem> imageList = new ArrayList<>();
                    for (StorageReference item : task.getResult().getItems()) {
                        // Get the download URL for each image
                        item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> uriTask) {
                                if (uriTask.isSuccessful()) {
                                    Uri downloadUri = uriTask.getResult();
                                    if (downloadUri != null) {
                                        imageList.add(new ImageItem(downloadUri.toString()));
                                        imageAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    // Handle the error
                                }
                            }
                        });
                    }
                    imageAdapter = new ImageAdapter(imageList,BlackSpidyActivity.this);
                    recyclerView.setAdapter(imageAdapter);
                } else {
                    // Handle the error
                }
            }
        });


    }
}