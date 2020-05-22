package com.ninhhk.galleryexample;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ninhhk.faster.Faster;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<MediaStoreData>>,
        PhotoAdapter.OnPhotoClickListener {

    private static final int REQUEST_READ_STORAGE = 0;

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<MediaStoreData> photoItems = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageView expandedImage;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.photo_recycler_view);
        expandedImage = findViewById(R.id.expanded_image);
        container = findViewById(R.id.container);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new PhotoAdapter(photoItems, this));

        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(
                new SpacesItemDecoration(spaceInPixels, 3, false));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        } else {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear_cache) {
            Faster faster = Faster.getInstance(this);
            if (faster != null) {
                faster.clearCache();
            }
        }
        return true;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoaderManager.getInstance(this).initLoader(0, null, this);
                } else {
                    Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show();
                    requestStoragePermission();
                }
            }
        }
    }


    @NonNull
    @Override
    public Loader<List<MediaStoreData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MediaStoreDataLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MediaStoreData>> loader, List<MediaStoreData> data) {
        recyclerView.setAdapter(new PhotoAdapter(data, this));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MediaStoreData>> loader) {

    }


    @Override
    public void onClick(@NonNull View view, @NonNull Uri uri) {

        ZoomThumbImageAnimation animation = new ZoomThumbImageAnimation(view, container);

        Faster.with(this)
                .load(uri)
                .setListener(data -> {
                    animation.zoomIn(expandedImage);
                    expandedImage.setOnClickListener(animation::zoomOut);
                })
                .into(expandedImage);
    }
}
