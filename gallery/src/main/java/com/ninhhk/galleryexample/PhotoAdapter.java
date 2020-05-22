package com.ninhhk.galleryexample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ninhhk.faster.Faster;

import java.util.List;
import java.util.Random;

public class PhotoAdapter extends
        RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    private static final Random sRandom = new Random();
    private static final ColorDrawable PLACE_HOLDER_DRAWABLE
            = new ColorDrawable(Color.TRANSPARENT);

    private final OnPhotoClickListener listener;


    private List<MediaStoreData> mediaStoreData;

    PhotoAdapter(@NonNull List<MediaStoreData> mediaStoreData,
                 @Nullable OnPhotoClickListener listener) {
        this.mediaStoreData = mediaStoreData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_gallery, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri imageUri = mediaStoreData.get(position).uri;
        holder.bind(imageUri, listener);
    }

    @Override
    public int getItemCount() {
        return mediaStoreData.size();
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
        }

        void bind(Drawable drawable){
            imageView.setImageDrawable(drawable);
        }

        void bind(Uri uri, @Nullable OnPhotoClickListener listener){

            Faster.with(imageView.getContext())
                    .load(uri)
                    .placeholder(new ColorDrawable(sRandom.nextInt()))
                    .into(imageView);

            imageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(v, uri);
                }
            });
        }
    }

    interface OnPhotoClickListener{
        void onClick(@NonNull View view, @NonNull Uri uri);
    }
}
