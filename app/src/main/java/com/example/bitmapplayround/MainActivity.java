package com.example.bitmapplayround;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ninhhk.faster.Faster;

public class MainActivity extends AppCompatActivity {


    private static final String IMAGE_SOURCE_PNG = "https://dummyimage.com/600x400/000/fff.png&text=PNG+Image";
    private static final String IMAGE_SOURCE_JPG = "https://dummyimage.com/600x400/000/fff.jpg&text=JPG+Image";
    private static final String IMAGE_SOURCE_WEBP = "https://www.gstatic.com/webp/gallery/4.webp";
    private static final String IMAGE_SOURCE_HEIC = "https://nokiatech.github.io/heif/content/images/old_bridge_1440x960.heic";

    private static final int MAX_BUFFER_IN_MB = 2;
    private static final int MAX_BUFFER_IN_BYTE = MAX_BUFFER_IN_MB * 1024 * 1024;
    private static final String TAG = MainActivity.class.getName();

    private ImageView imageView;
    private Button btnLoadImage;
    private boolean isLoading = false;
    private Button btnTestResponsive;
    private EditText txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        txtUrl = findViewById(R.id.txtUrl);

        btnLoadImage = findViewById(R.id.button);
        btnLoadImage.setOnClickListener((View v) -> {
            if (!isLoading) {
                String url = txtUrl.getText().toString();
                fetchImage(url);
                isLoading = true;
            }
        });

        btnTestResponsive = findViewById(R.id.button2);
        btnTestResponsive.setOnClickListener((View v) -> {
            Toast.makeText(MainActivity.this, "This button is responsive", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fetchImage(String imageSource) {
        Faster.getInstance()
                .load(imageSource)
                .resize(50, 50)
                .setListener((bitmap) -> {
                    isLoading = false;
                })
                .into(imageView);
    }
}
