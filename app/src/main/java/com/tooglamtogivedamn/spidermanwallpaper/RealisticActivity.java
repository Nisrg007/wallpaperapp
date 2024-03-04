package com.tooglamtogivedamn.spidermanwallpaper;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class RealisticActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realistic);

        recyclerView = findViewById(R.id.recyclerRealistic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

progressBar=findViewById(R.id.progressBar);


        // Fetch image URLs from Firestore collection or Firebase Storage as per your setup
        fetchImageUrlsFromFirebaseStorage();
    }

    private void fetchImageUrlsFromFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("realistic");

        progressBar.setVisibility(View.VISIBLE);
        // Fetch all items (images) from the "images" folder
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
                    imageAdapter = new ImageAdapter(imageList,RealisticActivity.this);
                    recyclerView.setAdapter(imageAdapter);
                } else {
                    // Handle the error
                }
            }
        });

    }
}




