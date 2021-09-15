package com.cookandroid.rsample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        imageView = findViewById(R.id.imageView);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static File imageFile;
    static Uri fileUri;

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            try {
                imageFile = createFile();
                fileUri = FileProvider.getUriForFile(this, "com.cookandroid.rsample", imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch(IOException e){
                e.printStackTrace();
            };
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras(); // Bundle 객체가 Intent 사이에 정보를 주고받을 수 있는 하나의 방법
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8; // 1/8로 축소한 이미지
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private File createFile() throws IOException {
        return File.createTempFile(
                simpleDateFormat.format(new Date()),
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
    }
}