package com.tooglamtogivedamn.spidermanwallpaper;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class WallpaperDialogFragment extends DialogFragment {

    private Bitmap wallpaperBitmap;

    public WallpaperDialogFragment(Bitmap bitmap) {
        this.wallpaperBitmap = bitmap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show, null);
        builder.setView(view);

        Button btnSetWallpaperHome = view.findViewById(R.id.btnSetWallpaperHome);
        Button btnSetWallpaperLock = view.findViewById(R.id.btnSetWallpaperLock);

        btnSetWallpaperHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper(true);
                dismiss();
            }
        });

        btnSetWallpaperLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper(false);
                dismiss();
            }
        });

        return builder.create();
    }

    private void setWallpaper(boolean setForHomeScreen) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
        try {
            if (setForHomeScreen) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(wallpaperBitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    Toast.makeText(getContext(), "WallpaperSet successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(wallpaperBitmap, null, true, WallpaperManager.FLAG_LOCK);
                    Toast.makeText(getContext(), "WallpaperSet successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Release the reference to the bitmap to free up memory
        wallpaperBitmap = null;
    }
}
