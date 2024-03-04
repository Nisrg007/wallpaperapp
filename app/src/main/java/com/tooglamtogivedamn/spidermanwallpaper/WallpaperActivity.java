package com.tooglamtogivedamn.spidermanwallpaper;

import static android.content.ContentValues.TAG;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooglamtogivedamn.spidermanwallpaper.databinding.ActivityWallpaperBinding;

public class WallpaperActivity extends AppCompatActivity {
    ActivityWallpaperBinding binding;
    private InterstitialAd mInterstitialAd;
    int count=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        //ad load...
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        loadAd();
        // Receive the image URL from the intent extra
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the full-size image and display it in full size
        ImageView imageView = findViewById(R.id.iv_wpaper);
        FloatingActionButton floatingActionButton=findViewById(R.id.setAw);
        FloatingActionButton downloadButton=findViewById(R.id.download);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the bitmap as wallpaper
                        imageView.setImageBitmap(resource);

                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                   count++;
                                if(count<2){
                                    showWallpaperDialog(resource);
                                }else{
                                    if (mInterstitialAd !=null){
                                        mInterstitialAd.show(WallpaperActivity.this);
                                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                            @Override
                                            public void onAdDismissedFullScreenContent() {
                                                super.onAdDismissedFullScreenContent();
                                                mInterstitialAd=null;
                                                count=0;
                                                showWallpaperDialog(resource);
                                                loadAd();
                                            }

                                            @Override
                                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                                super.onAdFailedToShowFullScreenContent(adError);
                                                mInterstitialAd=null;

                                            }
                                        });
                                    }else{

                                        showWallpaperDialog(resource);
                                    }
                                }


                            }
                        });

                        //click btn download
                        downloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        // Initialize the DownloadManager
                                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                                        // Create a Uri from the image URL
                                        Uri imageUri = Uri.parse(imageUrl);

                                        // Create a request to download the image
                                        DownloadManager.Request request = new DownloadManager.Request(imageUri);

                                        // Set download attributes
                                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                                .setAllowedOverRoaming(false)
                                                .setTitle("Wallpaper_Downloaded")
                                                .setMimeType("image/jpeg")
                                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Spiderman.jpg");

                                        // Enqueue the download request
                                        downloadManager.enqueue(request);
                                        Toast.makeText(WallpaperActivity.this, "Wallpaper Downloaded successfully", Toast.LENGTH_SHORT).show();

                                    } else {
                                        // Show a message if the image URL is not available
                                        Toast.makeText(WallpaperActivity.this, "Try again later", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                }
                        });
                    }


                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle if the loading is cleared
                    }
                });
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }


    private void showWallpaperDialog(Bitmap bitmap) {
        WallpaperDialogFragment dialogFragment = new WallpaperDialogFragment(bitmap);
        dialogFragment.show(getSupportFragmentManager(), "wallpaper_dialog");
    }


    }
