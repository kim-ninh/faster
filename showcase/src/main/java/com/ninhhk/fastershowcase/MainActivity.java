package com.ninhhk.fastershowcase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ninhhk.faster.Callback;
import com.ninhhk.faster.Faster;
import com.ninhhk.faster.Request;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String INTENT_IMAGE_TYPE = "image/*";
    private static final int REQUEST_CODE = 1234;

    private ImageView imageView;

    private TextView bitmapSize;

    private Spinner spinnerSourceType;
    private TextView textView;
    private Spinner spinnerLinks;

    private Spinner spinnerImgSize;

    private Spinner spinnerScale;

    private Button buttonReset;
    private Button buttonClearCache;
    private Button buttonLoad;


    private String[] sourceType;
    private String[] links;
    private String[] imageSizes;
    private String[] scaleType;

    private Request.Builder requestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        setUpView();
        bindData();

        spinnerSourceType.setOnItemSelectedListener(this);
        textView.setOnClickListener(this);
        spinnerLinks.setOnItemSelectedListener(this);
        spinnerImgSize.setOnItemSelectedListener(this);
        spinnerScale.setOnItemSelectedListener(this);

        buttonReset.setOnClickListener(this);
        buttonClearCache.setOnClickListener(this);
        buttonLoad.setOnClickListener(this);

        requestBuilder = Faster.init(MainActivity.this);

    }

    private void prepareData() {
        sourceType = getResources().getStringArray(R.array.source_type);
        links = getResources().getStringArray(R.array.image_link);
        imageSizes = getResources().getStringArray(R.array.image_sizes);
        scaleType = getResources().getStringArray(R.array.scale_type);
    }

    private void bindData() {
        spinnerSourceType.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, sourceType));

        spinnerLinks.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, links));

        spinnerImgSize.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, imageSizes));

        spinnerScale.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, scaleType));
    }

    private void setUpView() {
        imageView = findViewById(R.id.imageView);

        bitmapSize = findViewById(R.id.bitmapSize);

        spinnerSourceType = findViewById(R.id.spinnerSrcType);
        textView = findViewById(R.id.textView);
        spinnerLinks = findViewById(R.id.spinnerLink);

        spinnerImgSize = findViewById(R.id.spinnerImgSize);

        spinnerScale = findViewById(R.id.spinnerScale);

        buttonReset = findViewById(R.id.buttonReset);
        buttonClearCache = findViewById(R.id.buttonClearCache);
        buttonLoad = findViewById(R.id.buttonLoad);

    }

    private int[] parseImageSize(String str){
        int[] dimension = new int[2];

        String[] sizes = str.split(" ");

        if (sizes.length != dimension.length)
            return dimension;

        for (int i = 0; i < dimension.length; i++){
            dimension[i] = Integer.parseInt(sizes[i]);
        }

        return dimension;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerSourceType){

            if (sourceType[position].equals("Local"))
            {
                textView.setVisibility(View.VISIBLE);
                spinnerLinks.setVisibility(View.GONE);
                selectImage();

            }else if (sourceType[position].equals("Server")){
                spinnerLinks.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }

        }else if (parent == spinnerLinks){

            if (spinnerLinks.getVisibility() == View.VISIBLE){
                requestBuilder.load(links[position]);
                triggerSelectCurrent();
            }
        }else if (parent == spinnerImgSize){

            int[] dimension = parseImageSize(imageSizes[position]);
            if (dimension[0] == dimension[1]){
                requestBuilder.resize(dimension[0]);
            }else {
                requestBuilder.resize(dimension[0], dimension[1]);
            }

        }else if (parent == spinnerScale){

            ImageView.ScaleType imgScaleType = ImageView.ScaleType.FIT_CENTER;

            if (scaleType[position].equals("Center crop")){
                imgScaleType = ImageView.ScaleType.CENTER_CROP;
            }else if (scaleType[position].equals("Fit center")){
                imgScaleType = ImageView.ScaleType.FIT_CENTER;
            }else if (scaleType[position].equals("Center")){
                imgScaleType = ImageView.ScaleType.CENTER;
            }

            requestBuilder.setScaleType(imgScaleType);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType(INTENT_IMAGE_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v == buttonReset){
            imageView.setImageDrawable(null);
        }else if (v == buttonClearCache){
            Faster faster = Faster.getInstance();
            if (faster != null){
                faster.clearCache();
            }
        }else if (v == buttonLoad){
            requestBuilder.setListener(new Callback<Bitmap>() {
                @Override
                public void onReady(Bitmap data) {
                    int w = data.getWidth();
                    int h = data.getHeight();

                    String text = "Width: " + w + " Height: " + h;
                    bitmapSize.setText(text);
                }
            }).into(imageView);

            int srcTypePos = spinnerSourceType.getSelectedItemPosition();
            int linkPos = spinnerLinks.getSelectedItemPosition();
            String txtUri = textView.getText().toString();


            if (sourceType[srcTypePos].equals("Server")){
                spinnerLinks.setSelection(linkPos);
            }else {
                requestBuilder.load(Uri.parse(txtUri));
            }
            triggerSelectCurrent();
        }else if (v == textView){
            selectImage();
        }
    }

    private void triggerSelectCurrent() {

        int imgSizePos = spinnerImgSize.getSelectedItemPosition();
        int scalePos = spinnerScale.getSelectedItemPosition();

        spinnerImgSize.setSelection(imgSizePos);
        spinnerScale.setSelection(scalePos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            Uri image = data.getData();
            textView.setText(image.toString());
            requestBuilder.load(image);
        }
    }
}
