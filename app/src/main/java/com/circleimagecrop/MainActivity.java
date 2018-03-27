package com.circleimagecrop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.security.Permission;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 4;

    private ImageView previewImageView;

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewImageView = (ImageView) findViewById(R.id.activity_main_circle_image_view);

    }


    public void launchCamera(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = new File(Environment.getExternalStorageDirectory(), "image.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }

    }

    public void launchGallery(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){
                launchCamera(null);
            }else if(requestCode == GALLERY_PERMISSION_REQUEST_CODE){
                launchGallery(null);
            }else{

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    launchCrop(Uri.fromFile(imageFile));
                }
                break;

            case GALLERY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    launchCrop(data.getData());
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    previewImageView.setImageURI(resultUri);

                }
                break;

            default:

        }
    }

    private void launchCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)    // force it to be a circle (and not oval).
                .start(this);
    }
}
