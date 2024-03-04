package com.tooglamtogivedamn.spidermanwallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<ImageItem> imageList;
    private final Context context;

    public ImageAdapter(List<ImageItem> imageList,Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_show, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem imageItem = imageList.get(position);

        // Load image using Glide or your preferred image loading library
        Glide.with(holder.itemView.getContext())
                .load(imageItem.getImageUrl())
                .into(holder.imageView);
        // Handle click events for each item in the RecyclerView
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context,WallpaperActivity.class);
        intent.putExtra("imageUrl",imageItem.getImageUrl());
        context.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView homeListContainer;

        ImageViewHolder(View itemView) {
            super(itemView);
            homeListContainer=itemView.findViewById(R.id.homeListContainer);
            imageView = itemView.findViewById(R.id.ivList);
        }
    }
}

